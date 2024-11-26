def compile () {
    if (app_lang == "nodejs") {
        sh 'npm install'
        sh 'env'
    }
    if (app_lang == "maven") {
        sh 'mvn clean compile package'
    }
    if (app_lang == "golang") {
        sh 'go mod init'
        sh 'go get'
        sh 'go build'
    }

}


def unit_test () {
    if (app_lang == "nodejs") {
        sh 'npm test || true'
    }
    if (app_lang == "maven") {
        sh 'mvn test'
    }
    if (app_lang == "golang") {
        sh 'go test'
    }
    if (app_lang == "python") {
        sh 'python3 -m unittest'
    }
}

def email() {
    mail bcc: '', body: "failure in : ${JOB_BASE_NAME} pipeline\nTake a look with url\nDisplay_URL:${RUN_DISPLAY_URL}\n jenkins_URL:${JENKINS_URL}" , cc: '', from: 'chandrashekarpendem19@gmail.com', replyTo: '', subject: "Jenkins job:${JOB_BASE_NAME} Failure notification", to: 'chandrashekarpendem19@gmail.com'
}

def artifactpush() {
    sh "echo ${TAG_NAME} >version"

    if (app_lang == "nodejs") {
        sh "zip -r ${component}-${TAG_NAME}.zip node_modules server.js version ${extra_files}"
    }

    if (app_lang == "nginx" || app_lang == "python") {
        sh "zip -r ${component}-${TAG_NAME}.zip * -x Jenkinsfile ${extra_files}"
    }

    if (app_lang == "maven") {
        sh "mvn package && cp target/${component}-1.0.jar ${component}.jar ${extra_files}"
    }

    NEXUS_PASS = sh(script: 'aws ssm get-parameters --region us-east-1 --names nexus.pass  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
    NEXUS_USER = sh(script: 'aws ssm get-parameters --region us-east-1 --names nexus.user  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
    wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${NEXUS_PASS}", var: 'SECRET']]]) {
        sh "curl -v -u ${NEXUS_USER}:${NEXUS_PASS} --upload-file ${component}-${TAG_NAME}.zip http://172.31.32.102:8081/repository/${component}/${component}-${TAG_NAME}.zip"

    }

}