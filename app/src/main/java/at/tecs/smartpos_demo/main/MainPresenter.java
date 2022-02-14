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
    private MainContract.View.ResponseTab responseView;

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

    private final PaymentService paymentService;
    private final SmartPOSController smartPOSController;

    public static boolean bluetooth = false;


    MainPresenter() {
        paymentService = new PaymentService();

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
        repository = Repository.getInstance(view.getContext());
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

        //connectionView.setTerminalNumAdapter(arrayAdapter);
    }

    @Override
    public void deleteTermNum(String terminalNum) {
        repository.deleteTerminalNum(new TerminalNumberEntity(terminalNum));

        terminalNums = repository.getTerminalNumbers();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, terminalNums);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //connectionView.setTerminalNumAdapter(arrayAdapter);
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

        //connectionView.setHostnameAdapter(arrayAdapter);
    }

    @Override
    public void deleteHostName(String hostname) {
        repository.deleteHostname(new HostnameEntity(hostname));

        hostnames = repository.getHostnames();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, hostnames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //connectionView.setHostnameAdapter(arrayAdapter);
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

        //connectionView.setPortAdapter(arrayAdapter);
    }

    @Override
    public void deletePort(String port) {
        repository.deletePort(new PortEntity(port));

        ports = repository.getPorts();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, ports);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //connectionView.setPortAdapter(arrayAdapter);
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
        //if(connectionView.checkConnectionInputs())
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
                        public void onDisconnected() {
                            //Log.e("TEST", "Disconnected !");
                            view.showDisconnected();
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

            view.showToast("Disconnected from " + paymentService.getHostname() + ":" + paymentService.getPort());
            view.showDisconnected();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void send(TransactionEntity transactionEntity) {
        Transaction transaction = convertTransaction(transactionEntity, transactionID);
        transaction.terminalNum = TID;
        transaction.dateTime = dateTime;

        if(responseView != null)
            responseView.clearResponse();

        try {
            paymentService.sendTransaction(transaction);
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

        //transactionView.showTransaction(transactionEntity);
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

    @Override
    public PaymentService getPaymentService() {
        return paymentService;
    }

    @Override
    public void takeConnectionView(MainContract.View.ConnectionTab view) {
       //connectionView = view;

        initializeConnectionSpinners();
    }

    @Override
    public void takeTransactionView(MainContract.View.TransactionTab view) {
        //transactionView = view;

        initializeTransactionSpinners();
    }

    @Override
    public void takeResponseView(MainContract.View.ResponseTab view) {
        responseView = view;

        if(lastResponse != null) {
            responseView.showResponse(lastResponse);
        }
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
                //if(transactionView != null)
                    //transactionView.showTransactionAuto(transactionID, dateTime);
            }

            if(!isConnected()) {
                connect();
            }
        }
    }

    private void initializeConnectionSpinners() {

        terminalNums = repository.getTerminalNumbers();

        if (!terminalNums.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, terminalNums);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //connectionView.setTerminalNumAdapter(arrayAdapter);
        }

        hostnames = repository.getHostnames();

        if (!hostnames.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, hostnames);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //connectionView.setHostnameAdapter(arrayAdapter);
        }

        ports = repository.getPorts();

        if (!ports.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, ports);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //connectionView.setPortAdapter(arrayAdapter);
        }
    }

    private void initializeTransactionSpinners() {
        transactionNames = repository.getTransactionsNames();

        if (!transactionNames.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, transactionNames);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            //transactionView.setTransactionAdapter(arrayAdapter);
            //transactionView.showTransaction(repository.getTransaction(transactionNames.get(0)));
        }
    }

    private TransactionEntity convertTransaction(Transaction transaction,String name) {
        TransactionEntity trans = new TransactionEntity();

        trans.name = name;
        trans.id = Integer.parseInt(transaction.ID);
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

    private Transaction convertTransaction(TransactionEntity transaction, String ID) {
        Transaction trans = new Transaction();

        trans.ID = ID;
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
        trans.ecrdata = transaction.userdata;
        trans.langCode = transaction.langCode;
        trans.desCurrency = transaction.desCurrency;
        trans.receiptLayout = transaction.receiptLayout;
        trans.txOrigin = transaction.txOrigin;
        trans.personalID = transaction.personalID;

        return trans;
    }
}
