package com.fandow.aijiang.source;

import com.fandow.aijiang.Customer;
import com.fandow.aijiang.Movie;
import com.fandow.aijiang.Rental;
import org.junit.Assert;
import org.junit.Test;

/**
 * 顾客测试类
 *
 * @author 爱酱油不爱醋
 * @version 1.0.0
 * @since 2020-10-15
 */
public class CustomerTest {
	
	/**
	 * priceCode = 0
	 */
	private final static String REGULAR_STATEMENT = "记录顾客: 测试用户A\n" +
			"新视界的测试A 3.5\n" +
			"总计消费: 3.5\n" +
			"你将获得: 1 的积分";
	
	/**
	 * priceCode = 1
	 */
	private final static String NEW_RELEASE_STATEMENT = "记录顾客: 测试用户B\n" +
			"新视界的测试B 9.0\n" +
			"总计消费: 9.0\n" +
			"你将获得: 2 的积分";
	
	/**
	 * priceCode = 2
	 */
	private final static String CHILDREN_STATEMENT = "记录顾客: 测试用户C\n" +
			"新视界的测试C 1.5\n" +
			"总计消费: 1.5\n" +
			"你将获得: 1 的积分";
	
	/**
	 * 测试三个用户租用不同的影片同一天数输出的详单
	 */
	@Test
	public void statement() {
		
		Assert.assertEquals(REGULAR_STATEMENT,
				runApplication("测试用户A", "新视界的测试A",
						0, 3));
		
		Assert.assertEquals(NEW_RELEASE_STATEMENT,
				runApplication("测试用户B", "新视界的测试B",
						1, 3));
		
		Assert.assertEquals(CHILDREN_STATEMENT,
				runApplication("测试用户C", "新视界的测试C",
						2, 3));
	}
	
	/**
	 * 运行程序
	 *
	 * @param customerName 顾客名称
	 * @param movieTitle 影片名称
	 * @param priceCode 影片编号
	 * @param dayRental 租赁天数
	 * @return 详单
	 */
	private String runApplication(String customerName, String movieTitle, int priceCode, int dayRental) {
		Customer customer = new Customer(customerName);
		Movie movie = new Movie();
		movie.setTitle(movieTitle);
		movie.setPriceCode(priceCode);
		Rental rental = new Rental(movie, dayRental);
		customer.addRental(rental);
		return customer.statement();
	}
}
