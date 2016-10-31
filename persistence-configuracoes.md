# Configurações (Em desenvolvimento)
## Arquivo persistence.xml

O Framework Demoiselle utiliza como padrão a especificação JPA 

O Framework Demoiselle fornece um produtor padrão para contextos de persistência da JPA. Esse produtor lê o arquivo de configuração persistence.xml de seu projeto e toma as providências necessárias para fabricar uma instância da classe EntityManager que pode ser usada para gerenciar as entidades de sua aplicação. Além disso, instâncias de EntityManager produzidas pelo Framework Demoiselle participam automaticamente de transações abertas através da anotação @Transactional, conforme apresentado no capítulo sobre Transações.
Dica


```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.1"
	xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">

	<persistence-unit name="ExamplePU" transaction-type="JTA">
		<provider>org.hibernate.ejb.HibernatePersistence</provider>
		<jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>
		<class>meu.pacote.MinhaClasse</class>
		<properties>
			<property name="hibernate.dialect" value="org.hibernate.dialect.HSQLDialect" />
			<property name="hibernate.hbm2ddl.auto" value="create-drop" />
		</properties>
	</persistence-unit>

</persistence>
```

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
public class MeuDAO {

    @PersistenceContext(unitName = "ExamplePU")
    private EntityManager entityManager;

    public void persist(MeuObjeto objeto){
        entityManager.persist(objeto);
    }

}


