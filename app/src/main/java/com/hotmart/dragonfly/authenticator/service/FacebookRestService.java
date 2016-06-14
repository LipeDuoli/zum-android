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
package com.hotmart.dragonfly.authenticator.service;

import com.hotmart.dragonfly.rest.model.request.UserFacebookAccountVO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;

/**
 * @author anapsil
 */
public interface FacebookRestService {

    @POST("v1/user/facebook/bind")
    Call<Void> bind(@Body UserFacebookAccountVO userFacebookAccountVO);

    @DELETE("v1/user/facebook/unbind")
    Call<Void> unbind();
}
