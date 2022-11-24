package mx.uv;
import static spark.Spark.*;

import com.google.gson.*;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static Gson gson = new Gson();
    private static Map<String, Usuario> usuarios = new HashMap<>();

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        port(getHerokuAssignedPort());

        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });
        before((req, res) -> res.header("Access-Control-Allow-Origin", "*"));

        get("/usuarios", (req, res) -> gson.toJson(DAO.dameUsuarios()));

        post("/", (req, res) -> {
            String payload = req.body();
            String id = UUID.randomUUID().toString();
            Usuario u = gson.fromJson(payload, Usuario.class);
            u.setId(id);
            // usuarios.put(id, u);
            DAO dao = new DAO();

            JsonObject objetoJson = new JsonObject();
            objetoJson.addProperty("status", dao.crearUsuario(u));
            objetoJson.addProperty("id", id);
            return objetoJson;


            // String datosFormulario = req.body();
            // Usuario u = gson.fromJson(datosFormulario, Usuario.class);
            // // usuarios.put(u.getId(), u);
            // // return "usuario agregado";
            // return DAO.crearUsuario(u);
        });
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
