FROM registry.access.redhat.com/ubi8/ubi-minimal

RUN microdnf --nodocs -y install httpd \
  && microdnf clean all

ADD target/kubernetes-secrets-vault-provider-9.0.11.jar /var/www/html

RUN sed -i 's/Listen 80/Listen 8080/' /etc/httpd/conf/httpd.conf \
  && rm /etc/httpd/conf.d/welcome.conf \
  && chgrp -R 0 /var/log/httpd /var/run/httpd \
  && chmod -R g=u /var/log/httpd /var/run/httpd

EXPOSE 8080

USER 1001

CMD httpd -D FOREGROUND
