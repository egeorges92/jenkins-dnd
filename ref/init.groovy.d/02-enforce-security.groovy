#!groovy

import jenkins.model.Jenkins
import jenkins.security.s2m.AdminWhitelistRule
import hudson.security.csrf.DefaultCrumbIssuer

def jenkins = Jenkins.getInstance()

// Enable different security aspects
println("Enforce security")
// disable CLI
jenkins.getDescriptor("jenkins.CLI").get().setEnabled(false)
// enable CRUMB
jenkins.setCrumbIssuer(new DefaultCrumbIssuer(true))
jenkins.injector.getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false);

jenkins.save()