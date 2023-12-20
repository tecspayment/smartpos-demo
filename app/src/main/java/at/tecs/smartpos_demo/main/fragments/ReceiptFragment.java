package at.tecs.smartpos_demo.main.fragments;

import static at.tecs.smartpos_demo.Utils.showToast;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;


import java.io.File;
import java.io.FileOutputStream;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.main.MainContract;

public class ReceiptFragment extends Fragment implements MainContract.View.ReceiptTab {

    private ScrollView receiptContainer;
    private TextView receiptTextView;
    private TextView messageText1;
    private TextView messageText2;
    private TextView instructionTextView;

    private Callback.ReceiptTabCallBack callback;

    private String merchantReceipt = "";
    private String customerReceipt = "";
    private String datetime = "";

    private boolean merchantVisible = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receipt_frag, container, false);

        receiptContainer = view.findViewById(R.id.receiptContainer);
        receiptTextView = view.findViewById(R.id.receiptTextView);
        messageText1 = view.findViewById(R.id.messageText1);
        messageText2 = view.findViewById(R.id.messageText2);

        instructionTextView = view.findViewById(R.id.instructionTextView);

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

        receiptTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String root = Environment.getExternalStorageDirectory().toString();

                String fileName;
                if(merchantVisible) {
                    fileName = root + "/merchant_receipt_" + datetime + ".txt";
                } else {
                    fileName = root + "/customer_receipt_" + datetime + ".txt";
                }

                File receiptFile = new File(fileName);

                try {

                    FileOutputStream out = new FileOutputStream(receiptFile);
                    if(merchantVisible) {
                        out.write(merchantReceipt.getBytes());
                    } else {
                        out.write(customerReceipt.getBytes());
                    }
                    out.flush();
                    out.close();

                    if(merchantVisible) {
                        showToast(getContext(), "Merchant receipt has been saved!");
                    } else {
                        showToast(getContext(), "Customer receipt has been saved!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }
        });

        return view;
    }

    @Override
    public void showReceipt(final String merchantReceipt,final String customerReceipt, String datetime) {

        this.merchantReceipt = merchantReceipt;
        this.customerReceipt = customerReceipt;
        this.datetime = datetime;

        Activity activity = getActivity();

        if(activity != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageText1.setVisibility(View.INVISIBLE);
                    messageText2.setVisibility(View.INVISIBLE);

                    instructionTextView.setVisibility(View.VISIBLE);
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

                    instructionTextView.setVisibility(View.GONE);
                    receiptContainer.setVisibility(View.GONE);
                    receiptTextView.setText("");
                }
            });
    }

    public void setCallback(Callback.ReceiptTabCallBack receiptTabCallback) {
        this.callback = receiptTabCallback;
    }
}
