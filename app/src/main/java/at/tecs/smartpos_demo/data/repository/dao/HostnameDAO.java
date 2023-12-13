package at.tecs.smartpos_demo.data.repository.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

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
