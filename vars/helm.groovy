def call () {
    pipeline {

        agent { label 'workstation' }

        parameters {
            choice(name: 'INFRA_ENV', choices: ['dev', 'prod'], description: 'enter env like dev or prod')
            choice(name: 'COMPONENT', choices: ['frontend', 'cart', 'catalogue', 'user', 'payment', 'shipping'], description: 'enter which component')
            string(name: 'APP_VERSION', defaultValue: '', description: 'enter APPLICATION version to deploy ')
        }

        stages {
            stage('Get values form file called values.yaml') {
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
                    sh 'helm upgrade --install ${COMPONENT} ./HELM -f APP/values.yaml --namespace production --set-string image.tag="${APP_VERSION},ENV=${INFRA_ENV},COMPONENT=${COMPONENT}"'
                }
            }
        }

    }
    post {
        always {
            echo 'Cleaning workspace...'
            cleanWS() // This cleans the workspace after the pipeline completes
        }
    }

}