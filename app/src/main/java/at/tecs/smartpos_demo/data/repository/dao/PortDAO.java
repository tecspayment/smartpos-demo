package at.tecs.smartpos_demo.data.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

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