# SpringMVC

[TOC]

## 1、SpringMVC

### 1.1、什么是MVC

MVC：

- 模型（Model）-- dao，service：处理数据
  - 业务逻辑
  - 保存数据状态
- 视图（View）-- jsp，html：展示数据
  - 显示视图
- 控制器（Controller）-- Servlet：获取请求和返回响应
  - 取得表单数据
  - 调用业务逻辑
  - 转向指定的页面

 最典型的MVC就是JSP + Servlet + javabean的模式

![image-20210204102035815](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210204102035815.png)

![MVC框架](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/MVC%E6%A1%86%E6%9E%B6.png)

早期开发中，**JSP：本质就是一个Servlet**，但是JSP职责不单一（包含页面+java代码）不便于维护

### 1.2、Servlet，Tomcat

Servlet配置：

Servlet **web.xml**模板

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                                http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
         
</web-app>
```

目录结构

![image-20210205100707626](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210205100707626.png)

PS：在WEB-INF目录下的jsp，是无法被直接访问到的，对用户是隐藏的，需要通过**转发**或者**重定向**进行访问

PS：推测BUG，在gradle目录下，**web需要和main在同级目录下**，不然无法访问jsp，servlet配置无效，无法读取class

- 1. 重写HttpServlet的方法

  ```java
  public class HelloServlet extends HttpServlet {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp)
              throws ServletException, IOException {
          // 1.获取前端参数
          String method = req.getParameter("method");
          if (method.equals("add")) {
              req.getSession().setAttribute("msg","执行了add方法");
          }
          if (method.equals("delete")) {
              req.getSession().setAttribute("msg","执行了delete方法");
          }
          // 2.调用业务层（这里没写业务）
          // 3.视图转发或者重定向
          req.getRequestDispatcher("/WEB-INF/jsp/hello.jsp").forward(req,resp);
      }
      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
              throws ServletException, IOException {
          doGet(req, resp);
      }
  }
  ```

- 2. 在web.xml中配置servlet和servlet mapping

  ```xml
  <!--hello servlet 01-->
  <servlet>
      <servlet-name>hello</servlet-name>
      <servlet-class>com.neusoft.servlet.HelloServlet</servlet-class>
  </servlet>
  <servlet-mapping>
      <servlet-name>hello</servlet-name>
      <url-pattern>/hello</url-pattern>
  </servlet-mapping>
  ```

- 3. 编写转发的页面，接收数据的页面

  ```jsp
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <html>
      <head>
          <title>hello</title>
      </head>
      <body>
      	${msg}
      </body>
  </html>
  ```

- 4. 编写表单，action="/hello"

  ```jsp
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <html>
      <head>
          <title>form</title>
      </head>
      <body>
          <form action="${pageContext.request.contextPath}/hello" method="post">
              <label>
                  <input type="text" name="method">
              </label>
              <input type="submit">
          </form>
      </body>
  </html>
  ```

- 配置Tomcat

  ![image-20210205102637848](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210205102637848.png)

  ![image-20210205102746327](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210205102746327.png)

  ![image-20210205102859579](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210205102859579.png)

- 启动Tomcat，发送参数

  ![image-20210205103131075](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210205103131075.png)

  ![image-20210205103435844](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210205103435844.png)

### 1.3、SpringMVC 执行流程

==**SpringMVC是Spring Framework的一部分，是基于Java实现MVC（底层Servlet）的轻量级Web框架**==

- 特点：
  - 轻量级，简单易学
  - 高效，基于请求响应的MVC框架
  - 与Spring兼容性好，无缝结合
  - 约定大于配置（Maven特点也是这个：本身已经约定的条件大于自己配置的条件，**按照规定使用**）
  - 功能强大：RESTful风格(url传参数风格)、数据验证、格式化、主题.......
  - 简单灵活

旧版官方文档：

https://docs.spring.io/spring-framework/docs/4.3.24.RELEASE/spring-framework-reference/html/mvc.html

https://docs.spring.io/spring-framework/docs/4.3.24.RELEASE/spring-framework-reference/html/index.html

- DispatcherServlet

![mvc context hierarchy](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/mvc-context-hierarchy.png)

DispatcherServlet的作用是将请求分发到不同的处理器（作为一个中转站，统一处理请求）

==**执行流程**==

**Spring的web框架围绕DispartcherServlet设计，其本质还是Servlet**，可以将请求分发到不同的处理器

![image-20210208133528274](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210208133528274.png)

1. 发送请求：在web中发送hello请求【地址栏写入/hello并按下回车进入】的一瞬间
2. 前端控制器：在这里指代为DispatcherServlet
3. 发送请求过后，进入代码的web.xml，执行servlet，找到servlet-mapping，对hello请求进行匹配
4. 委托请求给处理器：处理器理解为代码中的Controller类
5. 调用业务的对象和返回模型数据，在该例中没写
6. 返回ModelAndView：模型和视图，模型装数据，视图显示。
7. 通过Model跳转到视图，通过发送过来的model来找对应的视图，再将视图返回给DispatcherServlet

**DispatcherServlet表示前端控制器，是整个SpringMVC的控制中心，用户在前端发出的请求，DispatcherServlet都会接收并拦截请求。**

------------------------------------------------------

- ### ==Tomcat-Local-URL==

==**http://localhost:8080/mvc/hello**==

==**请求位于服务器localhost:8080上的mvc站点（项目名![image-20210209102418576](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210209102418576.png)）的hello控制器**==

------------------------------------------------------------------------------------------------

### 1.4、HelloSpringMVC 

配置SpringMVC流程

- 1. 为maven子项目添加web框架支持

  ![image-20210208114344566](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210208114344566.png)

  ![image-20210208114409749](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210208114409749.png)

- 2. 配置web.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
           http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
           version="4.0">
      <!--1.注册DispatcherServlet-->
      <servlet>
          <servlet-name>springmvc</servlet-name>
          <servlet-class>
              org.springframework.web.servlet.DispatcherServlet
          </servlet-class>
          <!--关联一个springmvc的配置文件springmvc-servlet.xml-->
          <init-param>
              <param-name>contextConfigLocation</param-name>
              <param-value>classpath:springmvc-servlet.xml</param-value>
          </init-param>
          <!--启动级别 1-->
          <load-on-startup>1</load-on-startup>
      </servlet>
      <!--/  匹配所有请求：（不包括jsp）-->
      <!--/* 匹配所有请求：（包括jsp）-->
      <servlet-mapping>
          <servlet-name>springmvc</servlet-name>
          <url-pattern>/</url-pattern>
      </servlet-mapping>
  </web-app>
  ```

- 3.step 2中需要Spring-Servlet配置文件，在main-resource下编写【springmvc-servlet.xml】

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://www.springframework.org/schema/beans
          https://www.springframework.org/schema/beans/spring-beans.xsd">
      <!--添加处理映射器，由spring完成-->
      <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
      <!--添加处理器适配器，由spring完成-->
      <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>
      <!--添加视图解析器，由spring完成-->
      <!--视图解析器：DispatcherServlet 给它的 ModelAndView-->
      <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
            id="internalResourceViewResolver">
          <!--前缀-->
          <property name="prefix" value="/WEB-INF/jsp/"/>
          <!--后缀-->
          <property name="suffix" value=".jsp"/>
      </bean>
      <!--将自己的类交给SpringIOC容器，注册Bean-->
      <bean id="/hello" class="com.neusoft.controller.HelloController"/>
  </beans>
  ```

- 4.编写主类HelloController，接收前端发送的请求，执行代码

  ```java
  import org.springframework.web.servlet.ModelAndView;
  import org.springframework.web.servlet.mvc.Controller;
  
  import javax.servlet.http.HttpServletRequest;
  import javax.servlet.http.HttpServletResponse;
  
  // 导入Controller接口
  public class HelloController implements Controller {
  
      @Override
      public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) 
              throws Exception {
          // ModelAndView 模型和视图
          ModelAndView mv = new ModelAndView();
          // 封装对象，放在 ModelAndView 的 model 中
          mv.addObject("msg","HelloSpringMVC");
          // 封装视图，放在ModelAndView 的 view 中
          mv.setViewName("hello");
          return mv;
      }
  }
  ```

- 配置Tomcat，网页测试

  ![image-20210208115427828](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210208115427828.png)

### 1.5、SpringMVC注解开发

目录结构

![image-20210208160620709](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210208160620709.png)

- 1.配置DispatcherServlet

  ```xml
  <!--1.注册servlet-->
  <servlet>
      <servlet-name>SpringMVC</servlet-name>
      <servlet-class>
          org.springframework.web.servlet.DispatcherServlet
      </servlet-class>
      <!--通过初始化参数指定SpringMVC配置文件的位置，进行关联-->
      <init-param>
          <param-name>contextConfigLocation</param-name>
          <param-value>classpath:springmvc-servlet.xml</param-value>
      </init-param>
      <!-- 启动顺序，数字越小，启动越早 -->
      <load-on-startup>1</load-on-startup>
  </servlet>
  
  <!--所有请求都会被springmvc拦截 -->
  <servlet-mapping>
      <servlet-name>SpringMVC</servlet-name>
      <url-pattern>/</url-pattern>
  </servlet-mapping>
  ```

- 2.配置Spring配置文件

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns:context="http://www.springframework.org/schema/context"
         xmlns:mvc="http://www.springframework.org/schema/mvc"
         xsi:schemaLocation="http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd
         http://www.springframework.org/schema/context
         https://www.springframework.org/schema/context/spring-context.xsd
         http://www.springframework.org/schema/mvc
         https://www.springframework.org/schema/mvc/spring-mvc.xsd">
  
      <!-- 自动扫描包，让指定包下的注解生效,由IOC容器统一管理 -->
      <context:component-scan base-package="com.neusoft.controller"/>
      <!-- 让Spring MVC不处理静态资源 -->
      <mvc:default-servlet-handler />
      <!--
          支持mvc注解驱动
              在spring中一般采用@RequestMapping注解来完成映射关系
              要想使@RequestMapping注解生效
              必须向上下文中注册DefaultAnnotationHandlerMapping
              和一个AnnotationMethodHandlerAdapter实例
              这两个实例分别在类级别和方法级别处理。
              而annotation-driven配置帮助我们自动完成上述两个实例的注入。
       -->
      <mvc:annotation-driven />
  
      <!-- 视图解析器 -->
      <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
            id="internalResourceViewResolver">
          <!-- 前缀 -->
          <property name="prefix" value="/WEB-INF/jsp/" />
          <!-- 后缀 -->
          <property name="suffix" value=".jsp" />
      </bean>
  
  </beans>
  ```

- 3.配置controller

  ```java
  import org.springframework.stereotype.Controller;
  import org.springframework.ui.Model;
  import org.springframework.web.bind.annotation.RequestMapping;
  
  @Controller
  @RequestMapping("/hello")
  public class HelloController {
  
      @RequestMapping("/h1")
      public String Hello(Model model){
          // 封装数据
          model.addAttribute("msg","Hello SpringMVC Annotation");
          return "hello";     // 会被视图解析器处理
      }
  }
  ```

- 4.【hello.jsp】接收数据

  ```jsp
  <html>
  <head>
      <title>jsp：hello</title>
  </head>
  <body>
  ${msg}
  </body>
  </html>
  ```

- 启动Tomcat，查看结果

  ![image-20210208161812709](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210208161812709.png) 

## 2、Controller

配置web.xml，需要配置DispatcherServlet

```xml
<!--1.注册servlet-->
<servlet>
    <servlet-name>SpringMVC</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <!--通过初始化参数指定SpringMVC配置文件的位置，进行关联-->
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:springmvc-servlet.xml</param-value>
    </init-param>
    <!-- 启动顺序，数字越小，启动越早-->
    <load-on-startup>1</load-on-startup>
</servlet>

<!--所有请求都会被springmvc拦截-->
<servlet-mapping>
    <servlet-name>SpringMVC</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```



### 2.1、implements Controller

- Controller类

  ```java
  // 只要实现了Controller接口的类，就说明这是一个控制器了
  public class ControllerTest1 implements Controller {
      @Override
      public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) 
              throws Exception {
          ModelAndView modelAndView = new ModelAndView();
  
          modelAndView.addObject("msg","Controller Test 1");
          modelAndView.setViewName("test");
  
          return modelAndView;
      }
  }
  ```

- SpringMVC-Servlet配置文件

  ```xml
  <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
        id="internalResourceViewResolver">
      <!-- 前缀 -->
      <property name="prefix" value="/WEB-INF/jsp/" />
      <!-- 后缀 -->
      <property name="suffix" value=".jsp" />
  </bean>
  
  <bean id="/test1" class="com.neusoft.controller.ControllerTest1"/>
  ```

  在这个配置文件中，没有配置

  ```xml
  <context:component-scan base-package="com.neusoft.controller"/>
  <mvc:annotation-driven/>
  <mvc:default-servlet-handler/>
  ```

- WEB-INF/jsp/test.jsp填写接收的msg

  ```jsp
  <html>
  <head>
      <title>jsp：test1</title>
  </head>
  <body>
  ${msg}
  </body>
  </html>
  ```

### 2.2、@RequestMapping，@Controller

- Controller

```java
@Controller
// 代表这个类会被Spring接管
// 被标注为 @Controller 的类下的所有的方法
// 如果返回值是String，并且有具体的页面可以跳转，那么就会被视图解析器所解析
public class ControllerTest2{

    @RequestMapping("/test2")
    public String test2(Model model){

        model.addAttribute("msg","Controller Test 2");

        return "test";
    }

}
```

- springmvc-servlet配置文件

```xml
<!--扫描包-->
<context:component-scan base-package="com.neusoft.controller"/>

<!-- 视图解析器 -->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
      id="internalResourceViewResolver">
    <!-- 前缀 -->
    <property name="prefix" value="/WEB-INF/jsp/" />
    <!-- 后缀 -->
    <property name="suffix" value=".jsp" />
</bean>
```

- Tomcat测试

![image-20210209100340801](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210209100340801.png)

### 2.3、分析

相较之下，注解开发省去了在Spring配置文件中注册，多了一步配置**扫描包**，使包下的注解生效

```xml
<context:component-scan base-package="com.neusoft.controller"/>
```

如果使用实现接口的方法，就仍然需要在配置文件中注册

```xml
<bean id="/test1" class="com.neusoft.controller.ControllerTest1"/>
```

同时，可以在一个Controller中配置多个处理

![image-20210209101709416](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210209101709416.png)

@RequestMapping可以放在任意的方法上面，也可以放在任意类上面，可以实现一个继承的关系，**也可以用来调用多级的jsp页面**，方法等同于多层调用

![image-20210209104752067](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210209104752067.png)

## 3、RestFul 风格详解

简单例子对比

```
localhost:8080/servlet/hello?method=add&.......
```

```
localhost:8080/servlet/hello/method/add/.......
```

Restful就是一个资源定位及资源操作的风格。不是标准也不是协议，只是一种风格。基于这个风格设计的软件可以更简洁，更有层次，更易于实现缓存等机制。

环境准备

- web.xml配置DispatcherServlet
- spring.xml配置视图解析器（InternalResourceViewResolver）和扫描包（component-scan）【注解开发】
- 增加Controller类，添加注解

### 3.1、原始风格

```java
@RequestMapping("/add")
public String test1(int a, int b, Model model){
    int res = a+b;
    model.addAttribute("msg","结果为："+res);
    return "test";
}
```

测试

![image-20210209114248085](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210209114248085.png)

通过 ? 来传递参数，使用 & 进行参数分隔

### 3.2、RestFul 风格

```java
// RestFul
@RequestMapping(value = "/add2/{a}/{b}",method = RequestMethod.GET)
public String test2(@PathVariable int a, @PathVariable int b, Model model){
    int res = a+b;
    model.addAttribute("msg","结果为："+res);
    return "test";
}
```

测试

![image-20210209114546536](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210209114546536.png)

代码拆解：给需要传的参数添加上@PathVariable注解，并修改@RequestMapping为 /add2/{a}/{b} ，可以对RequestMapping指定请求方法![image-20210209115120152](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210209115120152.png)

可以为如下请求方法

![image-20210209141529041](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210209141529041.png)

相较之下，RestFul方法更加简单易读

-------------------

新方法：使用@GetMapping和@PostMapping

- @GetMapping

  ```java
  @GetMapping("/add3/{a}/{b}")
  public String test3(@PathVariable int a, @PathVariable int b, Model model){
      int res = a+b;
      model.addAttribute("msg","结果为："+res);
      return "test";
  }
  ```

  ![image-20210209144129159](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210209144129159.png)

- @PostMapping

  ```java
  @PostMapping("/add3/{a}/{b}")
  public String test4(@PathVariable int a, @PathVariable int b, Model model){
      int res = a+b;
      model.addAttribute("msg","结果为："+res);
      return "test";
  }
  ```

  编写表单

  ```xml
  <body>
  <form action="add3/1/2" method="post">
      <input type="submit">
  </form>
  </body>
  ```

  执行结果

  ![image-20210209143210679](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210209143210679.png)

![image-20210209143628175](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210209143628175.png)

![image-20210209143938165](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210209143938165.png)

## 4、重定向和转发

- 前提：没有配置视图解析器

  ![image-20210209153723261](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210209153723261.png)

### 4.1、转发

```java
@RequestMapping("/test1")
public String test(Model model){
    model.addAttribute("msg", "Model Test 1");
    // 转发
    return "/WEB-INF/jsp/hello.jsp";
}
```

```java
@RequestMapping("/test2")
public String test2(Model model){
    model.addAttribute("msg", "Model Test 2");
    // 转发
    return "forward:/WEB-INF/jsp/hello.jsp";
}
```

![image-20210209154436307](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210209154436307.png)

在加了`/`后，就指代的是转发，或者添加上`forward:`即可实现转发的操作

### 4.2、重定向

```java
@RequestMapping("/test3")
public String test3(Model model){
    model.addAttribute("msg", "Model Test 3");
    // 重定向
    return "redirect:/index.jsp";
}
```

`http://localhost:8080/servletapi/test3`
`http://localhost:8080/servletapi/index.jsp?msg=Model+Test+3`

![image-20210209155051265](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210209155051265.png)

### 4.3、分析

转发URL不会改变，重定向URL会改变

加了forward转发或者redirect重定向就不会走视图解析器

**重定向不能访问WEB-INF**

## 5、接收请求参数和数据回显

### 5.1、前端接收参数

```java
@GetMapping("/test1")
public String test1(@RequestParam("username") String name, Model model){
    // 1.接收前端参数
    System.out.println("接收到前端的参数："+name);
    // 2.将返回的结果传递给前端
    model.addAttribute("msg",name);
    // 3.视图跳转
    return "hello";
}
```

**注意：只要是前端发送的参数，都必须加上`@RequestParam("xxx")`**

### 5.2、前端接收对象

Model传输

```java
@GetMapping("test2")
public String test2(User user, Model model){
    System.out.println(user);
    model.addAttribute("msg",user);
    return "hello";
}
```

ModelMap传输

```java
@GetMapping("test3")
public String test3(User user, ModelMap map){
    System.out.println(user);
    map.addAttribute("msg",user);
    return "hello";
}
```

### 5.3、分析

1.接收前端用户传递的参数，判断参数的名字，假设名字直接在方法上，可以直接使用

2.假设传递的是一个对象`User`，匹配`User`对象中的字段名，如果名字匹配则成功返回值，否则返回`null`

LinkedHashMap -- ModelMap -- Model

**源码解析**

`ModelMap`和`Model`的`addAttribute`相同，都是可以传输一个`Object`的，并且可以为`null`

```java
/**
 * Add the supplied attribute under the supplied name.
 * @param attributeName the name of the model attribute (never {@code null})
 * @param attributeValue the model attribute value (can be {@code null})
 */
public ModelMap addAttribute(String attributeName, @Nullable Object attributeValue) {
   Assert.notNull(attributeName, "Model attribute name must not be null");
   put(attributeName, attributeValue);
   return this;
}
```

```java
@Override
public ExtendedModelMap addAttribute(String attributeName, @Nullable Object attributeValue) {
   super.addAttribute(attributeName, attributeValue);
   return this;
}
```

## 6、乱码

### 6.1、乱码出现情况

出现问题：前端表单以POST方式提交，提交给后端解析的时候出现乱码情况

![image-20210210081752938](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210210081752938.png)

前端编码是没有错的，格式化出来的数据是可以解析的

![image-20210210082803082](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210210082803082.png)

后端输出也为乱码，

![image-20210210081900018](C:\Users\LiuFeiyu\AppData\Roaming\Typora\typora-user-images\image-20210210081900018.png)

由此证明，在**数据从前端发送给后端**的时候出现了问题。

由于**get**请求封装了乱码的解析功能，因此传输一些字符的时候会避免乱码

### 6.2、解决方法

- 打开Tomcat安装目录，找到`..\apache-tomcat-9.0.41\conf\server.xml`，添加uri编码

  ![image-20210210100636403](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210210100636403.png)

  `URIEncoding="utf-8"`

- web.xml中添加过滤器

  ```xml
  <filter>
      <filter-name>encoding</filter-name>
      <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
      <init-param>
          <param-name>encoding</param-name>
          <param-value>utf-8</param-value>
      </init-param>
  </filter>
  <filter-mapping>
      <filter-name>encoding</filter-name>
      <url-pattern>/*</url-pattern>
  </filter-mapping>
  ```

  注意，在` <url-pattern>/*</url-pattern>`中，需要使用`/*`，使所有请求生效，包括**jsp**，如果是单单为`/`，则jsp无法生效

- 自定义过滤器，不到万不得已不使用。

- 

  ```java
  import javax.servlet.*;
  import javax.servlet.http.HttpServletRequest;
  import javax.servlet.http.HttpServletRequestWrapper;
  import javax.servlet.http.HttpServletResponse;
  import java.io.IOException;
  import java.io.UnsupportedEncodingException;
  import java.util.Map;
  
  /**
  * 解决get和post请求 全部乱码的过滤器
  */
  public class GenericEncodingFilter implements Filter {
  
     @Override
     public void destroy() {
    }
  
     @Override
     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
         //处理response的字符编码
         HttpServletResponse myResponse=(HttpServletResponse) response;
         myResponse.setContentType("text/html;charset=UTF-8");
  
         // 转型为与协议相关对象
         HttpServletRequest httpServletRequest = (HttpServletRequest) request;
         // 对request包装增强
         HttpServletRequest myrequest = new MyRequest(httpServletRequest);
         chain.doFilter(myrequest, response);
    }
  
     @Override
     public void init(FilterConfig filterConfig) throws ServletException {
    }
  
  }
  
  //自定义request对象，HttpServletRequest的包装类
  class MyRequest extends HttpServletRequestWrapper {
  
     private HttpServletRequest request;
     //是否编码的标记
     private boolean hasEncode;
     //定义一个可以传入HttpServletRequest对象的构造函数，以便对其进行装饰
     public MyRequest(HttpServletRequest request) {
         super(request);// super必须写
         this.request = request;
    }
  
     // 对需要增强方法 进行覆盖
     @Override
     public Map getParameterMap() {
         // 先获得请求方式
         String method = request.getMethod();
         if (method.equalsIgnoreCase("post")) {
             // post请求
             try {
                 // 处理post乱码
                 request.setCharacterEncoding("utf-8");
                 return request.getParameterMap();
            } catch (UnsupportedEncodingException e) {
                 e.printStackTrace();
            }
        } else if (method.equalsIgnoreCase("get")) {
             // get请求
             Map<String, String[]> parameterMap = request.getParameterMap();
             if (!hasEncode) { // 确保get手动编码逻辑只运行一次
                 for (String parameterName : parameterMap.keySet()) {
                     String[] values = parameterMap.get(parameterName);
                     if (values != null) {
                         for (int i = 0; i < values.length; i++) {
                             try {
                                 // 处理get乱码
                                 values[i] = new String(values[i]
                                        .getBytes("ISO-8859-1"), "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                 e.printStackTrace();
                            }
                        }
                    }
                }
                 hasEncode = true;
            }
             return parameterMap;
        }
         return super.getParameterMap();
    }
  
     //取一个值
     @Override
     public String getParameter(String name) {
         Map<String, String[]> parameterMap = getParameterMap();
         String[] values = parameterMap.get(name);
         if (values == null) {
             return null;
        }
         return values[0]; // 取回参数的第一个值
    }
  
     //取所有值
     @Override
     public String[] getParameterValues(String name) {
         Map<String, String[]> parameterMap = getParameterMap();
         String[] values = parameterMap.get(name);
         return values;
    }
  }
  ```

  添加过滤器

  ```xml
  <filter>
      <filter-name>encoding1</filter-name>
      <filter-class>com.neusoft.filter.GenericEncodingFilter</filter-class>
      <init-param>
          <param-name>encoding</param-name>
          <param-value>utf-8</param-value>
      </init-param>
  </filter>
  <filter-mapping>
      <filter-name>encoding1</filter-name>
      <url-pattern>/*</url-pattern>
  </filter-mapping>
  ```

## 7、JSON交互处理

前后端分离时代：

后端部署业务等，提供接口、数据

JSON作为前后端约定的数据格式

前端独立部署：负责渲染后端数据

Json格式

```json
{"name":"Jerry","age":15,"sex":"male"}
-------------------------------------------
[{"name":"Jerry","age":15,"sex":"male"},
 {"name":"Tomas","age":13,"sex":"male"},
 {"name":"Oxess","age":22,"sex":"male"}]
```

### 7.1、Jacketson

- 整合成工具类

```java
public class JsonUtils {
    public static String getJson(Object object){
        return getJson(object, "yyyy-MM-dd HH:mm:ss");
    }
    // 静态方法，不需要创建对象即可使用
    public static String getJson(Object object, String dateFormat) {
        // ObjectMapper,时间解析后默认格式为 Timestamp 时间戳
        ObjectMapper mapper = new ObjectMapper();
        // 自定义日期格式，
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            // 将默认时间戳方式设置为false
            mapper.configure(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
            mapper.setDateFormat(sdf);
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}
```

- Controller

`@RestController`
`@RequestMapping("/json")`

```java
@RequestMapping("/test1")
public String test1() {

    // 创建一个对象
    User user = new User("阿斯顿",15,"MALE");

    return JsonUtils.getJson(user);
}

@RequestMapping("/test2")
public String test2() {
    List<User> userList = new ArrayList<>();
    for (int i=1;i<=5;i++){
        User user = new User("阿斯顿"+i,15+i,"MALE");
        userList.add(user);
    }
    return JsonUtils.getJson(userList);
}

@RequestMapping("/test3")
public String test3() {
    Date date = new Date();
    return JsonUtils.getJson(date, "yyyy-MM-dd HH:mm:ss");
}
```

### 7.2、Fastjson

```java
@RequestMapping("/test4")
public String test4() {
    List<User> userList = new ArrayList<>();
    for (int i=1;i<=5;i++){
        User user = new User("阿斯顿"+i,15+i,"MALE");
        userList.add(user);
    }
    return JSON.toJSONString(userList);
}
```

### 7.3、总结和乱码处理

- 总结

@RestController 标注这个类下面所有方法只会返回字符串

@ResponseBody 这个注解可以让你不走视图解析器，直接返回一个字符串

![image-20210210132251112](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/A3sf6WquQ9wmCeZ.png)

多个对象的情况下会形成json数组

- 乱码处理

```xml
<!--Json 乱码解决-->
<mvc:annotation-driven>
    <mvc:message-converters register-defaults="true">
        <bean class="org.springframework.http.converter.StringHttpMessageConverter">
            <constructor-arg value="UTF-8"/>
        </bean>
        <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
            <property name="objectMapper">
                <bean class="org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean">
                    <property name="failOnEmptyBeans" value="false"/>
                </bean>
            </property>
        </bean>
    </mvc:message-converters>
</mvc:annotation-driven>
```

## 8、SSM整合 -- Mybatis

![image-20210218154447497](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210218154447497.png)

### 8.1、环境搭建

- 建立数据库

  ```sql
  CREATE DATABASE `ssmbuild`;
  
  USE `ssmbuild`;
  
  DROP TABLE IF EXISTS `books`;
  
  CREATE TABLE `books` 
  (
      `bookId` INT(10) NOT NULL AUTO_INCREMENT COMMENT '书id',
      `bookName` VARCHAR(100) NOT NULL COMMENT '书名',
      `bookCounts` INT(11) NOT NULL COMMENT '数量',
      `detail` VARCHAR(200) NOT NULL COMMENT '描述',
      KEY `bookId` (`bookId`)
  ) ENGINE=INNODB DEFAULT CHARSET=utf8
  
  INSERT  INTO `books`(`bookId`,`bookName`,`bookCounts`,`detail`)VALUES
  (1,'Java',1,'从入门到放弃'),
  (2,'MySQL',10,'从删库到跑路'),
  (3,'Linux',5,'从进门到进牢');
  ```

- pom配置

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
  
      <groupId>com.neusoft</groupId>
      <artifactId>SSM-Build</artifactId>
      <version>1.0-SNAPSHOT</version>
      
      <properties>
          <maven.compiler.source>11</maven.compiler.source>
          <maven.compiler.target>11</maven.compiler.target>
          <maven.compiler.compilerVersion>11</maven.compiler.compilerVersion>
      </properties>
  
      <dependencies>
          <!-- https://mvnrepository.com/artifact/junit/junit -->
          <dependency>
              <groupId>junit</groupId>
              <artifactId>junit</artifactId>
              <version>4.12</version>
          </dependency>
          <!-- https://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
          <dependency>
              <groupId>org.springframework</groupId>
              <artifactId>spring-webmvc</artifactId>
              <version>5.3.3</version>
          </dependency>
          <!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
          <dependency>
              <groupId>javax.servlet</groupId>
              <artifactId>javax.servlet-api</artifactId>
              <version>4.0.1</version>
          </dependency>
          <!-- https://mvnrepository.com/artifact/javax.servlet.jsp/javax.servlet.jsp-api -->
          <dependency>
              <groupId>javax.servlet.jsp</groupId>
              <artifactId>javax.servlet.jsp-api</artifactId>
              <version>2.3.3</version>
          </dependency>
          <!-- https://mvnrepository.com/artifact/javax.servlet/jstl -->
          <dependency>
              <groupId>javax.servlet</groupId>
              <artifactId>jstl</artifactId>
              <version>1.2</version>
          </dependency>
          <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
          <dependency>
              <groupId>org.projectlombok</groupId>
              <artifactId>lombok</artifactId>
              <version>1.18.12</version>
          </dependency>
          <!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
          <dependency>
              <groupId>com.alibaba</groupId>
              <artifactId>fastjson</artifactId>
              <version>1.2.75</version>
          </dependency>
          <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
          <dependency>
              <groupId>mysql</groupId>
              <artifactId>mysql-connector-java</artifactId>
              <version>5.1.49</version>
          </dependency>
          <!-- https://mvnrepository.com/artifact/com.mchange/c3p0 -->
          <dependency>
              <groupId>com.mchange</groupId>
              <artifactId>c3p0</artifactId>
              <version>0.9.5.2</version>
          </dependency>
          <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
          <dependency>
              <groupId>org.mybatis</groupId>
              <artifactId>mybatis</artifactId>
              <version>3.5.6</version>
          </dependency>
          <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis-spring -->
          <dependency>
              <groupId>org.mybatis</groupId>
              <artifactId>mybatis-spring</artifactId>
              <version>2.0.6</version>
          </dependency>
          <!-- https://mvnrepository.com/artifact/log4j/log4j -->
          <dependency>
              <groupId>log4j</groupId>
              <artifactId>log4j</artifactId>
              <version>1.2.17</version>
          </dependency>
          <!-- https://mvnrepository.com/artifact/org.springframework/spring-jdbc -->
          <dependency>
              <groupId>org.springframework</groupId>
              <artifactId>spring-jdbc</artifactId>
              <version>5.3.3</version>
          </dependency>
          <!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
          <dependency>
              <groupId>org.aspectj</groupId>
              <artifactId>aspectjweaver</artifactId>
              <version>1.9.6</version>
          </dependency>
      </dependencies>
  
      <!--资源过滤z-->
      <build>
          <resources>
              <resource>
                  <directory>src/main/java</directory>
                  <includes>
                      <include>**/*.properties</include>
                      <include>**/*.xml</include>
                  </includes>
                  <filtering>false</filtering>
              </resource>
              <resource>
                  <directory>src/main/resources</directory>
                  <includes>
                      <include>**/*.properties</include>
                      <include>**/*.xml</include>
                  </includes>
                  <filtering>false</filtering>
              </resource>
          </resources>
      </build>
  
  </project>
  ```

- 建立基本结构和配置框架

![image-20210218144935815](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210218144935815.png)

- mybatis-config.xml，由于整合了spring，因此**mybatis核心配置文件**仅仅需要写一些简单配置

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-config.dtd">
  <configuration>
  
      <typeAliases>
          <package name="com.neusoft.pojo"/>
      </typeAliases>
  
      <mappers>
          <mapper class="com.neusoft.dao.BookMapper"/>
      </mappers>
  
  </configuration>
  ```

- applicationContext.xml -- 整体整合文件

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="
         http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd">
  
      <import resource="classpath:spring-dao.xml"/>
      <import resource="classpath:spring-service.xml"/>
      <import resource="classpath:spring-mvc.xml"/>
  
  </beans>
  ```

- pojo实体类层

  ```java
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class Books {
  
      private int bookID;
      private String bookName;
      private int bookCounts;
      private String detail;
  }
  ```

- dao数据访问层，编写Mapper接口，稍后在BookMapper.xml中实现

  ```java
  public interface BookMapper {
      // 增加一本书
      int addBook(Books books);
      // 删除一本书
      int deleteBookById(@Param("bookId") int id);
      // 修改一本书
      int updateBookById(Books books);
      // 查询一本书
      Books queryBookById(int id);
      // 查询所有书
      List<Books> queryAllBook();
  }
  ```

  BookMapper.xml实现接口，id等同于接口名，

  由于在**mybatis核心配置文件**中使用了别名，parameterType仅仅需要写类名即可
  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.neusoft.dao.BookMapper">
      
      <insert id="addBook" parameterType="Books">
          insert into ssmbuild.books (bookName, bookCounts, detail)
          VALUES (#{bookName}, #{bookCounts}, #{detail});
      </insert>
  
      <delete id="deleteBookById" parameterType="int">
          delete from ssmbuild.books where bookID = #{bookId};
      </delete>
  
      <update id="updateBookById" parameterType="Books">
          update ssmbuild.books
          set bookName = #{bookName},bookCounts=#{bookCount},detail=#{detail}
          where bookID=#{bookID};
      </update>
  
      <select id="queryBookById" parameterType="int" resultType="Books">
          select * from ssmbuild.books
          where bookID = #{bookID};
      </select>
  
      <select id="queryAllBook" resultType="Books">
          select * from ssmbuild.books;
      </select>
      
  </mapper>
  ```

- Service服务层，编写接口

  ```java
  public interface BookService {
      // 增加一本书
      int addBook(Books books);
      // 删除一本书
      int deleteBookById(@Param("bookId") int id);
      // 修改一本书
      int updateBookById(Books books);
      // 查询一本书
      Books queryBookById(int id);
      // 查询所有书
      List<Books> queryAllBook();
  }
  ```

- Service 调用 Dao 层

  ```java
  public class BookServiceImpl implements BookService{
  
      // Service 调用 Dao 层
      // 调用dao层的操作，设置一个set接口，方便Spring管理
      private BookMapper bookMapper;
  
      // 通过set方法注入
      public void setBookMapper(BookMapper bookMapper) {
          this.bookMapper = bookMapper;
      }
  
      public int addBook(Books books) {
          return bookMapper.addBook(books);
      }
  
      public int deleteBookById(int id) {
          return bookMapper.deleteBookById(id);
      }
  
      public int updateBookById(Books books) {
          return bookMapper.updateBookById(books);
      }
  
      public Books queryBookById(int id) {
          return bookMapper.queryBookById(id);
      }
  
      public Books queryAllBook() {
          return bookMapper.queryAllBook();
      }
  }
  ```

### 8.2、Spring整合Mybatis -- 配置

- spring-dao配置文件，整合mybatis文件

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns:context="http://www.springframework.org/schema/context"
         xsi:schemaLocation="
         http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd
         http://www.springframework.org/schema/context
         https://www.springframework.org/schema/context/spring-context.xsd">
  
      <!--关联数据库配置文件-->
      <context:property-placeholder location="classpath:database.properties"/>
  
      <!--配置连接池
          dbcp：半自动化操作，不能自动连接
          c3p0：自动化操作（自动化加载配置文件，并且可以自动设置到对象中）
          druid
          hikari
      -->
      <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
          <property name="driverClass" value="${mysql.driver}"/>
          <property name="jdbcUrl" value="${mysql.url}"/>
          <property name="user" value="${mysql.username}"/>
          <property name="password" value="${mysql.password}"/>
  
          <!-- c3p0连接池的私有属性 -->
          <property name="maxPoolSize" value="30"/>
          <property name="minPoolSize" value="10"/>
          <!-- 关闭连接后不自动commit -->
          <property name="autoCommitOnClose" value="false"/>
          <!-- 获取连接超时时间 -->
          <property name="checkoutTimeout" value="10000"/>
          <!-- 当获取连接失败重试次数 -->
          <property name="acquireRetryAttempts" value="2"/>
  
      </bean>
  
      <!--sqlSessionFactory-->
      <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
          <property name="dataSource" ref="dataSource"/>
          <!--绑定Mybatis配置文件-->
          <property name="configLocation" value="classpath:mybatis-config.xml"/>
      </bean>
  
      <!--配置dao接口扫描包，动态实现了Dao接口注入到Spring容器中-->
      <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
          <!--注入SqlSessionFactory-->
          <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
          <property name="basePackage" value="com.neusoft.dao"/>
      </bean>
  
  </beans>
  ```

- spring-service配置文件，配置事务，实现业务

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns:context="http://www.springframework.org/schema/context"
         xsi:schemaLocation="
         http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd
         http://www.springframework.org/schema/context
         https://www.springframework.org/schema/context/spring-context.xsd">
  
      <!--扫描service下的包-->
      <context:component-scan base-package="com.neusoft.service"/>
  
      <!--将所有业务类，注入到Spring，可以通过配置或者注解实现-->
      <bean id="bookServiceImpl" class="com.neusoft.service.BookServiceImpl">
          <property name="bookMapper" ref="bookMapper"/>
      </bean>
  
      <!--声明式事务配置-->
      <bean id="transactionManager"
           class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
          <!--注入数据源-->
          <property name="dataSource" ref="dataSource"/>
      </bean>
  
      <!--aop事务支持【暂无】-->
  
  </beans>
  ```

### 8.3、SpringMVC层整合

- 1、增加web支持

  ![image-20210218151414514](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210218151414514.png)

- 2、配置web.xml，配置DispatcherServlet，乱码，session

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
           http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
           version="4.0">
  
      <!--配置DispatcherServlet-->
      <servlet>
          <servlet-name>SpringMVC</servlet-name>
          <servlet-class>
              org.springframework.web.servlet.DispatcherServlet
          </servlet-class>
          <init-param>
              <param-name>contextConfigLocation</param-name>
              <param-value>classpath:application-context.xml</param-value>
          </init-param>
          <!-- 启动顺序，数字越小，启动越早 -->
          <load-on-startup>1</load-on-startup>
      </servlet>
      <!--所有请求都会被springmvc拦截 -->
      <servlet-mapping>
          <servlet-name>SpringMVC</servlet-name>
          <url-pattern>/</url-pattern>
      </servlet-mapping>
  
      <!--乱码-->
      <filter>
          <filter-name>encoding</filter-name>
          <filter-class>
              org.springframework.web.filter.CharacterEncodingFilter
          </filter-class>
          <init-param>
              <param-name>encoding</param-name>
              <param-value>utf-8</param-value>
          </init-param>
      </filter>
      <filter-mapping>
          <filter-name>encoding</filter-name>
          <url-pattern>/*</url-pattern>
      </filter-mapping>
  
      <!--session-->
      <session-config>
          <session-timeout>15</session-timeout>
      </session-config>
  
  </web-app>
  ```

- 3、由于整合了Spring，因此需要关联spring配置文件，编写spring-mvc.xml配置文件

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns:context="http://www.springframework.org/schema/context"
         xmlns:mvc="http://www.springframework.org/schema/mvc"
         xsi:schemaLocation="
         http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd
         http://www.springframework.org/schema/context
         http://www.springframework.org/schema/context/spring-context.xsd
         http://www.springframework.org/schema/mvc
         http://www.springframework.org/schema/mvc/spring-mvc.xsd">
  
      <!--注解驱动-->
      <mvc:annotation-driven/>
      <!--静态资源过滤-->
      <mvc:default-servlet-handler/>
      <!--扫描包-->
      <context:component-scan base-package="com.neusoft.controller"/>
      <!--视图解析器-->
      <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
          <property name="prefix" value="/WEB-INF/jsp"/>
          <property name="suffix" value=".jsp"/>
      </bean>
  
  </beans>
  ```

### 8.4、实现业务，实现查询书籍功能

- 1、Controller层

  ```java
  @Controller
  @RequestMapping("/book")
  public class BookController {
      // controller 调用 Service层
      @Autowired
      @Qualifier("bookServiceImpl")
      private BookService bookService;
  
      // 查询全部书籍，并返回一个书籍展示页面
      @RequestMapping("/allBook")
      public String queryAllBook(Model model){
          List<Books> list = bookService.queryAllBook();
          model.addAttribute("list", list);
          return "allBook";
      }
  }
  ```

- 由于设置了视图解析器，controller中的方法只要是String的，都会返回一个jsp页面

  ```jsp
  <html>
      <head>
          <title>JSP: All-Book</title>
          <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
      </head>
      <body>
  
          <div class="container">
  <%--------------------------------------------------------------------------------------------------------------------%>
              <div class="row clearfix">
                  <div class="col-md-12 column">
                      <div class="page-header">
                          <h1>
                              <small>书籍列表 -------- 显示所有书籍</small>
                              <%--<%
                                  List<Books> list = (List<Books>) request.getAttribute("list");
                                  out.print(list.size());
                              %>--%>
                          </h1>
                      </div>
                  </div>
              </div>
  <%--------------------------------------------------------------------------------------------------------------------%>
              <div class="row clearfix">
                  <div class="col-md-12 column">
                      <table class="table table-hover table-striped">
                          <thead>
                              <tr>
                                  <th>书籍编号</th>
                                  <th>书籍名称</th>
                                  <th>书籍数量</th>
                                  <th>书籍详情</th>
                              </tr>
                          </thead>
                          <%--从数据库中查询--%>
                          <tbody>
                          <%--<c:if test="${list.size() == 3}">
                              <h1>111111111</h1>
                          </c:if>--%>
                              <c:forEach items="${list}" var="b">
                                  <tr>
                                      <td>${b.bookID}</td>
                                      <td>${b.bookName}</td>
                                      <td>${b.bookCounts}</td>
                                      <td>${b.detail}</td>
                                  </tr>
                              </c:forEach>
                          </tbody>
                      </table>
                  </div>
              </div>
  <%--------------------------------------------------------------------------------------------------------------------%>
          </div>
      </body>
  </html>
  ```

### 8.5、增加书籍

由于所有的配置都已经完成了，现在仅仅需要写业务即可

- Controller层添加请求，跳转到增加书籍页面

  ```java
  @RequestMapping("/toAddBook")
  public String toAddPage(){
      return "addBook";
  }
  ```

- 编写jsp，建立表单用来提交书籍书籍

  ```jsp
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  
  <html>
      <head>
          <title>JSP：Add-Book</title>
          <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
      </head>
  
      <body>
          <div class="container">
  <%--------------------------------------------------------------------------------------------------------------------%>
              <div class="row clearfix">
                  <div class="col-md-12 column">
                      <div class="page-header">
                          <h1>
                              <small>书籍列表 -------- 新增书籍</small>
                          </h1>
                      </div>
                  </div>
              </div>
  <%--------------------------------------------------------------------------------------------------------------------%>
          <form action="${pageContext.request.contextPath}/book/addBook" method="post">
              <div class="form-group">
                  <label for="BookName">书籍名称</label>
                  <input type="text" name="bookName" class="form-control" id="BookName" required>
              </div>
              <div class="form-group">
                  <label for="BookCounts">书籍数量</label>
                  <input type="text" name="bookCounts" class="form-control" id="BookCounts" required>
              </div>
              <div class="form-group">
                  <label for="BookDetail">书籍描述</label>
                  <input type="text" name="detail" class="form-control" id="BookDetail" required>
              </div>
  
              <button type="submit" class="btn btn-default">Submit</button>
          </form>
  <%--------------------------------------------------------------------------------------------------------------------%>
          </div>
      </body>
  </html>
  ```

- 建立添加书籍请求，返回页面到书籍列表

  ```java
  @RequestMapping("/addBook")
  public String addBook(Books books){
      System.out.println("addBook: "+books);
      bookService.addBook(books);
      // 重定向到allBook请求
      return "redirect:/book/allBook";
  }
  ```

### 8.6、修改书籍

- 修改书籍：首先进行携带数据的页面跳转

  ```java
  @RequestMapping("/toUpdatePage")
  public String toUpdatePage(int id, Model model){
      Books books = bookService.queryBookById(id);
      model.addAttribute("books",books);
      return "updateBook";
  }
  ```

- 编写页面，隐藏id，修改数据后发送请求

  ```jsp
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <html>
  <head>
      <title>JSP：Add-Book</title>
      <link href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
  </head>
  
  <body>
  <div class="container">
  <%--------------------------------------------------------------------------------------------------------------------%>
      <div class="row clearfix">
          <div class="col-md-12 column">
              <div class="page-header">
                  <h1>
                      <small>书籍列表 -------- 修改书籍</small>
                  </h1>
              </div>
          </div>
      </div>
  <%--------------------------------------------------------------------------------------------------------------------%>
      <form action="${pageContext.request.contextPath}/book/updateBook" method="post">
          <div class="form-group">
              <label for="BookId">书籍编号：${books.bookID}</label>
              <input type="hidden" name="bookID" class="form-control" id="BookId" value="${books.bookID}" required>
          </div>
          <div class="form-group">
              <label for="BookName">书籍名称</label>
              <input type="text" name="bookName" class="form-control" id="BookName" value="${books.bookName}" required>
          </div>
          <div class="form-group">
              <label for="BookCounts">书籍数量</label>
              <input type="text" name="bookCounts" class="form-control" id="BookCounts" value="${books.bookCounts}" required>
          </div>
          <div class="form-group">
              <label for="BookDetail">书籍描述</label>
              <input type="text" name="detail" class="form-control" id="BookDetail" value="${books.detail}" required>
          </div>
  
          <button type="submit" class="btn btn-default">修改</button>
      </form>
  <%--------------------------------------------------------------------------------------------------------------------%>
  </div>
  </body>
  </html>
  ```

- controller接收请求，重定向到书籍列表

  ```java
  @RequestMapping("/updateBook")
  public String updateBook(Books books){
      System.out.println("updateBooks:"+books);
      if (bookService.updateBookById(books) > 0) {
          System.out.println("update success");
      }else {
          System.out.println("false");
      }
      return "redirect:/book/allBook";
  }
  ```

### 8.7、删除书籍

没有什么特殊操作，在书籍列表执行操作即可

Controller层进行操作

```java
@RequestMapping("/deleteBook/{bookId}")
public String deleteBook(@PathVariable("bookId") int id){
    if (bookService.deleteBookById(id) > 0) {
        System.out.println("delete success");
    }
    return "redirect:/book/allBook";
}
```

## 9、SSM的高级查询

### 9.1、根据name查询

dao层，接口为
`List<Books> queryBookByName(@Param("bookName") String bookName);`

```java
<select id="queryBookByName" parameterType="string" resultType="Books">
    select * from ssmbuild.books
    where bookName = #{bookName};
</select>
```

service层接口，和dao层一样，不需要加`Param`注解
`List<Books> queryBookByName(String bookName);`

service调用dao，实现接口

```java
public List<Books> queryBookByName(String bookName) {
    return bookMapper.queryBookByName(bookName);
}
```

Controller层，获取前端接收数据【参数名和前端要一致，不然会报空指针异常】

```java
// 查询书籍
@RequestMapping("/queryBook")
public String queryBook(String queryBookName, Model model){
    List<Books> booksList = bookService.queryBookByName(queryBookName);
    if (queryBookName.isEmpty()) {
        return "redirect:/book/allBook";
    } else if (booksList == null)
    {
        return "allBook";
    }else {
        model.addAttribute("list",booksList);
        return "allBook";
    }
}
```

### 9.2、强化查询

基于普通查询更改，使用动态SQL，模糊查询，查询ID和Name

Dao层和service层如下

Dao

```java
List<Books> queryBooks(Map<Object,Object> map);
```

```xml
<select id="queryBooks" parameterType="map" resultType="Books">
    select * from ssmbuild.books
    <where>
        <if test="bookID != null">
            or bookID = #{bookID}
        </if>
        <if test="bookName != null">
            or bookName like concat('%', #{bookName}, '%')
        </if>
    </where>
</select>
```

采用where标签和if标签，实现动态sql，根据输入的书籍编号或者书名进行查询

service层

```java
List<Books> queryBooks(Map<Object,Object> map);
```

```java
// Service 调用 Dao 层
private BookMapper bookMapper;

public void setBookMapper(BookMapper bookMapper) {
    this.bookMapper = bookMapper;
}

public List<Books> queryBooks(Map<Object,Object> map) {
    return bookMapper.queryBooks(map);
}
```

## 10、AJAX

### 10.1、什么是AJAX

Asynchronous JavaScript And XML（异步的 JavaScript 和 XML）

异步无刷新请求（发送请求的时候会刷新页面）

**AJAX是一种在无需重新加载整个网页的情况下，能够==更新部分网页==的技术**

### 10.2、伪AJAX

了解ajax原理，并不是真正的ajax实现

-----------

编写index.jsp

**$ = jquery**，需要在jsp中的head标签调用脚本（官网的jquery无法访问，需要cdn加速）

```jsp
<script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.5.1/jquery.js"></script>
<%--官网jquery--%>
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
```

要实现ajax，先执行脚本，实现最简单的功能，在输入框失去焦点的时候，弹出警告框

```jsp
<script>
  function a1(){
    $.post({
      url:"${pageContext.request.contextPath}/ajax/a1",
      data:{'name':$("#username").val()},
      success:function (data,status) {
        alert(data);
        console.log("data: "+data);
        console.log("status: "+status)
      },
      error:function (data,status) {
        console.log("data: "+data);
        console.log("status: "+status)
      }
    });
  }
</script>
```

这个脚本中需要一个输入框，如下，失去焦点的时候调用函数a1()

![image-20210222102226631](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210222102226631.png)

```jsp
<label>
  <input type="text" id="username" onblur="a1()">
</label>
```

同时需要绑定Controller，简单写一个判断

```java
@RestController
@RequestMapping("/ajax")
public class AjaxController {
    @RequestMapping("/a1")
    public void a1(String name, HttpServletResponse resp)
            throws IOException {
        System.out.println("a1:param: "+name);
        if (name.equals("yes")){
            resp.getWriter().print("true");
        }else {
            resp.getWriter().print("false");
        }
    }
}
```

### 10.3、ajax实现页面无刷新

页面无刷新同时更新网页

-------

编写test.html，直接访问页面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>iframe页面无刷新</title>
    <script>
        function go() {
            document.getElementById("iframe1").src=document.getElementById("url").value;
        }
    </script>

</head>
<body>

<div>
    <p>请输入地址：</p>
    <p>
        <label>
            <input type="text" id="url" value="">
            <input type="button" value="提交" onclick="go()">
        </label>
    </p>

</div>

<div>
    <iframe id="iframe1" style="width: 100%; height: 500px"></iframe>
</div>

</body>
</html>
```

实现结果如下，在输入网址，点击提交后，页面无刷新，但是页面已经更新了

![image-20210222102911117](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210222102911117.png)

### 10.4、点击按钮更新页面

编写test2.jsp，并启动服务，直接访问该jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Test 2</title>

    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.5.1/jquery.js"></script>
    <script>
        $(function (){
            $("#btn").click(function (){
                $.post("${pageContext.request.contextPath}/ajax/a2",function (data){
                    // console.log(data);
                    let html = "";
                    for (let i = 0; i < data.length; i++) {
                        html += "<tr>" +
                            "<td>" + data[i].name + "</td>" +
                            "<td>" + data[i].age + "</td>" +
                            "<td>" + data[i].sex + "</td>" +
                            "</tr>"
                    }
                    $("#content").html(html);
                })
            })
        });
    </script>
</head>
<body>

<input id="btn" type="button" value="load data">

<table>
    <thead>
        <tr>
            <td>姓名</td>
            <td>年龄</td>
            <td>性别</td>
        </tr>
    </thead>
    <tbody id="content">
    <tr>
        <td></td>
    </tr>
    </tbody>
</table>

</body>
</html>
```

Controller层

```java
@RequestMapping("/a2")
public List<User> a2(){
    List<User> userList = new ArrayList<>();

    userList.add(new User("ass",1,"male"));
    userList.add(new User("bss",2,"female"));
    userList.add(new User("css",3,"male"));

    return userList;
}
```

建立一个button，一个table，button在按下的时候触发函数，执行controller的代码，获取用户列表，然后将用户列表返回给前端jsp，前端通过post的data来接收，console.log()出来的内容是json对象

创建变量，通过for循环，将获得的对象解析，并拼接成jsp的语句，再通过绑定tablebody的id，以html方法插入到tbody中

jsp的Line 10中，是将`post()`方法合并成了一行来写，第一个参数是url，第二个参数是data，剩下两个参数没写，为默认值

### 10.5、用户名密码判断

编写login.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>

    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.5.1/jquery.js"></script>
    <script>
        function a1(){
            $.post({
                url: "${pageContext.request.contextPath}/ajax/a3",
                data:{"name":$("#name").val()},
                success:function (data) {

                    console.log(data);
                    
                    if (data.toString()==='ok'){
                        $("#userInfo").css("color","green");
                    } else {
                        $("#userInfo").css("color","red");
                    }
                    $("#userInfo").html(data);
                },
            });
        }

        function a2(){
            $.post({
                url: "${pageContext.request.contextPath}/ajax/a3",
                data:{"pwd":$("#pwd").val()},
                success:function (data) {
                    console.log(data);

                    if (data.toString()==='ok'){
                        $("#pwdInfo").css("color","green");
                    } else {
                        $("#pwdInfo").css("color","red");
                    }
                    $("#pwdInfo").html(data);
                },
            });
        }
    </script>

</head>
<body>

<p>
    username: <input id="name" type="text" onblur="a1()">
    <span id="userInfo"></span>
</p>

<p>
    password: <input id="pwd" type="text" onblur="a2()">
    <span id="pwdInfo"></span>
</p>

</body>
</html>
```

添加两个输入框，两个span标签

先获取输入框中的内容，然后将输入框中的内容发给后端进行判断，再将后端的结果发回给前端，通过span标签来显示结果

  `$("#userInfo").html(data);`绑定span标签id，显示结果

controller层实行判断，参数和前端的控件id等同

```java
@RequestMapping("/a3")
public String a3(String name,String pwd){
    String msg = "";

    if (name != null) {
        if (name.equals("admin")){
            msg = "ok";
        }else {
            msg = "username error";
        }
    }

    if (pwd != null) {
        if (pwd.equals("123456")){
            msg = "ok";
        }else {
            msg = "password error";
        }
    }
    return msg;
}
```

将msg发送给前端data，前端data做判断

## 11、拦截器

类似于servlet的filter

### 11.1、拦截器基本理解

- 编写拦截器配置类

  ```java
  package com.neusoft.config;
  
  public class Interceptor implements HandlerInterceptor {
      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
              throws Exception {
          // return true; 执行下一个拦截器，放行
          // return false; 不执行下一个拦截器，放行，进程卡死
          System.out.println("=====处理前=====");
          return true;
      }
  
      // 拦截日志
      @Override
      public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
              throws Exception {
          System.out.println("=====处理后=====");
      }
  
      @Override
      public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
              throws Exception {
          System.out.println("=====清理=====");
  
      }
  }
  ```

- 有了拦截器，需要在spring配置文件中配置拦截器

  ```xml
  <!--配置拦截器-->
  <mvc:interceptors>
      <mvc:interceptor>
          <!--包括这个请求下面的所有请求-->
          <mvc:mapping path="/**"/>
          <bean class="com.neusoft.config.Interceptor"/>
      </mvc:interceptor>
  </mvc:interceptors>
  ```

- 编写controller层，简单测试

  ```java
  @RestController
  public class TestController {
  
      @GetMapping("/t1")
      public String test(){
  
          System.out.println("TestController -- test()方法 执行了");
          return "OK";
      }
  }
  ```

- 执行结果 -- 拦截器执行顺序如下图所示

  ![image-20210223081036393](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210223081036393.png)

结论：一般将要处理的事情放在`preHandle`中，进行请求的放行或拦截；`postHandle`一般可以用来记录日志，`afterCompletion`进行清理操作，常用的基本都是`preHandle`

### 11.2、拦截器范例

用于进行账户登录的简单判定

实现主页在没有登录的情况下，点击跳转只能到登录页面，登录过后，通过判断session是否存在等操作，进行放行拦截操作

index.jsp作为首页，发送请求，用于页面跳转

```jsp
<h1>
  <a href="${pageContext.request.contextPath}/user/goLogin">login</a>
</h1>

<h1>
  <a href="${pageContext.request.contextPath}/user/main">main</a>
</h1>
```

分别跳转到登录页面和主页，对应的controller如下

```java
@Controller
@RequestMapping("/user")
public class LoginController {

    @RequestMapping("/login")
    public String login(
        HttpSession session, String username, String password, Model model){
        // 把用户的信息存在session
        session.setAttribute("userLoginInfo",username);
        model.addAttribute("username",username);
        return "main";
    }

    @RequestMapping("/main")
    public String mainPage(HttpSession session, Model model){

        return "main";
    }

    @RequestMapping("/goLogin")
    public String login1(){
        return "login";
    }
}

```

index.jsp的请求分别跳转到登录页面和首页（该程序中没有配置`@ResponseBody`，即没有配置`@RestController`，则需要在spring中配置视图解析器`InternalResourceViewResolver`）

main.jsp存放于WEB-INF下，用于向session获取值

```jsp
<body>
main
<p>
    <span>
        ${sessionScope.userLoginInfo}
    </span>
</p>
<p>
    <a href="${pageContext.request.contextPath}/user/logout">logout</a>
</p>
</body>
```

`sessionScope.userLoginInfo`可以直接在页面获取session中的值，userLoginInfo作为session的键值名

login.jsp存放于WEB-INF下，作为登录页面

```jsp
<body>

<h1>Login</h1>
<form action="${pageContext.request.contextPath}/user/login" method="post">
<label>
    Username<input type="text" name="username">
    Password<input type="text" name="password">
</label>
    <input type="submit" value="submit">
</form>

</body>
```

通过form表单，发送login请求，将username和password发送给后端（后端controller变量要和前端name一致）执行存入输入数据到session操作，并登录到main主页

在目前情况下，由于没有拦截器存在，index.jsp中，可以直接跳转到main.jsp，因此添加拦截器LoginInterceptor

```java
public class LoginInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        // 放行判断，判断什么情况下登录
        if (request.getRequestURI().contains("goLogin")){
            return true;
        }

        if (request.getRequestURI().contains("login")){
            return true;
        }

        if (session.getAttribute("userLoginInfo") != null){
            return true;
        }

        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp")
            .forward(request,response);
        return false;
    }
}
```

同时需要在Spring中，配置拦截器

在这个配置中，需要注意的点是，在设置拦截路径的时候，需要在路径前加上斜杠，不然拼接uri的时候会出现错误，导致拦截器失效，这个path指的是：拦截`/user`下的所有请求，包括子文件夹（jsp文件）

`/**`的意思是所有文件夹及里面的子文件夹
`/*`是所有文件夹，不含子文件夹
`/`是web项目的根目录

```xml
<mvc:interceptors>
    <mvc:interceptor>
        <mvc:mapping path="/user/**"/>
        <bean class="com.neusoft.config.LoginInterceptor"/>
    </mvc:interceptor>
</mvc:interceptors>
```

该拦截器作用为，判断页面在什么情况下可以放行，否则禁止登录，转发请求到登录页面

假设，在没有登录的情况下，我直接点击进入主页，会转发到login页面，因为`/user/main`不满足请求条件（session为空，uri也不满足），反之，如果已经登录了，session存在，就会直接跳转到main页面，不会转发到登录页面。

## 9、文件上传和下载

文件上传配置需要导入外部依赖

```xml
<dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.3.3</version>
</dependency>
```

在spring中，需要配置

```xml
<!--文件上传配置-->
<!--这个bean的id必须为：multipartResolver，否则上传文件会报400的错误-->
<bean id="multipartResolver"
      class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
    <!-- 请求的编码格式，必须和jSP的pageEncoding属性一致，以便正确读取表单的内容，默认为ISO-8859-1 -->
    <property name="defaultEncoding" value="utf-8"/>
    <!-- 上传文件大小上限，单位为字节（10485760=10M） -->
    <property name="maxUploadSize" value="10485760"/>
    <property name="maxInMemorySize" value="40960"/>
</bean>
```

### 9.1、文件上传

index.jsp内容

```jsp
<form action="${pageContext.request.contextPath}/upload" enctype="multipart/form-data" method="post">
  <input type="file" name="file"/>
  <input type="submit" value="upload">
</form>
```

编写表单，type为file，可以用来选择文件

发送请求为`/upload`，编写controller层代码

文件上传方式1：

```java
//@RequestParam("file") 将name=file控件得到的文件封装成CommonsMultipartFile 对象
//批量上传CommonsMultipartFile则为数组即可
@RequestMapping("/upload")
public String fileUpload(
@RequestParam("file") CommonsMultipartFile file , HttpServletRequest request)
throws IOException {

//获取文件名 : file.getOriginalFilename();
String uploadFileName = file.getOriginalFilename();

//如果文件名为空，直接回到首页！
if ("".equals(uploadFileName)){
return "redirect:/index.jsp";
}
System.out.println("上传文件名 : "+uploadFileName);

//上传路径保存设置
String path = request.getServletContext().getRealPath("/upload");
//        System.out.println(path+"3333333333333333333");
//如果路径不存在，创建一个
File realPath = new File(path);
if (!realPath.exists()){
realPath.mkdir();
}
System.out.println("上传文件保存地址："+realPath);

InputStream is = file.getInputStream(); //文件输入流
OutputStream os = new FileOutputStream(new File(realPath,uploadFileName)); //文件输出流

//读取写出
int len=0;
byte[] buffer = new byte[1024];
while ((len=is.read(buffer))!=-1){
os.write(buffer,0,len);
os.flush();
}
os.close();
is.close();
return "redirect:/index.jsp";
}
```

文件上传方式2：

```java
// 采用file.Transto 来保存上传的文件
@RequestMapping("/upload2")
public String fileUpload2(
        @RequestParam("file") CommonsMultipartFile file, HttpServletRequest request)
        throws IOException {

    //上传路径保存设置
    String path = request.getServletContext().getRealPath("/upload");
    File realPath = new File(path);
    if (!realPath.exists()){
        realPath.mkdir();
    }
    //上传文件地址
    System.out.println("上传文件保存地址："+realPath);

    //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
    file.transferTo(new File(realPath +"/"+ file.getOriginalFilename()));

    return "redirect:/index.jsp";
}
```

### 9.2、文件下载

index.jsp

```jsp
<a href="${pageContext.request.contextPath}/res/1.png">点击下载</a>

<a href="${pageContext.request.contextPath}/download">点击下载</a>
```

controller编写

```java
@RequestMapping("/download")
public String downloads(HttpServletResponse response, HttpServletRequest request)
        throws Exception{
    //要下载的图片地址
    String path = request.getServletContext().getRealPath("/upload");
    String fileName = "1.png";//从上传点获取的文件名

    //1、设置response 响应头
    response.reset(); //设置页面不缓存,清空buffer
    response.setCharacterEncoding("UTF-8"); //字符编码
    response.setContentType("multipart/form-data"); //二进制传输数据
    //设置响应头
    response.setHeader("Content-Disposition",
            "attachment;fileName="+URLEncoder.encode(fileName, StandardCharsets.UTF_8));

    File file = new File(path,fileName);
    //2、 读取文件--输入流
    InputStream input=new FileInputStream(file);
    //3、 写出文件--输出流
    OutputStream out = response.getOutputStream();

    byte[] buff =new byte[1024];
    int index=0;
    //4、执行 写出操作
    while((index= input.read(buff))!= -1){
        out.write(buff, 0, index);
        out.flush();
    }
    out.close();
    input.close();
    return null;
}
```