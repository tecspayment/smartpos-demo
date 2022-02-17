package at.tecs.smartpos_demo.tx_history.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.entity.TransHistoryEntity;

public class TransHistoryFragment extends Fragment {

    private TransHistoryEntity entity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_frag, container, false);

        return view;
    }

    public void setTransEntity(TransHistoryEntity entity) {
        this.entity = entity;
    }
}
