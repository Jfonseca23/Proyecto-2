package proyecto2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class MovieSearchApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Movie Search App");

        TextField searchField = new TextField();
        searchField.setPromptText("Ingrese el título de la película");

        Label resultLabel = new Label();

        VBox layout = new VBox(10);
        layout.getChildren().addAll(searchField, resultLabel);

        Scene scene = new Scene(layout, 400, 200);
        primaryStage.setScene(scene);

        primaryStage.show();

        Pelicula pelicula = new Pelicula();

        searchField.setOnAction(e -> {
            String query = searchField.getText();
            pelicula.buscarInformacion(query);

            List<String> nombres = pelicula.obtenerNombres();
            List<String> sinopsis = pelicula.obtenerSinopsis();

            if (!nombres.isEmpty()) {
                StringBuilder resultadoTexto = new StringBuilder("Resultados:\n");
                for (int i = 0; i < nombres.size(); i++) {
                    resultadoTexto.append("Película ").append(i + 1).append(":\n");
                    resultadoTexto.append("Nombre: ").append(nombres.get(i)).append("\n");
                    resultadoTexto.append("Sinopsis: ").append(sinopsis.get(i)).append("\n\n");
                }
                resultLabel.setText(resultadoTexto.toString());
            } else {
                resultLabel.setText("No se encontraron resultados.");
            }
        });
    }
}
