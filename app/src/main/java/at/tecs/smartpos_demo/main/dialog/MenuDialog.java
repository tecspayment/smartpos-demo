package at.tecs.smartpos_demo.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.history_list.HistoryListActivity;
import at.tecs.smartpos_demo.settings.SettingsActivity;
import at.tecs.smartpos_demo.smartpos_controller.SmartPOSControllerActivity;

public class MenuDialog extends Dialog {

    private Callback.MenuDialogCallback callback;

    public MenuDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_dialog);

        Button cancelButton = findViewById(R.id.closeButton);
        Button settingsButton = findViewById(R.id.smartPOSSettingsButton);
        Button historyButton = findViewById(R.id.historyButton);
        Button controllerButton = findViewById(R.id.controllerButton);
        Button reconnectButton = findViewById(R.id.reconnectButton);
        Button androidSettingsButton = findViewById(R.id.androidSettingsButton);
        Button nataliSettingsButton = findViewById(R.id.nataliSettingsButton);
        Button killButton = findViewById(R.id.killButton);
        Button launchButton = findViewById(R.id.launchButton);

        killButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                callback.killNaTALI();
                dismiss();
                return false;
            }
        });

        nataliSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.maintenance();
                dismiss();
            }
        });

        androidSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });

        reconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.reconnect();
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                getContext().startActivity(intent);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HistoryListActivity.class);
                getContext().startActivity(intent);
            }
        });

        controllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SmartPOSControllerActivity.class);
                getContext().startActivity(intent);
            }
        });

        launchButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                callback.launchNaTALI();
                dismiss();
                return false;
            }
        });
    }

    public void setCallback(Callback.MenuDialogCallback callback) {
        this.callback = callback;
    }
}
