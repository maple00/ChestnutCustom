package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: shearson
 * @Time: 2020/3/1 18:24
 * @Desc: 消息
 */
public class MessageBean implements Serializable {

    private String id;         // 消息id
    private String text;        // 消息内容
    private String state;       // 消息状态、1：已读 0：未读
    private String time;        // 时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
