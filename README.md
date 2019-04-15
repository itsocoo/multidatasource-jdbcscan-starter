---
title: 多数据源下的统一数据库持久化组件
date: 2019-04-15
categories: ['心得']
tags: ['心得','分享']
comments: true
---



### 1.丑陋的多数据源代码

```java
    /**
     * 保存购物车信息，相同的用户和sku,数量相加，
     */
    @Override
    public Boolean save(String brandCode, Cart cart) {
		// 省略 ……
        if (brandCode.equalsIgnoreCase(BrandEnum.ONLY.getMsg())) {
            List<OnlyShoppingCart> onlyShoppingCartList = mongoTemplate.find(query, OnlyShoppingCart.class);
            if (onlyShoppingCartList != null && onlyShoppingCartList.size() == 0) {
                OnlyShoppingCart onlyCart = new OnlyShoppingCart();
                BeanUtils.copyProperties(cart, onlyCart);
                if(StringUtils.isEmpty(cart.getShopGuideId())){
                    onlyCart.setShopGuideId("");
                }
                onlyCart.setBrandCode(brandCode);
                onlyCart.setCreateTime(new Date());
                mongoTemplate.save(onlyCart);
            } else {
                OnlyShoppingCart onlyShoppingCart=onlyShoppingCartList.get(0);
                onlyShoppingCart.setQuantity(onlyShoppingCart.getQuantity()+cart.getQuantity());
                 query=new Query(Criteria.where("id").is(onlyShoppingCart.getId()));
                Update update= new Update()
                        .set("quantity", onlyShoppingCart.getQuantity()).set("updateTime",new Date());
                if(StringUtils.isNoneEmpty(cart.getShopGuideId())){
                    update.set("shopGuideId",cart.getShopGuideId());
                }
                //更新查询返回结果集的第一条
                mongoTemplate.updateFirst(query,update,OnlyShoppingCart.class);
            }
        } else if (brandCode.equalsIgnoreCase(BrandEnum.JACKJONES.name())) {
			// 重复 省略 ……
        } else if (brandCode.equalsIgnoreCase(BrandEnum.VEROMODA.name())) {
			// 重复 省略 ……
        } else if (brandCode.equalsIgnoreCase(BrandEnum.SELECTED.name())) {
			// 重复 省略 ……
        } else if (brandCode.equalsIgnoreCase(BrandEnum.JLINDEBERG.name())) {
			// 重复 省略 ……
        } else if (brandCode.equalsIgnoreCase(BrandEnum.NAMEIT.name())) {
			// 重复 省略 ……
        } else if (brandCode.equalsIgnoreCase(BrandEnum.BESTSELLER.name())) {
           // 重复 省略 ……
        }
        else if (brandCode.equalsIgnoreCase(BrandEnum.FOL.name())) {
            // 重复 省略 ……
        }

        return true;
    }
  // 这才是保存 还有修改 删除 查询
```

**上面代码的问题：大量重复代码 结构不明确 多数据源和业务完全耦合在一块 对于别人接手后完全无法维护**

### 2.解决上面的问题

* 使用前引入包

  ```xml
  <!--引入自己的多数据源jar包 具体在本GitHub中找-->
  <dependency>
      <groupId>com.itsocoo</groupId>
      <artifactId>multidatasource-jdbctemplate-spring-boot-starter</artifactId>
      <version>2.1.3.RELEASE</version>
  </dependency>
  
  <dependency>
      <groupId>com.itsocoo</groupId>
      <artifactId>multidatasource-jdbcscan-spring-boot-starter</artifactId>
      <version>2.1.3.RELEASE</version>
      <exclusions>
          <exclusion>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-jdbc</artifactId>
          </exclusion>
      </exclusions>
  </dependency>
  ```

* 配置信息 application.properties

  ```properties
  # 配置数据源信息
  itsocoo.multi.datasource.enable.profile-type=dev
  itsocoo.multi.datasource.enable.platform=andy,cindy,john,jack,chris,barry,celia,jones
  # 数据源中在spring注入的每个组件的后缀（andyJdbcTemplate、andyPlatformTransactionManager）
  itsocoo.scan.jdbc-template-suffix=JdbcTemplate
  itsocoo.scan.transaction-manager-suffix=PlatformTransactionManager
  # 查询时候参数是Map时候 手动传入该次查询所使用的数据源名称 map.put(KEY,'andy')
  itsocoo.scan.jdbc-template-args-key=KEY
  ```

* 注入多数据源接口

  ```java
  @Service
  @MultiDataScanApiService(name = "multiTestService")
  public interface MultiTestService {
  
      @MultiDataScanApi(types = MultiTest.class, sql = "SELECT id,`trigger`,age,username,password,last_name,content,end_name,status,create_time,last_update_time,amt,price,brand FROM multi_test WHERE create_time>=:timeStart AND create_time<:timeEnd", executeType = ExecuteType.SELECT)
      List<MultiTest> selectList(Map<String, Object> params);
  }
  ```

* 直接使用

  ```java
  // 使用的时候注入当前类中
  @Service
  public class TestController {
      
      @Autowired
      private MultiTestService multiTestService;
      
      public void test() throws IOException {
          // TOOD 设置需要的路由即可 有三种方式 具体看代码
          multiTestService.selectListPage(null);
      }
  }
  ```

### 3.使用简单API

```java
// TODO 三种方式设置需要的数据库路由

        // 1.方式 ChoiceHelper.choice("andy");
        Query1Way query1Way = context.getBean(Query1Way.class);
        query1Way.test();

        // 2.方式 使用@MultiChoiceParams("key") String key设置
        Query2Way query2Way = context.getBean(Query2Way.class);
        query2Way.test();

        // 3.方式 使用params.put("KEY", "john"); KEY为properties中itsocoo.scan.jdbc-template-args-key=KEY
        Query3Way query3Way = context.getBean(Query3Way.class);
        query3Way.test();

        // 查询分页
        QueryPageList queryPageList = context.getBean(QueryPageList.class);
        queryPageList.test();

        // TODO 事务的两种使用方式
        TransactionService transactionService = context.getBean(TransactionService.class);
        transactionService.testSingle();
        transactionService.testMoreExec();
        transactionService.testAnno(new String[]{"celia", "jones"});

        // TODO: 使用下面的方式由于是在ThreadLocal 中 所以后面不能使用多线程查询 否则会造成线程中获取不到路由
        // ChoiceHelper.choice("only");
        //
        //     IntStream.rangeClosed(0, 10).parallel().forEach(integer -> {
        //         List<WemallWxShareArticle> selectList = bean.selectList(params);
        //         final String chooser = ChoiceHelper.chooser(true);
        //         final String chooser1 = ChoiceHelper.chooser(false);
        //         logger.warn("===============>{}={}",chooser,chooser1);
        //     });
```



**具体参考[github](https://github.com/itsocoo/multidatasource-jdbcscan-starter)代码**




