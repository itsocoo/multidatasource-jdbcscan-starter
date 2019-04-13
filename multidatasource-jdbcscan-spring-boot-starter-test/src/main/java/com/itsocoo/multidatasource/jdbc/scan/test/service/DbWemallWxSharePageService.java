package com.itsocoo.multidatasource.jdbc.scan.test.service;

import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiDataScanApi;
import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiDataScanApiService;
import com.itsocoo.multidatasource.jdbc.scan.test.entity.WemallWxSharePage;

import java.util.List;
import java.util.Map;


/**
 * @author wanghaibo
 * @version V1.0
 * @Description 查询所有其他的品牌库数据
 * @date 2018/3/22 13:55
 */
@MultiDataScanApiService
public interface DbWemallWxSharePageService{

    @MultiDataScanApi(types = WemallWxSharePage.class, sql = "SELECT ? AS brand,id,page_url AS pageUrl,page_title AS pageTitle,create_by_openid AS createByOpenid,create_time AS createTime,share_count AS shareCount,open_count AS openCount,portrait,status,platform,cover_pic AS coverPic,region,type,template_id AS templateId,nickname,wechat_version AS wechatVersion,pictures FROM wx_share_page WHERE create_time>= ? AND create_time<?")
    List<WemallWxSharePage> selectList(Map<String, String> params);
}
