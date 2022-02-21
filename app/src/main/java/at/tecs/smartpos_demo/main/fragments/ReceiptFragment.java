package at.tecs.smartpos_demo.main.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.main.MainContract;

public class ReceiptFragment extends Fragment implements MainContract.View.ReceiptTab {

    private ConstraintLayout receiptContainer;
    private TextView receiptTextView;
    private TextView messageText1;
    private TextView messageText2;

    private Callback.ReceiptTabCallBack callback;

    private  String merchantReceipt = "";
    private  String customerReceipt = "";

    private boolean merchantVisible = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipt_frag, container, false);

        receiptContainer = view.findViewById(R.id.receiptContainer);
        receiptTextView = view.findViewById(R.id.receiptTextView);
        messageText1 = view.findViewById(R.id.messageText1);
        messageText2 = view.findViewById(R.id.messageText2);

        if(callback != null) {
            callback.onAttach(this);
        }

        receiptTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!merchantVisible) {
                    receiptTextView.setText(merchantReceipt);
                    merchantVisible = true;
                } else {
                    receiptTextView.setText(customerReceipt);
                    merchantVisible = false;
                }
            }
        });

        return view;
    }

    @Override
    public void showReceipt(final String merchantReceipt,final String customerReceipt) {

        this.merchantReceipt = merchantReceipt;
        this.customerReceipt = customerReceipt;

        Activity activity = getActivity();

        if(activity != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageText1.setVisibility(View.INVISIBLE);
                    messageText2.setVisibility(View.INVISIBLE);

                    receiptContainer.setVisibility(View.VISIBLE);
                    receiptTextView.setText(merchantReceipt);
                    merchantVisible = true;
                }
            });
    }

    @Override
    public void clearReceipt() {
        Activity activity = getActivity();

        if(activity != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageText1.setVisibility(View.VISIBLE);
                    messageText2.setVisibility(View.VISIBLE);

                    receiptContainer.setVisibility(View.GONE);
                    receiptTextView.setText("");
                }
            });
    }

    public void setCallback(Callback.ReceiptTabCallBack receiptTabCallback) {
        this.callback = receiptTabCallback;
    }
}
