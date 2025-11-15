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

    }
}
