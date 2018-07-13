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
 * @link      https://github.com/flyve-mdm/android-mdm-agent
 * @link      https://flyve-mdm.com
 * ------------------------------------------------------------------------------
 */

package org.flyve.inventory.agent.core.permission;

import android.app.Activity;

public class PermissionPresenter implements Permission.Presenter {

    private Permission.View view;
    private Permission.Model model;

    public PermissionPresenter(Permission.View view){
        this.view = view;
        model = new PermissionModel(this);
    }

    @Override
    public void showError(String message) {
        if(view!=null) {
            view.showError(message);
        }
    }

    @Override
    public void permissionSuccess() {
        if(view!=null) {
            view.permissionSuccess();
        }
    }

    @Override
    public void requestPermission(Activity activity) {
        model.requestPermission(activity);
    }

    @Override
    public void openMain(Activity activity) {
        model.openMain(activity);
    }


}
