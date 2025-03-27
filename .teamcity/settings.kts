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
        id("ReactTodoListBuild")
        name = "Build React TodoList"
        description = "Builds the React TodoList application"

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

    // Deployment Configuration
    buildType {
        id("ReactTodoListDeploy")
        name = "Deploy React TodoList"
        description = "Deploys the React TodoList application"

        vcs {
            root(mainVcsRoot)
        }

        steps {
            // Simple deployment step - this would be replaced with actual deployment logic
            script {
                name = "Deploy Application"
                scriptContent = """
                    echo "Deploying React TodoList application..."
                    # This is a placeholder for actual deployment commands
                    # For example, copying files to a web server or deploying to a cloud service
                """
            }
        }

        dependencies {
            snapshot(RelativeId("ReactTodoListBuild")) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }

        features {
            perfmon {
            }
        }
    }
}
