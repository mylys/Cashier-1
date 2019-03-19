package com.easygo.cashier.printer.local;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.easygo.cashier.MyApplication;
import com.tools.command.EscCommand;
import com.tools.command.LabelCommand;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;


/**
 * 打印机工具类
 */
public class PrinterUtils {

    public static final String TAG = PrinterUtils.class.getSimpleName();

    public static PrinterUtils getInstance() {
        return Holder.sInstance;
    }

    public static class Holder {
        static final PrinterUtils sInstance = new PrinterUtils();
    }

    private LinkedList<CMD> queue = new LinkedList<>();

    private static class CMD {
        public int type;
        public static int TYPE_PRINT = 0;
        public static int TYPE_POP_TILL = 1;
        Runnable task;
        long timeStamp;

        public CMD(int type, Runnable task) {
            this.type = type;
            this.task = task;
            this.timeStamp = System.currentTimeMillis();
        }
    }

    private void checkPrinterStateAndExecQueue() {
        //先检查 打印机的状态 再执行
        if(PrinterUtils.STATE_DISCONNECTED == getPrinterState()) {
            if(mListener != null) {
                mListener.onPleaseConnectPrinter();
            }
        }
    }

    private void execQueue() {
        while(!queue.isEmpty()) {
            Log.i(TAG, "execQueue: 执行任务");
            CMD cmd = queue.removeFirst();

            if(System.currentTimeMillis() - cmd.timeStamp > 5 * 60 * 1000) {
                //超过一定时间的 不再执行
                return;
            }
            ThreadPool.getInstantiation().addTask(cmd.task);
        }
    }



    /**
     * ESC查询打印机实时状态指令
     */
    public byte[] esc = {0x10, 0x04, 0x02};
    /**
     * CPCL查询打印机实时状态指令
     */
    public byte[] cpcl={0x1b,0x68};
    /**
     * TSC查询打印机状态指令
     */
    public byte[] tsc = {0x1b, '!', '?'};

    public int id = 0;

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;


    //广播
    /**USB权限请求*/
    public static final String ACTION_USB_PERMISSION = "com.android.USB_PERMISSION";
    /**USB连接状态*/
    public static final String ACTION_CONN_STATE = DeviceConnFactoryManager.ACTION_CONN_STATE;
    /**USB断开*/
    public static final String ACTION_USB_DEVICE_DETACHED = android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;
    /**USB连接*/
    public static final String ACTION_USB_DEVICE_ATTACHED = android.hardware.usb.UsbManager.ACTION_USB_DEVICE_ATTACHED;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "onReceive: 接受action -> " + action);
            switch (action) {
                case PrinterUtils.ACTION_USB_PERMISSION:
                    synchronized (this) {
                        UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            Log.i(TAG, "onReceive: usb权限通过 " + device);
                            if (device != null) {
                                usbConn(MyApplication.sApplication, device);
                            }
                        } else {
                            Log.i(TAG, "onReceive: usb权限被拒绝 " + device);
                            if(mListener != null) {
                                mListener.onUsbPermissionDeny();
                            }
                        }
                    }
                    break;
                case PrinterUtils.ACTION_CONN_STATE:
                    int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                    int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                    switch (state) {
                        case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                            Log.i(TAG, "onReceive: 已断开连接");
                            if (id == deviceId) {
                                if(mListener != null) {
                                    mListener.onDisconnected();
                                }
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                            Log.i(TAG, "onReceive: 连接中");
                            if(mListener != null) {
                                mListener.onConnecting();
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                            Log.i(TAG, "onReceive: 已连接");
                            if(mListener != null) {
                                mListener.onConnected();
                            }

                            execQueue();

                            break;
                        case DeviceConnFactoryManager.CONN_STATE_FAILED:
                            Log.i(TAG, "onReceive: 连接失败");
                            if(mListener != null) {
                                mListener.onConnectFailed();
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED_PAPER_ERR:
                            Log.i(TAG, "onReceive: 打印机缺纸");
                            if(mListener != null) {
                                mListener.onConnectError(DeviceConnFactoryManager.CONN_STATE_CONNECTED_PAPER_ERR
                                        , "缺纸");
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED_COVER_OPEN_ERR:
                            Log.i(TAG, "onReceive: 打印机开盖");
                            if(mListener != null) {
                                mListener.onConnectError(DeviceConnFactoryManager.CONN_STATE_CONNECTED_COVER_OPEN_ERR
                                        , "开盖");
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED_ERR_OCCURS:
                            Log.i(TAG, "onReceive: 打印机出错");
                            if(mListener != null) {
                                mListener.onConnectError(DeviceConnFactoryManager.CONN_STATE_CONNECTED_ERR_OCCURS
                                        , "出错");
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case PrinterUtils.ACTION_USB_DEVICE_ATTACHED:
                    connectUSB(MyApplication.sApplication, false);
                    break;
                case PrinterUtils.ACTION_USB_DEVICE_DETACHED:
                    if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id]!=null) {
                        UsbDevice detached_device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        UsbDevice connectedUsbDevice = DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnectedUsbDevice();
                        if(connectedUsbDevice != null && detached_device.getDeviceName()
                                .equals(connectedUsbDevice.getDeviceName())) {
                            if(DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort!=null) {
                                Log.i(TAG, "onReceive: 关闭端口 ");
                                DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].closePort(id);
                            }
                        }
                    }


                    break;
            }
        }
    };

    /**
     * 注册广播
     */
    public void registerReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PrinterUtils.ACTION_USB_PERMISSION);
        intentFilter.addAction(PrinterUtils.ACTION_USB_DEVICE_DETACHED);
        intentFilter.addAction(PrinterUtils.ACTION_CONN_STATE);
        intentFilter.addAction(PrinterUtils.ACTION_USB_DEVICE_ATTACHED);
        context.registerReceiver(receiver, intentFilter);
    }
    /**
     * 注销广播
     */
    public void unregisterReceiver(Context context) {
        if(receiver != null) {
            context.unregisterReceiver(receiver);
        }
    }

    private OnPrinterListener mListener;

    public interface OnPrinterListener {
        void onUsbPermissionDeny();
        void onConnecting();
        void onConnected();
        void onDisconnected();
        void onPleaseConnectPrinter();
        void onConnectFailed();
        void onConnectError(int error, String content);
        void onCommandError();
        void onNeedReplugged();
    }

    public void setOnPrinterListener(OnPrinterListener listener) {
        mListener = listener;
    }

    /**
     * 判断打印机是否未连接
     */
    public boolean isPrinterDisconnected() {
        return DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null ||
                !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState();
    }

    /**
     * 判断打印机是否是ESC
     */
    public boolean isEscPrinterCommand() {
        return DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand()
                == PrinterCommand.ESC;
    }

    /**
     * 打印机状态查询
     */
    public int getPrinterState() {
        //打印机状态查询
        if (isPrinterDisconnected()) {
            Log.i(TAG, "getPrinterState: 打印机未连接 ");
            return STATE_DISCONNECTED;
        }
        ThreadPool.getInstantiation().addTask(new Runnable() {
            @Override
            public void run() {
                Vector<Byte> data = new Vector<>(esc.length);
                if (isEscPrinterCommand()) {
                    Log.i(TAG, "getPrinterState: 打印机状态查询... ");
                    for (int i = 0; i < esc.length; i++) {
                        data.add(esc[i]);
                    }
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(data);
                }else if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.TSC) {
                    for (int i = 0; i < tsc.length; i++) {
                        data.add(tsc[i]);
                    }
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(data);
                }else if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.CPCL) {
                    for (int i = 0; i < cpcl.length; i++) {
                        data.add(cpcl[i]);
                    }
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(data);
                }
            }
        });
        return STATE_CONNECTING;
    }

    public String getConnDeviceInfo() {
        DeviceConnFactoryManager deviceConnFactoryManager = DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id];
        if (deviceConnFactoryManager != null
                && deviceConnFactoryManager.getConnState()) {
            if ("USB".equals(deviceConnFactoryManager.getConnMethod().toString())) {
                return deviceConnFactoryManager.usbDevice().getProductName();
            }
        }
        return "没有已连接USB打印机";
    }

    /**
     * 连接USB打印机
     * @param context
     * @param showRepluggedToast 是否弹出重新拔插usb打印机提示
     */
    public void connectUSB(Context context, boolean showRepluggedToast) {
        Log.i(TAG, "connectUSB:  ");
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        List<String> usbDeviceFromName = Utils.getUsbDeviceFromName(context);
        boolean hasUsbPrinter = false;
        if(usbDeviceFromName.size() > 0) {
            String usbName = usbDeviceFromName.get(0);
            if(!isPrinterDisconnected()) {
                UsbDevice usbDevice = DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnectedUsbDevice();
                if(usbDevice != null && usbName.equals(usbDevice.getDeviceName())) {
                    Log.i(TAG, "connectUSB: 打印机已经连接 usbName -> " + usbName);
                    checkPrinterStateAndExecQueue();
                    return;
                }
            }
            closeAllPort();
            Log.i(TAG, "connectUSB: 连接打印机名称 usbName -> " + usbName);
            //通过USB设备名找到USB设备
            UsbDevice usbDevice = Utils.getUsbDeviceFromName(context, usbName);
            if(usbDevice != null) {
                hasUsbPrinter = true;
                //判断USB设备是否有权限
                if (usbManager.hasPermission(usbDevice)) {
                    Log.i(TAG, "connectUSB: 有权限 usb准备连接 ");
                    usbConn(MyApplication.sApplication, usbDevice);
                } else {//请求权限
                    Log.i(TAG, "connectUSB: 请求权限 ");
                    PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0,
                            new Intent(PrinterUtils.ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(usbDevice, mPermissionIntent);
                }
            } else {
                hasUsbPrinter = false;
            }
        }
        if(!hasUsbPrinter && showRepluggedToast && mListener != null) {
            Log.i(TAG, "connectUSB: 需要重新插入设备");
            mListener.onNeedReplugged();
        }
    }

    public void connectUSB(Context context) {
        connectUSB(context, true);
    }


    private void usbConn(Context context, UsbDevice usbDevice) {
        Log.i(TAG, "usbConn: usb开始连接 ");
        new DeviceConnFactoryManager.Build()
                .setId(id)
                .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.USB)
                .setUsbDevice(usbDevice)
                .setContext(context)
                .build();
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
    }

    /**
     * 打开钱箱
     */
    public void popTill() {
        Log.i(TAG, "popTill: 添加任务");
        queue.add(new CMD(CMD.TYPE_POP_TILL, new Runnable() {
            @Override
            public void run() {
                if (PrinterUtils.getInstance().isEscPrinterCommand()) {
                    Log.i(TAG, "popTill: 开钱箱... ");
                    EscCommand esc = new EscCommand();
                    esc.addInitializePrinter();
                    esc.addPrintAndFeedLines((byte) 1);
                    // 开钱箱
                    esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
                    Vector<Byte> datas = esc.getCommand();
                    // 发送数据
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);
                } else {
                    if(mListener != null) {
                        mListener.onCommandError();
                    }
                }
            }
        }));

        connectUSB(MyApplication.sApplication);
    }

    public void cutPaper() {
        if(isPrinterDisconnected()) {
            if(mListener != null) {
                mListener.onPleaseConnectPrinter();
            }
            return;
        }
        ThreadPool.getInstantiation().addTask(new Runnable() {
            @Override
            public void run() {
                if (PrinterUtils.getInstance().isEscPrinterCommand()) {
                    EscCommand esc = new EscCommand();
                    esc.addInitializePrinter();
                    esc.addPrintAndFeedLines((byte) 3);
                    // 切纸
                    esc.addCutPaper();
                    Vector<Byte> datas = esc.getCommand();
                    // 发送数据
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);
                } else {
                    if(mListener != null) {
                        mListener.onCommandError();
                    }
                }
            }
        });
    }

    private int printCount;
    private int count;

    /**
     * 打印
     */
    public void print(final Vector<Byte> datas) {
        Log.i(TAG, "print: 添加任务");
        queue.add(new CMD(CMD.TYPE_PRINT, new Runnable() {
            @Override
            public void run() {
                if (!PrinterUtils.getInstance().isEscPrinterCommand()) {
                    if (mListener != null) {
                        mListener.onCommandError();
                    }
                    return;
                }
                Log.i(TAG, "print: 打印... ");
                // 发送数据
                DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);
            }
        }));

        connectUSB(MyApplication.sApplication);
    }


    /**
     * 重新连接回收上次连接的对象，避免内存泄漏
     */
    public void closeport(){
        Log.i(TAG, "closeport: 开始关闭端口 ");
        if(DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id]!=null&&DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort!=null) {
            Log.i(TAG, "closeport: 关闭端口 ");
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].reader.cancel();
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort.closePort();
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort=null;
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        Log.i(TAG, "release: 释放资源 ");
        DeviceConnFactoryManager.closeAllPort();
        if (ThreadPool.getInstantiation() != null) {
            ThreadPool.getInstantiation().stopThreadPool();
        }
    }

    public void closeAllPort() {
        DeviceConnFactoryManager.closeAllPort();
    }

}
