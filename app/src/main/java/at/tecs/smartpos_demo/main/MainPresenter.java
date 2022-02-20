package at.tecs.smartpos_demo.main;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import at.tecs.smartpos.PaymentService;
import at.tecs.smartpos.connector.ConnectionListener;
import at.tecs.smartpos.connector.ResponseListener;
import at.tecs.smartpos.data.Response;
import at.tecs.smartpos.data.Transaction;
import at.tecs.smartpos.exception.TransactionFieldException;
import at.tecs.smartpos_demo.data.repository.Repository;
import at.tecs.smartpos_demo.data.repository.entity.HostnameEntity;
import at.tecs.smartpos_demo.data.repository.entity.PortEntity;
import at.tecs.smartpos_demo.data.repository.entity.RespHistoryEntity;
import at.tecs.smartpos_demo.data.repository.entity.TerminalNumberEntity;
import at.tecs.smartpos_demo.data.repository.entity.TransHistoryEntity;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;

import static at.tecs.smartpos.data.ConnectionType.TCP;


public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;
    private MainContract.View.ResponseTab responseView;
    private MainContract.View.ReceiptTab receiptView;

    private Repository repository;

    private Timer timer;
    private Incrementer incrementer;

    private String transactionID;
    private String dateTime;

    private String TID = "";
    private String hostname = "";
    private String port = "";

    private boolean autoConnect = true;

    private Response lastResponse;

    private final PaymentService paymentService;


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
        repository = Repository.getInstance(view.getContext());
    }

    /**
     * Saves terminal number to database.
     * @param terminalNum Terminal number.
     */
    @Override
    public void saveTermNum(String terminalNum) {

        if(!repository.getTerminalNumbers().contains(terminalNum.replace(" ", ""))) {
            repository.saveTerminalNum(new TerminalNumberEntity(terminalNum.replace(" ", "")));
        }
    }

    @Override
    public void deleteTermNum(String terminalNum) {
        repository.deleteTerminalNum(new TerminalNumberEntity(terminalNum));
    }

    /**
     * Saves hostname to database.
     * @param hostname Hostname address.
     */
    @Override
    public void saveHostName(String hostname) {

        if(!repository.getHostnames().contains(hostname.replace(" ", ""))) {
            repository.saveHostname(new HostnameEntity(hostname.replace(" ", "")));
        }
    }

    @Override
    public void deleteHostName(String hostname) {
        repository.deleteHostname(new HostnameEntity(hostname));
    }

    /**
     * Saves port to database.
     * @param port Port number.
     */
    @Override
    public void savePort(String port) {

        if(!repository.getPorts().contains(port.replace(" ", ""))) {
            repository.savePort(new PortEntity(port));
        }
    }

    @Override
    public void deletePort(String port) {
        repository.deletePort(new PortEntity(port));
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

                    if(paymentService.getType() == TCP) {
                        view.showNotification("Connected to " + paymentService.getHostname() + ":" + paymentService.getPort());
                    }

                    paymentService.listen(new ResponseListener() {
                        @Override
                        public void onResponseReceived(Response response) {     //Readed response
                            lastResponse = response;

                            if(!response.msgType.equals("5747")) { //Ignore notification messages
                                repository.saveResponse(convertResponse(response));
                            }

                            view.showResponseTab(Integer.parseInt(response.responseCode));
                            if(responseView != null) {
                                responseView.showResponse(response);
                            }

                            if(response.getScanData() != null && !response.getScanData().isEmpty()) {
                                view.showMessage("Scan result", response.getScanData());
                            }

                            Log.e("TEST", "Customer Receipt: " + response.getCustomerReceipt());
                            Log.e("TEST", "Merchant Receipt: " + response.getMerchantReceipt());

                            if(response.getCustomerReceipt() != null && !response.getCustomerReceipt().isEmpty() ||
                                    response.getMerchantReceipt() != null && !response.getMerchantReceipt().isEmpty()) {
                                receiptView.showReceipt(response.getMerchantReceipt(), response.getCustomerReceipt());
                            }
                        }

                        @Override
                        public void onDisconnected() {
                            //Log.e("TEST", "Disconnected !");
                            view.showDisconnected();
                        }

                        @Override
                        public void onReadFailed() {
                            view.showNotification("Read Failed!");
                            disconnect();
                        }
                    });
                }

                @Override
                public void onUnknownHost(UnknownHostException e) {
                    e.printStackTrace();

                    view.showNotification("Unknown Host!");
                    view.showDisconnected();
                }

                @Override
                public void onSocketFail(IOException e) {
                    e.printStackTrace();

                    view.showNotification("Connection lost!");
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

            view.showNotification("Disconnected from " + paymentService.getHostname() + ":" + paymentService.getPort());
            view.showDisconnected();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(TransactionEntity transactionEntity) {
        transactionEntity.terminalNum = TID;
        transactionEntity.dateTime = dateTime;
        Transaction transaction = convertTransaction(transactionEntity, transactionID);

        if(responseView != null) {
            responseView.clearResponse();
        }

        try {
            paymentService.sendTransaction(transaction);
            if(paymentService.isConnected()) {
                repository.saveTransaction(convertTransaction(transactionEntity));
            }
        } catch (TransactionFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setHostname(String hostname) {
        paymentService.setHostname(hostname);

        this.hostname = hostname;
    }

    @Override
    public void setPort(String port) {
        if(port != null && !port.isEmpty()) {
            paymentService.setPort(Integer.parseInt(port));

            this.port = port;
        }
    }

    @Override
    public void setTID(String TID) {
        Log.e("TEST", "setTID - " + TID);
        this.TID = TID;
    }

    @Override
    public void setAutoConnect(boolean autoConnect) {
        this.autoConnect = autoConnect;
    }

    @Override
    public boolean isAutoConnect() {
        return this.autoConnect;
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
    public void takeResponseView(MainContract.View.ResponseTab view) {
        responseView = view;

        if(lastResponse != null) {
            responseView.showResponse(lastResponse);
        }
    }

    @Override
    public void takeReceiptView(MainContract.View.ReceiptTab view) {
        receiptView = view;
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    @Override
    public String getPort() {
        return port;
    }

    @Override
    public String getTerminalNum() {
        return TID;
    }

    @Override
    public void loadDefaults() {
        PortEntity portEntity = new PortEntity();
        portEntity.port = "9990";

        repository.savePort(portEntity);

        HostnameEntity hostnameEntity = new HostnameEntity();
        hostnameEntity.hostname = "localhost";

        repository.saveHostname(hostnameEntity);

        TransactionEntity transaction = new TransactionEntity();
        transaction.name = "Sale";
        transaction.amountVisibility = true;
        transaction.currencyVisibility = true;
        transaction.sourceIDVisibility = false;
        transaction.cardNumVisibility = false;
        transaction.cvc2Visibility = false;
        transaction.receiptNumVisibility = false;
        transaction.paymentReasonVisibility = false;
        transaction.transPlaceVisibility = false;
        transaction.authorNumVisibility = false;
        transaction.originIndVisibility = false;
        transaction.passwordVisibility = false;
        transaction.userdataVisibility = false;
        transaction.langCodeVisibility = false;
        transaction.receiptLayoutVisibility = false;
        transaction.desCurrencyVisibility = false;
        transaction.txOriginVisibility = false;
        transaction.personalIDVisibility = false;
        transaction.msgType = Transaction.MessageType.SALE;
        transaction.sourceID = "1";
        transaction.receiptNum = "1";
        transaction.originInd = "0";

        repository.saveTransaction(transaction);

        transaction = new TransactionEntity();
        transaction.name = "Refund";
        transaction.amountVisibility = true;
        transaction.currencyVisibility = true;
        transaction.sourceIDVisibility = false;
        transaction.cardNumVisibility = false;
        transaction.cvc2Visibility = false;
        transaction.receiptNumVisibility = false;
        transaction.paymentReasonVisibility = false;
        transaction.transPlaceVisibility = false;
        transaction.authorNumVisibility = false;
        transaction.originIndVisibility = false;
        transaction.passwordVisibility = false;
        transaction.userdataVisibility = false;
        transaction.langCodeVisibility = false;
        transaction.receiptLayoutVisibility = false;
        transaction.desCurrencyVisibility = false;
        transaction.txOriginVisibility = false;
        transaction.personalIDVisibility = false;
        transaction.msgType = Transaction.MessageType.CREDIT_NOTE;
        transaction.sourceID = "1";
        transaction.receiptNum = "1";
        transaction.originInd = "0";

        repository.saveTransaction(transaction);

        transaction = new TransactionEntity();
        transaction.name = "Cancellation";
        transaction.cardNumVisibility = true;
        transaction.amountVisibility = true;
        transaction.currencyVisibility = true;
        transaction.amountVisibility = true;
        transaction.currencyVisibility = true;
        transaction.sourceIDVisibility = false;
        transaction.cvc2Visibility = false;
        transaction.receiptNumVisibility = false;
        transaction.paymentReasonVisibility = false;
        transaction.transPlaceVisibility = false;
        transaction.authorNumVisibility = false;
        transaction.originIndVisibility = false;
        transaction.passwordVisibility = false;
        transaction.userdataVisibility = false;
        transaction.langCodeVisibility = false;
        transaction.receiptLayoutVisibility = false;
        transaction.desCurrencyVisibility = false;
        transaction.txOriginVisibility = false;
        transaction.personalIDVisibility = false;
        transaction.msgType = Transaction.MessageType.CANCEL;
        transaction.sourceID = "1";
        transaction.cardNum = "TXID";
        transaction.receiptNum = "1";
        transaction.originInd = "2";

        repository.saveTransaction(transaction);

        transaction = new TransactionEntity();
        transaction.name = "Connection status";
        transaction.cardNumVisibility = false;
        transaction.amountVisibility = false;
        transaction.currencyVisibility = false;
        transaction.amountVisibility = false;
        transaction.currencyVisibility = false;
        transaction.sourceIDVisibility = false;
        transaction.cvc2Visibility = false;
        transaction.receiptNumVisibility = false;
        transaction.paymentReasonVisibility = false;
        transaction.transPlaceVisibility = false;
        transaction.authorNumVisibility = false;
        transaction.originIndVisibility = false;
        transaction.passwordVisibility = false;
        transaction.userdataVisibility = false;
        transaction.langCodeVisibility = false;
        transaction.receiptLayoutVisibility = false;
        transaction.desCurrencyVisibility = false;
        transaction.txOriginVisibility = false;
        transaction.personalIDVisibility = false;
        transaction.msgType = Transaction.MessageType.CONNECTION_STATUS;

        repository.saveTransaction(transaction);

        transaction = new TransactionEntity();
        transaction.cardNumVisibility = false;
        transaction.amountVisibility = false;
        transaction.currencyVisibility = false;
        transaction.amountVisibility = false;
        transaction.currencyVisibility = false;
        transaction.sourceIDVisibility = false;
        transaction.cvc2Visibility = false;
        transaction.receiptNumVisibility = false;
        transaction.paymentReasonVisibility = false;
        transaction.transPlaceVisibility = false;
        transaction.authorNumVisibility = false;
        transaction.originIndVisibility = false;
        transaction.passwordVisibility = false;
        transaction.userdataVisibility = false;
        transaction.langCodeVisibility = false;
        transaction.receiptLayoutVisibility = false;
        transaction.desCurrencyVisibility = false;
        transaction.txOriginVisibility = false;
        transaction.personalIDVisibility = false;
        transaction.name = "Reconciliation request";
        transaction.msgType = Transaction.MessageType.RECONCILIATION_REQUEST;

        repository.saveTransaction(transaction);
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
            }

            if(autoConnect && !isConnected() && !TID.equals("") && !hostname.equals("") && !port.equals("")) {
                connect();
            }
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

    private TransHistoryEntity convertTransaction(TransactionEntity trans) {
        TransHistoryEntity transHistoryEntity = new TransHistoryEntity();

        Log.e("TEST", "convertTransaction: \nID: " + transactionID + "\n terminalNum: " + trans.terminalNum + "\ndateTime: " + trans.dateTime);

        transHistoryEntity.name = trans.name;
        transHistoryEntity.ID = Long.valueOf(transactionID);
        transHistoryEntity.transID = transactionID;
        transHistoryEntity.terminalNum = TID;
        transHistoryEntity.msgType = trans.msgType;
        transHistoryEntity.dateTime = trans.dateTime;
        transHistoryEntity.sourceID = trans.sourceID;
        transHistoryEntity.cardNum = trans.cardNum;
        transHistoryEntity.cvc2 = trans.cvc2;
        transHistoryEntity.amount = trans.amount;
        transHistoryEntity.currency = trans.currency;
        transHistoryEntity.terminalNum = trans.terminalNum;
        transHistoryEntity.receiptNum = trans.receiptNum;
        transHistoryEntity.transPlace = trans.transPlace;
        transHistoryEntity.authorNum = trans.authorNum;
        transHistoryEntity.originInd = trans.originInd;
        transHistoryEntity.password = trans.password;
        transHistoryEntity.userdata = trans.userdata;
        transHistoryEntity.langCode = trans.langCode;
        transHistoryEntity.desCurrency = trans.desCurrency;
        transHistoryEntity.receiptLayout = trans.receiptLayout;
        transHistoryEntity.txOrigin = trans.txOrigin;
        transHistoryEntity.personalID = trans.personalID;

        return transHistoryEntity;
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

    private RespHistoryEntity convertResponse(Response response) {
        RespHistoryEntity respHistoryEntity = new RespHistoryEntity();

        respHistoryEntity.creditCardIssuer = response.creditCardIssuer;
        respHistoryEntity.cardNum = response.cardNum;
        respHistoryEntity.transactionType = response.transactionType;
        respHistoryEntity.responseText = response.responseText;
        respHistoryEntity.responseCode = response.responseCode;
        respHistoryEntity.authorNum = response.authorNum;
        respHistoryEntity.length = response.length;
        respHistoryEntity.ID = Long.valueOf(response.transID);
        respHistoryEntity.transID = response.transID;
        respHistoryEntity.msgType = response.msgType;
        respHistoryEntity.transactionDateTime = response.transactionDateTime;
        respHistoryEntity.VUNum = response.VUNum;
        respHistoryEntity.operatorID = response.operatorID;
        respHistoryEntity.serienNR = response.serienNR;
        respHistoryEntity.origTXID = response.origTXID;
        respHistoryEntity.stan = response.stan;
        respHistoryEntity.origStan = response.origStan;
        respHistoryEntity.svc = response.svc;
        respHistoryEntity.ecrData = response.ecrData;
        respHistoryEntity.exchangeRate = response.exchangeRate;
        respHistoryEntity.foreignTXAmount = response.foreignTXAmount;
        respHistoryEntity.balanceAmount = response.balanceAmount;
        respHistoryEntity.merchantName = response.merchantName;
        respHistoryEntity.merchantAddress = response.merchantAddress;
        respHistoryEntity.receiptHeader = response.receiptHeader;
        respHistoryEntity.receiptFooter = response.receiptFooter;
        respHistoryEntity.bonusPoints = response.bonusPoints;
        respHistoryEntity.exFee = response.exFee;

        return respHistoryEntity;
    }
}
