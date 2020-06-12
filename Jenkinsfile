def getEnvFromBranch(branch) {
	if (branch == 'master') {
		return 'prod'
	} else {
		return branch
	}
}

pipeline {
    agent any

    environment {
        //registry = "registry.lab.local:5000/webdemo-dev"
        //registryUrl = 'https://100.64.21.108:5000'
        registryUrl = 'https://registry.lab.local:5000'
        //registryCredential = 'docker-registry-100.64.21.108'
        registryCredential = 'docker-registry-login'
        //dockerDOCKER_IMAGE_NAME = ''
        
        ENV_NAME = getEnvFromBranch("${env.BRANCH_NAME}")
        
        APP_NAME = "webdemo-${ENV_NAME}"
        APP_VERSION = "MAK001";
        
        //DOCKER_IMAGE_NAME = readMavenPom().getArtifactId()
    	//DOCKER_IMAGE_VERSION = readMavenPom().getVersion()    	
    	//DOCKER_IMAGE_NAME = "registry.lab.local:5000/${APP_NAME}"
    	//DOCKER_IMAGE_VERSION = "${BUILD_NUMBER}"
    	
    	//BUILD_ENV = "${BRANCH_NAME}"
    	
   		DOCKER_IMAGE_NAME = "registry.lab.local:5000/${APP_NAME}"
   		//DOCKER_IMAGE_VERSION = "${env.BRANCH_NAME}-${APP_VERSION}-${env.BUILD_ID}-${env.GIT_COMMIT}"
   		DOCKER_IMAGE_VERSION = "${env.BRANCH_NAME}-${APP_VERSION}-${env.GIT_COMMIT}"
   		   		   		    	
   		HOST = "100.64.21.141"
   		HOST_PORT = "31235"
    }

    tools { 
        maven 'mvn3.6.3' 
    }

    stages {
    	
        stage('User Input') {
            steps {            
            	echo 'wait user input...'            	
//                timeout(1) {
//                    script {                        
//                        approvalMap = input id: 'test', message: 'Hello', ok: 'Proceed?', parameters: [choice(choices: 'dev\nuat\nprod', description: 'Select env to build', name: 'Environment'), string(defaultValue: '', description: '', name: 'Remarks')], submitter: 'user1,user2,group1', submitterParameter: 'APPROVER'
//                    }
//                }
//                                                
//                echo "This build was approved by: ${approvalMap['APPROVER']}"
//                echo "build env: ${approvalMap['Environment']}"
//                echo "Remarks : ${approvalMap['Remarks']}"
            }
        }
		
        stage ('Initialize') {
            steps { 
                //sh '''
                //    echo "PATH = ${PATH}"
                //    echo "M2_HOME = ${M2_HOME}"
                //    echo ""
                //'''
                echo 'init'
            }
        }
                                                
        stage('Build & Test') {
        	steps {
        		//sh 'mvn clean package -P${env} -DskipTests'
        		sh 'mvn clean package'
        		//sh 'mvn clean package -DskipTests'
        	}
            post {
	            success {
	            	echo 'generate test report'
	                junit 'target/surefire-reports/**/*.xml' 
	            }
        	}        	
        }       
        
        
        stage('Dependency Check') {
          steps {
          		dependencycheck additionalArguments: '--project [project_name] --scan ./ --out ./target/dependency-check-report.html --format HTML --noupdate', odcInstallation: 'Dependency Checker'
          		//dependencyCheckPublisher pattern: './target/dependency-check-report.xml'
          }
        }        
        
        
        stage('SonarQube Analysis') {
          steps {
          
          	echo  'call sonar'
            //sh 'mvn sonar:sonar -Dsonar.login=$SONAR_PSW'
            
			    withSonarQubeEnv('SonarQube'){ 
			      sh 'mvn sonar:sonar -Dsonar.dependencyCheck.reportPath=target/dependency-check-report.html -Dsonar.dependencyCheck.htmlReportPath=target/dependency-check-report.html'
			      //sh 'mvn sonar:sonar'
			    }
          }
        }

	    stage('Quality Gate') {
	        steps {
	          timeout(time: 15, unit: 'MINUTES') {
	            waitForQualityGate abortPipeline: true
	          }
	        }
	    }      
	            
        stage('Build Docker DOCKER_IMAGE_NAMEs') {
        
		  steps {          	
				script {
				   	docker.withRegistry( registryUrl, registryCredential ) {
				        sh '''				          		                  
				            	docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_VERSION} .
				        '''
					}
				}
			}         
        }
          
          /*    
		stage('Docker security analysis'){
			steps {
				echo 'Performing Docker DOCKER_IMAGE_NAME analysis'
				//sh "CLAIR_ADDR=localhost:6060 CLAIR_OUTPUT=High CLAIR_THRESHOLD=100 /home/yyy/ ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_VERSION}"
			}
		}
		*/
		              
        stage('Push Docker Image') {
        
		  steps {          	
				script {
				   	docker.withRegistry( registryUrl, registryCredential ) {
				   	//docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_VERSION} ${DOCKER_IMAGE_NAME}:latest
				   	//docker push ${DOCKER_IMAGE_NAME}:latest
				        sh '''				          		                  						        
						        docker push ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_VERSION}
						        docker rmi ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_VERSION}
				        '''
					}
				}
			}         
        }

        stage('Deploy to K8') {
            steps{
            //envsubst < webdemo.yaml > webdemo_out.yaml
            	sh '''
            		
            		sed -i -e "s~\\${APP_NAME}~${APP_NAME}~g;s~\\${DOCKER_IMAGE_NAME}~${DOCKER_IMAGE_NAME}~g;s~\\${DOCKER_IMAGE_VERSION}~${DOCKER_IMAGE_VERSION}~g" webdemo.yaml
            		sed -i -e "s~\\${HOST_PORT}~${HOST_PORT}~g" webdemo.yaml
            		
            		kubectl replace -f webdemo.yaml --force            		
            		kubectl rollout status deployment ${APP_NAME} --watch --timeout=5m
            	'''                
            }            
        }
                                 		
		/*
        stage('OWASP Scan') {
        	agent { label 'zap' }
        	options { skipDefaultCheckout() }
        	steps {
        		 //sh 'mvn verify -Dhttp.proxyHost=localhost -Dhttp.proxyPort=8088 -Dhttps.proxyHost=localhost -Dhttps.proxyPort=8088'
        		 script {
        		    startZap(host: "100.64.21.108", port: 8088, timeout:500, zapHome: "/usr/share/owasp-zap")
                    //startZap(host: "127.0.0.1", port: 8088, timeout:500, zapHome: "/usr/share/owasp-zap")
                    //startZap(host: "localhost", port: 8088, timeout:1000, zapHome: "/usr/share/owasp-zap", sessionPath:"/tmp/session.session", allowedHosts:['100.64.21.136'])
        		 	//sh "mvn verify -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dhttp.proxyHost=100.64.21.108 -Dhttp.proxyPort=8088 -Dhttps.proxyHost=100.64.21.108 -Dhttps.proxyPort=8088 -DskipTests"        		 
                    runZapCrawler(host:"http://100.64.21.141:31235")
                    //runZapCrawler(host:"http://100.64.21.141:31235/getWebUserList")
                    //runZapAttack()
                 }        		 
        	}
		    post {
		        always {
		            script {
		                archiveZap(failAllAlerts: 100, failHighAlerts: 5, failMediumAlerts: 0, failLowAlerts: 0, falsePositivesFilePath: "zapFalsePositives.json")
		            }
		        }
		    }       	
        }
*/

		stage('OWASP Scan') {
			steps {
				script {					
					runLog = sh (script: "docker run --user \$(id -u):\$(id -g) -v ${workspace}:/zap/wrk/:rw -t owasp/zap2docker-stable zap-baseline.py -t http://${HOST}:${HOST_IP} -r zap_report.html -m 5", returnStatus: true)									
					publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: './', reportFiles: 'zap_report.html', reportName: 'OWASP ZAP Scan', reportTitles: 'OWASP ZAP Scan'])
					
					//0 if there are no issues, 1 if there are any failures or 2 if there are just warning
					echo "ZAP Run Log: ${runLog}"
				}
			}
		}
	
        stage('Smoke Test') {
            steps{
                echo 'ping curl...'
            }            
        }            		     
  		
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