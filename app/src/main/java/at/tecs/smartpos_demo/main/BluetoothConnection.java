package at.tecs.smartpos_demo.main;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import at.tecs.smartpos.iConnection;

public class BluetoothConnection implements iConnection {

    protected BluetoothSocket socket;
    protected BluetoothAdapter bluetoothAdapter;

    private BufferedInputStream bufferedInputStream;
    private BufferedOutputStream bufferedOutputStream;

    private UUID uuid;

    private String address = "";

    public BluetoothConnection() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void setDeviceAddress(String address) {
        this.address = address;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void connect() throws IOException {
        BluetoothDevice bluetoothDevice = null;
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for(BluetoothDevice device : pairedDevices) {
            if(device.getAddress().toLowerCase().equals(address.toLowerCase())) {
                bluetoothDevice = device;
            }
        }

        if(bluetoothDevice == null) {
            throw new IOException("Device not found!");
        }

        socket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);

        socket.connect();
        bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        bufferedInputStream = new BufferedInputStream(socket.getInputStream());
    }

    @Override
    public void disconnect() throws IOException {
        if (bufferedInputStream != null)
            bufferedInputStream.close();

        if (bufferedOutputStream != null)
            bufferedOutputStream.close();

        if (socket != null) {
            socket.close();
            socket = null;
        }
    }

    @Override
    public boolean write(byte[] bytes) {
        try {
            bufferedOutputStream.write(bytes, 0,bytes.length);
            bufferedOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public int read(final byte[] bytes) throws IOException {
        return bufferedInputStream.read(bytes, 0, bytes.length);
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
}
