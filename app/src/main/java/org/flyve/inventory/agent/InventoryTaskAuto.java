package org.flyve.inventory.agent;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Xml;

import com.flyvemdm.inventory.categories.Categories;

import org.flyve.inventory.agent.utils.FlyveLog;
import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;

public class InventoryTaskAuto {

    /*
     * TODO: Implémenter l'inventaire sous forme de Hashmap/Hashtable
     * <string,string> pour le moment
     */

    public ArrayList<Categories> mContent = null;
    public Date mStart = null, mEnd = null;
    public Context ctx = null;
    static final int OK = 0;
    static final int NOK = 1;

    public Boolean running = false;
    public int progress = 0;

    private AutoInventory mAgent;
    private FusionInventoryApp mFusionApp;

    public InventoryTaskAuto(AutoInventory test) {
        mAgent= test;
        ctx = mAgent.getApplicationContext();
        mFusionApp = (FusionInventoryApp) mAgent.getApplication();
        FlyveLog.log(this, "FusionInventoryApp = " + mFusionApp.toString(), Log.VERBOSE);
    }

    public String toXML() {

        if (mContent != null) {

            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();

            try {
                serializer.setOutput(writer);
                serializer
                    .setFeature(
                            "http://xmlpull.org/v1/doc/features.html#indent-output",
                            true);
                // indentation as 3 spaces

                serializer.startDocument("utf-8", true);
                // Start REQUEST
                serializer.startTag(null, "REQUEST");
                // Start CONTENT
                serializer.startTag(null, "QUERY");
                serializer.text("INVENTORY");
                serializer.endTag(null, "QUERY");

                serializer.startTag(null, "DEVICEID");
                serializer.text(mFusionApp.getDeviceID());
                serializer.endTag(null, "DEVICEID");

                serializer.startTag(null, "CONTENT");
                // Start ACCESSLOG
                serializer.startTag(null, "ACCESSLOG");

                serializer.startTag(null, "LOGDATE");

                serializer.text(DateFormat.format("yyyy-mm-dd hh:MM:ss", mStart)
                        .toString());
                serializer.endTag(null, "LOGDATE");

                serializer.startTag(null, "USERID");
                serializer.text("N/A");
                serializer.endTag(null, "USERID");

                serializer.endTag(null, "ACCESSLOG");
                // End ACCESSLOG

                //Manage accountinfos :: TAG
                if (!mFusionApp.getTag().equals("")) {
                    serializer.startTag(null, "ACCOUNTINFO");
                    serializer.startTag(null, "KEYNAME");
                    serializer.text("TAG");
                    serializer.endTag(null, "KEYNAME");
                    serializer.startTag(null, "KEYVALUE");
                    serializer.text(mFusionApp.getTag());
                    serializer.endTag(null, "KEYVALUE");
                    serializer.endTag(null, "ACCOUNTINFO");
                }

                for (Categories cat : mContent) {

                    cat.toXML(serializer);
                }

                serializer.endTag(null, "CONTENT");
                serializer.endTag(null, "REQUEST");
                serializer.endDocument();
                return (writer.toString());
            } catch (Exception e) {
                // TODO: handle exception
                throw new RuntimeException(e);
            }

        }
        return null;
    }


    @SuppressWarnings("unchecked")
        public synchronized void run() {

            running = true;
            mStart = new Date();

            mContent = new ArrayList<Categories>();

            String [] categories = {
                //                "PhoneStatus",
                "Hardware",
                "Bios",
                "Memory",
                "Inputs",
                "Sensors",
                "Drives",
                "Cpus",
                "Simcards",
                "Videos",
                "Cameras",
                "Networks",
                //                "LocationProviders",
                "Envs",
                "Jvm",
                "Softwares"
                    //                "Usbs",
                    //              "Battery",
                    //              "BluetoothAdapterCategory", // <- there is already a BluetoothAdapter class in android SDK
            };

            Class<Categories> cat_class;

            for(String c : categories) {
                cat_class = null;
                FlyveLog.log(this, String.format("INVENTORY of %s", c),Log.VERBOSE);
                try {
                    cat_class = (Class <Categories>) Class.forName(String.format("org.fusioninventory.categories.%s",c));
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(cat_class!=null) {
                    try {
                        Constructor<Categories> co = cat_class.getConstructor(Context.class);
                        mContent.add(co.newInstance(mFusionApp));
                    } catch (SecurityException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }


            FlyveLog.log(this, "end of loadInventory", Log.INFO);
            mEnd = new Date();
            running = false;
        }
}
