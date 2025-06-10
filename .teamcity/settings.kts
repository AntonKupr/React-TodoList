import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script

version = "2019.2"

project {
    description = "React TodoList Application"

    // Define Main VCS Root
    val mainVcsRoot = DslContext.settingsRoot

    // Build Configuration
    buildType {
        id("Build")
        name = "Build"
        description = "Build React TodoList application"

        vcs {
            root(mainVcsRoot)
        }

        steps {
            // Install dependencies
            script {
                name = "Install Dependencies"
                scriptContent = "npm install"
            }

            // Run tests
            script {
                name = "Run Tests"
                scriptContent = "npm test"
            }

            // Build application
            script {
                name = "Build Application"
                scriptContent = "npm run build"
            }
        }

        triggers {
            vcs {
                branchFilter = "+:*"
            }
        }

        features {
            perfmon {
            }
        }

        requirements {
            exists("env.NODE_JS_HOME")
        }
    }

    // Deploy Configuration
    buildType {
        id("Deploy")
        name = "Deploy"
        description = "Deploy React TodoList application"

        vcs {
            root(mainVcsRoot)
        }

        steps {
            // Install dependencies
            script {
                name = "Install Dependencies"
                scriptContent = "npm install"
            }

            // Build application
            script {
                name = "Build Application"
                scriptContent = "npm run build"
            }

            // Deploy application (example - copy to a deployment directory)
            script {
                name = "Deploy Application"
                scriptContent = """
                    echo "Deploying application..."
                    # This is a placeholder for actual deployment commands
                    # For example, copying to a web server directory or uploading to a cloud service
                    mkdir -p /tmp/deploy
                    cp -R build/* /tmp/deploy/
                    echo "Application deployed to /tmp/deploy"
                """.trimIndent()
            }
        }

        dependencies {
            snapshot(RelativeId("Build")) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }

        requirements {
            exists("env.NODE_JS_HOME")
        }
    }
}
