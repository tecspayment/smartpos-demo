package at.tecs.smartpos_demo.main;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;
import at.tecs.smartpos_demo.main.adapter.MainAdapter;
import at.tecs.smartpos_demo.main.dialog.ConnectionSettingsDialog;
import at.tecs.smartpos_demo.main.dialog.MenuDialog;
import at.tecs.smartpos_demo.main.dialog.MessageDialog;
import at.tecs.smartpos_demo.main.fragments.Callback;

import static at.tecs.smartpos.data.Response.Code.*;


public class MainActivity extends AppCompatActivity implements MainContract.View {

    //Natali application status
    public final static int SERVICE_ALIVE = 100;
    public final static int SERVICE_STOPED = 101;
    public final static int INTERPRET_STOPED = 102;
    public final static int SERVICE_LAUNCHED = 103;
    public final static int SERVICE_RELOADING = 104;
    public final static int SERVICE_READY_TO_PAY = 105;

    private final MainContract.Presenter presenter = MainPresenter.getInstance();

    private ViewPager viewPager;
    private TextView messageText;
    private TextView tidText;
    private ImageButton onlineStatus;
    private ImageButton menuButton;
    private TextView IPTextView;

    private SharedPreferences preferences = null;

    @Override
    protected void onResume() {
        super.onResume();
        presenter.startAutomatic(true);

        if(preferences.getBoolean("firstRun", true)) {
            presenter.loadDefaults();   //Load default transactions

            preferences.edit().putBoolean("firstRun", false).commit();
            preferences.edit().putBoolean("auto_connect", true).commit();
            preferences.edit().putString("hostname", "localhost").commit();
            preferences.edit().putString("port", "9990").commit();

            showMessage("Connection parameters", "Please set terminal number!", R.drawable.outline_warning_white_48dp, -1);
        }

        boolean showDialogResponse = preferences.getBoolean("show_dialog_response", false);
        boolean autoConnect = preferences.getBoolean("auto_connect", true);
        boolean showAutoResponse = preferences.getBoolean("auto_show_response", true);
        String hostname = preferences.getString("hostname", "localhost");
        String port = preferences.getString("port", "9990");
        String tid = preferences.getString("tid", "");

        presenter.setShowDialog(showDialogResponse);
        presenter.setAutoConnect(autoConnect);
        presenter.setHostname(hostname);
        presenter.setPort(port);
        presenter.setShowAutoResponse(showAutoResponse);

        if(!tid.isEmpty()) {
            presenter.setTID(tid);
            showTID(tid);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main_act);

        preferences = getSharedPreferences("at.tecs.smartpos_demo", MODE_PRIVATE);

        viewPager = findViewById(R.id.viewpager);
        tidText = findViewById(R.id.tidText);
        onlineStatus = findViewById(R.id.onlineStatus);
        menuButton = findViewById(R.id.menuButton);
        messageText = findViewById(R.id.messageText);
        IPTextView = findViewById(R.id.IPTextView);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuDialog menuDialog = new MenuDialog(getContext(), R.style.CustomDialogTheme);
                menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                menuDialog.setCallback(menuDialogCallback);

                menuDialog.show();
            }
        });

        onlineStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!presenter.isAutoConnect()) {
                    if(!presenter.isConnected()) {
                        presenter.connect();
                    } else {
                        presenter.disconnect();
                    }
                }
            }
        });

        registerReceiver(nataliReceiver, new IntentFilter("at.tecs.androidnatali.SERVICE_STATUS"));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        final MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());

        mainAdapter.setResponseTabCallback(responseTabCallback);
        mainAdapter.setTransactionsTabCallBack(transactionsTabCallBack);
        mainAdapter.setReceiptTabCallBack(receiptTabCallBack);

        viewPager.setOffscreenPageLimit(3);

        viewPager.setAdapter(mainAdapter);

        tidText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionSettingsDialog connectionDialog = new ConnectionSettingsDialog(getContext(), R.style.CustomDialogTheme);
                connectionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                connectionDialog.setCallback(connectionSettingsDialogCallback);
                connectionDialog.setHost(presenter.getHostname());
                connectionDialog.setPort(presenter.getPort());
                connectionDialog.setTID(presenter.getTerminalNum());
                connectionDialog.setUUID(presenter.getUUID());
                connectionDialog.setConnectionType(presenter.getConnectionType());
                connectionDialog.setAddress(presenter.getAddress());

                connectionDialog.show();
            }
        });

        presenter.takeView(this);

        presenter.initialize();

        if(presenter.isConnected()) {
            showConnected();
        } else {
            showDisconnected();
        }

        presenter.start();
    }

    @Override
    protected void onDestroy() {
        presenter.disconnect();
        unregisterReceiver(nataliReceiver);
        super.onDestroy();
    }

    @Override
    public void showIP(String IP) {
        IPTextView.setText("IP: " + IP);
    }

    /**
     * Shows Online text and icon, connectButton is for disconnecting the connection.
     */
    @Override
    public void showConnected() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //showToast(getContext(), "Connected");
                onlineStatus.setImageResource(R.drawable.round_done_white_48dp);
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
                //showToast(getContext(), "Disconnected");
                onlineStatus.setImageResource(R.drawable.outline_close_white_48dp);
            }
        });
    }

    /**
     * Shows Toast to user.
     *
     * @param msg Message which will be displayed
     */
    @Override
    public void showNotification(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageText.setText(msg);
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
                    showNotification("Terminal is in Initialize command and waiting for insert,swipe or tap card");
                    break;
                case TX_IN_PROGRESS:
                    showNotification("TransactionEntity is in progress");
                    break;
                case TX_NOT_IN_PROGRESS:
                    showNotification("TransactionEntity is not in progress");
                    break;
                case TX_WAITING_FOR_REMOVE_CARD:
                    showNotification("Waiting for remove card");
                    break;
                case TX_INITIALSE:
                    showNotification("Transactions is in initialization state");
                    break;
                case TX_FC_WAITING_FOR_CARD:
                    showNotification("Terminal is in Initialize command and waiting for insert card");
                    break;
                case TX_WAS_SENT_WAIT_ON_REMOVE_CARD:
                case TX_CANCELED_WAIT_ON_REMOVE_CARD:
                    showNotification("Terminal waiting on remove card");
                    break;
                case DATA_EXCHANGE_START:
                    showNotification("Open connection and sending data");
                    break;
                case DATA_EXCHANGE_END:
                    showNotification("Receive data and close connection");
                    break;
                case DATA_EXCHANGE_FAILED:
                    showNotification("Connection failure");
                    break;
                case SALE:
                    showNotification("Start sale transaction");
                    break;
                case VOID:
                    showNotification("Start void transaction");
                    break;

                default:    //Show normal response
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(1);
                        }
                    });

                    break;
            }
    }

    @Override
    public void showTab(final int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(position);
            }
        });

    }

    @Override
    public void showTID(String tid) {
        tidText.setText(tid);
    }

    @Override
    public void showHostname(String hostname) {

    }

    @Override
    public void showPort(String port) {

    }

    @Override
    public void showMessage(final String title, final String text, final int imageResource,final int timeout) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final MessageDialog messageDialog = new MessageDialog(getContext());
                messageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                messageDialog.setTitle(title);
                messageDialog.setText(text);
                messageDialog.setImage(imageResource);
                messageDialog.show();

                if(timeout > 0) {
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(messageDialog.isShowing()) {
                                messageDialog.dismiss();
                            }
                        }
                    }, timeout);
                }
            }
        });


    }

    private final BroadcastReceiver nataliReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            int status = intent.getIntExtra("status", -1);

            if(status != -1 && status != SERVICE_ALIVE) {
                presenter.setNataliStatus(status);
            }

            //Log.e("TEST", "Received status: " + status);

            switch (status) {
                case SERVICE_LAUNCHED:
                    showNotification("NaTALI has launched!");
                case SERVICE_STOPED:
                    showNotification("NaTALI has stopped!");
                case INTERPRET_STOPED:
                    showNotification("NaTALI's interpreter has stopped!");
                case SERVICE_RELOADING:
                    showNotification("NaTALI is reloading!");
                    if(presenter.isConnected()) {
                        presenter.disconnect();
                    }
                    break;
            }
        }
    };

    private final at.tecs.smartpos_demo.main.dialog.Callback.MenuDialogCallback menuDialogCallback = new at.tecs.smartpos_demo.main.dialog.Callback.MenuDialogCallback() {
        @Override
        public void reconnect() {
            presenter.disconnect();
        }

        @Override
        public void maintenance() {
            TransactionEntity transaction = new TransactionEntity();
            transaction.msgType = "7976";
            transaction.name = "NaTALI Menu";

            presenter.send(transaction);
        }

        @Override
        public void killNaTALI() {
            TransactionEntity transaction = new TransactionEntity();
            transaction.msgType = "4544";
            transaction.name = "NaTALI Kill";

            presenter.send(transaction);
        }

        @Override
        public void launchNaTALI() {
            try {
                Intent intent = new Intent();
                intent.setClassName("at.tecs.androidnatali.tecs", "at.tecs.androidnatali.MasterActivity");
                startActivity(intent);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    };

    private final Callback.ResponseTabCallback responseTabCallback = new Callback.ResponseTabCallback() {
        @Override
        public void onAttach(ResponseTab view) {
            presenter.takeResponseView(view);
        }
    };

    private final Callback.TransactionsTabCallBack transactionsTabCallBack = new Callback.TransactionsTabCallBack() {
        @Override
        public void performTransaction(TransactionEntity transactionEntity) {
            if(transactionEntity != null) {
                presenter.send(transactionEntity);
            }
        }

        @Override
        public void performAbort() {
            presenter.abort();
        }
    };

    private final Callback.ReceiptTabCallBack receiptTabCallBack = new Callback.ReceiptTabCallBack() {
        @Override
        public void onAttach(ReceiptTab view) {
            presenter.takeReceiptView(view);
        }
    };

    private final at.tecs.smartpos_demo.main.dialog.Callback.ConnectionSettingsDialogCallback connectionSettingsDialogCallback = new at.tecs.smartpos_demo.main.dialog.Callback.ConnectionSettingsDialogCallback() {
        @Override
        public void saveConnection(String tid, String hostname, String port, String connectionType, String UUID, String address) {
            if(!hostname.isEmpty()) {
                preferences.edit().putString("hostname", hostname).commit();
                presenter.saveHostName(hostname);
            }
            if(!port.isEmpty()) {
                preferences.edit().putString("port", port).commit();
                presenter.savePort(port);
            }
            if(!tid.isEmpty()) {
                preferences.edit().putString("tid", tid).commit();
                presenter.saveTermNum(tid);
            }

            presenter.setHostname(hostname);
            presenter.setTID(tid);
            presenter.setPort(port);

            presenter.saveAddress(address);
            presenter.saveUUID(UUID);
            presenter.setConnectionType(connectionType);

            showTID(tid);
        }
    };
}
