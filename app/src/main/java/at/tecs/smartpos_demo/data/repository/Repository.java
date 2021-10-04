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

    private static volatile Repository instance;
    private static volatile Database database;

    private Repository(Context context) {
        getDatabase(context);
    }

    public static Repository getInstance(Context context) {
        if(instance == null) {
            synchronized (Repository.class) {
                if (instance == null) {
                    instance = new Repository(context);
                }
            }
        }

        return instance;
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

    public ArrayList<TransactionEntity> getAllTransactions() {
        return (ArrayList<TransactionEntity>) database.transactionDAO().getAllTransactions();
    }

    public void updateTransaction(TransactionEntity transactionEntity) {
        database.transactionDAO().updateTransaction(transactionEntity);
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
