package com.rainwood.chestnut.domain;

/**
 * @Author: a797s
 * @Date: 2020/3/3 13:15
 * @Desc: title + label形式
 */
public class TitleAndHintBean  {

    private String title;
    private String label;

    @Override
    public String toString() {
        return "TitleAndHintBean{" +
                "title='" + title + '\'' +
                ", label='" + label + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
