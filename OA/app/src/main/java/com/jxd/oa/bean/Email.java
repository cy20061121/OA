package com.jxd.oa.bean;

import com.google.gson.annotations.SerializedName;
import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Finder;
import com.yftools.db.annotation.ForeignCollection;
import com.yftools.db.annotation.Table;
import com.yftools.db.sqlite.ForeignCollectionLazyLoader;

import java.util.Date;

/**
 * *****************************************
 * Description ：电子邮件
 * Created by cy on 2014/8/6.
 * *****************************************
 */
@Table(name = "t_email")
public class Email extends AbstractBean {

    private String id;
    @SerializedName("subject")
    private String title;//标题
    private String content;//内容
    private int important;//重要性
    private String fromId;//发送人
    @Finder(valueColumn = "fromId", targetColumn = "id")
    private User fromUser;//因为后台没有用对象。所以这里就直接查一下
    @SerializedName("toId2")
    private String toIds;//接收人
    private Date sendTime;
    private String attachmentName;//邮件列表，XX.doc|xx.xls
    private String attachmentSize;//邮件大小,10923|23432
    @ForeignCollection(valueColumn = "id", foreign = "emailId", foreignAutoCreate = true)
    private ForeignCollectionLazyLoader<EmailRecipient> emailRecipientList;//该对像不能序列化,transient
    private String localId;//本地id,用于存放草稿用


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToIds() {
        return toIds;
    }

    public void setToIds(String toIds) {
        this.toIds = toIds;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentSize() {
        return attachmentSize;
    }

    public void setAttachmentSize(String attachmentSize) {
        this.attachmentSize = attachmentSize;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public ForeignCollectionLazyLoader<EmailRecipient> getEmailRecipientList() {
        return emailRecipientList;
    }

    public void setEmailRecipientList(ForeignCollectionLazyLoader<EmailRecipient> emailRecipientList) {
        this.emailRecipientList = emailRecipientList;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }
}
