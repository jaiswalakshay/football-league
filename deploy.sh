# ssh server
# mkdir -p deployment/logs
# cd deployment
# scp jar and run-app.sh
# ./run-app.sh



--------

cd /opt/bitnami/apps/jenkins/jenkins_home/workspace/football-league/master-build/target
ssh -i /opt/bitnami/apps/jenkins/jenkins_home/jenkins.pem ubuntu@13.58.144.95
mkdir -p deployment/logs
scp -i /opt/bitnami/apps/jenkins/jenkins_home/jenkins.pem football-league-0.0.1-SNAPSHOT.jar ubuntu@13.58.144.95:/deployment
scp -i /opt/bitnami/apps/jenkins/jenkins_home/jenkins.pem ../