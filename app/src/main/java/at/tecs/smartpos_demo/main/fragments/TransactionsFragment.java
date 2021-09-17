package at.tecs.smartpos_demo.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.tecs.smartpos_demo.R;

public class TransactionsFragment extends Fragment {

    private Callback.TransactionsTabCallBack callback;

    private RecyclerView transactionRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transactions_frag, container, false);

        transactionRecyclerView = view.findViewById(R.id.transactionRecyclerView);

        return view;
    }

    public void setCallback(Callback.TransactionsTabCallBack transactionsTabCallBack) {
        this.callback = transactionsTabCallBack;
    }
}
