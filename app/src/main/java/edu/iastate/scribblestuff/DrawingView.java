package edu.iastate.scribblestuff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.Image;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.graphics.Bitmap.CompressFormat.PNG;

public class DrawingView extends View {

    private ArrayList<Path> paths = new ArrayList<>();
    private ArrayList<Integer> colors = new ArrayList<>();
    private int currentColor = 0xFF000000;
    private ArrayList<Integer> widths = new ArrayList<>();
    private int currentWidth = 6;
    private static String TAG = "DrawingView";

    public DrawingView(Context context) {
        super(context);
        Log.d("DrawViewConstruct","Called");
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void addPath(Path path) {
        paths.add(path);
        colors.add(currentColor);
        widths.add(currentWidth);
    }

    public Path getLastPath() {
        if (paths.size() > 0) {
            return paths.get(paths.size() - 1);
        }

        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int i = 0;
        for (Path path : paths) {
            Paint paint = new Paint();
            paint.setColor(colors.get(i));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(widths.get(i));
            canvas.drawPath(path, paint);
            i++;
        }
    }

    public void setCurrentColor(int color) {
        currentColor = color;
    }

    public void setCurrentWidth(int width) {
        currentWidth = (width + 1) * 2;
    }

    public void erase() {
        paths.clear();
        colors.clear();
        widths.clear();
        invalidate();
    }

    //TODO move to correct thread if time
    @SuppressLint("WrongThread")
    public byte[] getDrawing() {
        Bitmap bitmap = Bitmap.createBitmap(1500, 2300, Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(bitmap);
        int i = 0;
        for (Path path : paths) {
            Log.d(TAG, "draw path");
            Paint paint = new Paint();
            paint.setColor(colors.get(i));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(widths.get(i));
            tempCanvas.drawPath(path, paint);
            i++;
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
