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
                    choice(name: 'INFRA_ENV', choices: ['dev', 'prod'], description: 'enter env like dev or prod')
                    choice(name: 'ACTION', choices: ['apply', 'destroy'], description: 'Pick something')
                }

                    stages{
                        stage('terraform Init'){
                            steps{
                                sh "terraform init -backend-config=env-${INFRA_ENV}/state.tfvars"
                            }
                        }

                        stage('terraform Apply or Destroy'){
                            steps{
                                sh "terraform ${ACTION} -auto-approve -var-file=env-${INFRA_ENV}/main.tfvars"
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