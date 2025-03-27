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
                scriptContent = "npm test -- --watchAll=false"
            }

            // Build the application
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
    }

    // Deploy Configuration
    buildType {
        id("Deploy")
        name = "Deploy"

        vcs {
            root(mainVcsRoot)
        }

        steps {
            // Install dependencies
            script {
                name = "Install Dependencies"
                scriptContent = "npm install"
            }

            // Build the application
            script {
                name = "Build Application"
                scriptContent = "npm run build"
            }

            // Deploy the application (example - adjust as needed)
            script {
                name = "Deploy Application"
                scriptContent = """
                    echo "Deploying application..."
                    # Add your deployment commands here
                    # For example, copying to a web server or deploying to a cloud service
                    echo "Application deployed successfully"
                """.trimIndent()
            }
        }

        dependencies {
            snapshot(RelativeId("Build")) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }

        features {
            perfmon {
            }
        }
    }
}
