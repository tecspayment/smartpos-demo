package at.tecs.smartpos_demo.main;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import at.tecs.smartpos.data.Response;
import at.tecs.smartpos.data.Transaction;
import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;
import at.tecs.smartpos_demo.response.ResponseActivity;
import at.tecs.smartpos_demo.templates.TemplatesActivity;

import static at.tecs.smartpos.data.Response.Code.*;
import static at.tecs.smartpos_demo.response.ResponseActivity.RESPONSE_KEY;
import static at.tecs.smartpos_demo.templates.TemplatesActivity.PAYMENT_SERVICE_KEY;
import static at.tecs.smartpos_demo.templates.TemplatesActivity.TID_KEY;


public class MainActivity extends AppCompatActivity implements MainContract.View {

    private final static int SERVICE_ALIVE = 100;
    private final static int SERVICE_STOPED = 101;
    private final static int INTERPRET_STOPED = 102;
    private final static int SERVICE_LAUNCHED = 103;
    private final static int SERVICE_RELOADING = 104;

    private MainContract.Presenter presenter = new MainPresenter();

    private Button templatesButton;

    private Button transactionSave;
    private TextInputLayout transInputLayout;

    private Button terminalSave;
    private TextInputLayout termInputLayout;

    private Button hostnameSave;
    private TextInputLayout IPInputLayout;

    private Button portSave;
    private TextInputLayout portInputLayout;

    private TextView onlineStatus;
    private ImageView connectImage;

    private Spinner transactionSpinner;
    private Spinner terminalSpinner;
    private Spinner hostnameSpinner;
    private Spinner portSpinner;

    private Button sendButton;

    //Extended Params
    private TextInputEditText langCodeInput;
    private TextInputEditText receiptInput;
    private TextInputEditText destCurrencyInput;
    private TextInputEditText txOriginInput;
    private TextInputEditText personalIDInput;

    private TextInputEditText transactionIDInput;
    private TextInputEditText dateTimeInput;

    private TextInputEditText transactionInput;
    private TextInputEditText ipInput;
    private TextInputEditText portInput;
    private TextInputEditText terminalInput;

    private TextInputEditText lengthInput;

    private TextInputEditText msgTypeInput;
    private TextInputEditText sourceIDInput;
    private TextInputEditText cardNumInput;
    private TextInputEditText cvc2Input;
    private TextInputEditText amountInput;
    private TextInputEditText currencyInput;
    private TextInputEditText receiptNumInput;
    private TextInputEditText paymentReasonInput;
    private TextInputEditText transPlaceInput;
    private TextInputEditText authorNumInput;
    private TextInputEditText originInput;
    private TextInputEditText passInput;
    private TextInputEditText userDataInput;

    private Button nataliButton;
    private Button connectButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);

        registerReceiver(broadcastReceiver, new IntentFilter("at.tecs.androidnatali.SERVICE_STATUS"));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        presenter.takeView(this);

        prepareAddButtons();
        prepareSaveButtons();

        templatesButton = findViewById(R.id.templatesButton);

        lengthInput = findViewById(R.id.lengthInput);

        msgTypeInput = findViewById(R.id.msgTypeInput);
        sourceIDInput = findViewById(R.id.sourceIDInput);
        cardNumInput = findViewById(R.id.cardNumInput);
        cvc2Input = findViewById(R.id.cvc2Input);
        amountInput = findViewById(R.id.amountInput);
        currencyInput = findViewById(R.id.currencyInput);
        receiptNumInput = findViewById(R.id.receiptNumInput);
        paymentReasonInput = findViewById(R.id.paymentReasonInput);
        transPlaceInput = findViewById(R.id.transPlaceInput);
        authorNumInput = findViewById(R.id.authorNumInput);
        originInput = findViewById(R.id.originInput);
        passInput = findViewById(R.id.passInput);
        userDataInput = findViewById(R.id.userDataInput);

        //Extended Params
        langCodeInput = findViewById(R.id.langCodeInput);
        receiptInput = findViewById(R.id.receiptInput);
        destCurrencyInput = findViewById(R.id.destCurrencyInput);
        txOriginInput = findViewById(R.id.txOriginInput);
        personalIDInput = findViewById(R.id.personalIDInput);

        transactionSpinner = findViewById(R.id.transactionSpinner);
        terminalSpinner = findViewById(R.id.terminalSpinner);
        hostnameSpinner = findViewById(R.id.hostnameSpinner);
        portSpinner = findViewById(R.id.portSpinner);

        templatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(presenter.isConnected()) {
                    showTemplates();
                } else {
                    showToast("Client is disconnected !");
                }
            }
        });

        transactionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.loadTransaction(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        hostnameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setHostname(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        portSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setPort(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        presenter.initialize();

        onlineStatus = findViewById(R.id.onlineStatus);
        connectImage = findViewById(R.id.connectImage);

        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkTransactionInputs())
                    presenter.send(createTransaction());

            }
        });

        transactionInput = findViewById(R.id.transactionInput);
        ipInput = findViewById(R.id.ipInput);
        portInput = findViewById(R.id.portInput);
        terminalInput = findViewById(R.id.terminalInput);

        transactionIDInput = findViewById(R.id.transactionIDInput);
        dateTimeInput = findViewById(R.id.dateTimeInput);

        final CheckBox automaticCheck = findViewById(R.id.automaticCheck);

        presenter.startAutomatic(automaticCheck.isChecked());

        transactionIDInput.setEnabled(!automaticCheck.isChecked());
        dateTimeInput.setEnabled(!automaticCheck.isChecked());

        automaticCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dateTimeInput.setEnabled(!automaticCheck.isChecked());
                transactionIDInput.setEnabled(!automaticCheck.isChecked());
                presenter.startAutomatic(automaticCheck.isChecked());
            }
        });

        nataliButton = findViewById(R.id.nataliButton);

        int status = presenter.startNatali(getContext());       //Launch NaTALI at launch

        if (status == -1) {
            showToast("NaTALI not found !");
        }

        nataliButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = presenter.startNatali(getContext());       //Launch NaTALI at launch

                if (status == -1) {
                    showToast("NaTALI not found !");
                }

            }
        });

        Button responseButton = findViewById(R.id.responseButton);

        responseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResponse(presenter.getLastResponse());
            }
        });

        connectButton = findViewById(R.id.connectButton);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!presenter.isConnected()) {
                    if (checkConnectionInputs()) {
                        presenter.connect();
                    }
                } else {
                    presenter.disconnect();
                }
            }
        });

        presenter.start();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private void showAddTransaction() {
        transactionSave.setEnabled(true);
        transInputLayout.setEnabled(true);
    }


    private void showAddTermNum() {
        terminalSave.setEnabled(true);
        termInputLayout.setEnabled(true);
    }

    private void showAddHostName() {
        hostnameSave.setEnabled(true);
        IPInputLayout.setEnabled(true);
    }

    private void showAddPort() {
        portSave.setEnabled(true);
        portInputLayout.setEnabled(true);
    }

    @Override
    public void showTransactionAuto(final String transactionID, final String dateTime) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                transactionIDInput.setText(transactionID);
                dateTimeInput.setText(dateTime);
            }
        });
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
    public void setTransactionAdapter(ArrayAdapter<String> transactionAdapter) {
        transactionSpinner.setAdapter(transactionAdapter);
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
    public Context getContext() {
        return this;
    }

    /**
     * Shows and sets transactionEntity parameters.
     *
     * @param transactionEntity
     */
    @Override
    public void showTransaction(TransactionEntity transactionEntity) {

        msgTypeInput.setText(transactionEntity.msgType);
        sourceIDInput.setText(transactionEntity.sourceID);
        cardNumInput.setText(transactionEntity.cardNum);
        cvc2Input.setText(transactionEntity.cvc2);
        amountInput.setText(transactionEntity.amount);
        currencyInput.setText(transactionEntity.currency);
        receiptNumInput.setText(transactionEntity.receiptNum);
        paymentReasonInput.setText(transactionEntity.paymentReason);
        transPlaceInput.setText(transactionEntity.transPlace);
        authorNumInput.setText(transactionEntity.authorNum);
        originInput.setText(transactionEntity.originInd);
        passInput.setText(transactionEntity.password);
        userDataInput.setText(transactionEntity.userdata);

        langCodeInput.setText(transactionEntity.langCode);
        receiptInput.setText(transactionEntity.receiptLayout);
        destCurrencyInput.setText(transactionEntity.desCurrency);
        txOriginInput.setText(transactionEntity.txOrigin);
        personalIDInput.setText(transactionEntity.personalID);
    }

    /**
     * Shows terminal response through Toasts or through new Activity.
     *
     * @param response Response from terminal.
     */
    @Override
    public void showResponse(Response response) {
        if (response != null) {
            int code = Integer.valueOf(response.responseCode);

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
                    Intent intent = new Intent(this, ResponseActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(RESPONSE_KEY, response);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    break;
            }
        } else {
            Toast toast = Toast.makeText(getContext(), "Response does not exist !", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void showNataliStatus(int status) {
        switch (status) {
            case SERVICE_ALIVE:
                nataliButton.setText(getString(R.string.service_alive));
                nataliButton.setBackgroundResource(R.color.connected);
                nataliButton.setEnabled(false);
                break;
            case SERVICE_STOPED:
                nataliButton.setText(getString(R.string.service_stoped));
                nataliButton.setBackgroundResource(R.color.disconnected);
                break;
            case INTERPRET_STOPED:
                nataliButton.setText(getString(R.string.interpret_stoped));
                nataliButton.setBackgroundResource(R.color.disconnected);
                break;
            case SERVICE_LAUNCHED:
                nataliButton.setText(getString(R.string.interpret_launched));
                nataliButton.setBackgroundResource(R.color.disconnected);
                break;
            case SERVICE_RELOADING:
                nataliButton.setText(getString(R.string.service_reloading));
                nataliButton.setBackgroundResource(R.color.disconnected);
                break;
        }
    }

    @Override
    public void showTemplates() {
        Intent intent = new Intent(this, TemplatesActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(PAYMENT_SERVICE_KEY, presenter.getPaymentService());
        intent.putExtras(bundle);

        intent.putExtra(TID_KEY, terminalSpinner.getSelectedItem().toString());

        startActivity(intent);
    }

    private void prepareAddButtons() {
        Button transactionAdd = findViewById(R.id.transactionAdd);

        transactionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTransaction();
            }
        });

        Button terminalAdd = findViewById(R.id.terminalAdd);

        terminalAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTermNum();
            }
        });

        Button hostnameAdd = findViewById(R.id.hostnameAdd);

        hostnameAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddHostName();
            }
        });

        Button portAdd = findViewById(R.id.portAdd);

        portAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPort();
            }
        });
    }

    private Transaction createTransaction() {

        Transaction transaction = new Transaction();

        transaction.ID = transactionIDInput.getEditableText().toString();
        transaction.msgType = msgTypeInput.getEditableText().toString();
        transaction.dateTime = dateTimeInput.getEditableText().toString();
        transaction.sourceID = sourceIDInput.getEditableText().toString();
        transaction.cardNum = cardNumInput.getEditableText().toString();
        transaction.cvc2 = cvc2Input.getEditableText().toString();
        transaction.amount = amountInput.getEditableText().toString();
        transaction.currency = currencyInput.getEditableText().toString();
        transaction.terminalNum = terminalSpinner.getSelectedItem().toString();
        transaction.receiptNum = receiptNumInput.getEditableText().toString();
        transaction.transPlace = transPlaceInput.getEditableText().toString();
        transaction.authorNum = authorNumInput.getEditableText().toString();
        transaction.originInd = originInput.getEditableText().toString();
        transaction.password = passInput.getEditableText().toString();
        transaction.userdata = userDataInput.getEditableText().toString();
        transaction.langCode = langCodeInput.getEditableText().toString();
        transaction.desCurrency = destCurrencyInput.getEditableText().toString();
        transaction.receiptLayout = receiptInput.getEditableText().toString();
        transaction.txOrigin = txOriginInput.getEditableText().toString();
        transaction.personalID = personalIDInput.getEditableText().toString();


        return transaction;
    }

    private void prepareSaveButtons() {
        transactionSave = findViewById(R.id.transactionSave);
        transInputLayout = findViewById(R.id.transInputLayout);

        transactionSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transaction transaction = createTransaction();
                transaction.setTag(transactionInput.getEditableText().toString());

                presenter.saveTransaction(transaction);

                transactionSave.setEnabled(false);
                transInputLayout.setEnabled(false);

                if(transactionInput.getText() != null )
                    transactionInput.getText().clear();
            }
        });

        terminalSave = findViewById(R.id.terminalSave);
        termInputLayout = findViewById(R.id.termInputLayout);

        terminalSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveTermNum(terminalInput.getEditableText().toString());

                terminalSave.setEnabled(false);
                termInputLayout.setEnabled(false);

                if(terminalInput.getText() != null)
                    terminalInput.getText().clear();
            }
        });

        hostnameSave = findViewById(R.id.hostnameSave);
        IPInputLayout = findViewById(R.id.IPInputLayout);

        hostnameSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hostname = ipInput.getEditableText().toString();

                presenter.saveHostName(hostname);

                hostnameSave.setEnabled(false);
                IPInputLayout.setEnabled(false);

                if(ipInput.getText() != null)
                    ipInput.getText().clear();
            }
        });

        portSave = findViewById(R.id.portSave);
        portInputLayout = findViewById(R.id.portInputLayout);

        portSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.savePort(portInput.getEditableText().toString());

                portSave.setEnabled(false);
                portInputLayout.setEnabled(false);

                if(portInput.getText() != null)
                    portInput.getText().clear();
            }
        });

    }

    private void enableExtendedParams(boolean enable) {

        langCodeInput.setEnabled(enable);
        receiptInput.setEnabled(enable);
        destCurrencyInput.setEnabled(enable);
        txOriginInput.setEnabled(enable);
        personalIDInput.setEnabled(enable);

        if(enable) {
            lengthInput.setText(getString(R.string.len_1));
        } else {
            lengthInput.setText(getString(R.string.len_2));
        }
    }

    private boolean checkTransactionInputs() {

        String errorMsg = "";

        if (!presenter.isConnected()) {
            errorMsg += "Client is disconnected !";
            showDisconnected();
        }

        if (transactionIDInput.getEditableText().toString().isEmpty()) {
            errorMsg += "TransactionEntity ID is empty ! ";
        }
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
            Toast toast = Toast.makeText(this, errorMsg, Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
    }

    private boolean checkConnectionInputs() {

        String errorMsg = "";

        if (hostnameSpinner.getSelectedItem() == null || hostnameSpinner.getSelectedItem().toString().isEmpty()) {
            errorMsg += "HostnameEntity is not set !";
        }
        if (portSpinner.getSelectedItem() == null || portSpinner.getSelectedItem().toString().isEmpty()) {
            errorMsg += "PortEntity is not set !";
        }

        if (errorMsg.isEmpty()) {
            return true;
        } else {
            Toast toast = Toast.makeText(this, errorMsg, Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {




        @Override
        public void onReceive(Context context, Intent intent) {

            int status = intent.getIntExtra("status", -1);

            showNataliStatus(status);
        }
    };
}
