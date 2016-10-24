# Configurações
## Arquivo persistence.xml
 O Framework Demoiselle fornece um produtor padrão para contextos de persistência da JPA. Esse produtor lê o arquivo de configuração persistence.xml de seu projeto e toma as providências necessárias para fabricar uma instância da classe EntityManager que pode ser usada para gerenciar as entidades de sua aplicação. Além disso, instâncias de EntityManager produzidas pelo Framework Demoiselle participam automaticamente de transações abertas através da anotação @Transactional, conforme apresentado no capítulo sobre Transações.
Dica

Para acrescentar a dependência à extensão demoiselle-jpa, adicione esse código em seu arquivo pom.xml, na seção dependencies.

```xml
<dependency>
    <groupId></groupId>
    <artifactId>demoiselle-persistence-jpa</artifactId>
    <scope>compile</scope>
</dependency>
```

Para injetar uma instância de EntityManager em sua aplicação, basta usar a anotação @Inject. 

```java
public class BookmarkDAO extends JPACrud<Bookmark, Long> {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager entityManager;

    public void persistBookmark(Bookmark bookmark){
        entityManager.persist(bookmark);
    }

}
```
O produtor padrão injetará o EntityManager configurado no arquivo persistence.xml. Se houver mais de um contexto de persistência configurado em persistence.xml, será necessário especificar qual será injetado no ponto de injeção. Para isso use a anotação @Name. 

```java
public class BookmarkDAO {

    private static final long serialVersionUID = 1L;

    @Inject
    @Name("persistence_unit_1")
    private EntityManager entityManager;

    public void persistBookmark(Bookmark bookmark){
        entityManager.persist(bookmark);
    }  

}
```
