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

import androidx.annotation.Nullable;

import com.noobfitness.noobfitness.R;
import com.noobfitness.noobfitness.auth.AuthService;
import com.noobfitness.noobfitness.auth.AuthStateManager;
import com.noobfitness.noobfitness.dagger.InjectedApplication;
import com.squareup.picasso.Picasso;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;

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
    AuthService authService;
    @Inject
    AuthStateManager authStateManager;

    Button makeApiCallButton;
    Button signOutButton;
    TextView mFullName;
    ImageView mProfileView;

    Button fourDayDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((InjectedApplication) getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.legacy_activity_main);

        makeApiCallButton = findViewById(R.id.makeApiCall);
        signOutButton = findViewById(R.id.signOut);
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
        AuthState mAuthState = authStateManager.get();
        if (mAuthState != null && mAuthState.isAuthorized()) {
            if (makeApiCallButton.getVisibility() == View.GONE) {
                makeApiCallButton.setVisibility(View.VISIBLE);
                makeApiCallButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onMakeApiCallClicked();
                    }
                });
            }
            if (signOutButton.getVisibility() == View.GONE) {
                signOutButton.setVisibility(View.VISIBLE);
                signOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onSignOutClicked();
                    }
                });
            }
        } else {
            makeApiCallButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.GONE);
        }
    }

    private void onSignOutClicked() {
        authStateManager.set(null);
        authService.logout();
    }

    private void onMakeApiCallClicked() {
        authStateManager.get().performActionWithFreshTokens(authService.getAuthorizationService(), new AuthState.AuthStateAction() {
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
                                Picasso.with(MainActivity.this)
                                        .load(imageUrl)
                                        .placeholder(R.drawable.ring_accent)
                                        .into(mProfileView);
                            }
                            if (!TextUtils.isEmpty(fullName)) {
                                mFullName.setText(fullName);
                            }

                            fourDayDefault.setText(routines);
                        }
                    }
                }.execute(accessToken);
            }
        });
    }
}
