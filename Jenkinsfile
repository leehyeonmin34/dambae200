pipeline {
    agent any 	// 사용 가능한 에이전트에서 이 파이프라인 또는 해당 단계를 실행
    stages {
        stage('Prepare') {
            steps {
                git branch: 'main', credentialsId: 'leehyeonmin34-github-account', url: 'https://github.com/leehyeonmin34/dambae200'

            }

            post {
                success {
                    sh 'echo "Successfully Cloned Repository"'
                }
                failure {
                    sh 'echo "Fail Cloned Repository"'
                }
            }
        }

        stage('Build') {
            steps {
            	  // gralew이 있어야됨. git clone해서 project를 가져옴.
                sh 'chmod +x gradlew'
                sh  './gradlew --warning-mode=all --stacktrace clean build -x test'

                sh 'ls -al ./build'
            }
            post {
                success {
                    echo 'gradle build success'
                }

                failure {
                    echo 'gradle build failed'
                }
            }
        }

        // stage('SSH Tranfer') {
        //     steps {

        //     	sshagent(credentials: ['dambae200-ssh']){
        //           // remove existing code
        //     	    sh 'ssh -T root@dambae200-ssh rm -rf ~/docker-image/deploy/*'

        //           // transfer new code
								// 	sh 'scp -r ./build root@dambae200-ssh:~/docker-image/deploy/'

        //     	}
        //     }
        //     post {
        //         success {
        //             echo 'ssh transfer success'
        //         }

        //         failure {
        //             echo 'ssh transfer failed'
        //         }
        //     }
        // }

        stage('Build Docker image'){
            steps{
                sh 'docker buildx build -t leehyeonmin34/dambae200-server -f /var/jenkins_home/workspace/dambae200/Dockerfile /var/jenkins_home/workspace/dambae200'
            }
        }
        stage('Docker push image') {
            steps {
                withCredentials([string(credentialsId: 'leehyeonmin34-dockerhub-pw', variable: 'dockerHubPwd')]) {
                    sh "docker login -u leehyeonmin34 -p ${dockerHubPwd}"
                }
                sh 'docker push leehyeonmin34/dambae200-server'
            }

            post {
                success {
                    echo 'success'
                }

                failure {
                    echo 'failed'
                }
            }
        }
        stage('Run Container on SSH Dev Server'){
            steps{
                echo 'SSH'
                sshagent (credentials: ['dambae200-ssh']) {
                    sh '''
                        ssh -T root@dambae200-ssh <<- _EOF_
                        whoami
                        docker ps -q --filter name=dambae200-server | grep -q . && docker rm -f \$(docker ps -aq --filter name=dambae200-server-docker-image)
                        docker rmi -f leehyeonmin34/dambae200-server
                        docker pull leehyeonmin34/dambae200-server
                        cd docker-image
                        ./deploy.sh
                        exit
                        _EOF_'''
				// 	sh "ssh -o StrictHostKeyChecking=no ubuntu@172.31.13.153 'whoami'"
                    // sh "ssh -o StrictHostKeyChecking=no ubuntu@172.31.13.153 'docker ps -q --filter name=docker-jenkins-pipeline-test | grep -q . && docker rm -f \$(docker ps -aq --filter name=docker-jenkins-pipeline-test)'"
                    // sh "ssh -o StrictHostKeyChecking=no ubuntu@172.31.13.153 'docker rmi -f mooh2jj/docker-jenkins-pipeline-test'"
                    // sh "ssh -o StrictHostKeyChecking=no ubuntu@172.31.13.153 'docker run -d --name docker-jenkins-pipeline-test -p 8080:8080 mooh2jj/docker-jenkins-pipeline-test'"
                }

            }

        }

        // stage('Build Docker Image') {
        //     steps {

        //         sshagent(['dambae200-ssh']) {

        //          // build docker image
        //     	   sh '''
        //     	        ssh -T root@dambae200-ssh <<- _EOF_
        //         	        cd ~/docker-image
        //         	        docker build -t dambae200-server-docker-image .
        //         	        ls -al ./
        //         	        ~/docker-image/deploy.sh
        //         	        exit
        //         	        _EOF_'''
        //         }
        //     }
        //     post {
        //         success {
        //             echo 'build docker image success'
        //         }

        //         failure {
        //             echo 'build docker image failed'
        //         }
        //     }
        // }




        // stage('Docker Push') {
        //     steps {
        //         withCredentials([string(credentialsId: 'dockerHubPwd', variable: 'dockerHubPwd')]) {
        //             sh "docker login -u mooh2jj -p ${dockerHubPwd}"
        //         }
        //         sh 'docker push mooh2jj/board_vue_backend'
        //     }

        //     post {
        //         success {
        //             echo 'Docker Push success'
        //         }

        //         failure {
        //             echo 'Docker Push failed'
        //         }
        //     }
        // }
        // stage('Docker Deploy'){
        //     steps{
        //         sh 'docker-compose up -d'
        //         sh 'docker-compose ps'
        //     }
        //     post {
        //         success {
        //             echo 'docker-compose success'
        //         }

        //         failure {
        //             echo 'docker-compose failed'
        //         }
        //     }
        // }
    }
}