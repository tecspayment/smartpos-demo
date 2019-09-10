package at.tecs.smartpos_demo.templates;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import at.tecs.smartpos.PaymentService;
import at.tecs.smartpos_demo.R;

import static at.tecs.smartpos_demo.R.string.tid_;

public class TemplatesActivity extends AppCompatActivity {

    public final static String PAYMENT_SERVICE_KEY = "PAYMENT_SERVICE_KEY";
    public final static String TID_KEY = "TID_KEY";

    private PaymentService paymentService;

    private String TID;

    private TextInputEditText saleAmountInput;
    private TextInputEditText saleCurrencyInput;

    private TextInputEditText refundAmountInput;
    private TextInputEditText refundCurrencyInput;

    private TextInputEditText cancelTransIDInput;
    private TextInputEditText cancelAmountInput;
    private TextInputEditText cancelCurrencyInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.templates_act);

        paymentService = (PaymentService) getIntent().getSerializableExtra(PAYMENT_SERVICE_KEY);
        TID = getIntent().getStringExtra(TID_KEY);

        TextView TIDtextView = findViewById(R.id.TIDtextView);

        saleAmountInput = findViewById(R.id.saleAmountInput);
        saleCurrencyInput = findViewById(R.id.saleCurrencyInput);
        Button saleSendButton = findViewById(R.id.saleSendButton);

        refundAmountInput = findViewById(R.id.refundAmountInput);
        refundCurrencyInput = findViewById(R.id.refundCurrencyInput);
        Button refundSendButton = findViewById(R.id.refundSendButton);

        cancelTransIDInput = findViewById(R.id.cancelTransIDInput);
        cancelAmountInput = findViewById(R.id.cancelAmountInput);
        cancelCurrencyInput = findViewById(R.id.cancelCurrencyInput);
        Button cancelSendButton = findViewById(R.id.cancelSendButton);

        Button abortSendButton = findViewById(R.id.abortSendButton);

        TIDtextView.setText(String.format("%s%s", getString(tid_), TID));

        saleSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!saleAmountInput.getEditableText().toString().isEmpty() && !saleCurrencyInput.getEditableText().toString().isEmpty())
                    paymentService.sale(Integer.valueOf(TID), Integer.valueOf(saleAmountInput.getEditableText().toString()), saleCurrencyInput.getEditableText().toString());
            }
        });

        refundSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!refundAmountInput.getEditableText().toString().isEmpty() && !refundCurrencyInput.getEditableText().toString().isEmpty())
                    paymentService.refund(Integer.valueOf(TID), Integer.valueOf(refundAmountInput.getEditableText().toString()), refundCurrencyInput.getEditableText().toString());
            }
        });

        cancelSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cancelTransIDInput.getEditableText().toString().isEmpty() && !cancelAmountInput.getEditableText().toString().isEmpty() && !cancelCurrencyInput.getEditableText().toString().isEmpty())
                    paymentService.cancellation(Integer.valueOf(TID), cancelTransIDInput.getEditableText().toString(),  Integer.valueOf(cancelAmountInput.getEditableText().toString()), cancelCurrencyInput.getEditableText().toString());
            }
        });

        abortSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentService.abort();
            }
        });
    }
}
