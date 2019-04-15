package com.itsocoo.multidatasource.jdbc.scan.test.biz;

import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiTranParams;
import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiTransactional;
import com.itsocoo.multidatasource.jdbc.scan.common.ChoiceHelper;
import com.itsocoo.multidatasource.jdbc.scan.test.service.MultiTestService;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc 测试事务
 * @date 2019/4/8 10:34
 */
@Service
public class TransactionService {

    @Autowired
    private MultiTestService multiTestService;

    // 对于一个库的查询可以设置   @MultiDataScanApi(transaction = true)
    public void testSingle(){
        Map<String, Object> params = new HashMap<>();
        params.put("username", "marcar");
        params.put("id", 1);
        multiTestService.updateSingle("chris", params);
    }

    // 对于多个库的多个事务执行可以设置 方式一：transactionManager = {"chris","barry"}
    @MultiTransactional(transactionManager = {"chris","barry"})
    public void testMoreExec() {

        ChoiceHelper.choice("chris");
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("username", "marcar1");
        params.put("id", 1);
        int i = multiTestService.updateStatus(params);

        System.out.println(i);

        // TODO 测试失败回滚
        // if ("error1".contains("error1")) {
        //     throw new RuntimeException("Error1");
        // }

        ChoiceHelper.choice("barry");
        Map<String, Object> params2 = new LinkedHashMap<>();
        params2.put("username", "marcar2");
        params2.put("id", 1);
        int i2 = multiTestService.updateStatus(params2);

        System.out.println(i2);
    }

    // 对于多个库的多个事务执行可以设置 方式二：@MultiTranParams String[] choice
    @MultiTransactional
    public void testAnno(@MultiTranParams String[] choice) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("username", "marcar3");
        params.put("id", 1);
        params.put("KEY", choice[0]);
        int i = multiTestService.updateStatus(params);

        System.out.println(i);

        // TODO 测试失败回滚
        // if ("error1".contains("error1")) {
        //     throw new RuntimeException("Error1");
        // }

        Map<String, Object> params2 = new LinkedHashMap<>();
        params2.put("username", "marcar4");
        params2.put("id", 1);
        params.put("KEY", choice[1]);
        int i2 = multiTestService.updateStatus(params2);

        System.out.println(i2);
    }

}
