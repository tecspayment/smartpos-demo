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

        void showConnected();

        void showDisconnected();

        void showMessage(String msg);

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
            void showReceipt(String receipt);
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

        void loadTransaction(String name);

        Response getLastResponse();

        PaymentService getPaymentService();

        void takeResponseView(MainContract.View.ResponseTab view);

        void loadDefaults();

        String getHostname();
        String getPort();
        String getTerminalNum();
    }
}
