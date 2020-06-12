pipeline {
    agent any

    environment {
        //registry = "registry.lab.local:5000/webdemo-dev"
        //registryUrl = 'https://100.64.21.108:5000'
        registryUrl = 'https://registry.lab.local:5000'
        //registryCredential = 'docker-registry-100.64.21.108'
        registryCredential = 'docker-registry-login'
        //dockerImage = ''
        //env= "dev"
        //IMAGE = readMavenPom().getArtifactId()
    	//VERSION = readMavenPom().getVersion()
    	appname = "webdemo"
    	//IMAGE = "registry.lab.local:5000/${appname}"
    	//VERSION = "${BUILD_NUMBER}"
    	
   		VERSION = "${env.BUILD_ID}-${env.GIT_COMMIT}"
   		IMAGE = "registry.lab.local:5000/${appname}"    	
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
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    echo ""
                '''                                
            }  
        }
                                                
        stage('Build & Test') {
        	steps {
        		//sh 'mvn clean package -P${env} -DskipTests'
        		//sh 'mvn clean package -P${env}'
        		sh 'mvn clean package -DskipTests'
        	}
            post {
	            success {
	            	echo 'generate test report'
	                //junit 'target/surefire-reports/**/*.xml' 
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

	    stage("Quality Gate") {
	        steps {
	          timeout(time: 10, unit: 'MINUTES') {
	            waitForQualityGate abortPipeline: true
	          }
	        }
	    }      
	            
        stage('Build Docker Images') {
        
		  steps {          	
				script {
				   	docker.withRegistry( registryUrl, registryCredential ) {
				        sh '''				          		                  
				            	docker build -t ${IMAGE}:${VERSION} .
				        '''
					}
				}
			}         
        }
          
          /*    
		stage('Docker security analysis'){
			steps {
				echo 'Performing Docker image analysis'
				//sh "CLAIR_ADDR=localhost:6060 CLAIR_OUTPUT=High CLAIR_THRESHOLD=100 /home/yyy/ ${IMAGE}:${VERSION}"
			}
		}
		*/
		              
        stage('Push Docker Images') {
        
		  steps {          	
				script {
				   	docker.withRegistry( registryUrl, registryCredential ) {
				        sh '''				          		                  
						        docker tag ${IMAGE}:${VERSION} ${IMAGE}:latest
						        docker push ${IMAGE}:${VERSION}
						        docker push ${IMAGE}:latest
				        '''
					}
				}
			}         
        }

        stage('Deploy to K8') {
            steps{
            //envsubst < webdemo.yaml > webdemo_out.yaml
            	sh '''
            		
            		sed -i -e "s~\\${appname}~${appname}~g;s~\\${IMAGE}~${IMAGE}~g;s~\\${VERSION}~${VERSION}~g" webdemo.yaml
            		kubectl replace -f webdemo.yaml --force            		
            		kubectl rollout status deployment ${appname} --watch --timeout=5m
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
					runLog = sh (script: "docker run --user \$(id -u):\$(id -g) -v ${workspace}:/zap/wrk/:rw -t owasp/zap2docker-stable zap-baseline.py -t http://100.64.21.141:31235/ -r zap_report.html -m 5", returnStatus: true)									
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
            //deleteDir() /* clean up our workspace */
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