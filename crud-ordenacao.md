# CRUD - Ordenação

Uma requisição utilizando o método GET do protocolo HTTP contra um recurso que esteja estendendo da classe **AbstractREST** com o parâmetro '**sort=**' passado na query string retornará a quem solicitou o conjunto de dados ordenado de acordo com os campos informados no parâmetro '**sort=**'.

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
$ curl -XGET http://localhost:8080/api/users?sort=id,name&desc=email
```

A requisição acima irá retornar uma lista de usuários ordenado por 'id' e 'name' de forma crescente e o 'email' de forma decrescente.

Exemplos de requisições:

URL | Retorno
----|--------
users?sort=name | Lista de usuários ordenado de forma crescente pelo campo 'name' |
users?sort=id,name,email&desc | Lista de usuários ordenados de forma decrescente seguindo a ordem dos campos 'id', 'name' e 'email' |
users?sort=id,name,email&desc=name | Lista de usuários respeitando a sequência definida, ou seja, 'id' crescente seguido por 'name' decrescente seguido por email crescente |

