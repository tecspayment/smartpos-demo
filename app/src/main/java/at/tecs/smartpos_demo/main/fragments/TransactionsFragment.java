package at.tecs.smartpos_demo.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.Repository;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;
import at.tecs.smartpos_demo.main.adapter.TransactionAdapter;
import at.tecs.smartpos_demo.tx_settings.TransactionSettingsActivity;

public class TransactionsFragment extends Fragment {

    private Callback.TransactionsTabCallBack callback;
    private TransactionAdapter transactionAdapter;

    @Override
    public void onResume() {
        super.onResume();
        if(transactionAdapter != null) {
            ArrayList<TransactionEntity> transactionEntities = Repository.getInstance(getContext()).getAllTransactions();
            transactionAdapter.setTransaction(transactionEntities);
            transactionAdapter.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transactions_frag, container, false);

        RecyclerView transactionRecyclerView = view.findViewById(R.id.transactionRecyclerView);
        ImageButton addButton = view.findViewById(R.id.addButton);
        ImageButton abortButton = view.findViewById(R.id.abortButton);

        ArrayList<TransactionEntity> transactionEntities = Repository.getInstance(getContext()).getAllTransactions();

        transactionAdapter = new TransactionAdapter(transactionEntities, callback, getContext());
        transactionRecyclerView.setAdapter(transactionAdapter);
        transactionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getContext() != null) {
                    Intent intent = new Intent(getContext(), TransactionSettingsActivity.class);
                    if(getActivity() != null) {
                        getActivity().startActivity(intent);
                    }
                }
            }
        });

        abortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.performAbort();
            }
        });

        return view;
    }

    public void setCallback(Callback.TransactionsTabCallBack transactionsTabCallBack) {
        this.callback = transactionsTabCallBack;
    }
}