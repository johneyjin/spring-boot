##基本配置    
###POM配置  
  
	<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.4.5.RELEASE</version>
    </parent>
  	<dependencies>
  		<dependency>
	        <groupId>org.springframework.boot</groupId>
	        <artifactId>spring-boot-starter-web</artifactId>
    	</dependency>  
	</dependencies>

入口类：@SpringBootApplication，此类是Spring Boot的核心注解，他是一个组合注解，如下：  
@Target(ElementType.TYPE)  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
@Inherited  
@SpringBootConfiguration  
@EnableAutoConfiguration  
@ComponentScan(excludeFilters = @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class))      
  


- 1、在类中添加@SpringBootApplication便可使程序按spring boot运行  
如下：  
  
	@RestController
	@SpringBootApplication
	public class Hello {
		@RequestMapping(value="/")
		public String hello(){
			return "hello word";
		}
		public static void main(String[] args) {
			SpringApplication.run(Hello.class, args);
		}
	}  
  
打开浏览器输入 http://localhost:8080/就可在页面上显示hello word  
  
- 2、如不想使用@SpringBootApplication注解，也可以在类上方使用@EnableAutoConfiguration、@Configuration、@ComponentScan，效果一样  

###类型安全的配置（基于properties）  
在src/main/resources下添加application.properties内容如下  
  
	auther.name=johney
	auther.age=20  
  
新建一个javabean类  
  
	@Component
	@ConfigurationProperties(prefix="auther")
	public class Auther {	
		private String name;	
		private int age;
		public Auther() {
			super();
			// TODO Auto-generated constructor stub
		}
		//如下省略getter及setter方法
	}  
  
spring boot运行类  
  
	@RestController
	@SpringBootApplication
	//需要制定组件扫描的位置，本次位置为org.springstudy.myfirstsptingboot.bean,即上方声明的Auther类
	@ComponentScan(basePackages={"org.springstudy.myfirstsptingboot.bean"})
	public class Hello {
		
		@Autowired
		//@Resource都可
		private Auther auther;
		@RequestMapping(value="/")
		public String hello(){
			return "this auther is "+auther.getName()+" and his age is "+auther.getAge();
		}
		public static void main(String[] args) {
			SpringApplication.run(Hello.class, args);
		}
	}  
  
##Thymeleaf  
###maven配置  
在pom.xml中加入如下依赖  
	  
	<dependency>  
            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-starter-thymeleaf</artifactId>  
    </dependency>    
  
原来关于spring-boot-starter-web等的依赖就可以去掉了，因为spring-boot-starter-thymeleaf是包含这些依赖的。而关于jsp的依赖也可以去掉了，因为我们已经完全抛弃jsp了。  
  
###编写一个spring boot启动类  
  
	@Controller
	@SpringBootApplication
	public class HelloController {
	
		private Logger logger=LoggerFactory.getLogger(HelloController.class);
		
		@RequestMapping(value="/hello",method=RequestMethod.GET)
		/**
		 * @responseBody 注解的作用是将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，
		 * 写入到response对象的body区，通常用来返回JSON数据或者是XML数据，需要注意的呢，在使用此注解之后
		 * 不会再走试图处理器，而是直接将数据写入到输入流中，他的效果等同于通过response对象输出指定格式的数据。
		 * @param model
		 * @return
		 */
		public String hello(Model model){
			model.addAttribute("name","dear");
			//返回的是在src/main/resources/templates下的文件名称（后缀默认.html）
			return "hello";
		}
		
		public static void main(String[] args) {
			SpringApplication.run(HelloController.class, args);
		}
	}
  
###使用thymeleaf写一个hello.html，放在src/main/resources/templates目录下   
  
	<!DOCTYPE HTML>
	<html xmlns:th="http://www.thymeleaf.org">
	<head>
	    <title>hello</title>
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	</head>
	<body>
		<!--/*@thymesVar id="name" type="java.lang.String"*/-->
		<!-- 访问model中的数据 -->
		<p th:text="'Hello！, ' + ${name} + '!'" >3333</p>
	</body>
	</html>  
  
运行项目，在浏览器中输入http://localhost:8080/hello，可得到Hello！, dear!  
  
注意：spring-boot项目源码目录结构的约定，现在继续介绍资源文件的约定目录结构   
Maven的资源文件目录：/src/java/resources   
spring-boot项目静态文件目录：/src/java/resources/static   
spring-boot项目模板文件目录：/src/java/resources/templates   
spring-boot静态首页的支持，即index.html放在以下目录结构会直接映射到应用的根目录下：  
  
	classpath:/META-INF/resources/index.html  
	classpath:/resources/index.html  
	classpath:/static/index.html  
	calsspath:/public/index.html      
  
在spring-boot下，默认约定了Controller试图跳转中thymeleaf模板文件的的前缀prefix是”classpath:/templates/”,后缀suffix是”.html”     
  
####thymeleaf使用  
#####1、获取变量值  
  
	<p th:text="'Hello！, ' + ${name} + '!'" >3333</p>  
可以看出获取变量值用$符号,对于javaBean的话使用变量名.属性名方式获取,这点和EL表达式一样.

另外$表达式只能写在th标签内部,不然不会生效,上面例子就是使用th:text标签的值替换p标签里面的值,至于p里面的原有的值只是为了给前端开发时做展示用的。这样的话很好的做到了前后端分离。    

#####2、引入URL  
Thymeleaf对于URL的处理是通过语法@{…}来处理的  
  
	<a th:href="@{http://blog.csdn.net/u012706811}">绝对路径</a>
	<a th:href="@{/}">相对路径</a>
	<a th:href="@{css/bootstrap.min.css}">Content路径,默认访问static下的css文件夹</a>  
  
类似的标签有:th:href和th:src  
引入外部文件  
  	
	<script th:src="@{bootstrap/js/bootstrap.min.js}" ></script>    
  
在script中获取model值 
  
	<!-- 通过 th:inline="javascript"添加script标签，这样javascript即可访问model中的属性 -->
	<script th:inline="javascript">
		/* 通过[[${..}]]格式获取实际的值  */
		var name=[[${name}]];
		console.log(name);
	</script>  
  
在html中访问model中的属性  
  
	<span th:text="${name}">username</span>  

#####3、字符串替换    
很多时候可能我们只需要对一大段文字中的某一处地方进行替换，可以通过字符串拼接操作完成：  
  
	<span th:text="'Welcome to our application, ' + ${user.name} + '!'">  

一种更简洁的方式是：  
  
	<span th:text="|Welcome to our application, ${user.name}!|">  
当然这种形式限制比较多，|…|中只能包含变量表达式${…}，不能包含其他常量、条件表达式等。  
 
#####4、运算符  
在表达式中可以使用各类算术运算符，例如+, -, *, /, %  
  
	th:with="isEven=(${prodStat.count} % 2 == 0)"  

逻辑运算符>, <, <=,>=，==,!=都可以使用，唯一需要注意的是使用<,>时需要用它的HTML转义符：  
  
	th:if="${prodStat.count} &gt; 1"
    th:text="'Execution mode is ' + ( (${execMode} == 'dev')? 'Development' : 'Production')"  
  
#####5、条件  
  
**if/unless**  
Thymeleaf中使用th:if和th:unless属性进行条件判断，下面的例子中，标签只有在th:if中条件成立时才显示：  
  
	<a th:href="@{/login}" th:unless=${session.user != null}>Login</a>  

th:unless与th:if恰好相反，只有表达式中的条件不成立，才会显示其内容。  
  
	
**Switch**  
Thymeleaf同样支持多路选择Switch结构：  

  
	<div th:switch="${user.role}">
	  <p th:case="'admin'">User is an administrator</p>
	  <p th:case="#{roles.manager}">User is a manager</p>
	</div>  

默认属性default可以用*表示：  
  
	<div th:switch="${user.role}">
	  <p th:case="'admin'">User is an administrator</p>
	  <p th:case="#{roles.manager}">User is a manager</p>
	  <p th:case="*">User is some other thing</p>
	</div>  

#####6、循环  
渲染列表数据是一种非常常见的场景，例如现在有n条记录需要渲染成一个表格，该数据集合必须是可以遍历的，使用th:each标签：  
  
	<body>
	  <h1>Product list</h1>
	
	  <table>
	    <tr>
	      <th>NAME</th>
	      <th>PRICE</th>
	      <th>IN STOCK</th>
	    </tr>
	    <tr th:each="prod : ${prods}">
	      <td th:text="${prod.name}">Onions</td>
	      <td th:text="${prod.price}">2.41</td>
	      <td th:text="${prod.inStock}? #{true} : #{false}">yes</td>
	    </tr>
	  </table>
	
	  <p>
	    <a href="../home.html" th:href="@{/}">Return to home</a>
	  </p>
	</body>  
  
可以看到，需要在被循环渲染的元素（这里是）中加入th:each标签，其中th:each=”prod : ${prods}”意味着对集合变量prods进行遍历，循环变量是prod在循环体中可以通过表达式访问。   
 
#####7、Utilities
为了模板更加易用，Thymeleaf还提供了一系列Utility对象（内置于Context中），可以通过#直接访问：

- #dates  
- #calendars  
- #numbers  
- #strings  
- arrays  
- lists  
- sets  
- maps  
… 
下面用一段代码来举例一些常用的方法：
  
date  
  
	/*
	 * Format date with the specified pattern
	 * Also works with arrays, lists or sets
	 */
	${#dates.format(date, 'dd/MMM/yyyy HH:mm')}
	${#dates.arrayFormat(datesArray, 'dd/MMM/yyyy HH:mm')}
	${#dates.listFormat(datesList, 'dd/MMM/yyyy HH:mm')}
	${#dates.setFormat(datesSet, 'dd/MMM/yyyy HH:mm')}
	
	/*
	 * Create a date (java.util.Date) object for the current date and time
	 */
	${#dates.createNow()}
	
	/*
	 * Create a date (java.util.Date) object for the current date (time set to 00:00)
	 */
	${#dates.createToday()}  
  
string  
  
	/*
	 * Check whether a String is empty (or null). Performs a trim() operation before check
	 * Also works with arrays, lists or sets
	 */
	${#strings.isEmpty(name)}
	${#strings.arrayIsEmpty(nameArr)}
	${#strings.listIsEmpty(nameList)}
	${#strings.setIsEmpty(nameSet)}
	
	/*
	 * Check whether a String starts or ends with a fragment
	 * Also works with arrays, lists or sets
	 */
	${#strings.startsWith(name,'Don')}                  // also array*, list* and set*
	${#strings.endsWith(name,endingFragment)}           // also array*, list* and set*
	
	/*
	 * Compute length
	 * Also works with arrays, lists or sets
	 */
	${#strings.length(str)}
	
	/*
	 * Null-safe comparison and concatenation
	 */
	${#strings.equals(str)}
	${#strings.equalsIgnoreCase(str)}
	${#strings.concat(str)}
	${#strings.concatReplaceNulls(str)}
	
	/*
	 * Random
	 */
	${#strings.randomAlphanumeric(count)}    
  
####spring boot集成mybatis（xml格式书写）  
#####1、pom.xml配置  
  
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.5.8.RELEASE</version>
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<version.spring>3.2.9.RELEASE</version.spring>
		<version.jackson>2.8.10</version.jackson>
		<java.version>1.8</java.version>

		<!-- Dependency versions -->
		<!-- <activemq.version>5.14.3</activemq.version> <antlr2.version>2.7.7</antlr2.version> 
			<appengine-sdk.version>1.9.48</appengine-sdk.version> <artemis.version>1.5.2</artemis.version> 
			<aspectj.version>1.8.9</aspectj.version> <assertj.version>2.6.0</assertj.version> 
			<atomikos.version>3.9.3</atomikos.version> <bitronix.version>2.1.4</bitronix.version> 
			<caffeine.version>2.3.5</caffeine.version> <cassandra-driver.version>3.1.3</cassandra-driver.version> 
			<classmate.version>1.3.3</classmate.version> <commons-beanutils.version>1.9.3</commons-beanutils.version> 
			<commons-collections.version>3.2.2</commons-collections.version> <spring-data-releasetrain.version>Ingalls-RELEASE</spring-data-releasetrain.version> -->
	</properties>


	<dependencies>
		<dependency>
			<groupId>ch.qos.logback</groupId>
			<artifactId>logback-classic</artifactId>
			<version>1.2.3</version>
			<scope>compile</scope>
		</dependency>


		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
		</dependency>
		<!-- XML/XHTML/HTML5模板引擎,已经集成了spring-boot-starter-web -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>

		<!-- spring-boot-starter-freemarker -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-freemarker</artifactId>
		</dependency>

		<!-- Starter for using JDBC with the Tomcat JDBC connection pool -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>
		<!-- 用于启动Spring启动程序的启动器，它提供生产准备功能，帮助您监视和管理应用程序。 -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
		<!-- spring boot集成mybatis包，会自动加载mybatis-3.4.5.jar包。使用此包后即可使用mybatis的xml文件，也可使用类方法注解@Select或@Insert等方式 -->
		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>1.3.1</version>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
		</dependency>

		<dependency>
			<groupId>org.apache.tomcat</groupId>
			<artifactId>tomcat-jdbc</artifactId>
		</dependency>

		<!-- json包 -->
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>fastjson</artifactId>
			<version>1.1.43</version>
		</dependency>
		<!-- 集成了MVC与Spring Web MVC框架 -->
		<dependency>
			<groupId>com.mangofactory</groupId>
			<artifactId>swagger-springmvc</artifactId>
			<version>0.9.5</version>
		</dependency>

		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-annotations</artifactId>
			<version>${version.jackson}</version>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.dataformat</groupId>
			<artifactId>jackson-dataformat-xml</artifactId>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>${version.jackson}</version>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-core</artifactId>
			<version>${version.jackson}</version>
		</dependency>


	</dependencies>



	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

	<repositories>
		<repository>
			<id>spring-milestone</id>
			<url>https://repo.spring.io/libs-release</url>
		</repository>
	</repositories>

	<pluginRepositories>
		<pluginRepository>
			<id>spring-milestone</id>
			<url>https://repo.spring.io/libs-release</url>
		</pluginRepository>
	</pluginRepositories>  
  
#####2、配置数据源application.properties  
  
	spring.datasource.url=jdbc:mysql://localhost:3306/userlog?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true
	spring.datasource.username=root
	spring.datasource.password=admin
	spring.datasource.driver-class-name=com.mysql.jdbc.Driver
	#when you update the html file ,you will not need to restart the server while you add the code
	#thymeleaf不缓存，配置后更改页面后不必重新启动服务
	spring.thymeleaf.cache=false
	#mybatis配置文件包，此路径为src/main/resources/mybatis/mybatis-config.xml
	mybatis.config-locations=classpath:/mybatis/mybatis-config.xml  
	#实体类mapper包，如：src/main/resources/mybatis/user/UserMapper.xml 
	mybatis.mapper-locations=classpath:/mybatis/*/*.xml
	#实体bean包路径
	#mybatis.type-aliases-package=cn.no7player.model  
  
#####3、mybatis-config.xml  
  
	<?xml version="1.0" encoding="UTF-8" ?>  
	<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
	<configuration>
		<!-- 设置运行参数 -->
	    <settings>
	        <!-- 全局映射器启用缓存 -->
	        <setting name="cacheEnabled" value="true" />
	        <!-- 查询时,关闭关联对象及时加载以提高性能 -->
	        <setting name="lazyLoadingEnabled" value="false" />
	        <!-- 设置关联对象加载的形态,此处为按需加载字段(加载字段由SQL指定),不会加载关联表的所有字段,以提高性能 -->
	        <setting name="aggressiveLazyLoading" value="false" />
	        <!-- 对于位置的SQL查询,允许返回不同的结果集以达到通用的效果 -->
	        <setting name="multipleResultSetsEnabled" value="true" />
	        <!-- 允许使用列标签代替列明 -->
	        <setting name="useColumnLabel" value="true" />
	        <!-- 允许使用自定义的主键值(比如由程序生成的UUID 32位编码作为键值), 数据表的pk生成策略将被覆盖 -->
	        <setting name="useGeneratedKeys" value="true" />
	        <!-- 给予被嵌套的resultMap以字段-属性的映射支持 -->
	        <setting name="autoMappingBehavior" value="PARTIAL" />
	        <!-- 对于批量更新操作缓存SQL以提高性能 -->
	        <setting name="defaultExecutorType" value="REUSE" />
	        <!-- 数据库超过25000秒仍未响应则超时 -->
	        <setting name="defaultStatementTimeout" value="25000" />
	        <!-- 打印查询语句 -->
	        <!--<setting name="logImpl" value="STDOUT_LOGGING" />-->
	        <setting name="logImpl" value="SLF4J"/>
	    </settings>
	
		<typeAliases>
			<typeAlias alias="Integer" type="java.lang.Integer" />
			<typeAlias alias="Long" type="java.lang.Long" />
			<typeAlias alias="HashMap" type="java.util.HashMap" />
			<typeAlias alias="LinkedHashMap" type="java.util.LinkedHashMap" />
			<typeAlias alias="ArrayList" type="java.util.ArrayList" />
			<typeAlias alias="LinkedList" type="java.util.LinkedList" />
		</typeAliases>
	</configuration>   
  
#####4、实体bean  
  
	public class User  implements Serializable {	
		private Integer id;
		private String name;
		private int age;
		private String phone;
		private String address;
	}  
    
#####5、mapper类  

	public interface UserMapper {
		public List<User> findUserInfo();
		@Select(" select id, name, age,phone,address from t_user where name=#{user.name} ; ")
		@Results({ @Result(id = true, property = "id", javaType = Integer.class, column = "id"),
				@Result(property = "name", javaType = String.class, column = "name"),
				@Result(property = "phone", javaType = String.class, column = "phone"),
				@Result(property = "address", javaType = String.class, column = "address"),
				@Result(property = "age", javaType = Integer.class, column = "age") })
		public List<User> findUserInfoByUser(@Param("user")User user);
	}  
  
#####6、mapper的xml文件   
  
	  <?xml version="1.0" encoding="UTF-8"?>
	<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
	<mapper namespace="cn.no7player.mapper.UserMapper">
	
		<resultMap type="cn.no7player.model.User" id="UserMap">
			<id property="id" column="id" javaType="java.lang.Integer"/>
			<result property="name" column="name" javaType="java.lang.String"/>
			<result property="age" column="age" javaType="int"/>
			<result property="phone" column="phone" javaType="java.lang.String"/>
			<result property="address" column="address" javaType="java.lang.String"/>
		</resultMap>
		
		<select id="findUserInfo" resultMap="UserMap">
			select id, age,phone,address from t_user;
		</select>

	</mapper>  
  
#####7、启动方式  
  
	@EnableAutoConfiguration
	@SpringBootApplication //开启组件扫描和自动配置
	@ComponentScan(basePackages = "cn.no7player")
	@MapperScan(basePackages={"cn.no7player.mapper"})//扫描mapper包，多个包用“,”隔开
	public class Application {
    	private static Logger logger = Logger.getLogger(Application.class);  
		public static void main(String[] args) {
        	SpringApplication.run(Application.class, args);
        	logger.info("SpringBoot Start Success");
		}
    }
  
####Spring boot全局异常捕获    
声明一个全局异常类，使用@ExceptionHandler进行拦截，	@ControllerAdvice拦截controller切面类
  
	package cn.no7player.filter;
	import java.util.HashMap;
	import java.util.Map;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import org.apache.log4j.Logger;
	import org.springframework.web.bind.annotation.ControllerAdvice;
	import org.springframework.web.bind.annotation.ExceptionHandler;
	import org.springframework.web.bind.annotation.ResponseBody;
	import cn.no7player.Application;

	/**
	 * 全局异常捕获类
	 * 
	 * @author Johney
	 * @ControllerAdvice是 controller 的一个辅助类为最常用的就是作为全局异常处理的切面类； 约定了几种可行的返回值，如果是直接返回
	 *                    model 类的话，需要使用 @ResponseBody 进行 json 转换
	 */
	@ControllerAdvice
	public class GlobalExceptionHandler {
		private static Logger logger = Logger.getLogger(GlobalExceptionHandler.class);
	
		@ExceptionHandler(RuntimeException.class)
		@ResponseBody
		public Map<String, Object> exceptionHandle(RuntimeException ex, HttpServletRequest request,
				HttpServletResponse response) {
			logger.error("抛异常了！！\n" + ex.getStackTrace()[0]+"  行报错\n错误为："+ex.getClass().toString()+":"+ex.getMessage());
			Map<String, Object> map = new HashMap<String, Object>();
			// 判断异常时属于哪个类
			if (ex.getClass().equals(ArithmeticException.class)) {
				map.put("errorCode", "500");
				// 定位到具体是哪行报错
				map.put("errorLine", ex.getStackTrace()[0]);
				map.put("errorMsg", "除数为0");
				logger.error("抛异常了！" + map);
			} else if (ex.getClass().equals(NullPointerException.class)) {
				map.put("errorCode", "500");
				// 定位到具体是哪行报错
				map.put("errorLine", ex.getStackTrace()[0]);
				map.put("errorMsg", "空指针");
				logger.error("抛异常了！" + map);
			}
			return map;
		}
	}  
    
  
####多数据源配置    
#####1、application.properties配置  
  
	#数据源1，自行定义是否为主数据源
	spring.datasource.test1.driverClassName = com.mysql.jdbc.Driver
	spring.datasource.test1.url = jdbc:mysql://localhost:3306/userlog?useUnicode=true&characterEncoding=utf-8
	spring.datasource.test1.username = root
	spring.datasource.test1.password = admin
	  
	#数据源2
	spring.datasource.test2.driverClassName = com.mysql.jdbc.Driver
	spring.datasource.test2.url = jdbc:mysql://localhost:3306/test02?useUnicode=true&characterEncoding=utf-8
	spring.datasource.test2.username = root
	spring.datasource.test2.password = admin  
  
#####2、DataSource1Config.java和DataSource2Config.java
DataSource1Config类  
    
	/**
	 * 数据源1类
	 * @author Johney
	 * @Configuration 使用此注解将此类直接注入到springboot容器中
	 * @MapperScan 扫描包，即将此包下的mapper对应操作的数据库纳入到此数据源url下
	 */
	@Configuration
	@MapperScan(basePackages = "johney.userLog.mapper", sqlSessionFactoryRef = "test1SqlSessionFactory")
	public class DataSource1Config {
	
		/**
		 * 
		 * @methodDesc: 功能描述:(配置test1数据库)
		 * @param: @return
		 * @createTime:2017年9月17日 下午3:16:44
		 * @returnType:@return DataSource
		 * @Primary 使用此注解直接将此数据源作为主数据源
		 */
		@Bean(name = "test1DataSource")
		@Primary
		@ConfigurationProperties(prefix = "spring.datasource.test1")
		public DataSource testDataSource() {
			return DataSourceBuilder.create().build();
		}
	
		/**
		 * 
		 * @methodDesc: 功能描述:(test1 sql会话工厂)
		 * @param: @param
		 *             dataSource
		 * @param: @return
		 * @param: @throws
		 *             Exception
		 * @createTime:2017年9月17日 下午3:17:08
		 * @returnType:@param dataSource
		 * @returnType:@return
		 * @returnType:@throws Exception SqlSessionFactory
		 */
		@Bean(name = "test1SqlSessionFactory")
		@Primary
		public SqlSessionFactory testSqlSessionFactory(@Qualifier("test1DataSource") DataSource dataSource)
				throws Exception {
			SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
			bean.setDataSource(dataSource);
	//		bean.setMapperLocations(
	//				new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/test1/*.xml"));
			return bean.getObject();
		}
	
		/**
		 * 
		 * @methodDesc: 功能描述:(test1 事物管理)
		 * @param: @param
		 *             dataSource
		 * @param: @return
		 * @param: @throws
		 *             Exception
		 * @createTime:2017年9月17日 下午3:17:08
		 * @returnType:@param dataSource
		 * @returnType:@return
		 * @returnType:@throws Exception SqlSessionFactory
		 */
		@Bean(name = "test1TransactionManager")
		@Primary
		public DataSourceTransactionManager testTransactionManager(@Qualifier("test1DataSource") DataSource dataSource) {
			return new DataSourceTransactionManager(dataSource);
		}
	
		@Bean(name = "test1SqlSessionTemplate")
		public SqlSessionTemplate testSqlSessionTemplate(
				@Qualifier("test1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
			return new SqlSessionTemplate(sqlSessionFactory);
		}
	
	}
  
  
DataSource2Config类  

	/**
	 * 数据源2类
	 * @author Johney
	 * @Configuration 使用此注解将此类直接注入到springboot容器中
	 * @MapperScan 扫描包，即将此包下的mapper对应操作的数据库纳入到此数据源url下
	 */
	@Configuration
	@MapperScan(basePackages = "johney.userTest02.mapper", sqlSessionFactoryRef = "test2SqlSessionFactory")
	public class DataSource2Config {
	
		/**
		 * 
		 * @methodDesc: 功能描述:(配置test2数据库)
		 * @param: @return
		 * @createTime:2017年9月17日 下午3:16:44
		 * @returnType:@return DataSource
		 */
		@Bean(name = "test2DataSource")
		@ConfigurationProperties(prefix = "spring.datasource.test2")
		public DataSource testDataSource() {
			return DataSourceBuilder.create().build();
		}
	
		/**
		 * 
		 * @methodDesc: 功能描述:(test2 sql会话工厂)
		 * @param: @param
		 *             dataSource
		 * @param: @return
		 * @param: @throws
		 *             Exception
		 * @createTime:2017年9月17日 下午3:17:08
		 * @returnType:@param dataSource
		 * @returnType:@return
		 * @returnType:@throws Exception SqlSessionFactory
		 */
		@Bean(name = "test2SqlSessionFactory")
		public SqlSessionFactory testSqlSessionFactory(@Qualifier("test2DataSource") DataSource dataSource)
				throws Exception {
			SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
			bean.setDataSource(dataSource);
	//		bean.setMapperLocations(
	//				new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/test2/*.xml"));
			return bean.getObject();
		}
	
		/**
		 * 
		 * @methodDesc: 功能描述:(test2 事物管理)
		 * @param: @param
		 *             dataSource
		 * @param: @return
		 * @param: @throws
		 *             Exception
		 * @createTime:2017年9月17日 下午3:17:08
		 * @returnType:@param dataSource
		 * @returnType:@return
		 * @returnType:@throws Exception SqlSessionFactory
		 */
		@Bean(name = "test2TransactionManager")
		public DataSourceTransactionManager testTransactionManager(@Qualifier("test2DataSource") DataSource dataSource) {
			return new DataSourceTransactionManager(dataSource);
		}
	
		@Bean(name = "test2SqlSessionTemplate")
		public SqlSessionTemplate testSqlSessionTemplate(
				@Qualifier("test2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
			return new SqlSessionTemplate(sqlSessionFactory);
		}
	
	}  
  
#####3、启动容器  
  
	@EnableAutoConfiguration
	//扫包，把DataSource1Config和DataSource2Config注入到容器中
	@ComponentScan(basePackages = "johney")
	public class Application {
	    private static Logger logger = Logger.getLogger(Application.class);  
		public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("SpringBoot Start Success");
    	}
	}    
    
###spring boot事务管理
####springboot整合事物管理  
springboot默认集成事物,只主要在方法上加上@Transactional即可  
  
####spring boot分布式事务  
使用springboot+jta+atomikos 分布式事物管理  
  
#####1、导包  
  
	<!-- 分布式事务管理包spring-boot-starter-jta-atomikos，导入此包后，操作多数据源时可保证事务一致性 -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-jta-atomikos</artifactId>
	</dependency>  
  
#####2、配置文件application.properties  
  
	# Mysql 1
	mysql.datasource.test1.url = jdbc:mysql://localhost:3306/userlog?useUnicode=true&characterEncoding=utf-8
	mysql.datasource.test1.username = root
	mysql.datasource.test1.password = admin
	
	mysql.datasource.test1.minPoolSize = 3
	mysql.datasource.test1.maxPoolSize = 25
	mysql.datasource.test1.maxLifetime = 20000
	mysql.datasource.test1.borrowConnectionTimeout = 30
	mysql.datasource.test1.loginTimeout = 30
	mysql.datasource.test1.maintenanceInterval = 60
	mysql.datasource.test1.maxIdleTime = 60
	
	
	# Mysql 2
	mysql.datasource.test2.url =jdbc:mysql://localhost:3306/test02?useUnicode=true&characterEncoding=utf-8
	mysql.datasource.test2.username =root
	mysql.datasource.test2.password =admin
	
	mysql.datasource.test2.minPoolSize = 3
	mysql.datasource.test2.maxPoolSize = 25
	mysql.datasource.test2.maxLifetime = 20000
	mysql.datasource.test2.borrowConnectionTimeout = 30
	mysql.datasource.test2.loginTimeout = 30
	mysql.datasource.test2.maintenanceInterval = 60
	mysql.datasource.test2.maxIdleTime = 60

#####3、DBConfig2.java、DBConfig2.java  
  
	package johney.config;
	import org.springframework.boot.context.properties.ConfigurationProperties;
	/**
	 * 读取配置文件前缀是mysql.datasource.test1
	 * 属性须与配置文件中相同
	 * @author Johney
	 *
	 */  
	@ConfigurationProperties(prefix = "mysql.datasource.test1")
	public class DBConfig1 {
		private String url;
		private String username;
		private String password;
		private int minPoolSize;
		private int maxPoolSize;
		private int maxLifetime;
		private int borrowConnectionTimeout;
		private int loginTimeout;
		private int maintenanceInterval;
		private int maxIdleTime;
		private String testQuery;
		//省略getter和setter方法
	}
    
  
	import org.springframework.boot.context.properties.ConfigurationProperties;
	/**
	 * 读取配置文件前缀是mysql.datasource.test1
	 * 属性须与配置文件中相同
	 * @author Johney
	 *
	 */
	@ConfigurationProperties(prefix = "mysql.datasource.test2")
	public class DBConfig2 {
		private String url;
		private String username;
		private String password;
		private int minPoolSize;
		private int maxPoolSize;
		private int maxLifetime;
		private int borrowConnectionTimeout;
		private int loginTimeout;
		private int maintenanceInterval;
		private int maxIdleTime;
		private String testQuery;
		//省略getter和setter方法
	}  
  
#####4、TestMyBatisConfig1.java、TestMyBatisConfig2.java  
  
	@Configuration
	// basePackages 最好分开配置 如果放在同一个文件夹可能会报错
	@MapperScan(basePackages = "johney.userLog", sqlSessionTemplateRef = "test1SqlSessionTemplate")
	public class TestMyBatisConfig1 {
		// 配置数据源
		@Primary
		@Bean(name = "test1DataSource")
		public DataSource testDataSource(DBConfig1 testConfig) throws SQLException {
			MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
			mysqlXaDataSource.setUrl(testConfig.getUrl());
			mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
			mysqlXaDataSource.setPassword(testConfig.getPassword());
			mysqlXaDataSource.setUser(testConfig.getUsername());
			mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
	
			AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
			xaDataSource.setXaDataSource(mysqlXaDataSource);
			xaDataSource.setUniqueResourceName("test1DataSource");
	
			xaDataSource.setMinPoolSize(testConfig.getMinPoolSize());
			xaDataSource.setMaxPoolSize(testConfig.getMaxPoolSize());
			xaDataSource.setMaxLifetime(testConfig.getMaxLifetime());
			xaDataSource.setBorrowConnectionTimeout(testConfig.getBorrowConnectionTimeout());
			xaDataSource.setLoginTimeout(testConfig.getLoginTimeout());
			xaDataSource.setMaintenanceInterval(testConfig.getMaintenanceInterval());
			xaDataSource.setMaxIdleTime(testConfig.getMaxIdleTime());
			xaDataSource.setTestQuery(testConfig.getTestQuery());
			return xaDataSource;
		}
	
		@Bean(name = "test1SqlSessionFactory")
		public SqlSessionFactory testSqlSessionFactory(@Qualifier("test1DataSource") DataSource dataSource)
				throws Exception {
			SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
			bean.setDataSource(dataSource);
			return bean.getObject();
		}
	
		@Bean(name = "test1SqlSessionTemplate")
		public SqlSessionTemplate testSqlSessionTemplate(
				@Qualifier("test1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
			return new SqlSessionTemplate(sqlSessionFactory);
		}
	
	}  
  
	@Configuration
	// basePackages 最好分开配置 如果放在同一个文件夹可能会报错
	@MapperScan(basePackages = "johney.userTest02", sqlSessionTemplateRef = "test2SqlSessionTemplate")
	public class TestMyBatisConfig2 {
		// 配置数据源
		@Bean(name = "test2DataSource")
		public DataSource testDataSource(DBConfig1 testConfig) throws SQLException {
			MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
			mysqlXaDataSource.setUrl(testConfig.getUrl());
			mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
			mysqlXaDataSource.setPassword(testConfig.getPassword());
			mysqlXaDataSource.setUser(testConfig.getUsername());
			mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
	
			AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
			xaDataSource.setXaDataSource(mysqlXaDataSource);
			xaDataSource.setUniqueResourceName("test2DataSource");
	
			xaDataSource.setMinPoolSize(testConfig.getMinPoolSize());
			xaDataSource.setMaxPoolSize(testConfig.getMaxPoolSize());
			xaDataSource.setMaxLifetime(testConfig.getMaxLifetime());
			xaDataSource.setBorrowConnectionTimeout(testConfig.getBorrowConnectionTimeout());
			xaDataSource.setLoginTimeout(testConfig.getLoginTimeout());
			xaDataSource.setMaintenanceInterval(testConfig.getMaintenanceInterval());
			xaDataSource.setMaxIdleTime(testConfig.getMaxIdleTime());
			xaDataSource.setTestQuery(testConfig.getTestQuery());
			return xaDataSource;
		}
	
		@Bean(name = "test2SqlSessionFactory")
		public SqlSessionFactory testSqlSessionFactory(@Qualifier("test2DataSource") DataSource dataSource)
				throws Exception {
			SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
			bean.setDataSource(dataSource);
			return bean.getObject();
		}
	
		@Bean(name = "test2SqlSessionTemplate")
		public SqlSessionTemplate testSqlSessionTemplate(
				@Qualifier("test2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
			return new SqlSessionTemplate(sqlSessionFactory);
		}
	
	}  
  
#####6、启动    
  
	@EnableAutoConfiguration
	@ComponentScan(basePackages = {"johney"})
	@EnableConfigurationProperties(value = { DBConfig1.class, DBConfig2.class })
	public class Application {
	    private static Logger logger = Logger.getLogger(Application.class);
	    public static void main(String[] args) {
	        SpringApplication.run(Application.class, args);
	//        logger.info("SpringBoot Start Success");
	    }
	}  
    
###aop处理统一日志请求  
####1、引包  
  
	<!-- spring-boot-starter-aop切面管理工具包 -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-aop</artifactId>
	</dependency>  
  
####2、编写切面类  
主要加入@Aspect注解 
  
	/**
	 * 切面工具类
	 * @author Johney
	 * @Aspect 定义此类为切面类
	 * @Component 交给容器管理
	 */
	@Aspect
	@Component
	public class WebLogAspect {
		private Logger logger = LoggerFactory.getLogger(WebLogAspect.class);
		
		/**
		 * 需要进行切面管理的类方法路径
		 */
		@Pointcut("execution(public * com.johney.*.controller..*.*(..))")
		public void webLog() {
			
		}
		/**
		 * 在执行webLog()方法前的操作
		 * @param joinPoint
		 * @throws Throwable
		 */
		@Before("webLog()")
		public void doBefore(JoinPoint joinPoint) throws Throwable {
			// 接收到请求，记录请求内容
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			// 记录下请求内容
			logger.info("URL : " + request.getRequestURL().toString());
			logger.info("HTTP_METHOD : " + request.getMethod());
			logger.info("IP : " + request.getRemoteAddr());
			Enumeration<String> enu = request.getParameterNames();
			while (enu.hasMoreElements()) {
				String name = (String) enu.nextElement();
				logger.info("name:{},value:{}", name, request.getParameter(name));
			}
		}
		/**
		 * 执行完方法后返回时进行操作
		 * @param ret
		 * @throws Throwable
		 */
		@AfterReturning(returning = "ret", pointcut = "webLog()")
		public void doAfterReturning(Object ret) throws Throwable {
			// 处理完请求，返回内容
			logger.info("RESPONSE : " + ret);
		}
	}    
  

###Spring boot集成redis  
####1、导redis包  
  
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-redis</artifactId>
	</dependency>   
  
####2、添加配置文件类RedisConfig  
  
	package org.johney.project.weather.config;

	import java.lang.reflect.Method;
	
	import org.springframework.cache.CacheManager;
	import org.springframework.cache.annotation.EnableCaching;
	import org.springframework.cache.interceptor.KeyGenerator;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.data.redis.cache.RedisCacheManager;
	import org.springframework.data.redis.connection.RedisConnectionFactory;
	import org.springframework.data.redis.core.RedisTemplate;
	import org.springframework.data.redis.core.StringRedisTemplate;
	import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
	
	import com.fasterxml.jackson.annotation.JsonAutoDetect;
	import com.fasterxml.jackson.annotation.PropertyAccessor;
	import com.fasterxml.jackson.databind.ObjectMapper;
	
	@Configuration
	public class RedisConfig{
	
	    @Bean
	    public KeyGenerator redisKeyGenerator(){
	        return new KeyGenerator() {
	            @Override
	            public Object generate(Object target, Method method, Object... params) {
	                StringBuilder sb = new StringBuilder();
	                sb.append(target.getClass().getName());
	                sb.append(method.getName());
	                for (Object obj : params) {
	                    sb.append(obj.toString());
	                }
	                return sb.toString();
	            }
	        };
	
	    }
	
	    @Bean
	    public CacheManager cacheManager(
	            @SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
	        return new RedisCacheManager(redisTemplate);
	    }
	
	    @Bean
	    public RedisTemplate<String, String> redisTemplate(
	            RedisConnectionFactory factory) {
	        StringRedisTemplate template = new StringRedisTemplate(factory);
	        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
	        ObjectMapper om = new ObjectMapper();
	        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
	        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
	        jackson2JsonRedisSerializer.setObjectMapper(om);
	        template.setValueSerializer(jackson2JsonRedisSerializer);
	        template.afterPropertiesSet();
	        return template;
	    }
	}
  
####3、application.yml中添加内容  
  
	spring:  
	  redis:
		#默认为0
	    database: 0
		#地址
	    host: localhost  
		#端口
	    port: 6379
		#密码
	    password: 

###Spring boot打包  
  
####1、将jar改为war  
  
	<artifactId>springboot-mybatis</artifactId>
	<version>1.0</version>
	<packaging>war</packaging>    
  
打包后的名称为artifactId+version；以上即：springboot-mybatis-1.0
  
####2、启动类  
  
	@EnableAutoConfiguration
	@SpringBootApplication //开启组件扫描和自动配置
	@ComponentScan(basePackages = "com.johney")
	@MapperScan(basePackages={"com.johney.user.mapper","com.johney.game.mapper"})
	public class Application extends SpringBootServletInitializer{
	    private static Logger logger = LoggerFactory.getLogger(Application.class);
	    
	    public static void main(String[] args) {
	        SpringApplication.run(Application.class, args);
	        logger.info("SpringBoot Start Success");
	    }
	    
	    @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	        // TODO Auto-generated method stub
	        return builder.sources(Application.class);
	    }
	}   
  
####3、tomcat依赖使用provided  
  
	<!-- 打war包时加入此项， 告诉spring-boot tomcat相关jar包用外部的，不要打进去 -->
	<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
        <scope>provided</scope>
    </dependency>  
  
####4、application.properties中加入服务器server.context-path名称  
  
	server.context-path=/springboot-mybatis-1.0  
  
注：指定上面名称后，无论本地或者部署到tomcat，都需要在ip后加上项目名称；  
如：http://localhost:8080/springboot-mybatis-1.0/+路径
  
####5、js中请求controller中的路径需加入  
  
	/**
	 * 在js中获取user.html中的路径；
	 * 在spring boot部署到tomcat后或者
	 * 在application.properties文件中指定了server.context-path=/的路径；
	 * 则必须从后台获取数据必须执行context-path
	 * @returns 返回项目路径
	 */
	function getContextPath() {
	    var pathName = document.location.pathname;
	    var index = pathName.substr(1).indexOf("/");
	    var result = pathName.substr(0,index+1);
	    return result;
	}  
  
使用方式：  
  
	$('#dg').datagrid({
		url:url+'/user/getUserInfo',
		columns:[[
			{field:'name',title:'姓名',width:100},
			{field:'age',title:'年龄',width:100},
			{field:'phone',title:'手机号',width:100,align:'right'},
			{field:'address',title:'地址',width:100,align:'right'}
		]],
		onBeforeLoad:function(param){
//			console.info(param);
		},
		onLoadError:function(data){
//			console.info(data);
		}
	});
  
####6、打包命令  
  
	clean package -Dmaven.test.skip=true      
  

  
####7、部署  
部署到tomcat中的webapp下，点击启动。  
页面访问路径为localhost:8080/(打包名称)；打包名称如果为spring-1.0.war，则路径为localhost:8080/spring-1.0  
  

  

