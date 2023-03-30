import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.InputStream;
import java.net.URI;

public class App {
    public static void main(String[] args) throws Exception {
        // Diretorio de saida das figurinhas
        String pathSaida = "./saida/";
        File dirSaida = new File(pathSaida);

        // Array com "apis" de filmes e series por nota e popularidade
        String[] urlStrings = {
                "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopMovies.json",
                "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopTVs.json",
                "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/MostPopularMovies.json",
                "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/MostPopularTVs.json"
        };

        // Cria um cliente HTTP, e faz a requisição para a URL, e retorna o resultado
        // para variavel body
        var client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(urlStrings[0]))
                .build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        String body = res.body();

        // Chama o método parse da classe JsonParser, e retorna o resultado para
        // variavel movies
        var parser = new JsonParser();
        List<Map<String, String>> movies = parser.parse(body);
        // Imprime os top 10 filmes por nota

        var criador = new CriadorDeFigurinhas();
        // Checa se a pasta existe, se não existir, cria
        if (!dirSaida.exists()) {
            dirSaida.mkdir();
        }
        System.out.println("\u001b[1m \u001b[44m Top 10 Filmes por nota: \u001b[m");
        for (Map<String, String> map : movies) {
            // Pega o rank e converte para inteiro para por o emoji das medalhas :)
            int rank = Integer.parseInt(map.get("rank"));
            System.out.println("--------------------------------------------------");
            if (rank <= 3) {
                System.out.println("|\u001b[1m " + Character.toString(0x1F946 + rank) + " " + map.get("rank") + " - "
                        + map.get("title"));
            } else {
                System.out.println("|\u001b[1m " + map.get("rank") + " - " + map.get("title"));
            }
            System.out.println("\u001b[0m| \u2B50 Nota: " + map.get("imDbRating") +
                    " \uD83D\uDDD3\uFE0F  Ano: " + map.get("year"));

            // Cria umas figurinhas top
            InputStream img = URI.create(map.get("image")).toURL().openStream();
            // Tira os ":" do titulo para não dar erro na hora de salvar a imagem
            String fileName = (pathSaida + (map.get("title").replace(":", "") + ".png"));
            criador.criaFigurinha(img, fileName);
        }
    }
}
