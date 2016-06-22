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
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.rest.model.request.AddressUpdateRequestVO;
import com.hotmart.dragonfly.rest.model.response.AddressDetailResponseVO;
import com.hotmart.dragonfly.rest.model.response.AddressResponseVO;
import com.hotmart.dragonfly.rest.model.response.ChecklistItemResponseVO;
import com.hotmart.dragonfly.rest.service.AddressService;
import com.hotmart.dragonfly.rest.service.ApiServiceFactory;
import com.hotmart.dragonfly.tools.LogUtils;
import com.hotmart.dragonfly.ui.BaseActivity;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by felipe.arimateia on 6/16/2016.
 */

public class PlaceEditActivity extends BaseActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private static final String TAG = LogUtils.makeLogTag(PlaceEditActivity.class);

    public static final String EXTRA_ADDRESS = "extra_address";

    @BindView(R.id.place_label)
    AppCompatTextView placeLabel;

    @BindView(R.id.place_address)
    AppCompatTextView placeAddress;

    @BindView(R.id.list_checklist)
    RecyclerView listChecklist;

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;

    @BindView(R.id.header_place)
    LinearLayout headerPlace;

    @BindView(R.id.error_request)
    LinearLayout errorRequest;

    private AddressService mAddressService;
    private Call<AddressDetailResponseVO> mCallAddress;

    private Long mIdAddress;
    private AddressDetailResponseVO mAddress;
    private CheckListItemsAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_edit);

        AddressResponseVO address = getIntent().getParcelableExtra(EXTRA_ADDRESS);
        mIdAddress = address.getId();

        setTitle(address.getLabel());

        setUpToolbar();

        placeLabel.setText(address.getLabel());
        placeAddress.setText(address.getFormattedAddress());

        mAdapter = new CheckListItemsAdapter(this, new ArrayList<ChecklistItemResponseVO>());

        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        listChecklist.setLayoutManager(manager);
        listChecklist.setAdapter(mAdapter);

        getAddressDetail(mIdAddress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_place_edit, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mCallAddress != null && !mCallAddress.isExecuted()) {
            mCallAddress.cancel();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onClose() {
        return false;
    }
    @OnClick(R.id.btn_save)
    public void onClickSave() {

        if (mAddress == null) {
            return;
        }

        progressBar.show();

        AddressUpdateRequestVO requestVO = new AddressUpdateRequestVO();
        requestVO.setLabel(mAddress.getLabel());
        requestVO.setAvailableItems(new HashSet<Long>());

        for (ChecklistItemResponseVO item : mAddress.getChecklistItems()) {
            if (item.isAvailable()) {
                requestVO.getAvailableItems().add(item.getId());
            }
        }
        mAddressService.put(mIdAddress, requestVO).enqueue(new Callback<AddressResponseVO>() {
            @Override
            public void onResponse(Call<AddressResponseVO> call, Response<AddressResponseVO> response) {

                progressBar.hide();

                int message =  R.string.msg_success_save_address;

                if (response.isSuccessful()) {

                    finish();
                }
                else {
                  message =  R.string.msg_erro_save_address;
                }

                Toast.makeText(PlaceEditActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onFailure(Call<AddressResponseVO> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);

                Toast.makeText(PlaceEditActivity.this, R.string.msg_erro_save_address, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void setUpToolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void getAddressDetail(Long id) {

        mAddressService = ApiServiceFactory.createService(AddressService.class, this);
        mCallAddress = mAddressService.get(id);
        mCallAddress.enqueue(new Callback<AddressDetailResponseVO>() {
            @Override
            public void onResponse(Call<AddressDetailResponseVO> call,
                                   Response<AddressDetailResponseVO> response) {

                if (response.isSuccessful()) {
                    mAddress = response.body();
                    mAdapter.addAll(mAddress.getChecklistItems());
                    progressBar.hide();
                } else {
                    LogUtils.LOGD(TAG, "Status Code: " + response.code());
                    showError();
                }
            }

            @Override
            public void onFailure(Call<AddressDetailResponseVO> call, Throwable t) {
                LogUtils.LOGE(TAG, t.getMessage(), t);
                showError();
            }
        });
    }

    private void showError() {
        progressBar.hide();
        headerPlace.setVisibility(View.INVISIBLE);
        errorRequest.setVisibility(View.VISIBLE);
    }


}
