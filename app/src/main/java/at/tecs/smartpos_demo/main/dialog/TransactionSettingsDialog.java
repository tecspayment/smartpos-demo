package at.tecs.smartpos_demo.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;

import java.util.List;

public class TransactionSettingsDialog extends Dialog {

    private Callback.TransactionSettingsCallback callback;

    public TransactionSettingsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void setCallback(Callback.TransactionSettingsCallback callback) {
        this.callback = callback;
    }
}
