# CRUD - Filtro

Uma requisição utilizando o método GET do protocolo HTTP contra um recurso que esteja estendendo da classe **AbstractREST** com o nome dos campos da entidade passados na query string retornará a quem solicitou o conjunto de dados filtrados com o que foi solicitado.

Um exemplo para esclarecer:

Temos uma aplicação com um recurso chamado **User** e uma classe chamada **UserREST** que estende de **AbstractREST**.

User.java
``` java
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    @Size(max = 100, min = 2)
    @Column(length = 100)
    @NotNull
    private String name;

    @Size(max = 100, min = 4)
    @Column(length = 100, unique = true)
    @NotNull
    private String email;
    
    @Column(length = 1)    
    private String type;

    // getters and setters
}
```

UserREST.java
``` java
@Path("users")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@RequestScoped
public class UserREST extends AbstractREST<User, Long> {

    @Override
    @GET    
    public Result find() {
        return bc.find();
    }
}
```

Quando uma requisição do tipo GET do protocolo HTTP é enviada para o recurso de usuários

``` bash
$ curl -XGET http://localhost:8080/api/users?id=1,2,3,4
```

O retorno da requisição acima será

``` bash
$ curl -XGET http://localhost:8080/api/users
< Accept-Range: user 20
< Access-Control-Allow-Methods: HEAD, OPTIONS, TRACE, GET, POST, PUT, PATCH, DELETE
< Access-Control-Expose-Headers: Accept-Range, Content-Range, Link
< .... outros headers
< 
[
    {"id":1,"name":"João 1", "email": "joao1@test.com", "type": "A"},
    {"id":2,"name":"João 2", "email": "joao2@test.com", "type": "A"},
    {"id":3,"name":"João 3", "email": "joao3@test.com", "type": "B"},
    {"id":4,"name":"João 4", "email": "joao4@test.com", "type": "C"}
]
```

O retorno acima demostra a aplicação de um filtro no campo 'id' com os valores 1 ou 2 ou 3 ou 4.

Para a requisição:

``` bash
$ curl -XGET http://localhost:8080/api/users?name=João 1,João 4&type=A
```

O retorno acima demostra uma filtragem dos usuários seguindo a regra:

(*name* = 'João 1' **OU** 'João 4') **E** (*type* = 'A')

No exemplo acima apenas o registro abaixo será retornado pois 'João 4' não tem o 'type' igual a 'A';

``` bash
    {"id":1,"name":"João 1", "email": "joao1@test.com", "type": "A"}
```
