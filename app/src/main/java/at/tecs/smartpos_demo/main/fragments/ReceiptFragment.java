package at.tecs.smartpos_demo.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.main.MainContract;

public class ReceiptFragment extends Fragment implements MainContract.View.ReceiptTab {

    private ConstraintLayout receiptContainer;
    private TextView receiptTextView;

    private Callback.ReceiptTabCallBack callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipt_frag, container, false);

        receiptContainer = view.findViewById(R.id.receiptContainer);
        receiptTextView = view.findViewById(R.id.receiptTextView);

        if(callback != null) {
            callback.onAttach(this);
        }

        return view;
    }

    @Override
    public void showReceipt(String merchantReceipt, String customerReceipt) {
        receiptContainer.setVisibility(View.VISIBLE);
        receiptTextView.setText(merchantReceipt);
    }

    @Override
    public void clearReceipt() {
        receiptContainer.setVisibility(View.GONE);
        receiptTextView.setText("");
    }

    public void setCallback(Callback.ReceiptTabCallBack receiptTabCallback) {
        this.callback = receiptTabCallback;
    }
}
