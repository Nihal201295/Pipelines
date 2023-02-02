pipeline {
    agent {
        label 'Master-Slave-1'
    }

    stages {
        stage('Code-pulley-') {
            steps {
                git 'https://github.com/Nihal201295/onlinebookstore.git'
            }
        }
    }
}
