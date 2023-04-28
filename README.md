## 后端项目结构
```
├── src                         # 源代码目录
│   ├── main                    # 主程序目录
│   │   ├── java                # Java代码目录
│   │   │   ├───common     			# 通用类目录
│   │   │   ├── config          # 应用配置类目录
│   │   │   ├── controller      # 控制器目录
│   │   │   ├── entity          # 实体类目录
│   │   │   ├── mapper          # MyBatis 的映射器层目录，存放基于 MyBatis 的 SQL 映射文件和接口
│   │   │   ├── service         # 服务层目录
│   │   │   ├── util            # 工具类目录
│   │   │   ├── SpringbootApplication.java# 应用启动入口
│   │   │   └── ...
│   │   └── resources           # 资源目录
│   │       ├── application.yml # 应用配置文件
│   │       ├── mapper          # mapper文件目录
│   │       └── ...
│   └── test                     # 测试代码目录
├── .gitignore                   # git忽略文件
├── pom.xml                      # Maven配置文件
└── README.md                    # 项目说明文档
```
后端部分采用Spring Boot框架搭建，通过Maven进行依赖管理和构建。前后端通过接口通信，实现了良好的前后端分离，提高了开发效率和代码可维护性。