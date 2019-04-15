package com.itsocoo.multidatasource.jdbc.scan.test.biz;

import com.itsocoo.multidatasource.jdbc.scan.test.entity.MultiTest;
import com.itsocoo.multidatasource.jdbc.scan.test.service.MultiTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc
 * @date 2019/4/15 16:07
 */
@Component
public class Query2Way {
    private static final Logger logger = LoggerFactory.getLogger(Query2Way.class);

    @Autowired
    private MultiTestService multiTestService;

    public void test() {
        // 2.方式 使用@MultiChoiceParams("key") String key设置
        Map<String, Object> params = new HashMap<>();
        params.put("timeStart", "2018-04-09 18:33:22");
        params.put("timeEnd", "2018-04-17 18:33:22");

        List<MultiTest> cindyTests = multiTestService.selectList("cindy", params);
        logger.info("===============>cindyTests:{}", cindyTests);
    }
}
