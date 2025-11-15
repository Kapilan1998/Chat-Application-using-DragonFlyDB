pipeline {
    agent any

    tools {
        maven 'Maven3'
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


        stage('Quality Gate') {
            steps {
                timeout(time: 3, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}
