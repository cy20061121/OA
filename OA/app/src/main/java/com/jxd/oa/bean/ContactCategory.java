package com.jxd.oa.bean;

import com.google.gson.annotations.SerializedName;
import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Column;
import com.yftools.db.annotation.Id;
import com.yftools.db.annotation.Table;

/**
 * *****************************************
 * Description ：通讯录分类
 * Created by cy on 2014/8/8.
 * *****************************************
 */
@Table(name = "t_contact_category")
public class ContactCategory extends AbstractBean {
    @Id(column = "id")
    @SerializedName("groupId")
    private String id;
    @Column(column = "name")
    @SerializedName("groupName")
    private String name;
    @Column(column = "userId")
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
