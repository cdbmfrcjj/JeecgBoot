FROM registry.cn-hangzhou.aliyuncs.com/jeecgdocker/mysql:8.0.19

MAINTAINER jeecgos@163.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 按照执行顺序复制SQL文件（数字前缀控制顺序）

COPY ./jeecgboot-mysql-5.7.sql /docker-entrypoint-initdb.d/01_jeecgboot-mysql-5.7.sql

COPY ./gopha-autoapi.sql /docker-entrypoint-initdb.d/02_gopha-autoapi.sql

COPY ./tables_nacos.sql /docker-entrypoint-initdb.d/03_tables_nacos.sql

COPY ./tables_xxl_job.sql /docker-entrypoint-initdb.d/04_tables_xxl_job.sql
