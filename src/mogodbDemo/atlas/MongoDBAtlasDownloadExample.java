package mogodbDemo.atlas;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDBAtlasDownloadExample {

    public MongoDBAtlasDownloadExample() {

        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.load();
        String uri = dotenv.get("MONGODB_URI");

        if (uri == null || uri.isEmpty() || uri.contains("<db_password>")) {
            System.err.println("ERROR: The MONGODB_URI is not set correctly in the .env file.");
            System.err.println(
                    "Please ensure the .env file exists at the project root and contains your full MongoDB Atlas connection string.");
            return;
        }

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .serverApi(serverApi)
                .build();

        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            // Successful connection
            MongoDatabase adminDb = mongoClient.getDatabase("admin");
            adminDb.runCommand(new Document("ping", 1));
            System.out.println("You successfully connected to MongoDB!");

            // Fetching movies
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> moviesCollection = database.getCollection("movies");

            List<Movie> movieList = new ArrayList<>();

            // Find movies from the year 1975
            moviesCollection
                    .find(new Document("year", 1975))
                    .forEach(doc -> movieList.add(Movie.fromDocument(doc)));

            System.out.println("Downloaded movies from 1975: " + movieList.size());

            // I commented this out because it prints thousands of lines and hides my
            // answers
            // movieList.forEach(System.out::println);

            // --- MY ASSIGNMENT CODE STARTS HERE ---

            // 1. How many movies were made in 1975?
            long numberOfMovies = countMoviesFrom1975(movieList);
            System.out.println("1. Number of movies from 1975: " + numberOfMovies);

            // 2. Find the length of the movie that was longest
            int longestRuntime = findLongestRuntime(movieList);
            System.out.println("2. Longest runtime: " + longestRuntime + " minutes");

            // 3. How many UNIQUE genres did the movies from 1975 have?
            long uniqueGenresCount = countUniqueGenres(movieList);
            System.out.println("3. Number of unique genres: " + uniqueGenresCount);

            // 4. Which actors played in the movie with the highest IMDB rating?
            List<String> topActors = getActorsInHighestRatedMovie(movieList);
            System.out.println("4. Actors in highest rated movie: " + topActors);

            // 5. What is the title of the movie with the fewest listed actors?
            String titleFewestActors = getMovieWithFewestActors(movieList);
            System.out.println("5. Movie with fewest actors: " + titleFewestActors);

            // 6. How many actors were in more than 1 movie?
            long actorsInMultipleMovies = countActorsInMultipleMovies(movieList);
            System.out.println("6. Actors in more than one movie: " + actorsInMultipleMovies);

        } catch (MongoException e) {
            System.err.println("Connection or query failed. Error:");
            e.printStackTrace();
        }
    }

    // 1. Counts how many movies are in the list
    public long countMoviesFrom1975(List<Movie> movies) {
        // I use a stream to just count the movies
        return movies.stream().count();
    }

    // 2. Finds the highest runtime
    public int findLongestRuntime(List<Movie> movies) {
        // I map to int so I can use max() to find the longest one
        return movies.stream()
                .mapToInt(Movie::getRuntime)
                .max()
                .orElse(0);
    }

    // 3. Counts how many unique genres there are
    public long countUniqueGenres(List<Movie> movies) {
        // flatMap puts all genres in one stream, distinct removes duplicates
        return movies.stream()
                .flatMap(movie -> movie.getGenres().stream())
                .distinct()
                .count();
    }

    // 4. Returns the actors from the movie with the highest rating
    public List<String> getActorsInHighestRatedMovie(List<Movie> movies) {
        // I find the max movie by comparing IMDB ratings, then return its cast
        // If the list is completely empty, I return an empty list
        return movies.stream()
                .max((m1, m2) -> Double.compare(m1.getImdbRating(), m2.getImdbRating()))
                .map(Movie::getCast)
                .orElse(new ArrayList<>());
    }

    // 5. Gets the title of the movie with the fewest actors
    public String getMovieWithFewestActors(List<Movie> movies) {
        // I use min to find the movie with the smallest cast list size,
        // then I map it to just get the title
        return movies.stream()
                .min((m1, m2) -> Integer.compare(m1.getCast().size(), m2.getCast().size()))
                .map(Movie::getTitle)
                .orElse("No movie found");
    }

    // 6. Counts how many actors are in more than 1 movie
    public long countActorsInMultipleMovies(List<Movie> movies) {
        // I make one big list of every actor in every movie
        List<String> allActors = movies.stream()
                .flatMap(movie -> movie.getCast().stream())
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.toList());

        // I filter the distinct actors by checking if their frequency in the big list
        // is > 1
        return allActors.stream()
                .distinct()
                .filter(actor -> java.util.Collections.frequency(allActors, actor) > 1)
                .count();
    }

    public static void main(String[] args) {
        new MongoDBAtlasDownloadExample();
    }
}