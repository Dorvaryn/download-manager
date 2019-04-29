pipeline {
    agent { docker { image 'runmymind/docker-android-sdk' } }
    stages {
        stage('build') {
            steps {
                sh './gradlew clean assemble'
            }
        }
        stage('test') {
            steps {
                sh './gradlew build'
            }
        }
    }
}
