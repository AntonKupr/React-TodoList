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
*/

version = "2019.2"

project {
    description = "React TodoList Application"

    // Define VCS Root
    val vcsRoot = GitVcsRoot {
        id("ReactTodoListVcs")
        name = "React TodoList VCS"
        url = "https://github.com/yourusername/React-TodoList.git" // Replace with actual repository URL
        branch = "refs/heads/master"
        branchSpec = "+:refs/heads/*"
    }
    vcsRoot(vcsRoot)

    // Build Configuration
    buildType {
        id("ReactTodoListBuild")
        name = "Build"

        vcs {
            root(vcsRoot)
        }

        // Build Steps
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
                name = "Build"
                scriptContent = "npm run build"
            }
        }

        // Triggers
        triggers {
            vcs {
                branchFilter = "+:*"
            }
        }

        // Features
        features {
            perfmon {
            }
        }
    }

    // Deployment Configuration
    buildType {
        id("ReactTodoListDeploy")
        name = "Deploy"

        vcs {
            root(vcsRoot)
        }

        // Build Steps
        steps {
            // Install dependencies
            script {
                name = "Install Dependencies"
                scriptContent = "npm install"
            }

            // Build the application
            script {
                name = "Build"
                scriptContent = "npm run build"
            }

            // Deploy the application (generic example)
            script {
                name = "Deploy"
                scriptContent = """
                    echo "Deploying React TodoList application..."
                    # Copy build files to deployment server or service
                    # This is a placeholder - replace with actual deployment commands
                    echo "Deployment completed successfully"
                """.trimIndent()
            }
        }

        // Dependencies
        dependencies {
            snapshot(RelativeId("ReactTodoListBuild")) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }
}