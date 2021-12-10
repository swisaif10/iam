package digital.iam.ma.views.dashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Insets;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import digital.iam.ma.R;
import digital.iam.ma.adapter.LineAdapter;
import digital.iam.ma.databinding.ActivityDashboardBinding;
import digital.iam.ma.datamanager.retrofit.RetrofitClient;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.listener.OnDialogButtonsClickListener;
import digital.iam.ma.listener.OnRadioChecked;
import digital.iam.ma.models.login.Line;
import digital.iam.ma.models.logout.LogoutData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.LocaleManager;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.DashboardViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.base.BaseActivity;
import digital.iam.ma.views.dashboard.bundles.BundlesFragment;
import digital.iam.ma.views.dashboard.contract.ContractActivity;
import digital.iam.ma.views.dashboard.help.HelpActivity;
import digital.iam.ma.views.dashboard.home.HomeFragment;
import digital.iam.ma.views.dashboard.payment.PaymentFragment;
import digital.iam.ma.views.dashboard.personalinfo.PersonalInformationActivity;
import digital.iam.ma.views.dashboard.services.ServicesFragment;

public class DashboardActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, OnRadioChecked {

    private ActivityDashboardBinding activityBinding;
    private Boolean menuIsVisible = false;
    private PreferenceManager preferenceManager;
    private DashboardViewModel viewModel;
    private boolean lineClick = false;
    private int position;
    private int selectedFragment;
    List<String> list = new ArrayList<>();

    public static int getScreenWidth(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            return displayMetrics.widthPixels;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = 0;
        selectedFragment = 0;
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        preferenceManager = new PreferenceManager.Builder(this, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        viewModel.getLogoutLiveData().observe(this, this::handleLogoutData);
        initLineDropDown();
        init();

        activityBinding.lineDropDown.setOnClickListener(v -> {
            lineClick = !lineClick;
            activityBinding.lineRecyclerView.setVisibility(lineClick ? View.VISIBLE : View.GONE);
            activityBinding.separator1.setVisibility(lineClick ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onBackPressed() {
        if (menuIsVisible) {
            closeSideMenu();
        } else if (selectedFragment == 0) {
            finish();
        } else {
            selectedFragment = 0;
            HomeFragment homeFragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("POSITION", position);
            homeFragment.setArguments(bundle);
            replaceFragment(homeFragment, Constants.HOME);
            //replaceFragment(new HomeFragment(position), Constants.HOME);
            activityBinding.tabLayout.getMenu().getItem(0).setChecked(true);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {
            case R.id.action_compte:
                selectedFragment = 0;
                HomeFragment homeFragment = new HomeFragment();
                bundle.putInt("POSITION", position);
                homeFragment.setArguments(bundle);
                replaceFragment(homeFragment, Constants.HOME);
                return true;
            case R.id.action_paiement:
                selectedFragment = 1;
                PaymentFragment paymentFragment = new PaymentFragment();
                bundle.putInt(Constants.POSITION, position);
                paymentFragment.setArguments(bundle);
                replaceFragment(paymentFragment, Constants.PAYMENT);
                return true;

            case R.id.action_service:
                selectedFragment = 2;
                ServicesFragment servicesFragment = new ServicesFragment();
                bundle.putInt(Constants.POSITION, position);
                servicesFragment.setArguments(bundle);
                replaceFragment(servicesFragment, Constants.SERVICE);
                return true;

            case R.id.action_forfaits:
                selectedFragment = 3;
                BundlesFragment bundlesFragment = new BundlesFragment();
                bundle.putInt(Constants.POSITION, position);
                bundlesFragment.setArguments(bundle);
                replaceFragment(bundlesFragment, Constants.BUNDLE);
                return true;
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        if (preferenceManager.getValue(Constants.LANGUAGE, "fr").equalsIgnoreCase("ar")) {
            activityBinding.slideMenu.animate().translationX(-getScreenWidth(this));
            activityBinding.arabicBtn.setTextColor(ContextCompat.getColor(DashboardActivity.this, R.color.blue));
            activityBinding.arabicBtn.setPaintFlags(0);
            activityBinding.frenchBtn.setTextColor(ContextCompat.getColor(DashboardActivity.this, R.color.lightGrey));
            activityBinding.frenchBtn.setPaintFlags(activityBinding.arabicBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            activityBinding.arabicBtn.setPaintFlags(activityBinding.arabicBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }


        activityBinding.tabLayout.setOnNavigationItemSelectedListener(this);
        activityBinding.tabLayout.setSelectedItemId(R.id.action_compte);


        activityBinding.profileBtn.setOnClickListener(v -> {
            activityBinding.closeMenu.setVisibility(View.VISIBLE);
            activityBinding.slideMenu.setVisibility(View.VISIBLE);
            activityBinding.slideMenu.animate().translationX(0);
            menuIsVisible = true;
        });
        activityBinding.closeBtn.setOnClickListener(v -> closeSideMenu());

        activityBinding.closeMenu.setOnClickListener(v -> {
            if (menuIsVisible)
                closeSideMenu();
            else {
                activityBinding.notificationLayout.setVisibility(View.GONE);
                activityBinding.closeMenu.setVisibility(View.GONE);
            }
        });

        activityBinding.personalInfoBtn.setOnClickListener(v -> {
            closeSideMenu();
            startActivity(new Intent(this, PersonalInformationActivity.class));
        });

        activityBinding.helpBtn.setOnClickListener(v -> {
            closeSideMenu();
            startActivity(new Intent(this, HelpActivity.class));
        });

        activityBinding.bankCardsBtn.setOnClickListener(v -> {
            selectedFragment = 1;
            refresh();
            activityBinding.tabLayout.setSelectedItemId(R.id.action_paiement);
            closeSideMenu();
        });

        activityBinding.contractsBtn.setOnClickListener(v -> {
            closeSideMenu();
            Intent intent = new Intent(this, ContractActivity.class);
            intent.putExtra(Constants.LINE, position);
            startActivity(intent);
            //startActivity(new Intent(this, ContractActivity.class));
        });

        activityBinding.notificationBtn.setOnClickListener(v -> {
            activityBinding.closeMenu.setVisibility(View.VISIBLE);
            activityBinding.notificationLayout.setVisibility(View.VISIBLE);
            activityBinding.notificationLayout.setAlpha(0.0f);
            activityBinding.notificationLayout.animate()
                    .translationX(0)
                    .alpha(1.0f)
                    .setListener(null);
        });

        activityBinding.logoutBtn.setOnClickListener(v -> {
            closeSideMenu();
            Utilities.showLogoutDialog(this, getString(R.string.logout_discreption), new OnDialogButtonsClickListener() {
                @Override
                public void firstChoice() {
                    logout();
                }

                @Override
                public void secondChoice() {

                }
            });
        });

        activityBinding.frenchBtn.setOnClickListener(v -> {
            if (!preferenceManager.getValue(Constants.LANGUAGE, "fr").equalsIgnoreCase("fr"))
                setNewLocale(this, LocaleManager.FRENCH);
            closeSideMenu();
        });
        activityBinding.arabicBtn.setOnClickListener(v -> {
            if (!preferenceManager.getValue(Constants.LANGUAGE, "fr").equalsIgnoreCase("ar"))
                setNewLocale(this, LocaleManager.ARABIC);
            closeSideMenu();
        });
        activityBinding.englishBtn.setOnClickListener(v -> {
            if (!preferenceManager.getValue(Constants.LANGUAGE, "fr").equalsIgnoreCase("en")) {
                setNewLocale(this, LocaleManager.FRENCH);
            }
            closeSideMenu();
        });
    }

    private void closeSideMenu() {
        if (preferenceManager.getValue(Constants.LANGUAGE, "fr").equalsIgnoreCase("ar"))
            activityBinding.slideMenu.animate().translationX(-activityBinding.slideMenu.getWidth());
        else
            activityBinding.slideMenu.animate().translationX(activityBinding.slideMenu.getWidth());
        menuIsVisible = false;
        activityBinding.closeMenu.setVisibility(View.GONE);
    }

    private void logout() {
        activityBinding.loader.setVisibility(View.VISIBLE);
        viewModel.logout(preferenceManager.getValue(Constants.TOKEN, ""), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleLogoutData(Resource<LogoutData> responseData) {
        activityBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                preferenceManager.clearValue(Constants.IS_LOGGED_IN);
                startActivity(new Intent(DashboardActivity.this, AuthenticationActivity.class));
                finishAffinity();
                break;
            case INVALID_TOKEN:
                assert responseData.data != null;
                Utilities.showErrorPopupWithClick(this, responseData.data.getHeader().getMessage(), v -> {
                    startActivity(new Intent(this, AuthenticationActivity.class));
                    finishAffinity();
                });
                break;
            case ERROR:
                Utilities.showErrorPopup(this, responseData.message);
                break;
        }
    }

    public void selectPaymentsTab() {
        activityBinding.tabLayout.setSelectedItemId(R.id.action_paiement);
    }

    public void showHideTabLayout(int visibility) {
        activityBinding.tabLayout.setVisibility(visibility);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initLineDropDown() {
        List<Line> mList = getList();
        for (Line line : mList) {
            list.add(line.getMsisdn());
        }
        activityBinding.lineRecyclerView.setAdapter(new LineAdapter(this, this.list, this));
        activityBinding.lineRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        activityBinding.lineRecyclerView.setHasFixedSize(true);
        activityBinding.lineDropDown.setText(list.get(0));
    }

    @Override
    public void onRadioChecked(int position) {
        lineClick = !lineClick;
        activityBinding.lineRecyclerView.setVisibility(View.GONE);
        activityBinding.separator1.setVisibility(View.GONE);
        if (position != this.position) {
            this.position = position;
            activityBinding.lineDropDown.setText(list.get(position));
            refresh();
        }
    }

    public List<Line> getList() {
        List<Line> arrayItems;
        String serializedObject = preferenceManager.getValue(Constants.LINE_DETAILS, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Line>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
            return arrayItems;
        }
        return null;
    }

    public void deactivateUserInteraction() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void activateUserInteraction() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
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
                refresh();
            }
        });
    }

    public void refresh() {
        Bundle bundle = new Bundle();
        switch (selectedFragment) {
            case 0:
                removeFromBackStack(Constants.HOME);
                HomeFragment homeFragment = new HomeFragment();
                bundle.putInt(Constants.POSITION, position);
                homeFragment.setArguments(bundle);
                replaceFragment(homeFragment, Constants.HOME);
                break;
            case 1:
                removeFromBackStack(Constants.PAYMENT);
                PaymentFragment paymentFragment = new PaymentFragment();
                bundle.putInt(Constants.POSITION, position);
                paymentFragment.setArguments(bundle);
                replaceFragment(paymentFragment, Constants.PAYMENT);
                break;
            case 2:
                removeFromBackStack(Constants.SERVICE);
                ServicesFragment servicesFragment = new ServicesFragment();
                bundle.putInt(Constants.POSITION, position);
                servicesFragment.setArguments(bundle);
                replaceFragment(servicesFragment, Constants.SERVICE);
                break;
            case 3:
                removeFromBackStack(Constants.BUNDLE);
                BundlesFragment bundlesFragment = new BundlesFragment();
                bundle.putInt(Constants.POSITION, position);
                bundlesFragment.setArguments(bundle);
                replaceFragment(bundlesFragment, Constants.BUNDLE);
                break;
        }
    }

}