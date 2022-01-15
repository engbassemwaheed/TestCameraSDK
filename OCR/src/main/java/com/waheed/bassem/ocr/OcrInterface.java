package com.waheed.bassem.ocr;

import java.util.ArrayList;

public interface OcrInterface {

    void onOcrResult (ArrayList<String> text);
    void onBackPressed ();
    void onError(int errorCode);
}