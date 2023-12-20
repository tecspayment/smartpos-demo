package at.tecs.smartpos_demo.data.repository.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

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
