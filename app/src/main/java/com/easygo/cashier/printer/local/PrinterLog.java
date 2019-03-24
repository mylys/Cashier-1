package com.easygo.cashier.printer.local;

import com.niubility.library.base.BaseApplication;
import com.niubility.library.utils.FileUtils;
import com.niubility.library.utils.LogUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PrinterLog {

    public static File logFile;
    public static boolean writeToFile = true;
    private static SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.CHINA);


    public static void write(String content) {
        if(writeToFile) {
            if(logFile == null) {
                logFile = new File(BaseApplication.logDir, LogUtils.getFormatFileName("usb_printer"));
            }


            StringBuilder sb = new StringBuilder();
            sb.append("==================== ")
                    .append(sdf.format(new Date())).append("\n")
                    .append("isOpenPort: ")
                        .append((DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0] != null
                            && DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].getConnState()))
                    .append("\n")
                    .append("currentCommand： ")
                        .append((DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0] != null
                                ? DeviceConnFactoryManager.getDeviceConnFactoryManagers()[0].getCurrentPrinterCommand(): ""))
                    .append("\n")
                    .append("CMD数量： ").append(PrinterUtils.getInstance().queue.size()).append("\n")
                    .append("log: ").append(content).append("\n")
                    .append("\n");
            FileUtils.writeToFile(logFile, sb.toString(), true);
        }
    }

}
