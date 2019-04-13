package com.itsocoo.multidatasource.jdbc.scan.test.service;

import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiDataScanApi;
import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiDataScanApiService;
import com.itsocoo.multidatasource.jdbc.scan.test.entity.WxTempMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc
 * @date 2018/8/15 14:47
 */
@Service
@MultiDataScanApiService(name = "messageService")
public interface MessageService {
    @MultiDataScanApi(types = WxTempMessage.class, sql = "SELECT DISTINCT openid FROM guide_openid c1 RIGHT JOIN shop_guide_bind c2 ON c1.employeeid=c2.employee_id where openid NOTNULL")
    List<WxTempMessage> findAll(Map<String, Object> params);

    @MultiDataScanApi(types = WxTempMessage.class, sql = "SELECT count(*) FROM guide_openid")
    Integer countAll(Map<String, Object> params);
}