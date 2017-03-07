# CRUD

O módulo tem os facilitadores de manutenção dos dados \([https://pt.wikipedia.org/wiki/CRUD](https://pt.wikipedia.org/wiki/CRUD)\) nele temos duas configurações descritas a seguir:

Para usar o módulo faça a importação:

```bash
<dependency>
   <groupId>org.demoiselle.jee</groupId>
   <artifactId>demoiselle-crud</artifactId>
   <version>3.0.0</version>
</dependency>
```

Caso você crie sua app a partir de um parent

```bash
    <parent>
        <groupId>org.demoiselle.jee</groupId>
        <artifactId>demoiselle-parent</artifactId>
        <version>3.0.0</version>
    </parent>
```

Ele já agrega o módulo Rest, não há necessidade de colocar a dependência.

Exemplos de uso do CRUD

```bash
@Api("User")
@Path("users")
public class UserREST extends AbstractREST<User, String> {

}
```

```bash
public class UserBC extends AbstractBusiness<User, String> {

}
```

```bash
public class UserDAO extends AbstractDAO<User, String> {

}
```



