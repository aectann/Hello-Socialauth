To get up an running
 1. cd to project root
 2. run 'mvn install:install-file -DgroupId=org.brickred.socialauth -DartifactId=socialauth -Dversion=1.0-beta2 -Dfile='lib/socialauth-1.0-beta2.jar' -Dpackaging=jar'
 3. put oauth_consumer.properties into src/main/res folder (see http://code.google.com/p/socialauth/wiki/GettingStarted on how to form the file)
 4. since some of the providers doesn't allow you to do redirects to localhost you have to:
  4.1. alter your local hosts file to make 127.0.0.1 be resolved to i.e. http://app.example.org (replace with domain of your choice). On UNIX based systems the file probably placed under /etc folder, for other systems, just google its location.
  4.2. specify the domain as app site/domain to all your providers (facebook, twitter etc)
  4.3. for callback URI use http://app.example.org:8080/auth (replace domain name with yours).
  4.4. update web.xml with your app domain	
 5. run 'mvn jetty:run' from project root
 6. open 'http://app.example.org:8080' in a browser