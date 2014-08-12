package com.jxd.oa.constants;

import com.jxd.common.vo.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作常量
 */
public enum Const {
    // 表名跟实体类
    IMAGE_QUALITY_LOW("低", 480), IMAGE_QUALITY_MIDDLE("中", 800), IMAGE_QUALITY_HIGH("高", 1024),
    TYPE_IMPORTANT_LOW("一般", 0), TYPE_IMPORTANT_MIDDLE("重要", 1), TYPE_IMPORTANT_HIGH("非常重要", 2);
    // 成员变量
    private String name;
    private Object value;

    // 构造方法
    private Const(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    // 普通方法
    public static String getName(Object value) {
        for (Const c : Const.values()) {
            if (c.getValue().equals(value)) {
                return c.name;
            }
        }
        return null;
    }

    // 普通方法
    public static String getName(String prefix, Object value) {
        for (Const c : Const.values()) {
            if (c.name().startsWith(prefix) && c.getValue().equals(value)) {
                return c.name;
            }
        }
        return null;
    }

    public static List<String> getNameList(String prefix) {
        List<String> list = new ArrayList<String>();
        for (Const c : Const.values()) {
            if (c.name().startsWith(prefix)) {
                list.add(c.getName());
            }
        }
        return list;
    }

    public static List<Object> getValueList(String prefix) {
        List<Object> list = new ArrayList<Object>();
        for (Const c : Const.values()) {
            if (c.name().startsWith(prefix)) {
                list.add(c.getValue());
            }
        }
        return list;
    }

    /**
     * 按照前缀取得键值对
     *
     * @param prefix
     * @return
     */
    public static List<Item> getList(String prefix) {
        List<Item> items = new ArrayList<Item>();
        for (Const c : Const.values()) {
            // System.out.println("c.name()="+c.name());
            if (c.name().startsWith(prefix)) {
                // hm.put(c.getValue(), c.getName());
                items.add(new Item(c.getName(), (Integer) c.getValue()));
            }
        }
        return items;
    }

    /**
     * 按照前缀取得键值对
     *
     * @param prefix
     * @return
     */
    public static Map<Object, String> getMap(String prefix) {
        HashMap<Object, String> hm = new HashMap<Object, String>();
        for (Const c : Const.values()) {
            // System.out.println("c.name()="+c.name());
            if (c.name().startsWith(prefix)) {
                hm.put(c.getValue(), c.getName());
            }
        }
        return hm;
    }

    // get set_pressed 方法

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 覆盖方法
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.name + "_" + this.value;
    }
}
