/*
 *   Copyright © 2017 Teclib. All rights reserved.
 *
 * This file is part of flyve-mdm-android-agent
 *
 * flyve-mdm-android-agent is a subproject of Flyve MDM. Flyve MDM is a mobile
 * device management software.
 *
 * Flyve MDM is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * Flyve MDM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * ------------------------------------------------------------------------------
 * @author    Rafael Hernandez
 * @date      02/06/2017
 * @copyright Copyright © ${YEAR} Teclib. All rights reserved.
 * @license   GPLv3 https://www.gnu.org/licenses/gpl-3.0.html
 * @link      https://github.com/flyve-mdm/flyve-mdm-android-agent
 * @link      https://flyve-mdm.com
 * ------------------------------------------------------------------------------
 */

package org.flyve.inventory.agent.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ConnectionHTTP {

	private static Handler uiHandler;

	static {
		uiHandler = new Handler(Looper.getMainLooper());
	}

	private static int timeout = 18000;
	private static int readtimeout = 6000;

	public static void runOnUI(Runnable runnable) {
		uiHandler.post(runnable);
	}

	/**
	 * Send information by post with a JSONObject and header
	 * @param url String url
	 * @param data JSONObject data to send
	 * @param header Map with al the header information
	 * @param callback DataCallback
	 */
	public static void webData(final String url, final String data, final Map<String, String> header, final DataCallback callback)
	{
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					URL dataURL = new URL(url);
					FlyveLog.i("Method: POST - URL = " + url);
					HttpURLConnection conn = (HttpURLConnection)dataURL.openConnection();

					conn.setRequestMethod("POST");
					conn.setConnectTimeout(timeout);
					conn.setReadTimeout(readtimeout);

					for (Map.Entry<String, String> entry : header.entrySet()) {
						conn.setRequestProperty(entry.getKey(), entry.getValue());
						FlyveLog.d(entry.getKey() + " = " + entry.getValue());
					}

					// Send post request
					conn.setDoOutput(true);

					DataOutputStream os = new DataOutputStream(conn.getOutputStream());
					os.writeBytes(data);
					os.flush();
					os.close();

					if(conn.getResponseCode() >= 400) {
						InputStream is = conn.getErrorStream();
						final String result = inputStreamToString(is);

						ConnectionHTTP.runOnUI(new Runnable()
						{
							public void run()
							{
								callback.callback(result);
							}
						});
						return;
					}

					InputStream is = conn.getInputStream();
					final String result = inputStreamToString(is);

					ConnectionHTTP.runOnUI(new Runnable() {
						public void run() {
							callback.callback(result);
						}
					});

				}
				catch (final Exception ex)
				{
					ConnectionHTTP.runOnUI(new Runnable()
					{
						public void run()
						{
							callback.callback("EXCEPTION_HTTP_" + ex.getMessage());
							FlyveLog.e(ex.getClass() + " : " + ex.getMessage());
						}
					});
				}
			}
		});
		t.start();
	}

	/**
	 * Get the data in a synchronous way
	 * @param url the url
	 * @param JSONObject the data
	 * @param string HTPP methods
	 * @param Map string header
	 */
	public static String syncWebData(final String url, final String data) {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try
		{
			URL dataURL = new URL(url);
			FlyveLog.i("Method: POST - URL = " + url);
			HttpURLConnection conn = (HttpURLConnection)dataURL.openConnection();

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(readtimeout);

			// Send post request
			conn.setDoOutput(true);

			DataOutputStream os = new DataOutputStream(conn.getOutputStream());
			os.writeBytes(data);
			os.flush();
			os.close();

			if(conn.getResponseCode() >= 400) {
				InputStream is = conn.getErrorStream();
				return inputStreamToString(is);
			}

			InputStream is = conn.getInputStream();
			return inputStreamToString(is);

		}
		catch (final Exception ex)
		{
			String error = "EXCEPTION_HTTP_" + ex.getMessage();
			FlyveLog.e(error);
			return error;
		}
	}

	/**
	 * Convert inputStream to String
	 * @param stream InputStream to convert
	 * @return String converted
	 * @throws IOException error
	 */
	private static String inputStreamToString(final InputStream stream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		br.close();
		return sb.toString();
	}

	/**
	 * This is the return data interface
	 */
	public interface DataCallback {
		void callback(String data);
	}

}
