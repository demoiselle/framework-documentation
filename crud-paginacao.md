# CRUD - Paginação

Uma requisição utilizando o método GET do protocolo HTTP contra um recurso que esteja estendendo da classe **AbstractREST** poderá ser paginada desde que a propriedade **demoiselle.crud.pagination.isGlobalEnabled** esteja habilitada e o número de itens por página seja menor que o retorno da requisição.

Um exemplo para esclarer:

Temos uma aplicação com um recurso chamado **User** e uma classe chamada **UserREST** que estende de **AbstractREST**. Na base de dados temos 100 usuários cadastrados e a propriedade padrão de itens por página esta definido em 20.

**demoiselle.properties**

```properties
demoiselle.crud.pagination.defaultPagination = 20 # Itens por página
demoiselle.crud.pagination.isGlobalEnabled = true # Habilita ou desabilita a paginação no nível Global
```

**UserREST.java**

```java
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

**User.java**

```java
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

Quando uma requisição do tipo GET do protocolo HTTP é enviada para o recurso de usuários

```bash
$ curl -XGET http://localhost:8080/api/users
```

A paginação não irá trazer os 100 registros que temos no banco, pois o valor de itens por página foi definida como 20 itens por página.

```properties
demoiselle.crud.pagination.defaultPagination = 20 # Itens por página
...
```

O retorno da requisição acima será

```bash
$ curl -XGET http://localhost:8080/api/users
< HTTP/1.1 206 Partial Content
< Accept-Range: user 20
< Access-Control-Allow-Methods: HEAD, OPTIONS, TRACE, GET, POST, PUT, PATCH, DELETE
< Access-Control-Expose-Headers: Accept-Range, Content-Range, Link
< Content-Range: 0-19/100
< Content-Type: application/json
< Link: <http://localhost:8080/users/?range=0-19>; rel="next",<http://localhost:8080/users/?range=80-99>; rel="last"
< .... outros headers
< 
[
    {"id":1,"name":"João 1","email":"j1@test.com"},
    {"id":6,"name":"Maria","email":"maria@test.com"},
    {"id":8,"name":"Outro","email":"outro@test.com"},
    // demais usuários até o 20º usuário
]
```

Pontos a serem observado:

* Por se tratar de um retorno paginado é possível observar que o código do retorno foi **206 - Partial Content** informando a quem solicitou que não retornaram todos os elementos, quando não houver mais elementos a serem paginados o código HTTP de retorno será **200 - OK**.
* Headers de retorno
  * Accept-Range: informa o recurso e a quantidade padrão da paginação \(user 20\);
  * Content-Range: informa o primeiro registro até o limite retornado e a quantidade de elementos existentes \(0-19/100\);
  * Link: retorna os links disponíveis para fazer a navegação do tipo 'first', 'next', 'prev' e 'last';
  * Access-Control-Expose-Headers: informa ao browser que os headers acima são acessíveis devido ao CORS.


## Paginação via Query String

É possível definir na URL qual é a quantidade e o recorte que você deseja realizar no recurso usando na Query String a palavra reservada **range** no formato:

```
$ curl http://localhost:8080/api/users/?range=10-12
```

No comando acima foi solicitado os usuários que estão na posição 10, 11 e 12 contabilizando ao todo 3 usuários.

```bash
$ curl -XGET http://localhost:8080/api/users?range=10-12
< HTTP/1.1 206 Partial Content
< Accept-Range: user 20
< Access-Control-Expose-Headers: Accept-Range, Content-Range, Link
< Access-Control-Allow-Methods: HEAD, OPTIONS, TRACE, GET, POST, PUT, PATCH, DELETE
< Content-Range: 10-12/100
< Content-Type: application/json
< Link: <http://localhost:8080/users/?range=0-2>; rel="first",<http://localhost:8080/users/?range=7-9>; rel="prev", <http://localhost:8080/users/?range=13-15>; rel="next",<http://localhost:8080/users/?range=97-99>; rel="last"
< .... outros headers
< 
[
    {"id":48,"name":"Mateus","email":"mt@test.com"},
    {"id":49,"name":"Otono","email":"otono@test.com"},
    {"id":50,"name":"Outro","email":"outro@test.com"}
]
```

Como no exemplo acima a requisição não foi no inicio da coleção de usuários o HEADER de retorno Link mostra a navagação completa de 'first', 'prev', 'next' e 'last' respeitando a quantidade de registros solicitados.

## Paginação via a anotação @Search

Pensando em um melhor controle para os desenvolvedores criamos a anotação **@Search** que é utilizada em conjunto com o método que trata sua requição do tipo **@GET**.

```java
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
    @Search(fields={...}, withPagination = true, quantityPerPage = 5)
    public Result otherFind() {
        return bc.find();
    }
}
```

Uma requisição contra a URL **http://localhost:8080/api/users/other** terá a quantidade de itens por página de **5** sobrescrevendo o valor padrão de **20** apenas para esse tipo de requisição.

É possível desabilitar a paginação para o método que utiliza a anotação **@Search** atribuindo **false** para a propriedade **withPagination**.

Descrição dos atributos da anotação **@Search**

| Atributo | Descrição |
| :--- | :--- |
| fields | Recebe um array de String com os campos da entidade definida em AbstractREST&lt;**ENTIDADE**, ...&gt;. <br><br>Essa propriedade auxilia o desenvolvedor a restrigir quais são os campos que o cliente poderá visualizar no retorno da requisição.<br><br> Caso um campo seja solicitada na Query String na URL que não esteja habilidado no campo **fields** da anotação **@Search** será retornado ao solicitante um erro **403 - Bad Request** informando o motivo. |
| withPagination | Habilita ou desabilita a paginação para o recurso. Caso a paginação global esteja desativada esse atributo será desconsiderado.<br><br> Valor padrão: true |
| quantityPerPage | Número de itens por página. <br><br>Valor padrão: 20 |



