pipeline {
    agent any

    environment {
        KOYEB_EMAIL = credentials('GY_MESSAGES_KOYEB_EMAIL')
        KOYEB_PASSWORD = credentials('GY_MESSAGES_KOYEB_PASSWORD')
        KOYEB_SERVICE_ID = credentials('GY_MESSAGES_KOYEB_SERVICE_ID')
    }

    stages {
        stage('Login to Koyeb') {
            steps {
                script {
                    def bearerToken = sh(script: '''
                        response=$(curl -s -X POST "https://app.koyeb.com/v1/account/login" \
                        -H "Content-Type: application/json" \
                        -d '{
                            "email": "'"${KOYEB_EMAIL}"'",
                            "password": "'"${KOYEB_PASSWORD}"'"
                        }')
                
                        echo "$response" | jq -r '.token.id'
                    ''', returnStdout: true).trim()
                
                    if (!bearerToken || bearerToken == "null") {
                        error("🚨 ERROR: Wrong Koyeb credentials provided.")
                    }
                
                    env.BEARER_TOKEN = bearerToken
                    echo "✅ Successfully loged in to Koyeb."
                }
            }
        }

        stage('Deploy to Koyeb') {
                steps {
                    script {
                        def response = sh(script: """
                            curl -s -X POST "https://app.koyeb.com/v1/services/${KOYEB_SERVICE_ID}/redeploy" \
                            -H "Authorization: Bearer ${BEARER_TOKEN}" \
                            | jq -r '.deployment.id'
                        """, returnStdout: true).trim()
    
                        if (!response) {
                            error("🚨 ERROR: Koyeb deployment could not be started.")
                        }
    
                        env.DEPLOYMENT_ID = response
                        echo "🚀 Deployment started with ID: ${env.DEPLOYMENT_ID}"
    
                        timeout(time: 10, unit: 'MINUTES') {
                            waitUntil {
                                sleep 10
                                def status = sh(script: """
                                    curl -s -X GET "https://app.koyeb.com/v1/deployments/${env.DEPLOYMENT_ID}" \
                                    -H "Authorization: Bearer ${BEARER_TOKEN}" \
                                    | jq -r '.deployment.status'
                                """, returnStdout: true).trim()
    
                                echo "⏳ Current deployment status: ${status}"
    
                                if (status == "HEALTHY") {
                                    echo "✅ Deployment done."
                                    return true
                                } else if (status == "FAILED") {
                                    error("🚨 ERROR: Deployment has failed.")
                                }
                                
                                return false
                            }
                        }
                    }
                }
        }
    }
}
