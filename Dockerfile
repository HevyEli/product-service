FROM gitlab.services.itc.st.sk:4567/itc/containerstorage/openjdk:17.0
MAINTAINER "mcihal1.elias@t-mobile.cz"
LABEL application="prodict service"


ENV PORT=8080
ENV JAVA_OPT="-Dfile.encoding=UTF-8 -XX:+UseSerialGC -Xss512k -XX:MaxRAM=156m"
ENV CERT_OUTPUT_DIR=/app/certs

ARG TARGET=target
RUN set -ex \
    && mkdir -p /app/code \
    && mkdir -p /app/logs \
    && mkdir -p /app/certs \
    && useradd -m -s /bin/bash -d /app/code product-svc

WORKDIR /app/code

COPY ${TARGET}/*.jar ./app.jar
COPY app.sh ./


RUN chown -R product-svc /app \
    && chmod 744 * /app

EXPOSE 8080

USER product-svc
ENTRYPOINT ["./app.sh"]
