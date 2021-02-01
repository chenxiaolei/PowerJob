#!/bin/bash
# -p：允许后面跟一个字符串作为提示 -r：保证读入的是原始内容，不会发生任何转义
read -r -p "请输入Dockedr镜像版本:" version
echo "即将构建的 server 镜像：powerjob-server:$version"
read -r -p "任意键继续:"

# 一键部署脚本，请勿挪动脚本
cd `dirname $0`/../.. || exit

read -r -p "是否进行maven构建（y/n）:" needmvn
if [ "$needmvn" = "y" ] || [  "$needmvn" = "Y" ]; then
  echo "================== 构建 jar =================="
  # -U：强制检查snapshot库 -pl：指定需要构建的模块，多模块逗号分割 -am：同时构建依赖模块，一般与pl连用 -Pxxx：指定使用的配置文件
  mvn clean package -Pdev -DskipTests -U -e -pl powerjob-server -am
  echo "================== 拷贝 jar =================="
  /bin/cp -rf powerjob-server/target/*.jar powerjob-server/docker/powerjob-server.jar
  ls -l powerjob-server/docker/powerjob-server.jar
fi


read -r -p "是否重新构建镜像（y/n）:" rebuild
if [ "$rebuild" = "y" ] || [  "$rebuild" = "Y" ]; then
  echo "================== 删除旧镜像 =================="
  docker rmi -f nexus.docker.kefen.site:7300/kefen/powerjob-server:$version
  echo "================== 构建 powerjob-server 镜像 =================="
  docker build -t nexus.docker.kefen.site:7300/kefen/powerjob-server:$version powerjob-server/docker/. || exit

  read -r -p "是否正式发布该镜像（y/n）:" needrelease
  if [ "$needrelease" = "y" ] || [  "$needrelease" = "Y" ]; then
    read -r -p "三思！请确保当前处于已发布的kf分支！（y/n）:" needrelease
    if [ "$needrelease" = "y" ] || [  "$needrelease" = "Y" ]; then
      echo "================== 正在推送 server 镜像到KF私有仓库 =================="
      docker push nexus.docker.kefen.site:7300/kefen/powerjob-server:$version
    fi
  fi
fi