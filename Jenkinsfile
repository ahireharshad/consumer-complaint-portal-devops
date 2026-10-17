
pipeline {
  agent any
  options {
    timestamps()
    disableConcurrentBuilds()
  }

  environment {
    IMAGE_NAME = 'consumer-complaint-portal'
    IMAGE_TAG  = "${env.BUILD_NUMBER}"
    APP_PORT   = '8088'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build package') {
      steps {
        bat 'mvn -B -DskipTests clean package'
      }
    }

    stage('Start application for Selenium') {
      steps {
        bat '''
          if exist app.log del app.log
          start "ConsumerComplaintApp" /B java -Dserver.port=%APP_PORT% -jar target\\consumer-complaint-portal-1.0.0.jar > app.log 2>&1
          echo [CI] Waiting for app on port %APP_PORT%...
          powershell -NoProfile -ExecutionPolicy Bypass -Command "$ready=$false; for($i=0;$i -lt 35;$i++){ try { $r=Invoke-WebRequest -Uri 'http://localhost:%APP_PORT%/' -TimeoutSec 2 -UseBasicParsing; if($r.StatusCode -eq 200){$ready=$true; break} } catch {}; Start-Sleep -Seconds 2 }; if(-not $ready){ Get-Content app.log; exit 1 }"
          if errorlevel 1 (
            type app.log
            exit /b 1
          )
          echo [CI] App is reachable.
        '''
      }
    }

    stage('Selenium UI tests') {
      steps {
        bat 'mvn -B -Dheadless=true -DbaseUrl=http://localhost:%APP_PORT% test'
      }
    }

    stage('Build Docker image') {
      steps {
        bat 'docker build -t %IMAGE_NAME%:%IMAGE_TAG% -t %IMAGE_NAME%:latest .'
      }
    }

    stage('Publish image (optional)') {
      when {
        expression { return env.DOCKERHUB_USER?.trim() }
      }
      steps {
        withCredentials([
          usernamePassword(
            credentialsId: 'dockerhub',
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
          )
        ]) {
          bat '''
            @echo off
            echo Logging in to Docker Hub
            docker login -u "%DOCKER_USER%" --password-stdin
            if errorlevel 1 exit /b 1
          '''
          bat 'docker tag %IMAGE_NAME%:%IMAGE_TAG% %DOCKERHUB_USER%/%IMAGE_NAME%:%IMAGE_TAG%'
          bat 'docker push %DOCKERHUB_USER%/%IMAGE_NAME%:%IMAGE_TAG%'
          bat 'docker logout'
        }
      }
    }
  }

  post {
    always {
      bat '''
        @echo off
        powershell -NoProfile -ExecutionPolicy Bypass -Command "$p=Get-CimInstance Win32_Process -Filter \\"Name='java.exe'\\" | Where-Object { $_.CommandLine -like '*consumer-complaint-portal-1.0.0.jar*' -and $_.CommandLine -like '*server.port=8088*' }; foreach($x in $p){ Stop-Process -Id $x.ProcessId -Force -ErrorAction SilentlyContinue }"
        exit /b 0
      '''
      junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
      archiveArtifacts allowEmptyArchive: true,
        artifacts: 'target/*.jar,app.log,target/screenshots/**,target/surefire-reports/**'
    }
    failure {
      echo '[CI] Build FAILED -- review the console, Surefire reports and app.log.'
    }
  }
}
