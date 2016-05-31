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
package com.hotmart.dragonfly.signup.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.authenticator.service.OAuth2ServiceFactory;
import com.hotmart.dragonfly.authenticator.service.UserService;
import com.hotmart.dragonfly.authenticator.ui.AuthenticatorActivity;
import com.hotmart.dragonfly.rest.model.request.UserSignupRequestVO;
import com.hotmart.dragonfly.tools.PicassoTransformations;
import com.hotmart.dragonfly.ui.BaseActivity;
import com.hotmart.dragonfly.validation.UniqueEmailRule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity implements Validator.ValidationListener {

    @NotEmpty(messageResId = R.string.required_field)
    @BindView(R.id.sign_up_name)
    protected EditText mName;
    @Email(messageResId = R.string.invalid_email)
    @BindView(R.id.sign_up_email)
    protected EditText mEmail;
    @Password(min = 6, messageResId = R.string.invalid_password)
    @BindView(R.id.sign_up_password)
    protected EditText mPassword;
    @ConfirmPassword(messageResId = R.string.password_dont_match)
    @BindView(R.id.sign_up_password_confirm)
    protected EditText mPasswordConfirm;
    @BindView(R.id.photo_profile)
    ImageButton mPhotoProfile;
    @BindView(R.id.signup_with_facebook)
    ImageView mSignupWithFacebook;
    private UserService mUserService;
    private Validator mValidator;
    private CallbackManager mCallbackManager;

    public static Intent createIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCallbackManager = CallbackManager.Factory.create();

        mUserService = OAuth2ServiceFactory.createAnonymousService(UserService.class);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        UniqueEmailRule uniqueEmailRule = new UniqueEmailRule(mName, mUserService);
        mValidator.put(mEmail, uniqueEmailRule);

        registerCallback();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @OnClick(R.id.signup_with_facebook)
    public void onClickSignUpFacebook(View v) {
        LoginManager.getInstance()
                .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    @OnClick(R.id.sign_up_next)
    public void onClickNext(View v) {
        mValidator.validate(true);
    }

    @Override
    public void onValidationSucceeded() {
        signUp();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected boolean isLoginRequired() {
        return false;
    }

    private void registerCallback() {
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserEmail(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void getUserEmail(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    updateUserData(object.getString("email"));
                } catch (JSONException e) {
                    updateUserData("");
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void updateUserData(String email) {
        Profile profile = Profile.getCurrentProfile();
        mName.setText(profile.getName());

        int photoDimention = getResources().getDimensionPixelSize(R.dimen.avatar_dimention);
        Picasso.with(this)
                .load(profile.getProfilePictureUri(photoDimention, photoDimention))
                .transform(PicassoTransformations.getRoundedTransformation())
                .error(R.drawable.ic_signup)
                .placeholder(R.drawable.ic_signup)
                .into(mPhotoProfile);

        if (!TextUtils.isEmpty(email)) {
            mEmail.setText(email);
        }
    }

    private void signUp() {
        UserSignupRequestVO user = new UserSignupRequestVO();
        user.setName(mName.getText().toString());
        user.setEmail(mEmail.getText().toString());
        user.setPassword(mPassword.getText().toString());

        mUserService.signUp(user).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case HttpURLConnection.HTTP_CREATED:
                        Bundle bundle = new Bundle();
                        bundle.putString(AuthenticatorActivity.PARAM_USER_NAME, mEmail.getText().toString());
                        bundle.putString(AuthenticatorActivity.PARAM_USER_PASS, mPassword.getText().toString());
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        Snackbar.make(mName, R.string.status_400, Snackbar.LENGTH_LONG).show();
                    default:
                        Snackbar.make(mName, R.string.status_500, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Snackbar.make(mName, R.string.status_500, Snackbar.LENGTH_LONG).show();
                Log.e("dragonfly", t.getMessage(), t);
            }
        });
    }
}
