# Java MongoDB Atlas - Functional Programming Assignment

This is my submission for Assignment 1 in the Functional Programming course (JAVA25 / JAVAD25). The goal of this project is to connect to a cloud MongoDB Atlas database (`sample_mflix`), download a dataset of movies, and answer 9 specific questions using **Java Streams, Lambdas, and Functional Programming principles**.

## Project Goals (Achieved VG Level)
This project implements both the G (Godkänd) and VG (Väl Godkänd) requirements from the assignment instructions:
- Answer all 9 questions using only Streams and Lambdas (no traditional `for`-loops).
- Implement at least one **Higher-Order Function**.
- **100% Immutability**: The `Movie.java` data class only uses `final` variables and no setters.
- **Pure Functions**: No side-effects. The stream pipelines (like my custom `.groupingBy` for Question 6) do not mutate external state variables or external lists.
- **Test-Driven Development (TDD)**: Written fully mocked, database-independent unit tests for all 10 functions using JUnit 5.

## How to setup and run the project

### 1. Requirements
- Java 17 or higher
- Maven
- A MongoDB Atlas account

### 2. Environment Variables (.env)
To protect database credentials from being leaked on GitHub, this project uses the `dotenv-java` library. 
Before running the project, you must create a file called `.env` in the root directory of the project.

Inside `.env`, add your MongoDB connection string like this:
```
MONGODB_URI="mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority"
```
*(Note: `.env` is already included in the `.gitignore` so your password stays safe).*

### 3. Running the Main Application
To run the analysis against the real Atlas database:
1. Ensure your `.env` file is set up.
2. Ensure your IP address is whitelisted in MongoDB Atlas Network Access.
3. Run the `main` method located in `src/main/java/mogodbDemo/atlas/MongoDBAtlasDownloadExample.java`.

### 4. Running the Unit Tests
The unit tests are written to be **offline-safe**. They mock the data using custom `Movie` objects and bypass the database connection entirely using an overloaded constructor (`public MongoDBAtlasDownloadExample(boolean isTest)`).

To run the tests with Maven via terminal:
```bash
mvn clean test
```
Or simply right-click `MongoDBAtlasDownloadExampleTest.java` in IntelliJ and click "Run".

## The 9 Questions Answered in this code:
1. How many movies were made in 1975? *(Note: I implemented a pure `.filter()` inside the stream instead of relying on the DB query!)*
2. Find the length of the movie that was longest.
3. How many UNIQUE genres did the movies from 1975 have?
4. Which actors played in the movie with the highest IMDB rating?
5. What is the title of the movie with the fewest listed actors?
6. How many actors were in more than 1 movie?
7. What was the name of the actor who was in the most movies?
8. How many UNIQUE languages do the movies have?
9. Is there any title shared by more than one movie?

**+ VG Question:** Higher order function that takes a `Predicate<Movie>` to count movies dynamically!
