package at.tecs.smartpos_demo.main;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import at.tecs.ControlParser.Command;
import at.tecs.smartpos.CardControl;
import at.tecs.smartpos.PaymentService;
import at.tecs.smartpos.SmartPOSController;
import at.tecs.smartpos.connector.ConnectionListener;
import at.tecs.smartpos.connector.ResponseListener;
import at.tecs.smartpos.data.ConnectionType;
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

import static at.tecs.smartpos.data.ConnectionType.BLUETOOTH;
import static at.tecs.smartpos.data.ConnectionType.TCP;


public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;
    private MainContract.View.ConnectionTab connectionView;
    private MainContract.View.TransactionTab transactionView;
    private MainContract.View.ResponseTab responseView;
    private MainContract.View.TemplatesTab templatesView;
    private MainContract.View.CardTab cardView;

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
    private final SmartPOSController cardService;

    private ConnectionType connectionType;

    private BluetoothDevice bluetoothDevice;

    private BluetoothAdapter bluetoothAdapter;

    private ConnectionType selected = TCP;

    public static boolean bluetooth = false;

    MainPresenter() {
        paymentService = new PaymentService();
        connectionType = paymentService.getType();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetoothAdapter != null) {
            bluetooth = true;
        }

        cardService = new SmartPOSController();
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
       cardService.RFOpen(30000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {

           @Override
           public void onDetected(CardControl cardControl, int i, byte[] bytes) {
               cardView.showResponse("RF Card successful !");
               cardView.showResponse("Card Type : " +ByteUtil.byte2HexStr((byte) i));
               cardView.showResponse("UUID : " + Utils.bytes2HexStr(bytes));

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
                   disconnect();
               }

               if(RFReturnCode.CONNECTION_FAILED == rfReturnCode) {
                   cardView.showResponse("RF Open - CONNECTION_FAILED");
               }
           }
       });
    }

    @Override
    public void authenticateM0CardControl(final String data) {
       cardService.RFOpen(30000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {

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
                   disconnect();
               }

               if(RFReturnCode.CONNECTION_FAILED == rfReturnCode) {
                   cardView.showResponse("RF Open - CONNECTION_FAILED");
               }
           }
       });
    }

    @Override
    public void authenticateM1CardControl(final String keyMode, final String snr, final String blockID, final String key) {
        cardService.RFOpen(30000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {

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
                    disconnect();
                }

                if(RFReturnCode.CONNECTION_FAILED == rfReturnCode) {
                    cardView.showResponse("RF Open - CONNECTION_FAILED");
                }
            }
        });

    }

    @Override
    public void readCardControl(final String blockID) {
        cardService.RFOpen(30000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {

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
                    disconnect();
                }

                if(RFReturnCode.CONNECTION_FAILED == rfReturnCode) {
                    cardView.showResponse("RF Open - CONNECTION_FAILED");
                }
            }
        });
    }

    @Override
    public void writeCardControl(final String blockID, final String data) {
        cardService.RFOpen(30000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {

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
                    disconnect();
                }

                if(RFReturnCode.CONNECTION_FAILED == rfReturnCode) {
                    cardView.showResponse("RF Open - CONNECTION_FAILED");
                }
            }
        });
    }

    @Override
    public void transmitCardControl(final String data) {
        cardService.RFOpen(30000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {

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
                    disconnect();
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
