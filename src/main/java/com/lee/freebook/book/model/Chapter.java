package com.lee.freebook.book.model;

import lombok.Data;

import java.io.Serializable;
@Data
public class Chapter implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String title;
	private String url;
	private Integer novelId;
	private Integer volumeId;

	
}
