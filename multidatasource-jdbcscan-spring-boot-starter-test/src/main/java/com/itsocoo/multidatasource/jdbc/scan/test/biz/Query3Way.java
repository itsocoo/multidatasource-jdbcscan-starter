package com.itsocoo.multidatasource.jdbc.scan.test.biz;

import com.itsocoo.multidatasource.jdbc.scan.common.ChoiceHelper;
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
public class Query3Way {
    private static final Logger logger = LoggerFactory.getLogger(Query3Way.class);

    @Autowired
    private MultiTestService multiTestService;

    public void test() {
        // 3.方式 使用params.put("KEY", "john"); KEY为properties中itsocoo.scan.jdbc-template-args-key=KEY
        Map<String, Object> johnParams = new HashMap<>();
        johnParams.put("timeStart", "2018-04-09 18:33:22");
        johnParams.put("timeEnd", "2018-04-17 18:33:22");
        johnParams.put("KEY", "john");
        List<MultiTest> johnTests = multiTestService.selectList(johnParams);
        logger.info("===============>johnTests:{}", johnTests);
    }
}
