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
    post {
        always {
            archiveArtifacts artifacts: 'library/build/outputs/aar/*.aar', fingerprint: true
            junit 'library/build/reports/**/*.xml'
        }
    }
}
