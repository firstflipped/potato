FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.9_11
# dockerfile维护人员
MAINTAINER firstflipped@163.com
#定义标签
LABEL version=0.0.1
 #定义tomcat
ENV TOMCAT_APP apache-tomcat-9.0.80
 #定义tomcat工作目录
ENV WORKDIR /opt/tomcat
# tomcat启动会加载这个环境变量
ENV JAVA_OPTS "-Xmx2048m -Xms4096m -Xmn1024m -Xss2048m -XX:+HeapDumpOnOutOfMemoryError  -XX:HeapDumpPath=$WORKDIR/error -Duser.timezone=Asia/Shanghai"
#挂载error目录
VOLUME /opt/tomcat/error
# 配置时区
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories \
        && apk update \
        && apk add --no-cache tzdata \
        && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
 # 设置时区
ENV TZ Asia/Shanghai
# 设置工作目录
WORKDIR $WORKDIR
# 将tomcat拷贝到工作目录
COPY $TOMCAT_APP .
COPY geoserver.war ./$TOMCAT_APP/webapps/
# 暴露端口
EXPOSE 8080
# 启动tomcat
CMD $WORKDIR/bin/catalina.sh run