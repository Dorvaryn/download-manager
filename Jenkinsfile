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
                sh './gradlew test'
            }
        }
        stage('Static analysis') {
            parallel {
                stage('PMD') {
                    sh './gradlew pmdMain'
                }
                stage('Checkstyle') {
                    sh './gradlew checkStyleMain'
                }
                stage('Findbugs') {
                    sh './gradlew findbugs'
                }
                stage('lint') {
                    sh './gradlew lint'
                }
            }
        }
    }
}
