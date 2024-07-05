pipeline{
    agent any
    stages{
        // Config based on your current branch name
        stage('Build & Test Server') {
            steps{
                dir ('server'){
                    withGradle {
                        // Give permission for ./gradlew
                        sh 'chmod +x gradlew'

                        // Run the actual build & test for our server
                        sh './gradlew build'
                    }
                }
            }
        }
        stage('Build Client') {
            steps {
                script{
                    // Set default to staging for ticket purpose
                    def BUILD_MODE = ':staging'
                    if (env.BRANCH_NAME == 'staging'){
                        BUILD_MODE = ":${env.BRANCH_NAME}"
                    }
                    dir ('client') {
                        nodejs('Node18'){
                            sh "npm ci && npm run build${BUILD_MODE}"
                        }
                    }
                }
            }
        }
        stage('Build And Publish Images') {
            // condition to trigger this stage is only on staging branch
            when {branch 'staging'}
            steps{
                script{
                    def HOST_NAME = 'da-nang-internship-docker-local.dockerregistry.mgm-tp.com'
                    def NAMESPACE = 'com.mgmtp.da-nang-internship'
                    def GROUP_ID = 'com.mgmtp.cfu'
                    def VERSION = 'staging'

                    // Building Docker image
                    withDockerRegistry(credentialsId: 'ci-user-artifactory-token', url: 'https://docker-repos.dockerregistry.mgm-tp.com') {
                        // For Client
                        dir('client'){
                            sh "docker build . -t ${HOST_NAME}/${NAMESPACE}/${GROUP_ID}.client:${VERSION}"
                        }

                        // For Server
                        dir('server'){
                            sh "docker build . -t ${HOST_NAME}/${NAMESPACE}/${GROUP_ID}.server:${VERSION}"
                        }
                    }

                    // Publishing images
                    withDockerRegistry(credentialsId: 'ci-user-artifactory-token', url: 'https://da-nang-internship-docker-local.dockerregistry.mgm-tp.com') {
                        sh "docker push ${HOST_NAME}/${NAMESPACE}/${GROUP_ID}.client:${VERSION}" // for client
                        sh "docker push ${HOST_NAME}/${NAMESPACE}/${GROUP_ID}.server:${VERSION}" // for server
                    }
                }
            }
        }
        stage('Deploy') {
            // condition to trigger this stage is only on staging branch
            when {branch 'staging'}
            steps{
                script{
                    sshagent(['ci-user-ssh']) {
                        // Copy docker-compose.yaml to remote server
                        sh 'scp -o StrictHostKeyChecking=no ./docker-compose.yaml cfu@course4u.mgm-edv.de:/home/cfu'
                        withCredentials([usernamePassword(credentialsId: 'ci-user-artifactory-token', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                            // SSH to remote server, then login to Docker registry
                            sh 'ssh -o StrictHostKeyChecking=no cfu@course4u.mgm-edv.de "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD da-nang-internship-docker-local.dockerregistry.mgm-tp.com"'

                            // Pulling the latest Docker images and starts the server
                            sh "ssh -o StrictHostKeyChecking=no cfu@course4u.mgm-edv.de 'docker compose --project-name cfu --profile staging up -d --pull=always'"

                            // Logout from Docker registry
                            sh "ssh -o StrictHostKeyChecking=no cfu@course4u.mgm-edv.de 'docker logout da-nang-internship-docker-local.dockerregistry.mgm-tp.com'"
                        }
                    }
                }
            }
        }
    }
}