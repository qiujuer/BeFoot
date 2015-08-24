package net.qiujuer.example.timeline.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.qiujuer.example.timeline.R;
import net.qiujuer.example.timeline.model.TimeLineModel;
import net.qiujuer.example.timeline.widget.TimeLineMarker;

/**
 * Created by qiujuer
 * on 15/8/23.
 */
public class TimeLineViewHolder extends RecyclerView.ViewHolder {
    private TextView mName;

    public TimeLineViewHolder(View itemView, int type) {
        super(itemView);

        mName = (TextView) itemView.findViewById(R.id.item_time_line_txt);

        TimeLineMarker mMarker = (TimeLineMarker) itemView.findViewById(R.id.item_time_line_mark);
        if (type == ItemType.ATOM) {
            mMarker.setBeginLine(null);
            mMarker.setEndLine(null);
        } else if (type == ItemType.START) {
            mMarker.setBeginLine(null);
        } else if (type == ItemType.END) {
            mMarker.setEndLine(null);
        }

    }

    public void setData(TimeLineModel data) {
        mName.setText("Name:" + data.getName() + " Age:" + data.getAge());
    }
}
