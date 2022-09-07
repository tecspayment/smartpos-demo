package at.tecs.smartpos_demo.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.format.Formatter;
import android.util.Log;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import at.tecs.smartpos.PaymentService;
import at.tecs.smartpos.SmartPOSController;
import at.tecs.smartpos.connector.ConnectionListener;
import at.tecs.smartpos.connector.ResponseListener;
import at.tecs.smartpos.data.PrinterPrintType;
import at.tecs.smartpos.data.PrinterReturnCode;
import at.tecs.smartpos.data.Response;
import at.tecs.smartpos.data.Transaction;
import at.tecs.smartpos.exception.TransactionFieldException;
import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.Repository;
import at.tecs.smartpos_demo.data.repository.entity.HostnameEntity;
import at.tecs.smartpos_demo.data.repository.entity.PortEntity;
import at.tecs.smartpos_demo.data.repository.entity.RespHistoryEntity;
import at.tecs.smartpos_demo.data.repository.entity.TerminalNumberEntity;
import at.tecs.smartpos_demo.data.repository.entity.TransHistoryEntity;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;

import static at.tecs.smartpos.data.ConnectionType.TCP;
import static at.tecs.smartpos_demo.Utils.showToast;
import static at.tecs.smartpos_demo.main.MainActivity.SERVICE_READY_TO_PAY;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainPresenter implements MainContract.Presenter {

    public final static String BLUETOOTH_CON = "Bluetooth";
    public final static String TCP_CON = "TCP/IP";
    public final static String SERIAL_CON = "Serial";
    private static MainContract.Presenter instance;
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
    private boolean showDialog = false;
    private boolean showAutoResponse = true;
    private int nataliStatus = -1;
    private Response lastResponse;
    private String uuid = "d1a82d8d-933e-4f6a-8c79-ee13c6d010e2";
    private String deviceAddress = "B4:36:A9:F9:78:BD";
    private PaymentService paymentService;
    private String connectionType = "TCP/IP";


    MainPresenter() {
        paymentService = new PaymentService();
    }

    public static MainContract.Presenter getInstance() {
        if(instance == null) {
            instance = new MainPresenter();
        }

        return instance;
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

        Context context = view.getContext().getApplicationContext();
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        view.showIP(ip);
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
                    } else {
                        view.showNotification("Connected to device!");
                    }

                    paymentService.listen(new ResponseListener() {
                        @Override
                        public void onResponseReceived(Response response) {     //Readed response
                            lastResponse = response;

                            if(!response.msgType.equals("5747")) { //Ignore notification messages
                                repository.saveResponse(convertResponse(response));
                            }

                            if(showAutoResponse) {
                                view.showResponseTab(Integer.parseInt(response.responseCode));
                            }

                            if(responseView != null) {
                                if(showDialog) {
                                    StringBuilder responseText = new StringBuilder();
                                    for(int i = response.responseText.length() - 1; i >= 0; i--) {
                                        if(response.responseText.charAt(i) != ' ') {
                                            responseText.append(response.responseText.substring(0, i + 1));
                                            break;
                                        }
                                    }

                                    int imageResource = -1;

                                    if(response.responseCode.equals("0000")) {
                                        imageResource = R.drawable.outline_thumb_up_white_48dp;
                                    } else {
                                        imageResource = R.drawable.outline_thumb_down_white_48dp;
                                    }

                                    view.showMessage("Transaction result", responseText.toString(), imageResource, 3 * 1000);
                                }

                                responseView.showResponse(response);
                            }

                            if(response.getScanData() != null && !response.getScanData().isEmpty()) {
                                view.showMessage("Scan result", response.getScanData(), R.drawable.outline_qr_code_white_48dp, -1);
                            }

                            /*
                            if(response.getReconciliationResponse() != null) {
                                printReconciliation(response);
                            } else {
                                Log.e("Reconciliation", "No reconciliation!");
                            }

                             */

                            if(response.getEndOfDayResponse() != null) {
                                printEndOfDay(response);
                            } else {
                                Log.e("End of Day", "No End of Day!");
                            }

                            Log.i("TEST", "Customer Receipt: " + response.getCustomerReceipt());
                            Log.i("TEST", "Merchant Receipt: " + response.getMerchantReceipt());

                            if((response.getCustomerReceipt() != null && !response.getCustomerReceipt().isEmpty()) ||
                                    (response.getMerchantReceipt() != null && !response.getMerchantReceipt().isEmpty())) {
                                receiptView.showReceipt(response.getMerchantReceipt(), response.getCustomerReceipt(), response.transactionDateTime);
                            }
                        }

                        @Override
                        public void onDisconnected() {
                            view.showDisconnected();
                        }

                        @Override
                        public void onReadFailed() {
                            view.showNotification("Read Failed!");
                            if(connectionType.equals(TCP_CON)) {
                                disconnect();
                            } else {
                                disconnect();
                                connect();
                            }
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

            showToast(view.getContext(),"Send - " + transactionEntity.name);

            Log.i("TEST", "Transaction: " + transactionEntity.name);
            Log.i("TEST", "Transaction: " + transactionEntity);
            if(paymentService.isConnected()) {
                repository.saveTransaction(convertTransaction(transactionEntity));
            } else {
                disconnect();

                view.showMessage("Error", "Please try again!", R.drawable.outline_warning_white_48dp, 3000);
            }
        } catch (TransactionFieldException e) {
            view.showMessage("Error", e.getMessage(), R.drawable.outline_warning_white_48dp, -1);
            e.printStackTrace();
        }
    }

    @Override
    public void setTID(String TID) {
        this.TID = TID;
    }

    @Override
    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    @Override
    public void setShowAutoResponse(boolean autoResponse) {
        this.showAutoResponse = autoResponse;
    }

    @Override
    public void setNataliStatus(int status) {
        nataliStatus = status;
    }

    @Override
    public boolean isAutoConnect() {
        return this.autoConnect;
    }

    @Override
    public void setAutoConnect(boolean autoConnect) {
        this.autoConnect = autoConnect;
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
    public void setHostname(String hostname) {
        if(hostname != null && !hostname.isEmpty()) {
            if(connectionType.equals(TCP_CON) && !paymentService.getHostname().equals(hostname)) {
                try {
                    paymentService.disconnect();
                    paymentService.setHostname(hostname);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Log.e("TEST", "Set hostname - " + hostname);

            this.hostname = hostname;
        }
    }

    @Override
    public String getPort() {
        return port;
    }

    @Override
    public void setPort(String port) {
        if(port != null && !port.isEmpty()) {
            if(connectionType.equals(TCP_CON) && !paymentService.getPort().equals(port)) {
                try {
                    paymentService.disconnect();
                    paymentService.setPort(Integer.parseInt(port));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Log.e("TEST", "Set port - " + port);

            this.port = port;
        }
    }

    @Override
    public String getTerminalNum() {
        return TID;
    }

    @Override
    public void abort() {
        if(connectionType.equals(TCP_CON)) {
            final PaymentService paymentService = new PaymentService();
            paymentService.setHostname(hostname);
            paymentService.setPort(Integer.parseInt(port));
        } else if(connectionType.equals(BLUETOOTH_CON)) {
            BluetoothConnection bluetoothConnection = new BluetoothConnection();
            bluetoothConnection.setUUID(UUID.fromString(uuid));
            bluetoothConnection.setDeviceAddress(deviceAddress);
            paymentService = new PaymentService(bluetoothConnection);
        } else if(connectionType.equals(SERIAL_CON)) {
            SerialConnection serialConnection = SerialConnection.getInstance(view.getContext());

            serialConnection.receiveTimeout = 70;
            serialConnection.setBaudRate(19200);
            serialConnection.setParity(0);
            serialConnection.setDataBits(8);
            serialConnection.setStopBit(1);
            paymentService = new PaymentService(serialConnection);
        }
        paymentService.connect(new ConnectionListener() {
            @Override
            public void onConnected() {
                try {
                    Transaction transaction = new Transaction();
                    transaction.ID = transactionID;
                    transaction.dateTime = dateTime;
                    transaction.terminalNum = TID;
                    transaction.msgType = "0017";
                    transaction.sourceID = "1";
                    transaction.amount = "1";
                    transaction.currency = "EUR";
                    transaction.receiptNum = "1";
                    transaction.originInd = "0";
                    paymentService.sendTransaction(transaction);
                } catch (TransactionFieldException e) {
                    e.printStackTrace();
                    view.showNotification("Abort failed!");
                }
            }

            @Override
            public void onUnknownHost(UnknownHostException e) {
                try {
                    paymentService.disconnect();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                view.showNotification("Abort failed!");
            }

            @Override
            public void onSocketFail(IOException e) {
                try {
                    paymentService.disconnect();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                view.showNotification("Abort failed!");
            }
        });
    }

    @Override
    public void saveUUID(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public void saveAddress(String address) {
        deviceAddress = address;
    }

    @Override
    public String getConnectionType() {
        return connectionType;
    }

    @Override
    public void setConnectionType(String connectionType) {
        if(!connectionType.equals(this.connectionType)) {
            this.connectionType = connectionType;

            if (connectionType.equals(TCP_CON)) {
                paymentService = new PaymentService();
            } else if (connectionType.equals(BLUETOOTH_CON)) {
                BluetoothConnection bluetoothConnection = new BluetoothConnection();
                bluetoothConnection.setUUID(UUID.fromString(uuid));
                bluetoothConnection.setDeviceAddress(deviceAddress);
                paymentService = new PaymentService(bluetoothConnection);
            } else if (connectionType.equals(SERIAL_CON)) {
                SerialConnection serialConnection = SerialConnection.getInstance(view.getContext());

                serialConnection.receiveTimeout = 70;
                serialConnection.setBaudRate(19200);
                serialConnection.setParity(0);
                serialConnection.setDataBits(8);
                serialConnection.setStopBit(1);

                paymentService = new PaymentService(serialConnection);
            }
        }
    }

    @Override
    public String getUUID() {
        return uuid;
    }

    @Override
    public String getAddress() {
        return deviceAddress;
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
        transaction.index = 0;
        transaction.amount = "1";
        transaction.currency = "EUR";
        transaction.sourceID = "1";
        transaction.receiptNum = "1";
        transaction.originInd = "0";
        transaction.langCode = "EN";
        transaction.receiptLayout = "1";
        transaction.desCurrency = "EUR";
        transaction.txOrigin = "1";

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
        transaction.index = 1;
        transaction.amount = "1";
        transaction.currency = "EUR";
        transaction.sourceID = "1";
        transaction.receiptNum = "1";
        transaction.originInd = "0";
        transaction.langCode = "EN";
        transaction.receiptLayout = "1";
        transaction.desCurrency = "EUR";
        transaction.txOrigin = "1";


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
        transaction.index = 2;
        transaction.amount = "1";
        transaction.currency = "EUR";
        transaction.sourceID = "1";
        transaction.cardNum = "TXID";
        transaction.receiptNum = "1";
        transaction.originInd = "2";
        transaction.langCode = "EN";
        transaction.receiptLayout = "1";
        transaction.desCurrency = "EUR";
        transaction.txOrigin = "2";


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
        transaction.index = 3;
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
        transaction.index = 4;
        transaction.msgType = Transaction.MessageType.RECONCILIATION_REQUEST;

        repository.saveTransaction(transaction);
    }

    @Override
    public void vibrate() {
        Vibrator vibrator = (Vibrator) view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }

    @Override
    public void printReconciliation(final Response response) {
        String reconciliationResponse = response.getReconciliationResponse();

        Log.e("Reconciliation", reconciliationResponse);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SmartPOSController smartPOSController = new SmartPOSController();
                int ret = smartPOSController.PrinterOpen();
                if (ret != PrinterReturnCode.SUCCESS.value) {
                    smartPOSController.PrinterClose();
                    return;
                }

                int status = smartPOSController.PrinterGetStatus();
                if (status != PrinterReturnCode.SUCCESS.value) {
                    smartPOSController.PrinterClose();
                    return;
                }

                ret = smartPOSController.PrinterPrint(response.getMerchantReceipt(), PrinterPrintType.TEXT.value);
                if (ret != PrinterReturnCode.SUCCESS.value) {
                    smartPOSController.PrinterClose();
                }

                ret = smartPOSController.PrinterFeedLine(8);
                if (ret != PrinterReturnCode.SUCCESS.value) {
                    smartPOSController.PrinterClose();
                }

            }
        }).start();

        /*
        final StringBuilder receipt = new StringBuilder();

        try {
            //@SuppressLint("SimpleDateFormat")
            //Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(response.transactionDateTime);

            String dayString = "";
            String monthString = "";
            String hoursString = "";
            String minutesString = "";
            String yearString = "";

            int day = 0;
            int month = 0;
            int hours = 0;
            int minutes = 0;
            int year = 0;


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                LocalDateTime parsedDate = LocalDateTime.parse(response.transactionDateTime, formatter);

                day = parsedDate.getDayOfMonth();
                month = parsedDate.getMonthValue();
                hours = parsedDate.getHour();
                minutes = parsedDate.getMinute();
                year = parsedDate.getYear();
            } else {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = formatter.parse(response.transactionDateTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH) + 1;
                hours = calendar.get(Calendar.HOUR_OF_DAY);
                minutes = calendar.get(Calendar.MINUTE);
                year = calendar.get(Calendar.YEAR);
            }

            dayString = String.valueOf(day);
            if (day < 10) {
                dayString = "0" + String.valueOf(day);
            }

            monthString = String.valueOf(month);
            if (month < 10) {
                monthString = "0" + String.valueOf(month);
            }

            hoursString = String.valueOf(hours);
            if (hours < 10) {
                hoursString = "0" + String.valueOf(hours);
            }

            minutesString = String.valueOf(minutes);
            if (minutes < 10) {
                minutesString = "0" + String.valueOf(minutes);
            }

            yearString = String.valueOf(year);
            yearString = yearString.substring(2);

            receipt.append("        RECONCILIATION\n\n\n");
            receipt.append("TID: " + TID + "                 \nDate: " + dayString + "/" + monthString + "/" + yearString + "    Time: " + hoursString + ":" + minutesString + "\n\n\n");

            JSONObject reconciliation = new JSONObject(reconciliationResponse);

            JSONArray cardtypeData = reconciliation.getJSONArray("cardtypeData");

            for (int i = 0; i < cardtypeData.length(); i++) {
                JSONObject data = cardtypeData.getJSONObject(i);
                if (!data.isNull("cardAID")) {
                    String cardAID = data.getString("cardAID");
                    receipt.append("AID: ").append(cardAID).append("\n");
                }

                String cardProviderName = data.getString("cardProviderName");
                receipt.append("Brand name: ").append(cardProviderName).append("\n");

                int numberOfDebit = data.getInt("numberOfDebit");
                receipt.append("Debit count: ").append(numberOfDebit).append("\n");

                double debitAmount = data.getDouble("debitAmount");
                receipt.append("Debit amount: ").append(debitAmount).append("\n");

                double tip = data.getDouble("tip");
                receipt.append("Tip amount: ").append(tip).append("\n");

                double debitTotal = data.getDouble("debitAmount");
                receipt.append("Debit total amount: ").append(debitTotal).append("\n");

                int numberOfCredit = data.getInt("numberOfCredit");
                receipt.append("Credit count: ").append(numberOfCredit).append("\n");

                int creditTotal = data.getInt("creditTotal");
                receipt.append("Credit total amount: ").append(creditTotal).append("\n");

                receipt.append("------------------------------------------\n\n");
            }

            receipt.append("=============================\nSummarized:\n");
            int numberOfDebitSum = reconciliation.getInt("numberOfDebitSum");
            receipt.append("Debit count: ").append(numberOfDebitSum).append("\n");

            double debitAmountSum = reconciliation.getDouble("debitAmountSum");
            receipt.append("Debit amount: ").append(debitAmountSum).append("\n");

            double tipSum = reconciliation.getDouble("tipSum");
            receipt.append("Tip amount: ").append(tipSum).append("\n");

            double debitSum = reconciliation.getDouble("debitSum");
            receipt.append("Debit total amount: ").append(debitSum).append("\n");

            int numberOfCreditSum = reconciliation.getInt("numberOfCreditSum");
            receipt.append("Credit count: ").append(numberOfCreditSum).append("\n");

            int creditAmountSum = reconciliation.getInt("creditAmountSum");
            receipt.append("Credit amount: ").append(creditAmountSum).append("\n");
            receipt.append("=============================\n");
            receipt.append("          " + response.responseText);

            Log.e("Reconciliation", receipt.toString());

            receiptView.showReceipt(receipt.toString(), "", response.transactionDateTime);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SmartPOSController smartPOSController = new SmartPOSController();
                    int ret = smartPOSController.PrinterOpen();
                    if (ret != PrinterReturnCode.SUCCESS.value) {
                        smartPOSController.PrinterClose();
                        return;
                    }

                    int status = smartPOSController.PrinterGetStatus();
                    if (status != PrinterReturnCode.SUCCESS.value) {
                        smartPOSController.PrinterClose();
                        return;
                    }

                    ret = smartPOSController.PrinterPrint(receipt.toString(), PrinterPrintType.TEXT.value);
                    if (ret != PrinterReturnCode.SUCCESS.value) {
                        smartPOSController.PrinterClose();
                    }

                    ret = smartPOSController.PrinterFeedLine(8);
                    if (ret != PrinterReturnCode.SUCCESS.value) {
                        smartPOSController.PrinterClose();
                    }

                }
            }).start();
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

         */
    }

    @Override
    public void printEndOfDay(Response response) {
        String endOfDayResponse = response.getEndOfDayResponse();

        Log.e("End of Day", endOfDayResponse);

        final StringBuilder receipt = new StringBuilder();

        String dayString = "";
        String monthString = "";
        String hoursString = "";
        String minutesString = "";
        String yearString = "";

        int day = 0;
        int month = 0;
        int hours = 0;
        int minutes = 0;
        int year = 0;

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                LocalDateTime parsedDate = LocalDateTime.parse(response.transactionDateTime, formatter);

                day = parsedDate.getDayOfMonth();
                month = parsedDate.getMonthValue();
                hours = parsedDate.getHour();
                minutes = parsedDate.getMinute();
                year = parsedDate.getYear();
            } else {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = formatter.parse(response.transactionDateTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH) + 1;
                hours = calendar.get(Calendar.HOUR_OF_DAY);
                minutes = calendar.get(Calendar.MINUTE);
                year = calendar.get(Calendar.YEAR);
            }

            dayString = String.valueOf(day);
            if (day < 10) {
                dayString = "0" + String.valueOf(day);
            }

            monthString = String.valueOf(month);
            if (month < 10) {
                monthString = "0" + String.valueOf(month);
            }

            hoursString = String.valueOf(hours);
            if (hours < 10) {
                hoursString = "0" + String.valueOf(hours);
            }

            minutesString = String.valueOf(minutes);
            if (minutes < 10) {
                minutesString = "0" + String.valueOf(minutes);
            }

            yearString = String.valueOf(year);
            yearString = yearString.substring(2);

            receipt.append("        END OF DAY\n\n\n");
            receipt.append("TID: " + TID + "                 \nDate: " + dayString + "/" + monthString + "/" + yearString + "    Time: " + hoursString + ":" + minutesString + "\n\n\n");

            JSONObject reconciliation = new JSONObject(endOfDayResponse);

            JSONArray cardtypeData = reconciliation.getJSONArray("cardtypeData");

            for (int i = 0; i < cardtypeData.length(); i++) {
                JSONObject data = cardtypeData.getJSONObject(i);
                if (!data.isNull("cardAID")) {
                    String cardAID = data.getString("cardAID");
                    receipt.append("AID: ").append(cardAID).append("\n");
                }

                String cardProviderName = data.getString("cardProviderName");
                receipt.append("Brand name: ").append(cardProviderName).append("\n");

                int numberOfDebit = data.getInt("numberOfDebit");
                receipt.append("Debit count: ").append(numberOfDebit).append("\n");

                double debitAmount = data.getDouble("debitAmount");
                receipt.append("Debit amount: ").append(debitAmount).append("\n");

                double tip = data.getDouble("tip");
                receipt.append("Tip amount: ").append(tip).append("\n");

                double debitTotal = data.getDouble("debitAmount");
                receipt.append("Debit total amount: ").append(debitTotal).append("\n");

                int numberOfCredit = data.getInt("numberOfCredit");
                receipt.append("Credit count: ").append(numberOfCredit).append("\n");

                int creditTotal = data.getInt("creditTotal");
                receipt.append("Credit total amount: ").append(creditTotal).append("\n");

                receipt.append("------------------------------------------\n\n");
            }

            Log.e("End of Day", receipt.toString());

            receiptView.showReceipt(receipt.toString(), "", response.transactionDateTime);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SmartPOSController smartPOSController = new SmartPOSController();
                    int ret = smartPOSController.PrinterOpen();
                    if (ret != PrinterReturnCode.SUCCESS.value) {
                        smartPOSController.PrinterClose();
                        return;
                    }

                    int status = smartPOSController.PrinterGetStatus();
                    if (status != PrinterReturnCode.SUCCESS.value) {
                        smartPOSController.PrinterClose();
                        return;
                    }

                    ret = smartPOSController.PrinterPrint(receipt.toString(), PrinterPrintType.TEXT.value);
                    if (ret != PrinterReturnCode.SUCCESS.value) {
                        smartPOSController.PrinterClose();
                    }

                    ret = smartPOSController.PrinterFeedLine(8);
                    if (ret != PrinterReturnCode.SUCCESS.value) {
                        smartPOSController.PrinterClose();
                    }

                }
            }).start();

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
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

        //Log.e("TEST", "convertTransaction: \nID: " + transactionID + "\n terminalNum: " + trans.terminalNum + "\ndateTime: " + trans.dateTime);

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

    private class Incrementer extends TimerTask {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
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

        @Override
        public void run() {
            if (alive) {
                Date date = new Date(System.currentTimeMillis());
                transactionID = formatter.format(date);
                dateTime = formatter.format(date);
            }

            if(autoConnect && !isConnected() && !TID.equals("") && !hostname.equals("") && !port.equals("") && nataliStatus == SERVICE_READY_TO_PAY) {
                connect();
            }
        }
    }
}
