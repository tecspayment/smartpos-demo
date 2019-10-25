package at.tecs.smartpos_demo.main.fragments;

import at.tecs.smartpos.data.Transaction;
import at.tecs.smartpos_demo.main.MainContract;

public interface Callback {

    interface ConnectionTabCallback {
        void saveTerminalNumber(String terminalNum);
        void saveHostname(String hostname);
        void savePort(String port);
        void selectTID(String TID);
        void selectHostname(String hostname);
        void selectPort(String port);
        void onAttach(MainContract.View.ConnectionTab view);
    }

    interface ResponseTabCallback {
        void onAttach(MainContract.View.ResponseTab view);
    }

    interface TemplatesTabCallback {
        void onAttach(MainContract.View.TemplatesTab view);

        void performSale(String amount, String currency);
        void performRefund(String amount, String currecy);
        void performCancellation(String transID, String amount, String currency);
        void performAbort();
    }

    interface TransactionTabCallback {
        void saveTransaction(Transaction transaction, String name);
        void startAutomatic(boolean start);
        void loadTransaction(String transactionID);
        void onAttach(MainContract.View.TransactionTab view);
    }
}
