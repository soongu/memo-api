pipeline {
    agent any

    stages {
        stage('Checkout Source Code') {
            steps {
                echo '----- GitHub에서 소스 코드 가져오는 중 -----'
                // Jenkins가 제공하는 git checkout 기능을 사용합니다.
                git credentialsId: 'github-credentials', url: 'https://github.com/soongu/memo-api.git', branch: 'main'
            }
        }
        stage('Say Hello') {
            steps {
                echo '----- Hello, Jenkinsfile! -----'
            }
        }
        stage('Bye Bye') {
            steps {
                echo '----- Bye, Jenkinsfile! -----'
                ls -al
                cat ./src/main/resources/application.yml
            }
        }
    }
}