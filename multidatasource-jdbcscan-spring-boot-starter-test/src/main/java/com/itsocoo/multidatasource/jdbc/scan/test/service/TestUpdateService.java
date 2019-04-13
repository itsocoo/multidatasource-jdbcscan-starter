package com.itsocoo.multidatasource.jdbc.scan.test.service;

import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiDataScanApi;
import com.itsocoo.multidatasource.jdbc.scan.annotations.MultiDataScanApiService;
import com.itsocoo.multidatasource.jdbc.scan.enums.ExecuteType;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc
 * @date 2018/11/5 18:07
 */
@Service
@MultiDataScanApiService(name = "messageService")
public interface TestUpdateService {
    @MultiDataScanApi(sql = "UPDATE big_order SET pintuan_status=:pintuanStatus WHERE id =:id AND is_pintuan='pintuan' AND pintuan_status='waiting_for_pintuan' AND status='WattingShipment'", executeType = ExecuteType.UPDATE)
    int updateStatus(Map<String, Object> params);
}
