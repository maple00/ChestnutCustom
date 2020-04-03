package com.rainwood.chestnut.domain;

/**
 * @Author: a797s
 * @Date: 2020/3/2 10:46
 * @Desc: 语言设置
 */
public class LanguageBean {

    private String img;         // 标志
    private String name;        // 语言
    private boolean checked;    // 被选中

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
