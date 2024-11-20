def call () {
    pipeline {

        agent {
            node {
                label 'ansible'
            }
        }

        stages {
            stage('compile/build') {
                echo 'compile/build'
            }

            stage('unit-test') {
                echo 'unit-test'
            }

            stage('Quality control') {
                echo 'Sonar-qube'
            }

            stage('Artifactory') {
                echo 'Nexus'
            }

        }

    }
}