package com.waheed.bassem.ocr;

import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.waheed.bassem.ocr.display.CameraFragment;


public class OcrManager implements StatusInterface {

    private static final String TAG = "OcrManager";

    public static OcrManager ocrManager;

    private CameraFragment cameraFragment;
    private final String apiKey;
    private FragmentManager fragmentManager;

    private OcrManager(String apiKey) {
        Log.e(TAG, "OcrManager: ");
        this.apiKey = apiKey;
        cameraFragment = new CameraFragment();
        cameraFragment.setOcrManagerInterface(this);
    }

    /**
     * @param apiKey api key you will get from http://ocr.space/
     */
    public static OcrManager getInstance(String apiKey) {
        Log.e(TAG, "getInstance: ");
        if (ocrManager == null) {
            ocrManager = new OcrManager(apiKey);
        }
        return ocrManager;
    }

    /**
     * @param fragmentManager from getSupportFragmentManager()
     * @param containerId container view id in which the camera fragment will be displayed
     * @param ocrInterface interface containing results, errors and cancel operation
     */
    public void startOCR(FragmentManager fragmentManager, int containerId, OcrInterface ocrInterface) {
        Log.e(TAG, "startOCR: ");
        cameraFragment.setApiKey(apiKey);
        cameraFragment.setOcrInterface(ocrInterface);

        this.fragmentManager = fragmentManager;

        fragmentManager.beginTransaction()
                .replace(containerId, cameraFragment)
                .commit();
    }



    @Override
    public void onDone() {
        Log.e(TAG, "onDone: ");
        if (fragmentManager != null)
            fragmentManager.beginTransaction().remove(cameraFragment).commit();
    }

}
