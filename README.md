# Guardians of the galaxy demo
## DefinitelyScala phaser.io game using Akka HTTP with Scala.js
(Many thanks to https://github.com/beikern/scala-js-phaser-tutorial)

Collect stars and evade meteors!

## Run the application
1. Run MongoDB
2. sbt ~re-start
3. open http://0.0.0.0:8081

## Control the dude
```
→ move right
← move left
SPACE jump
```

## Config 

The application contains three directories:
* `server` Akka HTTP application (server side)
* `client` Scala.js application (client side)
* `shared` Scala code that you want to share between the server and the client

