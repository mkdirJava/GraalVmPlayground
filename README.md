
# GraalVm pracitce

----
### Intro, Why?

Was messing around with various languages like node, Golang and had a thought. I wanted to find out the answer to this question

* Given the Java ecosystem what would a http server look like in JAVA SE?
* Can we achieve the small footprint like a golang http server?

Now I appreciate a great many would say the kinds of words pertaining to frameworks

* Spring
* Micronaught
* Quarkus

A great deal of previous Java EE standards have been encapsulated by these frameworks such as JDBC / JPA, Servlets etc, each 
with their difficulties and benefits. I wanted to see what JAVA SE can do. 

So this project aims to using just the JAVA SE JDK ( GRAALVM version ) what would a simple http server look like

### Criteria:
1. Application must be able to run locally
2. Application must be able to compile down to native code
3. Application must be able to run in a dockerised environment

### Prerequisites
1. Have Graalvm installed [TO GraalVm](https://www.graalvm.org/downloads/)
   1. Project uses graalvm-ce:22.2.0 ( this is JDK 17)
   2. Remember to set JAVA_HOME to the installation of GraalVm 
3. Have docker installed [To Docker](https://docs.docker.com/engine/install/)
   1. Project uses: Docker Desktop 4.6.1
4. Have Maven installed [To Maven ](https://maven.apache.org/install.html)
   1. Project uses: apache-maven-3.8.6
---
### Application must be able to run locally

The most simple of the criteria seeing we are using java SE (17)
I feel most people don't remember to support "Normal"/"conventional" dev tooling

1. Open the project in you favourite IDK 
2. Run the main
Or
1. go to the root of the project and execute
```aidl
// Then we can curl to it
    curl localhost:9090
```

``` aidl
//windows
    mvn clean package && java -jar .\target\graalvm_practice.jar
//Linux
   mvn clean package && java -jar .\target\graalvm_practice.jar
   // Send some traffic 
   curl localhost:9090
```
---
### Application must be able to compile down to native code

This takes advantage of a maven plugin called "native-maven-plugin"

1. Rum the command

``` 
//windows
   mvn clean -Pnative package && .\target\Entry.exe
//Linux
   mvn clean -Pnative package && ./target/Entry
```
This should build the native image ( comes out as a binary ), then runs it

we can do the same command to execute

```aidl
   curl localhost:9090
```
---
### Application must be able to run in a dockerised environment
This one is also quite simple, there is a minimal multistep docker build process.
We trigger it using the maven plugin "docker-maven-plugin",

1. Go to the root of the project
2. execute
```aidl
   mvn clean package -Pdocker docker:build    
```
3. This packages the Jar and triggers the docker build. The build builds a native image inside docker
4. It then looses the origional build image 
5. Copies the native image
6. We can then run the image via the command

```aidl
   docker run -ePORT=9090 -p9090:9090 graalvm_practice:1.0-SNAPSHOT 
   // We can then curl to it
   curl localhost:9090
   // stop it via killing the container, might take a bit seeing it sends a stop signal
   docker container ls
   docker container stop <container_id>
   //
```
---
### Conclusion

I think it is possible to keep Java applications light akin to other languages like Node.js or Golang.
I am sure there are security issues with this approach. I am sure the internal httpServer might even be deprecated ... 
But I suppose languages like goLang and Node.js have third party Http Servers. There's nothing stopping this application from 
using Undertow, Jetty or any other servlet container. I wonder what the frameworks have baked in?

I think I will be exploring more of Graalvm as well as the frameworks in the future.