package at.tecs.smartpos_demo.data.repository.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import at.tecs.smartpos_demo.data.repository.entity.PortEntity;

@Dao
public interface PortDAO {

    @Query("SELECT * FROM PortEntity")
    List<PortEntity> getAllPorts();

    @Delete
    void deletePort(PortEntity port);

    @Insert
    void insertPort(PortEntity portEntity);
}