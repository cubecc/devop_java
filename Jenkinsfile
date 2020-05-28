pipeline {
	  agent {
	    node {
	      label 'docker'
	    }
	  }

    environment {
        registry = "registry.lab.local:5000/webdemo-dev"
        registryCredential = 'docker-registry-100.64.21.108'
        dockerImage = ''
        env= "dev"
    }

    tools { 
        maven 'mvn3.6.3' 
    }

    stages {

		stage ('clean before working'){
			steps{
				deleteDir()
			}
		}

		
        stage ('Initialize') {
            steps { 
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    echo ""
                '''                                 
            }  
        }
                
                
        stage('Build') {
        	 agent {
		        docker {
		          reuseNode true
		          image 'maven:3.6.3-jdk-8'
		        }
		      }
		      steps {		        
		        withMaven(options: [findbugsPublisher(), junitPublisher(ignoreAttachments: false)]) {
		          sh 'mvn clean findbugs:findbugs package'
		        }
		      }
        }       
    }
}