package com.noobfitness.noobfitness.legacy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.noobfitness.noobfitness.R;
import com.noobfitness.noobfitness.auth.LoginController;
import com.noobfitness.noobfitness.dagger.DaggerAppComponent;
import com.noobfitness.noobfitness.dagger.InjectedApplication;
import com.squareup.picasso.Picasso;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.TokenResponse;

import org.json.JSONObject;

import javax.inject.Inject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends Activity {

    private static final String LOG_TAG = "MainActivity";

    @Inject
    LoginController loginController;

    Button mMakeApiCall;
    Button mSignOut;
    TextView mGivenName;
    TextView mFamilyName;
    TextView mFullName;
    ImageView mProfileView;

    Button fourDayDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((InjectedApplication) getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.legacy_activity_main);

        mMakeApiCall = findViewById(R.id.makeApiCall);
        mSignOut = findViewById(R.id.signOut);
        mGivenName = findViewById(R.id.givenName);
        mFamilyName = findViewById(R.id.familyName);
        mFullName = findViewById(R.id.fullName);
        mProfileView = findViewById(R.id.profileImage);

        enablePostAuthorizationFlows();

        LinearLayout mainLinearLayout = findViewById(R.id.activity_main);

        fourDayDefault = new Button(this);
        fourDayDefault.setText("hello world");
        fourDayDefault.setGravity(Gravity.LEFT);
        fourDayDefault.setPadding(96, 96, 96, 96);
        fourDayDefault.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        fourDayDefault.setTextSize(24);
        fourDayDefault.setAllCaps(false);
        fourDayDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RoutinesActivity.class);
                startActivity(i);
            }
        });
        mainLinearLayout.addView(fourDayDefault);
    }

    private void enablePostAuthorizationFlows() {
        AuthState mAuthState = loginController.getAuthState();
        if (mAuthState != null && mAuthState.isAuthorized()) {
            if (mMakeApiCall.getVisibility() == View.GONE) {
                mMakeApiCall.setVisibility(View.VISIBLE);
                mMakeApiCall.setOnClickListener(new MainActivity.MakeApiCallListener(this, loginController.getAuthState(), new AuthorizationService(this)));
            }
            if (mSignOut.getVisibility() == View.GONE) {
                mSignOut.setVisibility(View.VISIBLE);
                mSignOut.setOnClickListener(new MainActivity.SignOutListener(this));
            }
        } else {
            mMakeApiCall.setVisibility(View.GONE);
            mSignOut.setVisibility(View.GONE);
        }
    }

    public static class SignOutListener implements Button.OnClickListener {

        private final MainActivity mMainActivity;

        public SignOutListener(@NonNull MainActivity mainActivity) {
            mMainActivity = mainActivity;
        }

        @Override
        public void onClick(View view) {
            mMainActivity.loginController.logout();
        }
    }

    public static class MakeApiCallListener implements Button.OnClickListener {

        private final MainActivity mMainActivity;
        private AuthState mAuthState;
        private AuthorizationService mAuthorizationService;

        public MakeApiCallListener(@NonNull MainActivity mainActivity, @NonNull AuthState authState, @NonNull AuthorizationService authorizationService) {
            mMainActivity = mainActivity;
            mAuthState = authState;
            mAuthorizationService = authorizationService;
        }

        @Override
        public void onClick(View view) {

            // code from the section 'Making API Calls' goes here
            mAuthState.performActionWithFreshTokens(mAuthorizationService, new AuthState.AuthStateAction() {
                @Override
                public void execute(@Nullable String accessToken, @Nullable String idToken, @Nullable AuthorizationException exception) {
                    new AsyncTask<String, Void, JSONObject>() {
                        @Override
                        protected JSONObject doInBackground(String... tokens) {
                            OkHttpClient client = new OkHttpClient();
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("access_token", tokens[0])
                                    .build();
                            Request request = new Request.Builder()
                                    .post(requestBody)
                                    .url("http://10.0.2.2:5000/api/auth/google")
                                    .build();
                            try {
                                Response response = client.newCall(request).execute();
                                String header = response.header("x-auth-token");
                                String jsonBody = response.body().string();
                                Log.i(LOG_TAG, String.format("User Info Response %s", jsonBody));
                                return new JSONObject(jsonBody);
                            } catch (Exception exception) {
                                Log.w(LOG_TAG, exception);
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(JSONObject userInfo) {
                            if (userInfo != null) {
                                String fullName = userInfo.optString("username", null);
                                String imageUrl = userInfo.optString("avatar", null);
                                String routines = userInfo.optString("routines", null);
                                if (!TextUtils.isEmpty(imageUrl)) {
                                    Picasso.with(mMainActivity)
                                            .load(imageUrl)
                                            .placeholder(R.drawable.ring_accent)
                                            .into(mMainActivity.mProfileView);
                                }
                                if (!TextUtils.isEmpty(fullName)) {
                                    mMainActivity.mFullName.setText(fullName);
                                }

                                mMainActivity.fourDayDefault.setText(routines);
                            }
                        }
                    }.execute(accessToken);
                }
            });
        }
    }
}
