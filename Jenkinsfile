pipeline {
  agent any
  options { timestamps(); disableConcurrentBuilds() }
  environment {
    IMAGE_NAME = 'consumer-complaint-portal'
    IMAGE_TAG = "${env.BUILD_NUMBER}"
  }
  stages {
    stage('Checkout') { steps { checkout scm } }
    stage('Build and unit tests') { steps { sh 'mvn -B clean test' } }
    stage('Package') { steps { sh 'mvn -B -DskipTests package' } }
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
  post { always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'; archiveArtifacts allowEmptyArchive: true, artifacts: 'target/*.jar' } }
}
