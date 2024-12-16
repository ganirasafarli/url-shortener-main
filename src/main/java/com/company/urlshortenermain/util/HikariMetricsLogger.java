package com.company.urlshortenermain.util;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HikariMetricsLogger {
    private static final Logger logger = LoggerFactory.getLogger(HikariMetricsLogger.class);

    private final HikariDataSource dataSource;

    @Scheduled(fixedRate = 60000) // Logs every 60 seconds
    public void logHikariPoolMetrics() {
        int totalConnections = dataSource.getHikariPoolMXBean().getTotalConnections();
        int idleConnections = dataSource.getHikariPoolMXBean().getIdleConnections();

        logger.info("HikariCP Pool Metrics - Total Connections: {}, Idle Connections: {}",
                totalConnections, idleConnections);
    }
}
