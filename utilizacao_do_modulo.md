# Multitenancy - Utilização do Módulo

Nas páginas anteriores sobre Multitenancy foram detalhados os passos para a criação da estrutura necessária para a funcionalidade, contudo o Demoiselle fornece um módulo especifico que permite com algumas configurações ter as mesmas funcionalidades.

O primeiro passo é adicionar o módulo de Multitenancy ao seu projeto.

```xml
<dependency>
    <groupId>org.demoiselle.jee</groupId>
    <artifactId>demoiselle-multitenancy-hibernate</artifactId>
</dependency>
```

> É importante fazer a ação de `maven update` no projeto após adicionar o novo módulo.

Adicione as seguintes configurações ao seu arquivo `demoiselle.properties`.

```
# SQL
demoiselle.multiTenancyMasterDatabase=user_master
demoiselle.multiTenancySetDatabaseSQL=USE
demoiselle.multiTenancyCreateDatabaseSQL=CREATE DATABASE IF NOT EXISTS
demoiselle.multiTenancyDropDatabaseSQL=DROP DATABASE IF EXISTS
demoiselle.multiTenancyTenantDatabasePrefix=user_

# Data source used for Hibernate
demoiselle.multiTenancyTenantsDatabaseDatasource=java:jboss/datasources/UserTenantsDS
demoiselle.multiTenancyMasterDatabaseDatasource=java:jboss/datasources/UserMasterDS

# DDL Files Path
demoiselle.multiTenancyCreateDatabaseDDL=../standalone/tmp/demoiselleMultiTenantCreate_User.ddl
demoiselle.multiTenancyDropDatabaseDDL=../standalone/tmp/demoiselleMultiTenantDrop_User.ddl
```

> As configurações acima irão variar de acordo com o banco utilizado, pois as intruções de seleção, create e drop necessárias normalmente não são idênticas. O exemplo acima refere-se ao banco de dados MySQL.

É necessário que existam 3 `persistence units` para a utilização do módulo:  
1. Unidade de persistência que acessa as bases dos Tenants, lembrando que na estratégia adotada cada Tenant terá um Schema separado  
2. Unidade de persistência que acessa a base de dados Master que contém os Tenants  
3. Unidade de persistência que gera os DDLs necessários para a criação do Tenant

Segue abaixo um exemplo de `persistence.xml`.

```xml
<persistence-unit name="UserTenantsPU" transaction-type="JTA">
    <provider>org.hibernate.ejb.HibernatePersistence</provider>
    <jta-data-source>java:jboss/datasources/UserTenantsDS</jta-data-source>
    <class>org.demoiselle.jee7.example.store.user.entity.User</class>
    <properties>
        <property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect" />
        <property name="hibernate.multiTenancy" value="SCHEMA" />
        <property name="hibernate.tenant_identifier_resolver"
            value="org.demoiselle.jee.multitenancy.hibernate.dao.SchemaResolver" />
        <property name="hibernate.multi_tenant_connection_provider"
            value="org.demoiselle.jee.multitenancy.hibernate.dao.MultiTenantProvider" />
    </properties>
</persistence-unit>

<persistence-unit name="UserMasterPU" transaction-type="JTA">
    <provider>org.hibernate.ejb.HibernatePersistence</provider>
    <jta-data-source>java:jboss/datasources/UserMasterDS</jta-data-source>
    <class>org.demoiselle.jee.multitenancy.hibernate.entity.Tenant</class>
    <exclude-unlisted-classes>true</exclude-unlisted-classes>
    <properties>
        <property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect" />
        <property name="hibernate.hbm2ddl.auto" value="update" />
        <property name="hibernate.show_sql" value="false" />        
    </properties>
</persistence-unit>

<persistence-unit name="UserExportPU" transaction-type="JTA">
    <provider>org.hibernate.ejb.HibernatePersistence</provider>
    <jta-data-source>java:jboss/datasources/UserTenantsDS</jta-data-source>
    <class>org.demoiselle.jee7.example.store.user.entity.User</class>
    <exclude-unlisted-classes>true</exclude-unlisted-classes>
    <properties>
        <property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect" />
        <property name="javax.persistence.schema-generation.scripts.action"
            value="drop-and-create" />
        <property name="javax.persistence.schema-generation.scripts.create-target"
            value="../standalone/tmp/demoiselleMultiTenantCreate_User.ddl" />
        <property name="javax.persistence.schema-generation.scripts.drop-target"
            value="../standalone/tmp/demoiselleMultiTenantDrop_User.ddl" />
    </properties>
</persistence-unit>
```

As Uniaddes de Persistência acima deverão estar apontadas para Fontes de Dados \(Data Sources\) que estarão no `standalone.xml` do servidor de aplicação.

Abaixo um exemplo de arquivo contendo os data sources para MySQL.

```xml
<datasource jta="true" jndi-name="java:jboss/datasources/UserTenantsDS" pool-name="UserTenanciesDS" enabled="true" use-java-context="true">
    <connection-url>jdbc:mysql://localhost</connection-url>
    <driver>com.mysql</driver>
    <security>
        <user-name>root</user-name>
        <password>root</password>
    </security>
    <validation>
        <validate-on-match>false</validate-on-match>
        <background-validation>false</background-validation>
    </validation>
    <statement>
        <share-prepared-statements>false</share-prepared-statements>
    </statement>
</datasource>
<datasource jta="true" jndi-name="java:jboss/datasources/UserMasterDS" pool-name="UserMasterDS" enabled="true" use-java-context="true">
    <connection-url>jdbc:mysql://localhost/user_master</connection-url>
    <driver>com.mysql</driver>
    <security>
        <user-name>root</user-name>
        <password>root</password>
    </security>
    <validation>
        <validate-on-match>false</validate-on-match>
        <background-validation>false</background-validation>
    </validation>
    <statement>
        <share-prepared-statements>false</share-prepared-statements>
    </statement>
</datasource>
<drivers>
    <driver name="com.mysql" module="com.mysql">
        <xa-datasource-class>com.mysql.jdbc.jdbc2.optional.MysqlXADataSource</xa-datasource-class>
    </driver>
</drivers>
```

Para que o módulo funcione corretamente é preciso que seja criado um Entity Manager que será injetado no módulo para ter acesso a base Master que contém os Tenants.

```java
public class EntityManagerMasterDAO implements EntityManagerMaster {
    @PersistenceContext(unitName = "UserMasterPU")
    protected EntityManager emEntity;

    public EntityManager getEntityManager() {
        return emEntity;
    }
}
```

Por fim o módulo não fornece os serviços REST por acreditarmos que cada sistema deve implementar a sua maneira, contudo são fornecidos facilitadores que cuidam de boa parte do processo.

Abaixo um exemplo de serviços REST que utiliza a classe TenantManager que permite que Tenants sejam criados e excluídos utilizando todas as regras de manipulação de banco.

```java
@Path("tenant")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class TenantREST {

    @Inject
    private TenantManager tenantManager;

    @GET
    @Cors
    public Result listAllTenants() throws Exception {
        return tenantManager.find();
    }

    @DELETE
    @Path("{id}")
    @Cors
    public Response deleteTenant(@PathParam("id") Long id) throws Exception {
        try {
            tenantManager.removeTenant(id);
            return Response.ok().build();
        } catch (final Exception e) {
            return Response.serverError().build();
        }
    }

    @POST    
    @ValidatePayload
    @Cors
    public Response createTenant(Tenant tenant) throws Exception {
        try {
            tenantManager.createTenant(tenant);
            return Response.ok().build();
        } catch (final Exception e) {
            return Response.serverError().build();
        }
    }
}
```

## Últimas Considerações

* O único schema que não será criado automaticamente \(mas esta configurado para criar as tabelas na propriedade **hibernate.hbm2ddl.auto com** valor **update**\) é a base master que nos exemplos acima é a user\_master
* O sistema deverá ter acesso ROOT ao banco de dados, ou algo que permita a criação e exclusão de schemas
* Cada tenant terá um schema para os seus dados, não compartilhando tabelas
* No caso de necessitar fazer update em todas as bases já criadas dos Tenants este processo deverá ser feito manualmente



