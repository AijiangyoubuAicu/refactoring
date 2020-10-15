package com.fandow.aijiang;

/**
 * 影片类: 这是一个简单的纯数据类
 *
 * @author 爱酱油不爱醋
 * @version 1.0.0
 * @since 2020-10-15
 */
public class Movie {
	
	/**
	 * 儿童影片的数量
	 */
	public static final int CHILDREN = 2;
	
	/**
	 * 普通影片的数量
	 */
	public static final int REGULAR = 0;
	
	/**
	 * 新影片的数量
	 */
	public static final int NEW_RELEASE = 1;
	
	/**
	 * 影片的标题
	 */
	private String title;
	
	/**
	 * 影片的编号
	 */
	private int priceCode;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getPriceCode() {
		return priceCode;
	}
	
	public void setPriceCode(int priceCode) {
		this.priceCode = priceCode;
	}
}
