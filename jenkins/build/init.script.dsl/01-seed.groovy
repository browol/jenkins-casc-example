def env = System.getenv()

String configuration_repository_subfolder = env['CONFIGURATION_REPOSITORY_SUBFOLDER'] ?: 'jenkins'

String title = 'Rebuild jobs only'
String id = title.replace(' ', '_').toLowerCase()
freeStyleJob(id) {
    displayName(title)
    label('built-in')
    scm {
        git {
            remote {
                url(env['CONFIGURATION_REPOSITORY_URL'])
                credentials("git-token-credentials")
            }
            branch(env['CONFIGURATION_REPOSITORY_BRANCH'])
            extensions {
                pathRestriction {
                    includedRegions("${configuration_repository_subfolder}/job-description/.*\\.groovy")
                    excludedRegions('')
                }
            }
        }
    }
    steps {
        dsl {
            external("${configuration_repository_subfolder}/job-description/*.groovy")
            ignoreExisting(false)
            lookupStrategy('JENKINS_ROOT')
            removeAction('DELETE')
            removeAction('DELETE')
        }
    }
}

// execute job on startup
queue(id)
