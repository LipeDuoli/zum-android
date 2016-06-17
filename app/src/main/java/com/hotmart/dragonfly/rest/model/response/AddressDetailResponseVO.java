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
package com.hotmart.dragonfly.rest.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AddressDetailResponseVO extends AddressResponseVO implements Parcelable{

	@SerializedName("checklistItems")
	private  List<ChecklistItemResponseVO> checklistItems;

	public List<ChecklistItemResponseVO> getChecklistItems() {
		return checklistItems;
	}

	public void setChecklistItems(List<ChecklistItemResponseVO> checklistItems) {
		this.checklistItems = checklistItems;
	}

	public AddressDetailResponseVO() {}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeTypedList(this.checklistItems);
	}

	protected AddressDetailResponseVO(Parcel in) {
		super(in);
		this.checklistItems = in.createTypedArrayList(ChecklistItemResponseVO.CREATOR);
	}

	public static final Creator<AddressDetailResponseVO> CREATOR = new Creator<AddressDetailResponseVO>() {
		@Override
		public AddressDetailResponseVO createFromParcel(Parcel source) {
			return new AddressDetailResponseVO(source);
		}

		@Override
		public AddressDetailResponseVO[] newArray(int size) {
			return new AddressDetailResponseVO[size];
		}
	};
}
