package com.waheed.bassem.ocr.utils;

public class ErrorCode {
    /**
     * Error with API key provided
     */
    public static final int API_KEY_ERROR = 1;
    /**
     * Network issue
     */
    public static final int OCR_REQUEST_ERROR = 2;
    /**
     * Camera didn't open
     */
    public static final int CAMERA_INIT_ERROR = 3;
    /**
     * Camera didn't capture the image
     */
    public static final int CAMERA_CAPTURE_ERROR = 4;
    /**
     * Camera doesn't have auto foucs (not fatal so you can ignore it) or we can't auto focus now
     */
    public static final int CAMERA_AUTO_FOCUS_ERROR = 5;
    /**
     * Error while converting the image to base64
     */
    public static final int IMAGE_BASE64_ERROR = 6;

}
