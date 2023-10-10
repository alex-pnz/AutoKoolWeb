# AutoKool Web :oncoming_police_car: :oncoming_automobile:

Introducing a comprehensive Driving Theory Test app, designed to help aspiring drivers master the knowledge and skills required to pass their theory exams with confidence. The app offers a user-friendly interface and a vast collection of practice questions, covering all essential topics such as road signs, traffic regulations, and safe driving practices.

With our app, you can simulate real exam conditions by taking timed mock tests, allowing you to assess your progress and identify areas that require further improvement. Our detailed explanations for each question will help you understand the reasoning behind the correct answers, ensuring a deeper understanding of the material.

Key features of our Driving Theory Test app include:

1. Extensive Question Bank: Access a wide range of carefully curated questions, covering all aspects of the theory test.

2. Mock Tests: Take simulated exams with randomized questions and time limits to simulate the actual test experience.

3. Progress Tracking: Monitor your performance over time, track your scores, and identify areas for improvement.

4. Offline Access: Study anytime, anywhere, even without an internet connection.

Prepare yourself thoroughly for your driving theory test with our user-friendly and feature-rich app. Download now and embark on your journey towards becoming a confident and knowledgeable driver.

### Project Tech Stack:
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white&style=plastic) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot&style=plastic)
![Vaadin](https://img.shields.io/badge/Vaadin-00B4F0?style=for-the-badge&logo=Vaadin&logoColor=white&style=plastic)<br>
![Spring Security Badge](https://img.shields.io/badge/Spring%20Security-6DB33F?logo=springsecurity&logoColor=fff&style=plastic) ![Maven](https://img.shields.io/badge/Maven-red?logo=Apache+Maven&logoColor=white&style=plastic)<br>
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white&style=plastic) ![MySQL Badge](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=fff&style=flat&style=plastic) <br>

### Tests:
![JUnit](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white&style=plastic) ![Mockito](https://img.shields.io/badge/Mockito-success?logo=MEGA) ![Selenium](https://img.shields.io/badge/Selenium-CB02A?logo=Selenium&logoColor=white)

### Running the application

The project is a standard Maven project. To run it from the command line,
type `mvnw` (Windows), or `./mvnw` (Mac & Linux), then open
http://localhost:8080 in your browser.

You can also import the project to your IDE of choice as you would with any
Maven project. Read more on [how to import Vaadin projects to different IDEs](https://vaadin.com/docs/latest/guide/step-by-step/importing) (Eclipse, IntelliJ IDEA, NetBeans, and VS Code).

### Deploying to Production

To create a production build, call `mvnw clean package -Pproduction` (Windows),
or `./mvnw clean package -Pproduction` (Mac & Linux).
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `target` folder after the build completes.

Once the JAR file is built, you can run it using
`java -jar target/autokoolweb-1.0-SNAPSHOT.jar`

### Project structure

- `MainLayout.java` in `src/main/java` contains the navigation setup (i.e., the
  side/top bar and the main menu). This setup uses
  [App Layout](https://vaadin.com/docs/components/app-layout).
- `views` package in `src/main/java` contains the server-side Java views of your application.
- `views` folder in `frontend/` contains the client-side JavaScript views of your application.
- `themes` folder in `frontend/` contains the custom CSS styles.

