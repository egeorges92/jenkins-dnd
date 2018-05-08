#!groovy

import jenkins.model.Jenkins
import jenkins.model.JenkinsLocationConfiguration

def jenkins = Jenkins.getInstance()

// Get environment variables.
def env = System.getenv()

def numExecutors = env.JENKINS_NUM_EXECUTORS ?: 5
def url = env.JENKINS_URL ?: null
def adminEmail = env.JENKINS_ADMIN_EMAIL ?: ''
def systemMessage = env.JENKINS_SYSTEM_MESSAGE ?: ''

// setup executors number
jenkins.setNumExecutors(numExecutors)

// Configure
JenkinsLocationConfiguration location = jenkins.getExtensionList('jenkins.model.JenkinsLocationConfiguration')[0]
if(url!=null) {
	location.url = url
}
location.adminAddress = adminEmail
location.save()

// Update system message
jenkins.systemMessage = systemMessage

// disable usage statistics
jenkins.setNoUsageStatistics(true)

jenkins.save()
