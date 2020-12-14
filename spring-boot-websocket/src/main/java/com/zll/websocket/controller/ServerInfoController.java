package com.zll.websocket.controller;

import cn.hutool.core.lang.Dict;
import com.zll.websocket.model.ServerInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerInfoController {

    @GetMapping("/serverInfo")
    public Dict serverInfo() {
        ServerInfo server = new ServerInfo();
        return Dict.create().set("serverInfo", server);
    }
}
