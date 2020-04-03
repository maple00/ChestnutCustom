package com.rainwood.chestnut.domain;

/**
 * @Author: a797s
 * @Date: 2020/3/2 9:49
 * @Desc: usually  EditVIew
 */
public class ComEditBean {

    private String title;           // 名称
    private String hint;            // 提示
    private String text;            // 填写的内容
    /**
     * 显示箭头的类型：
     * 0：不显示箭头
     * 1：显示箭头
     * 2：显示其他箭头
     */
    private int arrow;              // 箭头

    @Override
    public String toString() {
        return "ComEditBean{" +
                "title='" + title + '\'' +
                ", hint='" + hint + '\'' +
                ", text='" + text + '\'' +
                ", arrow=" + arrow +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getArrow() {
        return arrow;
    }

    public void setArrow(int arrow) {
        this.arrow = arrow;
    }
}
