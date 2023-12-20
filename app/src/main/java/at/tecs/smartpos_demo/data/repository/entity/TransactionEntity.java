package at.tecs.smartpos_demo.data.repository.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.io.Serializable;

@Entity
public class TransactionEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public String terminalNum;

    public boolean extended;

    public boolean expanded = false;

    @ColumnInfo(name = "index")
    public Integer index;

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
                "sourceID: " + sourceID + " - visbility: " + sourceIDVisibility + "\n" +
                "cardNum: " + cardNum + " - visbility: " + cardNumVisibility + "\n" +
                "cvc2: " + cvc2 + " - visbility: " + cvc2Visibility + "\n" +
                "amount: " + amount + " - visbility: " + amountVisibility + "\n" +
                "currency: " + currency + " - visbility: " + currencyVisibility + "\n" +
                "receiptNum: " + receiptNum + " - visbility: " + receiptNumVisibility + "\n" +
                "paymentReason: " + paymentReason + " - visbility: " + paymentReasonVisibility + "\n" +
                "transPlace: " + transPlace + " - visbility: " + transPlaceVisibility + "\n" +
                "authorNum: " + authorNum + " - visbility: " + authorNumVisibility + "\n" +
                "originInd: " + originInd + " - visbility: " + originIndVisibility + "\n" +
                "password: " + password + " - visbility: " + passwordVisibility + "\n" +
                "userdata: " + userdata + " - visbility: " + userdataVisibility + "\n" +
                "langCode: " + langCode + " - visbility: " + langCodeVisibility + "\n" +
                "receiptLayout: " + receiptLayout + " - visbility: " + receiptLayoutVisibility + "\n" +
                "desCurrency: " + desCurrency + " - visbility: " + desCurrencyVisibility + "\n" +
                "txOrigin: " + txOrigin + " - visbility: " + txOriginVisibility + "\n" +
                "personalID: " + personalID + " - visbility: " + personalIDVisibility + "\n";

    }
}
