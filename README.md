# 《使用 IDEA 的重构功能 + 常用的重构手法来改善既有代码》

# 内容总览
## IDEA重构功能总览
- Ctrl+Alt+Shift+T 在当前文件下开启重构面板
- Shift+F6         重命名
- Ctrl+F6   更改签名 
- Alt+F6    编辑属性值
- Ctrl+Shift+F6 类型迁移
- 转换为实例方法
- 提取与引入
    - Ctrl+Alt+V    引入变量    
    - Ctrl+Alt+C    引入常量
    - Ctrl+Alt+F    引入字段
    - Ctrl+Alt+P    引入参数
    - Ctrl+Alt+Shift+P  引入 Functional 参数
    - 引入函数变量
    - 引入参数对象
    - Ctrl+Alt+M    提取方法
    - 用方法对象替换方法
    - 提取代理
    - 提取接口
    - 提取超类
    - 子查询作为 CTE
- 内联
- 查找和替换代码副本
- 反转布尔值
- 向父类移动成员
- 向子类移动成员
- 尽可能使用接口
- 用委托代替继承
- 移除中间者
- 包装方法的返回值
- 封装字段
- 将临时替换为查询
- 用工厂方法替换构造函数
- 用构建器替换构造函数
- 泛型化
- 迁移

## 要点列表
- 如果你发现自己需要为程序添加一个特性，而代码结构使你无法很方便地达成目标，那就先重构那个程序，使特性的添加变得比较容易地进行，然后再添加特性
- 重构前，先检查自己是否有一套可靠的测试机制。这些测试必须有自我检验的能力
- 重构技术就是以微小步伐修改程序。如果你犯下错误，很容易便可发现它
- 任何一个傻瓜都能写出计算机可以理解的代码。唯有写出人类容易理解的代码，才是优秀的程序员
- 重构（名词）：对软件内部结构的一种调整，目的是在不改变软件可观察行为的前提下，提高可理解性，降低其修改成本
- 重构（动词）：使用一系列重构手法，在不改变软件可观察行为的前提下，提供其可理解性，降低其修改成本
- 事不过三，三则重构
- 不要过早发布接口。请修改你的代码所有权政策，使重构更顺畅
- 当你感觉需要撰写注释时，请先尝试重构，试着让所有注释都变得多余
- 确保所有测试都完全自动化，让它们检查自己的测试结果
- 一套测试就是一个强大的bug侦测器，能够大大缩减查找bug所需要的时间
- 频繁地运行测试。每次编译请把测试也考虑进去--每天至少执行每个测试一次
- 每当你收到bug报告，请先写一个单元测试暴漏这个bug
- 编写未臻完善的测试并实际运行，好过对完美测试的无尽等待
- 考虑可能出错的边界条件，把测试火力集中在哪儿
- 当事情被大家认为应该会出错时，别忘了检查是否抛出了预期的以长
- 不要因为测试无法捕捉所有bug就不写测试，因为测试的确可以捕捉到大多数bug


# 重构示例

这是一个影片出租店用的程序，负责计算每一位顾客的消费金额兵打印详单。

操作者告诉程序：顾客租了那些影片、租期多久，程序根据租赁时间和影片类型计算出费用。
影片分为三类：普通片、儿童片和新片。除了计算费用，还要为常客计算积分。
积分会根据租片种类是否为新片而有不同。

**Movie（影片类）**：是一个简单的纯数据类
```java
public class Movie {
	
	public static final int CHILDREN = 2;
	
	public static final int REGULAR = 0;
	
	public static final int NEW_RELEASE = 1;
	
	private String title;
	
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
```

**Rental（租赁类）**：表示某个顾客租了一部影片

```java
public class Rental {
	
	private Movie movie;
	
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
```

**Customer（顾客类）**：表示顾客，就像其他类一样，它也拥有数据和相应的访问函数
```java
public class Customer {
	
	private String name;
	
	private Vector rentals = new Vector();
	
	public Customer(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addRental(Rental arg) {
		rentals.addElement(arg);
	}
	
	public String statement() {
		double totalAmount = 0;
		int frequentRenterPoints = 0;
		Enumeration elements = rentals.elements();
		String result = "记录顾客: " + getName() + "\n";
		while (elements.hasMoreElements()) {
			double thisAmount = 0;
			Rental each = (Rental) elements.nextElement();
			
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
			
			frequentRenterPoints ++;
			
			if (each.getMovie().getPriceCode() == Movie.NEW_RELEASE
					&& each.getDayRented() > 1) {
				frequentRenterPoints++;
			}
			
			result += "\t" + each.getMovie().getTitle() + "\t"
					+ String.valueOf(thisAmount) + "\n";
			
			totalAmount += thisAmount;
		}
		
		result += "总计消费: " + String.valueOf(totalAmount) + "\n";
		result += "你将获得: " + String.valueOf(frequentRenterPoints) + " 的积分";
		return result;
	}
}

```

Application: 简单的应用
```java
public class Application {
	
	public static void main(String[] args) {
		Customer customer = new Customer("一名测试顾客");
		Movie movie = new Movie();
		movie.setTitle("新视界");
		movie.setPriceCode(0);
		Rental rental = new Rental(movie, 3);
		customer.addRental(rental);
		
		System.out.println(customer.statement());
	}
}
```

打印出的结果：
```text
记录顾客: 一名测试顾客
	新视界	3.5
总计消费: 3.5
你将获得: 1 的积分
```

## 重构的第一步：构筑测试体系
重构的第一个步骤永远使：**建立一组可靠的测试环境！**这是永远且最最最最最最重要的一步！

遵循重构手法可以避免绝大多数引入bug的情形，但写代码是人，那就有可能会犯错！

进行重构的时候，必须依赖测试，让它告诉我们是否引入了bug。好的测试是重构的根本！花时间建立一个优良的测试机制非常值得，
因为当你修改程序时，好测试会给你必要的安全保障。测试机制在重构领域非常重要！


针对上述的代码，我编写了一个单元测试
```java
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
```

> 当然，这只是个示例测试，这个功能的测试远远没有那么简单，望穷尽所期望的测试输出结果、可能会出现的意外测试结果和已经出现的错误结果

- 运行测试应当像执行编译一样地简单，每次在编译运行前都应该去运行测试
- 不要等待每次迭代结尾才增加测试，只要写好一点功能就应立即添加它们，这样就可以花很少的时间追查回归错误
- 如果测试需要手动运行，的确很让人烦闷，应当让测试尽可能的自动化（包括使用工具，如 Java 的 Juit 测试框架）
- 在撰写测试代码的最好时机是在开始动手编码之前，当需要添加特性时，先优先编写相应的测试代码；这样其实是在问自己：“为了添加这个功能，我需要实现些什么？”预先写好的测试能为我的工作按上一个明确的结束标志：“一旦测试代码正常运行，宣告工作结束！”

> 测试驱动开发（Test-Driven-Development，TDD）：先编写一个（失败的）测试，编写代码使其测试通过，然后进行重构以保证代码整洁。这样“测试、编码、重构”的循环应该在每个小时内完成很多次。


### 单元测试与功能测试
单元测试（Unit Test）：高度局部化的测试，每个测试都隶属单一包，它能够测试其他包的接口，除此之外它将假设其他包一切正常

> 编写单元测试的目的是为了提高程序员的生产率

功能测试（Functional Test）：用来保证软件能够正常运作，从客户的角度来保障质量，应该由一个喜欢寻找bug的独立团队来开发（说的就是测试部门啦！）

> 一旦功能测试者或最终用户找到了软件的bug，要除掉它至少要做两件事：一是你当然必须要修改代码，才能排除错误。二你应该添加一个单元测试，用来暴露这个bug。

### 探测边界条件
扮演“程序公敌”的角色，积极思考如何破坏代码才能添加更多的测试

请思考：
```text
对于一个付款的业务领域来讲，提供一个负值的需求值并算出一个利润意义何在？最小的需求量不应该是0吗？
```
或许设值的方法对负值有些不同的行为，比如抛出错误，或总是将值设置为0等等。当编写这些测试能够帮助你思考代码本应该如何应对边界场景

考虑可能出错的边界条件，把测试火力集中在哪儿。“寻找边界条件”也包括特殊的、可能导致测试失败的情况

> 考虑可能出错的边界条件，把测试火力集中在哪儿


### 添加更多的测试

观察类该做的事情，然后针对任何一项功能的任何这一种可能失败的情况，进行测试！

测试应该是一种**风险驱动**的行为，测试的目的是希望找出或未来可能出现的错误

测试你最担心的部分，这样你就能从测试工作当中得到最大利益

请改变思维：“任何测试都不能证明一个程序没有bug”。确实如此，但不并影响“测试可以提高编程速度”

> 当测试数量达到一定程度之后，继续添加测试带来的效益呈现递减趋势，而非持续递增。
> 你应该把测试集中在可能出错的地方，观察代码，看哪儿变复杂；观察函数，思考那些地方可能会出现错误。

## 重构的第二步：嗅出臭代码
### 神秘命名

### 重复代码

### 过长函数

### 过长参数列表

### 全局数据

### 可变数据

### 发散式变化

### 散弹式修改

### 依恋情结

### 数据泥团

### 基本类型偏执

### 重复的 switch

### 循环语句

### 沉赘的元素

### 夸夸其谈通用性

### 临时字段

### 过长的消息链

### 中间人

### 内幕交易

### 过大的类

### 异曲同工的类

### 纯数据类

### 被拒绝的遗赠

### 注释

## 重构的第三步：选择合适地重构手法进行重构

### 提取函数

### 搬移函数

### 以查询取代临时变量

### 塑造模板函数

### 以 State/Strategy 取代类型码

### 自封装字段

### 以多态取代条件表达式

# 重构手法
## 基础重构
### 提炼函数
// TODO

### 内联函数
// TODO

### 提炼变量
// TODO

### 内联变量
// TODO

### 改变函数声明
// TODO

### 封装变量
// TODO

### 引入参数对象
// TODO

### 函数组合成类

### 函数组合成变换

### 拆分阶段

## 封装
### 封装记录

### 封装集合

### 以对象取代基本类型
// TODO

### 以查询取代临时变量
// TODO

### 提炼类
// TODO

### 内联类

### 隐藏委托关系
// TODO

### 移除中间人
// TODO

### 替换算法

## 搬移特性
### 搬移函数

### 搬移字段

### 搬移语句到函数

### 搬移语句到调用者

### 以函数调用取代内联代码

### 移动语句

### 拆分循环

### 以管道取代循环

### 移除死代码
// TODO

## 重新组织数据
### 拆分变量

### 字段改名
// TODO

### 以查询取代派生变量

### 将引用对象改为值对象

### 将值对象改为引用对象

## 简化条件逻辑
### 分解条件表达式

### 合并条件表达式

### 以卫语句取代嵌套条件表达式

### 以多态取代条件表达式

### 引入特例

### 引入断言
// TODO

## 重构 API
### 将查询函数和修改函数分离

### 函数参数化

### 移除标记参数

### 保持对象完整

### 以查询取代参数

### 以参数取代查询

### 移除设置函数

### 以工厂函数取代构造函数

### 以命令取代函数

### 以函数取代命令

## 处理继承关系

### 函数上移
// TODO

### 字段上移

### 构造函数本体上移

### 函数下移
// TODO

### 提炼超类
// TODO

### 折叠继承体系

### 以委托取代子类

### 以委托取代超类


