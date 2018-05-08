#!groovy
import jenkins.model.Jenkins
import jenkins.install.InstallUtil
import jenkins.install.InstallState

import java.io.File
import org.apache.commons.io.FileUtils

Jenkins jenkins = Jenkins.getInstance()

// Get environment variables.
def env = System.getenv()

FileUtils.writeStringToFile(new File(jenkins.getRootDir(),"jenkins.install.InstallUtil.lastExecVersion"), env.JENKINS_VERSION)
FileUtils.writeStringToFile(new File(jenkins.getRootDir(),"jenkins.install.UpgradeWizard.state"), env.JENKINS_VERSION)

// TODO add states n other groovy files
//InstallUtil.proceedToNextStateFrom(InstallState.INITIAL_SETUP_COMPLETED);
