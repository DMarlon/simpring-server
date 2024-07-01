# Project Name

Simpring Server - A simple HTTP server

-  This is only a simple HTTP server that I made to learn and have fun, it should not be taken as a serious project.

## 🔧 Purposes

- Management of HTTP requests; 
- Simplify the way to deal with HTTP requests;
- Simplify the way to response HTTP content.

## Dependencies 

[![Maven](https://img.shields.io/badge/maven-bd1f39?style=for-the-badge&logo=apache-maven&logoColor=black)](https://maven.apache.org/)

# Run Project

To run the project, you can check the **main** file, there you will be able to see the server specification, and on the controller folder, you also can check some examples of controllers. Feel free to change the server configuration and add new controllers to make more tests and check how the server will behave.

1. Running the project 

    ```java
	public class Main {
		public static void main(String[] args) {
			Argument argument = Argument.process(args);

			ServerRunner.init()
				.onPort(argument.getPort())
				.usingVirtualThreads()
				//.usingRealThreads()
				//.withPoolSize(argument.getPoolSize())			
				.usingDirectory(argument.getDirectory())
				.usingCustomFactory(new DefaultCustomFactory())
				.controlling(RootController.class, EchoController.class, UserAgentController.class, FileController.class)
			.run();
		}
	}
    ``` 

2. Adding new controllers
   
	* To add new controllers, you can create a java class and implement the interface core.controller.Controller;
	* You should also include it on .controlling() on the ServerRunner.

## Made with:
[![UBUNTU](https://img.shields.io/badge/Ubuntu-e95420?style=for-the-badge&logo=ubuntu&logoColor=white)](https://ubuntu.com/download)
[![JAVA](https://img.shields.io/badge/Java-cc0000?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![ECLIPSE](https://img.shields.io/badge/Eclipse-2c2255?style=for-the-badge&logo=eclipse&logoColor=white)](https://www.eclipse.org/downloads/)
[![INSOMNIA](https://img.shields.io/badge/Insomnia-6600d8?style=for-the-badge&logo=insomnia&logoColor=white)](https://insomnia.rest/)

## 🔖 License
[![LICENSE](https://img.shields.io/badge/GPL_2.0-E58080?style=for-the-badge&logo=bookstack&logoColor=white)](https://www.gnu.org/licenses/old-licenses/gpl-2.0.html)

### Support or contact

[![GITHUB](https://img.shields.io/badge/Github-000000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/dmarlon/)
[![Linkedin](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/marlon-dauernheimer-55278073/)

### Reference Documentation
To additional references, consider the following sections:

* [Java 21](https://openjdk.org/projects/jdk/21/)
* [HTTP Messages](https://developer.mozilla.org/en-US/docs/Web/HTTP/Messages)

