package digital.iam.ma.views.base;

import static android.content.pm.PackageManager.GET_META_DATA;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import digital.iam.ma.R;
import digital.iam.ma.datamanager.retrofit.RetrofitClient;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.listener.OnBaseUrlChanged;
import digital.iam.ma.listener.ShakeListener;
import digital.iam.ma.models.login.Line;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.LocaleManager;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.views.dashboard.DashboardActivity;

public class BaseActivity extends AppCompatActivity  {

    private static final String TAG = "base url";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeListener mShakeDetector;
    private PreferenceManager preferenceManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetTitles();
        preferenceManager = new PreferenceManager.Builder(this, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
        // ShakeListener initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeListener();
        mShakeDetector.setOnShakeListener(new ShakeListener.OnShakeListener() {

            @Override
            public void onShake(int count) {
                handleShakeEvent(count);
            }
        });
    }

    public void handleShakeEvent(int count) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    protected void resetTitles() {
        try {
            ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
            if (info.labelRes != 0) {
                setTitle(info.labelRes);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(getApplicationContext(), language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void addFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void replaceFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    public void removeFromBackStack(String tag) {
        Fragment myFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (myFragment != null && myFragment.isVisible()) {
            FragmentManager manager = this.getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove(myFragment);
            trans.commit();
            manager.popBackStack();
        }
    }

    public List<Line> getList() {
        List<Line> arrayItems ;
        String serializedObject = preferenceManager.getValue(Constants.LINE_DETAILS, "");
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Line>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
            Collections.sort(arrayItems, new Comparator<Line>() {
                @Override
                public int compare(Line o1, Line o2) {
                    return o1.getMsisdn().compareTo(o2.getMsisdn());
                }
            });
            return arrayItems;
        }
        return null;
    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(key, json);
    }

    public void set(String key, String value) {
        preferenceManager.putValue(key, value);
    }

}