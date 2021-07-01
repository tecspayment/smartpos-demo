package at.tecs.smartpos_demo.main.fragments;

import android.bluetooth.BluetoothDevice;
import android.widget.ArrayAdapter;

import java.util.Set;

import at.tecs.smartpos.data.ConnectionType;
import at.tecs.smartpos.data.Transaction;
import at.tecs.smartpos_demo.main.MainContract;

public interface Callback {

    interface ConnectionTabCallback {

        ConnectionType selectedConnection();
        void saveTerminalNumber(String terminalNum);
        void deleteTerminalNumber(String terminalNum);
        void saveHostname(String hostname);
        void deleteHostname(String hostname);
        void savePort(String port);
        void deletePort(String port);
        void selectTID(String TID);
        void selectHostname(String hostname);
        void selectPort(String port);
        void onAttach(MainContract.View.ConnectionTab view);
        void startScan();
        void stopScan();
        void selectDevice(BluetoothDevice bluetoothDevice);
        Set<BluetoothDevice> getPairedDevices();
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
        void deleteTransaction(String name);
        void startAutomatic(boolean start);
        void loadTransaction(String transactionID);
        void onAttach(MainContract.View.TransactionTab view);
        void performAliPayScan();
        void performAliPayQR();
    }

    interface CardTabCallback {
        void onAttach(MainContract.View.CardTab view);

        void performConnect();
        void performAuthenticateM0(String data);
        void performAuthenticateM1(String keyMode, String snr, String blockID, String key);
        void performReadBlock(String blockID);
        void performWriteBlock(String blockID, String data);
        void performTransmit(String data);
        void performTransmitReadWholeCard(String key, int start, int end);
    }

    interface PrintTabCallback {
        void onAttach(MainContract.View.PrintTab view);

        void performFeedLine(int linesCount);
        void performPrint(String dataToPrint, int dataType);
        void printFullReceipt();
    }
}
