import com.fasterxml.jackson.databind.ObjectMapper;
import user.User;
import user.UserService;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.delete;

public class Main {

    private static UserService userService = new UserService();
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {

        // Start embedded server at the following port
        port(8080);

        // Main page, welcome
        get("/", (request, response) -> "Welcome");

        // POST - Add an user
        post("/user/add", (request, response) -> {

            String name = request.queryParams("name");
            String email = request.queryParams("email");
            User user = userService.add(name, email);
            response.status(201);
            return objectMapper.writeValueAsString(user);
        });

        // GET - Gets a user by a given id
        get("/user/:id", (request, response) -> {

            User user = userService.findById(request.params(":id"));
            if(user != null) {
                return objectMapper.writeValueAsString(user);
            } else {
                response.status(404);
                return objectMapper.writeValueAsString("User not found.");
            }
        });

        // GET - Gets all users
        put("/user", (request, response) -> {
            List users = userService.findAll();
            if(users.isEmpty()) {
                return objectMapper.writeValueAsString("No users found");
            } else {
                return objectMapper.writeValueAsString(userService.findAll());
            }
        });

        // PUT - Updates user
         put("/user/:id", (request, response) -> {
            String id = request.params(":id");
            User user = userService.findById(id);
            if(user != null) {
                String name = request.params("name");
                String email = request.params("email");
                userService.update(id, name, email);
                return objectMapper.writeValueAsString("User with id " + id + " has been updated");
            } else {
                response.status(404);
                return objectMapper.writeValueAsString("User not found.");
            }
         });

        // DELETE - Delete user
        delete("/user/:id", (request, response) -> {
           String id = request.params(":id");
           User user = userService.findById(id);
           if(user != null) {
               userService.delete(id);
               return objectMapper.writeValueAsString("User with id" + id + " has been deleted.");
           } else {
               response.status(404);
               return objectMapper.writeValueAsString("User not found.");
           }
        });

    }
}
