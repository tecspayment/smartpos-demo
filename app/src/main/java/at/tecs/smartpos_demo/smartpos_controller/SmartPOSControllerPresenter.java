package at.tecs.smartpos_demo.smartpos_controller;

import static at.tecs.ControlParser.Command.RETURN_CODES.SUCCESS;
import static at.tecs.smartpos_demo.Utils.concatenate;
import static at.tecs.smartpos_demo.Utils.createIvSpecFromZeros;
import static at.tecs.smartpos_demo.Utils.createZeros;
import static at.tecs.smartpos_demo.Utils.decrypt;
import static at.tecs.smartpos_demo.Utils.encrypt;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.crypto.spec.IvParameterSpec;

import at.tecs.ControlParser.Command;
import at.tecs.smartpos.CardControl;
import at.tecs.smartpos.SmartPOSController;
import at.tecs.smartpos.data.PrinterPrintType;
import at.tecs.smartpos.data.PrinterReturnCode;
import at.tecs.smartpos.data.RFReturnCode;
import at.tecs.smartpos.utils.ByteUtil;
import at.tecs.smartpos.utils.Pair;
import at.tecs.smartpos_demo.Utils;
import at.tecs.smartpos_demo.utils.TDEAKey;

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

        smartPOSController.RFOpen(100000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {
            @Override
            public void onDetected(CardControl cardControl, int cardType, byte[] UUID) {
                view.log("RFOpen -> onDetected!");
                view.log("RFOpen -> onDetected - UUID: " + ByteUtil.bytes2HexStr_2(UUID));
                view.log("RFOpen -> onDetected - Card Type: " + cardType + "\n");

                view.log("RFOpen -> onDetected - RFAuthenticateM0 [5DE3D9C591CF4CA241E3E1482EC5AE04]  called !");
                RFReturnCode authCode = cardControl.RFAuthenticateM0(ByteUtil.hexStr2Bytes("5DE3D9C591CF4CA241E3E1482EC5AE04"));
                view.log("RFOpen -> onDetected - RFAuthenticateM0 [5DE3D9C591CF4CA241E3E1482EC5AE04] - Return code:" + authCode.name());

                view.log("");

                view.log("RFOpen -> onDetected - RFReadBlock [0x01] called !");
                Pair<RFReturnCode, byte[]> returnCodes = cardControl.RFReadBlock(0x00);
                view.log("RFOpen -> onDetected - RFReadBlock [0x01] - Return code:" + returnCodes.first.name());
                view.log("RFOpen -> onDetected - RFReadBlock [0x01] - Data:" + ByteUtil.bytes2HexStr_2(returnCodes.second));

                view.log("");

                view.log("RFOpen -> onDetected - RFReadBlock [0x01] called !");
                returnCodes = cardControl.RFReadBlock(0x01);
                view.log("RFOpen -> onDetected - RFReadBlock [0x01] - Return code:" + returnCodes.first.name());
                view.log("RFOpen -> onDetected - RFReadBlock [0x01] - Data:" + ByteUtil.bytes2HexStr_2(returnCodes.second));

                view.log("");

                view.log("RFOpen -> onDetected - RFReadBlock [0x01] called !");
                returnCodes = cardControl.RFReadBlock(0x01);
                view.log("RFOpen -> onDetected - RFReadBlock [0x01] - Return code:" + returnCodes.first.name());
                view.log("RFOpen -> onDetected - RFReadBlock [0x01] - Data:" + ByteUtil.bytes2HexStr_2(returnCodes.second));

                view.log("");

                /*
                view.log("RFOpen -> onDetected - RFReadBlock [0x01] called !");
                returnCodes = cardControl.RFReadBlock(0x01);
                view.log("RFOpen -> onDetected - RFReadBlock [0x01] - Return code:" + returnCodes.first.name());
                view.log("RFOpen -> onDetected - RFReadBlock [0x01] - Data:" + ByteUtil.bytes2HexStr_2(returnCodes.second));

                view.log("");

                view.log("RFOpen -> onDetected - RFReadBlock [0x04] called !");
                returnCodes = cardControl.RFReadBlock(0x04);
                view.log("RFOpen -> onDetected - RFReadBlock [0x04] - Return code:" + returnCodes.first.name());
                view.log("RFOpen -> onDetected - RFReadBlock [0x04] - Data:" + ByteUtil.bytes2HexStr_2(returnCodes.second));


                 */

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
    public void readAll() {
        view.clear();
        view.log("Name: Read All");
        view.log("Start at : " + new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));

        view.description("Read All\n\n" +
                "1.  Card reader will be opened for 10 seconds waiting for M1 type card.\n" +
                "2.  When card is prompted UUID will be read.\n" +
                "3.  All block from interval <1,44> are read.\n" +
                "4.  Card reader will be closed.");

        view.log("RFOpen [10 sec, M1] called!\n");

        smartPOSController.RFOpen(30000, Command.CARD_TYPE.M0, new SmartPOSController.OpenListener() {

            @Override
            public void onDetected(CardControl cardControl, int ct, byte[] bytes) {
                view.log("RF Card successful !");
                view.log("Card Type : " + ByteUtil.byte2HexStr((byte) ct));
                view.log("UUID : " + Utils.bytes2HexStr(bytes));

                final int start = 0;
                final int end = 44;
                final String key = view.getKey(); //"2DF38FDF8581628C81CF54528039F80D";

                view.log("Start - " + start);
                view.log("End - " + end);
                view.log("Key - " + key);

                if(key != null && !key.isEmpty()) {   //Performing authentication
                    TDEAKey TDEAkey = new TDEAKey(ByteUtil.hexStr2Bytes(key));

                    Pair<RFReturnCode, byte[]> response = cardControl.RFTransmit(Utils.calcCrc(ByteUtil.hexStr2Bytes("1A00")));

                    if (response.first != RFReturnCode.SUCCESS || response.second.length != 11 || response.second[0] != (byte) 0xAF) {
                        view.log("Invalid tag key");

                        RFReturnCode close = cardControl.RFClose();
                        view.log("Close status : " + close);
                        return;
                    }

                    byte[] encryptedB = Arrays.copyOfRange(response.second, 1, 9);

                    IvParameterSpec ivSpec = createIvSpecFromZeros(8);
                    byte[] decryptedB;

                    try {
                        decryptedB = decrypt(encryptedB, TDEAkey, ivSpec);

                    } catch (Exception e) {
                        view.log("Failed to decrypt B" + e);

                        RFReturnCode close = cardControl.RFClose();
                        view.log("Close status : " + close);
                        return;
                    }

                    byte[] bDash = concatenate(Arrays.copyOfRange(decryptedB, 1, 8), Arrays.copyOfRange(decryptedB, 0, 1));

                    SecureRandom secureRandom = new SecureRandom();
                    byte[] a = createZeros(8);
                    secureRandom.nextBytes(a);

                    byte[] aDash = concatenate(Arrays.copyOfRange(a, 1, 8), Arrays.copyOfRange(a, 0, 1));
                    byte[] abDash = concatenate(a, bDash);

                    ivSpec = new IvParameterSpec(encryptedB);

                    byte[] encryptedAB;

                    try {
                        encryptedAB = encrypt(abDash, TDEAkey, ivSpec);

                    } catch (Exception e) {
                        view.log("Failed to encrypt AB" + e);

                        RFReturnCode close = cardControl.RFClose();
                        view.log("Close status : " + close);
                        return;
                    }

                    byte[] requestAB = concatenate(new byte[]{(byte) 0xAF}, encryptedAB);

                    Pair<RFReturnCode, byte[]> finalResponse;

                    try {
                        finalResponse = cardControl.RFTransmit(Utils.calcCrc(requestAB));

                    } catch (Exception e) {
                        view.log("Failed communication with chip" + e);

                        RFReturnCode close = cardControl.RFClose();
                        view.log("Close status : " + close);
                        return;
                    }

                    if (finalResponse.first != RFReturnCode.SUCCESS || finalResponse.second.length != 11 || finalResponse.second[0] != (byte) 0x00) {
                        view.log("Invalid tag key");

                        RFReturnCode close = cardControl.RFClose();
                        view.log("Close status : " + close);
                        return;
                    }
                    byte[] encryptedADash = Arrays.copyOfRange(finalResponse.second, 1, 9);

                    ivSpec = new IvParameterSpec(Arrays.copyOfRange(encryptedAB, 8, 16));

                    byte[] reconstructedADash;

                    try {
                        reconstructedADash = decrypt(encryptedADash, TDEAkey, ivSpec);

                    } catch (Exception e) {
                        view.log("Failed to decrypt ADash" + e);

                        RFReturnCode close = cardControl.RFClose();
                        view.log("Close status : " + close);
                        return;
                    }

                    if (!Arrays.equals(reconstructedADash, aDash)) {
                        view.log("Invalid tag key");

                        RFReturnCode close = cardControl.RFClose();
                        view.log("Close status : " + close);
                        return;
                    }
                }

                ArrayList<byte[]> request = new ArrayList<>();

                for (int i = start; i < end; i++) {
                    request.add(Utils.calcCrc(new byte[]{0x30,(byte) i}));
                }

                Pair<RFReturnCode, ArrayList<byte[]>> responseTmp = cardControl.RFTransmit(request);

                view.log("Transmit status : " + responseTmp.first);
                int i = 0;
                for (byte[] b : responseTmp.second) {
                    view.log("B" + (i++) +":\t[" + at.tecs.smartpos_demo.utils.ByteUtil.bytes2HexStr(b) + "]");
                }

                RFReturnCode close = cardControl.RFClose();
                view.log("Close status : " + close);
            }

            @Override
            public void onError(RFReturnCode rfReturnCode) {
                if(RFReturnCode.INTERNAL_ERROR == rfReturnCode) {
                    view.log("RF Open - INTERNAL_ERROR");
                }

                if(RFReturnCode.DEVICE_BUSY == rfReturnCode) {
                    view.log("RF Open - DEVICE_BUSY");
                }

                if(RFReturnCode.TIMEOUT == rfReturnCode) {
                    view.log("RF Open - TIMEOUT");
                }

                if(RFReturnCode.CONNECTION_FAILED == rfReturnCode) {
                    view.log("RF Open - CONNECTION_FAILED");
                }
            }
        });
    }

    @Override
    public void printerTest1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    view.clear();
                    //Open connection and printer
                    int ret = smartPOSController.PrinterOpen();
                    if (ret != PrinterReturnCode.SUCCESS.value) {
                        view.log("Open Printer - Failed!");
                        smartPOSController.PrinterClose();
                        return;
                    } else {
                        view.log("Open Printer - Success!");
                    }

                    int status = smartPOSController.PrinterGetStatus();
                    if (status != PrinterReturnCode.SUCCESS.value) {
                        view.log("Printer Status - Printer not ready!");
                        smartPOSController.PrinterClose();
                        return;
                    } else {
                        view.log("Printer Status - Success!");
                    }

                    //Printing image encoded as Base64 string
                    ret = smartPOSController.PrinterPrint("Qk3+EgAAAAAAAD4AAAAoAAAAgAEAAGQAAAABAAEAAAAAAMASAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP///wD/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////wAB////////8////////////////////////////////////////////////////gAAP///////8/////wB////////////////wH///////z///////////////////gAAH///////8/////AAf///8AAAAAf////4AAP/////AAB//////////////////gAAB///////8/////AAf///8AAAAAH////gAAB////4AAAf/////////////////gAAA///////8/////AAf///8AAAAAD///8AAAAf///gAAAH/////////////////gAAAf//////8/////AAf///8AAAAAD///4AAAAH//+AAAAB/////////////////gAAAP//////8/////AAf///8AAAAAB///gAAAAD//4AAAAA/////////////////gAAAP//////8/////AAf///8AAAAAB///AAAAAA//gAAAAAf////////////////gAAAH//////8/////AAf///8AAAAAB//+AAAAAAf/AAAAAAP////////////////gAAAH//////8/////AAf///8AAAAAB//8AAAAAAP+AAAAAAH////////////////gAAAD//////8/////AAf///8AAAAAB//4AAAAAAP+AAAAAAD////////////////gAAAD//////8/////AAf///8AAAAAB//wAAAAAAP+AAAAAAD////////////////gAAAB//////8/////AAf///8AB//////gAAH4AAf+AAD/gAB////////////////gAAAB//////8/////AAf///8AB//////AAA//gAf/AAf/4AB////////////////gAAAB//////8/////AAf///8AB//////AAD//4A//AD//+AB////////////////gAAAB//////8/////AAf///8AB/////+AAP//+B//gH///AA////////////////gAAAB//////8/////AAf///8AB/////+AAf///D//wf///AA////////////////gAAAB//////8/////AAf///8AB/////8AA////////////AA////////////////gAAAB//////8/////AAf///8AB/////8AB////////////gA////////////////gAAAB//////8/////AAf///8AB/////4AB////////////gA////////////////gAAAB//////8/////AAf///8AB/////4AD////////////gA////////////////gAAAB//////8/////AAf///8AB/////wAH////////////AA////////////////gAAAB//////8/////AAf///8AB/////wAH////////////AA////////////////gAAAB//////8/////AAf///8AB/////wAH////////////AA////////////////gAAAB//////8/////AAf///8AB/////gAP///////////+AA////////////////gAAAB//////8/////AAf///8AB/////gAP///////////8AA////////////////gAAAB//////8/////AAf///8AB/////gAP///////////wAB////////////////gAAAB//////8/////AAf///8AB/////gAf///////////gAB////////////////gAAAB//////8/////AAf///8AB/////gAf//////////+AAB////////////////gAAAB//////8/////AAf///8AB/////gAf//////////4AAD////////////////gAAAB//////8/////AAf///8AAAAP//AAf//////////gAAH////////////////gAAAB//////8/////AAf///8AAAAH//AAf/////////8AAAP////////////////gAAAB//////8/////AAf///8AAAAB//AAf/////////wAAAP////////////////gAAAB//////8/////AAf///8AAAAB//AAf/////////AAAAf////////////////gAAABqr////8/////AAf///8AAAAA//AAf////////8AAAB/////////////////gAAABVVf///8/////AAf///8AAAAA//AAf////////wAAAD/////////////////gAAABqqv///8/////AAf///8AAAAA//AA/////////gAAAP/////////////////gAAABVVV///8/////AAf///8AAAAA//AAf////////AAAAf/////////////////gAAABqqq///8/////AAf///8AAAAA//AAf///////+AAAB//////////////////gAAABVVVf//8/////AAf///8AAAAA//AAf///////8AAAH//////////////////gAAABqqqv//8/////AAf///8AAAAA//AAf///////4AAA///////////////////gAAABVUVX//8/////AAf///8AB/////gAf///////wAAD///////////////////gAAABqqqr//8/////AAf///8AB/////gAf///////wAAP///////////////////gAAABVVVV//8/////AAf///8AB/////gAf///////gAA////////////////////gAAABqqqr//8/////AAf///8AB/////gAP///////gAD////////////////////gAAABVVVU//8/////AAf///8AB/////gAP///////AAH////////////////////gAAABqqqq//8/////AAf///8AB/////wAP///////AAf////////////////////gAAABVVVVf/8/////AAf///8AB/////wAH///////AAf////////////////////gAAABqqqq//8/////AAf///8AB/////wAH///////AA/////////////////////gAAABVVVVf/8/////AAf///8AB/////4AD///////AA///////////////////4igAAABqqqqv/8/////AAf///8AB/////4AD///////AB//////////////////8AAgAAABVVVVf/8/////AAf///8AB/////4AB///////AB//////////////////oqKgAAABqqqqv/8/////AAf///8AB/////8AA///////AB/////////////////+AAAgAAABVVVVf/8/////AAf///8AB/////8AA///////AA/////////////////8iIigAAABqqqqv/8/////AAf///8AB/////+AAP///H//AA/////////////////4AAAgAAABVVVVf/8/////AAf///8AB/////+AAH//8B//AA///w/////////////6qqqgAAABqqqqv/8/////AAf///8AB//////AAD//wB//gAf//Af////////////gAAAwAAABVUVVf/8/////AAf///8AB//////gAAf/AA//gAH/8AP////////////IiIiQAAABqqqqv/8/+AABAAf///8AB//////gAAAAAAf/gAA/AAP////////////AAAAYAAABVVVVf/8/+AAAAAAAB/8AAAAAf//wAAAAAAf/wAAAAAH///////////+ioqKoAAABqqqqv/8/+AAAAAAAA/8AAAAAH//4AAAAAAP/wAAAAAH///////////+AAAAMAAABVVVVf/8/+AAAAAAAAf8AAAAAD//8AAAAAAP/4AAAAAD///////////+IiIiMAAABqqqqv/8/+AAAAAAAAf8AAAAAD//+AAAAAAf/8AAAAAD///////////8AAAAGAAABVVVVf/8/+AAAAAAAAP8AAAAAB///AAAAAA//+AAAAAH///////////+qqqqrAAABqqqqv/8/+AAAAAAAAP8AAAAAB///wAAAAD///AAAAAP///////////4AAAABgAABVVVVf/8//AAAAAAAAP8AAAAAB///4AAAAH///gAAAA////////////6IiIiI4AABqqqqv/8//AAAAAAAAP8AAAAAB///+AAAAf///4AAAD////////////4AAAAAMAABVVVVf/8//gAAAAAAAP8AAAAAB////wAAB////+AAAP////////////6ioqKivgABqqqq//8//4AAAAAAAP+AAAAAB////+AAf/////wAD/////////////4AAAAAA/////////////////////////////////////////////////////////6IiIiIiIiJ//////////////////////////////////////////////////////4AAAAAAAAB//////////////////////////////////////////////////////6qqqqqqqqr//////////////////////////////////////////////////////4AAAAAAAAD//////////////////////////////////////////////////////+IiIiIiIiP//////////////////////////////////////////////////////+AAAAAAAAP///////////////////////////////////////////////////////ioqKioqK//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////8=",
                            PrinterPrintType.IMAGE.value);
                    if (ret != PrinterReturnCode.SUCCESS.value) {
                        view.log("Print - Printer not ready!");
                        smartPOSController.PrinterClose();
                        return;
                    } else {
                        view.log("Print - Success!");
                    }
                    //Feed 2 lines
                    if(smartPOSController.PrinterFeedLine(2) == SUCCESS) {
                        view.log("Feed line - Success!");
                    } else {
                        view.log("Feed line - Failed!");
                    }

                    //Print multi line text
                    ret = smartPOSController.PrinterPrint("         KUNDENBELEG\n\n\nTID: 88091137   MID: 102003557\nDate: 07/05/2021     Time: 15:30\n\n\nSALE                            \nVISA CREDIT                     \nPAN: ************0119          \nPAN SEQ NO: 01                  \nVISA ACQUIRER TEST/CARD 01     \n\n\nSTAN: 154714                   \nTXID: 20210507153032           \nORIG. TXID: 20210507153032     \nAPPROVAL CODE: 213031          \nINVOICE:1                      \nAC: F46E3CEA8232A966           \nAID: A0000000031010                \n\n\nEUR                        1.00\n\n\n           Authorized\n",
                            PrinterPrintType.TEXT.value);
                    if (ret != PrinterReturnCode.SUCCESS.value) {
                        smartPOSController.PrinterClose();
                        return;
                    } else {
                        view.log("Print - Success!");
                    }
                    //Feed 2 lines
                    if(smartPOSController.PrinterFeedLine(2) == SUCCESS) {
                        view.log("Feed line - Success!");
                    } else {
                        view.log("Feed line - Failed!");
                    }

                    //Print QR code from provided text
                    ret = smartPOSController.PrinterPrint("https://www.tecs.at/", PrinterPrintType.QR_SMALL.value);
                    if (ret != PrinterReturnCode.SUCCESS.value) {
                        smartPOSController.PrinterClose();
                        return;
                    } else {
                        view.log("Print - Success!");
                    }

                    //Feed 10 lines
                    if(smartPOSController.PrinterFeedLine(10) == SUCCESS) {
                        view.log("Feed line - Success!");
                    } else {
                        view.log("Feed line - Failed!");
                    }

                    if(smartPOSController.PrinterClose() == SUCCESS) {
                        view.log("Close Printer - Success!");
                    } else {
                        view.log("Feed line - Failed!");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void printerTest2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int ret = smartPOSController.PrinterOpen();
                if (ret != PrinterReturnCode.SUCCESS.value) {
                    view.log("Open Printer - Failed!");
                    smartPOSController.PrinterClose();
                    return;
                } else {
                    view.log("Open Printer - Success!");

                    for(int i = 0; i < 10; i++) {
                        smartPOSController.PrinterPrint(i + "----", PrinterPrintType.TEXT.value);
                        smartPOSController.PrinterFeedLine(1);
                    }
                    smartPOSController.PrinterFeedLine(8);

                    smartPOSController.PrinterClose();
                }
            }
        }).start();
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
