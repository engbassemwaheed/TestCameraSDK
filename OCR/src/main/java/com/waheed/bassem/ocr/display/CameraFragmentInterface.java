package com.waheed.bassem.ocr.display;

import java.util.ArrayList;

public interface CameraFragmentInterface {
    void onResult(ArrayList<String> resultArrayList);
    void onError(int errorCode);
}
