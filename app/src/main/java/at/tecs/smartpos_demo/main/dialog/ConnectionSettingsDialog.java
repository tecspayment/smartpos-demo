package at.tecs.smartpos_demo.main.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.Repository;

public class ConnectionSettingsDialog extends Dialog {

    private Callback.ConnectionSettingsDialogCallback callback;

    private String TID = "";
    private String host = "";
    private String port = "";
    private String connectionType = "TCP/IP";
    private String address = "";
    private String uuid = "";

    private Spinner connectionTypeSpinner;

    public ConnectionSettingsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_dialog);

        final AutoCompleteTextView tidEditText = findViewById(R.id.tidEditText);
        final AutoCompleteTextView hostEditText = findViewById(R.id.hostEditText);
        final AutoCompleteTextView portEditText = findViewById(R.id.portEditText);
        final AutoCompleteTextView uuidEditText = findViewById(R.id.uuidEditText);
        final AutoCompleteTextView addressEditText = findViewById(R.id.addressEditText);

        final Button saveButton = findViewById(R.id.saveButton);
        connectionTypeSpinner = findViewById(R.id.connectionTypeSpinner);

        final TextInputLayout hostTextInputLayout = findViewById(R.id.hostTextInputLayout);
        final TextInputLayout portTextInputLayout = findViewById(R.id.portTextInputLayout);
        final TextInputLayout uuidTextInputLayout = findViewById(R.id.uuidTextInputLayout);
        final TextInputLayout addressTextInputLayout = findViewById(R.id.addressTextInputLayout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.connectionTypes, R.layout.hint_view);
        connectionTypeSpinner.setAdapter(adapter);

        tidEditText.setText(TID);
        hostEditText.setText(host);
        portEditText.setText(port);
        uuidEditText.setText(uuid);
        addressEditText.setText(address);

        for(int i = 0; i < 2; i++) {
            String item = (String) connectionTypeSpinner.getItemAtPosition(i);

            if(item.equals(connectionType)) {
                connectionTypeSpinner.setSelection(i);
            }
        }

        ArrayList<String> tids = Repository.getInstance(getContext()).getTerminalNumbers();

        ArrayAdapter<String> tidsAdapter = new ArrayAdapter<>(getContext(), R.layout.hint_view, tids);
        tidEditText.setAdapter(tidsAdapter);

        tidEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tidEditText.showDropDown();
                return false;
            }
        });

        connectionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getSelectedItem().toString().equals("TCP/IP")) {
                    uuidTextInputLayout.setVisibility(View.GONE);
                    addressTextInputLayout.setVisibility(View.GONE);
                    portTextInputLayout.setVisibility(View.VISIBLE);
                    hostTextInputLayout.setVisibility(View.VISIBLE);
                } else {
                    uuidTextInputLayout.setVisibility(View.VISIBLE);
                    addressTextInputLayout.setVisibility(View.VISIBLE);
                    portTextInputLayout.setVisibility(View.GONE);
                    hostTextInputLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Repository repository = Repository.getInstance(getContext());

        ArrayList<String> ports = repository.getPorts();

        ArrayAdapter<String> portsAdapter = new ArrayAdapter<>(getContext(), R.layout.hint_view, ports);
        portEditText.setAdapter(portsAdapter);

        portEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                portEditText.showDropDown();
                return false;
            }
        });

        ArrayList<String> hostnames = Repository.getInstance(getContext()).getHostnames();

        ArrayAdapter<String> hostnamesAdapter = new ArrayAdapter<>(getContext(), R.layout.hint_view, hostnames);
        hostEditText.setAdapter(hostnamesAdapter);

        hostEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hostEditText.showDropDown();
                return false;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback != null) {
                    callback.saveConnection(
                            tidEditText.getText().toString(),
                            hostEditText.getText().toString(),
                            portEditText.getText().toString(),
                            connectionTypeSpinner.getSelectedItem().toString(),
                            uuidEditText.getText().toString(),
                            addressEditText.getText().toString()
                            );
                }
                dismiss();
            }
        });
    }

    public void setCallback(Callback.ConnectionSettingsDialogCallback callback) {
        this.callback = callback;
    }

    public void setTID(String TID) {
        this.TID = TID;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }
}
