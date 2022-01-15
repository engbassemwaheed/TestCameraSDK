package com.waheed.bassem.ocr.camera;


import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

import static android.hardware.Camera.Parameters.FLASH_MODE_AUTO;
import static android.hardware.Camera.Parameters.FOCUS_MODE_AUTO;


public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraPreview";

    private static final int PICTURE_WIDTH = 1200;
    private static final int PICTURE_HEIGHT = 1200;

    private static CameraPreview cameraPreview;

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.Parameters mParameters = null;

    private CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        setCamera(camera);
    }

    public static CameraPreview getInstance (Context context, Camera camera) {
        if (cameraPreview == null) {
            cameraPreview = new CameraPreview(context, camera);
        }
        return cameraPreview;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            Log.e(TAG, "surfaceCreated: error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            Log.e(TAG, "surfaceChanged: preview surface does not exist");
            return;
        }

        if (mCamera == null) {
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // get Camera parameters
        Camera.Parameters params = mCamera.getParameters();

//        Camera.Size cameraSize = getOptimalPreviewSize(params.getSupportedPreviewSizes(), w, h);
//        Camera.Size imageSize = getOptimalPreviewSize(params.getSupportedPictureSizes(), PICTURE_WIDTH, PICTURE_HEIGHT);
//        params.setPreviewSize(cameraSize.width, cameraSize.height);
//
//        params.setPictureSize(imageSize.width, imageSize.height);

        List<String> supportedFlashModes = params.getSupportedFlashModes();
        if (supportedFlashModes != null && supportedFlashModes.size() > 0) {
            if (supportedFlashModes.contains(FLASH_MODE_AUTO)) {
                params.setFlashMode(FLASH_MODE_AUTO);
            }
        }

        List<String> supportedFoucsModes = params.getSupportedFocusModes();
        if (supportedFoucsModes != null && supportedFoucsModes.size() > 0) {
            if (supportedFoucsModes.contains(FOCUS_MODE_AUTO)) {
                params.setFocusMode(FOCUS_MODE_AUTO);
            }
        }

        // set Camera parameters
        mParameters = params;

        mCamera.setParameters(params);
        mCamera.setDisplayOrientation(90);


        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            startPreview();

        } catch (Exception e) {
            Log.e(TAG, "surfaceChanged: Error starting camera preview: " + e.getMessage());
        }
    }

    public void startPreview() {
        try {
            mCamera.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "startPreview: error " + e.getMessage());
        }
    }


    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public void setCamera(Camera camera) {
        if (mCamera == camera) {
            return;
        }

        stopPreviewAndFreeCamera();

        mCamera = camera;

        if (mCamera != null) {

            if (mParameters != null) {
                mCamera.setParameters(mParameters);
            }
            mCamera.setDisplayOrientation(90);

            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Important: Call startPreview() to start updating the preview
            // surface. Preview must be started before you can take a picture.
            mCamera.startPreview();
        }
    }

    public void stopPreviewAndFreeCamera() {

        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera.stopPreview();

            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            mCamera.release();

            mCamera = null;
        }
    }

}
