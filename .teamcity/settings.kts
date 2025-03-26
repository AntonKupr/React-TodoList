import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in IntelliJ IDEA, open the 'TeamCity'
tool window (View -> Tool Windows -> TeamCity), then click the
'Debug' button in the toolbar and select the desired settings file.
*/

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

            // Deploy step (example - adjust as needed)
            script {
                name = "Deploy Application"
                scriptContent = """
                    echo "Deploying application..."
                    # Add your deployment commands here
                    # For example: scp -r build/* user@server:/path/to/deployment
                    echo "Deployment completed"
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
