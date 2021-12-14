package digital.iam.ma;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;

import digital.iam.ma.firebase.FirebaseService;
import digital.iam.ma.utilities.LocaleManager;

public class Gray extends Application {

    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        context = this;

        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, BuildConfig.FLURRY_KEY);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
    }
}
