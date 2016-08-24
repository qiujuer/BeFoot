package net.qiujuer.sample.photowall;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WallAdapter.Callback , View.OnClickListener{
    private static final String TAG = MainActivity.class.getName();

    private final int mId = 100;
    private WallAdapter mAdapter;
    private RequestManager mGlideManager;

    private DataCallback mDataCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initRecycler();
        initLoader();
    }

    private void initView(){
        findViewById(R.id.btn_init).setOnClickListener(this);
        findViewById(R.id.btn_restart).setOnClickListener(this);
        findViewById(R.id.btn_destroy).setOnClickListener(this);
    }

    private void initRecycler() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new WallAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    private void initLoader() {
        mDataCallback = new DataCallback();
        Bundle bundle = new Bundle();
        bundle.putString("Key", "200");
        getSupportLoaderManager().initLoader(mId, bundle, mDataCallback);
    }


    @Override
    public RequestManager getGlideManager() {
        if (mGlideManager == null) {
            synchronized (this) {
                if (mGlideManager == null) {
                    mGlideManager = Glide.with(this);
                }
            }
        }
        return mGlideManager;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_init:
                getSupportLoaderManager().initLoader(mId, null, mDataCallback);
                break;
            case R.id.btn_restart:
                getSupportLoaderManager().restartLoader(mId, null, mDataCallback);
                break;
            case R.id.btn_destroy:
                getSupportLoaderManager().destroyLoader(mId);
                break;
        }
    }


    private class DataCallback implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String argStr = args==null?"null":args.toString();
            log("onCreateLoader id:"+ id+" args:"+argStr);
            return new CursorLoader(MainActivity.this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Images.Media._ID,
                            MediaStore.Images.Media.DATA,
                            MediaStore.Images.Media.MINI_THUMB_MAGIC,
                            MediaStore.Images.Media.DATE_MODIFIED,
                    },
                    null, null,
                    MediaStore.Images.Media.DATE_MODIFIED + " DESC"
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                if (data.getCount() > 0) {
                    List<PhotoBean> beanList = new ArrayList<>();
                    data.moveToFirst();

                    // 在循环外初始化基本数据
                    final int idIndex = data.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                    final int pathIndex = data.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    final int thumbPathIndex = data.getColumnIndexOrThrow(MediaStore.Images.Media.MINI_THUMB_MAGIC);
                    final int dateIndex = data.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED);

                    do{
                        // 当data.moveToNext()为true,那么继续循环拿数据
                        int id = data.getInt(idIndex);
                        String path = data.getString(pathIndex);
                        String thumbPath = data.getString(thumbPathIndex);
                        long date = data.getLong(dateIndex);

                        PhotoBean bean = new PhotoBean(id,path,thumbPath,date);
                        beanList.add(bean);
                    }while (data.moveToNext());
                    // 设置到界面中
                    mAdapter.setData(beanList);

                    log("onLoadFinished:"+beanList.size());
                    getSupportLoaderManager().destroyLoader(mId);
                    return;
                }
            }
            log("onLoadFinished data is null");
            mAdapter.setData(null);


        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            log("onLoaderReset");
            //mAdapter.setData(null);
        }
    }

    private static void log(String str) {
        Log.e(TAG, str);
    }

}
