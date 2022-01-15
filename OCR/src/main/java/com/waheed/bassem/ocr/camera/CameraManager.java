package com.waheed.bassem.ocr.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.util.Log;
import android.widget.FrameLayout;

import com.waheed.bassem.ocr.utils.ImageCropper;
import com.waheed.bassem.ocr.utils.PermissionChecker;

import java.util.List;

import static android.hardware.Camera.Parameters.FOCUS_MODE_AUTO;

public class CameraManager implements Camera.PictureCallback, Camera.ShutterCallback {

    private static final String TAG = "CameraManager";

    private final FrameLayout previewFrameLayout;
    private final Context context;
    private final CameraInterface cameraInterface;

    private Camera camera;
    private CameraPreview cameraPreview;

    private boolean isViewAdded;
    private boolean isCapturedPressed;

    public CameraManager (Context context, FrameLayout previewFrameLayout, CameraInterface cameraInterface) {
        this.context = context;
        this.previewFrameLayout = previewFrameLayout;
        this.cameraInterface = cameraInterface;
        isViewAdded = false;
        initCamera();
        cameraPreview = CameraPreview.getInstance(context, camera);
    }

    private Camera getCameraInstance() {
        if (camera == null) {
            try {
                camera = Camera.open(); // attempt to get a Camera instance
            } catch (Exception e) {
                Log.e(TAG, "getCameraInstance: Camera is not available (in use or does not exist)");
            }
        }
        return camera;
    }

    public boolean initCamera() {
        isCapturedPressed = false;
        camera = getCameraInstance();
        if (camera == null) {
            return false;
        }
        if (cameraPreview == null) cameraPreview = CameraPreview.getInstance(context, camera);
        if (!isViewAdded) {
            previewFrameLayout.addView(cameraPreview);
            isViewAdded = true;
        }
        cameraPreview.setCamera(camera);
        return true;
    }

    public void pauseCamera () {
        previewFrameLayout.removeAllViews();
        if (camera != null) {
            isViewAdded  = false;
            camera = null;
        }
    }

    public boolean capture() {
        if (!PermissionChecker.checkCameraPermissions(context)) {
            Log.e(TAG, "capture: camera permission is not granted");
            return false;
        } else if (camera == null) {
            Log.e(TAG, "capture: camera is null");
            return false;
        } else if (isCapturedPressed) {
            Log.e(TAG, "capture: capture button has already been pressed");
            return false;
        }
        isCapturedPressed = true;
        camera.takePicture(this, null, this);
        return true;
    }

    public boolean autoFocus () {
        if (!PermissionChecker.checkCameraPermissions(context)) {
            Log.e(TAG, "adjustAutoFocus: camera permission is not granted");
            return false;
        }

        try {
            if (hasAutoFocus(camera)) {
                camera.autoFocus((b, camera) -> {});
                return true;
            } else {
                Log.e(TAG, "adjustAutoFocus: camera doesn't have auto focus feature");
                return false;
            }
        } catch (Exception ex) {
            Log.e(TAG, "adjustAutoFocus: exception: " + ex.getMessage());
            return false;
        }
    }

    private boolean hasAutoFocus(Camera camera) {
        Camera.Parameters params = camera.getParameters();
        List<String> autoFocusModes = params.getSupportedFocusModes();
        return autoFocusModes != null && autoFocusModes.size() > 0 && (autoFocusModes.contains(FOCUS_MODE_AUTO));
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        isCapturedPressed = false;

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        Matrix mat = new Matrix();
        mat.postRotate(90);  // angle is the desired angle you wish to rotate
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);

        Bitmap resultBitmap = ImageCropper.cropImage(context,
                previewFrameLayout,
                rotatedBitmap,
                previewFrameLayout.getWidth(),
                75);


        cameraInterface.onImageCaptured(resultBitmap);
    }

    @Override
    public void onShutter() {

    }
}
