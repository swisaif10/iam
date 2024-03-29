package digital.iam.ma.views.dashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import digital.iam.ma.R;
import digital.iam.ma.databinding.ActivityDashboardBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.listener.OnDialogButtonsClickListener;
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

public class DashboardActivity extends BaseActivity {

    private ActivityDashboardBinding activityBinding;
    private ArrayList<String> names;
    private ArrayList<Integer> icons;
    private ArrayList<Fragment> fragments;
    private Boolean menuIsVisible = false;
    private PreferenceManager preferenceManager;
    private DashboardViewModel viewModel;

    public static int getScreenWidth(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getDisplay().getRealMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        preferenceManager = new PreferenceManager.Builder(this, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        viewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        viewModel.getLogoutLiveData().observe(this, this::handleLogoutData);

        init();
    }

    @Override
    public void onBackPressed() {
        if (menuIsVisible)
            closeSideMenu();
        else if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            getSupportFragmentManager().popBackStack();
        else
            finish();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        if (preferenceManager.getValue(Constants.LANGUAGE, "fr").equalsIgnoreCase("ar")) {
            activityBinding.slideMenu.animate().translationX(-getScreenWidth(this));
            activityBinding.arabicBtn.setTextColor(ContextCompat.getColor(DashboardActivity.this, R.color.orange));
            activityBinding.arabicBtn.setPaintFlags(0);
            activityBinding.frenchBtn.setTextColor(ContextCompat.getColor(DashboardActivity.this, R.color.lightGrey));
            activityBinding.frenchBtn.setPaintFlags(activityBinding.arabicBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            activityBinding.arabicBtn.setPaintFlags(activityBinding.arabicBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

        names = new ArrayList<String>() {{
            add(getString(R.string.bundle_menu_item_label));
            add(getString(R.string.payments_menu_item_label));
            add(getString(R.string.services_menu_item_label));
            add(getString(R.string.update_bundle_menu_item_label));
        }};

        icons = new ArrayList<Integer>() {{
            add(R.drawable.ic_home);
            add(R.drawable.ic_paiments);
            add(R.drawable.ic_services);
            add(R.drawable.ic_forfait);
        }};

        fragments = new ArrayList<Fragment>() {{
            add(HomeFragment.newInstance(getIntent().getBooleanExtra("is_first_login", false)));
            add(new PaymentFragment());
            add(new ServicesFragment());
            add(new BundlesFragment());
        }};

        for (int i = 0; i < names.size(); i++) {
            TabLayout.Tab tab = activityBinding.tabLayout.newTab();
            View view = tab.getCustomView() == null ? LayoutInflater.from(activityBinding.tabLayout.getContext()).inflate(R.layout.tab_item_layout, null) : tab.getCustomView();
            if (tab.getCustomView() == null) {
                tab.setCustomView(view);
            }
            TextView tabTextView = view.findViewById(R.id.title);
            tabTextView.setText(names.get(i));
            ImageView tabImageView = view.findViewById(R.id.icon);
            tabImageView.setImageResource(icons.get(i));
            if (preferenceManager.getValue(Constants.IS_LINE_ACTIVATED, "").equalsIgnoreCase("pending") && (i == 2 || i == 3)) {
                tabImageView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#E1E1E1")));
                tabTextView.setTextColor(Color.parseColor("#E1E1E1"));
            }
            activityBinding.tabLayout.addTab(tab);
        }

        activityBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                replaceFragment(fragments.get(position));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                replaceFragment(fragments.get(position));
            }
        });

        if (preferenceManager.getValue(Constants.IS_LINE_ACTIVATED, "").equalsIgnoreCase("pending")) {
            LinearLayout tabStrip = ((LinearLayout) activityBinding.tabLayout.getChildAt(0));
            tabStrip.getChildAt(2).setOnTouchListener((v, event) -> true);
            tabStrip.getChildAt(3).setOnTouchListener((v, event) -> true);
        }

        activityBinding.tabLayout.getTabAt(0).select();

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

        activityBinding.contractsBtn.setOnClickListener(v -> {
            closeSideMenu();
            startActivity(new Intent(this, ContractActivity.class));
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
            if (preferenceManager.getValue(Constants.LANGUAGE, "fr").equalsIgnoreCase("ar"))
                setNewLocale(this, LocaleManager.FRENCH);
            closeSideMenu();
        });
        activityBinding.arabicBtn.setOnClickListener(v -> {
            if (preferenceManager.getValue(Constants.LANGUAGE, "fr").equalsIgnoreCase("fr"))
                setNewLocale(this, LocaleManager.ARABIC);
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
        activityBinding.tabLayout.getTabAt(1).select();
    }

    public void showHideTabLayout(int visibility) {
        activityBinding.tabLayout.setVisibility(visibility);
    }

}