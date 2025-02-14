package com.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableCaching  // 캐싱 활성화
@EnableJpaAuditing  // JPA Auditing 활성화 (자동 createdAt, updatedAt 관리)

// SpringBootServletInitializer는 Spring Boot 애플리케이션을 외부 톰캣(Tomcat) 같은 서블릿 컨테이너에서 실행할 수 있도록 도와주는 클래스
public class ECommerceApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ECommerceApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ECommerceApplication.class, args);
        System.out.println("✅ Spring Boot 서버 실행 완료!");
    }
}
