package com.zl.es;

import com.zl.es.service.EsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.zl.es")
public class SpringNativeEsApplication {
    private static ApplicationContext context;
    public static void main(String[] args) {
        context = SpringApplication.run(SpringNativeEsApplication.class, args);
        EsService esService = context.getBean(EsService.class);
        esService.createData();
        esService.queryDocument();
        esService.updateDocument();
        esService.deleteDocument();
        esService.deleteByQueryAsync();
    }

}
