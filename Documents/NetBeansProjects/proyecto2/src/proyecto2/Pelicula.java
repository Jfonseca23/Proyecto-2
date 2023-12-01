package proyecto2;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Pelicula {
    private static final String API_KEY = "e1dd8357febbf0ee0827cf7cc776bafb";

    private List<PeliculaInfo> peliculas = new ArrayList<>();

    public void buscarInformacion(String query) {
        try {
            URL apiUrl = new URL("https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=" + query + "&language=es-ES");
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            Gson gson = new Gson();

            ResultadoBusqueda resultado = gson.fromJson(reader, ResultadoBusqueda.class);

            if (resultado != null && resultado.results != null && !resultado.results.isEmpty()) {
                for (PeliculaInfo peliculaEncontrada : resultado.results) {
                    peliculas.add(peliculaEncontrada);
                }
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PeliculaInfo> obtenerPeliculas() {
        return peliculas;
    }

    class ResultadoBusqueda {
        List<PeliculaInfo> results;
    }

    class PeliculaInfo {
        int id;
        String title;
        String overview;
        String poster_path;
        String trailerUrl;  // Agregamos un campo para la URL del trailer

        // Constructor para simplificar la inicialización de trailerUrl
        public PeliculaInfo(int id, String title, String overview, String poster_path) {
            this.id = id;
            this.title = title;
            this.overview = overview;
            this.poster_path = poster_path;
            this.trailerUrl = obtenerUrlTrailer(id);
        }

        private String obtenerUrlTrailer(int movieId) {
            try {
                URL apiUrl = new URL("https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + API_KEY);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                Gson gson = new Gson();

                ResultadoVideos resultadoVideos = gson.fromJson(reader, ResultadoVideos.class);

                if (resultadoVideos != null && resultadoVideos.results != null && !resultadoVideos.results.isEmpty()) {
                    return "https://www.youtube.com/embed/" + resultadoVideos.results.get(0).key;  // Cambiamos a la URL embebida de YouTube
                }
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    class ResultadoVideos {
        List<VideoInfo> results;
    }

    class VideoInfo {
        String key;
    }
}
