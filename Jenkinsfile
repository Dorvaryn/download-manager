pipeline {
    agent { node { label 'mobile' } }
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
    post {
        always {
            archiveArtifacts artifacts: 'library/build/outputs/aar/*.aar', fingerprint: true
            junit 'library/build/test-results/**/*.xml'
        }
    }
}
