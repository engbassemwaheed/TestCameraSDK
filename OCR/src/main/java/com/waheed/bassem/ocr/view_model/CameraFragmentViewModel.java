package com.waheed.bassem.ocr.view_model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.lifecycle.MutableLiveData;

import com.waheed.bassem.ocr.apis.ApiManager;
import com.waheed.bassem.ocr.apis.OcrResultContainer;
import com.waheed.bassem.ocr.camera.CameraInterface;
import com.waheed.bassem.ocr.camera.CameraManager;
import com.waheed.bassem.ocr.display.CameraFragmentInterface;
import com.waheed.bassem.ocr.utils.DataConverter;
import com.waheed.bassem.ocr.utils.ErrorCode;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraFragmentViewModel implements CameraInterface {

    private static final String TAG = "CameraFragmentViewModel";

    private static CameraFragmentViewModel cameraFragmentViewModel;

    private final CameraManager cameraManager;
    private final ApiManager apiManager;
    private final CameraFragmentInterface cameraFragmentInterface;
    private boolean isProcessing;

    public CameraFragmentViewModel(Context context,
                                    FrameLayout previewFrameLayout,
                                    String apiKey,
                                    CameraFragmentInterface cameraFragmentInterface) {
        Log.e(TAG, "CameraFragmentViewModel: ");
        apiManager = ApiManager.getInstance();
        isProcessing = false;
        boolean hasNoError = apiManager.setApiKey(apiKey);
        if (!hasNoError) {
            cameraFragmentInterface.onError(ErrorCode.API_KEY_ERROR);
        }
        cameraManager = new CameraManager(context, previewFrameLayout, this);
        this.cameraFragmentInterface = cameraFragmentInterface;
    }

    public static CameraFragmentViewModel getInstance(Context context,
                                                      FrameLayout previewFrameLayout,
                                                      String apiKey,
                                                      CameraFragmentInterface cameraFragmentInterface) {

        if (cameraFragmentViewModel == null) {
            cameraFragmentViewModel = new CameraFragmentViewModel(context,
                    previewFrameLayout,
                    apiKey,
                    cameraFragmentInterface);
        }
        return cameraFragmentViewModel;
    }

    public void captureImage() {
        Log.e(TAG, "captureImage: ");
        isProcessing = true;
        boolean hasNoError = cameraManager.capture();
        if (!hasNoError) {
            cameraFragmentInterface.onError(ErrorCode.CAMERA_CAPTURE_ERROR);
        }
    }

    public void autoFocus() {
        Log.e(TAG, "autoFocus: ");
        if (!isProcessing) {
            boolean hasNoError = cameraManager.autoFocus();
            if (!hasNoError) {
                cameraFragmentInterface.onError(ErrorCode.CAMERA_AUTO_FOCUS_ERROR);
            }
        }
    }

    @Override
    public void onImageCaptured(Bitmap bitmap) {
        Log.e(TAG, "onImageCaptured: ");
        String base64Image = DataConverter.imageToBase64(bitmap, apiManager.getMaxFileSize());
        boolean hasNoError = apiManager.parseText(base64Image, new Callback<OcrResultContainer>() {
            @Override
            public void onResponse(Call<OcrResultContainer> call, Response<OcrResultContainer> response) {
                isProcessing = false;
                if (response != null) {
                    OcrResultContainer ocrResultContainer = response.body();
                    if (ocrResultContainer != null) {
                        cameraFragmentInterface.onResult(ocrResultContainer.getParsedText());
                    } else {
                        Log.e(TAG, "onResponse: ocrResultContainer is null");
                        cameraFragmentInterface.onError(ErrorCode.OCR_REQUEST_ERROR);
                    }
                } else {
                    Log.e(TAG, "onResponse: response is null");
                    cameraFragmentInterface.onError(ErrorCode.OCR_REQUEST_ERROR);
                }
            }

            @Override
            public void onFailure(Call<OcrResultContainer> call, Throwable t) {
                Log.e(TAG, "onFailure: error " + t.getMessage());
                isProcessing = false;
                cameraFragmentInterface.onError(ErrorCode.OCR_REQUEST_ERROR);
            }
        });

        if (!hasNoError) {
            cameraFragmentInterface.onError(ErrorCode.IMAGE_BASE64_ERROR);
        }
    }

    public void initCamera() {
        Log.e(TAG, "initCamera: ");
        boolean hasNoError = cameraManager.initCamera();
        isProcessing = false;
        if (!hasNoError) {
            cameraFragmentInterface.onError(ErrorCode.CAMERA_INIT_ERROR);
        }
    }

    public void pauseCamera() {
        Log.e(TAG, "pauseCamera: ");
        cameraManager.pauseCamera();
    }
}
