package com.example.keepfresh;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TritonAPIHelper {

    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

    public static String sendPhotoToTriton(Bitmap photoBitmap) throws IOException {
        // Triton 서버의 엔드포인트 URL
        String tritonUrl = "https://your-triton-server.com/api/endpoint";

        // Bitmap을 ByteArray로 변환
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // OkHttpClient 생성
        OkHttpClient client = new OkHttpClient();

        // 사진 파일을 RequestBody에 추가
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("photo", "photo.jpg",
                        RequestBody.create(MEDIA_TYPE_JPEG, byteArray))
                .build();

        // POST 요청 생성
        Request request = new Request.Builder()
                .url(tritonUrl)
                .post(requestBody)
                .build();

        // 요청 실행 및 응답 처리
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            return response.body().string();
        }
    }

}