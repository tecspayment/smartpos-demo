package at.tecs.smartpos_demo.main.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.Repository;

public class ConnectionSettingsDialog extends Dialog {

    private Callback.ConnectionSettingsDialogCallback callback;

    private String TID = "";
    private String host = "";
    private String port = "";

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
        final Button saveButton = findViewById(R.id.saveButton);

        tidEditText.setText(TID);
        hostEditText.setText(host);
        portEditText.setText(port);

        ArrayList<String> tids = Repository.getInstance(getContext()).getTerminalNumbers();

        ArrayAdapter<String> tidsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, tids);
        tidEditText.setAdapter(tidsAdapter);

        tidEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tidEditText.showDropDown();
                return false;
            }
        });

        Repository repository = Repository.getInstance(getContext());

        ArrayList<String> ports = repository.getPorts();

        ArrayAdapter<String> portsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, ports);
        portEditText.setAdapter(portsAdapter);

        portEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                portEditText.showDropDown();
                return false;
            }
        });

        ArrayList<String> hostnames = Repository.getInstance(getContext()).getHostnames();

        ArrayAdapter<String> hostnamesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, hostnames);
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
                            portEditText.getText().toString());
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
}
