pipelineJob('Nginx Deployment') {
    displayName('Nginx Deployment')
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/browol/ci-cd-pipeline.git')
                        credentials('git-token-credentials')
                    }
                    branch('main')
                }
                scriptPath('jenkins/jobs/pipelines/jenkinsfile.nginx-deployment')
            }
        }
    }
    triggers {
        upstream('Docker Image Builder', 'SUCCESS')
    }
}
