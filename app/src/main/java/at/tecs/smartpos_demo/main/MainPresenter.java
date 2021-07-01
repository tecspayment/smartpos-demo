package at.tecs.smartpos_demo.main;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.spec.IvParameterSpec;

import at.tecs.ControlParser.Command;
import at.tecs.smartpos.CardControl;
import at.tecs.smartpos.PaymentService;
import at.tecs.smartpos.SmartPOSController;
import at.tecs.smartpos.connector.ConnectionListener;
import at.tecs.smartpos.connector.ResponseListener;
import at.tecs.smartpos.data.ConnectionType;
import at.tecs.smartpos.data.PrinterPrintType;
import at.tecs.smartpos.data.PrinterReturnCode;
import at.tecs.smartpos.data.RFKeyMode;
import at.tecs.smartpos.data.RFReturnCode;
import at.tecs.smartpos.data.Response;
import at.tecs.smartpos.data.Transaction;
import at.tecs.smartpos.exception.BluetoothException;
import at.tecs.smartpos.exception.TransactionFieldException;
import at.tecs.smartpos.utils.ByteUtil;
import at.tecs.smartpos_demo.Utils;
import at.tecs.smartpos_demo.data.repository.Repository;
import at.tecs.smartpos_demo.data.repository.entity.HostnameEntity;
import at.tecs.smartpos_demo.data.repository.entity.PortEntity;
import at.tecs.smartpos_demo.data.repository.entity.TerminalNumberEntity;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;
import at.tecs.smartpos_demo.utils.TDEAKey;

import static at.tecs.smartpos.data.ConnectionType.BLUETOOTH;
import static at.tecs.smartpos.data.ConnectionType.TCP;
import static at.tecs.smartpos_demo.Utils.concatenate;
import static at.tecs.smartpos_demo.Utils.createIvSpecFromZeros;
import static at.tecs.smartpos_demo.Utils.createZeros;
import static at.tecs.smartpos_demo.Utils.decrypt;
import static at.tecs.smartpos_demo.Utils.encrypt;


public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;
    private MainContract.View.ConnectionTab connectionView;
    private MainContract.View.TransactionTab transactionView;
    private MainContract.View.ResponseTab responseView;
    private MainContract.View.TemplatesTab templatesView;
    private MainContract.View.CardTab cardView;
    private MainContract.View.PrintTab printView;

    private Repository repository;

    private Timer timer;
    private Incrementer incrementer;

    private String transactionID;
    private String dateTime;

    private String TID;
    private String hostname;
    private String port;

    private Response lastResponse;

    private ArrayList<String> transactionNames = new ArrayList<>();
    private ArrayList<String> terminalNums = new ArrayList<>();
    private ArrayList<String> hostnames = new ArrayList<>();
    private ArrayList<String> ports = new ArrayList<>();

    private PaymentService paymentService;
    private final SmartPOSController smartPOSController;

    private ConnectionType connectionType;

    private BluetoothDevice bluetoothDevice;

    private BluetoothAdapter bluetoothAdapter;

    private ConnectionType selected = TCP;

    private boolean cardConnected = false;

    public static boolean bluetooth = false;


    MainPresenter() {
        paymentService = new PaymentService();
        connectionType = paymentService.getType();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter != null) {
            bluetooth = true;
        }

        smartPOSController = new SmartPOSController();
    }

    @Override
    public void takeView(MainContract.View view) {
        this.view = view;
    }

    /**
     * Starts counter.
     */
    @Override
    public void start() {
        timer.schedule(incrementer, 1000, 1000);
    }

    @Override
    public void initialize() {
        incrementer = new Incrementer();

        timer = new Timer();
        repository = new Repository(view.getContext());
    }

    /**
     * Saves transaction to database.
     * @param transaction Transaction data object.
     * @param name Name of transaction.
     */
    @Override
    public void saveTransaction(Transaction transaction, String name) {

        for(int i = 0; i < transactionNames.size(); i ++) {
            if(name.equals(transactionNames.get(i))) {
                view.showToast("Transaction with this name is already saved !");
                return;
            }
        }

        TransactionEntity trans = convertTransaction(transaction, name);

        repository.saveTransaction(trans);

        transactionNames.add(transaction.getTag());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, transactionNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        transactionView.setTransactionAdapter(arrayAdapter);
    }

    @Override
    public void deleteTransaction(String name) {
        repository.deleteTransation(name);

        transactionNames = repository.getTransactionsNames();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, transactionNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        transactionView.setTransactionAdapter(arrayAdapter);
        if(transactionNames.isEmpty()) {
            transactionView.showTransaction(new TransactionEntity());
        } else {
            transactionView.showTransaction(repository.getTransaction(transactionNames.get(0)));
        }
    }

    /**
     * Saves terminal number to database.
     * @param terminalNum Terminal number.
     */
    @Override
    public void saveTermNum(String terminalNum) {

        for(int i = 0; i < terminalNums.size(); i ++) {
            if(terminalNum.equals(terminalNums.get(i))) {
                view.showToast("Terminal number is already saved !");
                return;
            }
        }

        repository.saveTerminalNum(new TerminalNumberEntity(terminalNum));

        terminalNums.add(terminalNum);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, terminalNums);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        connectionView.setTerminalNumAdapter(arrayAdapter);
    }

    @Override
    public void deleteTermNum(String terminalNum) {
        repository.deleteTerminalNum(new TerminalNumberEntity(terminalNum));

        terminalNums = repository.getTerminalNumbers();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, terminalNums);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        connectionView.setTerminalNumAdapter(arrayAdapter);
    }

    /**
     * Saves hostname to database.
     * @param hostname Hostname address.
     */
    @Override
    public void saveHostName(String hostname) {

        for(int i = 0; i < hostnames.size(); i ++) {
            if(hostname.equals(hostnames.get(i))) {
                view.showToast("Hostname is already saved !");
                return;
            }
        }

        repository.saveHostname(new HostnameEntity(hostname));

        hostnames.add(hostname);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, hostnames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        connectionView.setHostnameAdapter(arrayAdapter);
    }

    @Override
    public void deleteHostName(String hostname) {
        repository.deleteHostname(new HostnameEntity(hostname));

        hostnames = repository.getHostnames();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, hostnames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        connectionView.setHostnameAdapter(arrayAdapter);
    }

    /**
     * Saves port to database.
     * @param port Port number.
     */
    @Override
    public void savePort(String port) {

        for(int i = 0; i < ports.size(); i ++) {
            if(port.equals(ports.get(i))) {
                view.showToast("Port is already saved !");
                return;
            }
        }

        repository.savePort(new PortEntity(port));

        ports.add(port);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, ports);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        connectionView.setPortAdapter(arrayAdapter);
    }

    @Override
    public void deletePort(String port) {
        repository.deletePort(new PortEntity(port));

        ports = repository.getPorts();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, ports);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        connectionView.setPortAdapter(arrayAdapter);
    }

    /**
     * Starts or terminates automatic incrementation of parameters.
     * @param automatic Automatic incrementation.
     */
    @Override
    public void startAutomatic(boolean automatic) {
        if (automatic) {
            incrementer.go();
        } else {
            incrementer.terminate();
        }
    }

    /**
     * Connects to hostname:port.
     */
    @Override
    public void connect() {
        if(connectionView.checkConnectionInputs())
            paymentService.connect(new ConnectionListener() {
                @Override
                public void onConnected() {
                    view.showConnected();

                    if(paymentService.getType() == TCP)
                        view.showToast("Connected to " + paymentService.getHostname() + ":" + paymentService.getPort());
                    else
                        view.showToast("Connected to " + paymentService.getDeviceName() + ":" + paymentService.getDeviceAddress());

                    paymentService.listen(new ResponseListener() {
                        @Override
                        public void onResponseReceived(Response response) {     //Readed response
                            lastResponse = response;
                            view.showResponseTab(Integer.parseInt(response.responseCode));
                            if(responseView != null) {
                                responseView.showResponse(response);
                            }
                        }

                        @Override
                        public void onReadFailed() {
                            view.showToast("Read Failed !");
                            disconnect();
                        }
                    });
                }

                @Override
                public void onUnknownHost(UnknownHostException e) {
                    e.printStackTrace();

                    view.showToast("Unknown Host !");
                    view.showDisconnected();
                }

                @Override
                public void onSocketFail(IOException e) {
                    e.printStackTrace();

                    view.showToast("Socket Failed !");
                    view.showDisconnected();
                }
            });
    }

    /**
     * Disconnect from hostname:port
     */
    @Override
    public void disconnect() {
        try {
            paymentService.disconnect();

            bluetoothDevice = null;

            view.showToast("Disconnected from " + paymentService.getHostname() + ":" + paymentService.getPort());
            view.showDisconnected();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Send transaction.
     */
    @Override
    public void send() {
        if(connectionView.checkConnectionInputs() && transactionView.checkTransactionInputs()) {
            Transaction transaction = transactionView.createTransaction();
            transaction.terminalNum = TID;

            TransactionEntity lastTransaction = repository.getTransaction("Last Transaction");

            if(lastTransaction == null) {
                repository.saveTransaction(convertTransaction(transaction, "Last Transaction"));
            } else {
                repository.deleteTransation("Last Transaction");
                repository.saveTransaction(convertTransaction(transaction, "Last Transaction"));
            }

            initializeTransactionSpinners();

            if(responseView != null)
                responseView.clearResponse();

            try {
                paymentService.sendTransaction(transaction);
            } catch (TransactionFieldException e) {
                e.printStackTrace();
            }

            view.showToast("Message has been send to " + paymentService.getHostname() + ":" + paymentService.getPort());
        }
    }

    @Override
    public void selectConnection(String type) {
        if(type.equals("Bluetooth") && paymentService.getType() != BLUETOOTH ) {
            try {
                view.showToast("Changed to bluetooth");

                paymentService.disconnect();
                view.showDisconnected();

                paymentService = new PaymentService(BLUETOOTH);
                paymentService.setDevice(bluetoothDevice);

                connectionView.showBluetooth();

                connectionType = BLUETOOTH;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (BluetoothException e) {
                bluetooth = false;
                e.printStackTrace();
            }
        } else if (type.equals("TCP") && paymentService.getType() != TCP) {
            try {
                view.showToast("Changed to TCP");

                paymentService.disconnect();
                view.showDisconnected();

                paymentService = new PaymentService(TCP);
                paymentService.setHostname(hostname);
                paymentService.setPort(Integer.valueOf(port));

                connectionView.showTCP();

                connectionType = TCP;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (BluetoothException e) {
                bluetooth = false;
                e.printStackTrace();
            }
        }

    }

    @Override
    public void sale(String amount, String currency) {
        try {
            paymentService.sale(Integer.parseInt(TID), this.transactionID, this.dateTime, Integer.parseInt(amount), currency);
        } catch (TransactionFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refund(String amount, String currecy) {
        try {
            paymentService.refund(Integer.parseInt(TID), this.transactionID, this.dateTime, Integer.parseInt(amount), currecy);
        } catch (TransactionFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancellation(String transID, String amount, String currency) {
        try {
            paymentService.cancellation(Integer.parseInt(TID), this.transactionID, this.dateTime, transID,  Integer.parseInt(amount), currency);
        } catch (TransactionFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void abort() {
        try {
            paymentService.abort(this.transactionID, this.dateTime);
        } catch (TransactionFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load transaction from database.
     * @param name Name of saved transaction.
     */
    @Override
    public void loadTransaction(String name) {
        TransactionEntity transactionEntity = repository.getTransaction(name);

        transactionView.showTransaction(transactionEntity);
    }

    @Override
    public void setHostname(String hostname) {
        paymentService.setHostname(hostname);

        this.hostname = hostname;
    }

    @Override
    public void setPort(String port) {
        paymentService.setPort(Integer.parseInt(port));

        this.port = port;
    }

    @Override
    public void setTID(String TID) {
        this.TID = TID;
    }

    @Override
    public boolean isConnected() {
        return paymentService.isConnected();
    }

    @Override
    public Response getLastResponse() {
        return this.lastResponse;
    }

    /*
    @Override
    public int startNatali(Context context) {
       return paymentService.startService(context);
    }
    */

    @Override
    public PaymentService getPaymentService() {
        return paymentService;
    }

    @Override
    public void takeConnectionView(MainContract.View.ConnectionTab view) {
        connectionView = view;

        initializeConnectionSpinners();
    }

    @Override
    public void takeTransactionView(MainContract.View.TransactionTab view) {
        transactionView = view;

        initializeTransactionSpinners();
    }

    @Override
    public void takeResponseView(MainContract.View.ResponseTab view) {
        responseView = view;

        if(lastResponse != null) {
            responseView.showResponse(lastResponse);
        }
    }

    @Override
    public void takeTemplatesView(MainContract.View.TemplatesTab view) {
        templatesView = view;
    }

    @Override
    public void takeCardView(MainContract.View.CardTab view) {
        cardView = view;
    }

    @Override
    public void takePrintView(MainContract.View.PrintTab view) {
        printView = view;
    }

    @Override
    public void startScan() {
        if(bluetoothAdapter.startDiscovery()) {
            Log.d("DBG","startScan run !");
        } else {
            Log.d("DBG","startScan failed !");
        }
    }

    @Override
    public void stopScan() {
        if(bluetoothAdapter.cancelDiscovery()) {
            Log.d("DBG","stopScan run !");
        } else {
            Log.d("DBG","stopScan failed !");
        }
    }

    @Override
    public Set<BluetoothDevice> getPairedDevices() {
        if(bluetoothAdapter != null)
            return bluetoothAdapter.getBondedDevices();
        else return null;
    }


    @Override
    public void openCardControl() {
        if(cardConnected) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(smartPOSController.RFClose() != RFReturnCode.SUCCESS) {
                        cardView.showResponse("DISCONNECT : failed");
                    }
                    cardConnected = false;
                }
            }).start();
        } else {
            cardConnected = true;
            cardView.changeOpen("DISCONNECT");
            smartPOSController.RFOpen(10000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {

                @Override
                public void onDetected(CardControl cardControl, int i, byte[] bytes) {
                    cardView.showResponse("RF Card successful !");
                    cardView.showResponse("Card Type : " +ByteUtil.byte2HexStr((byte) i));
                    cardView.showResponse("UUID : " + Utils.bytes2HexStr(bytes));

                    RFReturnCode close = cardControl.RFClose();
                    cardView.showResponse("Close status : " + close);
                    cardView.changeOpen("OPEN");
                    cardConnected = false;
                }

                @Override
                public void onError(RFReturnCode rfReturnCode) {
                    cardConnected = false;

                    if(RFReturnCode.INTERNAL_ERROR == rfReturnCode) {
                        cardView.showResponse("RF Open - INTERNAL_ERROR");
                        cardView.changeOpen("OPEN");
                    }

                    if(RFReturnCode.DEVICE_BUSY == rfReturnCode) {
                        cardView.showResponse("RF Open - DEVICE_BUSY");
                        cardView.changeOpen("OPEN");
                    }

                    if(RFReturnCode.TIMEOUT == rfReturnCode) {
                        cardView.showResponse("RF Open - TIMEOUT");
                        cardView.changeOpen("OPEN");
                    }

                    if(RFReturnCode.CONNECTION_FAILED == rfReturnCode) {
                        cardView.showResponse("RF Open - CONNECTION_FAILED");
                        cardView.changeOpen("OPEN");
                    }

                    if(RFReturnCode.DISCONNECTED == rfReturnCode) {
                        cardView.showResponse("RF Open - DISCONNECTED");
                        cardView.changeOpen("OPEN");
                    }
                }
            });
        }
    }

    @Override
    public void authenticateM0CardControl(final String data) {
       smartPOSController.RFOpen(30000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {

           @Override
           public void onDetected(CardControl cardControl, int i, byte[] bytes) {
               cardView.showResponse("RF Card successful !");
               cardView.showResponse("Card Type : " +ByteUtil.byte2HexStr((byte) i));
               cardView.showResponse("UUID : " + Utils.bytes2HexStr(bytes));

               RFReturnCode authenticateM0 = cardControl.RFAuthenticateM0(ByteUtil.hexStr2Bytes(data));
               cardView.showResponse("Auth-M0 status : " + authenticateM0);

               RFReturnCode close = cardControl.RFClose();
               cardView.showResponse("Close status : " + close);
           }

           @Override
           public void onError(RFReturnCode rfReturnCode) {
               if(RFReturnCode.INTERNAL_ERROR == rfReturnCode) {
                   cardView.showResponse("RF Open - INTERNAL_ERROR");
               }

               if(RFReturnCode.DEVICE_BUSY == rfReturnCode) {
                   cardView.showResponse("RF Open - DEVICE_BUSY");
               }

               if(RFReturnCode.TIMEOUT == rfReturnCode) {
                   cardView.showResponse("RF Open - TIMEOUT");
               }

               if(RFReturnCode.CONNECTION_FAILED == rfReturnCode) {
                   cardView.showResponse("RF Open - CONNECTION_FAILED");
               }
           }
       });
    }

    @Override
    public void authenticateM1CardControl(final String keyMode, final String snr, final String blockID, final String key) {
        smartPOSController.RFOpen(30000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {

            @Override
            public void onDetected(CardControl cardControl, int i, byte[] bytes) {
                cardView.showResponse("RF Card successful !");
                cardView.showResponse("Card Type : " +ByteUtil.byte2HexStr((byte) i));
                cardView.showResponse("UUID : " + Utils.bytes2HexStr(bytes));

                byte selectedKeyMode = ByteUtil.hexStr2Byte(keyMode);

                RFReturnCode authenticateM1 = RFReturnCode.INTERNAL_ERROR;

                if(selectedKeyMode == RFKeyMode.KEY_A_0x00.value) {
                    authenticateM1 = cardControl.RFAuthenticateM1(RFKeyMode.KEY_A_0x00, ByteUtil.hexStr2Bytes(snr), Integer.parseInt(blockID), ByteUtil.hexStr2Bytes(key));
                } else if(selectedKeyMode == RFKeyMode.KEY_A_0x60.value) {
                    authenticateM1 = cardControl.RFAuthenticateM1(RFKeyMode.KEY_A_0x60, ByteUtil.hexStr2Bytes(snr), Integer.parseInt(blockID), ByteUtil.hexStr2Bytes(key));
                } else if(selectedKeyMode == RFKeyMode.KEY_B_0x01.value) {
                    authenticateM1 = cardControl.RFAuthenticateM1(RFKeyMode.KEY_B_0x01, ByteUtil.hexStr2Bytes(snr), Integer.parseInt(blockID), ByteUtil.hexStr2Bytes(key));
                } else if(selectedKeyMode == RFKeyMode.KEY_B_0x61.value) {
                    authenticateM1 = cardControl.RFAuthenticateM1(RFKeyMode.KEY_B_0x61, ByteUtil.hexStr2Bytes(snr), Integer.parseInt(blockID), ByteUtil.hexStr2Bytes(key));
                }

                cardView.showResponse("Auth-M1 status : " + authenticateM1);

                RFReturnCode close = cardControl.RFClose();
                cardView.showResponse("Close status : " + close);
            }

            @Override
            public void onError(RFReturnCode rfReturnCode) {
                if(RFReturnCode.INTERNAL_ERROR == rfReturnCode) {
                    cardView.showResponse("RF Open - INTERNAL_ERROR");
                }

                if(RFReturnCode.DEVICE_BUSY == rfReturnCode) {
                    cardView.showResponse("RF Open - DEVICE_BUSY");
                }

                if(RFReturnCode.TIMEOUT == rfReturnCode) {
                    cardView.showResponse("RF Open - TIMEOUT");
                }

                if(RFReturnCode.CONNECTION_FAILED == rfReturnCode) {
                    cardView.showResponse("RF Open - CONNECTION_FAILED");
                }
            }
        });

    }

    @Override
    public void readCardControl(final String blockID) {
        smartPOSController.RFOpen(30000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {

            @Override
            public void onDetected(CardControl cardControl, int i, byte[] bytes) {
                cardView.showResponse("RF Card successful !");
                cardView.showResponse("Card Type : " +ByteUtil.byte2HexStr((byte) i));
                cardView.showResponse("UUID : " + Utils.bytes2HexStr(bytes));

                Pair<RFReturnCode, byte[]> response = cardControl.RFReadBlock(Integer.parseInt(blockID));
                cardView.showResponse("Read status : " + response.first);
                cardView.showResponse("Read data : [" + ByteUtil.bytes2HexStr_2(response.second) + "]");

                RFReturnCode close = cardControl.RFClose();
                cardView.showResponse("Close status : " + close);
            }

            @Override
            public void onError(RFReturnCode rfReturnCode) {
                if(RFReturnCode.INTERNAL_ERROR == rfReturnCode) {
                    cardView.showResponse("RF Open - INTERNAL_ERROR");
                }

                if(RFReturnCode.DEVICE_BUSY == rfReturnCode) {
                    cardView.showResponse("RF Open - DEVICE_BUSY");
                }

                if(RFReturnCode.TIMEOUT == rfReturnCode) {
                    cardView.showResponse("RF Open - TIMEOUT");
                }

                if(RFReturnCode.CONNECTION_FAILED == rfReturnCode) {
                    cardView.showResponse("RF Open - CONNECTION_FAILED");
                }
            }
        });
    }

    @Override
    public void writeCardControl(final String blockID, final String data) {
        smartPOSController.RFOpen(30000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {

            @Override
            public void onDetected(CardControl cardControl, int i, byte[] bytes) {
                cardView.showResponse("RF Card successful !");
                cardView.showResponse("Card Type : " +ByteUtil.byte2HexStr((byte) i));
                cardView.showResponse("UUID : " + Utils.bytes2HexStr(bytes));

                RFReturnCode write = cardControl.RFWriteBlock(Integer.parseInt(blockID), ByteUtil.hexStr2Bytes(data));
                cardView.showResponse("Write status : " + write);

                RFReturnCode close = cardControl.RFClose();
                cardView.showResponse("Close status : " + close);
            }

            @Override
            public void onError(RFReturnCode rfReturnCode) {
                if(RFReturnCode.INTERNAL_ERROR == rfReturnCode) {
                    cardView.showResponse("RF Open - INTERNAL_ERROR");
                }

                if(RFReturnCode.DEVICE_BUSY == rfReturnCode) {
                    cardView.showResponse("RF Open - DEVICE_BUSY");
                }

                if(RFReturnCode.TIMEOUT == rfReturnCode) {
                    cardView.showResponse("RF Open - TIMEOUT");
                }

                if(RFReturnCode.CONNECTION_FAILED == rfReturnCode) {
                    cardView.showResponse("RF Open - CONNECTION_FAILED");
                }
            }
        });
    }

    @Override
    public void transmitCardControl(final String data) {
        smartPOSController.RFOpen(30000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {

            @Override
            public void onDetected(CardControl cardControl, int i, byte[] bytes) {
                cardView.showResponse("RF Card successful !");
                cardView.showResponse("Card Type : " + ByteUtil.byte2HexStr((byte) i));
                cardView.showResponse("UUID : " + Utils.bytes2HexStr(bytes));

                Pair<RFReturnCode, byte[]> response = cardControl.RFTransmit(ByteUtil.hexStr2Bytes(data));
                cardView.showResponse("Transmit status : " + response.first);
                cardView.showResponse("Transmit read data : [" + ByteUtil.bytes2HexStr_2(response.second) + "]");

                RFReturnCode close = cardControl.RFClose();
                cardView.showResponse("Close status : " + close);
            }

            @Override
            public void onError(RFReturnCode rfReturnCode) {
                if(RFReturnCode.INTERNAL_ERROR == rfReturnCode) {
                    cardView.showResponse("RF Open - INTERNAL_ERROR");
                }

                if(RFReturnCode.DEVICE_BUSY == rfReturnCode) {
                    cardView.showResponse("RF Open - DEVICE_BUSY");
                }

                if(RFReturnCode.TIMEOUT == rfReturnCode) {
                    cardView.showResponse("RF Open - TIMEOUT");
                }

                if(RFReturnCode.CONNECTION_FAILED == rfReturnCode) {
                    cardView.showResponse("RF Open - CONNECTION_FAILED");
                }
            }
        });
    }

    @Override
    public void transmitCardControlReadAll(final String key, final int start, final int end) {
        smartPOSController.RFOpen(30000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {

            @Override
            public void onDetected(CardControl cardControl, int ct, byte[] bytes) {
                cardView.showResponse("RF Card successful !");
                cardView.showResponse("Card Type : " + ByteUtil.byte2HexStr((byte) ct));
                cardView.showResponse("UUID : " + Utils.bytes2HexStr(bytes));


                if(end > 3 && key != null) {   //Performing authentication
                    TDEAKey TDEAkey = new TDEAKey(ByteUtil.hexStr2Bytes(key));

                    Pair<RFReturnCode, byte[]> response = cardControl.RFTransmit(Utils.calcCrc(ByteUtil.hexStr2Bytes("1A00")));

                    if (response.first != RFReturnCode.SUCCESS || response.second.length != 11 || response.second[0] != (byte) 0xAF) {
                        cardView.showResponse("Invalid tag key");

                        RFReturnCode close = cardControl.RFClose();
                        cardView.showResponse("Close status : " + close);
                        return;
                    }

                    byte[] encryptedB = Arrays.copyOfRange(response.second, 1, 9);

                    IvParameterSpec ivSpec = createIvSpecFromZeros(8);
                    byte[] decryptedB;

                    try {
                        decryptedB = decrypt(encryptedB, TDEAkey, ivSpec);

                    } catch (Exception e) {
                        cardView.showResponse("Failed to decrypt B" + e);

                        RFReturnCode close = cardControl.RFClose();
                        cardView.showResponse("Close status : " + close);
                        return;
                    }

                    byte[] bDash = concatenate(Arrays.copyOfRange(decryptedB, 1, 8), Arrays.copyOfRange(decryptedB, 0, 1));

                    SecureRandom secureRandom = new SecureRandom();
                    byte[] a = createZeros(8);
                    secureRandom.nextBytes(a);

                    byte[] aDash = concatenate(Arrays.copyOfRange(a, 1, 8), Arrays.copyOfRange(a, 0, 1));
                    byte[] abDash = concatenate(a, bDash);

                    ivSpec = new IvParameterSpec(encryptedB);

                    byte[] encryptedAB;

                    try {
                        encryptedAB = encrypt(abDash, TDEAkey, ivSpec);

                    } catch (Exception e) {
                        cardView.showResponse("Failed to encrypt AB" + e);

                        RFReturnCode close = cardControl.RFClose();
                        cardView.showResponse("Close status : " + close);
                        return;
                    }

                    byte[] requestAB = concatenate(new byte[]{(byte) 0xAF}, encryptedAB);

                    Pair<RFReturnCode, byte[]> finalResponse;

                    try {
                        finalResponse = cardControl.RFTransmit(Utils.calcCrc(requestAB));

                    } catch (Exception e) {
                        cardView.showResponse("Failed communication with chip" + e);

                        RFReturnCode close = cardControl.RFClose();
                        cardView.showResponse("Close status : " + close);
                        return;
                    }

                    if (finalResponse.first != RFReturnCode.SUCCESS || finalResponse.second.length != 11 || finalResponse.second[0] != (byte) 0x00) {
                        cardView.showResponse("Invalid tag key");

                        RFReturnCode close = cardControl.RFClose();
                        cardView.showResponse("Close status : " + close);
                        return;
                    }
                    byte[] encryptedADash = Arrays.copyOfRange(finalResponse.second, 1, 9);

                    ivSpec = new IvParameterSpec(Arrays.copyOfRange(encryptedAB, 8, 16));

                    byte[] reconstructedADash;

                    try {
                        reconstructedADash = decrypt(encryptedADash, TDEAkey, ivSpec);

                    } catch (Exception e) {
                        cardView.showResponse("Failed to decrypt ADash" + e);

                        RFReturnCode close = cardControl.RFClose();
                        cardView.showResponse("Close status : " + close);
                        return;
                    }

                    if (!Arrays.equals(reconstructedADash, aDash)) {
                        cardView.showResponse("Invalid tag key");

                        RFReturnCode close = cardControl.RFClose();
                        cardView.showResponse("Close status : " + close);
                        return;
                    }
                }

                ArrayList<byte[]> request = new ArrayList<>();

                for (int i = start; i < end; i++) {
                    request.add(Utils.calcCrc(new byte[]{0x30,(byte) i}));
                }

                Pair<RFReturnCode, ArrayList<byte[]>> responseTmp = cardControl.RFTransmit(request);

                cardView.showResponse("Transmit status : " + responseTmp.first);
                int i = 0;
                for (byte[] b : responseTmp.second) {
                    cardView.showResponse("B" + (i++) +": [" + at.tecs.smartpos_demo.utils.ByteUtil.bytes2HexStr(b) + "]");
                }

                RFReturnCode close = cardControl.RFClose();
                cardView.showResponse("Close status : " + close);
            }

            @Override
            public void onError(RFReturnCode rfReturnCode) {
                if(RFReturnCode.INTERNAL_ERROR == rfReturnCode) {
                    cardView.showResponse("RF Open - INTERNAL_ERROR");
                }

                if(RFReturnCode.DEVICE_BUSY == rfReturnCode) {
                    cardView.showResponse("RF Open - DEVICE_BUSY");
                }

                if(RFReturnCode.TIMEOUT == rfReturnCode) {
                    cardView.showResponse("RF Open - TIMEOUT");
                }

                if(RFReturnCode.CONNECTION_FAILED == rfReturnCode) {
                    cardView.showResponse("RF Open - CONNECTION_FAILED");
                }
            }
        });
    }

    @Override
    public ConnectionType getSelected() {
        return selected;
    }

    private class Incrementer extends TimerTask {

        private volatile boolean alive;

        Incrementer() {
            alive = true;
        }

        void terminate() {
            alive = false;
        }

        void go() {
            alive = true;
        }

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        @Override
        public void run() {
            if (alive) {
                Date date = new Date(System.currentTimeMillis());
                transactionID = formatter.format(date);
                dateTime = formatter.format(date);
                if(transactionView != null)
                    transactionView.showTransactionAuto(transactionID, dateTime);
            }
        }
    }

    @Override
    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        if(bluetoothDevice != null) {
            paymentService.setDevice(bluetoothDevice);
        }
    }

    @Override
    public void printerPrint(final String data, final int dataType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    smartPOSController.PrinterOpen();
                    smartPOSController.PrinterPrint(data, dataType);
                    smartPOSController.PrinterClose();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void printerFeedLine(final int linesCount) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    smartPOSController.PrinterOpen();
                    smartPOSController.PrinterFeedLine(linesCount);
                    smartPOSController.PrinterClose();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void printerFullReceipt() {
       new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //Open connection and printer
                    int ret = smartPOSController.PrinterOpen();
                    if(ret != PrinterReturnCode.SUCCESS.value) {
                        smartPOSController.PrinterClose();
                        return;
                    }

                    int status = smartPOSController.PrinterGetStatus();
                    if(status != PrinterReturnCode.SUCCESS.value) {
                        smartPOSController.PrinterClose();
                        return;
                    }

                    //Printing image encoded as Base64 string
                    ret = smartPOSController.PrinterPrint("Qk3+EgAAAAAAAD4AAAAoAAAAgAEAAGQAAAABAAEAAAAAAMASAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP///wD/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAB////////8////////////////////////////////////////////////////gAAP///////8/////wB////////////////wH///////z///////////////////gAAH///////8/////AAf///8AAAAAf////4AAP/////AAB//////////////////gAAB///////8/////AAf///8AAAAAH////gAAB////4AAAf/////////////////gAAA///////8/////AAf///8AAAAAD///8AAAAf///gAAAH/////////////////gAAAf//////8/////AAf///8AAAAAD///4AAAAH//+AAAAB/////////////////gAAAP//////8/////AAf///8AAAAAB///gAAAAD//4AAAAA/////////////////gAAAP//////8/////AAf///8AAAAAB///AAAAAA//gAAAAAf////////////////gAAAH//////8/////AAf///8AAAAAB//+AAAAAAf/AAAAAAP////////////////gAAAH//////8/////AAf///8AAAAAB//8AAAAAAP+AAAAAAH////////////////gAAAD//////8/////AAf///8AAAAAB//4AAAAAAP+AAAAAAD////////////////gAAAD//////8/////AAf///8AAAAAB//wAAAAAAP+AAAAAAD////////////////gAAAB//////8/////AAf///8AB//////gAAH4AAf+AAD/gAB////////////////gAAAB//////8/////AAf///8AB//////AAA//gAf/AAf/4AB////////////////gAAAB//////8/////AAf///8AB//////AAD//4A//AD//+AB////////////////gAAAB//////8/////AAf///8AB/////+AAP//+B//gH///AA////////////////gAAAB//////8/////AAf///8AB/////+AAf///D//wf///AA////////////////gAAAB//////8/////AAf///8AB/////8AA////////////AA////////////////gAAAB//////8/////AAf///8AB/////8AB////////////gA////////////////gAAAB//////8/////AAf///8AB/////4AB////////////gA////////////////gAAAB//////8/////AAf///8AB/////4AD////////////gA////////////////gAAAB//////8/////AAf///8AB/////wAH////////////AA////////////////gAAAB//////8/////AAf///8AB/////wAH////////////AA////////////////gAAAB//////8/////AAf///8AB/////wAH////////////AA////////////////gAAAB//////8/////AAf///8AB/////gAP///////////+AA////////////////gAAAB//////8/////AAf///8AB/////gAP///////////8AA////////////////gAAAB//////8/////AAf///8AB/////gAP///////////wAB////////////////gAAAB//////8/////AAf///8AB/////gAf///////////gAB////////////////gAAAB//////8/////AAf///8AB/////gAf//////////+AAB////////////////gAAAB//////8/////AAf///8AB/////gAf//////////4AAD////////////////gAAAB//////8/////AAf///8AAAAP//AAf//////////gAAH////////////////gAAAB//////8/////AAf///8AAAAH//AAf/////////8AAAP////////////////gAAAB//////8/////AAf///8AAAAB//AAf/////////wAAAP////////////////gAAAB//////8/////AAf///8AAAAB//AAf/////////AAAAf////////////////gAAABqr////8/////AAf///8AAAAA//AAf////////8AAAB/////////////////gAAABVVf///8/////AAf///8AAAAA//AAf////////wAAAD/////////////////gAAABqqv///8/////AAf///8AAAAA//AA/////////gAAAP/////////////////gAAABVVV///8/////AAf///8AAAAA//AAf////////AAAAf/////////////////gAAABqqq///8/////AAf///8AAAAA//AAf///////+AAAB//////////////////gAAABVVVf//8/////AAf///8AAAAA//AAf///////8AAAH//////////////////gAAABqqqv//8/////AAf///8AAAAA//AAf///////4AAA///////////////////gAAABVUVX//8/////AAf///8AB/////gAf///////wAAD///////////////////gAAABqqqr//8/////AAf///8AB/////gAf///////wAAP///////////////////gAAABVVVV//8/////AAf///8AB/////gAf///////gAA////////////////////gAAABqqqr//8/////AAf///8AB/////gAP///////gAD////////////////////gAAABVVVU//8/////AAf///8AB/////gAP///////AAH////////////////////gAAABqqqq//8/////AAf///8AB/////wAP///////AAf////////////////////gAAABVVVVf/8/////AAf///8AB/////wAH///////AAf////////////////////gAAABqqqq//8/////AAf///8AB/////wAH///////AA/////////////////////gAAABVVVVf/8/////AAf///8AB/////4AD///////AA///////////////////4igAAABqqqqv/8/////AAf///8AB/////4AD///////AB//////////////////8AAgAAABVVVVf/8/////AAf///8AB/////4AB///////AB//////////////////oqKgAAABqqqqv/8/////AAf///8AB/////8AA///////AB/////////////////+AAAgAAABVVVVf/8/////AAf///8AB/////8AA///////AA/////////////////8iIigAAABqqqqv/8/////AAf///8AB/////+AAP///H//AA/////////////////4AAAgAAABVVVVf/8/////AAf///8AB/////+AAH//8B//AA///w/////////////6qqqgAAABqqqqv/8/////AAf///8AB//////AAD//wB//gAf//Af////////////gAAAwAAABVUVVf/8/////AAf///8AB//////gAAf/AA//gAH/8AP////////////IiIiQAAABqqqqv/8/+AABAAf///8AB//////gAAAAAAf/gAA/AAP////////////AAAAYAAABVVVVf/8/+AAAAAAAB/8AAAAAf//wAAAAAAf/wAAAAAH///////////+ioqKoAAABqqqqv/8/+AAAAAAAA/8AAAAAH//4AAAAAAP/wAAAAAH///////////+AAAAMAAABVVVVf/8/+AAAAAAAAf8AAAAAD//8AAAAAAP/4AAAAAD///////////+IiIiMAAABqqqqv/8/+AAAAAAAAf8AAAAAD//+AAAAAAf/8AAAAAD///////////8AAAAGAAABVVVVf/8/+AAAAAAAAP8AAAAAB///AAAAAA//+AAAAAH///////////+qqqqrAAABqqqqv/8/+AAAAAAAAP8AAAAAB///wAAAAD///AAAAAP///////////4AAAABgAABVVVVf/8//AAAAAAAAP8AAAAAB///4AAAAH///gAAAA////////////6IiIiI4AABqqqqv/8//AAAAAAAAP8AAAAAB///+AAAAf///4AAAD////////////4AAAAAMAABVVVVf/8//gAAAAAAAP8AAAAAB////wAAB////+AAAP////////////6ioqKivgABqqqq//8//4AAAAAAAP+AAAAAB////+AAf/////wAD/////////////4AAAAAA/////////////////////////////////////////////////////////6IiIiIiIiJ//////////////////////////////////////////////////////4AAAAAAAAB//////////////////////////////////////////////////////6qqqqqqqqr//////////////////////////////////////////////////////4AAAAAAAAD//////////////////////////////////////////////////////+IiIiIiIiP//////////////////////////////////////////////////////+AAAAAAAAP///////////////////////////////////////////////////////ioqKioqK//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8=",
                            PrinterPrintType.IMAGE.value);
                    if(ret != PrinterReturnCode.SUCCESS.value) {
                        smartPOSController.PrinterClose();
                        return;
                    }
                    //Feed 2 lines
                    smartPOSController.PrinterFeedLine(2);

                    //Print multi line text
                    ret = smartPOSController.PrinterPrint("         KUNDENBELEG\n\n\nTID: 88091137   MID: 102003557\nDate: 07/05/2021     Time: 15:30\n\n\nSALE                            \nVISA CREDIT                     \nPAN: ************0119          \nPAN SEQ NO: 01                  \nVISA ACQUIRER TEST/CARD 01     \n\n\nSTAN: 154714                   \nTXID: 20210507153032           \nORIG. TXID: 20210507153032     \nAPPROVAL CODE: 213031          \nINVOICE:1                      \nAC: F46E3CEA8232A966           \nAID: A0000000031010                \n\n\nEUR                        1.00\n\n\n           Authorized\n",
                            PrinterPrintType.TEXT.value);
                    if(ret != PrinterReturnCode.SUCCESS.value) {
                        smartPOSController.PrinterClose();
                        return;
                    }
                    //Feed 2 lines
                    smartPOSController.PrinterFeedLine(2);

                    //Print QR code from provided text
                    ret = smartPOSController.PrinterPrint("https://www.tecs.at/", PrinterPrintType.QR_SMALL.value);
                    if(ret != PrinterReturnCode.SUCCESS.value) {
                        smartPOSController.PrinterClose();
                        return;
                    }

                    //Feed 10 lines
                    smartPOSController.PrinterFeedLine(10);

                    smartPOSController.PrinterClose();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initializeConnectionSpinners() {

        terminalNums = repository.getTerminalNumbers();

        if (!terminalNums.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, terminalNums);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            connectionView.setTerminalNumAdapter(arrayAdapter);
        }

        hostnames = repository.getHostnames();

        if (!hostnames.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, hostnames);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            connectionView.setHostnameAdapter(arrayAdapter);
        }

        ports = repository.getPorts();

        if (!ports.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, ports);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            connectionView.setPortAdapter(arrayAdapter);
        }
    }

    private void initializeTransactionSpinners() {
        transactionNames = repository.getTransactionsNames();

        if (!transactionNames.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, transactionNames);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            transactionView.setTransactionAdapter(arrayAdapter);
            transactionView.showTransaction(repository.getTransaction(transactionNames.get(0)));
        }
    }

    private TransactionEntity convertTransaction(Transaction transaction,String name) {
        TransactionEntity trans = new TransactionEntity();

        trans.name = name;
        trans.ID = transaction.ID;
        trans.msgType = transaction.msgType;
        trans.dateTime = transaction.dateTime;
        trans.sourceID = transaction.sourceID;
        trans.cardNum = transaction.cardNum;
        trans.cvc2 = transaction.cvc2;
        trans.amount = transaction.amount;
        trans.currency = transaction.currency;
        trans.terminalNum = TID;
        trans.receiptNum = transaction.receiptNum;
        trans.transPlace = transaction.transPlace;
        trans.authorNum = transaction.authorNum;
        trans.originInd = transaction.originInd;
        trans.password = transaction.password;
        trans.userdata = transaction.ecrdata;
        trans.langCode = transaction.langCode;
        trans.desCurrency = transaction.desCurrency;
        trans.receiptLayout = transaction.receiptLayout;
        trans.txOrigin = transaction.txOrigin;
        trans.personalID = transaction.personalID;

        return trans;
    }
}
