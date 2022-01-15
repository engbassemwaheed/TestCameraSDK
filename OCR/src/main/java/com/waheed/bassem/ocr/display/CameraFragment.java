package com.waheed.bassem.ocr.display;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.waheed.bassem.ocr.OcrInterface;
import com.waheed.bassem.ocr.R;
import com.waheed.bassem.ocr.StatusInterface;
import com.waheed.bassem.ocr.view_model.CameraFragmentViewModel;

import java.util.ArrayList;

public class CameraFragment extends Fragment implements CameraFragmentInterface {

    private static final String TAG = "CameraFragment";

    private OcrInterface ocrInterface;
    private String apiKey;
    private StatusInterface statusInterface;

    private FrameLayout loadingFrameLayout;
    private FrameLayout previewFrameLayout;
    private ImageButton captureImageButton;
    private ImageButton cancelImageButton;

    private CameraFragmentViewModel  cameraFragmentViewModel;

    public CameraFragment() {

    }

    public void setOcrInterface(OcrInterface ocrInterface) {
        this.ocrInterface = ocrInterface;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setOcrManagerInterface(StatusInterface statusInterface) {
        this.statusInterface = statusInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        Log.e(TAG, "onCreateView: ");
        initViews(rootView);
        initVariables();
        setListeners();

        return rootView;
    }

    private void initViews(View rootView) {
        loadingFrameLayout = rootView.findViewById(R.id.charger_loading);
        previewFrameLayout = rootView.findViewById(R.id.camera_preview_framelayout);
        cancelImageButton = rootView.findViewById(R.id.cancel_button);
        captureImageButton = rootView.findViewById(R.id.capture_button);
    }

    private void initVariables() {
        cameraFragmentViewModel = new CameraFragmentViewModel(requireContext(),
                previewFrameLayout,
                apiKey,
                this);

    }

    private void setListeners() {
        captureImageButton.setOnClickListener(v -> {
            cameraFragmentViewModel.captureImage();
            captureImageButton.setVisibility(View.GONE);
            cancelImageButton.setVisibility(View.GONE);
            loadingFrameLayout.setVisibility(View.VISIBLE);
        });

        cancelImageButton.setOnClickListener(v -> {
//            if (cameraFragmentViewModel != null) cameraFragmentViewModel.pauseCamera();
            if (ocrInterface != null) ocrInterface.onCancelPressed();
            if (statusInterface != null) statusInterface.onDone();
        });

        previewFrameLayout.setOnClickListener(v -> {
            if (cameraFragmentViewModel != null) cameraFragmentViewModel.autoFocus();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
        if (cameraFragmentViewModel != null) cameraFragmentViewModel.initCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");
        if (cameraFragmentViewModel != null) cameraFragmentViewModel.pauseCamera();
    }

    @Override
    public void onResult(ArrayList<String> resultArrayList) {
        if (ocrInterface != null) ocrInterface.onOcrResult(resultArrayList);
//            if (cameraFragmentViewModel != null) cameraFragmentViewModel.pauseCamera();
        if (statusInterface != null) statusInterface.onDone();
    }

    @Override
    public void onError(int errorCode) {
//        if (cameraFragmentViewModel != null) cameraFragmentViewModel.pauseCamera();
        if (ocrInterface != null) ocrInterface.onError(errorCode);
        if (statusInterface != null) statusInterface.onDone();
    }


}
