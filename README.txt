To get up an running
 1. cd to project root
 2. run 'mvn install:install-file -DgroupId=org.brickred.socialauth -DartifactId=socialauth -Dversion=1.0-beta2 -Dfile='lib/socialauth-1.0-beta2.jar' -Dpackaging=jar'
 3. put oauth_consumer.properties into src/main/res folder (see http://code.google.com/p/socialauth/wiki/GettingStarted on how to form the file)
 4. run 'mvn jetty:run' from project root