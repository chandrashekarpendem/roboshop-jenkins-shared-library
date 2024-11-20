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
                    echo 'compile/build'
                }

            }

            stage('unit-test') {
                steps {
                    echo 'unit-test'
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