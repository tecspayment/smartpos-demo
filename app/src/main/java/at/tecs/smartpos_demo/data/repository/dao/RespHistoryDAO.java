package at.tecs.smartpos_demo.data.repository.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import at.tecs.smartpos_demo.data.repository.entity.RespHistoryEntity;

@Dao
public interface RespHistoryDAO {

    @Query("SELECT * FROM RespHistoryEntity WHERE ID=:ID")
    RespHistoryEntity getResponseHistory(Long ID);

    @Query("SELECT * FROM RespHistoryEntity")
    List<RespHistoryEntity> getAll();

    @Delete
    void removeResponse(RespHistoryEntity entity);

    @Insert
    void insertResponseHistory(RespHistoryEntity respHistoryEntity);
}
