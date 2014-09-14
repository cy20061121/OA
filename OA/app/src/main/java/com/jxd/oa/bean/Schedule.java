package com.jxd.oa.bean;

import com.google.gson.annotations.SerializedName;
import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Foreign;
import com.yftools.db.annotation.Table;

import java.util.Date;

/**
 * *****************************************
 * Description ：今日日程
 * Created by cy on 2014/8/18.
 * *****************************************
 */
@Table(name = "t_schedule")
public class Schedule extends AbstractBean {
    @SerializedName("caSid")
    private String id;
    @SerializedName("caSubject")
    private String title;//主题
    @SerializedName("caArea")
    private String address;//地点
    @SerializedName("caDesc")
    private String content;//内容
    @SerializedName("caClass")
    private int important;//重要性
    @Foreign(column = "categoryId", foreign = "id", foreignAutoCreate = true)
    @SerializedName("scheduleCategory")
    private ScheduleCategory category;//类型
    @SerializedName("caBtime")
    private Date startDate;//开始时间
    @SerializedName("caEtime")
    private Date endDate;//结束时间
    private String attachmentName;//附件列表，XX.doc|xx.xls
    private String attachmentSize;//附件大小,10923|23432
    private boolean isFinished;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public ScheduleCategory getCategory() {
        return category;
    }

    public void setCategory(ScheduleCategory category) {
        this.category = category;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isRead) {
        this.isFinished = isRead;
    }
}
