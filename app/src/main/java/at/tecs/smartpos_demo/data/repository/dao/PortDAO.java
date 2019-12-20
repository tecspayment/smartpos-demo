package at.tecs.smartpos_demo.data.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import at.tecs.smartpos_demo.data.repository.entity.PortEntity;

@Dao
public interface PortDAO {

    @Query("SELECT * FROM PortEntity")
    List<PortEntity> getAllPorts();

    @Query("DELETE FROM PortEntity WHERE port=:port")
    void deletePort(String port);

    @Insert
    void insertPort(PortEntity portEntity);
}