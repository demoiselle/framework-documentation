# Utilização do Módulo
Nas páginas anteriores sobre Multitenancy foram detalhados os passos para a criação da estrutura necessária para a funcionalidade, contudo o Demoiselle fornece um módulo especifico que permite com algumas configurações ter as mesmas funcionalidades.


Add no pom.xml

```xml
<dependency>
    <groupId>org.demoiselle.jee</groupId>
    <artifactId>demoiselle-multitenancy-hibernate</artifactId>
</dependency>
```

> É importante fazer a ação de `maven update` no projeto após adicionar o novo módulo.

Add no demoiselle.properties

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

Add no persistence.xml

```xml
<persistence-unit name="UserTenantsPU" transaction-type="JTA">
    <provider>org.hibernate.ejb.HibernatePersistence</provider>
    <jta-data-source>java:jboss/datasources/UserTenantsDS</jta-data-source>
    <class>org.demoiselle.jee7.example.store.user.entity.User</class>
    <!-- <exclude-unlisted-classes>true</exclude-unlisted-classes> -->
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

Alteração do Standalone.xml

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


Criação do Entity Manager

```java
public class EntityManagerMasterDAO implements EntityManagerMaster {

	@PersistenceContext(unitName = "UserMasterPU")
	protected EntityManager emEntity;

	public EntityManager getEntityManager() {
		return emEntity;
	}

}
```

Criação da classe de serviço TenantREST

```java
@Path("tenant")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class TenantREST {

	@Inject
	private TenantManager tenantManager;

	private Logger logger;

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
			logger.log(Level.SEVERE, "Error trying to DELETE Tenant", e);
			return Response.serverError().build();
		}
	}

	@GET
	@Path("context")
	@Cors
	public Response multitenancyContext() throws Exception {
		return Response.ok().entity(tenantManager.getTenantName()).build();
	}

	@POST
	@Cors
	@ValidatePayload
	public Response createTenant(Tenant tenant) throws Exception {
		try {
			tenantManager.createTenant(tenant);
			return Response.ok().build();
		} catch (final Exception e) {
			logger.log(Level.SEVERE, "Error trying to CREATE Tenant", e);
			return Response.serverError().build();
		}
	}

}
```
