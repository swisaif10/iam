package digital.iam.ma;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.flurry.android.FlurryAgent;

public class Gray extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, getResources().getString(R.string.flurry_key));
    }
}
