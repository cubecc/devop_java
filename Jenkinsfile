pipeline {
    agent any

    environment {
        registry = "registry.lab.local:5000/webdemo-dev"
        //registryUrl = 'https://100.64.21.108:5000'
        registryUrl = 'https://registry.lab.local:5000'
        //registryCredential = 'docker-registry-100.64.21.108'
        registryCredential = 'docker-registry-login'
        //dockerImage = ''
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
    	
        stage('User Input') {
            steps {
                timeout(60) {
                    script {                        
                        approvalMap = input id: 'test', message: 'Hello', ok: 'Proceed?', parameters: [choice(choices: 'apple\npear\norange', description: 'Select a fruit for this build', name: 'FRUIT'), string(defaultValue: '', description: '', name: 'myparam')], submitter: 'user1,user2,group1', submitterParameter: 'APPROVER'
                    }
                }
                                                
                echo "This build was approved by: ${approvalMap['APPROVER']}"
                echo "This build is brought to you today by the fruit: ${approvalMap['FRUIT']}"
                echo "This is myparam: ${approvalMap['myparam']}"
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
                                
        stage('Build & Test') {
        	steps {
        		sh 'mvn clean package -P${env}'
        	}
            post {
	            success {
	                junit 'target/surefire-reports/**/*.xml' 
	            }
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
	          	script {
		           	docker.withRegistry( registryUrl, registryCredential ) {
				        sh '''				          		                  
		                    	docker build -t ${IMAGE}:${VERSION} .
						        docker tag ${IMAGE}:${VERSION} ${IMAGE}:latest
						        docker push ${IMAGE}:latest		                    		         		
				        '''
			        }
			     }
          }         
        }

        stage('Deploy to K8') {
            steps{
                sh("kubectl replace -f webdemo.yaml --force")
            }            
        } 
        
        stage('Post test') {
            steps{
                echo 'ping'
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
	  post {
        always {
            echo 'post status : clearup workspace'
            deleteDir() /* clean up our workspace */
        }
        success {
            echo 'post status : success'
        }
        unstable {
            echo 'post status : unstable'
        }
	    failure {		      
	    	  echo 'post status : failure'
	      	  //mail to: 'alex.cc.chan@jos.com',
	          //subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
	          //body: "Something is wrong with ${env.BUILD_URL}"
	    }
        changed {
            echo 'post status : changed'
        }		  

	  }    
}