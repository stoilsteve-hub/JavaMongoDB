package mogodbDemo.atlas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MongoDBAtlasDownloadExampleTest {

    private MongoDBAtlasDownloadExample exampleApp;
    private List<Movie> mockMovies;

    @BeforeEach
    void setUp() {
        // I set up testing constructor so it bypasses database connection!
        exampleApp = new MongoDBAtlasDownloadExample(true);

        // Mock data instead of real database
        mockMovies = Arrays.asList(
                new Movie("1", "Heated Rivalry", 1975,
                        Arrays.asList("Romance", "Drama"),
                        Arrays.asList("Fake Director 1"),
                        Arrays.asList("Actor A", "Ilya Rozanov", "Shane Hollander"),
                        8.0,
                        Arrays.asList("English"),
                        124),

                // Duplicate title to test question 9
                new Movie("2", "Heated Rivalry", 1975,
                        Arrays.asList("Romance"),
                        Arrays.asList("Fake Director 2"),
                        Arrays.asList("Actor A"),
                        4.0,
                        Arrays.asList("English"),
                        90),

                new Movie("3", "Red, White and Royal Blue", 1977,
                        Arrays.asList("Romance", "Comedy"),
                        Arrays.asList("Fake Director 3"),
                        Arrays.asList("Taylor Zakhar Perez", "Nicholas Galitzine"),
                        8.6,
                        Arrays.asList("English", "Spanish"),
                        121),

                new Movie("4", "Young Royals", 1972,
                        Arrays.asList("Drama", "Romance"),
                        Arrays.asList("Fake Director 4"),
                        Arrays.asList("Edvin Ryding", "Omar Rudberg"),
                        9.2,
                        Arrays.asList("Swedish", "English"),
                        175),

                new Movie("5", "Heartstopper", 2022,
                        Arrays.asList("Romance", "Drama"),
                        Arrays.asList("Fake Director 5"),
                        Arrays.asList("Joe Locke", "Kit Connor", "Actor A"),
                        9.8,
                        Arrays.asList("English"),
                        30));
    }

    @Test
    void testCountMoviesFrom1975() {
        // Only Heated Rivalry and Heated Rivalry (duplicate) are from 1975
        long count = exampleApp.countMoviesFrom1975(mockMovies);
        assertEquals(2, count);
    }

    @Test
    void testFindLongestRuntime() {
        // Young Royals is the longest at 175 minutes
        int longest = exampleApp.findLongestRuntime(mockMovies);
        assertEquals(175, longest);
    }

    @Test
    void testCountUniqueGenres() {
        // Genres across the 5 movies: Romance, Drama, Comedy -> 3 unique
        long uniqueGenres = exampleApp.countUniqueGenres(mockMovies);
        assertEquals(3, uniqueGenres);
    }

    @Test
    void testGetActorsInHighestRatedMovie() {
        // Heartstopper is highest rated at 9.8
        List<String> topActors = exampleApp.getActorsInHighestRatedMovie(mockMovies);
        assertEquals(3, topActors.size());
        assertTrue(topActors.contains("Joe Locke"));
    }

    @Test
    void testGetMovieWithFewestActors() {
        // The duplicate Heated Rivalry has only 1 actor ("Actor A")
        String title = exampleApp.getMovieWithFewestActors(mockMovies);
        assertEquals("Heated Rivalry", title);
    }

    @Test
    void testCountActorsInMultipleMovies() {
        // Only "Actor A" is in 3 movies (Heated Rivalry 1, Heated Rivalry 2,
        // Heartstopper)
        long count = exampleApp.countActorsInMultipleMovies(mockMovies);
        assertEquals(1, count);
    }

    @Test
    void testGetMostFrequentActor() {
        // Actor A appears 3 times
        String actor = exampleApp.getMostFrequentActor(mockMovies);
        assertEquals("Actor A", actor);
    }

    @Test
    void testCountUniqueLanguages() {
        // Languages across all mock movies: English, Spanish, Swedish -> 3 unique
        long count = exampleApp.countUniqueLanguages(mockMovies);
        assertEquals(3, count);
    }

    @Test
    void testHasDuplicateTitles() {
        // We artificially added a duplicate "Heated Rivalry" title
        boolean hasDuplicates = exampleApp.hasDuplicateTitles(mockMovies);
        assertTrue(hasDuplicates);
    }

    @Test
    void testCountMoviesMatchingCondition_HigherOrderFunction() {
        // Test counting movies with runtime > 120
        // (Heated Rivalry 1 [124], Red White Royal Blue [121], Young Royals [175] = 3)
        long count = exampleApp.countMoviesMatchingCondition(mockMovies, m -> m.getRuntime() > 120);
        assertEquals(3, count);
    }
}