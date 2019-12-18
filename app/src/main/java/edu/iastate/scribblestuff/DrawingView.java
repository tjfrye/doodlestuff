package edu.iastate.scribblestuff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DrawingView extends View {

    private ArrayList<Path> paths = new ArrayList<>();
    private ArrayList<Integer> colors = new ArrayList<>();
    private int currentColor = 0xFF000000;
    private ArrayList<Integer> widths = new ArrayList<>();
    private int currentWidth = 6;
    private int Width = 400;
    private int Height = 600;
    private Bitmap myBitmap = Bitmap.createBitmap(Width, Height, Bitmap.Config.RGB_565 );
    private static String TAG = "DrawingView";

    /**
     *
     * @param context
     */
    public DrawingView(Context context) {
        super(context);
        Log.d("DrawViewConstruct","Called");
    }

    /**
     *
     * @param context
     * @param attrs
     */
    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     *
     * @param path
     */
    public void addPath(Path path) {
        paths.add(path);
        colors.add(currentColor);
        widths.add(currentWidth);
    }

    /**
     *
     * @return
     */
    public Path getLastPath() {
        if (paths.size() > 0) {
            return paths.get(paths.size() - 1);
        }

        return null;
    }

    /**
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
      //  canvas.setBitmap(myBitmap);//sets bitmap for the canvas to draw on
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

    /**
     *
     * @param color
     */
    public void setCurrentColor(int color) {
        currentColor = color;
    }

    /**
     *
     * @param width
     */
    public void setCurrentWidth(int width) {
        currentWidth = (width + 1) * 2;
    }

    /**
     *
     */
    public void erase() {
        paths.clear();
        colors.clear();
        widths.clear();
        invalidate();
    }

    /**
     *
     * @return
     */
    @SuppressLint("WrongThread")
    private Bitmap getMyBitmap() {
        return myBitmap;
    }

    /**
     *
     * @return
     */
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
