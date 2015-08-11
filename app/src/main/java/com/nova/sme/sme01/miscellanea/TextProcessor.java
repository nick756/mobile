package com.nova.sme.sme01.miscellanea;

import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TextProcessor {
    private Paint paint  = new Paint();
    private Rect  bounds = new Rect();


    public int getLines(TextView textView, int viewWidthPx, String text, float text_size ) {
        paint.setTextSize(text_size);
        paint.getTextBounds(text, 0, text.length(), bounds);

        int width = (int) Math.ceil( bounds.width());

        return (width/viewWidthPx) + 1;
/*
        paint.setTextSize(text_size);
//        paint.setTypeface(yourTextViewTypeface);

        List<String> strings = splitWordsIntoStringsThatFit(text, viewWidthPx, paint);
        textView.setText(TextUtils.join("\n", strings));

        int lineCount = strings.size();

        return lineCount;
*/
    }

    public TextProcessor() {

    }
    public List<String> splitWordsIntoStringsThatFit(String source, float maxWidthPx, Paint paint) {
        ArrayList<String> result = new ArrayList<>();

        ArrayList<String> currentLine = new ArrayList<>();

        String[] sources = source.split("\\s");
        for(String chunk : sources) {
            if(paint.measureText(chunk) < maxWidthPx) {
                processFitChunk(maxWidthPx, paint, result, currentLine, chunk);
            } else {
                //the chunk is too big, split it.
                List<String> splitChunk = splitIntoStringsThatFit(chunk, maxWidthPx, paint);
                for(String chunkChunk : splitChunk) {
                    processFitChunk(maxWidthPx, paint, result, currentLine, chunkChunk);
                }
            }
        }

        if(! currentLine.isEmpty()) {
            result.add(TextUtils.join(" ", currentLine));
        }
        return result;
    }

    /**
     * Splits a string to multiple strings each of which does not exceed the width
     * of maxWidthPx.
     */
    private List<String> splitIntoStringsThatFit(String source, float maxWidthPx, Paint paint) {
        if(TextUtils.isEmpty(source) || paint.measureText(source) <= maxWidthPx) {
            return Arrays.asList(source);
        }

        ArrayList<String> result = new ArrayList<>();
        int start = 0;
        for(int i = 1; i <= source.length(); i++) {
            String substr = source.substring(start, i);
            if(paint.measureText(substr) >= maxWidthPx) {
                //this one doesn't fit, take the previous one which fits
                String fits = source.substring(start, i - 1);
                result.add(fits);
                start = i - 1;
            }
            if (i == source.length()) {
                String fits = source.substring(start, i);
                result.add(fits);
            }
        }

        return result;
    }

    /**
     * Processes the chunk which does not exceed maxWidth.
     */
    private void processFitChunk(float maxWidth, Paint paint, ArrayList<String> result, ArrayList<String> currentLine, String chunk) {
        currentLine.add(chunk);
        String currentLineStr = TextUtils.join(" ", currentLine);
        if (paint.measureText(currentLineStr) >= maxWidth) {
            //remove chunk
            currentLine.remove(currentLine.size() - 1);
            result.add(TextUtils.join(" ", currentLine));
            currentLine.clear();
            //ok because chunk fits
            currentLine.add(chunk);
        }
    }
}
