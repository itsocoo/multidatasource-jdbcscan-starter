package com.itsocoo.multidatasource.jdbc.scan.properties;

/**
 * @author wanghaibo
 * @version V1.0
 * @desc
 * @date 2019/4/2 10:47
 */
public class DefaultScanSupportFactory implements ScanSupportFactory {

    private ScanConfigurationProperties scanConfigurationProperties;

    public DefaultScanSupportFactory(ScanConfigurationProperties scanConfigurationProperties) {
        this.scanConfigurationProperties = scanConfigurationProperties;
    }

    public ScanConfigurationProperties getScanConfigurationProperties() {
        return scanConfigurationProperties;
    }

    public void setScanConfigurationProperties(ScanConfigurationProperties scanConfigurationProperties) {
        this.scanConfigurationProperties = scanConfigurationProperties;
    }
}
