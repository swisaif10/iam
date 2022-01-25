package digital.iam.ma.views.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import digital.iam.ma.R;
import digital.iam.ma.datamanager.retrofit.RetrofitClient;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.RootUtils;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.views.authentication.login.LoginFragment;
import digital.iam.ma.views.authentication.resetpassword.ResetPasswordFragment;
import digital.iam.ma.views.base.BaseActivity;

public class AuthenticationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        if (getIntent().getExtras() != null && getIntent().getBooleanExtra("resetPasswordAction", false)) {
            addFragment(ResetPasswordFragment.newInstance(getIntent().getStringExtra("resetToken")));
        } else
            addFragment(new LoginFragment());
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            getSupportFragmentManager().popBackStack();
        else
            finish();
    }

    /*@Override
    public void handleShakeEvent(int count) {
        super.handleShakeEvent(count);
        Utilities.showConfirmDialog(this, "Êtes vous sûr de vouloir changer la base url ?", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitClient retrofitClient = RetrofitClient.getInstance();
                if (retrofitClient.getUrl().equals(Constants.DEV_URL))
                    retrofitClient.changeBaseUrl(Constants.PREPROD_URL);
                else
                    retrofitClient.changeBaseUrl(Constants.DEV_URL);
                //refresh();
            }
        });
    }*/


    @Override
    protected void onResume() {
        super.onResume();
        if (RootUtils.isDeviceRooted()) {
            Utilities.showRootDialog(this, getString(R.string.rooted_message_dialog), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}