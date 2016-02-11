# ScalaProjects
A project developed to familiarise myself with the Play framework.

- Features separate front end and backend communicating via http
- Allows the submission of a user record with name and email, and the listing of all submitted user records
- Features logging, error-handling and validation

*I didn't manage to get the sbt to download the mongoDB libraries, and have to rely on having the mongoDB jar in the lib folder for the backend application - the jars are not in the git hub repo and should be downloaded from https://oss.sonatype.org/content/repositories/releases/org/mongodb/scala/mongo-scala-driver_2.11/1.0.1/

*The frontend assumes the backend is running on localhost:9001
