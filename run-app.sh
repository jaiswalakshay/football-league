mkdir -p deployment/logs

mv football-league-0.0.1-SNAPSHOT.jar deployment/football-league.jar
cd deployment
nohup java -jar football-league.jar >> ~/deployment/logs/output.log 2>&1 &