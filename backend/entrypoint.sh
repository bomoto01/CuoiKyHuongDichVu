#!/bin/sh
set -e

echo "[HaUI Library] Khoi dong cac Microservices..."

# 1. Chay 4 domain services o background
java -Xms32m -Xmx96m -XX:+UseSerialGC -Dserver.port=8081 -Dfile.encoding=UTF-8 -jar account.jar > /dev/null 2>&1 &
java -Xms32m -Xmx96m -XX:+UseSerialGC -Dserver.port=8082 -Dfile.encoding=UTF-8 -jar book.jar > /dev/null 2>&1 &
java -Xms32m -Xmx96m -XX:+UseSerialGC -Dserver.port=8083 -Dfile.encoding=UTF-8 -jar customer.jar > /dev/null 2>&1 &
java -Xms32m -Xmx96m -XX:+UseSerialGC -Dserver.port=8084 -Dservices.book-service.url=http://localhost:8082 -Dservices.customer-service.url=http://localhost:8083 -Dfile.encoding=UTF-8 -jar borrow.jar > /dev/null 2>&1 &

echo "[HaUI Library] Cho 5s de cac microservice khoi dong..."
sleep 5

echo "[HaUI Library] Khoi dong Spring Cloud API Gateway tai Port ${PORT:-8080}..."
# 2. Chay Gateway o foreground lam main process
exec java -Xms32m -Xmx96m -XX:+UseSerialGC -Dserver.port=${PORT:-8080} -DACCOUNT_SERVICE_URL=http://localhost:8081 -DBOOK_SERVICE_URL=http://localhost:8082 -DCUSTOMER_SERVICE_URL=http://localhost:8083 -DBORROW_SERVICE_URL=http://localhost:8084 -Dfile.encoding=UTF-8 -jar gateway.jar
