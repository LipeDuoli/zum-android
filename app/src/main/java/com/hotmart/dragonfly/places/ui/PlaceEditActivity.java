/*
 * This file is part of Zum.
 *
 * Zum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Zum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.hotmart.dragonfly.places.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.rest.model.response.AddressResponseVO;
import com.hotmart.dragonfly.ui.BaseActivity;

/**
 * Created by felip on 6/16/2016.
 */

public class PlaceEditActivity extends BaseActivity {

    public static final String EXTRA_ADDRESS = "extra_address";
    private AddressResponseVO address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_edit);

        address = getIntent().getParcelableExtra(EXTRA_ADDRESS);

        setTitle(address.getLabel());

        setUpToolbar();
    }

    private void setUpToolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }
}
