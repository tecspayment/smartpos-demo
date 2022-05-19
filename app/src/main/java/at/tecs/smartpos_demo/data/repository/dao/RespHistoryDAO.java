package at.tecs.smartpos_demo.data.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import at.tecs.smartpos_demo.data.repository.entity.RespHistoryEntity;
import at.tecs.smartpos_demo.data.repository.entity.TerminalNumberEntity;
import at.tecs.smartpos_demo.data.repository.entity.TransHistoryEntity;

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
