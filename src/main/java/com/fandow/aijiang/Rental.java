package com.fandow.aijiang;

/**
 * 租赁类
 *
 * @author 爱酱油不爱醋
 * @version 1.0.0
 * @since 2020-10-15
 */
public class Rental {
	
	/**
	 * 租赁的影片
	 */
	private Movie movie;
	
	/**
	 * 租赁天数
	 */
	private int dayRented;
	
	public Rental(Movie movie, int dayRented) {
		this.movie = movie;
		this.dayRented = dayRented;
	}
	
	public Movie getMovie() {
		return movie;
	}
	
	public int getDayRented() {
		return dayRented;
	}
}
