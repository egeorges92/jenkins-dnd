#!groovy

import jenkins.model.Jenkins
import hudson.security.GlobalMatrixAuthorizationStrategy
import hudson.security.Permission

Jenkins jenkins = Jenkins.getInstance()

println('Setup authorization strategy')

// Get environment variables.
def env = System.getenv()

GlobalMatrixAuthorizationStrategy strategy = new GlobalMatrixAuthorizationStrategy()

// Setting groups permissions

//  Slave Permissions
strategy.add(hudson.model.Computer.BUILD,'authenticated')
strategy.add(hudson.model.Computer.CONFIGURE,'authenticated')
strategy.add(hudson.model.Computer.CONNECT,'slave')
strategy.add(hudson.model.Computer.CREATE,'authenticated')
strategy.add(hudson.model.Computer.DELETE,'authenticated')
strategy.add(hudson.model.Computer.DISCONNECT,'slave')

//  Credential Permissions
strategy.add(com.cloudbees.plugins.credentials.CredentialsProvider.CREATE,'authenticated')
strategy.add(com.cloudbees.plugins.credentials.CredentialsProvider.DELETE,'authenticated')
strategy.add(com.cloudbees.plugins.credentials.CredentialsProvider.MANAGE_DOMAINS,'authenticated')
strategy.add(com.cloudbees.plugins.credentials.CredentialsProvider.UPDATE,'authenticated')
strategy.add(com.cloudbees.plugins.credentials.CredentialsProvider.VIEW,'authenticated')

//  Overall Permissions
strategy.add(hudson.model.Hudson.ADMINISTER,'admin')
strategy.add(hudson.model.Hudson.READ,'authenticated')

//  Job Permissions
strategy.add(hudson.model.Item.BUILD,'authenticated')
strategy.add(hudson.model.Item.CANCEL,'authenticated')
strategy.add(hudson.model.Item.CONFIGURE,'authenticated')
strategy.add(hudson.model.Item.CREATE,'authenticated')
strategy.add(hudson.model.Item.DELETE,'authenticated')
strategy.add(hudson.model.Item.DISCOVER,'authenticated')
strategy.add(Permission.fromId("hudson.model.Item.Move"),'authenticated')
strategy.add(hudson.model.Item.READ,'authenticated')
strategy.add(hudson.model.Item.WORKSPACE,'authenticated')
strategy.add(Permission.fromId("hudson.model.Item.Gitflow"),'authenticated')

//  Run Permissions
strategy.add(hudson.model.Run.DELETE,'authenticated')
strategy.add(Permission.fromId("hudson.model.Run.Replay"),'authenticated')
strategy.add(hudson.model.Run.UPDATE,'authenticated')

//  View Permissions
strategy.add(hudson.model.View.CONFIGURE,'authenticated')
strategy.add(hudson.model.View.CREATE,'authenticated')
strategy.add(hudson.model.View.DELETE,'authenticated')
strategy.add(hudson.model.View.READ,'authenticated')

// SCM Permissions
strategy.add(Permission.fromId("hudson.scm.SCM.Tag"),'authenticated')

jenkins.setAuthorizationStrategy(strategy)
jenkins.save()
