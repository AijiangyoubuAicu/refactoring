package com.fandow.aijiang;

import java.util.Enumeration;
import java.util.Vector;

/**
 * 顾客类
 *
 * @author 爱酱油不爱醋
 * @version 1.0.0
 * @since 2020-10-15
 */
public class Customer {
	
	private String name;
	
	private Vector rentals = new Vector();
	
	public Customer(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * 增加顾客
	 *
	 * @param arg 顾客的数据
	 */
	public void addRental(Rental arg) {
		rentals.addElement(arg);
	}
	
	/**
	 * 租赁影片
	 *
	 * @return 账单结果
	 */
	public String statement() {
		double totalAmount = 0;
		int frequentRenterPoints = 0;
		Enumeration elements = rentals.elements();
		String result = "记录顾客: " + getName() + "\n";
		while (elements.hasMoreElements()) {
			double thisAmount = 0;
			Rental each = (Rental) elements.nextElement();
			
			thisAmount = amountFor(thisAmount, each);
			
			frequentRenterPoints ++;
			
			if (each.getMovie().getPriceCode() == Movie.NEW_RELEASE
					&& each.getDayRented() > 1) {
				frequentRenterPoints++;
			}
			
			result += each.getMovie().getTitle() + " " + String.valueOf(thisAmount) + "\n";
			
			totalAmount += thisAmount;
		}
		
		result += "总计消费: " + String.valueOf(totalAmount) + "\n";
		result += "你将获得: " + String.valueOf(frequentRenterPoints) + " 的积分";
		return result;
	}
	
	private double amountFor(double thisAmount, Rental each) {
		// 根据影片的类型编号决定影片的价格计算规则
		switch (each.getMovie().getPriceCode()) {
			case Movie.REGULAR:
				thisAmount += 2;
				if (each.getDayRented() > 2) {
					thisAmount += (each.getDayRented() - 2) * 1.5;
				}
				break;
			case Movie.NEW_RELEASE:
				thisAmount += each.getDayRented() * 3;
				break;
			case Movie.CHILDREN:
				thisAmount += 1.5;
				if (each.getDayRented() > 3) {
					thisAmount += (each.getDayRented() - 3) * 1.5;
					break;
				}
		}
		return thisAmount;
	}
	
}
