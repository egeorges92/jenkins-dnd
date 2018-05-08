#!groovy

import jenkins.model.Jenkins

Jenkins jenkins = Jenkins.getInstance()

println("Setup agent protocols : avoid deprecated protocols")

// avoid deprecated agent protocols
Set<String> agentProtocolsList = ['JNLP4-connect', 'Ping']
jenkins.setAgentProtocols(agentProtocolsList)

jenkins.save()