package mogodbDemo.atlas;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.List;

public class Movie {
    private final String id;
    private final String title;
    private final int year;
    private final int runtime;
    private final List<String> genres;
    private final List<String> directors;
    private final List<String> cast;
    private final double imdbRating;
    private final List<String> languages;

    public Movie(
            String id,
            String title,
            int year,
            List<String> genres,
            List<String> directors,
            List<String> cast,
            double imdbRating,
            List<String> languages,
            int runtime) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.runtime = runtime;
        this.genres = genres;
        this.directors = directors;
        this.cast = cast;
        this.imdbRating = imdbRating;
        this.languages = languages;
    }

    // Convert MongoDB document to Movie object 
    public static Movie fromDocument(Document doc) {
        if (doc == null) {
            return new Movie("", "", 0,
                    Collections.<String>emptyList(),
                    Collections.<String>emptyList(),
                    Collections.<String>emptyList(),
                    0.0,
                    Collections.<String>emptyList(),
                    0);
        }

        Document imdb = doc.get("imdb", Document.class);
        double rating = 0.0;
        if (imdb != null) {
            Object r = imdb.get("rating");
            if (r instanceof Number)
                rating = ((Number) r).doubleValue();
        }

        ObjectId objectId = doc.getObjectId("_id");
        String id = (objectId != null) ? objectId.toString() : "";

        String title = doc.getString("title");
        if (title == null)
            title = "";

        Integer yearVal = doc.getInteger("year");
        int year = (yearVal != null) ? yearVal : 0;

        Integer runtimeVal = doc.getInteger("runtime");
        int runtime = (runtimeVal != null) ? runtimeVal : 0;

        List<String> genres = doc.getList("genres", String.class);
        if (genres == null)
            genres = Collections.emptyList();

        List<String> directors = doc.getList("directors", String.class);
        if (directors == null)
            directors = Collections.emptyList();

        List<String> cast = doc.getList("cast", String.class);
        if (cast == null)
            cast = Collections.emptyList();

        List<String> languages = doc.getList("languages", String.class);
        if (languages == null)
            languages = Collections.emptyList();

        return new Movie(id, title, year, genres, directors, cast, rating, languages, runtime);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public int getRuntime() {
        return runtime;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<String> getCast() {
        return cast;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public List<String> getLanguages() {
        return languages;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", runtime=" + runtime +
                ", genres=" + genres +
                ", directors=" + directors +
                ", cast=" + cast +
                ", imdbRating=" + imdbRating +
                ", languages=" + languages +
                '}';
    }
}