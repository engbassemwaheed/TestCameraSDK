package com.waheed.bassem.ocr;

import java.util.ArrayList;

public interface OcrInterface extends StatusInterface{
    void onOcrResult (ArrayList<String> text);
    void onError(int errorCode);
}
