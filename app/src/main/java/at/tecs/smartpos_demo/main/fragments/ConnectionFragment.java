package at.tecs.smartpos_demo.main.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.main.MainContract;

import static android.view.View.GONE;

public class ConnectionFragment extends Fragment implements MainContract.View.ConnectionTab {

    private Spinner terminalSpinner;
    private Spinner hostnameSpinner;
    private Spinner portSpinner;

    private Button terminalSave;
    private TextInputEditText terminalInput;
    private TextInputLayout termInputLayout;

    private Button hostnameSave;
    private TextInputEditText ipInput;
    private TextInputLayout IPInputLayout;

    private Button portSave;
    private TextInputEditText portInput;
    private TextInputLayout portInputLayout;

    private Callback.ConnectionTabCallback callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.connection_frag, container, false);

        terminalSpinner = view.findViewById(R.id.terminalSpinner2);
        hostnameSpinner = view.findViewById(R.id.hostnameSpinner2);
        portSpinner = view.findViewById(R.id.portSpinner3);

        terminalSave = view.findViewById(R.id.terminalSave2);
        terminalInput = view.findViewById(R.id.terminalInput);
        termInputLayout = view.findViewById(R.id.termInputLayout2);

        hostnameSave = view.findViewById(R.id.hostnameSave2);
        ipInput = view.findViewById(R.id.ipInput);
        IPInputLayout = view.findViewById(R.id.IPInputLayout);

        portSave = view.findViewById(R.id.portSave2);
        portInput = view.findViewById(R.id.portInput);
        portInputLayout = view.findViewById(R.id.portInputLayout);

        callback.onAttach(this);

        terminalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(terminalSpinner.getSelectedItem() != null)
                    callback.selectTID(terminalSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        hostnameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                callback.selectHostname(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        portSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                callback.selectPort(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        terminalSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                terminalSave.setVisibility(View.GONE);
                termInputLayout.setVisibility(View.GONE);

                if (terminalInput.getText() != null && !terminalInput.getText().toString().isEmpty()) {
                    callback.saveTerminalNumber(terminalInput.getEditableText().toString());
                    terminalInput.getText().clear();
                }
            }
        });

        final Button terminalAdd = view.findViewById(R.id.terminalAdd2);

        terminalAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                termInputLayout.setVisibility(View.VISIBLE);
                terminalSave.setVisibility(View.VISIBLE);

            }
        });

        hostnameSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IPInputLayout.setVisibility(GONE);
                hostnameSave.setVisibility(GONE);

                if(ipInput.getText() != null && !ipInput.getText().toString().isEmpty()) {
                    callback.saveHostname(ipInput.getEditableText().toString());
                    ipInput.getText().clear();
                }

            }
        });

        final Button hostnameAdd = view.findViewById(R.id.hostnameAdd2);

        hostnameAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IPInputLayout.setVisibility(View.VISIBLE);
                hostnameSave.setVisibility(View.VISIBLE);

            }
        });

        portSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                portSave.setVisibility(GONE);
                portInputLayout.setVisibility(GONE);

                if(portInput.getText() != null && !portInput.getText().toString().isEmpty()) {
                    callback.savePort(portInput.getEditableText().toString());
                    portInput.getText().clear();
                }

            }
        });

        final Button portAdd = view.findViewById(R.id.portAdd2);

        portAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                portSave.setVisibility(View.VISIBLE);
                portInputLayout.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void setTerminalNumAdapter(ArrayAdapter<String> terminalNumAdapter) {
        terminalSpinner.setAdapter(terminalNumAdapter);
    }

    @Override
    public void setHostnameAdapter(ArrayAdapter<String> hostnameAdapter) {
        hostnameSpinner.setAdapter(hostnameAdapter);
    }

    @Override
    public void setPortAdapter(ArrayAdapter<String> portAdapter) {
        portSpinner.setAdapter(portAdapter);
    }

    @Override
    public boolean checkConnectionInputs() {
        String errorMsg = "";

        if (terminalSpinner.getSelectedItem() == null) {
            errorMsg += "Terminal number is empty ! ";
        }
        if (hostnameSpinner.getSelectedItem() == null) {
            errorMsg += "HostnameEntity is not set ! ";
        }
        if (portSpinner.getSelectedItem() == null) {
            errorMsg += "PortEntity is not set ! ";
        }

        if (errorMsg.isEmpty()) {
            return true;
        } else {
            Toast toast = Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
    }

    public void setCallback(Callback.ConnectionTabCallback callback) {
        this.callback = callback;
    }

}
