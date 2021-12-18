package digital.iam.ma.views.dashboard.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import digital.iam.ma.databinding.FragmentActivateSimBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.models.commons.ResponseData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.NumericKeyBoardTransformationMethod;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.HomeViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.dashboard.DashboardActivity;

public class ActivateSimFragment extends Fragment {

    private FragmentActivateSimBinding fragmentBinding;
    private HomeViewModel viewModel;
    private PreferenceManager preferenceManager;
    final BarcodeScanner scanner = BarcodeScanning.getClient();
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    public ActivateSimFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        viewModel.getActivateSIMLiveData().observe(this, this::handleSIMActivationData);

        preferenceManager = new PreferenceManager.Builder(requireContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentActivateSimBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        fragmentBinding.backBtn.setOnClickListener(v -> requireActivity().onBackPressed());
        fragmentBinding.container.setOnClickListener(v -> Utilities.hideSoftKeyboard(requireContext(), requireView()));
        fragmentBinding.validateBtn.setOnClickListener(v -> activateSim());

        fragmentBinding.code.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        fragmentBinding.code.setTransformationMethod(new NumericKeyBoardTransformationMethod());

        // BARCODE READER
        fragmentBinding.scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 100);
                } else {
                    extractQRCode();
                }

            }
        });
    }

    private void extractQRCode() {
        fragmentBinding.cameraContainer.setVisibility(View.VISIBLE);
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(
                () -> {
                    try {
                        bindImageAnalysis(cameraProviderFuture.get());
                    } catch (InterruptedException ie) {
                        //logger.error("InterruptedException: ", ie);
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException ee) {
                        //logger.error("ExecutionException: ",ee);
                    }
                },
                ContextCompat.getMainExecutor(requireContext())
        );
    }

    private void bindImageAnalysis(ProcessCameraProvider cameraProvider) {
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(requireContext()),
                this::scanBarcodes);
        cameraProvider.unbindAll();
        OrientationEventListener orientationEventListener = new OrientationEventListener(requireContext()) {
            @Override
            public void onOrientationChanged(int orientation) {
//                binding.orientation.setText(String.valueOf(orientation));
            }
        };

        orientationEventListener.enable();

        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(fragmentBinding.previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle(
                requireActivity(),
                cameraSelector,
                imageAnalysis,
                preview
        );
    }

    private void scanBarcodes(ImageProxy imageProxy) {

        @SuppressLint({"UnsafeExperimentalUsageError", "UnsafeOptInUsageError"}) final InputImage image = InputImage.fromMediaImage(Objects.requireNonNull(imageProxy.getImage()), imageProxy.getImageInfo().getRotationDegrees());

        scanner
                .process(image)
                .addOnSuccessListener(
                        barcodes -> {
                            for (Barcode barcode : barcodes) {
                                //if (barcode.getValueType() == Barcode.TYPE_TEXT) {
                                    String value = barcode.getDisplayValue();
                                    if (value != null && !value.equals("")) {
                                        try {
                                            TimeUnit.MILLISECONDS.sleep(500);
                                        } catch (InterruptedException exception) {
                                            exception.printStackTrace();
                                        }
                                        fragmentBinding.cameraContainer.setVisibility(View.GONE);
                                        fragmentBinding.code.setText(value);
                                        return;
                                    }
                                //}
                            }
                            imageProxy.close();
                        })
                .addOnFailureListener(
                        e -> imageProxy.close()
                );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0) {
            extractQRCode();
        }
    }

    private void activateSim() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        viewModel.activateSIM(preferenceManager.getValue(Constants.TOKEN, ""), preferenceManager.getValue(Constants.MSISDN, ""), fragmentBinding.code.getText().toString(), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleSIMActivationData(Resource<ResponseData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                ((DashboardActivity) requireActivity()).getSupportFragmentManager().setFragmentResult("100", new Bundle());
                requireActivity().getSupportFragmentManager().popBackStack();
                break;
            case INVALID_TOKEN:
                Utilities.showErrorPopupWithClick(requireContext(), responseData.data.getHeader().getMessage(), v -> {
                    startActivity(new Intent(requireActivity(), AuthenticationActivity.class));
                    requireActivity().finishAffinity();
                });
                break;
            case ERROR:
                Utilities.showErrorPopup(requireContext(), responseData.message);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            fragmentBinding.code.setText(getArguments().getString(Constants.QR_CODE));
            fragmentBinding.scanQR.setEnabled(false);
        }
    }
}