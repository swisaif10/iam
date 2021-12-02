pipeline {
    agent {
        label "Master"
    }
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "appcenter.blanc.tech"
        NEXUS_REPOSITORY = "Gray_Mobile"
        NEXUS_CREDENTIAL_ID = "nexus-user-credentials"
        VERSION = ""
        BODY = ""
    }
    stages {
        stage('Build') {
            steps {
                sh "chmod +x ./gradlew"
                sh "./gradlew assemble"
            }
        }
        /*stage('Upload Apk')
        {
            steps {
                script {
                    def apkJson = readJSON file: 'app/build/outputs/apk/release/output-metadata.json'
                    VERSION = apkJson.elements.versionName[0]
                    echo "*** File: ${VERSION}"
                    sh "cp /var/lib/jenkins/install.html $WORKSPACE/install.html"
                    contentReplace(configs: [fileContentReplaceConfig(configs: [fileContentReplaceItemConfig(matchCount: 0, replace: "$VERSION", search: 'PARAM_VERSION')], fileEncoding: 'UTF-8', filePath: 'install.html')])
                    //contentReplace(configs: [fileContentReplaceConfig(configs: [fileContentReplaceItemConfig(matchCount: 0, replace: "$PortParam", search: 'PARAM_DATE')], fileEncoding: 'UTF-8', filePath: 'install.html')])
                    nexusArtifactUploader(
                        nexusVersion: NEXUS_VERSION,
                        protocol: NEXUS_PROTOCOL,
                        nexusUrl: NEXUS_URL,
                        groupId: "myinwi.Android",
                        version: VERSION,
                        repository: NEXUS_REPOSITORY,
                        credentialsId: NEXUS_CREDENTIAL_ID,
                        artifacts: [
                            [artifactId: "myinwi",
                            classifier: '',
                            file: "app/build/outputs/apk/release/app-release.apk",
                            type: "apk"],
                            [artifactId: "myinwi",
                            classifier: '',
                            file: "install.html",
                            type: "html"]
                        ]
                    );
                }
            }
        }*/
        stage ('Send Mail')
        {
            steps{
                script {
                BODY = '''<p>bonjour!</p>
<p>un nouveau APK est disponible sur le <a href="http://repo.blanc.tech/repository/MyInwi_Mobile/myinwi/Android/myinwi/PARAM_VERSION/myinwi-PARAM_VERSION.html" target="_blank">lien</a>.</p>
<p>login: myinwi</p>
<p>passwd: <span style="background-color: #000000; color: #000000;">Myinwi2021</span></p>
<p>crdlt</p>'''
                BODY = BODY.replaceAll("PARAM_VERSION", "$VERSION")
                }
                emailext body: "$BODY", subject: 'Nouveau APK', to: '${PARAM_RECIPIENTS}'
            }
        }
    }
}
