package com.jxd.oa.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.EmailRecipient;
import com.jxd.oa.bean.Notice;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.view.AttachmentViewView;
import com.yftools.ViewUtil;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ViewInject;

/**
 * *****************************************
 * Description ：通知详情
 * Created by cy on 2014/8/14.
 * *****************************************
 */
public class NoticeDetailActivity extends AbstractActivity {

    @ViewInject(R.id.title_tv)
    private TextView title_tv;
    @ViewInject(R.id.createdUser_tv)
    private TextView createdUser_tv;
    @ViewInject(R.id.publishTime_tv)
    private TextView publishTime_tv;
    @ViewInject(R.id.content_tv)
    private TextView content_tv;
    @ViewInject(R.id.attachment_label)
    private TextView attachment_label;
    @ViewInject(R.id.email_avv)
    private AttachmentViewView email_avv;
    private Notice notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle("通知详情");
        notice = (Notice) getIntent().getSerializableExtra("notice");
        initData();
    }

    private void initData() {
        title_tv.setText(notice.getTitle());
        if (notice.getPublishTime() != null) {
            publishTime_tv.setText(DateUtil.dateTimeToString(notice.getPublishTime()));
        }
        content_tv.setText(Html.fromHtml(notice.getContent()));
        if (!TextUtils.isEmpty(notice.getAttachmentName()) && !TextUtils.isEmpty(notice.getAttachmentSize())) {
            attachment_label.setVisibility(View.VISIBLE);
            email_avv.setVisibility(View.VISIBLE);
            email_avv.initData(notice.getAttachmentName(), notice.getAttachmentSize());
        } else {
            attachment_label.setVisibility(View.GONE);
            email_avv.setVisibility(View.GONE);
        }
        if (notice.getCreatedUser() != null) {
            createdUser_tv.setText(notice.getCreatedUser().getName());
        }

    }
}