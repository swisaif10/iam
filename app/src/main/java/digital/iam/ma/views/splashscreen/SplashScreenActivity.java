package digital.iam.ma.views.splashscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import digital.iam.ma.R;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.base.BaseActivity;
import digital.iam.ma.views.dashboard.DashboardActivity;

public class SplashScreenActivity extends BaseActivity {

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        preferenceManager = new PreferenceManager.Builder(this, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        new Handler(Looper.getMainLooper()).postDelayed(this::movToNextActivity, 2000);

    }

    private void movToNextActivity() {
        if (getIntent().getData() != null)
            handleIntent();
        else {
            Intent intent;
            if (preferenceManager.getValue(Constants.IS_LOGGED_IN, false))
                intent = new Intent(SplashScreenActivity.this, DashboardActivity.class);
            else
                intent = new Intent(SplashScreenActivity.this, AuthenticationActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void handleIntent() {
        String data = getIntent().getData().toString();
        String token = data.substring(data.indexOf("=") + 1);
        Intent intent = new Intent(SplashScreenActivity.this, AuthenticationActivity.class);
        intent.putExtra("resetPasswordAction", true);
        intent.putExtra("resetToken", token);
        startActivity(intent);
        finish();
    }
}