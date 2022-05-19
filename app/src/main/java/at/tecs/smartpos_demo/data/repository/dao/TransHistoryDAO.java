package at.tecs.smartpos_demo.data.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import at.tecs.smartpos_demo.data.repository.entity.TransHistoryEntity;

@Dao
public interface TransHistoryDAO {

    @Query("SELECT * FROM TransHistoryEntity WHERE transID=:transID")
    TransHistoryEntity getTransactionHistory(Long transID);

    @Query("SELECT * FROM TransHistoryEntity")
    List<TransHistoryEntity> getAll();

    @Delete
    void removeTransaction(TransHistoryEntity entity);

    @Insert
    void insertTransactionHistory(TransHistoryEntity transHistoryEntity);
}
