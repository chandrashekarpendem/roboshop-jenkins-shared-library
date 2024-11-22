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
    }    catch (Exception e) {
            common.email('failed')
        }

}