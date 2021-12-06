pipeline {
    agent {
        label "Jenkins-Slave"
    }
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "https"
        NEXUS_URL = "appcenter.blanc.tech"
        NEXUS_REPOSITORY = "Gray_Mobile"
        NEXUS_CREDENTIAL_ID = "nexus3"
        VERSION = "1.0"
        BODY = ""
    }
    stages {
        stage('Build') {
            steps {
                sh "chmod +x ./gradlew"
                sh "./gradlew assemble"
            }
        }
        stage('Upload Apk')
        {
            steps {
                script {

                    echo "*** File: ${VERSION}"
                    contentReplace(configs: [fileContentReplaceConfig(configs: [fileContentReplaceItemConfig(matchCount: 0, replace: "$PARAM_URL",    search: 'PARAM_URL')], fileEncoding: 'UTF-8', filePath: 'install.html')])
                    nexusArtifactUploader artifacts: 
                    [
                        [artifactId: 'Gray', 
                        classifier: '', 
                        file: 'app/build/outputs/apk/dev/debug/app-dev-debug.apk', 
                        type: 'apk'], 
                        [artifactId: 'Gray', 
                        classifier: '', 
                        file: 'install.html', 
                        type: 'html']
                    ], 
                        credentialsId: NEXUS_CREDENTIAL_ID, 
                        groupId: 'Gray_Android', 
                        nexusUrl: NEXUS_URL, 
                        nexusVersion: NEXUS_VERSION, 
                        protocol: NEXUS_PROTOCOL, 
                        repository: NEXUS_REPOSITORY, 
                        version: VERSION
                    
                }
            }
        }
        stage ('Send Mail')
        {
            steps{
                script {
                BODY = '''<p>bonjour!</p>
<p>un nouveau APK est disponible sur le <a href="HTML_URL" target="_blank">lien</a>.</p>
<p>login: myinwi</p>
<p>passwd: <span style="background-color: #000000; color: #000000;">Myinwi2021</span></p>
<p>crdlt</p>'''
                BODY = BODY.replaceAll("HTML_URL", "$HTML_URL")

                OBJET = 'PARAM_OBJET'
                OBJET = OBJET.replaceAll("PARAM_OBJET","$PARAM_OBJET")

                }
                emailext body: "$BODY", subject: "$OBJET", to: '${PARAM_RECIPIENTS}'
            }
        }
    }
}
