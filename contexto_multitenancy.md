# Contexto Multitenancy

```java
@Inject
private MultiTenantContext multiTenancyContext;
```

```
Tenant tenant = multiTenancyContext.getTenant();
```

```java
@Entity
public class Tenant implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	@NotNull
	private String name;

	
	@Column
	private String databaseAppVersion;

    ...
    
}
```

```java
public class MeuBC {

	@Inject
	private MultiTenantContext multiTenancyContext;

    public void metodo(Entity entity) {
        // Utilização dos dados do Tenant
        Tenant tenant = multiTenancyContext.getTenant();
		
        // Continuação...
		
	}

}
```