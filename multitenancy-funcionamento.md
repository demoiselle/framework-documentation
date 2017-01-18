# Multitenancy - Funcionamento

## Camada de Serviços

Na camada de serviços existe um filtro que basicamente pega a primeira posição após o **ApplicationPath** selecionado na aplicação, por exemplo na URL [http://dominio.com.br/contexto/api/cliente01/usuarios](http://dominio.com.br/contexto/api/cliente01/usuarios) ele pegará o texto **cliente01** e tentará encontrar um Tenant schema Master, caso encontre ele irá retirar o Tenant da URL ficando [http://dominio.com.br/contexto/api/usuarios](http://dominio.com.br/contexto/api/usuarios) para que o serviço REST possa atuar, e além disso ele irá adicionar no contexto do multitenancy o Tenant encontrado.

Utilizando esta estratégia todos os serviços que foram criados sem pensar no funcionamento do multitenancy poderão ser utilizados sem grandes modificações.

## Camada de Negócio

Na camada de negócio para acessar o Tenant selecionado na camada de serviço basta acessar o contexto injetando ele em sua classe, por exemplo:

```java
public class MinhaClasseBC {

    @Inject
    private MultiTenantContext multiTenantContext;

    public void metodoDeNegocio() {

        // Utilização dos dados do Tenant
            Tenant tenant = multiTenancyContext.getTenant();

    }

}
```

## Camada de Acesso a Dados



