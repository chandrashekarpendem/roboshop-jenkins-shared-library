pipeline {
    agent {
       label 'workstation'
    }

    parameters{
        choice(name:'APP_ENV', choices: ['dev', 'prod'], description: 'enter env like dev or prod')
        choice(name:'COMPONENT', choices: ['frontend', 'cart', 'catalogue', 'user', 'payment', 'shipping'], description: 'enter which component')
        string(name:'APP_VERSION', defaultValue: '', description: 'enter APPLICATION version to deploy ')

    }

    options {
        ansiColor('xterm')
    }

    environment {
        SSH=credentials('SSH')
    }

    stages {
        stage('deploy_new_app_version'){
            steps {
                sh '''
                aws ssm put-parameter --name "${APP_ENV}.${COMPONENT}.app_version" --type "String" --value "${APP_VERSION}" --overwrite
                
//            Below are used for mutable approach
             aws ec2 describe-instances     --filters "Name=tag:Name,Values=${APP_ENV}-${COMPONENT}"  | jq ".Reservations[].Instances[].PrivateIpAddress" >/tmp/hosts
             ansible-playbook  -i /tmp/hosts  deploy.yml -e component=${COMPONENT} -e env=${APP_ENV} -e ansible_user=${SSH_USR} -e ansible_password=${SSH_PSW}       
               '''
            }

        }
    }
}