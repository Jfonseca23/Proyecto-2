package proyecto2;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PeliculaApp extends Application {
    private static final String API_KEY = "e1dd8357febbf0ee0827cf7cc776bafb";

    private List<String> nombres = new ArrayList<>();
    private List<String> sinopsis = new ArrayList<>();
    private List<String> imagenes = new ArrayList<>();
    private List<String> trailers = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Crear el contenedor principal (VBox)
        VBox vbox = new VBox();

        // Crear el panel de desplazamiento
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vbox);

        // Agregar el panel de desplazamiento a la escena
        Scene scene = new Scene(scrollPane, 600, 400);

        // Configurar el escenario
        primaryStage.setTitle("Peliculas App");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Crear un campo de texto para la búsqueda
        TextField textField = new TextField();

        // Crear un botón para iniciar la búsqueda
        Button buscarButton = new Button("Buscar");
        buscarButton.setOnAction(e -> {
            // Limpiar listas antes de realizar una nueva búsqueda
            nombres.clear();
            sinopsis.clear();
            imagenes.clear();
            trailers.clear();

            // Realizar la búsqueda con el texto ingresado en el campo
            buscarInformacion(textField.getText());

            // Limpiar el VBox antes de agregar nuevos resultados
            vbox.getChildren().clear();

            // Agregar los resultados al VBox
            for (int i = 0; i < nombres.size(); i++) {
                Label label = new Label(nombres.get(i) + "\n" + sinopsis.get(i));

                // Agregar una ImageView para mostrar la imagen de portada
                ImageView imageView = new ImageView(new Image(imagenes.get(i), 100, 150, true, true));

                // Crear un VBox para mostrar título, sinopsis, imagen de portada y botón para reproducir el trailer
                VBox movieInfoVBox = new VBox(label, imageView);

                // Agregar botón para reproducir el trailer
                Button trailerButton = new Button("Ver Trailer");
                int finalI = i;  // Variable final para ser utilizada dentro de la expresión lambda
                trailerButton.setOnAction(e1 -> abrirTrailer(trailers.get(finalI)));
                movieInfoVBox.getChildren().add(trailerButton);

                vbox.getChildren().add(movieInfoVBox);
            }
        });

        // Agregar el campo de texto y el botón al VBox
        vbox.getChildren().addAll(textField, buscarButton);
    }

    public void buscarInformacion(String query) {
        try {
            // Agregar el parámetro de idioma para obtener resultados en español
            URL apiUrl = new URL("https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=" + query + "&language=es-ES");
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            Gson gson = new Gson();

            ResultadoBusqueda resultado = gson.fromJson(reader, ResultadoBusqueda.class);

            if (resultado != null && resultado.results != null && !resultado.results.isEmpty()) {
                for (PeliculaInfo peliculaEncontrada : resultado.results) {
                    nombres.add(peliculaEncontrada.title);
                    sinopsis.add(peliculaEncontrada.overview);
                    imagenes.add("https://image.tmdb.org/t/p/w500" + peliculaEncontrada.poster_path);
                    trailers.add(obtenerUrlTrailer(peliculaEncontrada.id));  // Obtener la URL del trailer
                }
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String obtenerUrlTrailer(int movieId) {
        try {
            URL apiUrl = new URL("https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            Gson gson = new Gson();

            ResultadoVideos resultadoVideos = gson.fromJson(reader, ResultadoVideos.class);

            if (resultadoVideos != null && resultadoVideos.results != null && !resultadoVideos.results.isEmpty()) {
                return "https://www.youtube.com/watch?v=" + resultadoVideos.results.get(0).key;  // Tomar el primer video de la lista (puedes ajustar esto según tus necesidades)
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";  // Devolver una cadena vacía si no se encuentra un trailer
    }

    private void abrirTrailer(String trailerUrl) {
        // Este método debería abrir el enlace del trailer en el navegador web predeterminado
        // Puedes implementar esto utilizando java.awt.Desktop o utilizando bibliotecas de terceros si es necesario
        // Ten en cuenta que el comportamiento puede variar según el sistema operativo
        // Aquí se presenta una implementación simple utilizando java.awt.Desktop
        try {
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().browse(new java.net.URI(trailerUrl));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Clase interna para representar la estructura de datos de la respuesta JSON de videos
    class ResultadoVideos {
        List<VideoInfo> results;
    }

    class VideoInfo {
        String key;  // Clave del video (se utiliza para construir la URL de YouTube)
        // Puedes agregar más campos según la estructura real de la respuesta JSON
    }

    // Clase interna para representar la estructura de datos de la respuesta JSON
    class ResultadoBusqueda {
        List<PeliculaInfo> results;
    }

    class PeliculaInfo {
        String title;
        String overview;
        String poster_path;
        int id;
    }
}
