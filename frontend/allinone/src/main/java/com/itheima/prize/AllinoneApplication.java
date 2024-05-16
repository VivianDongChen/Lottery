package com.itheima.prize;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.itheima.prize.commons.db.mapper")
@EnableCaching
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = -1)
@EnableScheduling
public class AllinoneApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AllinoneApplication.class).run(args);
    }




}