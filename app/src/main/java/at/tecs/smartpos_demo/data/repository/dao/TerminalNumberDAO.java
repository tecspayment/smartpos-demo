package at.tecs.smartpos_demo.data.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import at.tecs.smartpos_demo.data.repository.entity.TerminalNumberEntity;


@Dao
public interface TerminalNumberDAO {

    @Query("SELECT * FROM TerminalNumberEntity")
    List<TerminalNumberEntity> getAllTerminalNums();

    @Insert
    void insertTerminalNum(TerminalNumberEntity terminalNumberEntity);
}
