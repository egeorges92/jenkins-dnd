#!groovy

import hudson.util.Secret
import jenkins.model.Jenkins
import jenkins.model.IdStrategy

import hudson.security.LDAPSecurityRealm
import hudson.security.SecurityRealm
import hudson.security.HudsonPrivateSecurityRealm

import jenkins.security.plugins.ldap.LDAPGroupMembershipStrategy
import jenkins.security.plugins.ldap.FromGroupSearchLDAPGroupMembershipStrategy

Jenkins jenkins = Jenkins.instance

// Get environment variables.
def env = System.getenv()

// setup realm
SecurityRealm hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount("admin", "admin")
hudsonRealm.createAccount("slave", "slave")
jenkins.setSecurityRealm(hudsonRealm)

jenkins.save()