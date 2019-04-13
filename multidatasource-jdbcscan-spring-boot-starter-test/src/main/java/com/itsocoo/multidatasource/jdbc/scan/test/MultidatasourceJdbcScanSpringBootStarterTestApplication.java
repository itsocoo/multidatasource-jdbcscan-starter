package com.itsocoo.multidatasource.jdbc.scan.test;

import com.itsocoo.multidatasource.jdbc.scan.test.service.DbWemallWxShareArticleService;
import com.itsocoo.multidatasource.jdbc.scan.test.service.TestMulitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
// @EnableMultiDataApiScan(basePackages = "com.lzsz.multidatasource.jdbc.scan.test.service", jdbcTemplateRef = "jdbcTemplateMap")
public class MultidatasourceJdbcScanSpringBootStarterTestApplication {
    private static final Logger logger = LoggerFactory.getLogger(MultidatasourceJdbcScanSpringBootStarterTestApplication.class);

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(MultidatasourceJdbcScanSpringBootStarterTestApplication.class, args);

        DbWemallWxShareArticleService bean = context.getBean(DbWemallWxShareArticleService.class);
        //
        // Map<String, String> params = new LinkedHashMap<>();
        // params.put("preTodayEnd", "2018-09-06 23:59:59");
        // params.put("preTodayStart", "2010-06-29 23:59:59");
        // params.put("brand", "only");
        // // params.put("KEY", "only");
        // ChoiceHelper.choice("only");
        // // params.put(SelectDataSourceType.KEY.name, SelectDataSourceType.ONLY.name);
        // List<WemallWxShareArticle> selectList = bean.selectList(params);
        // logger.info("===============>selectList:{}",selectList);

        // new Thread(() -> {
        //     ChoiceHelper.choice("only",true);
        //
        //     IntStream.rangeClosed(0, 10).parallel().forEach(integer -> {
        //         List<WemallWxShareArticle> selectList = bean.selectList(params);
        //         final String chooser = ChoiceHelper.chooser(true);
        //         final String chooser1 = ChoiceHelper.chooser(false);
        //         logger.warn("===============>{}={}",chooser,chooser1);
        //     });
        // }).start();
        //
        // new Thread(() -> {
        //     ChoiceHelper.choice("jackjones",false);
        //     List<WemallWxShareArticle> selectList = bean.selectList(params);
        //     final String chooser = ChoiceHelper.chooser(true);
        //     final String chooser1 = ChoiceHelper.chooser(false);
        //     logger.warn("+++++++++++++++++>{}={}",chooser,chooser1);
        // }).start();
        //
        // new Thread(() -> {
        //     ChoiceHelper.choice("fol",true);
        //
        //     IntStream.rangeClosed(0, 10).parallel().forEach(integer -> {
        //         List<WemallWxShareArticle> selectList = bean.selectList(params);
        //         final String chooser = ChoiceHelper.chooser(true);
        //         final String chooser1 = ChoiceHelper.chooser(false);
        //         logger.warn("###################>{}={}",chooser,chooser1);
        //     });
        // }).start();

        // MessageService bean = context.getBean(MessageService.class);
        // Map<String, Object> params = new LinkedHashMap<>();
        // params.put("brand", "only");
        // params.put(SelectDataSourceType.KEY.name, SelectDataSourceType.ONLY.name);
        // List<WxTempMessage> list = bean.findAll(params);
        // logger.info("===============>selectList:{}", list);
        // Integer countAll = bean.countAll(params);
        // logger.info("===============>countAll:{}", countAll);

        // TestUpdateService bean = context.getBean(TestUpdateService.class);
        // Map<String, Object> params = new LinkedHashMap<>();
        // params.put("pintuanStatus", "pintuan_success");
        // params.put("id", 1726757L);
        // // params.put(SelectDataSourceType.KEY.jdbcTemplateName, SelectDataSourceType.getSelectJdbcTemplateNameType("only"));
        // int i = bean.updateStatus(params);
        //
        // System.out.println(i);
        // System.out.println(params);

        // error();

        // ChoiceHelper.setName("selected");
        //
        // int a = bean.updateStatus(params);

        // System.out.println(a);

        // ChoiceHelper.choice("fol");

        TestMulitService service = context.getBean(TestMulitService.class);
        service.test(new String[]{"only", "fol"} );
        // service.test2();
    }

    private static void error() {
        throw new RuntimeException("1111");
    }


}
