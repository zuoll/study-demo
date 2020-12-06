<!doctype html>
<html lang="en">
<#include "../common/head.ftl">
<body>
<div id="app" style="margin: 20px 20%">
    <form action="/freemarker/user/login" method="post">
        用户名:<input type="text" name="username" placeholder="用户名"/><br>
        密码:<input type="password" name="password" placeholder="密码"/><br>
        <input type="submit" value="登录">
    </form>
</div>
</body>
</html>