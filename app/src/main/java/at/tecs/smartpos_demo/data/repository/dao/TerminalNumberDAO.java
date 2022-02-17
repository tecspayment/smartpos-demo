package at.tecs.smartpos_demo.data.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import at.tecs.smartpos_demo.data.repository.entity.TerminalNumberEntity;


@Dao
public interface TerminalNumberDAO {

    @Query("SELECT * FROM TerminalNumberEntity")
    List<TerminalNumberEntity> getAllTerminalNums();

    @Delete
    void deleteTerminalNum(TerminalNumberEntity termNum);

    @Insert
    void insertTerminalNum(TerminalNumberEntity terminalNumberEntity);
}
