pipeline {
  agent any
  options { timestamps(); disableConcurrentBuilds() }

  environment {
    IMAGE_NAME = 'consumer-complaint-portal'
    IMAGE_TAG  = "${env.BUILD_NUMBER}"
    // Jenkins occupies port 8080 on this host; the app runs on 8088.
    APP_PORT   = '8088'
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build package') {
      steps { sh 'mvn -B -DskipTests clean package' }
    }

    stage('Start application for Selenium') {
      steps {
        sh '''
          java -Dserver.port=${APP_PORT} \
               -jar target/consumer-complaint-portal-1.0.0.jar \
               > app.log 2>&1 &
          echo $! > app.pid
          echo "[CI] Waiting for app on port ${APP_PORT}..."
          for i in $(seq 1 35); do
            if curl --fail --silent http://localhost:${APP_PORT}/ > /dev/null 2>&1; then
              echo "[CI] App is reachable on port ${APP_PORT}"
              exit 0
            fi
            sleep 2
          done
          echo "[CI] App did not start within 70 s. Log:"
          cat app.log
          exit 1
        '''
      }
    }

    stage('Selenium UI tests') {
      steps {
        sh 'mvn -B -Dheadless=true -DbaseUrl=http://localhost:${APP_PORT} test'
      }
    }

    stage('Build Docker image') {
      steps {
        sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:latest .'
      }
    }

    stage('Publish image (optional)') {
      when { expression { return env.DOCKERHUB_USER?.trim() } }
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub',
                                          usernameVariable: 'DOCKER_USER',
                                          passwordVariable: 'DOCKER_PASS')]) {
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
      // Stop the app even when Selenium tests fail
      sh 'if [ -f app.pid ]; then kill "$(cat app.pid)" 2>/dev/null || true; rm -f app.pid; fi'
      junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
      archiveArtifacts allowEmptyArchive: true,
                       artifacts: 'target/*.jar,app.log,target/screenshots/**,target/surefire-reports/**'
    }
    failure {
      echo '[CI] Build FAILED -- review surefire-reports and app.log above.'
    }
  }
}
