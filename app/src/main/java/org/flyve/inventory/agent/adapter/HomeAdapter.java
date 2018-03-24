/*
 * Copyright Teclib. All rights reserved.
 *
 * Flyve MDM is a mobile device management software.
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
 * @copyright Copyright Teclib. All rights reserved.
 * @license   GPLv3 https://www.gnu.org/licenses/gpl-3.0.html
 * @link      https://github.com/flyve-mdm/android-inventory-agent
 * @link      https://flyve-mdm.com
 * ------------------------------------------------------------------------------
 */

package org.flyve.inventory.agent.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.flyve.inventory.agent.R;
import org.flyve.inventory.agent.core.home.HomeSchema;

import java.util.List;

public class HomeAdapter extends BaseAdapter {

	private List<HomeSchema> data;
	private LayoutInflater inflater = null;

	public HomeAdapter(Activity activity, List<HomeSchema> data) {
		this.data = data;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * Get count of the data
	 * @return int the data size 
	 */
	@Override
	public int getCount() {
		return this.data.size();
	}

	/**
	 * Get the data item associated with the specified position
     * @param position of the item whose data we want
	 * @return Object the data at the specified position
 	 */
	@Override
	public Object getItem(int position) {
		return position;
	}

	/**
	 * Get the row ID associated with the specified position
	 * @param position of the item whose row ID we want
	 * @return long the ID of the item at the specified position
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Get a View that displays the data at the specified position
	 * @param position of the item within the adapter's data set of the item whose View we want
	 * @param convertView the old View to reuse, if possible
	 * @param parent the parent that this View will eventually be attached to
	 * @return View a View corresponding to the data at the specified position
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		HomeSchema homeSchema = this.data.get(position);
		View vi = inflater.inflate(R.layout.list_item_home_button, null);;

		if(homeSchema.getHeader()) {
			vi = inflater.inflate(R.layout.list_item_home_header, null);
		}

		if(homeSchema.getHasCheck()) {
			vi = inflater.inflate(R.layout.list_item_home_check, null);

			CheckBox chkValue = vi.findViewById(R.id.chkValue);
			chkValue.setChecked(homeSchema.getCheckValue());
		}

		TextView txtTitle = vi.findViewById(R.id.txtTitle);
		txtTitle.setText(homeSchema.getTitle());

		if(!homeSchema.getHeader()) {
			TextView txtSubTitle = vi.findViewById(R.id.txtSubTitle);
			txtSubTitle.setText(homeSchema.getSubTitle());
		}

		return vi;
	}
}
