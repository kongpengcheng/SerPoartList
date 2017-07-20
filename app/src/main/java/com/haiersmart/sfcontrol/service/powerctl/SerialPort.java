/**
 * Created by Harry.Kong.
 * Time 2017/5/5.
 * Email kongpengcheng@aliyun.com.
 * Description:
 */
package com.haiersmart.sfcontrol.service.powerctl;

import android.os.RemoteException;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {

    private static final String TAG = "SerialPort";

    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    private boolean isReady = false;
    private int mSerialPortNum = 0;
    private final String[] mSerialPortDevice = {"/dev/ttyS3", "/dev/ttyMFD1", "/dev/ttyMT1", "/dev/ttyS0", "/dev/ttyAMA3", "/dev/ttyS1"};
    private final int mBaudRate = 9600;
    private String strModel;
    private String strVersion;

    public SerialPort() {
        try {
            openSerialPort();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public String getStrVersion() {
        return strVersion;
    }

    public String getStrModel() {
        return strModel;
    }

    private void openSerialPort() throws IOException, RemoteException {
        strModel = "UG";
        strVersion = "";//os的版本
        //        strModel = "XD";
        Log.i(TAG, "SystemModel:" + strModel);
        if (strModel.indexOf("XD") >= 0) {
            mSerialPortNum = 3;
            mFd = SerialOpen(mSerialPortDevice[mSerialPortNum], mBaudRate);
        } else if (strModel.indexOf("595") >= 0) {
            mSerialPortNum = 1;
            mFd = SerialOpen(mSerialPortDevice[mSerialPortNum], mBaudRate);
        } else if (strModel.indexOf("UG") >= 0) {
            Log.e(TAG,"串口打开");
            mSerialPortNum = 2;
            mFd = SerialOpen(mSerialPortDevice[mSerialPortNum], mBaudRate);
        } else if (strModel.indexOf("HLT") >= 0) {
            mSerialPortNum = 4;
            mFd = SerialOpen(mSerialPortDevice[mSerialPortNum], mBaudRate);
        } else {
            mSerialPortNum = 1;
            mFd = SerialOpen(mSerialPortDevice[mSerialPortNum], mBaudRate);
            if (mFd == null) {
                mSerialPortNum = 2;
                mFd = SerialOpen(mSerialPortDevice[mSerialPortNum], mBaudRate);
                if (mFd == null) {
                    mSerialPortNum = 3;
                    mFd = SerialOpen(mSerialPortDevice[mSerialPortNum], mBaudRate);
                    if (mFd == null) {
                        mSerialPortNum = 4;
                        mFd = SerialOpen(mSerialPortDevice[mSerialPortNum], mBaudRate);
                        if (mFd == null) {
                            mSerialPortNum = 5;
                            mFd = SerialOpen(mSerialPortDevice[mSerialPortNum], mBaudRate);
                        }
                    }
                }
            }
        }
        if (mFd == null) {
            isReady = false;
            mSerialPortNum = 0;
            throw new NullPointerException("串口设置有误");
        } else {
            isReady = true;
            mFileInputStream = new FileInputStream(mFd);
            mFileOutputStream = new FileOutputStream(mFd);
            Log.e(TAG,"串口设置成功");
        }
    }

    // Getters and setters

    public boolean isReady() {
        return isReady;
    }

    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }


    // JNI
    private native static FileDescriptor SerialOpen(String path, int baudrate);

    public native void SerialClose();

    static {
        System.loadLibrary("serial_port");
    }

    public FileDescriptor getmFd() {
        return mFd;
    }

    public int getSerialPortNum() {
        return mSerialPortNum;
    }

    public void SerialPortClose() {
        if (mFd != null) {
            isReady = false;
            SerialClose();
        }
    }

    public void SerialPortReOpen() {
        mFd = SerialOpen(mSerialPortDevice[mSerialPortNum], mBaudRate);
        isReady = true;
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }
}
