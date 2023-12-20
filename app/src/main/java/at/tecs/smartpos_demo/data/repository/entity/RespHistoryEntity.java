package at.tecs.smartpos_demo.data.repository.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RespHistoryEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public Long ID;

    @ColumnInfo(name = "creditCardIssuer")
    public String creditCardIssuer;

    @ColumnInfo(name = "cardNum")
    public String cardNum;

    @ColumnInfo(name = "transactionType")
    public String transactionType;

    @ColumnInfo(name = "responseText")
    public String responseText;

    @ColumnInfo(name = "responseCode")
    public String responseCode;

    @ColumnInfo(name = "authorNum")
    public String authorNum;

    @ColumnInfo(name = "length")
    public String length;

    @ColumnInfo(name = "transID")
    public String transID;

    @ColumnInfo(name = "msgType")
    public String msgType;

    @ColumnInfo(name = "transactionDateTime")
    public String transactionDateTime;

    @ColumnInfo(name = "VUNum")
    public String VUNum;

    @ColumnInfo(name = "operatorID")
    public String operatorID;

    @ColumnInfo(name = "serienNR")
    public String serienNR;

    @ColumnInfo(name = "origTXID")
    public String origTXID;

    @ColumnInfo(name = "stan")
    public String stan;

    @ColumnInfo(name = "origStan")
    public String origStan;

    @ColumnInfo(name = "svc")
    public String svc;

    @ColumnInfo(name = "ecrData")
    public String ecrData;

    @ColumnInfo(name = "exchangeRate")
    public String exchangeRate;

    @ColumnInfo(name = "foreignTXAmount")
    public String foreignTXAmount;

    @ColumnInfo(name = "balanceAmount")
    public String balanceAmount;

    @ColumnInfo(name = "merchantName")
    public String merchantName;

    @ColumnInfo(name = "merchantAddress")
    public String merchantAddress;

    @ColumnInfo(name = "receiptHeader")
    public String receiptHeader;

    @ColumnInfo(name = "receiptFooter")
    public String receiptFooter;

    @ColumnInfo(name = "bonusPoints")
    public String bonusPoints;

    @ColumnInfo(name = "exFee")
    public String exFee;
}
