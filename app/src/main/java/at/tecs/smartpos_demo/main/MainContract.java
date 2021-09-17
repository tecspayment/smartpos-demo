package at.tecs.smartpos_demo.main;

import android.content.Context;
import android.widget.ArrayAdapter;


import at.tecs.smartpos.PaymentService;
import at.tecs.smartpos.data.Response;
import at.tecs.smartpos.data.Transaction;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;

public interface MainContract {

    interface View {

        void showResponseTab(int code);

        void showLog(String log);

        void showConnected();

        void showDisconnected();

        void showToast(String msg);

        void showNataliStatus(int status);

        Context getContext();

        interface ConnectionTab {

            void setTerminalNumAdapter(ArrayAdapter<String> terminalNumAdapter);

            void setHostnameAdapter(ArrayAdapter<String> hostnameAdapter);

            void setPortAdapter(ArrayAdapter<String> portAdapter);

            boolean checkConnectionInputs();

            void showTCP();

            //void showBluetooth();
        }

        interface TransactionTab {

            void setTransactionAdapter(ArrayAdapter<String> transactionAdapter);

            Transaction createTransaction();

            void showTransaction(TransactionEntity transactionEntity);

            void showTransactionAuto(final String transactionID, final String dateTime);

            boolean checkTransactionInputs();
        }

        interface ResponseTab {
            void showResponse(Response response);
            void clearResponse();
        }

        interface ReceiptTab {
            void showReceipt(String receipt);
            void clearReceipt();
        }

        /*
        interface CardTab {
            void showResponse(String text);
            void changeOpen(String text);
        }
        */
    }

    interface Presenter {

        void start();

        void initialize();

        void takeView(MainContract.View view);

        void saveTransaction(Transaction transaction, String name);

        void deleteTransaction(String name);

        void saveTermNum(String terminalNum);

        void deleteTermNum(String terminalNum);

        void saveHostName(String hostname);

        void deleteHostName(String hostname);

        void savePort(String port);

        void deletePort(String port);

        void setHostname(String hostname);

        void setPort(String port);

        void setTID(String TID);

        boolean isConnected();

        void startAutomatic(boolean automatic);

        void connect();

        void disconnect();

        void send();

        //void selectConnection(String type);

        //void setBluetoothDevice(BluetoothDevice bluetoothDevice);

        void startScan();

        void stopScan();

        //Set<BluetoothDevice> getPairedDevices();

        void sale(String amount, String currency);
        void refund(String amount, String currecy);
        void cancellation(String transID, String amount, String currency);
        void abort();

        void loadTransaction(String name);

        //int startNatali(Context context);

        Response getLastResponse();

        PaymentService getPaymentService();

        void takeConnectionView(MainContract.View.ConnectionTab view);

        void takeTransactionView(MainContract.View.TransactionTab view);

        void takeResponseView(MainContract.View.ResponseTab view);

        //void takeCardView(MainContract.View.CardTab view);

        //void takePrintView(MainContract.View.PrintTab view);

        /*
        void openCardControl();

        void authenticateM0CardControl(String data);

        void authenticateM1CardControl(String keyMode, String snr, String blockID, String key);

        void readCardControl(String blockID);

        void writeCardControl(String blockID, String data);

        void transmitCardControl(String data);

        void transmitCardControlReadAll(String key, int start, int end);

        void printerPrint(String data, int dataType);

        void printerFeedLine(int linesCount);

        void printerFullReceipt();

        ConnectionType getSelected();
        */

    }
}
