pipelineJob('Docker Image Builder') {
    displayName('Docker Image Builder')
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/browol/ci-cd-pipeline.git')
                        credentials('git-token-credentials')
                    }
                    branch('jenkins')
                }
                scriptPath('jenkins/jobs/pipelines/jenkinsfile.image-builder')
            }
        }
    }
    parameters {
        stringParam('REGISTRY_URL', 'acrdemo3fh45a.azurecr.io', 'The container registry URL.')
        stringParam('IMAGE_NAME', 'nginx/nginx-unprivileged', 'The image name.')
        stringParam('IMAGE_TAG', '', 'The image tag name.')
    }
    properties {
        githubProjectUrl('https://github.com/browol/ci-cd-pipeline')
    }
    triggers {
        genericTrigger {
            genericVariables {
                genericVariable {
                    key("PULL_REQUEST_MERGED")
                    value("\$.pull_request.merged")
                    expressionType("JSONPath")
                    regexpFilter("")
                    defaultValue("")
                }
                genericVariable {
                    key("PULL_REQUEST_BASE_REF")
                    value("\$.pull_request.base.ref")
                    expressionType("JSONPath")
                    regexpFilter("")
                    defaultValue("")
                }
            }
            tokenCredentialId('webhook-token')
            printContributedVariables(true)
            printPostContent(true)
            silentResponse(false)
            shouldNotFlattern(false)
            regexpFilterText("\$PULL_REQUEST_MERGED \$PULL_REQUEST_BASE_REF")
            regexpFilterExpression("true jenkins")
        }
    }
}
