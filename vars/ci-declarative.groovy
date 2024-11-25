def call () {
    try {
        pipeline {

            agent {
                node {
                    label 'ansible'
                }
            }

            stages {

                stage('compile/build') {
                    steps {
                        script {
                            common.compile()
                        }
                    }

                }

                stage('unit-test') {
                    steps {
                        script {
                            common.unit_test()
                        }
                    }
                }

                stage('Quality control') {
                    environment {
                        SONAR_USER = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.user  --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
                        SONAR_PASS = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.pass  --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
                    }
                    steps {
                        sh  "sonar-scanner -Dsonar.host.url=http://172.31.13.126:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASS} -Dsonar.projectKey=${component}"
                    }
                }

                stage('Artifactory') {
                    steps {
                        echo 'Nexus'
                    }
                }

            }

        }
    }    catch (Exception e) {
            common.email('failed')
        }

}