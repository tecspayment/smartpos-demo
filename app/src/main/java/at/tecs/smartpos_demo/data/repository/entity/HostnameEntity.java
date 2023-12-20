package at.tecs.smartpos_demo.data.repository.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class HostnameEntity {

    @NonNull
    @PrimaryKey
    public String hostname = "localhost";

    public HostnameEntity() {

    }

    @Ignore
    public HostnameEntity(String hostname) {
        this.hostname = hostname;
    }
}
