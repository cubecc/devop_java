pipeline {
	agent any

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
/*		
		stage ('cleanup'){
			steps{
				deleteDir()
			}
		}
*/
		
        stage ('Initialize') {
            steps { 
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    echo ""
                ''' 
                
                
            }  
        }
                
    }
}