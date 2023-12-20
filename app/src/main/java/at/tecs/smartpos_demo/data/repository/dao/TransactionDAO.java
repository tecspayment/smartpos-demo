package at.tecs.smartpos_demo.data.repository.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;

@Dao
public interface TransactionDAO {

    @Query("SELECT * FROM TransactionEntity")
    List<TransactionEntity> getAllTransactions();

    @Delete
    void deleteTransaction(TransactionEntity transactionEntity);

    @Query("SELECT * FROM TransactionEntity WHERE id =:id")
    TransactionEntity getTransaction(String id);

    @Update
    void updateTransaction(TransactionEntity transactionEntity);

    @Insert
    void insertTransaction(TransactionEntity transactionEntity);
}
