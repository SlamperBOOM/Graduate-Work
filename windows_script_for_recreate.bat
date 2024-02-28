@echo off
if [%1] == [] echo Error: provide docker-compose filename && exit
docker-compose down --rmi all --volumes
docker-compose -f %1 up -d --build