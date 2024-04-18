pipeline{
    agent any
    stages{
        stage("Build Frontend"){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Skyrdow/tingeso-pep1-frontend']])
                    bat "npm install"
                    bat "npm run build"
                
            }
        }
        stage("Build and Push Docker Image"){
            steps{
                script{
                        withDockerRegistry(credentialsId: 'docker-credentials'){
                        bat "docker build -t skyrdow/autofix-frontend ."
                        bat "docker push skyrdow/autofix-frontend"
                    }
                }   
            }
        }
    }
}