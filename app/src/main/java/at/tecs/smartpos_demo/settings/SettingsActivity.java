package at.tecs.smartpos_demo.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.Repository;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences preferences = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_act);

        preferences = getSharedPreferences("at.tecs.smartpos_demo", MODE_PRIVATE);

        Button clearHistoryButton = findViewById(R.id.clearHistoryButton);
        Button clearTIDButton = findViewById(R.id.clearTIDButton);
        Button clearTransactionButton = findViewById(R.id.clearTransactionButton);
        Button clearHostnamesButton = findViewById(R.id.clearHostnamesButton);
        Button clearPortsButton = findViewById(R.id.clearPortsButton);

        final Repository repository = Repository.getInstance(this);

        final Context context = this;

        clearHostnamesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Please hold the button.", Toast.LENGTH_SHORT).show();
            }
        });

        clearHostnamesButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                repository.deleteHostnames();
                Toast.makeText(context, "Hostnames cleared!", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        clearPortsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Please hold the button.", Toast.LENGTH_SHORT).show();
            }
        });

        clearPortsButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                repository.deletePorts();
                Toast.makeText(context, "Ports cleared!", Toast.LENGTH_LONG).show();
                return true;
            }
        });


        clearHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Please hold the button.", Toast.LENGTH_SHORT).show();
            }
        });

        clearHistoryButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                repository.clearHistory();
                Toast.makeText(context, "History cleared!", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        clearTIDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Please hold the button.", Toast.LENGTH_SHORT).show();
            }
        });

        clearTIDButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                repository.deleteTerminalNums();
                Toast.makeText(context, "Terminal numbers cleared!", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        clearTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Please hold the button.", Toast.LENGTH_SHORT).show();
            }
        });

        clearTransactionButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                repository.deleteTransations();
                Toast.makeText(context, "Transactions cleared!", Toast.LENGTH_LONG).show();
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

        boolean autoShowResponse = preferences.getBoolean("auto_show_response", false);

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
