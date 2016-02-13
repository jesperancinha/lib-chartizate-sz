package com.steelzack.chartizate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.steelzack.chartizate.objects.ChartizateCharacterImg;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author joao
 */
public class ChartizateImageManagerImpl extends ChartizateImageManager<Integer, Typeface> {

    private final InputStream inputStream;
    private Bitmap bitmap;

    public ChartizateImageManagerImpl(final InputStream inputStream) {
        this.inputStream = inputStream;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        try {
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (Exception e) {
            bitmap = null;
            e.printStackTrace();
        }
    }

    @Override
    public int getImageWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getImageHeight() {
        return bitmap.getHeight();
    }

    @Override
    int getImagePixelRGB(int x, int y) {
        return bitmap.getPixel(x, y);
    }

    @Override
    Integer createColor(int alpha, int red, int green, int blue) {
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    public void saveImage( //
                           ChartizateCharacterImg<?>[][] chartizateCharacterImgs, //
                           ChartizateFontManager<Typeface> chartizateFontManager, //
                           String outputFile, //
                           int outputWidth, //
                           int outputHeight //
    ) throws IOException {

        final Bitmap bitmap = Bitmap.createBitmap(outputWidth, outputHeight, Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        final Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(chartizateFontManager.getFontSize());
        paint.setTypeface(chartizateFontManager.getFont());
        paint.setColor(Color.BLACK);
        canvas.drawPaint(paint);

        int currentWidth = 0;
        for (int i = 0; i < chartizateCharacterImgs.length; i++) {
            int rowLength = chartizateCharacterImgs[i].length;
            for (int j = 0; j < rowLength; j++) {
                final ChartizateCharacterImg<Integer> character = (ChartizateCharacterImg<Integer>) chartizateCharacterImgs[i][j];
                paint.setColor(character.getFg());
                canvas.drawText(character.toString(), (float) currentWidth, (float) (chartizateFontManager.getFontSize() * (i + 1)), paint);
                currentWidth += chartizateCharacterImgs[i][j].getWidth();
            }
            currentWidth = 0;
        }


        final FileOutputStream out = new FileOutputStream(outputFile);
        if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) { //
            Log.i("File", outputFile + " is saved");
        }
    }
}
