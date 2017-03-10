# CRUD - Campo

Uma requisição utilizando o método GET do protocolo HTTP contra um recurso que esteja estendendo da classe **AbstractREST** com o parâmetro **fields=** passado na query string retornará a quem solicitou o conjunto de dados somente com os campos descritos no parâmentro **fields=**.

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
$ curl -XGET http://localhost:8080/api/users?fields=id,name
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
    {"id":1,"name":"João 1"},
    {"id":6,"name":"Maria"},
    {"id":8,"name":"Outro"},
    // demais usuários
]
```

Apesar da entidade **User** ter os campos 'id', 'name' e 'address' apenas os campos 'id' e 'name' foram retornados na requisição respeitando o pedido feito no parâmetro **fields=id,name**

## Campos via annotação @Search

Pensando em um melhor controle para os desenvolvedores criamos a anotação @Search que é utilizada em conjunto com o método que trata sua requição do tipo @GET.

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

    @GET
    @Path("other")
    @Search(fields={"id", "name"}) // Referente aos campos da entidade User definida na declaração do AbstractREST<User, Long>
    public Result otherFind() {
        return bc.find();
    }
}
```

Uma requisição contra a URL http://localhost:8080/api/users/other retornará uma lista com os usuários porém apenas os campos 'id' e 'name' serão retornados, ignorando o campo 'address' como foi definido na anotação @Search(fields={"id", "name"}).

Caso um campo seja solicitada na Query String na URL que não esteja habilidado no campo fields da anotação @Search será retornado ao solicitante um erro **403 - Bad Request** informando o motivo.

## Campos de 2º nível

Quando uma classe tem como um campo sendo outra classe como **User** tem um **Profile** é possível definir quais campos da entidade **Profile** será apresentada no retorno.

User.java
``` java
public class User {

    private Long id;
    private String name;
    private String email;

    private Profile profile;

    // getters and setters
}
```

Profile.java
``` java
public class Profile {

    private Long id;
    private String username;
    private String password; // Dado sensível

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

    @GET
    @Path("other")
    @Search(fields={"id", "name", "profile(username)"}, withPagination = true, quantityPerPage = 5)
    public Result otherFind() {
        return bc.find();
    }
}
```

Quando uma requisição do tipo GET do protocolo HTTP é enviada para o recurso de usuários

``` bash
$ curl -XGET http://localhost:8080/api/users/other
```

O retorno da requisição acima será

``` bash
$ curl -XGET http://localhost:8080/api/users/other
< Accept-Range: user 20
< Access-Control-Allow-Methods: HEAD, OPTIONS, TRACE, GET, POST, PUT, PATCH, DELETE
< Access-Control-Expose-Headers: Accept-Range, Content-Range, Link
< .... outros headers
< 
[
    {"id":1,"name":"João 1", "profile": {"username": "joao1"}},
    {"id":6,"name":"Maria", "profile": {"username": "maria"}},
    {"id":8,"name":"Outro", "profile": {"username": "outro"}},
    // demais usuários
]
```

Também é possível utilizar a Query String na requisição para restringir os campos de retorno no 2º nível.

```bash
$ curl -XGET http://localhost:8080/api/users/other?fields=name,profile(username)
```