/**
 * Copyright 2009-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itsocoo.multidatasource.jdbc.scan.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc 扫描的配置文件
 * @date 2019/4/1 16:11
 */
@ConfigurationProperties(prefix = ScanConfigurationProperties.SCAN_PREFIX)
public class ScanConfigurationProperties {

    static final String SCAN_PREFIX = "itsocoo.scan";

    // 所有数据源使用的JdbcTemplate后缀名称
    private String JdbcTemplateSuffix = "JdbcTemplate";

    // 所有数据源使用的DataSourceTransactionManager后缀名称
    private String TransactionManagerSuffix = "PlatformTransactionManager";

    // 选择数据源时候在参数map中被识别的key
    private String JdbcTemplateArgsKey = "KEY";

    public String getJdbcTemplateArgsKey() {
        return JdbcTemplateArgsKey;
    }

    public void setJdbcTemplateArgsKey(String jdbcTemplateArgsKey) {
        JdbcTemplateArgsKey = jdbcTemplateArgsKey;
    }

    public String getJdbcTemplateSuffix() {
        return JdbcTemplateSuffix;
    }

    public void setJdbcTemplateSuffix(String jdbcTemplateSuffix) {
        JdbcTemplateSuffix = jdbcTemplateSuffix;
    }

    public String getTransactionManagerSuffix() {
        return TransactionManagerSuffix;
    }

    public void setTransactionManagerSuffix(String transactionManagerSuffix) {
        TransactionManagerSuffix = transactionManagerSuffix;
    }

    @Override
    public String toString() {
        return "ScanConfigurationProperties{" +
                "JdbcTemplateSuffix='" + JdbcTemplateSuffix + '\'' +
                ", TransactionManagerSuffix='" + TransactionManagerSuffix + '\'' +
                ", JdbcTemplateArgsKey='" + JdbcTemplateArgsKey + '\'' +
                '}';
    }
}
