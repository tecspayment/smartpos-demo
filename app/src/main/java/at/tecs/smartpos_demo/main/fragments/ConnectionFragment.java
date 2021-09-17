package at.tecs.smartpos_demo.main.fragments;

import android.bluetooth.BluetoothDevice;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import at.tecs.smartpos.data.ConnectionType;
import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.main.MainContract;
import at.tecs.smartpos_demo.main.MainPresenter;

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

    private TextView hostnameTextView;
    private Button hostnameAdd;
    private Button hostnameSave;
    private Button hostnameDelete;
    private TextInputEditText ipInput;
    private TextInputLayout IPInputLayout;

    private TextView portTextView;
    private Button portAdd;
    private Button portSave;
    private Button portDelete;
    private TextInputEditText portInput;
    private TextInputLayout portInputLayout;

    private Spinner devicesSpinner;
    private Button scanButton;

    private Callback.ConnectionTabCallback callback;

    private ArrayList<BluetoothDevice> pairedDevices = new ArrayList<>();

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

        hostnameTextView = view.findViewById(R.id.hostnameTextView);
        hostnameSave = view.findViewById(R.id.hostnameSave);
        hostnameDelete = view.findViewById(R.id.hostnameDelete);
        ipInput = view.findViewById(R.id.ipInput);
        IPInputLayout = view.findViewById(R.id.IPInputLayout);

        portTextView = view.findViewById(R.id.portTextView);
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

                terminalAdd.setText(getString(R.string.add));

                terminalAdd.setOnClickListener(showTerminalEdit);
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

                terminalAdd.setText(getString(R.string.add));

                terminalAdd.setOnClickListener(showTerminalEdit);
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

                hostnameAdd.setText(getString(R.string.add));

                hostnameAdd.setOnClickListener(showHostnamesEdit);
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

                hostnameAdd.setText(getString(R.string.add));

                hostnameAdd.setOnClickListener(showHostnamesEdit);
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

                portAdd.setText(getString(R.string.add));

                portAdd.setOnClickListener(showPortEdit);
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

                portAdd.setText(getString(R.string.add));

                portAdd.setOnClickListener(showPortEdit);
            }
        });

        portAdd = view.findViewById(R.id.portAdd);

        portAdd.setOnClickListener(showPortEdit);

        devicesSpinner = view.findViewById(R.id.devicesSpinner);
        scanButton = view.findViewById(R.id.scanButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshPairedDevices();
            }
        });

        refreshPairedDevices();

        devicesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    callback.selectDevice(pairedDevices.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                callback.selectDevice(pairedDevices.get(0));
            }
        });

        if(callback.selectedConnection() == ConnectionType.BLUETOOTH) {
            //showBluetooth();
        } else {
            showTCP();
        }

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

    @Override
    public void showTCP() {
        portTextView.setVisibility(View.VISIBLE);
        portSpinner.setVisibility(View.VISIBLE);
        portAdd.setVisibility(View.VISIBLE);

        hostnameTextView.setVisibility(View.VISIBLE);
        hostnameSpinner.setVisibility(View.VISIBLE);
        hostnameAdd.setVisibility(View.VISIBLE);

        scanButton.setVisibility(GONE);
        devicesSpinner.setVisibility(GONE);
    }

    /*
    @Override
    public void showBluetooth() {
        portTextView.setVisibility(GONE);
        portSpinner.setVisibility(GONE);
        portAdd.setVisibility(GONE);

        hostnameTextView.setVisibility(GONE);
        hostnameSpinner.setVisibility(GONE);
        hostnameAdd.setVisibility(GONE);

        scanButton.setVisibility(View.VISIBLE);
        devicesSpinner.setVisibility(View.VISIBLE);

        pairedDevices = new ArrayList<>(callback.getPairedDevices());

        for (BluetoothDevice device : callback.getPairedDevices()) {
            callback.selectDevice(device);
        }
    }
    */

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

    private void refreshPairedDevices() {
        if(MainPresenter.bluetooth) {
            pairedDevices = new ArrayList<>(callback.getPairedDevices());

            if(!pairedDevices.isEmpty()) {
                ArrayList<String> names = new ArrayList<>();

                for(BluetoothDevice device : pairedDevices) {
                    names.add(device.getName() + "-" + device.getAddress());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, names);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                devicesSpinner.setAdapter(arrayAdapter);

                devicesSpinner.setSelection(0);

                callback.selectDevice(pairedDevices.get(0));
            }
        }
    }
 }
