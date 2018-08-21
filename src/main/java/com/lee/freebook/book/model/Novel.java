package com.lee.freebook.book.model;

import lombok.Data;

@Data
public class Novel {

    private Integer id;

    private String name;

    private String author;

    private String url;

    private String type;

    private String lastUpdateChapter;

    private String lastUpdateChapterUrl;

    private String lastUpdateTime;

    private Integer status;

    private String addTime;
    //是否分卷
    private Integer isVolume;
}