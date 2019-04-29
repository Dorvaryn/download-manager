pipeline {
    agent { docker { image 'runmymind/docker-android-sdk' } }
    stages {
        stage('build') {
            steps {
                sh './gradlew --version'
            }
        }
    }
}
