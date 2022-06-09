package at.tecs.smartpos_demo.main.dialog;

public interface Callback {

    interface ConnectionSettingsDialogCallback {
        void saveConnection(String tid, String hostname, String port, String connectionType, String UUID, String address);
    }

    interface MenuDialogCallback {
        void reconnect();
        void maintenance();
        void killNaTALI();
        void launchNaTALI();
    }
}
