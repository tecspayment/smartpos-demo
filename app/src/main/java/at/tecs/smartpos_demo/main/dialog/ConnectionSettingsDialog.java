package at.tecs.smartpos_demo.main.dialog;

import static at.tecs.smartpos_demo.main.MainPresenter.BLUETOOTH_CON;
import static at.tecs.smartpos_demo.main.MainPresenter.TCP_CON;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.Repository;

public class ConnectionSettingsDialog extends Dialog {

    private Callback.ConnectionSettingsDialogCallback callback;

    private String TID = "";
    private String host = "";
    private String port = "";
    private String connectionType = TCP_CON;
    private String address = "";
    private String uuid = "";

    private Spinner scannedDevicesSpinner;
    private Spinner pairedDevicesSpinner;
    private Spinner connectionTypeSpinner;

    private final ArrayList<String> scannedDevicesAddress = new ArrayList<>();
    private final ArrayList<BluetoothDevice> scannedDevices = new ArrayList<>();
    private final ArrayList<String> pairedDevicesAddress = new ArrayList<>();
    private final ArrayList<BluetoothDevice> pairedDevices = new ArrayList<>();
    private ArrayAdapter<String> pairedAddress;
    private ArrayAdapter<String> deviceAdapter;

    public ConnectionSettingsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_dialog);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getContext().registerReceiver(scanReceiver, filter);

        final AutoCompleteTextView tidEditText = findViewById(R.id.tidEditText);
        final AutoCompleteTextView hostEditText = findViewById(R.id.hostEditText);
        final AutoCompleteTextView portEditText = findViewById(R.id.portEditText);
        final AutoCompleteTextView uuidEditText = findViewById(R.id.uuidEditText);
        final TextView pairedTextView = findViewById(R.id.pairedTextView);
        final TextView scanTextView = findViewById(R.id.scanTextView);

        final Button scanButton = findViewById(R.id.scanButton);
        final Button saveButton = findViewById(R.id.saveButton);

        connectionTypeSpinner = findViewById(R.id.connectionTypeSpinner);
        scannedDevicesSpinner = findViewById(R.id.scannedDevicesSpinner);
        pairedDevicesSpinner = findViewById(R.id.pairedDevicesSpinner);

        final TextInputLayout hostTextInputLayout = findViewById(R.id.hostTextInputLayout);
        final TextInputLayout portTextInputLayout = findViewById(R.id.portTextInputLayout);
        final TextInputLayout uuidTextInputLayout = findViewById(R.id.uuidTextInputLayout);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannedDevicesAddress.clear();
                scannedDevices.clear();
                pairedAddress.clear();
                pairedDevices.clear();

                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if(bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }

                bluetoothAdapter.startDiscovery();

                for(BluetoothDevice device : BluetoothAdapter.getDefaultAdapter().getBondedDevices()) {
                    pairedDevicesAddress.add(device.getAddress());
                }
                pairedDevices.addAll(BluetoothAdapter.getDefaultAdapter().getBondedDevices());

                pairedAddress.notifyDataSetChanged();
            }
        });


        for(BluetoothDevice device : BluetoothAdapter.getDefaultAdapter().getBondedDevices()) {
            pairedDevicesAddress.add(device.getAddress());
        }
        pairedDevices.addAll(BluetoothAdapter.getDefaultAdapter().getBondedDevices());

        pairedAddress = new ArrayAdapter<>(getContext(), R.layout.hint_view, pairedDevicesAddress);
        pairedDevicesSpinner.setAdapter(pairedAddress);

        ArrayAdapter<CharSequence> connectionAdapter = ArrayAdapter.createFromResource(getContext(), R.array.connectionTypes, R.layout.hint_view);
        connectionTypeSpinner.setAdapter(connectionAdapter);

        deviceAdapter = new ArrayAdapter<>(getContext(), R.layout.hint_view, scannedDevicesAddress);
        scannedDevicesSpinner.setAdapter(deviceAdapter);

        tidEditText.setText(TID);
        hostEditText.setText(host);
        portEditText.setText(port);
        uuidEditText.setText(uuid);

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
                if(adapterView.getSelectedItem().toString().equals(TCP_CON)) {
                    uuidTextInputLayout.setVisibility(View.GONE);
                    pairedDevicesSpinner.setVisibility(View.GONE);
                    scannedDevicesSpinner.setVisibility(View.GONE);
                    pairedTextView.setVisibility(View.GONE);
                    scanTextView.setVisibility(View.GONE);
                    portTextInputLayout.setVisibility(View.VISIBLE);
                    hostTextInputLayout.setVisibility(View.VISIBLE);
                } else if(adapterView.getSelectedItem().toString().equals(BLUETOOTH_CON)) {
                    uuidTextInputLayout.setVisibility(View.VISIBLE);
                    pairedDevicesSpinner.setVisibility(View.VISIBLE);
                    scannedDevicesSpinner.setVisibility(View.VISIBLE);
                    pairedTextView.setVisibility(View.VISIBLE);
                    scanTextView.setVisibility(View.VISIBLE);
                    portTextInputLayout.setVisibility(View.GONE);
                    hostTextInputLayout.setVisibility(View.GONE);
                } else {
                    uuidTextInputLayout.setVisibility(View.GONE);
                    pairedDevicesSpinner.setVisibility(View.GONE);
                    scannedDevicesSpinner.setVisibility(View.GONE);
                    pairedTextView.setVisibility(View.GONE);
                    scanTextView.setVisibility(View.GONE);
                    portTextInputLayout.setVisibility(View.GONE);
                    hostTextInputLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        scannedDevicesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pair(scannedDevices.get(i));
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
                            connectionTypeSpinner.getSelectedItem() == null? TCP_CON : connectionTypeSpinner.getSelectedItem() .toString(),
                            uuidEditText.getText().toString(),
                            pairedDevicesSpinner.getSelectedItem() == null? "" : pairedDevicesSpinner.getSelectedItem().toString()
                            );
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().unregisterReceiver(scanReceiver);
    }

    private final BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceHardwareAddress = device.getAddress(); // MAC address
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                scannedDevices.add(bluetoothDevice);
                scannedDevicesAddress.add(deviceHardwareAddress);
                deviceAdapter.notifyDataSetChanged();
            }
        }
    };

    public void setCallback(Callback.ConnectionSettingsDialogCallback callback) {
        this.callback = callback;
    }

    private void pair(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
