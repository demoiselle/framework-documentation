# Multitenancy - Armazenamento de Dados
Como padrão no Framework Demoiselle foi utilizada a implementação de **Schemas diferentes dentro de uma mesma instância**, mas isso não significa que todas as aplicações devam utilizar esta estratégias, pois a estratégia usada deve ser avaliada caso a caso.

## Estratégia Utilizada
A estratégia disponível no Demoiselle Framework é a de....

## Classe MultiTenantProvider

```java
public class MultiTenantProvider implements MultiTenantConnectionProvider, ServiceRegistryAwareService {

	private DataSource dataSource;

	@Override
	public boolean supportsAggressiveRelease() {
		return false;
	}

	@Override
	public void injectServices(ServiceRegistryImplementor serviceRegistry) {
		try {
			final Context init = new InitialContext();
			dataSource = (DataSource) init.lookup("java:jboss/datasources/TenantsDS");
		} catch (final NamingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isUnwrappableAs(Class clazz) {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> clazz) {
		return null;
	}

	@Override
	public Connection getAnyConnection() throws SQLException {
		final Connection connection = dataSource.getConnection();
		return connection;
	}

	@Override
	public Connection getConnection(String tenantIdentifier) throws SQLException {
		final Connection connection = getAnyConnection();
		try {
			connection.createStatement().execute("SET SCHEMA " + tenantIdentifier);
		} catch (final SQLException e) {
			throw new HibernateException("Error trying to alter schema [" + tenantIdentifier + "]", e);
		}
		return connection;
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {	
		connection.close();
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
		releaseAnyConnection(connection);
	}
}
```

## Classe SchemaResolver

```java
public class SchemaResolver implements CurrentTenantIdentifierResolver {

	@Override
	public String resolveCurrentTenantIdentifier() {
		MultiTenantContext o = CDI.current().select(MultiTenantContext.class).get();
		return o.getTenant().getName();
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return false;
	}

}
```

## Arquivo persistence.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.1"
	xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">

	<persistence-unit name="TenantsPU" transaction-type="JTA">
		<provider>org.hibernate.ejb.HibernatePersistence</provider>
		<jta-data-source>java:jboss/datasources/TenantsDS</jta-data-source>
		<class>org.demoiselle.store.usuario.entity.Usuario</class>
		<!-- <exclude-unlisted-classes>true</exclude-unlisted-classes> -->
		<properties>
			<property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect" />
			<property name="hibernate.multiTenancy" value="SCHEMA" />
			<property name="hibernate.tenant_identifier_resolver"
				value="org.demoiselle.store.usuario.dao.multitenancy.SchemaResolver" />
			<property name="hibernate.multi_tenant_connection_provider"
				value="org.demoiselle.store.usuario.dao.multitenancy.MultiTenantProvider" />
		</properties>
	</persistence-unit>

	<persistence-unit name="MasterPU" transaction-type="JTA">
		<provider>org.hibernate.ejb.HibernatePersistence</provider>
		<jta-data-source>java:jboss/datasources/MasterDS</jta-data-source>
		<class>org.demoiselle.store.usuario.entity.Tenant</class>
		<exclude-unlisted-classes>true</exclude-unlisted-classes>
		<properties>
			<property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect" />
			<property name="hibernate.hbm2ddl.auto" value="update" />
			<property name="hibernate.show_sql" value="false" />		
		</properties>
	</persistence-unit>

	<persistence-unit name="ExportPU" transaction-type="JTA">
		<provider>org.hibernate.ejb.HibernatePersistence</provider>
		<jta-data-source>java:jboss/datasources/TenantsDS</jta-data-source>
		<class>org.demoiselle.store.usuario.entity.Usuario</class>
		<exclude-unlisted-classes>true</exclude-unlisted-classes>
		<properties>
			<property name="hibernate.dialect" value="org.hibernate.dialect.MySQLDialect" />
			<property name="javax.persistence.schema-generation.scripts.action"
				value="drop-and-create" />
			<property name="javax.persistence.schema-generation.scripts.create-target"
				value="../standalone/tmp/demoiselleMultiTenantCreate.ddl" />
			<property name="javax.persistence.schema-generation.scripts.drop-target"
				value="../standalone/tmp/demoiselleMultiTenantDrop.ddl" />
		</properties>
	</persistence-unit>
	
</persistence>
```
