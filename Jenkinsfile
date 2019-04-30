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
                    steps {
                        sh './gradlew pmdMain'
                    }
                }
                stage('Checkstyle') {
                    steps {
                        sh './gradlew checkStyleMain'
                    }
                }
                stage('Findbugs') {
                    steps {
                        sh './gradlew findbugs'
                    }
                }
                stage('lint') {
                    steps {
                        sh './gradlew lint'
                    }
                }
            }
        }
    }
}
