package at.tecs.smartpos_demo.main;

import android.content.Context;
import android.widget.ArrayAdapter;


import at.tecs.smartpos.PaymentService;
import at.tecs.smartpos.data.Response;
import at.tecs.smartpos.data.Transaction;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;

public interface MainContract {

    interface View {

        void showIP(String IP);

        void showResponseTab(int code);

        void showTab(int position);

        void showConnected();

        void showDisconnected();

        void showNotification(String msg);

        void showMessage(String title, String text, int imageResource, int timeout);

        Context getContext();

        void showTID(String tid);

        void showHostname(String hostname);

        void showPort(String port);

        interface TransactionTab {
            Transaction createTransaction();

            void showTransaction(TransactionEntity transactionEntity);
        }

        interface ResponseTab {
            void showResponse(Response response);
            void clearResponse();
        }

        interface ReceiptTab {
            void showReceipt(String merchantReceipt, String customerReceipt);
            void clearReceipt();
        }
    }

    interface Presenter {

        void start();

        void initialize();

        void takeView(MainContract.View view);

        void setHostname(String hostname);

        void setPort(String port);

        void setTID(String TID);

        void setAutoConnect(boolean autoConnect);

        void setShowDialog(boolean showDialog);

        void setShowAutoResponse(boolean autoResponse);

        boolean isAutoConnect();

        boolean isConnected();

        void startAutomatic(boolean automatic);

        void connect();

        void disconnect();

        void send(TransactionEntity transactionEntity);

        void saveTermNum(String terminalNum);

        void deleteTermNum(String terminalNum);

        void saveHostName(String hostname);

        void deleteHostName(String hostname);

        void savePort(String port);

        void deletePort(String port);

        Response getLastResponse();

        PaymentService getPaymentService();

        void takeResponseView(MainContract.View.ResponseTab view);
        void takeReceiptView(MainContract.View.ReceiptTab view);

        void loadDefaults();

        String getHostname();
        String getPort();
        String getTerminalNum();
    }
}
