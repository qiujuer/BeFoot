package net.qiujuer.aidlservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import net.qiujuer.sample.service.IServiceAidlInterface;
import net.qiujuer.sample.service.IndependentService;
import net.qiujuer.sample.service.bean.User;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = (TextView) findViewById(R.id.txt_str);

        bindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBindService();
    }

    private IServiceAidlInterface mService = null;

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = IServiceAidlInterface.Stub.asInterface(iBinder);
            if (mService != null)
                run();
            else
                showText("Bind Error.");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }
    };

    private void bindService() {
        // UnBind
        unBindService();

        Intent intent = new Intent(this, IndependentService.class);
        bindService(intent, mConn, Context.BIND_AUTO_CREATE);
    }

    private void unBindService() {
        // Service
        IServiceAidlInterface service = mService;
        mService = null;
        if (service != null) {
            unbindService(mConn);
        }
    }

    private void run() {
        User user = null;
        try {
            user = mService.getUser();
            showText(user.toString());

            mService.addAge();
            user = mService.getUser();
            showText(user.toString());

            mService.setName("FangFang");
            user = mService.getUser();
            showText(user.toString());

        } catch (RemoteException e) {
            e.printStackTrace();
            showText(e.toString());
        }
    }

    private void showText(String str) {
        Log.d(TAG, str);
        mText.append("\n");
        mText.append(str);
    }
}
