def call (){
    pipeline {
        options {
            ansiColor('xterm')
        }
            agent {
                node {
                    label "workstation"
                }
            }

                parameters {
                    choice(name: 'INFRA_ENV', defaultValue: '', description: 'enter env like dev or prod')
                    choice(name: 'Action', defaultValue: '', description: 'Apply or Destroy')
                }

                    stages{
                        stage('terraform init'){
                            steps{
                                sh "terraform init -backend-config=env-${INFRA_ENV}/state.tfvars"
                            }
                        }

                        stage('terraform Apply or Destroy'){
                            steps{
                                sh "terraform ${Action} -auto-approve -var-file=env-${INFRA_ENV}/main.tfvars"
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