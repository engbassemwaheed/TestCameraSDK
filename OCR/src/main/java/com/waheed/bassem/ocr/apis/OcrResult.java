package com.waheed.bassem.ocr.apis;

import com.google.gson.annotations.SerializedName;

class OcrResult {

    @SerializedName("ParsedText")
    private final String parsedText;

    OcrResult(String parsedText) {
        this.parsedText = parsedText;
    }

    String getParsedText() {
        return parsedText;
    }
}
