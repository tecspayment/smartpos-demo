package at.tecs.smartpos_demo.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.main.MainContract;

public class TemplatesFragment extends Fragment implements MainContract.View.TemplatesTab {

    private Callback.TemplatesTabCallback callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.templates_act, container, false);

        callback.onAttach(this);

        return view;
    }

    public void setTemplatesTabCallback(Callback.TemplatesTabCallback templatesTabCallback) {
        this.callback = templatesTabCallback;
    }
}
