package com.itsocoo.multidatasource.jdbc.scan.test;

import com.itsocoo.multidatasource.jdbc.scan.test.biz.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MultiDatasourceJdbcScanTestApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MultiDatasourceJdbcScanTestApplication.class, args);

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
    }

}
