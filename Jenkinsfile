pipeline {
	agent any

    environment {
        registry = "registry.lab.local:5000/webdemo"
        registryCredential = 'docker-registry-100.64.21.108'
        dockerImage = ''
        env= "uat"
    }

    tools { 
        maven 'mvn3.6.3' 
    }
        
    stages {
    	stage("deploy to dev") {
		  when {
		    branch 'dev'
		    steps {
		    	sh '''
		      		echo "deploy to dev"
		      	'''
		    }
		  }
		}
		
		stage("deploy to uat") {
		  when {
		    branch 'master'
		    steps {
		    	sh ''' 
		      		echo "deploy to uat"
		      	'''
		    }
		  }
		}
		
        stage ('Initialize') {
            steps { 
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                ''' 
            }  
        }
        
        stage("Cloning Git") {
            steps{
                git credentialsId: 'cube-github', url: 'git@github.com:cubecc/devop_java.git'
            }
        }      
        
        stage ('Build Src') {
            steps {
                echo 'Start Build...'
				//sh 'mvn -Dmaven.test.failure.ignore=true package' 
				sh 'mvn package -P${env}' 
            }
            post {
	            success {
	                junit 'target/surefire-reports/**/*.xml' 
	            }
        	}
        }


        
        stage('Building image') {
            steps{
                script {
                    dockerImage = docker.build registry + ":$BUILD_NUMBER -f Dockerfile"
                }
            }
            
        }
        
        stage('Push Image') {
            steps{
                script {
                    docker.withRegistry( 'https://registry.lab.local:5000', registryCredential ) {
                   		sh "docker tag $registry:$BUILD_NUMBER $registry:latest"
                    	sh "docker push $registry:latest"
                    }
                }
            }
        }           
        stage('Deploy K8') {
            steps{
                sh("kubectl replace -f webdemo.yaml --force")
            }
            
        }        

		stage('Remove Unused docker image') {
		  steps{
		    sh "docker rmi $registry:$BUILD_NUMBER"
		  }
		}            
    }
}