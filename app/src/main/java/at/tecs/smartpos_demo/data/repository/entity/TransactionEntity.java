package at.tecs.smartpos_demo.data.repository.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class TransactionEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public String terminalNum;

    public boolean extended;

    public boolean expanded = false;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "msgType")
    public String msgType;

    @ColumnInfo(name = "sourceID")
    public String sourceID;

    @ColumnInfo(name = "sourceIDVisibility")
    public Boolean sourceIDVisibility;

    @ColumnInfo(name = "cardNum")
    public String cardNum;

    @ColumnInfo(name = "cardNumVisibility")
    public Boolean cardNumVisibility;

    @ColumnInfo(name = "cvc2")
    public String cvc2;

    @ColumnInfo(name = "cvc2Visibility")
    public Boolean cvc2Visibility;

    @ColumnInfo(name = "amount")
    public String amount;

    @ColumnInfo(name = "amountVisibility")
    public Boolean amountVisibility;

    @ColumnInfo(name = "currency")
    public String currency;

    @ColumnInfo(name = "currencyVisibility")
    public Boolean currencyVisibility;

    @ColumnInfo(name = "receiptNum")
    public String receiptNum;

    @ColumnInfo(name = "receiptNumVisibility")
    public Boolean receiptNumVisibility;

    @ColumnInfo(name = "paymentReason")
    public String paymentReason;

    @ColumnInfo(name = "paymentReasonVisibility")
    public Boolean paymentReasonVisibility;

    @ColumnInfo(name = "transPlace")
    public String transPlace;

    @ColumnInfo(name = "transPlaceVisibility")
    public Boolean transPlaceVisibility;

    @ColumnInfo(name = "authorNum")
    public String authorNum;

    @ColumnInfo(name = "authorNumVisibility")
    public Boolean authorNumVisibility;

    @ColumnInfo(name = "originInd")
    public String originInd;

    @ColumnInfo(name = "originIndVisibility")
    public Boolean originIndVisibility;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "passwordVisibility")
    public Boolean passwordVisibility;

    @ColumnInfo(name = "userdata")
    public String userdata;

    @ColumnInfo(name = "userdataVisibility")
    public Boolean userdataVisibility;

    @ColumnInfo(name = "langCode")
    public String langCode;

    @ColumnInfo(name = "langCodeVisibility")
    public Boolean langCodeVisibility;

    @ColumnInfo(name = "receiptLayout")
    public String receiptLayout;

    @ColumnInfo(name = "receiptLayoutVisibility")
    public Boolean receiptLayoutVisibility;

    @ColumnInfo(name = "desCurrency")
    public String desCurrency;

    @ColumnInfo(name = "desCurrencyVisibility")
    public Boolean desCurrencyVisibility;

    @ColumnInfo(name = "txOrigin")
    public String txOrigin;

    @ColumnInfo(name = "txOriginVisibility")
    public Boolean txOriginVisibility;

    @ColumnInfo(name = "personalID")
    public String personalID;

    @ColumnInfo(name = "personalIDVisibility")
    public Boolean personalIDVisibility;

    public String dateTime;

    public TransactionEntity() {

    }

    @NonNull
    @Override
    public String toString() {
        return "Transaction entity: \n" +
                "Name: " + name + "\n" +
                "ID: " + id + "\n" +
                "TID: " + terminalNum + "\n" +
                "msgType: " + msgType + "\n" +
                "sourceID: " + sourceID + "\n" +
                "cardNum: " + cardNum + "\n" +
                "cvc2: " + cvc2 + "\n" +
                "amount: " + amount + "\n" +
                "currency: " + currency + "\n";
    }
}
