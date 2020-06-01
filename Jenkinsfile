pipeline {
    agent any

    environment {
        registry = "registry.lab.local:5000/webdemo-dev"
        registryCredential = 'docker-registry-100.64.21.108'
        dockerImage = ''
        env= "dev"
        //IMAGE = readMavenPom().getArtifactId()
    	//VERSION = readMavenPom().getVersion()
    	IMAGE = "registry.lab.local:5000/webdemo-dev"
    	VERSION = "${BUILD_NUMBER}"
    }

    tools { 
        maven 'mvn3.6.3' 
    }

    stages {
		
        stage ('Initialize') {
            steps { 
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    echo ""
                '''                                
            }  
        }
                
                
        stage('Build & Test') {
        	steps {
        		sh 'mvn clean package -P${env}'
        	}
        }       
        
        stage('QA') {
          steps {
          	echo  'call sonar for qa (TODO)'
            //sh 'mvn sonar:sonar -Dsonar.login=$SONAR_PSW'
          }        
        }
        
        stage('Build & Publish Images') {
        
          steps {          	
		        sh '''
		          
                    docker.withRegistry( 'https://registry.lab.local:5000', registryCredential ) {
                    	docker build -t ${IMAGE}:${VERSION} .
				          docker tag ${IMAGE} ${IMAGE}:${VERSION}
				          docker push ${IMAGE}:${VERSION}
                    }		          

		        '''
          }         
        }

		//stage('Build and Publish Image') {
		//}  
		
		//stage ('clean after working'){
		//	steps{
		//		deleteDir()
		//	}
		//}      
    }
}