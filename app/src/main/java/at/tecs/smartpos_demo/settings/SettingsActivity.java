package at.tecs.smartpos_demo.settings;

import static at.tecs.smartpos_demo.Utils.showToast;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import at.tecs.smartpos.data.Transaction;
import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.Repository;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences preferences = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.settings_act);

        preferences = getSharedPreferences("at.tecs.smartpos_demo", MODE_PRIVATE);

        Button clearHistoryButton = findViewById(R.id.clearHistoryButton);
        Button clearTIDButton = findViewById(R.id.clearTIDButton);
        Button clearTransactionButton = findViewById(R.id.clearTransactionButton);
        Button clearHostnamesButton = findViewById(R.id.clearHostnamesButton);
        Button clearPortsButton = findViewById(R.id.clearPortsButton);
        Button defaultTransactionButton = findViewById(R.id.defaultTransactionButton);

        final Repository repository = Repository.getInstance(this);

        final Context context = this;

        clearHostnamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(context, "Please hold the button.");
            }
        });

        clearHostnamesButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                repository.deleteHostnames();
                showToast(context, "Hostnames cleared!");
                return true;
            }
        });

        clearPortsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(context, "Please hold the button.");
            }
        });

        clearPortsButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                repository.deletePorts();
                showToast(context, "Ports cleared!");
                return true;
            }
        });


        clearHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(context, "Please hold the button.");
            }
        });

        clearHistoryButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                repository.clearHistory();
                showToast(context, "History cleared!");
                return true;
            }
        });

        clearTIDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(context, "Please hold the button.");
            }
        });

        clearTIDButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                repository.deleteTerminalNums();
                showToast(context, "Terminal numbers cleared!");
                return false;
            }
        });

        clearTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(context, "Please hold the button.");
            }
        });

        clearTransactionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                repository.deleteTransations();
                showToast(context, "Transactions cleared!");
                return true;
            }
        });

        defaultTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast(context, "Please hold the button.");
            }
        });

        defaultTransactionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                TransactionEntity transaction = new TransactionEntity();
                transaction.name = "Sale";
                transaction.amountVisibility = true;
                transaction.currencyVisibility = true;
                transaction.sourceIDVisibility = false;
                transaction.cardNumVisibility = false;
                transaction.cvc2Visibility = false;
                transaction.receiptNumVisibility = false;
                transaction.paymentReasonVisibility = false;
                transaction.transPlaceVisibility = false;
                transaction.authorNumVisibility = false;
                transaction.originIndVisibility = false;
                transaction.passwordVisibility = false;
                transaction.userdataVisibility = false;
                transaction.langCodeVisibility = false;
                transaction.receiptLayoutVisibility = false;
                transaction.desCurrencyVisibility = false;
                transaction.txOriginVisibility = false;
                transaction.personalIDVisibility = false;
                transaction.msgType = Transaction.MessageType.SALE;
                transaction.index = 0;
                transaction.amount = "1";
                transaction.currency = "EUR";
                transaction.sourceID = "1";
                transaction.receiptNum = "1";
                transaction.originInd = "0";
                transaction.langCode = "EN";
                transaction.receiptLayout = "1";
                transaction.desCurrency = "EUR";
                transaction.txOrigin = "1";

                repository.saveTransaction(transaction);

                transaction = new TransactionEntity();
                transaction.name = "Refund";
                transaction.amountVisibility = true;
                transaction.currencyVisibility = true;
                transaction.sourceIDVisibility = false;
                transaction.cardNumVisibility = false;
                transaction.cvc2Visibility = false;
                transaction.receiptNumVisibility = false;
                transaction.paymentReasonVisibility = false;
                transaction.transPlaceVisibility = false;
                transaction.authorNumVisibility = false;
                transaction.originIndVisibility = false;
                transaction.passwordVisibility = false;
                transaction.userdataVisibility = false;
                transaction.langCodeVisibility = false;
                transaction.receiptLayoutVisibility = false;
                transaction.desCurrencyVisibility = false;
                transaction.txOriginVisibility = false;
                transaction.personalIDVisibility = false;
                transaction.msgType = Transaction.MessageType.CREDIT_NOTE;
                transaction.index = 1;
                transaction.amount = "1";
                transaction.currency = "EUR";
                transaction.sourceID = "1";
                transaction.receiptNum = "1";
                transaction.originInd = "0";
                transaction.langCode = "EN";
                transaction.receiptLayout = "1";
                transaction.desCurrency = "EUR";
                transaction.txOrigin = "1";


                repository.saveTransaction(transaction);

                transaction = new TransactionEntity();
                transaction.name = "Cancellation";
                transaction.cardNumVisibility = true;
                transaction.amountVisibility = true;
                transaction.currencyVisibility = true;
                transaction.amountVisibility = true;
                transaction.currencyVisibility = true;
                transaction.sourceIDVisibility = false;
                transaction.cvc2Visibility = false;
                transaction.receiptNumVisibility = false;
                transaction.paymentReasonVisibility = false;
                transaction.transPlaceVisibility = false;
                transaction.authorNumVisibility = false;
                transaction.originIndVisibility = false;
                transaction.passwordVisibility = false;
                transaction.userdataVisibility = false;
                transaction.langCodeVisibility = false;
                transaction.receiptLayoutVisibility = false;
                transaction.desCurrencyVisibility = false;
                transaction.txOriginVisibility = false;
                transaction.personalIDVisibility = false;
                transaction.msgType = Transaction.MessageType.CANCEL;
                transaction.index = 2;
                transaction.amount = "1";
                transaction.currency = "EUR";
                transaction.sourceID = "1";
                transaction.cardNum = "TXID";
                transaction.receiptNum = "1";
                transaction.originInd = "2";
                transaction.langCode = "EN";
                transaction.receiptLayout = "1";
                transaction.desCurrency = "EUR";
                transaction.txOrigin = "2";


                repository.saveTransaction(transaction);

                transaction = new TransactionEntity();
                transaction.name = "Connection status";
                transaction.cardNumVisibility = false;
                transaction.amountVisibility = false;
                transaction.currencyVisibility = false;
                transaction.amountVisibility = false;
                transaction.currencyVisibility = false;
                transaction.sourceIDVisibility = false;
                transaction.cvc2Visibility = false;
                transaction.receiptNumVisibility = false;
                transaction.paymentReasonVisibility = false;
                transaction.transPlaceVisibility = false;
                transaction.authorNumVisibility = false;
                transaction.originIndVisibility = false;
                transaction.passwordVisibility = false;
                transaction.userdataVisibility = false;
                transaction.langCodeVisibility = false;
                transaction.receiptLayoutVisibility = false;
                transaction.desCurrencyVisibility = false;
                transaction.txOriginVisibility = false;
                transaction.personalIDVisibility = false;
                transaction.index = 3;
                transaction.msgType = Transaction.MessageType.CONNECTION_STATUS;

                repository.saveTransaction(transaction);

                transaction = new TransactionEntity();
                transaction.cardNumVisibility = false;
                transaction.amountVisibility = false;
                transaction.currencyVisibility = false;
                transaction.amountVisibility = false;
                transaction.currencyVisibility = false;
                transaction.sourceIDVisibility = false;
                transaction.cvc2Visibility = false;
                transaction.receiptNumVisibility = false;
                transaction.paymentReasonVisibility = false;
                transaction.transPlaceVisibility = false;
                transaction.authorNumVisibility = false;
                transaction.originIndVisibility = false;
                transaction.passwordVisibility = false;
                transaction.userdataVisibility = false;
                transaction.langCodeVisibility = false;
                transaction.receiptLayoutVisibility = false;
                transaction.desCurrencyVisibility = false;
                transaction.txOriginVisibility = false;
                transaction.personalIDVisibility = false;
                transaction.name = "Reconciliation request";
                transaction.index = 4;
                transaction.msgType = Transaction.MessageType.RECONCILIATION_REQUEST;

                repository.saveTransaction(transaction);

                showToast(context, "Default transactions loaded!");
                return true;
            }
        });

        Button closeButton = findViewById(R.id.closeButton);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CheckBox autoConnectCheckBox = findViewById(R.id.autoConnectCheckBox);

        boolean autoConnect = preferences.getBoolean("auto_connect", true);

        autoConnectCheckBox.setChecked(autoConnect);

        autoConnectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    preferences.edit().putBoolean("auto_connect", true).commit();
                } else {
                    preferences.edit().putBoolean("auto_connect", false).commit();
                }
            }
        });

        CheckBox showDialogCheckBox = findViewById(R.id.showDialogCheckBox);

        boolean showDialogResponse = preferences.getBoolean("show_dialog_response", false);

        showDialogCheckBox.setChecked(showDialogResponse);

        showDialogCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    preferences.edit().putBoolean("show_dialog_response", true).commit();
                } else {
                    preferences.edit().putBoolean("show_dialog_response", false).commit();
                }
            }
        });

        CheckBox autoShowResponseCheckBox = findViewById(R.id.autoShowResponseCheckBox);

        boolean autoShowResponse = preferences.getBoolean("auto_show_response", true);

        autoShowResponseCheckBox.setChecked(autoShowResponse);

        autoShowResponseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    preferences.edit().putBoolean("auto_show_response", true).commit();
                } else {
                    preferences.edit().putBoolean("auto_show_response", false).commit();
                }
            }
        });
    }
}
