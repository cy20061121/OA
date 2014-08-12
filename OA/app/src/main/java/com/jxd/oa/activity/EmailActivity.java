package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jxd.common.view.JxdAlertDialog;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.adapter.EmailAdapter;
import com.jxd.oa.bean.Email;
import com.jxd.oa.bean.EmailRecipient;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.ParamManager;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.exception.DbException;
import com.yftools.exception.HttpException;
import com.yftools.http.RequestParams;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.http.client.multipart.content.StringBody;
import com.yftools.json.Json;
import com.yftools.ui.DatePickUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnItemClick;

import java.util.List;

/**
 * *****************************************
 * Description ：邮件
 * Created by cy on 2014/8/4.
 * *****************************************
 */
public class EmailActivity extends AbstractActivity {

    private static final int CODE_EMAIL_ADD = 101;
    @ViewInject(R.id.mListView)
    private ListView mListView;
    private EmailAdapter adapter;
    private List<Email> emailList;
    private Status status = Status.INBOX;

    private enum Status {
        INBOX, OUTBOX, DRAFT_BOX;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        ViewUtil.inject(this);
        initTopBar();
        registerForContextMenu(mListView);
        initData();
        try {
            List<EmailRecipient> emailRecipients = DbOperationManager.getInstance().getBeans(Selector.from(EmailRecipient.class).where("toId", "=", SysConfig.getInstance().getUserId()));
            DbOperationManager.getInstance().getBeans(Selector.from(Email.class).where("fromId", "!=", SysConfig.getInstance().getUserId()).and("id", "in", emailRecipients));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        //取收件箱中数据
        try {
            emailList = DbOperationManager.getInstance().getBeans(Selector.from(Email.class).where("fromId", "!=", SysConfig.getInstance().getUserId()));
        } catch (DbException e) {
            LogUtil.e(e);
        }
        fillList();
    }

    private void fillList() {
        if (adapter == null) {
            adapter = new EmailAdapter(mContext, emailList);
            mListView.setAdapter(adapter);
        } else {
            adapter.setObjects(emailList);
            adapter.notifyDataSetChanged();
        }
    }


    @OnItemClick(R.id.mListView)
    public void listItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, EmailDetailActivity.class);
        if (adapter.getItem(position).getLocalId() != null) {//草稿直接到发布界面
            intent.setClass(mContext, EmailAddActivity.class);
        }
        intent.putExtra("emailId", adapter.getItem(position).getId());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (status == Status.INBOX) {
            getMenuInflater().inflate(R.menu.menu_sync_read, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_sync_add, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivityForResult(new Intent(mContext, EmailAddActivity.class), CODE_EMAIL_ADD);
                return true;
            case R.id.action_sync:
                new DatePickUtil(mContext, "请选择开始时间", new DatePickUtil.DateSetFinished() {
                    @Override
                    public void onDateSetFinished(String pickYear, String pickMonth, String pickDay) {
                        syncData(Email.class, pickYear + "-" + pickMonth + "-" + pickDay);
                    }
                }).showDateDialog();
                return true;
            case R.id.action_read:
                readSubmit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void readSubmit() {
        if (emailList != null) {
            displayToast("暂无未读邮件");
            return;
        }
        final StringBuffer sb = new StringBuffer();
        for (Email email : emailList) {
            sb.append(email.getId()).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        RequestParams params = ParamManager.setDefaultParams();
        params.addBodyParameter("id", sb.toString());
        HttpUtil.getInstance().send(ParamManager.parseBaseUrl("readEmail.action"), params, new RequestCallBack<Json>() {
            @Override
            public void onSuccess(ResponseInfo<Json> responseInfo) {
                //更新接收时间
                try {
                    DbOperationManager.getInstance().getBeanFirst(Selector.from(EmailRecipient.class).where("emailId", "in", sb.toString()).and("toId", "=", SysConfig.getInstance().getUserId()));
                    refreshData();
                    displayToast("邮件全部已读");
                } catch (DbException e) {
                    LogUtil.e(e);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                displayToast(msg);
            }
        });
    }


    private void initTopBar() {
        ActionBar actionBar = getSupportActionBar();
        //这里可以设置下拉的文本跟下拉的item文本不一样
        /*android.R.layout.simple_spinner_dropdown_item*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.menu_email));
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.menu_email));
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.custom_spinner, getResources().getStringArray(R.array.menu_email));
        //adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ActionBar.OnNavigationListener callback = new MyOnNavigationListener();
        actionBar.setListNavigationCallbacks(adapter, callback);//new ActionMenuAdapter(this,new String[]{"全部通讯录","分类通讯录"})
    }

    private class MyOnNavigationListener implements ActionBar.OnNavigationListener {
        @Override
        public boolean onNavigationItemSelected(int itemPosition, long itemId) {
            switch (itemPosition) {
                case 0://收件箱
                    if (status != Status.INBOX) {
                        status = Status.INBOX;
                        try {
                            emailList = DbOperationManager.getInstance().getBeans(Selector.from(Email.class).where("fromId", "!=", SysConfig.getInstance().getUserId()));
                        } catch (DbException e) {
                            LogUtil.e(e);
                        }
                        fillList();
                        supportInvalidateOptionsMenu();
                    }
                    break;
                case 1://发件箱
                    if (status != Status.OUTBOX) {
                        status = Status.OUTBOX;
                        try {
                            emailList = DbOperationManager.getInstance().getBeans(Selector.from(Email.class).where("fromId", "=", SysConfig.getInstance().getUserId()).and("localId", "=", null));
                        } catch (DbException e) {
                            LogUtil.e(e);
                        }
                        fillList();
                        supportInvalidateOptionsMenu();
                    }
                    break;
                case 2://草稿箱
                    if (status != Status.DRAFT_BOX) {
                        status = Status.DRAFT_BOX;
                        try {
                            emailList = DbOperationManager.getInstance().getBeans(Selector.from(Email.class).where("fromId", "=", SysConfig.getInstance().getUserId()).and("localId", "!=", null));
                        } catch (DbException e) {
                            LogUtil.e(e);
                        }
                        fillList();
                        supportInvalidateOptionsMenu();
                    }
                    break;
            }
            return true;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //添加菜单项
        menu.add(Menu.NONE, 0, 0, "删除");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(adapter.getItem(info.position).getTitle());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int currentSelectedPosition = info.position;
        //删除
        new JxdAlertDialog(this, getString(R.string.txt_tips), "确定删除？", getString(R.string.txt_confirm), getString(R.string.txt_cancel)) {
            @Override
            protected void positive() {
                try {
                    DbOperationManager.getInstance().deleteBean(adapter.getItem(currentSelectedPosition));
                    refreshData();
                } catch (DbException e) {
                    LogUtil.e(e);
                }
            }
        }.show();
        return super.onContextItemSelected(item);
    }

    @Override
    protected void refreshData() {
        try {
            switch (status) {
                case INBOX:
                    emailList = DbOperationManager.getInstance().getBeans(Selector.from(Email.class).where("fromId", "!=", SysConfig.getInstance().getUserId()));
                    break;
                case OUTBOX:
                    emailList = DbOperationManager.getInstance().getBeans(Selector.from(Email.class).where("fromId", "=", SysConfig.getInstance().getUserId()).and("localId", "=", null));
                    break;
                case DRAFT_BOX:
                    emailList = DbOperationManager.getInstance().getBeans(Selector.from(Email.class).where("fromId", "=", SysConfig.getInstance().getUserId()).and("localId", "!=", null));
                    break;
            }
            fillList();
        } catch (DbException e) {
            LogUtil.e(e);
        }
    }
}
