package com.dambae200.dambae200.global.common.config.data_source;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Profile("prod")
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        // Replication 적용
        DataSourceType dataSourceType = TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? DataSourceType.SLAVE : DataSourceType.MASTER;
        return dataSourceType;

        // Replication 없이 단일서버
//        return DataSourceType.MASTER;
    }
}