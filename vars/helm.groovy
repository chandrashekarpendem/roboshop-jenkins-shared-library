def call () {
    pipeline {

        agent {
            node {
                label 'workstation'
            }
        }
        parameters {
            choice(name: 'COMPONENT', choices: ['frontend', 'cart', 'catalogue', 'user', 'payment', 'shipping'], description: 'enter which component')
            string(name: 'APP_VERSION', defaultValue: '', description: 'enter APPLICATION version to deploy ')
        }

        stages {
            stage('Get values form file') {
                steps {
                    dir('APP') {
                        git branch: 'main', url: 'https://github.com/chandrashekarpendem/${COMPONENT}.git'
                    }

                    dir('HELM') {
                        git branch: 'main', url: 'https://github.com/chandrashekarpendem/roboshop-helm-chart.git'
                    }
                }
            }

            stage('HELM upgrade/install') {
                steps {
                    //              sh 'helm upgrade   -i ${COMPONENT} ./HELM -f APP/values.yaml --namespace production --set-string image.tag="${APP_VERSION},ENV=prod,COMPONENT=${COMPONENT}"'
                }
            }
        }

        post {
            always {
                cleanWS()
            }
        }
    }
}