package at.tecs.smartpos_demo.data.repository.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class TerminalNumberEntity {

    @NonNull
    @PrimaryKey
    public String termNum = "-1";

    public TerminalNumberEntity() {

    }

    @Ignore
    public TerminalNumberEntity(String termNum) {
        this.termNum = termNum;
    }
}
