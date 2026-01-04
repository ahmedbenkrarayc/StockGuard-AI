pipeline {
    agent any

    environment {
        IMAGE_NAME = "stockguard:latest"
        TAR_NAME   = "stockguard.tar"
        REMOTE_DIR = "/opt/stockguard"
        SERVER_IP  = credentials('server-ip')
        SSH_PORT   = credentials('ssh-port')
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Checking out code..."
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                echo "Running Maven build and tests..."
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image..."
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Save Docker Image') {
            steps {
                echo "Saving Docker image to tar file..."
                sh "docker save ${IMAGE_NAME} -o ${TAR_NAME}"
            }
        }

        stage('Deploy to Server') {
            steps {
                sshagent(credentials: ['stockguard-ssh']) {
                    echo "Copying Docker tar to server..."
                    sh "scp -P ${SSH_PORT} ${TAR_NAME} ahmed@${SERVER_IP}:${REMOTE_DIR}"

                    echo "Loading Docker image and deploying with docker-compose.dev.yml..."
                    sh """
                        ssh -p ${SSH_PORT} ahmed@${SERVER_IP} '
                            cd ${REMOTE_DIR} &&
                            docker load -i ${TAR_NAME} &&
                            docker compose -f docker-compose.dev.yml up -d --force-recreate
                        '
                    """
                }
            }
        }
    }

    post {
        success { echo "✅ Deployment Successful!" }
        failure { echo "❌ Pipeline Failed!" }
    }
}
