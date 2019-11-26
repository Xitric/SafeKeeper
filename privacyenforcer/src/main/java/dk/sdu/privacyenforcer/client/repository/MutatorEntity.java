package dk.sdu.privacyenforcer.client.repository;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MutatorEntity {

    @PrimaryKey
    @NonNull
    public String mid;

    @ColumnInfo(name = "type")
    public String type;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
