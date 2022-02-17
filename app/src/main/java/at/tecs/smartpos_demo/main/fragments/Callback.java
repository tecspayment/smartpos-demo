package at.tecs.smartpos_demo.main.fragments;

import at.tecs.smartpos.data.Transaction;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;
import at.tecs.smartpos_demo.main.MainContract;

public interface Callback {

    interface ResponseTabCallback {
        void onAttach(MainContract.View.ResponseTab view);
    }

    interface TransactionTabCallback {
        void saveTransaction(Transaction transaction, String name);
        void deleteTransaction(String name);
        void startAutomatic(boolean start);
        void loadTransaction(String transactionID);
        void onAttach(MainContract.View.TransactionTab view);
        void performAliPayScan();
        void performAliPayQR();
    }

    interface TransactionsTabCallBack {
        void performTransaction(TransactionEntity transactionEntity);
    }

    interface ReceiptTabCallBack {
        void onAttach(MainContract.View.ReceiptTab view);
    }
}
