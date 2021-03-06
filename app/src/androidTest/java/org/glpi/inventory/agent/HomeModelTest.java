/**
 * ---------------------------------------------------------------------
 * GLPI Android Inventory Agent
 * Copyright (C) 2019 Teclib.
 *
 * https://glpi-project.org
 *
 * Based on Flyve MDM Inventory Agent For Android
 * Copyright © 2018 Teclib. All rights reserved.
 *
 * ---------------------------------------------------------------------
 *
 *  LICENSE
 *
 *  This file is part of GLPI Android Inventory Agent.
 *
 *  GLPI Android Inventory Agent is a subproject of GLPI.
 *
 *  GLPI Android Inventory Agent is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 *
 *  GLPI Android Inventory Agent is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  ---------------------------------------------------------------------
 *  @copyright Copyright © 2019 Teclib. All rights reserved.
 *  @license   GPLv3 https://www.gnu.org/licenses/gpl-3.0.html
 *  @link      https://github.com/glpi-project/android-inventory-agent
 *  @link      https://glpi-project.org/glpi-network/
 *  ---------------------------------------------------------------------
 */

package org.glpi.inventory.agent;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.ListView;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import org.glpi.inventory.agent.core.home.Home;
import org.glpi.inventory.agent.core.home.HomeModel;
import org.glpi.inventory.agent.core.home.HomeSchema;
import org.glpi.inventory.agent.ui.ActivityMain;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4ClassRunner.class)
public class HomeModelTest {

    @Rule
    public ActivityTestRule<ActivityMain> rule  = new ActivityTestRule<>(ActivityMain.class);

    @Test
    public void setupList() {
        Home.Presenter presenter = mock(Home.Presenter.class);
        HomeModel homeModel = new HomeModel(presenter);
        ListView lst = mock(ListView.class);

        Activity activity = rule.getActivity();
        homeModel.setupList(activity, lst);
    }

    @Test
    public void clickItem() {
        Home.Presenter presenter = mock(Home.Presenter.class);
        HomeModel homeModel = new HomeModel(presenter);

        KeyEvent kdown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);

        Activity activity = rule.getActivity();
        HomeSchema homeSchema = new HomeSchema("1", "");
        homeModel.clickItem(activity, homeSchema);
        rule.getActivity().dispatchKeyEvent(kdown);
    }
}