package com.waheed.bassem.ocr;

import androidx.fragment.app.FragmentManager;

import com.waheed.bassem.ocr.display.CameraFragment;


public class OcrManager implements StatusInterface {

    public static OcrManager ocrManager;

    private final CameraFragment cameraFragment;
    private final String apiKey;
    private FragmentManager fragmentManager;

    private OcrManager(String apiKey) {
        this.apiKey = apiKey;
        cameraFragment = new CameraFragment();
        cameraFragment.setOcrManagerInterface(this);
    }

    public static OcrManager getInstance(String apiKey) {
        if (ocrManager == null) {
            ocrManager = new OcrManager(apiKey);
        }
        return ocrManager;
    }

    public void startOCR(FragmentManager fragmentManager, int containerId, OcrInterface ocrInterface) {
        cameraFragment.setApiKey(apiKey);
        cameraFragment.setOcrInterface(ocrInterface);

        this.fragmentManager = fragmentManager;

        fragmentManager.beginTransaction()
                .replace(containerId, cameraFragment)
                .commit();
    }

    @Override
    public void onCancelPressed() {
        if (fragmentManager != null)
            fragmentManager.beginTransaction().remove(cameraFragment).commit();
    }

}
