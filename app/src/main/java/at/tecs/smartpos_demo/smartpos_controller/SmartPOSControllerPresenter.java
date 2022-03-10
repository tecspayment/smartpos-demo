package at.tecs.smartpos_demo.smartpos_controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import at.tecs.ControlParser.Command;
import at.tecs.smartpos.CardControl;
import at.tecs.smartpos.SmartPOSController;
import at.tecs.smartpos.data.RFReturnCode;
import at.tecs.smartpos.utils.ByteUtil;
import at.tecs.smartpos.utils.Pair;

public class SmartPOSControllerPresenter implements SmartPOSControllerContract.Presenter {

    private SmartPOSControllerContract.View view;
    private final SmartPOSController smartPOSController;

    public SmartPOSControllerPresenter() {
        smartPOSController = new SmartPOSController();
    }

    @Override
    public void attachView(SmartPOSControllerContract.View view) {
        this.view = view;
    }

    @Override
    public void open() {
        view.clear();
        view.description("Testing Open function\n\n" +
                "1.  Card reader will be opened for 10 seconds waiting for M0 type card.\n" +
                "2.  When card is prompted UUID is read.\n" +
                "3.  Card reader will be closed.");
        view.log("Name: Simple open");
        view.log("Start at : " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));

        view.log("RFOpen [10 sec, M0] called!\n");

        smartPOSController.RFOpen(10000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {
            @Override
            public void onDetected(CardControl cardControl, int cardType, byte[] UUID) {
                view.log("RFOpen -> onDetected!");
                view.log("RFOpen -> onDetected - UUID: " + ByteUtil.bytes2HexStr_2(UUID));
                view.log("RFOpen -> onDetected - Card Type: " + cardType + "\n");

                view.log("RFOpen -> onDetected - RFClose called!");
                RFReturnCode returnCode = cardControl.RFClose();
                view.log("RFOpen -> onDetected - RFClose - Return code: " + returnCode.name());

                view.log("Finished at : " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
            }

            @Override
            public void onError(RFReturnCode rfReturnCode) {
                view.log("RFOpen -> onError!");
                view.log("RFOpen -> onError - Error: " + rfReturnCode.name());

                view.log("RFOpen -> onDetected - RFClose called!");
                RFReturnCode returnCode = smartPOSController.RFClose();
                view.log("RFOpen -> onDetected - RFClose - Return code: " + returnCode.name());

                view.log("Finished at : " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
            }
        });
    }

    @Override
    public void performTest1() {
        view.clear();
        view.description("Test 1\n\n" +
                "1.  Card reader will be opened for 10 seconds waiting for M0 type card.\n" +
                "2.  When card is prompted UUID will be read.\n" +
                "3.  Authentication of M0 will be performed with the key - 5DE3D9C591CF4CA241E3E1482EC5AE04.\n" +
                "4.  Data will be read from block 0x00.\n" +
                "5.  Data will be read from block 0x01.\n" +
                "6.  Data will be read from block 0x04.\n" +
                "7.  Card reader will be closed.");
        view.log("Name: Test 1");
        view.log("Start at : " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));

        view.log("RFOpen [10 sec, M0] called!\n");

        smartPOSController.RFOpen(10000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {
            @Override
            public void onDetected(CardControl cardControl, int cardType, byte[] UUID) {
                view.log("RFOpen -> onDetected!");
                view.log("RFOpen -> onDetected - UUID: " + ByteUtil.bytes2HexStr_2(UUID));
                view.log("RFOpen -> onDetected - Card Type: " + cardType + "\n");

                view.log("RFOpen -> onDetected - RFAuthenticateM0 [5DE3D9C591CF4CA241E3E1482EC5AE04]  called !");
                RFReturnCode authCode =  cardControl.RFAuthenticateM0(ByteUtil.hexStr2Bytes("5DE3D9C591CF4CA241E3E1482EC5AE04"));
                view.log("RFOpen -> onDetected - RFAuthenticateM0 [5DE3D9C591CF4CA241E3E1482EC5AE04] - Return code:" + authCode.name());

                view.log("");

                view.log("RFOpen -> onDetected - RFReadBlock [0x00] called !");
                Pair<RFReturnCode, byte[]> returnCodes = cardControl.RFReadBlock(0x00);
                view.log("RFOpen -> onDetected - RFReadBlock [0x00] - Return code:" + returnCodes.first.name());
                view.log("RFOpen -> onDetected - RFReadBlock [0x00] - Data:" + ByteUtil.bytes2HexStr_2(returnCodes.second));

                view.log("");

                view.log("RFOpen -> onDetected - RFReadBlock [0x01] called !");
                returnCodes = cardControl.RFReadBlock(0x01);
                view.log("RFOpen -> onDetected - RFReadBlock [0x01] - Return code:" + returnCodes.first.name());
                view.log("RFOpen -> onDetected - RFReadBlock [0x01] - Data:" + ByteUtil.bytes2HexStr_2(returnCodes.second));

                view.log("");

                view.log("RFOpen -> onDetected - RFReadBlock [0x04] called !");
                returnCodes = cardControl.RFReadBlock(0x04);
                view.log("RFOpen -> onDetected - RFReadBlock [0x04] - Return code:" + returnCodes.first.name());
                view.log("RFOpen -> onDetected - RFReadBlock [0x04] - Data:" + ByteUtil.bytes2HexStr_2(returnCodes.second));


                view.log("");

                view.log("RFOpen -> onDetected - RFClose called!");
                RFReturnCode returnCode = cardControl.RFClose();
                view.log("RFOpen -> onDetected - RFClose - Return code: " + returnCode.name());

                view.log("Finished at : " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
            }

            @Override
            public void onError(RFReturnCode rfReturnCode) {
                view.log("RFOpen -> onError!");
                view.log("RFOpen -> onError - Error: " + rfReturnCode.name());

                view.log("RFOpen -> onDetected - RFClose called!");
                RFReturnCode returnCode = smartPOSController.RFClose();
                view.log("RFOpen -> onDetected - RFClose - Return code: " + returnCode.name());

                view.log("Finished at : " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
            }
        });
    }

    @Override
    public void performTest2() {
        view.clear();
        view.log("Name: Test 2");
        view.log("Start at : " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));

        view.description("Test 2\n\n" +
                "1.  Card reader will be opened for 10 seconds waiting for M1 type card.\n" +
                "2.  When card is prompted UUID will be read.\n" +
                "3.  Data will be read from block 0x00.\n" +
                "4.  Data will be write to block 0x01.\n" +
                "5.  Data will be read from block 0x00.\n" +
                "6.  Card reader will be closed.");

        view.log("RFOpen [10 sec, M1] called!\n");

        smartPOSController.RFOpen(10000, Command.CARD_TYPE.M1, new SmartPOSController.OpenListener() {
            @Override
            public void onDetected(CardControl cardControl, int cardType, byte[] UUID) {
                view.log("RFOpen -> onDetected!");
                view.log("RFOpen -> onDetected - UUID: " + ByteUtil.bytes2HexStr_2(UUID));
                view.log("RFOpen -> onDetected - Card Type: " + cardType + "\n");

                view.log("RFOpen -> onDetected - RFReadBlock [0x00] called !");
                Pair<RFReturnCode, byte[]> returnCodes = cardControl.RFReadBlock(0x00);
                view.log("RFOpen -> onDetected - RFReadBlock [0x00] - Return code:" + returnCodes.first.name());
                view.log("RFOpen -> onDetected - RFReadBlock [0x00] - Data:" + ByteUtil.bytes2HexStr_2(returnCodes.second));

                view.log("");

                byte[] data = new byte[] {0x00, 0x01, 0x02, 0x03};
                view.log("RFOpen -> onDetected - RFWriteBlock [0x01, " + ByteUtil.bytes2HexStr_2(data) + "] called !");
                RFReturnCode returnCode = cardControl.RFWriteBlock(0x01, data);
                view.log("RFOpen -> onDetected - RFWriteBlock [0x01, " + ByteUtil.bytes2HexStr_2(data) + "] - Return code:" + returnCode.name());

                view.log("");

                view.log("RFOpen -> onDetected - RFReadBlock [0x00] called !");
                returnCodes = cardControl.RFReadBlock(0x00);
                view.log("RFOpen -> onDetected - RFReadBlock [0x00] - Return code:" + returnCodes.first.name());
                view.log("RFOpen -> onDetected - RFReadBlock [0x00] - Data:" + ByteUtil.bytes2HexStr_2(returnCodes.second));

                view.log("");

                view.log("RFOpen -> onDetected - RFClose called!");
                returnCode = cardControl.RFClose();
                view.log("RFOpen -> onDetected - RFClose - Return code: " + returnCode.name());

                view.log("Finished at : " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
            }

            @Override
            public void onError(RFReturnCode rfReturnCode) {
                view.log("RFOpen -> onError!");
                view.log("RFOpen -> onError - Error: " + rfReturnCode.name());

                view.log("RFOpen -> onDetected - RFClose called!");
                RFReturnCode returnCode = smartPOSController.RFClose();
                view.log("RFOpen -> onDetected - RFClose - Return code: " + returnCode.name());

                view.log("Finished at : " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
            }
        });
    }

    @Override
    public void close() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (smartPOSController.RFIsConnected()) {
                    view.log("RFClose called!");
                    RFReturnCode returnCode = smartPOSController.RFClose();
                    view.log("RFClose Return code: " + returnCode.name());
                } else {
                    view.log("Interface already closed!");
                }
            }
        }).start();
    }
}
