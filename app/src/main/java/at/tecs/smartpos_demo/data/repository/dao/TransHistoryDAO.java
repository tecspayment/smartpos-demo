package at.tecs.smartpos_demo.data.repository.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

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
