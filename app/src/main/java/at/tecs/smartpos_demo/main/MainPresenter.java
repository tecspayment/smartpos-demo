package at.tecs.smartpos_demo.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import at.tecs.smartpos.PaymentService;
import at.tecs.smartpos.connector.ConnectionListener;
import at.tecs.smartpos.connector.ResponseListener;
import at.tecs.smartpos.data.Response;
import at.tecs.smartpos.data.Transaction;
import at.tecs.smartpos_demo.data.repository.Repository;
import at.tecs.smartpos_demo.data.repository.entity.HostnameEntity;
import at.tecs.smartpos_demo.data.repository.entity.PortEntity;
import at.tecs.smartpos_demo.data.repository.entity.TerminalNumberEntity;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;


public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;

    private Repository repository;

    private Timer timer;
    private Incrementer incrementer;

    private String transactionID;
    private String dateTime;

    private Response lastResponse;

    private ArrayList<String> transactionNames = new ArrayList<>();
    private ArrayList<String> terminalNums = new ArrayList<>();
    private ArrayList<String> hostnames = new ArrayList<>();
    private ArrayList<String> ports = new ArrayList<>();

    private PaymentService paymentService;

    MainPresenter() {
        paymentService = new PaymentService();
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

        initializeSpinners();
    }

    /**
     * Saves transaction to database.
     * @param transaction
     */
    @Override
    public void saveTransaction(Transaction transaction) {

        TransactionEntity trans = new TransactionEntity();

        trans.ID = transaction.ID;
        trans.msgType = transaction.msgType;
        trans.dateTime = transaction.dateTime;
        trans.sourceID = transaction.sourceID;
        trans.cardNum = transaction.cardNum;
        trans.cvc2 = transaction.cvc2;
        trans.amount = transaction.amount;
        trans.currency = transaction.currency;
        trans.terminalNum = transaction.terminalNum;
        trans.receiptNum = transaction.receiptNum;
        trans.transPlace = transaction.transPlace;
        trans.authorNum = transaction.authorNum;
        trans.originInd = transaction.originInd;
        trans.password = transaction.password;
        trans.userdata = transaction.userdata;
        trans.langCode = transaction.langCode;
        trans.desCurrency = transaction.desCurrency;
        trans.receiptLayout = transaction.receiptLayout;
        trans.txOrigin = transaction.txOrigin;
        trans.personalID = transaction.personalID;

        repository.saveTransaction(trans);

        transactionNames.add(transaction.getTag());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, transactionNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        view.setTransactionAdapter(arrayAdapter);
    }

    /**
     * Saves terminal number to database.
     * @param terminalNum
     */
    @Override
    public void saveTermNum(String terminalNum) {
        repository.saveTerminalNum(new TerminalNumberEntity(terminalNum));

        terminalNums.add(terminalNum);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, terminalNums);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        view.setTerminalNumAdapter(arrayAdapter);
    }

    /**
     * Saves hostname to database.
     * @param hostname
     */
    @Override
    public void saveHostName(String hostname) {
        repository.saveHostname(new HostnameEntity(hostname));

        hostnames.add(hostname);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, hostnames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        view.setHostnameAdapter(arrayAdapter);
    }

    /**
     * Saves port to database.
     * @param port
     */
    @Override
    public void savePort(String port) {
        repository.savePort(new PortEntity(port));

        ports.add(port);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, ports);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        view.setPortAdapter(arrayAdapter);
    }

    /**
     * Starts or terminates automatic incrementation of parameters.
     * @param automatic
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
        paymentService.connect(new ConnectionListener() {
            @Override
            public void onConnected() {
                view.showConnected();

                view.showToast("Connected to " + paymentService.getHostname() + ":" + paymentService.getPort());

                paymentService.listen(new ResponseListener() {
                    @Override
                    public void onResponseReceived(Response response) {     //Readed response
                        lastResponse = response;
                        view.showResponse(response);
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

            view.showToast("Disconnected from " + paymentService.getHostname() + ":" + paymentService.getPort());
            view.showDisconnected();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Send transaction.
     * @param transaction
     */
    @Override
    public void send(Transaction transaction) {

        paymentService.sendTransaction(transaction);

        view.showToast("Message has been send to " + paymentService.getHostname() + ":" + paymentService.getPort());
    }

    /**
     * Load transaction from database.
     * @param name
     */
    @Override
    public void loadTransaction(String name) {
        TransactionEntity transactionEntity = repository.getTransaction(name);

        view.showTransaction(transactionEntity);
    }

    @Override
    public void setHostname(String hostname) {
        paymentService.setHostname(hostname);
    }

    @Override
    public void setPort(String port) {
        paymentService.setPort(Integer.valueOf(port));
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
    public int startNatali(Context context) {
       return paymentService.startService(context);
    }

    @Override
    public PaymentService getPaymentService() {
        return paymentService;
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
                transactionID = formatter.format(date) + "000000";
                dateTime = formatter.format(date);
                view.showTransactionAuto(transactionID, dateTime);
            }
        }
    }

    private void initializeSpinners() {
        transactionNames = repository.getTransactionsNames();

        if (!transactionNames.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, transactionNames);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            view.setTransactionAdapter(arrayAdapter);
            view.showTransaction(repository.getTransaction(transactionNames.get(0)));
        }

        terminalNums = repository.getTerminalNumbers();

        if (!terminalNums.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, terminalNums);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            view.setTerminalNumAdapter(arrayAdapter);
        }

        hostnames = repository.getHostnames();

        if (!hostnames.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, hostnames);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            view.setHostnameAdapter(arrayAdapter);
        }

        ports = repository.getPorts();

        if (!ports.isEmpty()) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, ports);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            view.setPortAdapter(arrayAdapter);
        }
    }
}
