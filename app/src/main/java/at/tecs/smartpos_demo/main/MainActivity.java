package at.tecs.smartpos_demo.main;


import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Set;

import at.tecs.smartpos.data.ConnectionType;
import at.tecs.smartpos.data.Transaction;
import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;
import at.tecs.smartpos_demo.main.adapter.TabAdapter;
import at.tecs.smartpos_demo.main.fragments.Callback;

import static at.tecs.smartpos.data.Response.Code.*;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    //Natali application status
    private final static int SERVICE_ALIVE = 100;
    private final static int SERVICE_STOPED = 101;
    private final static int INTERPRET_STOPED = 102;
    private final static int SERVICE_LAUNCHED = 103;
    private final static int SERVICE_RELOADING = 104;

    private final MainContract.Presenter presenter = new MainPresenter();

    private TextView onlineStatus;
    private ImageView connectImage;

    //private Button nataliButton;
    private Spinner connectionSpinner;
    private Button connectButton;

    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);

        registerReceiver(nataliReceiver, new IntentFilter("at.tecs.androidnatali.SERVICE_STATUS"));
        registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        presenter.takeView(this);

        viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        final TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());

        tabAdapter.setConnectionTabCallback(connectionTabCallback);
        tabAdapter.setTransactionTabCallback(transactionTabCallback);
        tabAdapter.setResponseTabCallback(responseTabCallback);
        tabAdapter.setTemplatesTabCallback(templatesTabCallback);
        tabAdapter.setCardTabCallback(cardTabCallback);

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        Button sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(presenter.isConnected())
                    presenter.send();
            }
        });

        presenter.initialize();

        onlineStatus = findViewById(R.id.onlineStatus);
        connectImage = findViewById(R.id.connectImage);

        //nataliButton = findViewById(R.id.nataliButton);

        connectionSpinner = findViewById(R.id.connectionSpinner);

        ArrayList<String> connectionTypes = new ArrayList<>();
        connectionTypes.add("TCP");
        if(MainPresenter.bluetooth)
            connectionTypes.add("Bluetooth");

        connectionSpinner.setSelection(0);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, connectionTypes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        connectionSpinner.setAdapter(arrayAdapter);

        connectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.selectConnection(connectionSpinner.getSelectedItem().toString());
                tabAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*
        int status = presenter.startNatali(getContext());       //Launch NaTALI at launch

        if (status == -1) {
            showToast("NaTALI not found !");
        }
        */

        /*
        nataliButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = presenter.startNatali(getContext());       //Launch NaTALI at launch

                if (status == -1) {
                    showToast("NaTALI not found !");
                }
            }
        });
        */

        connectButton = findViewById(R.id.connectButton);

        if(presenter.isConnected()) {
            showConnected();
        } else {
            showDisconnected();
        }

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!presenter.isConnected()) {
                    presenter.connect();
                } else {
                    presenter.disconnect();
                }
            }
        });

        presenter.start();
    }

    @Override
    protected void onDestroy() {
        presenter.disconnect();
        unregisterReceiver(nataliReceiver);
        unregisterReceiver(bluetoothReceiver);
        super.onDestroy();
    }

    /**
     * Shows Online text and icon, connectButton is for disconnecting the connection.
     */
    @Override
    public void showConnected() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onlineStatus.setText(getString(R.string.online));
                connectImage.setImageResource(android.R.drawable.presence_online);

                connectButton.setText(getString(R.string.disconnected));
                connectButton.setBackgroundResource(R.color.disconnected);
            }
        });

    }

    /**
     * Shows Disconnect text and icon.
     */
    @Override
    public void showDisconnected() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onlineStatus.setText(getString(R.string.offline));
                connectImage.setImageResource(android.R.drawable.presence_offline);

                connectButton.setText(getString(R.string.connect_caps));
                connectButton.setBackgroundResource(R.color.connected);
            }
        });
    }

    /**
     * Shows Toast to user.
     *
     * @param msg Message which will be displayed
     */
    @Override
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    /**
     * Shows terminal response through Toasts or through new Activity.*
     */
    @Override
    public void showResponseTab(int code) {

            switch (code) { //Show status response
                case TX_WAITING_FOR_CARD:
                    showToast("Terminal is in Initialize command and waiting for insert,swipe or tap card");
                    break;
                case TX_IN_PROGRESS:
                    showToast("TransactionEntity is in progress");
                    break;
                case TX_NOT_IN_PROGRESS:
                    showToast("TransactionEntity is not in progress");
                    break;
                case TX_WAITING_FOR_REMOVE_CARD:
                    showToast("Waiting for remove card");
                    break;
                case TX_INITIALSE:
                    showToast("Transactions is in initialization state");
                    break;
                case TX_FC_WAITING_FOR_CARD:
                    showToast("Terminal is in Initialize command and waiting for insert card");
                    break;
                case TX_WAS_SENT_WAIT_ON_REMOVE_CARD:
                case TX_CANCELED_WAIT_ON_REMOVE_CARD:
                    showToast("Terminal waiting on remove card");
                    break;
                case DATA_EXCHANGE_START:
                    showToast("Open connection and sending data");
                    break;
                case DATA_EXCHANGE_END:
                    showToast("Receive data and close connection");
                    break;
                case DATA_EXCHANGE_FAILED:
                    showToast("Connection failure");
                    break;
                case SALE:
                    showToast("Start sale transaction");
                    break;
                case VOID:
                    showToast("Start void transaction");
                    break;

                default:    //Show normal response
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(2);
                        }
                    });

                    break;
            }
    }

    @Override
    public void showNataliStatus(int status) {

        /*
        switch (status) {
            case SERVICE_ALIVE:
                showToast("Natali status : " + status);
                break;
            case SERVICE_STOPED:

                break;
            case INTERPRET_STOPED:

                break;
            case SERVICE_LAUNCHED:

                break;
            case SERVICE_RELOADING:

                break;
        }
        */
    }

    private BroadcastReceiver nataliReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            int status = intent.getIntExtra("status", -1);

            //showNataliStatus(status);
            switch (status) {
                case SERVICE_ALIVE:
                    //showToast("Natali status : " + status);
                    break;
                case SERVICE_LAUNCHED:
                case SERVICE_STOPED:
                case INTERPRET_STOPED:
                case SERVICE_RELOADING:
                    //showToast("Natali status : " + status);
                    if(presenter.isConnected())
                        presenter.disconnect();
                    break;
            }
        }
    };

    private Callback.ConnectionTabCallback connectionTabCallback = new Callback.ConnectionTabCallback() {
        @Override
        public void saveTerminalNumber(String terminalNum) {
            presenter.saveTermNum(terminalNum);
        }

        @Override
        public void deleteTerminalNumber(String terminalNum) {
            presenter.deleteTermNum(terminalNum);
        }

        @Override
        public void saveHostname(String hostname) {
            presenter.saveHostName(hostname);
        }

        @Override
        public void deleteHostname(String hostname) {
            presenter.deleteHostName(hostname);
        }

        @Override
        public void savePort(String port) {
            presenter.savePort(port);
        }

        @Override
        public void deletePort(String port) {
            presenter.deletePort(port);
        }

        @Override
        public void selectHostname(String hostname) {
            presenter.setHostname(hostname);
        }

        @Override
        public void selectPort(String port) {
            presenter.setPort(port);
        }

        @Override
        public void selectTID(String TID) {
            presenter.setTID(TID);
        }

        @Override
        public void onAttach(ConnectionTab view) {
            presenter.takeConnectionView(view);
        }

        @Override
        public void startScan() {
            presenter.startScan();
        }

        @Override
        public void stopScan() {
            presenter.stopScan();
        }

        @Override
        public void selectDevice(BluetoothDevice bluetoothDevice) {
            presenter.setBluetoothDevice(bluetoothDevice);
        }

        @Override
        public Set<BluetoothDevice> getPairedDevices() {
            return presenter.getPairedDevices();
        }

        @Override
        public ConnectionType selectedConnection() {
            return presenter.getSelected();
        }
    };

    private Callback.TransactionTabCallback transactionTabCallback = new Callback.TransactionTabCallback() {
        @Override
        public void saveTransaction(Transaction transaction, String name) {
            presenter.saveTransaction(transaction, name);
        }

        @Override
        public void deleteTransaction(String name) {
            presenter.deleteTransaction(name);
        }

        @Override
        public void startAutomatic(boolean start) {
            presenter.startAutomatic(start);
        }

        @Override
        public void loadTransaction(String transactionID) {
            presenter.loadTransaction(transactionID);
        }

        @Override
        public void onAttach(TransactionTab view) {
            presenter.takeTransactionView(view);
        }

        @Override
        public void performAliPayScan() {
            presenter.send();
        }

        @Override
        public void performAliPayQR() {
            presenter.send();
        }
    };

    private Callback.ResponseTabCallback responseTabCallback = new Callback.ResponseTabCallback() {
        @Override
        public void onAttach(ResponseTab view) {
            presenter.takeResponseView(view);
        }
    };

    private Callback.TemplatesTabCallback templatesTabCallback = new Callback.TemplatesTabCallback() {
        @Override
        public void onAttach(TemplatesTab view) {
            presenter.takeTemplatesView(view);
        }

        @Override
        public void performSale(String amount, String currency) {
            presenter.sale(amount, currency);
        }

        @Override
        public void performRefund(String amount, String currecy) {
            presenter.refund(amount, currecy);
        }

        @Override
        public void performCancellation(String transID, String amount, String currency) {
            presenter.cancellation(transID, amount, currency);
        }

        @Override
        public void performAbort() {
            presenter.abort();
        }
    };

    private final Callback.CardTabCallback cardTabCallback = new Callback.CardTabCallback() {
        @Override
        public void onAttach(CardTab view) {
            presenter.takeCardView(view);
        }

        @Override
        public void performConnect() {
            presenter.openCardControl();
        }

        @Override
        public void performAuthenticateM0(String data) {
            presenter.authenticateM0CardControl(data);
        }

        @Override
        public void performAuthenticateM1(String keyMode, String snr, String blockID, String key) {
            presenter.authenticateM1CardControl(keyMode, snr, blockID, key);
        }

        @Override
        public void performReadBlock(String blockID) {
            presenter.readCardControl(blockID);
        }

        @Override
        public void performWriteBlock(String blockID, String data) {
            presenter.writeCardControl(blockID, data);
        }

        @Override
        public void performTransmit(String data) {
            presenter.transmitCardControl(data);
        }

    };


    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                Log.d("DBG", "Device found " + deviceName);

                showToast("Device found : " + deviceName + ":" + deviceHardwareAddress);

                presenter.setBluetoothDevice(device);
            }
        }
    };


}
