def env = System.getenv()

pipelineJob('Docker Image Builder') {
    displayName('Docker Image Builder')
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(env['CONFIGURATION_REPOSITORY_URL'])
                        credentials('git-token-credentials')
                    }
                    branch(env['CONFIGURATION_REPOSITORY_BRANCH'])
                }
                scriptPath('jenkins/jobs/pipelines/jenkinsfile.image-builder')
            }
        }
    }
    parameters {
        stringParam('REGISTRY_URL', env['CONTAINER_REGISTRY_URL'], 'The container registry URL.')
        stringParam('IMAGE_NAME', 'nginx/nginx-unprivileged', 'The image name.')
        stringParam('IMAGE_TAG', '', 'The image tag name.')
    }
    properties {
        githubProjectUrl('https://github.com/browol/devops')
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
            regexpFilterExpression("true ${env.CONFIGURATION_REPOSITORY_BRANCH}")
        }
    }
}
