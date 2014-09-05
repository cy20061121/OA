package com.jxd.oa.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.jxd.common.vo.Item;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.Contact;
import com.jxd.oa.bean.ContactCategory;
import com.jxd.oa.constants.Const;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.GsonUtil;
import com.jxd.oa.utils.ParamManager;
import com.jxd.oa.view.SelectEditView;
import com.jxd.oa.view.TypeView;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.exception.DbException;
import com.yftools.exception.HttpException;
import com.yftools.http.RequestParams;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.json.Json;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************
 * Description ：个人通讯录新增、修改、删除
 * Created by cywf on 2014/8/9.
 * *****************************************
 */
public class ContactAddActivity extends AbstractActivity {

    @ViewInject(R.id.name_et)
    private EditText name_et;
    @ViewInject(R.id.category_tv)
    private TypeView category_tv;
    @ViewInject(R.id.sex_sev)
    private SelectEditView sex_sev;
    @ViewInject(R.id.mobile_et)
    private EditText mobile_et;
    @ViewInject(R.id.homeTle_et)
    private EditText homeTle_et;
    @ViewInject(R.id.companyName_et)
    private EditText companyName_et;
    @ViewInject(R.id.ministration_et)
    private EditText ministration_et;
    @ViewInject(R.id.companyAddr_et)
    private EditText companyAddr_et;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle("增加联系人");
        contact = (Contact) getIntent().getSerializableExtra("contact");
        initData();
    }

    private void initData() {
        //初始化类型选择
        try {
            List<ContactCategory> contactCategoryList = DbOperationManager.getInstance().getBeans(Selector.from(ContactCategory.class).where("userId", "=", SysConfig.getInstance().getUserId()));
            List<Item> itemList = new ArrayList<Item>();
            for (ContactCategory contactCategory : contactCategoryList) {
                itemList.add(new Item(contactCategory.getGroupName(), contactCategory.getId()));
            }
            category_tv.initData(itemList);
        } catch (DbException e) {
            LogUtil.e(e);
        }
        if (contact != null) {
            name_et.setText(contact.getName());
            if (contact.getCategory() != null) {
                category_tv.setValue(contact.getCategory().getGroupName(), contact.getCategory().getId());
            }
            sex_sev.setContent(contact.getSex());
            mobile_et.setText(contact.getMobile());
            homeTle_et.setText(contact.getHomeTel());
            companyName_et.setText(contact.getCompanyName());
            companyAddr_et.setText(contact.getCompanyAddr());
            ministration_et.setText(contact.getMinistration());
        }

    }

    @OnClick(R.id.sex_sev)
    public void importantClick(View view) {
        final List<String> nameList = Const.getNameList("SEX_");
        Dialog alertDialog = new AlertDialog.Builder(this).setTitle("请选择性别")
                .setItems(nameList.toArray(new String[nameList.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sex_sev.setContent(nameList.get(which), Const.getValueList("SEX_").get(which) + "");
                    }
                }).create();
        alertDialog.show();
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
                if (validate() && setData()) {
                    contactSubmit();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void contactSubmit() {
        RequestParams params = ParamManager.setDefaultParams();
        params.addBodyParameter("data", GsonUtil.getInstance().getGson().toJson(contact));
        HttpUtil.getInstance().sendInDialog(mContext, getString(R.string.txt_is_upload_data), ParamManager.parseBaseUrl("contactSave.action"), params, new RequestCallBack<Json>() {
            @Override
            public void onSuccess(ResponseInfo<Json> responseInfo) {
                try {
                    DbOperationManager.getInstance().save(GsonUtil.getInstance().getGson().fromJson(responseInfo.result.toString(), Contact.class));
                    finish();
                    sendBroadcast(new Intent(Constant.ACTION_REFRESH));
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

    private boolean setData() {
        if (contact == null) {
            contact = new Contact();
        }
        contact.setName(name_et.getText().toString());
        ContactCategory contactCategory = new ContactCategory();
        contactCategory.setGroupName(category_tv.getContent());
        if (category_tv.getValue() != null) {
            contactCategory.setId(category_tv.getValue() + "");
        }
        contact.setUserId(SysConfig.getInstance().getUserId());
        contact.setCategory(contactCategory);
        contact.setCompanyName(companyName_et.getText().toString());
        contact.setCompanyAddr(companyAddr_et.getText().toString());
        contact.setMobile(mobile_et.getText().toString());
        contact.setHomeTel(homeTle_et.getText().toString());
        contact.setMinistration(ministration_et.getText().toString());
        contact.setSex(sex_sev.getValue() + "");
        return true;
    }

    private boolean validate() {
        if (TextUtils.isEmpty(name_et.getText())) {
            displayToast("请输入姓名");
            return false;
        }
        if (TextUtils.isEmpty(category_tv.getContent())) {
            displayToast("请选择分组");
            return false;
        }
        if (sex_sev.getValue() == null) {
            displayToast("请选择性别");
            return false;
        }
        if (TextUtils.isEmpty(mobile_et.getText()) && TextUtils.isEmpty(homeTle_et.getText())) {
            displayToast("请至少输入一个联系方式");
            return false;
        }
        return true;
    }
}