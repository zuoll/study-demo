package com.zll.jpa;

import com.zll.jpa.entity.TestBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 *
 */
@SpringBootApplication
public class JpaDemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(JpaDemoApplication.class, args);
        JpaDemoApplication jpaDemoApplication = new JpaDemoApplication();
        System.out.println(jpaDemoApplication.testBean());
    }

    /**
     * //TODO 为什么注入不了啊？？？
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "test")
    public TestBean testBean() {
        TestBean testBean = new TestBean();
        System.out.println(testBean);
        return testBean;
    }
}


