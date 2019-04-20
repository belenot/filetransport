# filetransport
Intended for file transfering between server and client



# Overview

(curent version: 1)

That project maintaining via maven. It consist of three packages ***filetransport-server"***, ***filetransport-cient"*** and ***filetransport-message"***.
In the rest of the readme it would be called ***Server***, ***Client*** and ***Message*** accordinlgy.

***Server*** and ***Client*** have dependency on ***Message***, and do not on each other.

# Installation
Copy this repository, **cd** into it, and if you want to build all print ***mvn clean package***, if you need only server, or client side adjast ***-Dplugin=client*** or ***-Dplugin=server*** accordingly.

Required **jar**s would be in filetransport-server/target/ and filetransport-client/target/ repository in two versions: *regular jar* and *jar with dependencies*

# Client
Generate messages to server.

***Client*** has only **ClientHelper** class, which responsability is create messages to server. 
In current implementation it has **Client** class additionally, but it not required use just it.
Idea is that create own **Client** class which will be using **ClientHelper**.

# Server
**Server** class maintains conections and threads. Client queries are deletaged to **ClientService** class which process them and gives response.
Additionally that module has **CommandReader** class, which need for more accurate server's maintaining from cli 
(it was seem like an good idea, but now it looks like useless feature, maybe in future it will be removed).
Also it has logger functionality provided by **ServerLogger**  

**ClientService** choose what to do with query and delegate task to one of services, which are functional interface.
Main idea was that when it comes to add more functions to application, it won't be required to change server internal. 
For adding new functionality all is needed is create class extended functional interface, add it to **ClientService**' case statement, and add accorded function for creating client query.

# Message

It's simple: we have **ServerResponse** and **ClientQuery**, which produced by server and client accordinly.

Both implement **Bytesalizable** interface, for transfering data via net. 

Both contain *enum* (**ResponseCode** or **ClientCommand**), headers map implemented by **Map<String, String>**, and byte array- **data** field.

# Summary diagram
![filetransport](https://github.com/belenot/filetransport/blob/master/FileTransfer.png)
