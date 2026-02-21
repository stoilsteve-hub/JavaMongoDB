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
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.err.println("ERROR: The MONGODB_URI is not set correctly in the .env file.");
            System.err.println("Please ensure the .env file exists at the project root and contains your full MongoDB Atlas connection string.");
            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
            // Send a ping to confirm a successful connection
            MongoDatabase adminDb = mongoClient.getDatabase("admin");
            adminDb.runCommand(new Document("ping", 1));
            System.out.println("Pinged your deployment. You successfully connected to MongoDB!");

            // Now, proceed with the original logic of fetching movies
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> moviesCollection = database.getCollection("movies");

            List<Movie> movieList = new ArrayList<>();

            // Find movies from the year 1975
            moviesCollection
                    .find(new Document("year", 1975))
                    .forEach(doc -> movieList.add(Movie.fromDocument(doc)));

            System.out.println("Downloaded movies from 1975: " + movieList.size());

            // Print movies using a lambda forEach
            movieList.forEach(System.out::println);

        } catch (MongoException e) {
            System.err.println("Connection or query failed. Error:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MongoDBAtlasDownloadExample();
    }
}