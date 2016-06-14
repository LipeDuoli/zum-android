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
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.rest.model.response.PageableList;
import com.hotmart.dragonfly.rest.model.response.VerificationResponseVO;
import com.hotmart.dragonfly.tools.DateUtils;
import com.hotmart.dragonfly.ui.CollectionRecyclerViewAdapter;

import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckLogAdapter extends CollectionRecyclerViewAdapter<CheckLogAdapter.ViewHolder, VerificationResponseVO> {


    public CheckLogAdapter(Context context) {
        super(context);
    }

    public CheckLogAdapter(Context context, Collection<VerificationResponseVO> dataset) {
        super(context, dataset);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_list, null, false);
        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, VerificationResponseVO object) {
        holder.setModel(object);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private Context mContext;

        @BindView(R.id.history_label)
        TextView mLabel;

        @BindView(R.id.history_date)
        TextView mVerificationDate;

        @BindView(R.id.history_address)
        TextView mAddress;
        private VerificationResponseVO model;

        public ViewHolder(View view) {
            super(view);
            mContext = view.getContext();
            ButterKnife.bind(this, view);
        }

        public void setModel(VerificationResponseVO model) {
            mLabel.setText(model.getAddress().getLabel());
            mVerificationDate.setText(DateUtils.formatDate(model.getVerificationDate()));
            mAddress.setText(String.format(mContext.getString(R.string.history_address),model.getAddress().getAddress(),
                    model.getAddress().getNumber(),
                    model.getAddress().getNeighborhood(),
                    model.getAddress().getZipCode()));
        }
    }
}
