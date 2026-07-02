package io.github.xcvqqz.cloud_file_storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication              //эта аннотация содержит @ComponentScan, @Configuration (ВСЁ СКАНИРУЕТ)
public class CloudFileStorageRunConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(CloudFileStorageRunConfiguration.class, args);
    }
}
