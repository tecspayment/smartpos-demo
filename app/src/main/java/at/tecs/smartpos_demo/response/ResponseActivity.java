package at.tecs.smartpos_demo.response;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;


import at.tecs.smartpos.data.Response;
import at.tecs.smartpos_demo.R;

public class ResponseActivity extends AppCompatActivity {

    public static String RESPONSE_KEY = "RESPONSE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.response_act);

        Response response = (Response) getIntent().getSerializableExtra(RESPONSE_KEY);

        TextInputEditText creaditCardIssuer = findViewById(R.id.creaditCardIssuer);
        TextInputEditText cardNum = findViewById(R.id.cardNum);
        TextInputEditText transactionType = findViewById(R.id.transactionType);
        TextInputEditText responseText = findViewById(R.id.responseText);
        TextInputEditText responseCode = findViewById(R.id.responseCode);
        TextInputEditText authorNum = findViewById(R.id.authorNum);
        TextInputEditText length = findViewById(R.id.length);
        TextInputEditText transID = findViewById(R.id.transID);
        TextInputEditText msgType = findViewById(R.id.msgType);
        TextInputEditText transactionDateTime = findViewById(R.id.transactionDateTime);
        TextInputEditText VUNum = findViewById(R.id.VUNum);
        TextInputEditText operatorID = findViewById(R.id.operatorID);
        TextInputEditText serienNR = findViewById(R.id.serienNR);
        TextInputEditText origTXID = findViewById(R.id.origTXID);
        TextInputEditText stan = findViewById(R.id.stan);
        TextInputEditText origStan = findViewById(R.id.origStan);
        TextInputEditText svc = findViewById(R.id.svc);
        TextInputEditText ecrData = findViewById(R.id.ecrData);
        TextInputEditText exchangeRate = findViewById(R.id.exchangeRate);
        TextInputEditText foreignTXAmount = findViewById(R.id.foreignTXAmount);
        TextInputEditText balanceAmount = findViewById(R.id.balanceAmount);
        TextInputEditText merchantName = findViewById(R.id.merchantName);
        TextInputEditText merchantAddress = findViewById(R.id.merchantAddress);
        TextInputEditText receiptHeader = findViewById(R.id.receiptHeader);
        TextInputEditText receiptFooter = findViewById(R.id.receiptFooter);
        TextInputEditText bonusPoints = findViewById(R.id.bonusPoints);
        TextInputEditText exFee = findViewById(R.id.exFee);

        creaditCardIssuer.setText(response.creditCardIssuer);
        cardNum.setText(response.cardNum);
        transactionType.setText(response.transactionType);
        responseText.setText(response.responseText);
        responseCode.setText(response.responseCode);
        authorNum.setText(response.authorNum);
        length.setText(response.length);
        transID.setText(response.transID);
        msgType.setText(response.msgType);
        transactionDateTime.setText(response.transactionDateTime);
        VUNum.setText(response.VUNum);
        operatorID.setText(response.operatorID);
        serienNR.setText(response.serienNR);
        origTXID.setText(response.origTXID);
        stan.setText(response.stan);
        origStan.setText(response.origStan);
        svc.setText(response.svc);
        ecrData.setText(response.ecrData);
        exchangeRate.setText(response.exchangeRate);
        foreignTXAmount.setText(response.foreignTXAmount);
        balanceAmount.setText(response.balanceAmount);
        merchantName.setText(response.merchantName);
        merchantAddress.setText(response.merchantAddress);
        receiptHeader.setText(response.receiptHeader);
        receiptFooter.setText(response.receiptFooter);
        bonusPoints.setText(response.bonusPoints);
        exFee.setText(response.exFee);

    }
}
