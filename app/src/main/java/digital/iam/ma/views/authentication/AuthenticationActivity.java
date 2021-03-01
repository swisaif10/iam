package digital.iam.ma.views.authentication;

import android.os.Bundle;

import digital.iam.ma.R;
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
}