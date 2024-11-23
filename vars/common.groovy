def compile () {
    if (app_lang == "nodejs") {
        sh 'npm install'
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