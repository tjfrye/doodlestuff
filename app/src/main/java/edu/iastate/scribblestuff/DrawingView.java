package edu.iastate.scribblestuff;

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
    private Bitmap getMyBitmap(){
        return myBitmap;
    }
}
