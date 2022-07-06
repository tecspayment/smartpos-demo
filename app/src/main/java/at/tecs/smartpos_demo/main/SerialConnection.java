package at.tecs.smartpos_demo.main;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import at.tecs.smartpos.iConnection;

public class SerialConnection implements iConnection {

    private static final int DEVICE_CONNECTED = 0;
    private static final int DEVICE_DISCONNECTED = -1;

    public static final String PERMISSION_GRANTED = "PERMISSION_GRANTED";
    public static final String PERMISSION_NOT_GRANTED = "PERMISSION_NOT_GRANTED";

    public static final String ACTION_USB_PERMISSION = "com.examples.accessory.controller.action.USB_PERMISSION";

    private final PendingIntent pendingIntent;

    private String permission = "";

    private UsbDevice usbDevice;

    private UsbDeviceConnection usbDeviceConnection;

    private UsbEndpoint usbEndpointInput;
    private UsbEndpoint usbEndpointOutput;

    private UsbManager usbManager;

    private static SerialConnection serialPort;

    protected int sendTimeout = 70;
    protected int receiveTimeout = 70;
    protected int baudRate;
    protected int dataBits;
    protected int parity;
    protected int stopBit;
    private String comPortName;

    private final Context context;

    private volatile int status = DEVICE_DISCONNECTED;

    private SerialConnection(Context context) {
        this.context = context;
        pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
    }

    protected StringBuilder cache = new StringBuilder();

    public static SerialConnection getInstance(Context context) {
        if (serialPort == null) {
            serialPort = new SerialConnection(context);
            return serialPort;
        } else {
            return serialPort;
        }
    }

    @Override
    public void connect() throws IOException {
        if(initialization()) {
            open();
        } else {
            throw new IOException("Serial port initialization failed!");
        }
    }

    @Override
    public void disconnect() throws IOException {
        status = DEVICE_DISCONNECTED;
        if(usbDevice != null && usbDeviceConnection != null) {
            usbDeviceConnection.releaseInterface(usbDevice.getInterface(0));
            usbDeviceConnection.close();
        }
    }

    @Override
    public boolean write(byte[] bytes) {
        boolean start = true;

        if(bytes == null || status == DEVICE_DISCONNECTED) {
            return false;
        }

        String data = new String(bytes);

        String temp;

        while (start) {
            if (data.length() > usbEndpointOutput.getMaxPacketSize()) {
                temp = data.substring(0, usbEndpointOutput.getMaxPacketSize());
            } else {
                temp = data;
                start = false;
            }

            int len = usbDeviceConnection.bulkTransfer(usbEndpointOutput, temp.getBytes(), temp.getBytes().length, sendTimeout);

            if (len > 0) {
                if(start)
                    data = data.substring(usbEndpointOutput.getMaxPacketSize());
            } else {
                return false;
            }
        }

        return true;
    }

    @Override
    public int read(final byte[] bytes) throws IOException {
        boolean timeout = false;
        long time = System.currentTimeMillis();

        while(bytes.length > cache.length() && !timeout) {

            if(status == DEVICE_DISCONNECTED) {
                throw new IOException("Device is disconnected!");
            }

            if(readToCache() == -1 && cache.length() != 0) {
                break;
            }

            long now = System.currentTimeMillis();

            if(now - time > (receiveTimeout * 1000L)) {
                timeout = true;
            }
        }

        String ret = "";
        if(cache.length() > 0 ) {
            if(cache.length() > bytes.length) {
                ret = cache.substring(0, bytes.length);
                for(int i = 0; i < ret.length(); i++) {
                    bytes[i] = ret.getBytes()[i];
                }
                cache.delete(0, bytes.length);
            } else {
                ret = cache.toString();
                for(int i = 0; i < ret.length(); i++) {
                    bytes[i] = ret.getBytes()[i];
                }
                cache.delete(0, cache.length());
            }
        }

        return ret.length();
    }

    @Override
    public void setSoTimeout(int i) {

    }

    @Override
    public void setHostname(String s) {

    }

    @Override
    public String getHostname() {
        return null;
    }

    @Override
    public void setPort(int i) {

    }

    @Override
    public int getPort() {
        return 0;
    }

    public void setBaudRate(int baudRate) {

        this.baudRate = baudRate;

        switch (baudRate) {
            case 300:
                this.baudRate = 0x2710;
                break;
            case 600:
                this.baudRate = 0x1388;
                break;
            case 1200:
                this.baudRate = 0x09C4;
                break;
            case 2400:
                this.baudRate = 0x04E2;
                break;
            case 4800:
                this.baudRate = 0x0271;
                break;
            case 9600:
                this.baudRate = 0x4138;
                break;
            case 19200:
                this.baudRate = 0x809C;
                break;
            case 38400:
                this.baudRate = 0xC04E;
                break;
            case 57600:
                this.baudRate = 0x0034;
                break;
            case 115200:
                this.baudRate = 0x001A;
                break;
            case 460800:
                this.baudRate = 0x4006;
                break;
            case 921600:
                this.baudRate = 0x8003;
                break;
        }

    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public void setParity(int parity) {

        switch (parity) {
            case 0:
                this.parity = (0);              //NOPARITY
                break;

            case 1:
                this.parity = (0x1 << 8);       //ODDPARITY
                break;

            case 2:
                this.parity = (0x2 << 8);       //EVENPARITY
                break;

            case 3:
                this.parity = (0x3 << 8);       //MARKPARITY
                break;

            case 4:
                this.parity = (0x4 << 8);       //SPACEPARITY
                break;
        }
    }

    public void setStopBit(int stopBit) {

        switch (stopBit) {
            case 1:
                this.stopBit = (0);
                break;

            case 2:
                this.stopBit = (0x2 << 11);
                break;
        }
    }

    private boolean open() {
        if (usbDevice == null) {
            return false;
        }

        usbDeviceConnection = usbManager.openDevice(usbDevice);

        if (!usbDeviceConnection.claimInterface(usbDevice.getInterface(0), true)) {
            return false;
        }

        for (int i = 0; i < usbDevice.getInterface(0).getEndpointCount(); i++) {

            switch (usbDevice.getInterface(0).getEndpoint(i).getDirection()) {
                case UsbConstants.USB_DIR_IN:                                               //Direction is host to device
                    usbEndpointInput = usbDevice.getInterface(0).getEndpoint(i);
                    break;

                case UsbConstants.USB_DIR_OUT:                                              //Direction is device to host
                    usbEndpointOutput = usbDevice.getInterface(0).getEndpoint(i);
                    break;
            }
        }

        if (usbDeviceConnection != null) {

            if (usbDeviceConnection.controlTransfer(0x40, 0, 0, 0, null, 0, 0) < 0) { //reset
                return false;
            }

            if (usbDeviceConnection.controlTransfer(0x40, 0x03, baudRate, 0, null, 0, 0) < 0) { //set baudrate
                return false;
            }

            int config = this.dataBits | this.parity | this.stopBit;

            if (usbDeviceConnection.controlTransfer(0x40, 0x04, config, 0, null, 0, 0) < 0) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean initialization() {

        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();

        for (UsbDevice device : deviceList.values()) {

            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);

            context.registerReceiver(broadcastReceiver, filter);

            if (!usbManager.hasPermission(device)) {
                usbManager.requestPermission(device, pendingIntent);

                while (permission.isEmpty()) {    //Wait for permission
                    status = DEVICE_DISCONNECTED;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                if (permission.equals(PERMISSION_GRANTED)) {
                    cache.delete(0, cache.length());
                    status = DEVICE_CONNECTED;
                    usbDevice = device;

                    context.unregisterReceiver(broadcastReceiver);

                    permission = "";

                    return true;
                } else {

                    permission = "";

                    return false;
                }
            }
        }
        permission = "";

        return false;
    }

    private int readToCache() {

        byte[] chunk = new byte[usbEndpointInput.getMaxPacketSize()];

        try {
            int len = usbDeviceConnection.bulkTransfer(usbEndpointInput, chunk, 0, usbEndpointInput.getMaxPacketSize(), receiveTimeout);

            if (len < 0) {
                return len;
            } else if (len > 2) {
                cache.append(new String(Arrays.copyOfRange(chunk, 2, len)));

                return len;
            }

        } catch (Exception e) {
            return -1;
        }

        return -1;
    }

    private void setPermission(String permission) {
        this.permission = permission;
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {

                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    setPermission(PERMISSION_GRANTED);
                } else {
                    setPermission(PERMISSION_NOT_GRANTED);
                }
            }
        }
    };
}
