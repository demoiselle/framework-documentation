# Multitenancy - Persistence
Como padrão no Framework Demoiselle foi utilizada a implementação de **URL**, mas isso não significa que todas as aplicações devam utilizar esta estratégias, pois a estratégia usada deve ser avaliada caso a caso.

Classe MultiTenantProvider

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

	@SuppressWarnings("rawtypes")
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