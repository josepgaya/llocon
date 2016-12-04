FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD llocon.jar app.jar
ADD truststore.jks truststore.jks
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Djavax.net.ssl.trustStore=truststore.jks -Djavax.net.ssl.trustStorePassword=tecnologies -jar /app.jar" ]
