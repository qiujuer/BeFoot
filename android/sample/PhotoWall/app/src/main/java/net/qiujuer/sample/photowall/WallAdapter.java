package net.qiujuer.sample.photowall;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JuQiu on 16/7/31.
 */

public class WallAdapter extends RecyclerView.Adapter<WallAdapter.Holder> {
    private final List<PhotoBean> mData = new ArrayList<>();
    private Callback mCallback;

    public WallAdapter(Callback callback) {
        mCallback = callback;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.lay_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        PhotoBean bean = mData.get(position);
        holder.bind(bean, mCallback.getGlideManager());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<PhotoBean> beanList) {
        //清空原有数据
        mData.clear();
        //判断传入数据并赋值
        if (beanList != null && beanList.size() > 0) {
            PhotoBean bean = beanList.get(1);
            for (int i = 0;i<300;i++){
                mData.add(bean);
            }
            //mData.addAll(beanList);
        }
        //通知更新
        notifyDataSetChanged();
    }

    public interface Callback {
        RequestManager getGlideManager();
    }

    static class Holder extends RecyclerView.ViewHolder {
        private ImageView mImage;

        public Holder(View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.iv_photo);
        }

        public void bind(PhotoBean bean, RequestManager manager) {
            manager.load(bean.getPath())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(mImage);
        }
    }
}
