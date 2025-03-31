pipeline {
    agent any

    environment {
            DOCKER_HUB_USERNAME = 'teemvat'
            DOCKER_HUB_PASSWORD = credentials('docker_hub_password')  // Store in Jenkins credentials
            IMAGE_NAME = 'teemvat/frontend'
            TAG = "${BUILD_NUMBER}"
        }

    tools {
        jdk 'JDK17'
        maven 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/teemvat/hiutale-frontend.git'
            }
        }
        stage('Build & Test') {
            steps {
                sh 'mvn clean verify'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_HUB_USERNAME}/${IMAGE_NAME}:${TAG} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    sh "echo ${DOCKER_HUB_PASSWORD} | docker login -u ${DOCKER_HUB_USERNAME} --password-stdin"

                    sh "docker push ${DOCKER_HUB_USERNAME}/${IMAGE_NAME}:${TAG}"
                }
            }
        }

        stage('Code Coverage') {
            steps {
                jacoco()
            }
        }
    }

    post {
        success {
            echo 'Build successful!'
        }
        failure {
            echo 'Build failed.'
        }
        always {
            sh "docker logout"
        }
    }
}