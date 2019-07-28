package com.noobfitness.noobfitness.auth;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;
import com.noobfitness.noobfitness.dagger.InjectedApplication;
import com.noobfitness.noobfitness.R;
import com.noobfitness.noobfitness.legacy.MainActivity;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;

public class LoginActivity extends Activity {

    private static final String LOG_TAG = "LoginActivity";

    SignInButton mAuthorize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((InjectedApplication) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_login);

        mAuthorize = findViewById(R.id.authorize);

        mAuthorize.setOnClickListener(new AuthorizeListener());
    }

    /**
     * Kicks off the authorization flow.
     */
    public static class AuthorizeListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            Context context = view.getContext();

            // code from the step 'Create the Authorization Request',
            // and the step 'Perform the Authorization Request' goes here.
            AuthorizationServiceConfiguration serviceConfiguration = new AuthorizationServiceConfiguration(
                    Uri.parse("https://accounts.google.com/o/oauth2/v2/auth") /* auth endpoint */,
                    Uri.parse("https://www.googleapis.com/oauth2/v4/token") /* token endpoint */
            );

            String clientId = context.getString(R.string.google_client_id);
            Uri redirectUri = Uri.parse("com.noobfitness.noobfitness:/oauth2callback");
            AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                    serviceConfiguration,
                    clientId,
                    "code",
                    redirectUri
            );
            builder.setScopes("openid profile email");
            AuthorizationRequest request = builder.build();

            AuthorizationService authorizationService = new AuthorizationService(context);

            Intent postAuthorizationIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, request.hashCode(), postAuthorizationIntent, 0);
            authorizationService.performAuthorizationRequest(request, pendingIntent);
        }
    }
}
