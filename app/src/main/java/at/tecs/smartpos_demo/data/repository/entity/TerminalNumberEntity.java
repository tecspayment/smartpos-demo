package at.tecs.smartpos_demo.data.repository.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

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
