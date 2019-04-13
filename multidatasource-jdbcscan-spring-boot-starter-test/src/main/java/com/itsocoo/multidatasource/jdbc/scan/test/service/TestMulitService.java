package com.itsocoo.multidatasource.jdbc.scan.test.service;

import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiTranParams;
import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiTransactional;
import com.itsocoo.multidatasource.jdbc.scan.common.ChoiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc
 * @date 2019/4/8 10:34
 */
@Service
public class TestMulitService {
    @Autowired
    private TestUpdateService testUpdateService;

    // @MultiTransactional
    // public void test(String[] choice) {
    //     ChoiceHelper.setName(choice[0]);
    //     Map<String, Object> params = new LinkedHashMap<>();
    //     params.put("pintuanStatus", "pintuan_success");
    //     params.put("id", 1726757L);
    //     int i = testUpdateService.updateStatus(params);
    //
    //     System.out.println(i);
    //
    //     if ("error1".contains("error1")) {
    //         throw new RuntimeException("Error1");
    //     }
    //
    //     ChoiceHelper.setName(choice[1]);
    //     Map<String, Object> params2 = new LinkedHashMap<>();
    //     params2.put("pintuanStatus", "pintuan_success");
    //     // params2.put("id", 1726603L);
    //     params2.put("id", 2174154L);
    //     int i2 = testUpdateService.updateStatus(params2);
    //
    //     System.out.println(i2);
    // }

    @MultiTransactional
    public void test(@MultiTranParams String[] choice) {
        ChoiceHelper.choice(choice[0]);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("pintuanStatus", "pintuan_success");
        params.put("id", 1726757L);
        int i = testUpdateService.updateStatus(params);

        System.out.println(i);

        if ("error1".contains("error1")) {
            throw new RuntimeException("Error1");
        }

        ChoiceHelper.choice(choice[1]);
        Map<String, Object> params2 = new LinkedHashMap<>();
        params2.put("pintuanStatus", "pintuan_success");
        // params2.put("id", 1726603L);
        params2.put("id", 2174154L);
        int i2 = testUpdateService.updateStatus(params2);

        System.out.println(i2);
    }

    @MultiTransactional(transactionManager = {"only", "fol"})
    public void test2() {
        ChoiceHelper.choice("only");
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("pintuanStatus", "pintuan_success");
        params.put("id", 1726757L);
        int i = testUpdateService.updateStatus(params);

        System.out.println(i);

        // if ("error1".contains("error1")) {
        //     throw new RuntimeException("Error1");
        // }

        ChoiceHelper.choice("fol");
        Map<String, Object> params2 = new LinkedHashMap<>();
        params2.put("pintuanStatus", "pintuan_success");
        // params2.put("id", 1726603L);
        params2.put("id", 2174154L);
        int i2 = testUpdateService.updateStatus(params2);

        System.out.println(i2);
    }
}
