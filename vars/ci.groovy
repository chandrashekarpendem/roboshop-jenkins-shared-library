def call() {

  if(!env.SONAR_EXTRA_OPTS){
    env.SONAR_EXTRA_OPTS=" "
  }

  if(!env.extra_files){
    env.extra_files=" "
  }

  if(!env.TAG_NAME) {
    env.PUSH_CODE = 'false'
  } else{
      env.PUSH_CODE='true'
    }

  try {
    node('workstation') {

      stage('Scripted Checkout:SCM') {
        cleanWs()
        git branch: 'main', url: "https://github.com/pcs1999/${component}.git"
        sh 'env'
      }

//      stage('Compile') {
//        common.compile()
//      }

      stage('Docker_Build') {
        common.Docker_build()
      }

      stage('Unit Tests') {
        common.unit_test()
      }

      stage('Quality Control') {
//        SONAR_PASS = sh ( script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.pass  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
//        SONAR_USER = sh ( script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.user  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
//        wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${SONAR_PASS}", var: 'SECRET']]]) {
//         sh "sonar-scanner -Dsonar.host.url=http://172.31.93.103:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASS} -Dsonar.projectKey=${component} -Dsonar.qualitygate.wait=true ${SONAR_EXTRA_OPTS}"
//          sh "echo Sonar Scan"
//        }
        sh "echo Sonar Scan"
      }

      if (app_lang == "maven") {
        stage('Build Package') {
          sh "mvn package && cp target/${component}-1.0.jar ${component}.jar"
        }
      }

//      if(env.PUSH_CODE == "true") {
//        stage('Upload Code to Centralized Place Nexus') {
//         common.artifactpush()
//        }
//      }

      if(env.PUSH_CODE == "true") {
        stage('Upload images to Centralized Place AWS_ECR') {
          common.docker_build_push()
        }
      }


    }

  } catch(Exception e) {
    common.email_notification("Failed")
  }
}