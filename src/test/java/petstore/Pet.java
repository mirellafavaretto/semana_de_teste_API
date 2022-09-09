// 1 - pacote
package petstore;
//bibliotecas


import org.testng.annotations.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;

// 2 - classe
public class Pet {
    //3.1 atributos (caracteristicas)
    String uri = "https://petstore.swagger.io/v2/pet"; //endereço da entidade Pet

    //3.2 - métodos e funções
    public String lerJson(String caminhoJson) throws IOException { //função para fazer a leitura do JSON
        return new String(Files.readAllBytes(Paths.get(caminhoJson))); //função de java para fazer a leitura
    }

    // Incluir - Create - Post

    @Test (priority = 1) //identifica o método ou função como um teste para o TestNG ou Junit, o priority a gente coloca no grau de prioridade para execução dos testes
    public void incluirPet() throws IOException { //post
        String jsonBody = lerJson("db/pet1.json");

        //Sintaxe Gherkin
        // Dado - Quando - Então
        // Given - When - Then

        // a partir daqui estamos de fato usando o rest assured
        given()
                .contentType("application/json") //comum em API REST - antigas era "text/html"
                .log().all() //log do inicio
                .body(jsonBody)
        .when()
                .post(uri)
        .then()
                .log().all() // log do final
                .statusCode(200)
                .body("name", is("Atena")) //importar core matches no is que ficou em vermelho
                .body("status", is("available"))
                .body("category.name", is("dog")) //importar matchers contains, validamos assim pois esse elemento está "dentro" de outro lá no Json, com is usamos para quando não tiver colchetes [] no JSON
                .body("tags.name", contains("sta"))

        ;
    }

    @Test (priority = 2)
    public void consultarPet(){ //get
        String petId = "1996020302";

        given()
                .contentType("application/json")
                .log().all()

        .when()
                .get(uri + "/" + petId)

        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Atena"))
                .body("status", is("available"))
                .body("category.name", is("dog"))
                .body("tags.name", contains("sta"))

        ;
    }

    @Test(priority = 3)
    public void alterarPet() throws IOException { //put
        String jsonBody = lerJson("db/pet2.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .put(uri)

        .then()
                .log().all() // log do final
                .statusCode(200)
                .body("name", is("Atena"))
                .body("status", is("sold"))
        ;
    }

    @Test (priority = 4)
    public void excluirPet(){
        String petId = "1996020302";

        given()
                .contentType("application/json")
                .log().all()

        .when()
                .delete(uri + "/" + petId)

        .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is(petId))
        ;
    }


}
