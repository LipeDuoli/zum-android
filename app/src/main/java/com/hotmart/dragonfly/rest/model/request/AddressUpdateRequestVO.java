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
package com.hotmart.dragonfly.rest.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class AddressUpdateRequestVO {

	@SerializedName("label")
	private String mLabel;

	@SerializedName("availableItems")
	private Set<Long> mAvailableItems;

	public String getLabel() {
		return mLabel;
	}

	public void setLabel(String label) {
		this.mLabel = label;
	}

	public Set<Long> getAvailableItems() {
		return mAvailableItems;
	}

	public void setAvailableItems(Set<Long> availableItems) {
		this.mAvailableItems = availableItems;
	}

}
