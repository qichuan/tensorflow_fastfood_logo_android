package com.example.android.tflitecamerademo;

/**
 * Created by Qichuan on 16/3/18.
 */
public class ImageClassificationResult {
    public String resultText;
    public Float confidence;

    public ImageClassificationResult(String resultText, Float confidence) {
        this.resultText = resultText;
        this.confidence = confidence;
    }
}
