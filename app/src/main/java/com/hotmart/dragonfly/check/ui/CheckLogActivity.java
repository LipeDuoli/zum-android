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
package com.hotmart.dragonfly.check.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hotmart.dragonfly.R;

import com.hotmart.dragonfly.rest.model.response.PageableList;
import com.hotmart.dragonfly.rest.model.response.VerificationResponseVO;

import com.hotmart.dragonfly.rest.service.ApiServiceFactory;
import com.hotmart.dragonfly.rest.service.CheckLogService;
import com.hotmart.dragonfly.ui.BaseActivity;
import com.hotmart.dragonfly.ui.DividerItemDecoration;

import java.util.LinkedHashSet;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckLogActivity extends BaseActivity {

    @BindView(R.id.list_history)
    RecyclerView mListHistory;
    @BindView(R.id.progress_loading)
    LinearLayout mProgressLoading;
    @BindView(R.id.error_request)
    LinearLayout mErrorRequest;

    private CheckLogAdapter mCheckLogAdapter;
    private CheckLogService mCheckLogService;

    public static Intent createIntent(Context context) {
        return new Intent(context, CheckLogActivity.class);
    }

    @Override
    protected int getSelfNavigationMenuItem() {
        return R.id.nav_check_log;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_log_activity);

        mListHistory.setHasFixedSize(true);
        mListHistory.setLayoutManager(new LinearLayoutManager(this));
        mListHistory.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));
        mCheckLogAdapter = new CheckLogAdapter(this, new LinkedHashSet<VerificationResponseVO>());
        mListHistory.setAdapter(mCheckLogAdapter);

        getVerification();
    }

    private void getVerification() {
        mCheckLogService = ApiServiceFactory.createService(CheckLogService.class, this);

        mCheckLogService.get().enqueue(new Callback<PageableList<VerificationResponseVO>>() {
            @Override
            public void onResponse(Call<PageableList<VerificationResponseVO>> call, Response<PageableList<VerificationResponseVO>> response) {
                if(response.isSuccessful()){
                    mProgressLoading.setVisibility(View.GONE);
                    mListHistory.setVisibility(View.VISIBLE);
                    mCheckLogAdapter.addAll(response.body().getData());
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<PageableList<VerificationResponseVO>> call, Throwable t) {
                showError();
            }
        });
    }

    private void showError() {
        mProgressLoading.setVisibility(View.GONE);
        mListHistory.setVisibility(View.GONE);
        mErrorRequest.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.try_again)
    public void onClickTryAgain(View v) {
        mProgressLoading.setVisibility(View.VISIBLE);
        mListHistory.setVisibility(View.GONE);
        mErrorRequest.setVisibility(View.GONE);
        getVerification();
    }

}
