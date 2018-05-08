#!groovy

import jenkins.model.Jenkins
import hudson.model.Node
import hudson.model.Slave
import hudson.slaves.DumbSlave
import hudson.slaves.JNLPLauncher
import hudson.slaves.RetentionStrategy
import hudson.slaves.EnvironmentVariablesNodeProperty
import hudson.slaves.EnvironmentVariablesNodeProperty.Entry

Jenkins jenkins = Jenkins.getInstance()

println( "Create single slave.")

// Get environment variables.
def env = System.getenv()

def numExecutors = env.JENKINS_NUM_EXECUTORS ?: 5

// Define a "Permanent Agent"
Slave agent = new DumbSlave(
        "slave-1",
        "/home/jenkins",
        new JNLPLauncher())
agent.setNodeDescription("slave-1")
agent.setNumExecutors(numExecutors)
agent.setLabelString("slave-1")
agent.setMode(Node.Mode.NORMAL)
agent.setRetentionStrategy(new RetentionStrategy.Demand(10, 10))

// List<Entry> env = new ArrayList<Entry>();
// env.add(new Entry("key1","value1"))
// env.add(new Entry("key2","value2"))
// EnvironmentVariablesNodeProperty envPro = new EnvironmentVariablesNodeProperty(env);
// agent.getNodeProperties().add(envPro)

// Create a "Permanent Agent"
jenkins.addNode(agent)

jenkins.save()
