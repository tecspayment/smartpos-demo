package at.tecs.smartpos_demo.data.repository.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class TransHistoryEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public Long ID;

    @ColumnInfo(name = "transID")
    public String transID;

    @ColumnInfo(name = "terminalNum")
    public String terminalNum;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "msgType")
    public String msgType;

    @ColumnInfo(name = "dateTime")
    public String dateTime;

    @ColumnInfo(name = "sourceID")
    public String sourceID;

    @ColumnInfo(name = "cardNum")
    public String cardNum;

    @ColumnInfo(name = "cvc2")
    public String cvc2;

    @ColumnInfo(name = "amount")
    public String amount;

    @ColumnInfo(name = "currency")
    public String currency;

    @ColumnInfo(name = "receiptNum")
    public String receiptNum;

    @ColumnInfo(name = "paymentReason")
    public String paymentReason;

    @ColumnInfo(name = "transPlace")
    public String transPlace;

    @ColumnInfo(name = "authorNum")
    public String authorNum;

    @ColumnInfo(name = "originInd")
    public String originInd;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "userdata")
    public String userdata;

    @ColumnInfo(name = "langCode")
    public String langCode;

    @ColumnInfo(name = "receiptLayout")
    public String receiptLayout;

    @ColumnInfo(name = "desCurrency")
    public String desCurrency;

    @ColumnInfo(name = "txOrigin")
    public String txOrigin;

    @ColumnInfo(name = "personalID")
    public String personalID;
}
