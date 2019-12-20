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

    private Button terminalAdd;
    private Button terminalSave;
    private Button terminalDelete;
    private TextInputEditText terminalInput;
    private TextInputLayout termInputLayout;

    private Button hostnameAdd;
    private Button hostnameSave;
    private Button hostnameDelete;
    private TextInputEditText ipInput;
    private TextInputLayout IPInputLayout;

    private Button portAdd;
    private Button portSave;
    private Button portDelete;
    private TextInputEditText portInput;
    private TextInputLayout portInputLayout;

    private Callback.ConnectionTabCallback callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.connection_frag, container, false);

        terminalSpinner = view.findViewById(R.id.terminalSpinner);
        hostnameSpinner = view.findViewById(R.id.hostnameSpinner);
        portSpinner = view.findViewById(R.id.portSpinner);

        terminalSave = view.findViewById(R.id.terminalSave);
        terminalDelete = view.findViewById(R.id.terminalDelete);
        terminalInput = view.findViewById(R.id.terminalInput);
        termInputLayout = view.findViewById(R.id.termInputLayout);

        hostnameSave = view.findViewById(R.id.hostnameSave);
        hostnameDelete = view.findViewById(R.id.hostnameDelete);
        ipInput = view.findViewById(R.id.ipInput);
        IPInputLayout = view.findViewById(R.id.IPInputLayout);

        portSave = view.findViewById(R.id.portSave);
        portDelete = view.findViewById(R.id.portDelete);
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

        terminalDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminalSave.setVisibility(View.GONE);
                termInputLayout.setVisibility(View.GONE);
                terminalDelete.setVisibility(View.GONE);

                if(terminalSpinner.getSelectedItem()!= null && !terminalSpinner.getSelectedItem().toString().isEmpty()) {
                    callback.deleteTerminalNumber(terminalSpinner.getSelectedItem().toString());
                }
            }
        });

        terminalSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                terminalSave.setVisibility(View.GONE);
                termInputLayout.setVisibility(View.GONE);
                terminalDelete.setVisibility(View.GONE);

                if (terminalInput.getText() != null && !terminalInput.getText().toString().isEmpty()) {
                    callback.saveTerminalNumber(terminalInput.getEditableText().toString());
                    terminalInput.getText().clear();
                }
            }
        });

        terminalAdd = view.findViewById(R.id.terminalAdd);

        terminalAdd.setOnClickListener(showTerminalEdit);

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

        hostnameDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IPInputLayout.setVisibility(GONE);
                hostnameSave.setVisibility(GONE);
                hostnameDelete.setVisibility(GONE);

                if(hostnameSpinner.getSelectedItem() != null && !hostnameSpinner.getSelectedItem().toString().isEmpty()) {
                    callback.deleteHostname(hostnameSpinner.getSelectedItem().toString());
                }
            }
        });

        hostnameAdd = view.findViewById(R.id.hostnameAdd);

        hostnameAdd.setOnClickListener(showHostnamesEdit);

        portSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                portSave.setVisibility(GONE);
                portInputLayout.setVisibility(GONE);
                portDelete.setVisibility(GONE);

                if(portInput.getText() != null && !portInput.getText().toString().isEmpty()) {
                    callback.savePort(portInput.getEditableText().toString());
                    portInput.getText().clear();
                }

            }
        });

        portDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                portSave.setVisibility(GONE);
                portInputLayout.setVisibility(GONE);
                portDelete.setVisibility(GONE);

                if(portSpinner.getSelectedItem() != null && !portSpinner.getSelectedItem().toString().isEmpty()) {
                    callback.deletePort(portSpinner.getSelectedItem().toString());
                }
            }
        });

        portAdd = view.findViewById(R.id.portAdd);

        portAdd.setOnClickListener(showPortEdit);

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

    private View.OnClickListener showHostnamesEdit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IPInputLayout.setVisibility(View.VISIBLE);
            hostnameSave.setVisibility(View.VISIBLE);
            hostnameDelete.setVisibility(View.VISIBLE);

            hostnameAdd.setText(getString(R.string.cancel));

            hostnameAdd.setOnClickListener(cancelHostnamesEdit);
        }
    };

    private View.OnClickListener cancelHostnamesEdit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IPInputLayout.setVisibility(View.GONE);
            hostnameSave.setVisibility(View.GONE);
            hostnameDelete.setVisibility(View.GONE);

            hostnameAdd.setText(getString(R.string.add));

            hostnameAdd.setOnClickListener(showHostnamesEdit);
        }
    };

    private View.OnClickListener showPortEdit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            portInputLayout.setVisibility(View.VISIBLE);
            portSave.setVisibility(View.VISIBLE);
            portDelete.setVisibility(View.VISIBLE);

            portAdd.setText(getString(R.string.cancel));

            portAdd.setOnClickListener(cancelPortEdit);
        }
    };

    private View.OnClickListener cancelPortEdit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            portInputLayout.setVisibility(View.GONE);
            portSave.setVisibility(View.GONE);
            portDelete.setVisibility(View.GONE);

            portAdd.setText(getString(R.string.add));

            portAdd.setOnClickListener(showPortEdit);
        }
    };

    private View.OnClickListener showTerminalEdit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            termInputLayout.setVisibility(View.VISIBLE);
            terminalSave.setVisibility(View.VISIBLE);
            terminalDelete.setVisibility(View.VISIBLE);

            terminalAdd.setText(getString(R.string.cancel));

            terminalAdd.setOnClickListener(cancelTerminalEdit);
        }
    };

    private View.OnClickListener cancelTerminalEdit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            termInputLayout.setVisibility(View.GONE);
            terminalSave.setVisibility(View.GONE);
            terminalDelete.setVisibility(View.GONE);

            terminalAdd.setText(getString(R.string.add));

            terminalAdd.setOnClickListener(showTerminalEdit);
        }
    };

}
