package at.tecs.smartpos_demo.data.repository;

import android.arch.persistence.room.RoomDatabase;

import at.tecs.smartpos_demo.data.repository.dao.HostnameDAO;
import at.tecs.smartpos_demo.data.repository.dao.PortDAO;
import at.tecs.smartpos_demo.data.repository.dao.RespHistoryDAO;
import at.tecs.smartpos_demo.data.repository.dao.TerminalNumberDAO;
import at.tecs.smartpos_demo.data.repository.dao.TransHistoryDAO;
import at.tecs.smartpos_demo.data.repository.dao.TransactionDAO;
import at.tecs.smartpos_demo.data.repository.entity.HostnameEntity;
import at.tecs.smartpos_demo.data.repository.entity.PortEntity;
import at.tecs.smartpos_demo.data.repository.entity.RespHistoryEntity;
import at.tecs.smartpos_demo.data.repository.entity.TerminalNumberEntity;
import at.tecs.smartpos_demo.data.repository.entity.TransHistoryEntity;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;


@android.arch.persistence.room.Database(entities = {HostnameEntity.class, PortEntity.class, TerminalNumberEntity.class, TransactionEntity.class, TransHistoryEntity.class, RespHistoryEntity.class}, version = 3, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract HostnameDAO hostnameDAO();
    public abstract PortDAO portDAO();
    public abstract TerminalNumberDAO terminalNumberDAO();
    public abstract TransactionDAO transactionDAO();
    public abstract TransHistoryDAO transHistoryDAO();
    public abstract RespHistoryDAO respHistoryDAO();

}
