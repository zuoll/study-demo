### spring boot 整合websocket 实现后端主动推送数据给前端， 本示例推送的是服务器的状态信息


### 参考
> 服务器信息采集工具 oshi https://github.com/oshi/oshi

> spring boot 整合websocket 官方文档
https://docs.spring.io/spring/docs/5.1.2.RELEASE/spring-framework-reference/web.html#websocket

> 基于promise用于浏览器和node.js的http客户端
axios.js 用法：https://github.com/axios/axios#example

> STOMP javascript 客户端会使用ws://的URL与STOMP 服务端进行交互。
STOMP即Simple (or Streaming) Text Orientated Messaging Protocol，简单(流)文本定向消息协议，它提供了一个可互操作的连接格式，允许STOMP客户端与任意STOMP消息代理（Broker）进行交互。STOMP协议由于设计简单，易于开发客户端，因此在多种语言和多种平台上得到广泛地应用。
stomp.js 用法：https://github.com/jmesnil/stomp-websocket

> 为了应对许多浏览器不支持WebSocket协议的问题，设计了备选SockJs。
sockjs 用法：https://github.com/sockjs/sockjs-client

> vue.js 语法：https://cn.vuejs.org/v2/guide/
> element-ui 用法：http://element-cn.eleme.io/#/zh-CN


### WebSocket+SockJs+STMOP 三者的关系
> https://www.jianshu.com/p/4ef5004a1c81