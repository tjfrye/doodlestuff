package edu.iastate.scribblestuff;

import android.content.DialogInterface;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DrawActivity extends AppCompatActivity {

        DrawingView drawingView;
        SeekBar drawThickness;
        SeekBar drawColor;

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
            drawThickness = findViewById(R.id.seekBar);

            drawThickness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    drawingView.setCurrentWidth(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

            }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
        //TODO implent a slide to add more colors
        public void setColor(View view) {
            ColorDrawable buttonColor = (ColorDrawable) view.getBackground();
            drawingView.setCurrentColor(buttonColor.getColor());
            if (view.getTag() != null && view.getTag().equals("eraser")) {
                drawingView.setCurrentWidth(drawThickness.getProgress() * 4);
            } else {
                drawingView.setCurrentWidth(drawThickness.getProgress());
            }
        }
        //TODO add a shake feature to delete the drawing
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
    }

