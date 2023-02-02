pipeline {
    agent {
        label 'Master-Slave-1'
    }

    stages {
        stage('Code-pull') {
            steps {
                git 'https://github.com/Nihal201295/onlinebookstore.git'
            }
        }
    }
    stages {
        stage('Code-Buid') {
            steps {
                sh 'sudo apt-get update -y'
                sh 'sudo apt-get install maven -y'
                sh 'mvn --version'
                sh 'mvn clean package'
            }
        }
    }
}
