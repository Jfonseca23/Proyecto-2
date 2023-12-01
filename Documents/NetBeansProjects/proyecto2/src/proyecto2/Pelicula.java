package proyecto2;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Pelicula {
    private static final String API_KEY = "e1dd8357febbf0ee0827cf7cc776bafb";

    private List<String> nombres = new ArrayList<>();
    private List<String> sinopsis = new ArrayList<>();

    public void buscarInformacion(String query) {
        try {
            URL apiUrl = new URL("https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=" + query);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            Gson gson = new Gson();

            ResultadoBusqueda resultado = gson.fromJson(reader, ResultadoBusqueda.class);

            if (resultado != null && resultado.results != null && !resultado.results.isEmpty()) {
                for (int i = 0; i < Math.min(3, resultado.results.size()); i++) {
                    PeliculaInfo peliculaEncontrada = resultado.results.get(i);
                    nombres.add(peliculaEncontrada.title);
                    sinopsis.add(peliculaEncontrada.overview);
                    // Puedes agregar más campos según la estructura real de la respuesta JSON
                }
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> obtenerNombres() {
        return nombres;
    }

    public List<String> obtenerSinopsis() {
        return sinopsis;
    }

    // Clase interna para representar la estructura de datos de la respuesta JSON
    class ResultadoBusqueda {
        List<PeliculaInfo> results;
    }

    class PeliculaInfo {
        String title;
        String overview;
        // Puedes agregar más campos según la estructura real de la respuesta JSON
    }
}
