package com.itsocoo.multidatasource.jdbc.scan.test.biz;

import com.itsocoo.multidatasource.jdbc.scan.test.service.MultiTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc
 * @date 2019/4/15 16:07
 */
@Component
public class QueryPageList {
    private static final Logger logger = LoggerFactory.getLogger(QueryPageList.class);

    @Autowired
    private MultiTestService multiTestService;

    public void test() {
        // 1.查询总数
        Integer size = multiTestService.count(getCountMap());
        String site = "jack";
        int pageSize = 10;
        int totalPage = (size + pageSize - 1) / pageSize;
        logger.info("===============>site={},size:{},totalPage={}", site, size, totalPage);

        // 2.查询所有分页数据
        List<Object> jackListPage = IntStream.rangeClosed(1, totalPage).boxed().parallel().flatMap(page -> multiTestService.selectListPage(getQueryMap(site, page, pageSize)).stream()).collect(Collectors.toList());

        logger.info("===============>jackListPage:{}", jackListPage);
    }

    private Map<String, Object> getCountMap() {
        Map<String, Object> johnParams = new HashMap<>();
        johnParams.put("timeStart", "2018-04-09 18:33:22");
        johnParams.put("timeEnd", "2018-04-17 18:33:22");
        johnParams.put("KEY", "john");
        return johnParams;
    }

    private static Map<String, Object> getQueryMap(String site, int currentPage, int pageSize) {
        Map<String, Object> pageParams = new HashMap<>();
        pageParams.put("site", site);
        pageParams.put("offsetSize", (currentPage - 1) * pageSize);
        pageParams.put("pageSize", pageSize);
        pageParams.put("timeStart", "2018-04-09 18:33:22");
        pageParams.put("timeEnd", "2018-04-17 18:33:22");
        pageParams.put("KEY", "jack");

        return pageParams;
    }

}
