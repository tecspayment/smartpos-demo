package at.tecs.smartpos_demo.smartpos_controller;

public interface SmartPOSControllerContract {

    interface Presenter {
        void attachView(SmartPOSControllerContract.View view);

        void open();
        void close();
        void performTest1();
        void performTest2();
    }

    interface View {
        void description(String description);
        void log(String log);
        void clear();
    }
}
