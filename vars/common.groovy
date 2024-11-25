def compile () {
    if (app_lang == "nodejs") {
        sh 'npm install'
        sh 'env'
    }
    if (app_lang == "maven") {
        sh 'mvn clean package'
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

def email (email_note){
    mail bcc: '', body: "failure in : ${JOB_BASE_NAME} pipeline\nTake a look with url\nDisplay_URL:${RUN_DISPLAY_URL}\n jenkins_URL:${JENKINS_URL}" , cc: '', from: 'chandrashekarpendem19@gmail.com', replyTo: '', subject: "Jenkins job:${JOB_BASE_NAME} Failure notification", to: 'chandrashekarpendem19@gmail.com'
}