pipeline{
    agent any
    tools{
        maven "maven"
    }
    stages{
        stage("Build JAR File"){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Skyrdow/tingeso-pep1-backend']])
                    bat "mvn clean install"
            }
        }
        stage("Test"){
            steps{
                    bat "mvn test"
            }
        }        
        stage("Build and Push Docker Image"){
            steps{
                    script{
                         withDockerRegistry(credentialsId: 'docker-credentials'){
                            bat "docker build -t skyrdow/autofix-backend ."
                            bat "docker push skyrdow/autofix-backend"
                        }
                    }
            }
        }
    }
}