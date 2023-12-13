package at.tecs.smartpos_demo.data.repository.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

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
