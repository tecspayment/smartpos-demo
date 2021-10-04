package at.tecs.smartpos_demo.data.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;

@Dao
public interface TransactionDAO {

    @Query("SELECT * FROM TransactionEntity")
    List<TransactionEntity> getAllTransactions();

    @Query("DELETE FROM TransactionEntity WHERE id=:id")
    void deleteTransaction(String id);

    @Query("SELECT * FROM TransactionEntity WHERE id =:id")
    TransactionEntity getTransaction(String id);

    @Update
    void updateTransaction(TransactionEntity transactionEntity);

    @Insert
    void insertTransaction(TransactionEntity transactionEntity);
}
