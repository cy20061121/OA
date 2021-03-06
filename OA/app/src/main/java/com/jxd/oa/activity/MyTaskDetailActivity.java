package com.jxd.oa.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jxd.common.view.JxdAlertDialog;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.Task;
import com.jxd.oa.bean.User;
import com.jxd.oa.constants.Const;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.view.AttachmentViewView;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.exception.DbException;
import com.yftools.view.annotation.ContentView;
import com.yftools.view.annotation.ViewInject;

/**
 * *****************************************
 * Description ：工作详情
 * Created by cy on 2014/8/4.
 * *****************************************
 */
@ContentView(R.layout.activity_my_task_detail)
public class MyTaskDetailActivity extends AbstractActivity {

    @ViewInject(R.id.title_tv)
    private TextView title_tv;
    @ViewInject(R.id.principal_tv)
    private TextView principal_tv;
    @ViewInject(R.id.participant_tv)
    private TextView participant_tv;
    @ViewInject(R.id.important_tv)
    private TextView important_tv;
    @ViewInject(R.id.category_tv)
    private TextView category_tv;
    @ViewInject(R.id.starDate_tv)
    private TextView starDate_tv;
    @ViewInject(R.id.endDate_tv)
    private TextView endDate_tv;
    @ViewInject(R.id.content_tv)
    private TextView content_tv;
    @ViewInject(R.id.attachment_label)
    private TextView attachment_label;
    @ViewInject(R.id.schedule_avv)
    private AttachmentViewView schedule_avv;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_title_my_task_detail));
        task = (Task) getIntent().getSerializableExtra("task");
        initData();
    }

    public void initData() {
        title_tv.setText(task.getTitle());
        important_tv.setText(Const.getName("TYPE_IMPORTANT_", task.getImportant()));
        if (task.getCategory() != null) {
            category_tv.setText(task.getCategory().getName());
        }
        starDate_tv.setText(task.getStartDate());
        endDate_tv.setText(task.getEndDate());
        content_tv.setText(task.getContent());
        if (!TextUtils.isEmpty(task.getAttachmentName()) && !TextUtils.isEmpty(task.getAttachmentSize())) {
            attachment_label.setVisibility(View.VISIBLE);
            schedule_avv.setVisibility(View.VISIBLE);
            schedule_avv.initData(task.getAttachmentName(), task.getAttachmentSize());
        } else {
            attachment_label.setVisibility(View.GONE);
            schedule_avv.setVisibility(View.GONE);
        }
        //负责人
        try {
            User principalUser = DbOperationManager.getInstance().getBeanById(User.class, task.getPrincipal());
            if (principalUser != null) {
                principal_tv.setText(principalUser.getName());
            }
        } catch (DbException e) {
            LogUtil.e(e);
        }
        //参与人
        if (!TextUtils.isEmpty(task.getParticipant())) {
            StringBuffer sb = new StringBuffer();
            String[] participants = task.getParticipant().split(",");
            if (participants != null) {
                for (String participant : participants) {
                    try {
                        User participantUser = DbOperationManager.getInstance().getBeanById(User.class, participant);
                        if (participantUser != null) {
                            sb.append(participantUser.getName()).append(";");
                        }
                    } catch (DbException e) {
                        LogUtil.e(e);
                    }

                }
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            participant_tv.setText(sb.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                new JxdAlertDialog(this, getString(R.string.txt_tips), "确定设为已完成吗？", getString(R.string.txt_confirm), getString(R.string.txt_cancel)) {
                    @Override
                    protected void positive() {
                        try {
                            task.setFinished(true);
                            DbOperationManager.getInstance().saveOrUpdate(task);
                            sendRefresh();
                            finish();
                        } catch (DbException e) {
                            LogUtil.e(e);
                        }
                    }
                }.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
