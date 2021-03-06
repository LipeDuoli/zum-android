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
package com.hotmart.dragonfly.authenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpInterceptor implements Interceptor {

    private Context mContext;

    public HttpInterceptor(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();

        AccountManager accountManager = AccountManager.get(mContext);
        Account[] accounts = accountManager.getAccountsByType(AuthConstants.ACCOUNT_TYPE);
        if (accounts.length != 0) {
            String token = accountManager.peekAuthToken(accounts[0], AuthConstants.AUTHTOKEN_TYPE);
            if (token != null) {
                builder.header("Authorization", "Bearer " + token);
            }
        }

        request = builder.build();
        return chain.proceed(request);
    }
}
