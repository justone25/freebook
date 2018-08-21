package com.lee.freebook.book.model;

import lombok.Data;

import java.io.Serializable;
@Data
public class ChapterDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	private String title;
	private String content;
	private String prve;
	private String next;
	private Integer chapterId;

}
