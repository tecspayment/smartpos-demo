package at.tecs.smartpos_demo.data.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
import at.tecs.smartpos_demo.data.repository.entity.HostnameEntity;

@Dao
public interface HostnameDAO {

    @Query("SELECT * FROM HostnameEntity")
    List<HostnameEntity> getAllHostnames();

    @Query("DELETE FROM HostnameEntity WHERE hostname=:name")
    void deleteHostname(String name);

    @Insert
    void insertHostname(HostnameEntity hostnameEntity);
}
