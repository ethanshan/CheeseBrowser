package net.codingpark.cheesebrowser.serialport;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * Created by ethanshan on 14-9-9.
 */
public class SerialPortManager {

    private SerialPort mSerialPort = null;
    private static SerialPortManager instance = null;

    private SerialPortManager() {
    }

    public static SerialPortManager getInstance()
    {
        if (instance == null) {
            instance = new SerialPortManager();
        }
        return instance;
    }

    public void closeSerialPort() {
        if (this.mSerialPort != null) {
            this.mSerialPort.close();
            this.mSerialPort = null;
        }
    }

    public SerialPort getSerialPort()
            throws SecurityException, IOException, InvalidParameterException {
    	mSerialPort = new SerialPort(new File("/dev/ttyS7"), 9600, 0);
        return mSerialPort;
    }
}
