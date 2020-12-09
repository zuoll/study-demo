package com.zll.war;

import cn.hutool.core.lang.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 打包成war 包需要启动器 继承 {@literal SpringBootServletInitializer} 重写 configure 方法
 *
 * @see <a href="https://docs.spring.io/spring-boot/docs/2.1.0.RELEASE/reference/htmlsingle/#howto-create-a-deployable-war-file">官方打war包参考</a>
 * @see SpringBootServletInitializer#configure(SpringApplicationBuilder)
 */
@SpringBootApplication
@RestController
@Slf4j
public class WarDemoApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(WarDemoApplication.class, args);
        log.info("WarDemoApplication 启动成功");
    }

    /**
     * @param application
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WarDemoApplication.class);
    }


    /**
     * @return
     */
    @RequestMapping("/hello-war")
    public Dict helloWar(HttpServletRequest request) {
        log.info("===>path={}", request.getRequestURI());
        return Dict.create().set("msg", "hello war," + LocalDateTime.now());
    }
}

/**
 * 如果运行启动类出现servlet API 找不到就 mvn clean package ,
 * 然后 删除掉edit configurations 的启动了,重新运行启动类就可以了
 */
