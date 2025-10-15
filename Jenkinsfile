pipeline {
    agent any

    // 파이프라인 전체에서 사용할 변수를 정의합니다.
    environment {
        AWS_ACCOUNT_ID = '423458036718'
        AWS_DEFAULT_REGION = 'ap-northeast-2'
        IMAGE_REPO_NAME = 'my-spring-app'
        IMAGE_TAG = '' // 아래에서 동적으로 채워질 예정
    }

    stages {
        stage('Checkout') {
            steps {
                echo "----- 1. GitHub에서 소스 코드 가져오기 -----"
                git credentialsId: 'github-credentials', url: 'https://github.com/soongu/memo-api.git', branch: 'main'
            }
        }

        stage('Build') {
            steps {
                echo "----- 2. Gradle로 프로젝트 빌드하기 -----"
                sh './gradlew clean build'
            }
        }

        stage('Build & Push Image to ECR') {
            steps {
                echo "----- 3. Docker 이미지 빌드 및 ECR에 푸시 -----"
                script {
                    // Git 커밋 해시의 앞 7자리를 이미지 태그로 사용합니다. (버전 관리)
                    def IMAGE_TAG = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()

                    // withCredentials 블록 안에서만 AWS 자격증명을 사용할 수 있습니다.
                    withCredentials([aws(credentialsId: 'aws-credentials')]) {
                        // 1. ECR에 로그인합니다.
                        sh '/usr/bin/aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com'

                        // 2. Docker 이미지를 빌드합니다. (ARM Mac -> x86 EKS 호환 이미지)
                        sh 'docker buildx build --platform linux/amd64 --tag ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${IMAGE_TAG} .'

                        // 3. 빌드된 이미지를 ECR에 푸시합니다.
                        sh 'docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${IMAGE_TAG}'
                    }
                }
            }
        }
    }
}