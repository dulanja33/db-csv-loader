package com.dulanja33.dcl;

import com.dulanja33.dcl.exception.DclException;
import com.dulanja33.dcl.properties.Context;
import com.dulanja33.dcl.service.DBCsvLoaderService;
import com.dulanja33.dcl.util.CliUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DbCsvLoaderApplication implements CommandLineRunner {

    final private DBCsvLoaderService dbCsvLoaderService;

    public DbCsvLoaderApplication(DBCsvLoaderService dbCsvLoaderService) {
        this.dbCsvLoaderService = dbCsvLoaderService;
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DbCsvLoaderApplication.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Context context = CliUtil.cliOptionHandle(args);
            dbCsvLoaderService.loadData(context);
        } catch (DclException e) {
            log.error(e.getMessage());
        }
    }
}
