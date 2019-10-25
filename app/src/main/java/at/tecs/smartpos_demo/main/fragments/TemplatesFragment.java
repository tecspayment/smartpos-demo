package at.tecs.smartpos_demo.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.main.MainContract;

public class TemplatesFragment extends Fragment implements MainContract.View.TemplatesTab {

    private TextInputEditText saleAmountInput;
    private TextInputEditText saleCurrencyInput;

    private TextInputEditText refundAmountInput;
    private TextInputEditText refundCurrencyInput;

    private TextInputEditText cancelTransIDInput;
    private TextInputEditText cancelAmountInput;
    private TextInputEditText cancelCurrencyInput;

    private Callback.TemplatesTabCallback callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.templates_act, container, false);

        saleAmountInput = view.findViewById(R.id.saleAmountInput);
        saleCurrencyInput = view.findViewById(R.id.saleCurrencyInput);
        Button saleSendButton = view.findViewById(R.id.saleSendButton);

        refundAmountInput = view.findViewById(R.id.refundAmountInput);
        refundCurrencyInput = view.findViewById(R.id.refundCurrencyInput);
        Button refundSendButton = view.findViewById(R.id.refundSendButton);

        cancelTransIDInput = view.findViewById(R.id.cancelTransIDInput);
        cancelAmountInput = view.findViewById(R.id.cancelAmountInput);
        cancelCurrencyInput = view.findViewById(R.id.cancelCurrencyInput);
        final Button cancelSendButton = view.findViewById(R.id.cancelSendButton);

        Button abortSendButton = view.findViewById(R.id.abortSendButton);

        saleSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!saleAmountInput.getEditableText().toString().isEmpty() && !saleCurrencyInput.getEditableText().toString().isEmpty())
                    callback.performSale(saleAmountInput.getEditableText().toString(),saleCurrencyInput.getEditableText().toString());
            }
        });

        refundSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!refundAmountInput.getEditableText().toString().isEmpty() && !refundCurrencyInput.getEditableText().toString().isEmpty())
                    callback.performRefund(refundAmountInput.getEditableText().toString(),refundCurrencyInput.getEditableText().toString());
            }
        });

        cancelSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cancelTransIDInput.getEditableText().toString().isEmpty() && !cancelAmountInput.getEditableText().toString().isEmpty() && !cancelCurrencyInput.getEditableText().toString().isEmpty())
                    callback.performCancellation(cancelTransIDInput.getEditableText().toString(),cancelAmountInput.getEditableText().toString(),cancelCurrencyInput.getEditableText().toString());
            }
        });

        abortSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.performAbort();
            }
        });

        callback.onAttach(this);

        return view;
    }

    public void setTemplatesTabCallback(Callback.TemplatesTabCallback templatesTabCallback) {
        this.callback = templatesTabCallback;
    }
}
