package com.waheed.bassem.ocr;

import androidx.fragment.app.FragmentManager;

import com.waheed.bassem.ocr.display.CameraFragment;

public class OcrManager {

    public static OcrManager ocrManager;

    private final CameraFragment cameraFragment;
    private final String apiKey;

    private OcrManager(String apiKey) {
        this.apiKey = apiKey;
        cameraFragment = new CameraFragment();
    }

    public static OcrManager getInstance(String apiKey) {
        if (ocrManager == null)  {
            ocrManager = new OcrManager(apiKey);
        }
        return ocrManager;
    }

    public void startOCR (FragmentManager fragmentManager, int containerId,  OcrInterface ocrInterface) {
        cameraFragment.setApiKey(apiKey);
        cameraFragment.setOcrInterface(ocrInterface);

        fragmentManager.beginTransaction()
                .replace(containerId, cameraFragment)
                .commit();
    }


}
