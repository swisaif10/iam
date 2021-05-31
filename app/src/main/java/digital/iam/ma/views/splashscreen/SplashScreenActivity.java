package digital.iam.ma.views.splashscreen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.ViewModelProviders;

import digital.iam.ma.R;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.listener.OnDialogButtonsClickListener;
import digital.iam.ma.models.controlversion.ControlVersionData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.SplashScreenViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.base.BaseActivity;
import digital.iam.ma.views.dashboard.DashboardActivity;

public class SplashScreenActivity extends BaseActivity {

    private PreferenceManager preferenceManager;
    private SplashScreenViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        preferenceManager = new PreferenceManager.Builder(this, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        viewModel = ViewModelProviders.of(this).get(SplashScreenViewModel.class);
        viewModel.getControlVersionLiveData().observe(this, this::handleControlVersionData);


        new Handler(Looper.getMainLooper()).postDelayed(this::controlVersion, 2000);

    }

    private void controlVersion() {
        viewModel.controlVersion("fr");
    }

    private void handleControlVersionData(Resource<ControlVersionData> responseData) {
        switch (responseData.status) {
            case SUCCESS:
                assert responseData.data != null;
                String appStatus = responseData.data.getResponse().getStatus();
                switch (appStatus) {
                    case "200":
                        movToNextActivity();
                        break;
                    case "401":
                    case "402":
                        Utilities.showUpdateDialog(this, responseData.data.getHeader().getMessage(), appStatus, new OnDialogButtonsClickListener() {
                            @Override
                            public void firstChoice() {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(responseData.data.getResponse().getLink())));
                            }

                            @Override
                            public void secondChoice() {
                                movToNextActivity();
                            }
                        });
                        break;
                }
                break;
            case INVALID_TOKEN:
                break;
            case ERROR:
                Utilities.showErrorPopup(this, responseData.message);
                break;
        }
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