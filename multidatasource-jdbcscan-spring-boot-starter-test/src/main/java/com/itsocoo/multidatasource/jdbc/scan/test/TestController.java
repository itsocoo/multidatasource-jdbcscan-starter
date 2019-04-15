package com.itsocoo.multidatasource.jdbc.scan.test;

import com.itsocoo.multidatasource.jdbc.scan.test.service.MultiTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc
 * @date 2019/4/15 16:55
 */
@Service
public class TestController {

    @Autowired
    private MultiTestService multiTestService;

    public void test() throws IOException {
        // 设置需要的路由即可 有三种方式 具体看代码
        multiTestService.selectListPage(null);
    }
}
