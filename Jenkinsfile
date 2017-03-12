pipeline {
    stages {
        stage ('Build') {
            steps {
                sh 'mvn clean install' 
            }
        }
	stage('Deploy') {
            steps {
                sh 'mvn deploy'
            }
        }
    }
}
