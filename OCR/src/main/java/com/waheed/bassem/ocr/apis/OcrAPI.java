package com.waheed.bassem.ocr.apis;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

interface OcrAPI {

    @FormUrlEncoded
    @POST(Constants.BASE_OCR_URL)
    Call<OcrResultContainer> parseText (@Field("apikey") String apiKey,
                                        @Field("base64Image") String imageBase64);

}
