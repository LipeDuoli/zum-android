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

public class UserPasswordRequestVO {

	@SerializedName("newPassword")
	private String mNewPassword;

	@SerializedName("repeatPassword")
	private String mRepeatPassword;

	@SerializedName("currentPassword")
	private String mCurrentPassword;


	public UserPasswordRequestVO(){

	}

	public UserPasswordRequestVO(String password, String newPassword, String repeatPassword){
		this.mCurrentPassword = password;
		this.mNewPassword = newPassword;
		this.mRepeatPassword = repeatPassword;
	}

	public String getNewPassword() {
		return mNewPassword;
	}

	public String getRepeatPassword() {
		return mRepeatPassword;
	}

	public String getCurrentPassword() {
		return mCurrentPassword;
	}

}


