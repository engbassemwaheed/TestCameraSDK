package com.waheed.bassem.ocr;

import java.util.ArrayList;

public interface OcrInterface {

    /**
     * @param textArrayList result textArrayList
     */
    void onOcrResult (ArrayList<String> textArrayList);

    /**
     * @param errorCode error codes will be found in ErrorCode.java
     */
    void onError(int errorCode);

    /**
     * when the user clicks on cancel
     */
    void onCancelPressed();
}
