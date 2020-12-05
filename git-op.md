### 1,创建工程之后，用终端进入到工程的根目录下
```$xslt
D:\ws-20200108\ratelimit-demo>
```



### 2，建立本地仓库
```$xslt
git init
```

### 3，将本地项目工作区的所有文件添加到暂存区(排除maven 的target 的文件 和 IDEA 工程特定文件 .git .idea **.iml)
```$xslt
git add  .
```

### 4，将暂存区的文件提交到本都仓库
```$xslt
git commit -m "工程初始化"
```

### 5，将本地仓库和远程的github 仓库关联起来
```$xslt
git remote add  https://github.com/zuoll/study-demo.git
```


### 6，将本地仓库上传到远程的仓库，刷新github 仓库，既可以看到上传成功

*  (不加这句可能报错出现错误的主要原因是github中的README.md文件不在本地代码目录中)
```$xslt
git pull --rebase origin master 
```

*  (需要填写账号、密码时候，自己填写。通常一次通过之后就不需要了。)
```$xslt
git push -u origin master 
```


### 一些常用的命令
##### 查看代码的修改状态
```$xslt
git status
```

##### 查看代码修改的内容
```$xslt
git diff <文件名称>
```

##### 提交暂存的文件
```
git add <文件名称>
```

##### 删除暂存的文件
```$xslt
git rm -r <文件名称>

```

##### 提交暂存的文件
```$xslt
git commit -m "提交的信息"
```

##### 同步远程的代码到本地
```$xslt
git pull

```

##### 推送本地代码到远程仓库
```$xslt
git push origin master
```

##### 切换分支
```$xslt
git checkout -b <分支名称>

```






