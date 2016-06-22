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

public class ChecklistItemResponseVO implements Parcelable {

	@SerializedName("id")
	private Long mId;

	@SerializedName("name")
	private String mName;

	@SerializedName("description")
	private String mDescription;

	@SerializedName("imageUrl")
	private String imageUrl;

	@SerializedName("available")
	private boolean available;

	private transient boolean check;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Long getId() {
		return mId;
	}

	public void setId(Long id) {
		this.mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		this.mDescription = description;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public ChecklistItemResponseVO() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.mId);
		dest.writeString(this.mName);
		dest.writeString(this.mDescription);
		dest.writeString(this.imageUrl);
		dest.writeByte(this.available ? (byte) 1 : (byte) 0);
	}

	protected ChecklistItemResponseVO(Parcel in) {
		this.mId = (Long) in.readValue(Long.class.getClassLoader());
		this.mName = in.readString();
		this.mDescription = in.readString();
		this.imageUrl = in.readString();
		this.available = in.readByte() != 0;
	}

	public static final Creator<ChecklistItemResponseVO> CREATOR = new Creator<ChecklistItemResponseVO>() {
		@Override
		public ChecklistItemResponseVO createFromParcel(Parcel source) {
			return new ChecklistItemResponseVO(source);
		}

		@Override
		public ChecklistItemResponseVO[] newArray(int size) {
			return new ChecklistItemResponseVO[size];
		}
	};
}
