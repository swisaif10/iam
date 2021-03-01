package digital.iam.ma.views.authentication.discover;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import digital.iam.ma.databinding.FragmentDiscoverOffersBinding;
import okhttp3.Credentials;

public class DiscoverOffersFragment extends Fragment {

    private FragmentDiscoverOffersBinding fragmentBinding;

    public DiscoverOffersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        fragmentBinding.backBtn.setOnClickListener(v -> requireActivity().onBackPressed());
        //fragmentBinding.webView.getSettings().setJavaScriptEnabled(true);
        String AUTHORIZATION = Credentials.basic("iam", "gray");
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", AUTHORIZATION);
        fragmentBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        fragmentBinding.webView.loadUrl("http://web.gray.mobiblanc.com/", headers);

    }

}