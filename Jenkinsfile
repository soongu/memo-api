pipeline {
    agent any

    environment {
        AWS_ACCOUNT_ID     = '423458036718'
        AWS_DEFAULT_REGION = 'ap-northeast-2'
        IMAGE_REPO_NAME    = 'my-spring-app'
    }

    stages {
        stage('Checkout') {
            steps {
                echo "----- GitHub에서 소스 코드 가져오기 -----"
                git credentialsId: 'github-credentials', url: 'https://github.com/soongu/memo-api.git', branch: 'main'
            }
        }

        stage('Build & Push Image to ECR') {
            steps {
                script {
                    def imageTag = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()

                    // withCredentials로 AWS 자격증명을 환경변수로 불러옵니다.
                    withCredentials([aws(credentialsId: 'aws-credentials')]) {
                        // 1. ECR에 로그인합니다.
                        sh 'aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com'

                        // 2. Docker 이미지를 빌드합니다.
                        sh "docker build -t ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${imageTag} ."

                        // 3. 빌드된 이미지를 ECR에 푸시합니다.
                        sh "docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${imageTag}"
                    }
                }
            }
        }

    }
}