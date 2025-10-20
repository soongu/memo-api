pipeline {
    agent any

    environment {
        AWS_ACCOUNT_ID     = '423458036718'
        AWS_DEFAULT_REGION = 'ap-northeast-2'
        IMAGE_REPO_NAME    = 'my-spring-app'

        // EKS Deployment 정보를 변수로 관리하면 편리합니다.
        EKS_CLUSTER_NAME   = 'my-eks-cluster'
        EKS_DEPLOYMENT_NAME= 'memo-api-deployment'
        EKS_CONTAINER_NAME = 'memo-api-container'
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

                    // 이 imageTag를 다른 Stage에서도 사용할 수 있도록 전역 변수에 저장합니다.
                    env.IMAGE_TAG = imageTag 

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

        // 👇 이 아래 Deploy to EKS Stage를 추가합니다.
        stage('Deploy to EKS') {
            steps {
                echo "----- EKS 클러스터에 새로운 버전 배포 -----"
                script {
                    withCredentials([aws(credentialsId: 'my-aws-key')]) {
                        // 1. kubectl이 EKS 클러스터를 바라보도록 설정 업데이트 (필요 시)
                        sh "aws eks update-kubeconfig --name ${EKS_CLUSTER_NAME} --region ${AWS_DEFAULT_REGION}"
                        
                        // 2. 배포할 이미지의 전체 URI를 변수에 저장합니다.
                        def fullImageUrl = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${env.IMAGE_TAG}"
                        
                        // 3. (핵심!) sed 명령어로 deployment.yml 파일의 이미지 주소를 동적으로 변경합니다.
                        // 'image:' 뒤에 오는 모든 문자열을 방금 빌드한 이미지 주소로 바꿔치기합니다.
                        sh "sed -i 's|image:.*|image: ${fullImageUrl}|g' deployment.yml"
                        
                        // 4. 이제 모든 설정 파일(Service, Deployment)을 apply 합니다.
                        // - 리소스가 없으면 새로 생성(Create)해주고,
                        // - 리소스가 이미 있으면 변경된 부분만 업데이트(Update)해줍니다.
                        sh "kubectl apply -f service.yml"
                        sh "kubectl apply -f deployment.yml"
                    }
                }
            }
        }

    }
}
