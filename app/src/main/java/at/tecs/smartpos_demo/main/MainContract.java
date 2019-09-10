package at.tecs.smartpos_demo.main;

import android.content.Context;
import android.widget.ArrayAdapter;


import at.tecs.smartpos.PaymentService;
import at.tecs.smartpos.data.Response;
import at.tecs.smartpos.data.Transaction;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;


public interface MainContract {

    interface View {

        void showTransactionAuto(final String transactionID, final String dateTime);
        void showTransaction(TransactionEntity transactionEntity);
        void showResponse(Response response);

        void showConnected();
        void showDisconnected();

        void showToast(String msg);

        void setTransactionAdapter(ArrayAdapter<String> transactionAdapter);
        void setTerminalNumAdapter(ArrayAdapter<String> terminalNumAdapter);
        void setHostnameAdapter(ArrayAdapter<String> hostnameAdapter);
        void setPortAdapter(ArrayAdapter<String> portAdapter);

        void showNataliStatus(int status);

        void showTemplates();

        Context getContext();
    }

    interface Presenter {

        void start();
        void initialize();
        void takeView(MainContract.View view);
        void saveTransaction(Transaction transaction);
        void saveTermNum(String terminalNum);
        void saveHostName(String hostname);
        void savePort(String port);

        void setHostname(String hostname);
        void setPort(String port);

        boolean isConnected();

        void startAutomatic(boolean automatic);
        void connect();
        void disconnect();
        void send(Transaction transaction);
        void loadTransaction(String name);

        int startNatali(Context context);

        Response getLastResponse();
        PaymentService getPaymentService();
    }
}
