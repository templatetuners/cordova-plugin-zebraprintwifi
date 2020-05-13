// based on 
// https://www.outsystems.com/blog/posts/how-to-create-a-cordova-plugin-from-scratch/

package com.justapplications.cordova.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// The native Toast API
import android.widget.Toast;

import android.net.Uri;
import android.os.Environment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
//import android.os.Looper;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;

// Cordova-required packages
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.device.ZebraIllegalArgumentException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.zebra.sdk.printer.SGD;
import com.zebra.sdk.printer.PrinterStatus;
import com.zebra.sdk.printer.PrinterStatusMessages;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.ZebraPrinterLinkOs;

public class zebraprintwifi extends CordovaPlugin {

    //final String DEFAULT_IP_ADDRESS = "192.168.128.212";
    //private static final String DURATION_LONG = "long";

    //private static final String LOG_TAG = "Printer";
    //private LabelPrinter printer;

    private int rotationAngle = 90;

    private Connection printerConnection;
   
    private ZebraPrinter printer;
    //private EditText macAddress, ipDNSAddress, portNumber;

    private static File file = null;

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        // Create an instance( LabelPrinter class )
        //printer = new LabelPrinter();
    }

    public void disconnect(int portType, String address) {
        try {
            //setStatus("Disconnecting", Color.RED);
            //Toast.makeText(cordova.getActivity(), "Disconnecting from "+address, Toast.LENGTH_SHORT).show();
            if (printerConnection != null) {
                printerConnection.close();
            }
            //setStatus("Not Connected", Color.RED);
            Toast.makeText(cordova.getActivity(), "Disconnected - "+address, Toast.LENGTH_SHORT).show();
        } catch (ConnectionException e) {
            //setStatus("COMM Error! Disconnected", Color.RED);
            Toast.makeText(cordova.getActivity(), "COMM Error! Disconnected", Toast.LENGTH_SHORT).show();
        } finally {
            //enableTestButton(true);
            // dupa conectare pot rula ceva gen activarea unui buton sau print
        }
    }

    public ZebraPrinter connect(int portType, String address) {
        //setStatus("Connecting...", Color.YELLOW);
        //Toast.makeText(cordova.getActivity(), "Connecting...", Toast.LENGTH_SHORT).show();
        printerConnection = null;
        
        try {
            //int port = Integer.parseInt(portType);
            printerConnection = new TcpConnection(address, 9100);
            //SettingsHelper.saveIp(this, address);
            //SettingsHelper.savePort(this, port);
        } catch (NumberFormatException e) {
            //setStatus("Port Number Is Invalid", Color.RED);
            Toast.makeText(cordova.getActivity(), "Port Number Is Invalid", Toast.LENGTH_SHORT).show();
            return null;
        }
        
        try {
            printerConnection.open();
            //setStatus("Connected", Color.GREEN);
            Toast.makeText(cordova.getActivity(), "Connected", Toast.LENGTH_LONG).show();
        } catch (ConnectionException e) {
            //setStatus("Comm Error! Disconnecting", Color.RED);
            Toast.makeText(cordova.getActivity(), "Comunication Error", Toast.LENGTH_SHORT).show();
            //DemoSleeper.sleep(1000);
            //disconnect(9100, address);
        }

        ZebraPrinter printer = null;

        if (printerConnection.isConnected()) {
            try {
                printer = ZebraPrinterFactory.getInstance(printerConnection);
                //setStatus("Determining Printer Language", Color.YELLOW);
                //Toast.makeText(cordova.getActivity(), "Determining Printer Language", Toast.LENGTH_SHORT).show();
                PrinterLanguage pl = printer.getPrinterControlLanguage();
                //setStatus("Printer Language " + pl, Color.BLUE);
                Toast.makeText(cordova.getActivity(), "Connected! Printer Language " + pl, Toast.LENGTH_SHORT).show();
            } catch (ConnectionException e) {
                //setStatus("Unknown Printer Language", Color.RED);
                Toast.makeText(cordova.getActivity(), "Unknown Printer Language", Toast.LENGTH_SHORT).show();
                printer = null;
                //DemoSleeper.sleep(1000);
                //disconnect(9100, address);
            } catch (ZebraPrinterLanguageUnknownException e) {
                //setStatus("Unknown Printer Language", Color.RED);
                Toast.makeText(cordova.getActivity(), "Unknown Printer Language", Toast.LENGTH_SHORT).show();
                printer = null;
                //DemoSleeper.sleep(1000);
                //disconnect(9100, address);
            }
        }

        return printer;
    }

    private Bitmap rotateBitmap(final Bitmap bitmap, int rotationAngle) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationAngle);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    private void printPhotoFromExternal(final Bitmap bitmap, String address) {
        //Connection connection = new TcpConnection(address, 9100);
        printer = connect(9100, address);
        if (printer != null) {
            try {
                //Looper.prepare();
                //helper.showLoadingDialog("Sending image to printer");
                //Toast.makeText(cordova.getActivity(), "Sending image to printer", Toast.LENGTH_SHORT).show();
                //ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
                        
                //Bitmap rotatedBitmap = rotateBitmap(bitmap, rotationAngle);
                //printer.printImage(new ZebraImageAndroid(rotatedBitmap), 0, 140, 620, 1100, false); //550,412
                
                //https://techdocs.zebra.com/link-os/2-12/webservices/content/com/zebra/sdk/printer/GraphicsUtil.html

                printer.printImage(new ZebraImageAndroid(bitmap), -50, 0, 619, 1132, false); //550,412

                //printImage(String imageFilePath, int x, int y, int width, int height, boolean insideFormat)
                //Prints an image from the connecting device file system to the connected device as a monochrome image.
                //x - horizontal starting position in dots.
                //y - vertical starting position in dots.

                //connection.close();
                Toast.makeText(cordova.getActivity(), "Label Printed", Toast.LENGTH_SHORT).show();
    
                //if (file != null) {
                    //file.delete();
                    //file = null;
                //}
            	//file = new File(Environment.getExternalStorageDirectory(), "ORD0000002600.png");
                
            } catch (ConnectionException e) {
                //helper.showErrorDialogOnGuiThread(e.getMessage());
                Toast.makeText(cordova.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            //} catch (ZebraPrinterLanguageUnknownException e) {
                //helper.showErrorDialogOnGuiThread(e.getMessage());
                //Toast.makeText(cordova.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            //} catch (ZebraIllegalArgumentException e) {
                //helper.showErrorDialogOnGuiThread(e.getMessage());
                //Toast.makeText(cordova.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                bitmap.recycle();
                //helper.dismissLoadingDialog();
                //Looper.myLooper().quit();
            }
        } else {
            Toast.makeText(cordova.getActivity(), "Bad connection. Please restart the printer.", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * Returns the command for a test label depending on the printer control language
    * The test label is a box with the word "TEST" inside of it
    * 
    * _________________________
    * |                       |
    * |                       |
    * |        TEST           |
    * |                       |
    * |                       |
    * |_______________________|
    * 
    * 
    */
    private void sendTestLabel(int portType, String address) {
        printer = connect(9100, address);

        byte[] configLabel = null;

        if (printer != null) {
            try {
                PrinterLanguage printerLanguage = printer.getPrinterControlLanguage();
                
                if (printerLanguage == PrinterLanguage.ZPL) {
                    configLabel = "^XA^FO17,16^GB379,371,8^FS^FT65,255^A0N,135,134^FDTEST^FS^XZ".getBytes();
                } else if (printerLanguage == PrinterLanguage.CPCL) {
                    String cpclConfigLabel = "! 0 200 200 406 1\r\n" + "ON-FEED IGNORE\r\n" + "BOX 20 20 380 380 8\r\n" + "T 0 6 137 177 TEST\r\n" + "PRINT\r\n";
                    configLabel = cpclConfigLabel.getBytes();
                }

                printerConnection.write(configLabel);
                
            } catch (ConnectionException e) {
                Toast.makeText(cordova.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();            
            }
        } else {
            Toast.makeText(cordova.getActivity(), "Please connect with green button", Toast.LENGTH_SHORT).show();            
        }
    }

    public void PrinterStatus(int portType, String address) {
        if (printerConnection.isConnected()) {
            try {
                if (printerConnection != null) {
                    printerConnection.close();
                }
            } catch (ConnectionException e) {
                Toast.makeText(cordova.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        Connection connection = new TcpConnection(address, 9100);
        //printer = connect(9100, address);
        try {
            connection.open();
            ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
            PrinterStatus printerStatus = printer.getCurrentStatus();
            if (printerStatus.isReadyToPrint) {
                //System.out.println("Ready To Print");
                Toast.makeText(cordova.getActivity(),"Ready To Print", Toast.LENGTH_SHORT).show();
            } else if (printerStatus.isPaused) {
                //System.out.println("Cannot Print because the printer is paused.");
                Toast.makeText(cordova.getActivity(), "Cannot Print because the printer is paused.", Toast.LENGTH_SHORT).show();
             } else if (printerStatus.isHeadOpen) {
                //System.out.println("Cannot Print because the printer head is open.");
                Toast.makeText(cordova.getActivity(), "Cannot Print because the printer head is open.", Toast.LENGTH_SHORT).show();
            } else if (printerStatus.isPaperOut) {
                //System.out.println("Cannot Print because the paper is out.");
                Toast.makeText(cordova.getActivity(), "Cannot Print because the paper is out.", Toast.LENGTH_SHORT).show();
            } else {
                //System.out.println("Cannot Print.");
                Toast.makeText(cordova.getActivity(), "Cannot Print.", Toast.LENGTH_SHORT).show();
            }
            connection.close();
        } catch (ConnectionException e) {
            //e.printStackTrace();
            Toast.makeText(cordova.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();       
        } catch (ZebraPrinterLanguageUnknownException e) {
            //e.printStackTrace();
            Toast.makeText(cordova.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            //connection.close();
            //connect(9100, address);
        }
    }

    private void printPDF(String message, String address){
        //Connection connection = new TcpConnection(address, 9100);         
        //try {             
            //connection.open(); 
            //Toast.makeText(cordova.getActivity(), "Sending PDF to printer", Toast.LENGTH_SHORT).show();            
            //ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
            //connection.sendFileContents(message);   
            //connection.close();      
        //} catch (ConnectionException e) {             
            //e.printStackTrace();         
            //Toast.makeText(cordova.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        //} catch (ZebraPrinterLanguageUnknownException e) {
            //e.printStackTrace();
            //Toast.makeText(cordova.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        //} catch (ZebraIllegalArgumentException e) {
            //e.printStackTrace();
        //} finally {
            //connection.close();
            //Toast.makeText(cordova.getActivity(), "Printed", Toast.LENGTH_SHORT).show();
        //}
    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {
        // Verify that the user sent a correct action
        String message;
        String address;
        try {
            JSONObject options = args.getJSONObject(0);
            message = options.getString("message");
            address = options.getString("address");
        } catch (JSONException e) {
            callbackContext.error("Error encountered: " + e.getMessage());
            return false;
        }
        if(action.equals("connect")) {
            //int port = Integer.parseInt(message);
            connect(9100, address);
            //status(printer);
        } else if(action.equals("testprint")){
            sendTestLabel(9100, address);
        } else if(action.equals("print")) {
            Uri imgPath = Uri.parse(message);//.getData();
            Bitmap myBitmap = null;
            try {
                myBitmap = MediaStore.Images.Media.getBitmap(cordova.getActivity().getContentResolver(), imgPath);
                //Media.getBitmap(getContentResolver(), imgPath);
            } catch (FileNotFoundException e) {
                //helper.showErrorDialog(e.getMessage());
                Toast.makeText(cordova.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                //helper.showErrorDialog(e.getMessage());
                Toast.makeText(cordova.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            printPhotoFromExternal(myBitmap, address);
        } else if(action.equals("printpdf")) {
            printPDF(message, address);
        } else if(action.equals("disconnect")){
            //int port = Integer.parseInt(message);
            disconnect(9100, address);
        } else if(action.equals("status")){
            PrinterStatus(9100, address);
        } else {
            callbackContext.error("\"" + action + "\" is not a recognized action.");
            return false;
        }
        // Send a positive result to the callbackContext
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
        callbackContext.sendPluginResult(pluginResult);
        return true;
    }
}