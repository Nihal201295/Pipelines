pipeline {
    agent {
        label 'slave'
    }

    stages {
        stage('Code-Pull') {
            steps {
                git 'https://github.com/Nihal201295/onlinebookstore.git'
            }
        }
        stage('Code-mvn') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Code-Build') {
            steps {
                withAWS(credentials: '9c9533ca-90d8-4d09-8020-bbca67b00611', region: 'us-east-1') {
                    sh 'sudo apt-get update -y'
                    sh 'sudo apt-get install awscli -y'
                    //sh 'aws s3 cp /var/lib/jenkins/workspace/Job-2/target/onlinebookstore.war s3://was-swa'
                }
            }
        }
        stage('Code-Deploy') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'df6786c3-7476-496e-bf78-39dc331148bc', keyFileVariable: 'Nagpur')]) {
                     sh'''
                    ssh -i ${Nagpur} -o StrictHostKeyChecking=no ubuntu@54.146.70.46<<EOF
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

