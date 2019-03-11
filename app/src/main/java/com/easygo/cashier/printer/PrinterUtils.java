package com.easygo.cashier.printer;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.easygo.cashier.R;
import com.tools.command.EscCommand;
import com.tools.command.LabelCommand;

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

    /**
     * TSC查询打印机状态指令
     */
    public int id = 0;

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;


    //广播
    /**USB权限请求*/
    public static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    /**USB连接状态*/
    public static final String ACTION_CONN_STATE = DeviceConnFactoryManager.ACTION_CONN_STATE;
    /**USB连接状态*/
    public static final String ACTION_USB_DEVICE_DETACHED = android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;
    public static final String ACTION_USB_DEVICE_ATTACHED = android.hardware.usb.UsbManager.ACTION_USB_DEVICE_ATTACHED;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case PrinterUtils.ACTION_USB_PERMISSION:
                    synchronized (this) {
                        UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (device != null) {
                                Log.i(TAG, "onReceive: permission ok for device " + device);
                                usbConn(context, device);
                            }
                        } else {
                            Log.i(TAG, "onReceive: permission denied for device " + device);
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
                            if (id == deviceId) {
                                if(mListener != null) {
                                    mListener.onDisconnected();
                                }
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                            if(mListener != null) {
                                mListener.onConnecting();
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                            if(mListener != null) {
                                mListener.onConnected();
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_FAILED:
                            if(mListener != null) {
                                mListener.onConnectFailed();
                            }
                            break;
                        default:
                            break;
                    }
                    break;
            }
        }
    };

    /**
     * 注册广播
     */
    public void registerReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter(PrinterUtils.ACTION_USB_PERMISSION);
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
        void onConnectFailed();
        void onCommandError();
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
            return STATE_DISCONNECTED;
        }
        ThreadPool.getInstantiation().addTask(new Runnable() {
            @Override
            public void run() {
                Vector<Byte> data = new Vector<>(esc.length);
                if (isEscPrinterCommand()) {

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
                return deviceConnFactoryManager.usbDevice().getDeviceName();
            }
        }
        return "没有已连接USB打印机";
    }

    /**
     * 连接USB打印机
     * @param context
     */
    public void connectUSB(Context context) {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        List<String> usbDeviceFromName = Utils.getUsbDeviceFromName(context);
        if(usbDeviceFromName.size() > 0) {
            String usbName = usbDeviceFromName.get(0);
            //通过USB设备名找到USB设备
            UsbDevice usbDevice = Utils.getUsbDeviceFromName(context, usbName);
            //判断USB设备是否有权限
            if (usbManager.hasPermission(usbDevice)) {
                usbConn(context, usbDevice);
            } else {//请求权限
                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0,
                        new Intent(PrinterUtils.ACTION_USB_PERMISSION), 0);
                usbManager.requestPermission(usbDevice, mPermissionIntent);
            }
        }
    }


    public void usbConn(Context context, UsbDevice usbDevice) {
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
        //打印机状态查询
        if(isPrinterDisconnected()) {
            if(mListener != null) {
                mListener.onDisconnected();
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
        });
    }

    public void cutPaper() {
        if(isPrinterDisconnected()) {
            if(mListener != null) {
                mListener.onDisconnected();
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
    public void print(Vector<Byte> datas) {
//        if(count > 1) {//连续打印
//
//        } else {
//
//        }
        // 发送数据
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);
    }


    /**
     * 重新连接回收上次连接的对象，避免内存泄漏
     */
    public void closeport(){
        if(DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id]!=null&&DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort!=null) {
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].reader.cancel();
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort.closePort();
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort=null;
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        DeviceConnFactoryManager.closeAllPort();
        if (ThreadPool.getInstantiation() != null) {
            ThreadPool.getInstantiation().stopThreadPool();
        }
    }

}
