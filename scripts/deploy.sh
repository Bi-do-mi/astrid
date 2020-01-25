#!/usr/bin/env bash

$mvn clean package

echo 'Copy files...'

#webapps_dir=/opt/tomcat/latest/astridapp
# Remove existing assets (if any)
#ssh -i ~/.ssh/id_rsa bidomi@95.165.102.1 << EOF
# echo "Martini1" | sudo -S "rm -rf $webapps_dir/ROOT"
#EOF

scp -i ~/.ssh/id_rsa \
    target/ROOT.war \
    bidomi@95.165.102.1:/home/bidomi/

# Restart tomcat
#ssh -i ~/.ssh/id_rsa bidomi@95.165.102.1 << EOF
#service tomcat restart
#EOF

echo 'Restart server...'

#ssh -i ~/.ssh/id_rsa bidomi@95.165.102.1 << EOF
#pgrep java | xargs kill -9
#java -jar astrid.war &
#nohup java -jar astrid-0.0.1-SNAPSHOT.war > log.txt &
#EOF

echo 'Bye'