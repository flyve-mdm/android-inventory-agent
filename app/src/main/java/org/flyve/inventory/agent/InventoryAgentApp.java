/*
 * Copyright (C) 2017 Teclib'
 *
 * This file is part of Flyve MDM Inventory Agent Android.
 *
 * Flyve MDM Inventory Agent Android is a subproject of Flyve MDM. Flyve MDM is a mobile
 * device management software.
 *
 * Flyve MDM Android is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * Flyve MDM Inventory Agent Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * ------------------------------------------------------------------------------
 * @author    Rafael Hernandez - rafaelje
 * @copyright Copyright (c) 2017 Flyve MDM
 * @license   GPLv3 https://www.gnu.org/licenses/gpl-3.0.html
 * @link      https://github.com/flyve-mdm/flyve-mdm-android-inventory-agent/
 * @link      http://www.glpi-project.org/
 * @link      https://flyve-mdm.com/
 * ------------------------------------------------------------------------------
 */
package org.flyve.inventory.agent;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;
import org.flyve.inventory.agent.utils.FlyveLog;


/**
 * This is the url ACRA Configuration
 */
@ReportsCrashes(
        formUri = "https://collector.tracepot.com/632edab5",
        reportType = HttpSender.Type.JSON,
        httpMethod = HttpSender.Method.POST,
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.STACK_TRACE
        },
        mode = ReportingInteractionMode.SILENT
)

public class InventoryAgentApp extends Application implements OnSharedPreferenceChangeListener {

    private SharedPreferences prefs;

    private Boolean mShouldAutoStart = null;
    private String mUrl = null;
    private String mLogin = null;
    private String mPassword = null;
    private String mDeviceID = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }

    /**
     * This method is called when the application is starting, it gets the default Shared Preferences
     */
    @Override
    public void onCreate() {
        super.onCreate();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        
        String deviceId = prefs.getString("device_id", null);

        FlyveLog.log(this, deviceId, Log.VERBOSE);
             
        if(deviceId == null) {
           TelephonyManager mTM= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
           prefs.edit()
                .putString("device_id", String.format("%s-%s",mTM.getDeviceId(),DateFormat.format("yyyy-MM-dd-kk-mm-ss", System.currentTimeMillis())))
                .commit();
        }
    }

    /**
     * Gets the ID of the device
     * @return string the device ID
     */
    public String getDeviceID(){
        if (mDeviceID == null) {
            mDeviceID = prefs.getString("device_id","<not set>");
        }
        return mDeviceID;
    }
    
    /**
     * Gets if it should auto start
     * @return boolean true if it should, false otherwise
     */
    public Boolean getShouldAutoStart() {
        if (mShouldAutoStart == null) {
            mShouldAutoStart = prefs.getBoolean("boot", false);
        }
        return mShouldAutoStart;

    }

    /**
     * Gets the URL
     * @return string the URL 
     */
    public String getUrl() {
        if (mUrl == null) {
            mUrl = prefs.getString("url","https://dev.flyve.org/glpi/plugins/fusioninventory/");
        }
        return mUrl;
    }

    /**
     * Called when a shared preference is changed, added, or removed
     * @param SharedPreferences the SharedPreferences that received the change
     * @param string the key of the preference that was changed, added or removed
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mUrl = null;
        mShouldAutoStart = null;
        FlyveLog.log(this, "InventoryAgentApp = " + this.toString(), Log.VERBOSE);
    }

    /**
     * Gets the credentials login
     * @return string the login
     */
    public String getCredentialsLogin() {
        if (mLogin == null) {
        	mLogin = prefs.getString("login", "");
        }
        return mLogin;
    }

    /**
     * Gets the credentials password
     * @return string the password
     */
    public String getCredentialsPassword() {
        if (mPassword == null) {
        	mPassword = prefs.getString("password", "");
        }
        return mPassword;
    }

}
