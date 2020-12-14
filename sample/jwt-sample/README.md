#### 分布式下session，cookie替代方案
> Json Web Token（JWT）：JSON网络令牌，是为了在网络应用环境间传递声明而制定的一种基于JSON的开放标准（(RFC 7519)。JWT是一个轻便的安全跨平台传输格式，定义了一个紧凑的自包含的方式用于通信双方之间以 JSON 对象行使安全的传递信息。因为数字签名的存在，这些信息是可信的。


#####JWT含有三个部分：
* 头部（header）
* 载荷（payload）
* 签证（signature）

> 核心是通过Base64 这种对称加密的方式


> 参考
https://www.jianshu.com/p/576dbf44b2ae

> git jwt demo 
https://github.com/pyygithub/JWT-DEMO

