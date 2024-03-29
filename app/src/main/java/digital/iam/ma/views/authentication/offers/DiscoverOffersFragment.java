package digital.iam.ma.views.authentication.offers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import digital.iam.ma.databinding.FragmentDiscoverOffersBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.utilities.Constants;

public class DiscoverOffersFragment extends Fragment {

    private FragmentDiscoverOffersBinding fragmentBinding;
    private PreferenceManager preferenceManager;
    private Boolean fromDashboard = false;


    public DiscoverOffersFragment() {
        // Required empty public constructor
    }

    public static DiscoverOffersFragment newInstance(Boolean fromDashboard) {
        DiscoverOffersFragment fragment = new DiscoverOffersFragment();
        Bundle args = new Bundle();
        args.putBoolean("fromDashboard", fromDashboard);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager.Builder(requireContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        if (getArguments() != null)
            fromDashboard = getArguments().getBoolean("fromDashboard", false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentDiscoverOffersBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        if (preferenceManager.getValue(Constants.LANGUAGE, "fr").equalsIgnoreCase("ar"))
            fragmentBinding.backImage.setRotation(180f);
        if (fromDashboard) {
            fragmentBinding.title.setVisibility(View.VISIBLE);
            fragmentBinding.backBtn.setVisibility(View.GONE);
        }

        fragmentBinding.loader.setVisibility(View.VISIBLE);
        fragmentBinding.backBtn.setOnClickListener(v -> requireActivity().onBackPressed());
        fragmentBinding.webView.getSettings().setJavaScriptEnabled(true);
        fragmentBinding.webView.getSettings().setAllowContentAccess(true);
        fragmentBinding.webView.getSettings().setAllowFileAccess(true);
        fragmentBinding.webView.getSettings().setDatabaseEnabled(false);
        fragmentBinding.webView.getSettings().setDomStorageEnabled(true);
        fragmentBinding.webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                handler.proceed("iam", "gray");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                fragmentBinding.loader.setVisibility(View.GONE);
            }
        });
        fragmentBinding.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        fragmentBinding.webView.loadUrl("https://webgray.mobiblanc.com/buy-package/choose");

    }

}