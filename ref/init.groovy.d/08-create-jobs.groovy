#!groovy
// https://stackoverflow.com/questions/16963309/how-create-and-configure-a-new-jenkins-job-using-groovy

println( "Create simple pipeline job.")
def simplePipelineJobDSL="""
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Build'
                sleep 10
            }
        }
        stage('Q/A') {
            steps {
                echo 'Q/A'
                sleep 10
            }
        }
        stage('Publish') {
            steps {
                echo 'Publish'
                sleep 10
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploy'
                sleep 10
            }
        }
    }
}
""";
PipelineUtils.createPipelineJob("simple-pipeline", simplePipelineJobDSL);
println( "Create docker pipeline job.");

def dockerPipelineJobDSL="""
pipeline {
    agent any
    stages {
        stage('Back-end') {
            agent {
                docker { image 'maven:3-alpine' }
            }
            steps {
                sh 'mvn --version'
            }
        }
        stage('Front-end') {
            agent {
                docker { image 'node:7-alpine' }
            }
            steps {
                sh 'node --version'
            }
        }
    }
}
""";
PipelineUtils.createPipelineJob("docker-pipeline", dockerPipelineJobDSL)

println( "Create multibranch pipeline job.");
PipelineUtils.createMultibranchPipelineJob('jenkins-multi-branch-pipeline', 'https://github.com/egeorges92/jenkins-multi-branch-pipeline.git');

class PipelineUtils {
    public static createPipelineJob(String name, String jobDSL) {
        //http://javadoc.jenkins.io/plugin/workflow-cps/index.html?org/jenkinsci/plugins/workflow/cps/CpsFlowDefinition.html
        def flowDefinition = new org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition(jobDSL, true);
        //http://javadoc.jenkins.io/jenkins/model/Jenkins.html
        def jenkins = jenkins.model.Jenkins.getInstance();
        //http://javadoc.jenkins.io/plugin/workflow-job/org/jenkinsci/plugins/workflow/job/WorkflowJob.html
        def job = new org.jenkinsci.plugins.workflow.job.WorkflowJob(jenkins, name)
        job.definition = flowDefinition

        job.setConcurrentBuild(false);

        //http://javadoc.jenkins-ci.org/hudson/tasks/LogRotator.html
        job.addProperty( new jenkins.model.BuildDiscarderProperty( new hudson.tasks.LogRotator(-1, 1, -1, 1) ));

        //http://javadoc.jenkins.io/plugin/branch-api/jenkins/branch/RateLimitBranchProperty.html
        //job.addProperty( new jenkins.branch.RateLimitBranchProperty.JobPropertyImpl(new jenkins.branch.RateLimitBranchProperty.Throttle (60,"hours",false)));
        hudson.triggers.TimerTrigger newCron = new hudson.triggers.TimerTrigger("@hourly");
        newCron.start(job, true);
        job.addTrigger(newCron);
        job.save();

        jenkins.reload();
    }
    
    public static createMultibranchPipelineJob(String name, String gitUrl) {
        jenkins.model.Jenkins jenkins = jenkins.model.Jenkins.getInstance();
        org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject mp = jenkins.createProject(org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject.class, name);
        mp.getSourcesList().add(new jenkins.branch.BranchSource(new jenkins.plugins.git.GitSCMSource(null, gitUrl, "", "*", "", false), new jenkins.branch.DefaultBranchPropertyStrategy(new jenkins.branch.BranchProperty[0])));
        org.jenkinsci.plugins.workflow.multibranch.WorkflowBranchProjectFactory projectFactory = new org.jenkinsci.plugins.workflow.multibranch.WorkflowBranchProjectFactory();
        projectFactory.setScriptPath("Jenkinsfile");
        mp.setProjectFactory(projectFactory);
        com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger newCron = new com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger("1h");
        newCron.start(mp, true);
        mp.addTrigger(newCron);
        mp.save();
        mp.scheduleBuild2(0, new hudson.model.CauseAction(new hudson.model.Cause.UserIdCause()));
    }
}