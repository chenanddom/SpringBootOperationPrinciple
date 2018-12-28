# SpringBoot基础

## SpringBoot的核心功能

1.独立运行的Spring项目
Spring Boot可以以jar包的形式独立运行，运行一个Spring Boot项目之需要通过java -jar xx.jar来运行。

2.内嵌的Servlet容器
Spring Boot可选择内嵌Tomcat,jetty或者Undertow，这样我们无需以war包形式部署项目。

3.提供starter简化Maven配置
Spring提供了一系列的starter pom来简化Maven的依赖加载，例如加入了spring-boot-starter-web时，会自带加入web开发需要的其他jar包。


4.自动配置Spring
Spring Boot会根据在路径中的jar包，类的=，为jar包的类自动配置Bean，这样会极大的减少我们使用的配置。这样在大多数情况下都可以满足，但是在极少数的情况下都需要我们自己配置，此时就没有提供支持，需要我们自定义配置。


5.准生产的应用监控
Spring Boot提供了基于http,ssh,telnet对运行时的项目进行监控。


6.无代码生成和xml配置。

Spring 4.x提倡使用Java配置和注解配置组合，而Spring Boot不需要任何xml配置即可实现的所有配置.Spring4.x提供了条件注解，使得Spring Boot变的灵活。


# SpringBoot核心

## 基本配置

### 入口类和@SpringBootApplication

Spring Boot通常有一个名为*Application的入口类，入口类里面有一个main方法，这个main方法其实就是一个标准的Java应用入口方法。在main方法中使用SpringApplication.run(*Application.class,args),启动SpringBoot

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Inherited
	@SpringBootConfiguration
	@EnableAutoConfiguration
	@ComponentScan(excludeFilters = {
			@Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
			@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
	public @interface SpringBootApplication {

从上面的源码可以知道@SpringBootApplication注解是一个组合注解，它组合了@EnableAutoConfiguration，@ComponentScan;通过使用这个组合注解我们可以省去自己配置的麻烦，但是我们也可以在启动类上直接使用@SpringBootConfiguration，@EnableAutoConfiguration，@ComponentScan自己手动配置,其中@EnableAutoConfiguration會讓Spring Boot根据类路径中的jar包依赖为当前项目进行自动配置。

使用了@SpringBootApplication所在类的同级别包以及以下级包里的Bean。一般情况下奖入口类放置在groupId+arctifactId组合包名下.


### 使用xml配置
Spring Boot提倡零配置，即没有xml配置，但是在实际项目中，可能有一些特殊要求必须要使用xml配置，这时我们可以通过Spring提供的@ImportResource来加载。

@ImportResource({"classpath:demo-context.xml","classpath:another-context.xml"})


### 常规属性配置
在常规的Spring项目下，需要注入properties文件，然后再配置类里面通过@PropertiySource指明properties文件的位置，然后使用@Value注入值，但是在Spring Boot里面，只需在application.properties或者application.yml定义属性，然后使用@Value直接注入即可。

### 类型安全的配置(基于properties)
上例中使用@Value注入每个配置在实际项目中显得格外的麻烦，因为我们的配置通常会是许多个，若使用上例的方式则需要使用@Value注入多次。
SpringBoot提供了基于类型安全的配置方式，通过@ConfigurationProperties奖properties属性和一个Bean及属性进行关联，从而实现类型安全的配置。
例子：

application.yml(配置文件)

	user:
	  userName: zhangsan
	  userAge: 28


UserConfig.java(配置类)

	@Configuration
	@ConfigurationProperties(prefix = "user")
	public class UserConfig {
		private String userName;
		private String userAge;

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getUserAge() {
			return userAge;
		}

		public void setUserAge(String userAge) {
			this.userAge = userAge;
		}
	}

在控制类引入配置类:

		@Autowired
		UserConfig userConfig;

控制类调用配置类获取属性:

		@RequestMapping("/getUserInfo_2")
		public String getInfo_2(){
			return "userName:"+userConfig.getUserName()+"-"+"userAge:"+userConfig.getUserAge();
		}
请求结果:

![](https://oscimg.oschina.net/oscnet/ac697d0372996c3e6946071b66fb5737a52.jpg)


## 日志配置
Spring Boot支持Java Util Logging,Log4J,Log4J2和Logback作为日志框架，无论使用哪种日志框架，SpringBoot都已经为当前日志框架的控制台输出以及文件做好了配置。默认情况下Spring Boot使用Logbac作为日志框架。配置日志级别：

例子：




## Profile配置

Profile是Spring用来针对不同的环境对不同的配置提供支持，全局的Profile配置使用application-{profile}.yml或者application-{profile}.properties.在application中设置spring.profile.active=prod来指定获得的Profile.

![](https://oscimg.oschina.net/oscnet/6873e52df152cb1541c64161636886544b5.jpg)

如上图所示，如果在application.yml中配置了spring.profiles.active=dev，那么久使用dev的配置，配置了prod就使用prod的配置。


## Spring Boot运行原理

Spring Boot关于自动配置的源码在spring-boot-autoconfigure-x.jar内，主要的配置如下:

![](https://oscimg.oschina.net/oscnet/64ff82de8fe391a8505388c3cfede470a5a.jpg)
如果想知道Spring为我们左了那些自动配置，可以通过三种方式查看当前项目中已开启额和为开启的自动配置的报告。

1.运行jar时增加--debug
java -jar xx.jar --debug

2.application.yml配置()
debug=true

3.在STS中设置。

测试结果：

![](https://oscimg.oschina.net/oscnet/8af209a53c139f23bfc82a278fa95e16b5e.jpg)
![](https://oscimg.oschina.net/oscnet/bf6d67c908b3dafba44f18c665052a8f82c.jpg)


### 运作原理


关于Spring Boot的运行原理，这些都需要归结到@SpringBootApplication注解上来，这个注解是一个组合注解，核心功能由@EnableAutoConfiguration注解提供的。

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Inherited
	@SpringBootConfiguration
	@EnableAutoConfiguration
	@ComponentScan(excludeFilters = {
			@Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
			@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
	public @interface SpringBootApplication {


这个地方最重要的是@EnableAutoConfiguration注解，改注解内最为重要的是@Import注解，AutoConfigurationImportSelector使用了SpringFactoriesLoader.loadFactoryNames()方法来扫面具有META-INF/spring.factories文件的jar包，而spring-boot-autoconfigure-x.jar刚好由这个spring.factories文件,此文件中声明了又那些自动配置.

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Inherited
	@AutoConfigurationPackage
	@Import(AutoConfigurationImportSelector.class)
	public @interface EnableAutoConfiguration {


### 核心注解
在org.springframework.boot.autoconfigure.condition包下又如下的注解:

@ConditionalOnBean:当容器又知道的Bean的条件下
@ConditionalOnClass：当类路径下又指定的类的条件下
@ConditionalOnCloudPlatform:这与指定的云平台处于活动状态时匹配
@ConditionalOnExpression:基于SpEL表达式的条件下
@ConditionalOnJava:基于JVM版本作为判断条件.
@ConditionalOnJndi:在jndi存在的条件下查找指定的位置
@ConditionalOnMissingBean:容器里没有指定的Bean的情况下
@ConditionalOnMissingClass:当类路径没有指定的类的条件下
@ConditionalOnNotWebApplication：当前项目不在web项目的条件下
@ConditionalOnProperty：指定的属性是否又指定的值
@ConditionalOnResource：类路径下是否有指定的值。
@ConditionalOnSingleCandidate:当指定的Bean在容器中只有一个，或者虽然有多个但是指定首选的Bean.
@ConditionalOnWebApplication:当前项目在Web项目的条件下.



	private ConditionOutcome isWebApplication(ConditionContext context,
			AnnotatedTypeMetadata metadata, boolean required) {
		switch (deduceType(metadata)) {
		case SERVLET:
			return isServletWebApplication(context);
		case REACTIVE://springBoot2.0的WebFlux开发ReactiveWeb
			return isReactiveWebApplication(context);
		default:
			return isAnyWebApplication(context, required);
		}
	}

	private ConditionOutcome isServletWebApplication(ConditionContext context) {
			ConditionMessage.Builder message = ConditionMessage.forCondition("");
			//判断GenericWebApplicationContext是否在类路径中
			if (!ClassNameFilter.isPresent(SERVLET_WEB_APPLICATION_CLASS,
					context.getClassLoader())) {
				return ConditionOutcome.noMatch(
						message.didNotFind("servlet web application classes").atAll());
			}
			//容器是否有名为session的scope
			if (context.getBeanFactory() != null) {
				String[] scopes = context.getBeanFactory().getRegisteredScopeNames();
				if (ObjectUtils.containsElement(scopes, "session")) {
					return ConditionOutcome.match(message.foundExactly("'session' scope"));
				}
			}
			//判断当前容器的Enviromemt是不是ConfigurableWebEnvironment
			if (context.getEnvironment() instanceof ConfigurableWebEnvironment) {
				return ConditionOutcome
						.match(message.foundExactly("ConfigurableWebEnvironment"));
			}
			//当前的ResourceLoader是否为WebApplicationContext(ResourceLoader是ApplicationContext的顶级接口之一)
			if (context.getResourceLoader() instanceof WebApplicationContext) {
				return ConditionOutcome.match(message.foundExactly("WebApplicationContext"));
			}
			//返回一个ConditionOutcome对象
			return ConditionOutcome.noMatch(message.because("not a servlet web application"));
		}

#### 实现自动配置

HelloService.java(需要自动注入的bean)

		public class HelloService {
		private String msg;

			public String getMsg() {
				return msg;
			}

			public void setMsg(String msg) {
				this.msg = msg;
			}
		}

HelloServiceProperties.java(类型安全的配置类）

		@ConfigurationProperties(prefix = "hello")
		public class HelloServiceProperties {

		private static final String MSG="world";

		private String msg=MSG;

			public String getMsg() {
				return msg;
			}

			public void setMsg(String msg) {
				this.msg = msg;
			}
		}

HelloAutoConfiguration.java（自动配置类）

		@Configuration//设置这是一个配置类
		@EnableConfigurationProperties(HelloServiceProperties.class)//
		@ConditionalOnClass(HelloService.class)
		@ConditionalOnProperty(prefix = "hello",value = "enabled",matchIfMissing = true)
		public class HelloAutoConfiguration {
			@Autowired
			HelloServiceProperties helloServiceProperties;

			@Bean
			@ConditionalOnMissingBean(HelloService.class)
			public HelloService helloService(){
				HelloService helloService = new HelloService();
				helloService.setMsg(helloServiceProperties.getMsg());
				return helloService;
			}
		}

除了上面的步骤之外还需在resources目录下创建一个spring.factories文件，并且配置(\是换行，多个配置类使用逗号隔开)：

	org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
	com.example.demo.config.HelloAutoConfiguration


启动之后可以看到：

![](https://oscimg.oschina.net/oscnet/1559285ccc01807a67695ba2d84620237d2.jpg)

项目地址:https://github.com/chenanddom/SpringBootOperationPrinciple
