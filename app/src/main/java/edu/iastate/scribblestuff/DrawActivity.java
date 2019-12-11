package edu.iastate.scribblestuff;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DrawActivity extends AppCompatActivity implements SensorEventListener {
    private static final float NORMAL_GRAVITY = 2.7f;
    private static final int SHAKE_DELAY = 500;
    private static final int RESET_SHAKE_LENGTH = 3000;
    private SensorEventListener mListener;
    private DrawingView drawingView;
    private SeekBar drawThickness;
    private SeekBar drawColor;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_draw);
            drawingView = findViewById(R.id.canvasPage);

            // Create a custom ontouch listener object.
            View.OnTouchListener onTouchListener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {

                    float x = event.getX();
                    float y = event.getY();
                   DrawingView dView = (DrawingView) view;
                    Path path;

                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            path = new Path();
                            path.moveTo(x, y);
                            dView.addPath(path);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            path = dView.getLastPath();
                            if (path != null) {
                                path.lineTo(x, y);
                            }
                            break;
                    }

                    dView.invalidate();

                    return true;
                }
            };
            drawingView.setOnTouchListener(onTouchListener);
            drawThickness = findViewById(R.id.seekBarThick);

            drawThickness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    drawingView.setCurrentWidth(progress);
                    drawThickness.setThumbTintList(ColorStateList.valueOf(Color.BLACK));
                    drawThickness.getProgressDrawable().setColorFilter(
                            Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

            }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            drawColor = findViewById(R.id.seekBarColor);
            drawColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int currentColor=Color.BLACK;
                    switch (progress) {
                        case 1:  currentColor = Color.RED;
                            break;
                        case 2:  currentColor = Color.rgb(255,165,0);//Orange
                            break;
                        case 3:  currentColor = Color.YELLOW;
                            break;
                        case 4:  currentColor = Color.GREEN;
                            break;
                        case 5:  currentColor = Color.BLUE;
                            break;
                        case 6:  currentColor = Color.rgb(63,0,255);//indigo
                            break;
                        case 7:  currentColor = Color.MAGENTA;
                            break;
                        case 8:  currentColor = Color.rgb(255,182,193);//pink
                            break;
                        case 9:  currentColor = Color.WHITE;
                            break;
                        case 10: currentColor = Color.BLACK;
                            break;
                        default: currentColor = Color.BLACK;
                            break;
                    }
                    drawingView.setCurrentColor(currentColor);
                    drawColor.setThumbTintList(ColorStateList.valueOf(currentColor));
                    drawColor.getProgressDrawable().setColorFilter(
                            currentColor, android.graphics.PorterDuff.Mode.SRC_IN);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        public void setColor(View view) {
            ColorDrawable buttonColor = (ColorDrawable) view.getBackground();
            drawingView.setCurrentColor(buttonColor.getColor());
            if (view.getTag() != null && view.getTag().equals("eraser")) {
                drawingView.setCurrentWidth(drawColor.getProgress() * 4);
            } else {
                drawingView.setCurrentWidth(drawColor.getProgress());
            }
        }

        public void deleteDrawing(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to erase everything?")
                    .setTitle("Delete Drawing")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            drawingView.erase();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    //TODO add a shake feature to delete the drawing
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (mListener != null) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
//            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);
//
//            if (gForce > NORMAL_GRAVITY) {
//                final long now = System.currentTimeMillis();
//                // ignore shake events too close to each other (500ms)
//                if (mShakeTimestamp + SHAKE_DELAY > now) {
//                    return;
//                }
//
//                // reset the shake count after 3 seconds of no shakes
//                if (mShakeTimestamp + RESET_SHAKE_LENGTH < now) {
//                    mShakeCount = 0;
//                }
//
//                mShakeTimestamp = now;
//                mShakeCount++;
//
//                mListener.onShake(mShakeCount);
//            }

        }
    }
    @Override
    public void onAccuracyChanged (Sensor sensor,int accuracy){

    }
}

