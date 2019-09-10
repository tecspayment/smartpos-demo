package at.tecs.smartpos_demo.data.repository.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

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
