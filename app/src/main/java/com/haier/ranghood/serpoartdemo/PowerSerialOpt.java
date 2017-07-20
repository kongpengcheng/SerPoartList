/**
 * Created by Harry.Kong.
 * Time 2017/5/5.
 * Email kongpengcheng@aliyun.com.
 * Description:
 */

package com.haier.ranghood.serpoartdemo;

import android.os.RemoteException;
import android.util.Log;


import com.haiersmart.sfcontrol.service.powerctl.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PowerSerialOpt {
    private final String TAG = "PowerSerialOpt";
    private SerialPort mSerialPort = null;//
    protected OutputStream mOutputStream;//
    private InputStream mInputStream;//
    private SmartReadThread smartReadThread;
    public SerialData mSerialData;//

    protected boolean mReadFlag = false;//
    private boolean vainTypeId = false;//
    private boolean ReadWriteParseThreadSwitch = true;//
    public List<byte[]> byteSendList;//
    private static PowerSerialOpt instance;

    public static synchronized PowerSerialOpt getInstance() throws IOException {
        if (instance == null) {
            synchronized (PowerSerialOpt.class) {
                if (instance == null) {
                    instance = new PowerSerialOpt();
                }
            }
        }
        return instance;
    }

    private class SmartReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            int size = 0;
            int maxLength = 256;
            byte[] buffer = new byte[maxLength];
            int outtimeCnt = 0;//超时计数
            try {
                waitSerialPortReady();//
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            while (!isInterrupted()) {
                if (ReadWriteParseThreadSwitch) {//读写线程开关
                    if (!isSendListEmpty()) {
                        Log.i(TAG, "mSendByte:list大小" + byteSendList.size());
                        byte[] mSendByte = byteSendList.get(byteSendList.size() - 1);//获取发送的的指令
                        byteSendList.clear();
                        // byteSendList.remove(0);
                        if (mReadFlag == false) {//发送命令
                            // Log.i(TAG, "mSendByte:发送的指令" + PrintUtil.BytesToString(mSendByte, 16));
                            if (mOutputStream != null) {
                                Log.i(TAG, "串口打开成功");
                                try {
                                    mOutputStream.write(mSendByte);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            mReadFlag = true;
                            try {
                                if (mInputStream.available() < 1) {//没有接收到返回数据
                                    try {
                                        sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    outtimeCnt++;
                                    if (outtimeCnt >= 2) {
                                        mReadFlag = false;//超时300ms，重新发送命令
                                    }
                                } else {
                                    try {
                                        size = mInputStream.read(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //      Log.i(TAG, "buffer:收到的指令" + PrintUtil.BytesToString(buffer, 16, size) + ",size:" + size);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                }
            }
        }
    }

    private void waitSerialPortReady() throws RemoteException {
        int countsReady = 0;
        boolean mWaiting = true;
        while (mWaiting) {
            try {
                SmartReadThread.sleep(500);
//                 mWriteReadThread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countsReady++;
            if (countsReady > 10 || mSerialPort.isReady()) {
                mWaiting = false;
            }
        }
        if (mSerialPort.isReady()) {
            Log.i(TAG, "read:" + "seriaport的数据");
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
        } else {
            Log.i(TAG, "read:" + "自己创建的");
            mOutputStream = null;
            mInputStream = new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            };
        }
//        mSerialData.setmOSVersion(getOSVersion());
//        mSerialData.setmOSType(getOSType());
    }

    public PowerSerialOpt() {
        //   mProtocolCommand = new ProtocolCommand();
        mSerialPort = new SerialPort();

        byteSendList = java.util.Collections.synchronizedList(new LinkedList<byte[]>());

        mSerialData = SerialData.getInstance();
        smartReadThread = new SmartReadThread();
        smartReadThread.start();
//        mWriteReadThread = new WriteReadThread();
//        mWriteReadThread.start();
    }

    public boolean isSendListEmpty() {
        boolean res = true;
        if (byteSendList.size() > 0) {
            res = false;
        }
        return res;
    }


    public void sendCmdArray(ArrayList<byte[]> cmdArray) {
        for (byte[] cmd : cmdArray) {
            byteSendList.add(cmd);
        }
    }

    public void sendCmd(byte[] cmd) {
        mReadFlag = false;
        byteSendList.add(cmd);
    }


    public void PowerSerialOptClose() {
        ReadWriteParseThreadSwitch = false;
        mSerialPort.SerialPortClose();
    }

    public void PowerSerialOptReOpen() {
        mSerialPort.SerialPortReOpen();
        ReadWriteParseThreadSwitch = true;
    }

    public boolean isReadWriteParseThreadSwitch() {
        return ReadWriteParseThreadSwitch;
    }

    public void setReadWriteParseThreadSwitch(boolean readWriteParseThreadSwitch) {
        ReadWriteParseThreadSwitch = readWriteParseThreadSwitch;
    }

    public String getOSVersion() {
        return mSerialPort.getStrVersion();
    }

    public String getOSType() {
        return mSerialPort.getStrModel();
    }
}
