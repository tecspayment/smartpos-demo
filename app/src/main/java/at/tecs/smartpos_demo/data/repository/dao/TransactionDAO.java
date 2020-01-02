package at.tecs.smartpos_demo.data.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;

@Dao
public interface TransactionDAO {

    @Query("SELECT * FROM TransactionEntity")
    List<TransactionEntity> getAllTransactions();

    @Query("DELETE FROM TransactionEntity WHERE name=:name")
    void deleteTransaction(String name);

    @Query("SELECT * FROM TransactionEntity WHERE name =:name")
    TransactionEntity getTransaction(String name);

    @Insert
    void insertTransaction(TransactionEntity transactionEntity);
}
