pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        DOCKER_REGISTRY = 'docker.io'                       // Docker registry
        DOCKER_IMAGE = 'kapilan1998/chat-image'            // Docker image name
        IMAGE_TAG = '1.0'                                  // Docker image tag
        GIT_CREDENTIALS_ID = 'github-credentials'          // Jenkins Git credentials (already used in checkout)
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'    // Docker Hub credentials
    }

    stages {

        stage('Maven Clean') {
            steps {
                sh "mvn clean"
            }
        }

        stage('Maven Build (skip tests)') {
            steps {
                sh "mvn install -DskipTests"
            }
        }

        stage('Run Tests') {
            steps {
                sh "mvn test"
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('MySonarServer') {
                    sh """
                        mvn verify sonar:sonar \
                        -Dsonar.projectKey=chat-application-dragonfly \
                        -Dsonar.projectName='Chat-Application-DragonFly' \
                        -Dsonar.host.url=http://host.docker.internal:9000 \
                        -Dsonar.junit.reportPaths=target/surefire-reports \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    """
                }
            }
        }

//         stage('Quality Gate') {
//             steps {
//                 timeout(time: 5, unit: 'MINUTES') {
//                     script {
//                         def result = waitForQualityGate abortPipeline: true
//                         if (result.status != 'OK') {
//                             error "Pipeline failed due to quality gate: ${result.status}"
//                         }
//                     }
//                 }
//             }
//         }


        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image..."
                    docker.build("${DOCKER_IMAGE}:${IMAGE_TAG}")
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    echo "Pushing Docker image to registry..."
                    docker.withRegistry("https://${DOCKER_REGISTRY}", "${DOCKER_CREDENTIALS_ID}") {
                        docker.image("${DOCKER_IMAGE}:${IMAGE_TAG}").push()
                    }
                }
            }



        }

    }

    post {
        success {
            echo "Pipeline completed successfully. Docker image pushed: ${DOCKER_IMAGE}:${IMAGE_TAG}"
        }
        failure {
            echo "Pipeline failed!"
        }
    }
}
