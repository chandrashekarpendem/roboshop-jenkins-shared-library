def call () {
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

                }
            }

            stage('Quality control') {
                steps {
                    echo 'Sonar-qube'
                }
            }

            stage('Artifactory') {
                steps {
                    echo 'Nexus'
                }
            }

        }

    }
}