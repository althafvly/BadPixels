package tk.al54.dev.badpixels;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
    private static final int[] COLORS = {
            R.color.black, R.color.red, R.color.green, R.color.blue,
            R.color.cyan, R.color.magenta, R.color.yellow, R.color.white
    };
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 50;
    private TouchLinearLayout mainBG;
    private TextView tvn, tvi, tvv;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        final GestureDetector gestureDetector = new GestureDetector(this, new DetectGesture());

        mainBG = findViewById(R.id.mainBG);
        mainBG.setOnClickListener(this);
        mainBG.setOnTouchListener((v, event) -> {
            if (gestureDetector.onTouchEvent(event)) {
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick();
            }
            return true;
        });

        tvn = findViewById(R.id.appname);
        tvi = findViewById(R.id.appinfo);
        tvv = findViewById(R.id.appversion);
        tvv.setText(getString(R.string.version_display, getString(R.string.version),
                versionName, getString(R.string.build), versionCode));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mainBG) {
            i++;
            changeColor();
        }
    }

    private int getCompatColor(int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getColor(colorResId);
        } else {
            return getResources().getColor(colorResId);
        }
    }

    private void changeColor() {
        i = (i % COLORS.length + COLORS.length) % COLORS.length;
        if (tvn.getVisibility() == View.VISIBLE) {
            tvn.setVisibility(View.GONE);
            tvi.setVisibility(View.GONE);
            tvv.setVisibility(View.GONE);
        }
        mainBG.setBackgroundColor(getCompatColor(COLORS[i]));
    }

    private class DetectGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mainBG.performClick();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1 == null || e2 == null) return false;

            float diffX = e1.getX() - e2.getX();
            float diffY = e1.getY() - e2.getY();

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (diffX > 0) {
                        i++;
                    } else {
                        i--;
                    }
                    changeColor();
                    return true;
                }
            } else {
                if (Math.abs(diffY) > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    if (diffY > 0) {
                        i++;
                    } else {
                        i--;
                    }
                    changeColor();
                    return true;
                }
            }
            return false;
        }
    }
}
