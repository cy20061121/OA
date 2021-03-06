package com.jxd.oa.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.adapter.base.AbstractAdapter;
import com.jxd.oa.bean.Task;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.List;

/**
 * *****************************************
 * Description ：我的工作
 * Created by cy on 2014/9/14.
 * *****************************************
 */
public class TaskAdapter extends AbstractAdapter<Task> {
    public TaskAdapter(Context context, List<Task> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = getInflater().inflate(R.layout.item_task, null);
            ViewUtil.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title_tv.setText(getItem(position).getTitle());
        if (TextUtils.isEmpty(getItem(position).getAttachmentName())) {
            viewHolder.title_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            viewHolder.title_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_attach, 0, 0, 0);
        }
        viewHolder.date_tv.setText("完成时间：" + getItem(position).getEndDate());
        if (!getItem(position).isFinished()) {
            viewHolder.status_tv.setTextColor(getContext().getResources().getColor(R.color.color_red));
        } else {
            viewHolder.status_tv.setTextColor(getContext().getResources().getColor(R.color.color_black_font));
        }
        viewHolder.status_tv.setText(getItem(position).isFinished() ? "已完成" : "待办理");
        return view;
    }

    static class ViewHolder {
        @ViewInject(R.id.title_tv)
        private TextView title_tv;
        @ViewInject(R.id.date_tv)
        private TextView date_tv;
        @ViewInject(R.id.status_tv)
        private TextView status_tv;
    }
}
