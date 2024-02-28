#!/bin/bash
if [%1] == []; then
	echo Error: provide docker-compose filename 
	exit
fi
docker-compose down --rmi all --volumes
docker-compose -f %1 up -d --build