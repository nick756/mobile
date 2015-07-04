package com.nova.sme.sme01;


import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;
import android.widget.TextView;

public class TextAlignment {
    Context context = null;

    public TextAlignment(Context context) {
        this.context = context;
    }

    private int getHeightLineText(String text, int textSize, int maxWidth) {
        TextPaint paint = new TextPaint();
        paint.setTextSize(textSize);

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int height = bounds.height();
        return height;
    }

    private int getWidthLineText(String text, int textSize, int maxWidth) {
        TextPaint paint = new TextPaint();
        paint.setTextSize(textSize);

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.width();
        return width;
    }

    public void ResizeFontSizeByWidth(TextView tvText, String text, float additionalFactor, int maxWidth)
    {
        if (text.length() == 0)
            return;

        int textSize = 200;

        while (getWidthLineText(text, textSize, tvText.getWidth()) > maxWidth)
            textSize--;

        float scaleFactor      =  context.getResources().getDisplayMetrics().scaledDensity;
        float new_text_size    = textSize / (additionalFactor*scaleFactor);
        tvText.setTextSize(new_text_size);
    }
    public void ResizeFontSizeByHeight(TextView tvText, String text, float additionalFactor, int maxHeight)
    {
        if (text.length() == 0)
            return;

        int textSize = 200;

        while (getHeightLineText(text, textSize, tvText.getWidth()) > maxHeight)
            textSize--;

        float scaleFactor      =  context.getResources().getDisplayMetrics().scaledDensity;
        float new_text_size    = textSize / (additionalFactor*scaleFactor);
        tvText.setTextSize(new_text_size);
    }
}
