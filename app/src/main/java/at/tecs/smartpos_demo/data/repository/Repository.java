package at.tecs.smartpos_demo.data.repository;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import at.tecs.smartpos_demo.data.repository.entity.HostnameEntity;
import at.tecs.smartpos_demo.data.repository.entity.PortEntity;
import at.tecs.smartpos_demo.data.repository.entity.TerminalNumberEntity;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;


public class Repository {

    private static volatile Database database;

    public Repository(Context context) {
        getDatabase(context);
        prepareTemplates();
    }

    private static void getDatabase(Context context) {
        if(database == null) {
            synchronized (Database.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context, Database.class, "local_db").allowMainThreadQueries().build();
                }
            }
        }
    }

    private void prepareTemplates() {
        if(getTransaction("Sale") == null) {
            TransactionEntity sale = new TransactionEntity();
            sale.name = "Sale";
            sale.msgType = "1";
            sale.sourceID = "1";
            sale.amount = "1";
            sale.currency = "EUR";
            sale.receiptNum = "1";
            sale.paymentReason = "EMV transaction test";
            sale.transPlace = "Test Place";
            sale.originInd = "0";

            sale.langCode = "EN";
            sale.receiptLayout = "1";
            sale.desCurrency = "EUR";
            sale.txOrigin = "1";

            saveTransaction(sale);
        }

        if(getTransaction("Refund") == null) {
            TransactionEntity refund = new TransactionEntity();
            refund.name = "Refund";
            refund.msgType = "11";
            refund.sourceID = "1";
            refund.amount = "1";
            refund.currency = "EUR";
            refund.receiptNum = "1";
            refund.paymentReason = "EMV transaction test";
            refund.transPlace = "Test Place";
            refund.originInd = "0";

            refund.langCode = "EN";
            refund.receiptLayout = "1";
            refund.desCurrency = "EUR";
            refund.txOrigin = "1";

            saveTransaction(refund);
        }

        if(getTransaction("Refund - no Pin Pad") == null) {
            TransactionEntity refund = new TransactionEntity();
            refund.name = "Refund - no Pin Pad";
            refund.msgType = "11";
            refund.sourceID = "1";
            refund.amount = "1";
            refund.currency = "EUR";
            refund.receiptNum = "1";
            refund.paymentReason = "EMV transaction test";
            refund.transPlace = "Test Place";
            refund.originInd = "2";

            refund.langCode = "EN";
            refund.receiptLayout = "1";
            refund.desCurrency = "EUR";
            refund.txOrigin = "1";

            saveTransaction(refund);
        }

        if(getTransaction("Cancellation") == null) {
            TransactionEntity cancellation = new TransactionEntity();
            cancellation.name = "Cancellation";
            cancellation.cardNum = "TXID";
            cancellation.msgType = "13";
            cancellation.sourceID = "1";
            cancellation.amount = "1";
            cancellation.currency = "EUR";
            cancellation.receiptNum = "1";
            cancellation.paymentReason = "EMV transaction test";
            cancellation.transPlace = "Test Place";
            cancellation.originInd = "2";

            cancellation.langCode = "EN";
            cancellation.receiptLayout = "1";
            cancellation.desCurrency = "EUR";
            cancellation.txOrigin = "2";

            saveTransaction(cancellation);
        }

        if(getTransaction("PreAuth") == null) {
            TransactionEntity preAuth = new TransactionEntity();
            preAuth.name = "PreAuth";
            preAuth.msgType = "1";
            preAuth.sourceID = "1";
            preAuth.amount = "1";
            preAuth.currency = "EUR";
            preAuth.receiptNum = "1";
            preAuth.paymentReason = "EMV transaction test";
            preAuth.transPlace = "Test Place";
            preAuth.originInd = "0";

            preAuth.langCode = "EN";
            preAuth.receiptLayout = "1";
            preAuth.desCurrency = "EUR";
            preAuth.txOrigin = "5";

            saveTransaction(preAuth);
        }

        if(getTransaction("PreAuth Completion") == null) {
            TransactionEntity preAuth = new TransactionEntity();
            preAuth.name = "PreAuth Completion";
            preAuth.msgType = "13";
            preAuth.sourceID = "1";
            preAuth.amount = "1";
            preAuth.currency = "EUR";
            preAuth.receiptNum = "1";
            preAuth.paymentReason = "EMV transaction test";
            preAuth.transPlace = "Test Place";
            preAuth.originInd = "2";

            preAuth.langCode = "EN";
            preAuth.receiptLayout = "1";
            preAuth.desCurrency = "EUR";
            preAuth.txOrigin = "4";

            saveTransaction(preAuth);
        }

        if(getTransaction("Abort") == null) {
            TransactionEntity abort = new TransactionEntity();
            abort.name = "Abort";
            abort.msgType = "17";
            abort.sourceID = "1";
            abort.amount = "1";
            abort.currency = "EUR";
            abort.receiptNum = "1";
            abort.paymentReason = "EMV transaction test";
            abort.transPlace = "Test Place";
            abort.originInd = "0";

            abort.langCode = "EN";
            abort.receiptLayout = "1";
            abort.desCurrency = "EUR";
            abort.txOrigin = "1";

            saveTransaction(abort);
        }

        if(getTransaction("Moto") == null) {
            TransactionEntity moto = new TransactionEntity();
            moto.name = "Moto";
            moto.msgType = "1";
            moto.sourceID = "1";
            moto.amount = "1";
            moto.currency = "EUR";
            moto.receiptNum = "1";
            moto.paymentReason = "EMV transaction test";
            moto.transPlace = "Test Place";
            moto.originInd = "0";

            moto.langCode = "EN";
            moto.receiptLayout = "1";
            moto.desCurrency = "EUR";
            moto.txOrigin = "2";

            saveTransaction(moto);
        }

        if(getTransaction("AliPay QR") == null) {
            TransactionEntity sale = new TransactionEntity();
            sale.name = "AliPay QR";
            sale.msgType = "30";
            sale.sourceID = "1";
            sale.amount = "1";
            sale.currency = "EUR";
            sale.receiptNum = "1";
            sale.paymentReason = "EMV transaction test";
            sale.transPlace = "Test Place";
            sale.originInd = "0";

            sale.langCode = "EN";
            sale.receiptLayout = "1";
            sale.desCurrency = "EUR";
            sale.txOrigin = "1";

            saveTransaction(sale);
        }

        if(getTransaction("AliPay Scan") == null) {
            TransactionEntity sale = new TransactionEntity();
            sale.name = "AliPay Scan";
            sale.msgType = "31";
            sale.sourceID = "1";
            sale.amount = "1";
            sale.currency = "EUR";
            sale.receiptNum = "1";
            sale.paymentReason = "EMV transaction test";
            sale.transPlace = "Test Place";
            sale.originInd = "0";

            sale.langCode = "EN";
            sale.receiptLayout = "1";
            sale.desCurrency = "EUR";
            sale.txOrigin = "1";

            saveTransaction(sale);
        }

        if(getTransaction("Connection Status") == null) {
            TransactionEntity connection = new TransactionEntity();
            connection.name = "Connection Status";
            connection.msgType = "2667";

            saveTransaction(connection);
        }

        if(getTransaction("Kill Natali") == null) {
            TransactionEntity kill = new TransactionEntity();
            kill.name = "Kill Natali";
            kill.msgType = "4544";

            saveTransaction(kill);
        }

        if(getHostname("localhost") == null) {
            HostnameEntity hostname = new HostnameEntity();
            hostname.hostname = "localhost";

            saveHostname(hostname);
        }

        if(getPort("9990") == null) {
            PortEntity portEntity = new PortEntity();
            portEntity.port = "9990";

            savePort(portEntity);
        }

    }

    public void saveTransaction(TransactionEntity transactionEntity) {
        database.transactionDAO().insertTransaction(transactionEntity);
    }

    public void deleteTransation(String name) {
        database.transactionDAO().deleteTransaction(name);
    }

    public void savePort(PortEntity portEntity) {
        database.portDAO().insertPort(portEntity);
    }

    public void deletePort(PortEntity portEntity) {
        database.portDAO().deletePort(portEntity.port);
    }

    public void saveTerminalNum(TerminalNumberEntity terminalNumberEntity) {
        database.terminalNumberDAO().insertTerminalNum(terminalNumberEntity);
    }

    public void deleteTerminalNum(TerminalNumberEntity terminalNumberEntity) {
        database.terminalNumberDAO().deleteTerminalNum(terminalNumberEntity.termNum);
    }

    public void saveHostname(HostnameEntity hostnameEntity) {
        database.hostnameDAO().insertHostname(hostnameEntity);
    }

    public void deleteHostname(HostnameEntity hostnameEntity) {
        database.hostnameDAO().deleteHostname(hostnameEntity.hostname);
    }

    public TransactionEntity getTransaction(String name) {
        return database.transactionDAO().getTransaction(name);
    }

    public ArrayList<String> getTransactionsNames() {
        ArrayList<String> names = new ArrayList<>();

        List<TransactionEntity> transactionEntities = database.transactionDAO().getAllTransactions();

        for (TransactionEntity transactionEntity : transactionEntities) {
            names.add(transactionEntity.name);
        }

        if(names.get(names.size() - 1).equals("Last Transaction")) {
            names.add(0, names.get(names.size() - 1));
            names.remove(names.size() - 1);
        }

        return names;
    }

    public ArrayList<String> getTerminalNumbers() {
        ArrayList<String> names = new ArrayList<>();

        List<TerminalNumberEntity> terminalNumberEntities = database.terminalNumberDAO().getAllTerminalNums();

        for (TerminalNumberEntity terminalNumberEntity :
                terminalNumberEntities) {
            names.add(terminalNumberEntity.termNum);
        }

        return names;
    }

    public ArrayList<String> getPorts() {
        ArrayList<String> names = new ArrayList<>();

        List<PortEntity> portEntities = database.portDAO().getAllPorts();
        for (PortEntity portEntity :
                portEntities) {
            names.add(portEntity.port);
        }

        return names;
    }

    public ArrayList<String> getHostnames() {
        ArrayList<String> names = new ArrayList<>();

        List<HostnameEntity> hostnameEntities = database.hostnameDAO().getAllHostnames();
        for (HostnameEntity hostnameEntity :
                hostnameEntities) {
            names.add(hostnameEntity.hostname);
        }

        return names;
    }

    private String getHostname(String hostname) {

        List<HostnameEntity> hostnameEntities = database.hostnameDAO().getAllHostnames();
        for (HostnameEntity hostnameEntity :
                hostnameEntities) {
            if(hostname.equals(hostnameEntity.hostname))
                return hostnameEntity.hostname;
        }

        return null;
    }

    private String getPort(String port) {

        ArrayList<String> names = new ArrayList<>();

        List<PortEntity> portEntities = database.portDAO().getAllPorts();
        for (PortEntity portEntity :
                portEntities) {
            if(portEntity.port.equals(port)) {
                return portEntity.port;
            }
        }

        return null;
    }

}
