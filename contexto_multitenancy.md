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