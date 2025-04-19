# Chat-Application-using-DragonFlyDB
simple chat application using web socket and dragon fly DB


server.port=8087  ( so here we can run in 8087 server)
# mvn spring-boot:run -Dspring-boot.run.profiles=instance1
#above command for run this project in another port number (like in another server)

before start application, up the container of dragonfly.
Then only it will work correctly. if not redis connection (dragonfly DB connect) won't exist.