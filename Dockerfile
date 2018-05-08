FROM jenkins/jenkins:lts-alpine

USER root

# Install docker in Docker
USER root

RUN apk --update --no-cache add docker
# Setup Jenkins
RUN echo "jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers
RUN apk add --update shadow \
    && groupadd -g 50 staff \
    && usermod -a -G staff jenkins

USER jenkins

# Copy setup groovy scripts
COPY ./ref/ /usr/share/jenkins/ref/

# Install plugins
RUN xargs /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

# tell Jenkins that no banner prompt for pipeline plugins is needed 
# see: https://github.com/jenkinsci/docker#preinstalling-plugins 
RUN echo 2.0 > /usr/share/jenkins/ref/jenkins.install.UpgradeWizard.state 
