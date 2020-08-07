package cordova.plugin.z91printer;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zcs.sdk.DriverManager;
import com.zcs.sdk.Printer;

public class z91Printer extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("print")) {
            String message = args.getString(0);
            this.print(message, callbackContext);
            return true;
        }
        return false;
    }

    private void print(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            DriverManager mDriverManager= DriverManager.getInstance();
            Printer mPrinter = mDriverManager.getPrinter();
            Sys mSys = mDriverManager.getBaseSysDevice();
            int openSerialPort() {
                int statue = mSys.getFirmwareVer(new String[1]);
                if (statue != SdkResult.SDK_OK) {
                    int sysPowerOn = mSys.sysPowerOn();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return mSys.sdkInit(ConnectTypeEnum.COM);
            }
            int printStatus = mPrinter.getPrinterStatus();
            if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
                callbackContext.error('Paper Out');
            } else {
                PrnStrFormat format = new PrnStrFormat();
                mPrinter.setPrintAppendString(message, format);
                printStatus = mPrinter.setPrintStart();
                if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
                    callbackContext.error('Paper Out');
                }
            }
            callbackContext.success('Print Ok');

        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
