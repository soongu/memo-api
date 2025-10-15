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
                echo "----- 1. GitHub에서 소스 코드 가져오기 -----"
                git credentialsId: 'github-credentials', url: 'https://github.com/soongu/memo-api.git', branch: 'main'
            }
        }
        stage('Ultimate Credentials Test') {
            steps {
                echo '----- 자격 증명 시스템 핵심 기능 테스트 시작 -----'
                
                // Docker 플러그인 없이, Jenkins의 가장 기본적인 withCredentials 기능을 사용합니다.
                // 'test-secret' ID를 가진 Secret text 자격 증명을 찾아, 그 값을 MY_SECRET 변수에 담습니다.
                withCredentials([string(credentialsId: 'my-aws-key', variable: 'MY_SECRET')]) {
                    
                    // 만약 이 블록 안으로 성공적으로 들어왔다면, 
                    // Jenkins의 핵심 자격 증명 시스템은 정상이라는 의미입니다.
                    echo 'SUCCESS: "test-secret" 자격 증명을 성공적으로 찾았습니다!'
                    
                    // 실제 비밀 값은 Jenkins가 로그에서 자동으로 마스킹(*****) 처리합니다.
                    sh 'echo "The secret value is: $MY_SECRET"'
                }
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
                script {
                    echo "----- 3. Docker 이미지 빌드 및 ECR에 푸시 (Plugin 사용) -----"
                    
                    def imageTag = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    def ecrRepoUrl = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"

                    // 1. ECR 플러그인을 사용하여 ECR 레지스트리에 로그인합니다.
                    // 'aws-credentials' ID를 가진 자격 증명을 사용합니다.
                    docker.withRegistry("https://"+ecrRepoUrl, 'my-aws-key') {

                        echo 'SUCCESS: aws-credentials를 성공적으로 찾았습니다!'
                        // 2. Docker Pipeline 플러그인을 사용하여 이미지를 빌드합니다.
                        def customImage = docker.build("${IMAGE_REPO_NAME}:${imageTag}", "--platform linux/amd64 .")

                        // 3. ECR에 이미지를 푸시합니다.
                        customImage.push()
                    }
                }
            }
        }
    }
}