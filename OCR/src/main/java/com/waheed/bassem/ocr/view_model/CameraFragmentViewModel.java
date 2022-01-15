package com.waheed.bassem.ocr.view_model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

public class CameraFragmentViewModel extends ViewModel implements CameraInterface {

    private static final String TAG = "CameraFragmentViewModel";

    private final MutableLiveData<ArrayList<String>> identifiedTextMutableLiveData;
    private CameraManager cameraManager;
    private final ApiManager apiManager;
    private CameraFragmentInterface cameraFragmentInterface;

    public CameraFragmentViewModel() {
        identifiedTextMutableLiveData = new MutableLiveData<>();
        apiManager = ApiManager.getInstance();
    }

    public void init(Context context, FrameLayout previewFrameLayout, String apiKey, CameraFragmentInterface cameraFragmentInterface) {
        boolean hasNoError = apiManager.setApiKey(apiKey);
        if (!hasNoError) {
            cameraFragmentInterface.onError(ErrorCode.API_KEY_ERROR);
        }
        cameraManager = new CameraManager(context, previewFrameLayout, this);
        this.cameraFragmentInterface = cameraFragmentInterface;
    }

    public MutableLiveData<ArrayList<String>> getIdentifiedTextMutableLiveData() {
        return identifiedTextMutableLiveData;
    }

    public void captureImage() {
        boolean hasNoError = cameraManager.capture();
        if (!hasNoError) {
            cameraFragmentInterface.onError(ErrorCode.CAMERA_CAPTURE_ERROR);
        }
    }

    public void autoFocus() {
        boolean hasNoError = cameraManager.autoFocus();
        if (!hasNoError) {
            cameraFragmentInterface.onError(ErrorCode.CAMERA_AUTO_FOCUS_ERROR);
        }
    }

    @Override
    public void onImageCaptured(Bitmap bitmap) {
        String base64Image = DataConverter.imageToBase64(bitmap, apiManager.getMaxFileSize());
        boolean hasNoError = apiManager.parseText(base64Image, new Callback<OcrResultContainer>() {
            @Override
            public void onResponse(Call<OcrResultContainer> call, Response<OcrResultContainer> response) {
                if (response != null) {
                    OcrResultContainer ocrResultContainer = response.body();
                    if (ocrResultContainer != null) {
                        identifiedTextMutableLiveData.postValue(ocrResultContainer.getParsedText());
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
                cameraFragmentInterface.onError(ErrorCode.OCR_REQUEST_ERROR);
            }
        });

        if (!hasNoError) {
            cameraFragmentInterface.onError(ErrorCode.IMAGE_BASE64_ERROR);
        }
    }

    public void initCamera() {
        boolean hasNoError = cameraManager.initCamera();
        if (!hasNoError) {
            cameraFragmentInterface.onError(ErrorCode.CAMERA_INIT_ERROR);
        }
    }

    public void pauseCamera() {
        cameraManager.pauseCamera();
    }
}
