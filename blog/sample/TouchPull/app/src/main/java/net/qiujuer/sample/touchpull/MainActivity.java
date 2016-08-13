package net.qiujuer.sample.touchpull;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final float TOUCH_MOVE_MAX_Y = 600;

    // The Pull view
    private TouchPullView mTouchPullView;

    // The drag value
    private float mTouchMoveStartY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTouchPullView = (TouchPullView) findViewById(R.id.pull);
        findViewById(R.id.activity_main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchMoveStartY = event.getY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float y = event.getY();
                        if (mTouchMoveStartY <= y) {
                            float moveSize = y - mTouchMoveStartY;
                            mTouchPullView.setProgress(moveSize > TOUCH_MOVE_MAX_Y ? 1 : moveSize / TOUCH_MOVE_MAX_Y);
                        }
                        return true;
                    default:
                        mTouchPullView.animToOrigin();
                        return false;
                }
            }
        });
    }


}
