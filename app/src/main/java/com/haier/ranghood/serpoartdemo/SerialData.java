package com.haier.ranghood.serpoartdemo;

/**
 * Created by Holy.Han on 2016/9/27 10:21
 * email hanholy1210@163.com
 */
public class SerialData {
    private final String TAG = "SerialData";
    private static final int MAX_BUFF_LEN = 110;
    private byte[] ReceiveBuff;
    private int DataLen;
    private String mCurrentModel;
    private boolean isSerialDataReady = false;
    private String mOSVersion;
    private String mOSType;

    private static SerialData instance;

    public static synchronized SerialData getInstance() {
        if (instance == null) {
            synchronized (SerialData.class) {
                if (instance == null)
                    instance = new SerialData();
            }
        }
        return instance;
    }

//    private SerialData() {
//        mMainBoardInfo = new MainBoardInfo();
//        ReceiveBuff = new byte[MAX_BUFF_LEN];
//        // MyLogUtil.i(TAG,"SerialData constructor ReceiveBuff.length="+ReceiveBuff.length );
//        DataLen = 0;
//        DeviceUtil.enablePirUG(true);
//    }
//
//    public String getCurrentModel() {
//        return mCurrentModel;
//    }
//
    public void copyByte(byte[] data, int len) {
        int i;
        DataLen = len;
        if (DataLen > MAX_BUFF_LEN) {
            DataLen = MAX_BUFF_LEN-1;
        }
        for (i = 0; i < DataLen; i++) {
            ReceiveBuff[i] = data[i];
        }
        ReceiveBuff[i] = 0;
    }
//
//
//    public byte[] getFrameData() {
//        byte[] res = new byte[DataLen];
//        for (int i = 0; i < DataLen; i++) {
//            res[i] = ReceiveBuff[i];
//        }
//        return res;
//    }
//
//    private Boolean isFrameHeader() {
//        Boolean res = false;
//        if ((ReceiveBuff[0] & 0xff) == 0xAA) {
//            if ((ReceiveBuff[1] & 0xff) == 0x55) {
//                res = true;
//            }
//        }
//        return res;
//    }
//
//    private boolean isFrameLenth() {
//        boolean res = false;
//        if (ReceiveBuff[2] == (DataLen - 3)) {
//            res = true;
//        }
//        return res;
//
//    }
//
//    private boolean isSumCheck() {
//        boolean res = false;
//        int len = DataLen - 1;
//        byte mSum = 0;
//        for (int i = 2; i < len; i++) {
//            mSum += ReceiveBuff[i];
//        }
//        if (mSum == ReceiveBuff[len]) {
//            res = true;
//        }
//        return res;
//
//    }
//
//    public boolean isDataCheck() {
//        boolean res = false;
//        if (isFrameHeader() && isFrameLenth() && isSumCheck()) {
//            res = true;
//        }
//        return res;
//
//    }
//
//    private byte[] getReceiveBuff() {
//        byte[] res = new byte[DataLen];
//        for (int i = 0; i < DataLen; i++) {
//            res[i] = ReceiveBuff[i];
//        }
//        return res;
//    }
//
//    private void updateBoardInfo(String fridgeId) {
//        mMainBoardInfo.updateBoardInfo(fridgeId);
//    }
//
//    private void updateBoardInfo(byte[] dataFrame) {
//        mMainBoardInfo.updateBoardInfo(dataFrame);
//    }
//
//    private String getFridgeId() {
//        return mMainBoardInfo.getFridgeId();
//    }
//
//    // TODO: 2016/11/14 增加冰箱型号此处要增加对应
//
//    /**
//     * 创建主控板模型
//     */
//    public void createMainBoard() {
//        String typeId = getFridgeId();
//        if (typeId.equals(ConstantUtil.BCD251_SN)) {
//            MyLogUtil.i(TAG, "fridge mode:251");
//            mCurrentModel = ConstantUtil.BCD251_MODEL;
//            mMainBoard = new MainBoardTwoFiveOne();
//        } else if (typeId.equals(ConstantUtil.BCD476_SN)) {
//            MyLogUtil.i(TAG, "fridge mode:476");
//            mCurrentModel = ConstantUtil.BCD476_MODEL;
//            mMainBoard = new MainBoardFourSevenSix();
//        } else if (typeId.equals(ConstantUtil.BCD256_SN)) {
//            MyLogUtil.i(TAG, "fridge mode:256");
//            mCurrentModel = ConstantUtil.BCD256_MODEL;
//            mMainBoard = new MainBoardTwoFiveSix();
//        } else if (typeId.equals(ConstantUtil.BCD401_SN)) {
//            MyLogUtil.i(TAG, "fridge mode:401");
//            mCurrentModel = ConstantUtil.BCD401_MODEL;
//            mMainBoard = new MainBoardFourZeroOne();
//        } else if (typeId.equals(ConstantUtil.BCD658_SN)) {
//            MyLogUtil.i(TAG, "fridge mode:658");
//            mCurrentModel = ConstantUtil.BCD658_MODEL;
//            mMainBoard = new MainBoardSixFiveEight();
//        }else {
//            MyLogUtil.i(TAG, "fridge mode:default 251");
//            mCurrentModel = ConstantUtil.BCD251_MODEL;
//            mMainBoard = new MainBoardTwoFiveOne();
//        }
//        mMainBoard.init();
//        isSerialDataReady = true;
//    }
//
//    /**
//     * 获取主控板控制类
//     *
//     * @return
//     */
//    public ArrayList<MainBoardEntry> getMainBoardControl() {
//        ArrayList<MainBoardEntry> res = null;
//        if (mMainBoard != null) {
//            res = mMainBoard.getMainBoardControl();
//        }
//        return res;
//
//    }
//
//    /**
//     * 通过名字获取控制类值
//     *
//     * @param name
//     * @return
//     */
//    public int getMbcValueByName(String name) {
//        return mMainBoard.getMainBoardControlByName(name);
//    }
//
//    /**
//     * 获取主控板状态类
//     *
//     * @return
//     */
//    public ArrayList<MainBoardEntry> getMainBoardStatus() {
//        ArrayList<MainBoardEntry> res = null;
//        if (mMainBoard != null) {
//            res = mMainBoard.getMainBoardStatus();
//        }
//        return res;
//    }
//
//    /**
//     * 通过名字获取状态类值
//     *
//     * @param name
//     * @return
//     */
//    public int getMbsValueByName(String name) {
//        return mMainBoard.getMainBoardStatusByName(name);
//    }
//
//    /**
//     * 获取主控板调试类
//     *
//     * @return
//     */
//    public ArrayList<MainBoardEntry> getMainBoardDebug() {
//        ArrayList<MainBoardEntry> res = null;
//        if (mMainBoard != null) {
//            res = mMainBoard.getMainBoardDebug();
//        }
//        return res;
//    }
//
//    /**
//     * 通过名字获取调试类值
//     *
//     * @return
//     */
//    public int getMbdValueByName(String name) {
//        return mMainBoard.getMainBoardDebugByName(name);
//    }
//
//    /**
//     * 获取主板信息
//     *
//     * @return
//     */
//    public MainBoardInfo getMainBoardInfo() {
//        return mMainBoardInfo;
//    }
//
//    /**
//     * 获得档位温度范围
//     *
//     * @return TargetTempRange
//     */
//    public TargetTempRange getTargetTempRange() {
//        TargetTempRange res = null;
//        if (mMainBoard != null) {
//            res = mMainBoard.getTargetTempRange();
//        }
//        return res;
//
//    }

    /**
     * 数据解析
     */
    public void ProcData() {
        switch (ReceiveBuff[3]) {
            case (byte) 0x02: //用户状态帧

                break;
            case (byte) 0x03: //无效帧


                break;
            case (byte) 0x71: //设备识别码查询应答帧
                break;
            case (byte) 0xff: //设备调试信息

                break;
            default:
                break;
        }
    }




    /**
     * 模式与档位同步,主控板与用户行为保持一致
     *
     * @return
     */
//    public ArrayList<byte[]> packSyncMode() {
//        ArrayList<byte[]> res = null;
//        if (mMainBoard != null) {
//            res = mMainBoard.packSyncMode();
//        }
//        return res;
//    }
//
//    public void setCommunicationOverTime(boolean b) {
//        if (mMainBoard != null) {
//            mMainBoard.setCommunicationOverTime(b);
//        }
//        MyLogUtil.i(TAG, "CommunicationOverTime is " + b);
//    }
//
//    public void setCommunicationErr(int value) {
//        if (mMainBoard != null) {
//            mMainBoard.setCommunicationErr(value);
//        }
//        MyLogUtil.i(TAG, "CommunicationErr counts is " + value);
//    }
//
//    public boolean isSerialDataReady() {
//        return isSerialDataReady;
//    }
//
//    public String getmOSVersion() {
//        return mOSVersion;
//    }
//
//    public void setmOSVersion(String mOSVersion) {
//        this.mOSVersion = mOSVersion;
//    }
//
//    public String getmOSType() {
//        return mOSType;
//    }
//
//    public void setmOSType(String mOSType) {
//        this.mOSType = mOSType;
//    }
//
//    public byte[] setDataBaseToBytes() {
//        byte[] res;
//        if (DataLen < 5) {
//            res = null;
//        } else {
//            res = mMainBoard.setDataBaseToBytes(getReceiveBuff());
//        }
//        return res;
//    }
//
//    public void setDoorStauts(boolean b) {
//        mMainBoard.testDoor = b;
//    }
//
//    public String handleDoorEvents(){
//        return mMainBoard.handleDoorEvents();
//    }
}
