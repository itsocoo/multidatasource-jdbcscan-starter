package com.itsocoo.multidatasource.jdbc.scan.test.service;



import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiDataScanApi;
import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiDataScanApiService;
import com.itsocoo.multidatasource.jdbc.scan.enums.ExecuteType;
import com.itsocoo.multidatasource.jdbc.scan.test.entity.WemallWxShareArticle;

import java.util.List;
import java.util.Map;


/**
 * @author wanghaibo
 * @version V1.0
 * @Description 查询所有其他的品牌库数据
 * @date 2017/8/22 13:55
 */
@MultiDataScanApiService(name = "wemallWxShareArticleServiceMap")
public interface DbWemallWxShareArticleService {

    @MultiDataScanApi(types = WemallWxShareArticle.class, sql = "SELECT :brand AS brand,id, title, pic_url, link_url , tags, sort_by, source, create_time, sub_title, marketing_words FROM wx_share_article WHERE create_time>= :preTodayStart AND create_time<:preTodayEnd", executeType = ExecuteType.SELECT)
    // List<WemallWxShareArticle> selectList(@MultiChoiceParams("key") String key, Map<String, String> params);
    List<WemallWxShareArticle> selectList(Map<String, String> params);
}
