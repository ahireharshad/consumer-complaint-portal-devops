pipeline {
  agent any
  options { timestamps(); disableConcurrentBuilds() }
  environment { IMAGE_NAME = 'consumer-complaint-portal'; IMAGE_TAG = "${env.BUILD_NUMBER}" }
  stages {
    stage('Checkout') { steps { checkout scm } }
    stage('Build package') { steps { sh 'mvn -B -DskipTests clean package' } }
    stage('Start application for Selenium') {
      steps {
        sh '''
          (java -jar target/consumer-complaint-portal-1.0.0.jar > app.log 2>&1 & echo $! > app.pid)
          for i in $(seq 1 30); do
            if curl --fail --silent http://localhost:8080/ > /dev/null; then exit 0; fi
            sleep 2
          done
          cat app.log
          exit 1
        '''
      }
    }
    stage('Selenium UI tests') { steps { sh 'mvn -B -Dheadless=true -DbaseUrl=http://localhost:8080 test' } }
    stage('Build Docker image') { steps { sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:latest .' } }
    stage('Publish image (optional)') {
      when { expression { return env.DOCKERHUB_USER?.trim() } }
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
          sh 'docker tag ${IMAGE_NAME}:${IMAGE_TAG} "$DOCKERHUB_USER/${IMAGE_NAME}:${IMAGE_TAG}"'
          sh 'docker push "$DOCKERHUB_USER/${IMAGE_NAME}:${IMAGE_TAG}"'
          sh 'docker logout'
        }
      }
    }
  }
  post {
    always {
      sh 'if [ -f app.pid ]; then kill "$(cat app.pid)" || true; fi'
      junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
      archiveArtifacts allowEmptyArchive: true, artifacts: 'target/*.jar,app.log'
    }
  }
}