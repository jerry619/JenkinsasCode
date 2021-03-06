FROM jenkins/jenkins:lts
MAINTAINER Jerry
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
COPY auth/* /auth/ 
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
COPY groovy/* /usr/share/jenkins/ref/init.groovy.d/
