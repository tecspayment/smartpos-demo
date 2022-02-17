package at.tecs.smartpos_demo.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.history_list.HistoryListActivity;
import at.tecs.smartpos_demo.settings.SettingsActivity;

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
        Button settingsButton = findViewById(R.id.settingsButton);
        Button historyButton = findViewById(R.id.historyButton);
        Button controllerButton = findViewById(R.id.controllerButton);
        Button reconnectButton = findViewById(R.id.reconnectButton);

        reconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.reconnect();
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

            }
        });
    }

    public void setCallback(Callback.MenuDialogCallback callback) {
        this.callback = callback;
    }
}
