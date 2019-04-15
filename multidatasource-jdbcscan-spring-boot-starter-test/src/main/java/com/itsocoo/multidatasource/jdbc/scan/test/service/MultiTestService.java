package com.itsocoo.multidatasource.jdbc.scan.test.service;


import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiChoiceParams;
import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiDataScanApi;
import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiDataScanApiService;
import com.itsocoo.multidatasource.jdbc.scan.enums.ExecuteType;
import com.itsocoo.multidatasource.jdbc.scan.test.entity.MultiTest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * @author wanghaibo
 * @version V1.0
 * @Description 查询所有其他的品牌库数据
 * @date 2017/8/22 13:55
 */
@Service
@MultiDataScanApiService(name = "multiTestService")
public interface MultiTestService {

    @MultiDataScanApi(types = MultiTest.class, sql = "SELECT id,`trigger`,age,username,password,last_name,content,end_name,status,create_time,last_update_time,amt,price,brand FROM multi_test WHERE create_time>=:timeStart AND create_time<:timeEnd", executeType = ExecuteType.SELECT)
    List<MultiTest> selectList(Map<String, Object> params);

    @MultiDataScanApi(types = MultiTest.class, sql = "SELECT id,`trigger`,age,username,password,last_name,content,end_name,status,create_time,last_update_time,amt,price,brand FROM multi_test WHERE create_time>=:timeStart AND create_time<:timeEnd", executeType = ExecuteType.SELECT)
    List<MultiTest> selectList(@MultiChoiceParams("key") String key, Map<String, Object> params);

    @MultiDataScanApi(types = MultiTest.class, sql = "SELECT :site AS site,id,`trigger`,age,username,password,last_name,content,end_name,status,create_time,last_update_time,amt,price,brand FROM multi_test WHERE create_time>= :timeStart AND create_time<:timeEnd LIMIT :pageSize OFFSET :offsetSize")
    List<MultiTest> selectListPage(Map<String, Object> params);

    @MultiDataScanApi(sql = "SELECT COUNT(*) AS num FROM multi_test WHERE create_time>= :timeStart AND create_time<:timeEnd")
    Integer count(Map<String, Object> params);

    @MultiDataScanApi(sql = "UPDATE multi_test SET username=:username WHERE id =:id", executeType = ExecuteType.UPDATE)
    int updateStatus(Map<String, Object> params);

    @MultiDataScanApi(sql = "UPDATE multi_test SET username=:username WHERE id =:id", executeType = ExecuteType.UPDATE, transaction = true)
    int updateSingle(@MultiChoiceParams("key") String key,Map<String, Object> params);

}
