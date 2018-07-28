cd deployment
mv football-league-0.0.1-SNAPSHOT.jar football-league.jar
nohup java -jar football-league.jar 2>&1 > ~/deployment/logs/output.log &