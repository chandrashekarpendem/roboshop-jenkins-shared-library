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

def email_notification(email_note) {
    mail bcc: '', body: "failure in : ${JOB_BASE_NAME} pipeline\nTake a look with url\nDisplay_URL:${RUN_DISPLAY_URL}\n jenkins_URL:${JENKINS_URL}" , cc: '', from: 'chandrashekarpendem19@gmail.com', replyTo: '', subject: "Jenkins job:${JOB_BASE_NAME} Failure notification", to: 'chandrashekarpendem19@gmail.com'
}

def artifactpush() {

//    below commands are used to push Artifact to nexus as we use servers
    sh "echo ${TAG_NAME} >version"

    if (app_lang == "nodejs") {
        sh "zip -r ${component}-${TAG_NAME}.zip node_modules server.js version ${extra_files}"
    }

    if (app_lang == "nginx" || app_lang == "python") {
        sh "zip -r ${component}-${TAG_NAME}.zip * -x Jenkinsfile ${extra_files}"
    }


    if (app_lang == "maven")  {
        sh "zip -r ${component}-${TAG_NAME}.zip  ${component}.jar VERSION ${extra_files}"
    }
    NEXUS_PASS = sh(script: 'aws ssm get-parameters --region us-east-1 --names nexus.pass  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
    NEXUS_USER = sh(script: 'aws ssm get-parameters --region us-east-1 --names nexus.user  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
    wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${NEXUS_PASS}", var: 'SECRET']]]) {
        sh "curl -v -u ${NEXUS_USER}:${NEXUS_PASS} --upload-file ${component}-${TAG_NAME}.zip http://172.31.91.211:8081/repository/${component}/${component}-${TAG_NAME}.zip"

    }

}

def Docker_build () {
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

    sh " docker build -t 225989332181.dkr.ecr.us-east-1.amazonaws.com/${component}:${TAG_NAME} . "
}


def docker_build_push(){

    sh "  aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 225989332181.dkr.ecr.us-east-1.amazonaws.com "

    sh "  docker push 225989332181.dkr.ecr.us-east-1.amazonaws.com/${component}:${TAG_NAME}"
}