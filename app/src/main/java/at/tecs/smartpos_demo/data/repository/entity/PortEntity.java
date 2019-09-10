package at.tecs.smartpos_demo.data.repository.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class PortEntity {

    @NonNull
    @PrimaryKey
    public String port = "9990";

    public PortEntity() {

    }

    @Ignore
    public PortEntity(String port) {
        this.port = port;
    }
}
