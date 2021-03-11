package digital.iam.ma.views.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import digital.iam.ma.R;
import digital.iam.ma.databinding.ActivityDashboardBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.models.logout.LogoutData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.DashboardViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.base.BaseActivity;
import digital.iam.ma.views.dashboard.bundles.BundlesFragment;
import digital.iam.ma.views.dashboard.cart.CartFragment;
import digital.iam.ma.views.dashboard.home.HomeFragment;
import digital.iam.ma.views.dashboard.payment.PaymentFragment;
import digital.iam.ma.views.dashboard.profile.HelpFragment;
import digital.iam.ma.views.dashboard.profile.PersonalInformationFragment;
import digital.iam.ma.views.dashboard.services.ServicesFragment;

public class DashboardActivity extends BaseActivity {

    private ActivityDashboardBinding activityBinding;
    private ArrayList<String> names;
    private ArrayList<Integer> icons;
    private ArrayList<Fragment> fragments;
    private Boolean menuIsVisible = false;
    private PreferenceManager preferenceManager;
    private DashboardViewModel viewModel;

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

    private void init() {
        names = new ArrayList<String>() {{
            add("Mon compte");
            add("Paiements");
            add("Services");
            add("Forfaits");
        }};

        icons = new ArrayList<Integer>() {{
            add(R.drawable.ic_home);
            add(R.drawable.ic_paiments);
            add(R.drawable.ic_services);
            add(R.drawable.ic_forfait);
        }};

        fragments = new ArrayList<Fragment>() {{
            add(new HomeFragment());
            add(new PaymentFragment());
            add(new ServicesFragment());
            add(new BundlesFragment());
        }};

        for (int i = 0; i < names.size(); i++) {
            TabLayout.Tab tab = activityBinding.tabLayout.newTab();
            tab.setText(names.get(i));
            tab.setIcon(icons.get(i));
            activityBinding.tabLayout.addTab(tab);
        }

        activityBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                replaceFragment(fragments.get(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                replaceFragment(fragments.get(tab.getPosition()));
            }
        });

        addFragment(HomeFragment.newInstance(getIntent().getBooleanExtra("is_first_login", false)));

        activityBinding.profileBtn.setOnClickListener(v -> {
            activityBinding.closeMenu.setVisibility(View.VISIBLE);
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
            addFragment(new PersonalInformationFragment());
        });

        activityBinding.helpBtn.setOnClickListener(v -> {
            closeSideMenu();
            addFragment(new HelpFragment());
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

        activityBinding.cartBtn.setOnClickListener(v -> addFragment(new CartFragment()));

        activityBinding.logoutBtn.setOnClickListener(v -> {
            closeSideMenu();
            logout();
        });

    }

    private void closeSideMenu() {
        activityBinding.slideMenu.animate().translationX(activityBinding.slideMenu.getWidth());
        menuIsVisible = false;
        activityBinding.closeMenu.setVisibility(View.GONE);
    }

    private void logout() {
        activityBinding.loader.setVisibility(View.VISIBLE);
        viewModel.logout(preferenceManager.getValue(Constants.TOKEN, ""), "fr");
    }

    private void handleLogoutData(Resource<LogoutData> responseData) {
        activityBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                preferenceManager.clearValue(Constants.IS_LOGGED_IN);
                startActivity(new Intent(DashboardActivity.this, AuthenticationActivity.class));
                finishAffinity();
                break;
            case LOADING:
                break;
            case ERROR:
                Utilities.showErrorPopup(this, responseData.message);
                break;
        }
    }
}