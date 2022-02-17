package at.tecs.smartpos_demo.data.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
import at.tecs.smartpos_demo.data.repository.entity.HostnameEntity;

@Dao
public interface HostnameDAO {

    @Query("SELECT * FROM HostnameEntity")
    List<HostnameEntity> getAllHostnames();

    @Delete
    void deleteHostname(HostnameEntity hostname);

    @Insert
    void insertHostname(HostnameEntity hostnameEntity);
}
