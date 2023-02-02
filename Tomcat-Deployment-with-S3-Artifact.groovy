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
        stage('Code-mvn') {
            steps {
                sh 'mvn --version'
                sh 'mvn clean package'
            }
        }
        stage('Code-Build') {
            steps {
                withAWS(credentials: '4d7ec8e0-5f99-487b-bec1-bb4fb87bf134', region: 'us-east-1') {
                    sh'sudo apt-get update -y'
                    sh 'sudo apt-get install awscli -y'
                }
            }
        }
        stage('Code-Deploy') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'ccf334b0-92dd-4bc1-a114-d75acb765912', keyFileVariable: 'Nagpur')]) {
                    sh '''
                    ssh -i ${Nagpur} -o StrictHostKeyChecking=no ubuntu@54.160.106.2<<EOF
                    sudo apt update -y
                    sudo apt install awscli -y
                    aws --version
                    aws configure set aws_access_key_id AKIAX3D244GCQOEF4CRB
                    aws configure set aws_secret_access_key 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
                    aws s3 ls
                    aws s3 cp s3://was-swa/onlinebookstore.war .
                    curl -O https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.71/bin/apache-tomcat-9.0.71.tar.gz
                    sudo tar -xvf apache-tomcat-9.0.71.tar.gz -C /opt/
                    sudo sh /opt/apache-tomcat-9.0.71/bin/shutdown.sh
                    sudo cp -rv onlinebookstore.war bookstore.war 
                    sudo cp -rv bookstore.war /opt/apache-tomcat-9.0.71/webapps/ 
                    sudo sh /opt/apache-tomcat-9.0.71/bin/startup.sh

                    '''
                }
            }
        }
        
    }
}
