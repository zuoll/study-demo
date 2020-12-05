package com.zll.prop;

import cn.hutool.core.lang.Dict;
import com.zll.prop.config.AppConfig;
import com.zll.prop.config.DeveloperConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * {@code @AllArgsConstructor(onConstructor_ = @Autowired)}
 * 可以给所有的属性注入依赖,而不必每个属性都去@Autowired
 */
@AllArgsConstructor(onConstructor_ = @Autowired)
@SpringBootApplication
@RestController
public class PropApplication {

    private AppConfig appConfig;

    private DeveloperConfig developerConfig;

    public static void main(String[] args) {
        SpringApplication.run(PropApplication.class, args);
    }


    @GetMapping("/get-config")
    public Dict testGetConfig() {
        return Dict.create().set("app_config", appConfig)
                .set("develop_config", developerConfig);
    }

}
