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

A camada de acesso a dados da funcionalidade de multitenancy é baseada totalmente na implementação do Hibernate e tem como principais pontos as 2 classes que são informadas no persistence.xml:

### **hibernate.tenant_identifier_resolver:** org.demoiselle.jee.multitenancy.hibernate.dao.SchemaResolver

Esta classe tem como principal objetivo retornar ao hibernate qual é o tenant atualmente selecionado, abaixo o trecho de código que faz exatamente esta função:

```java
@Override
public String resolveCurrentTenantIdentifier() {
	MultiTenantContext o = CDI.current().select(MultiTenantContext.class).get();
	return o.getTenant().getName();
}
```

### **hibernate.multi_tenant_connection_provider:** org.demoiselle.jee.multitenancy.hibernate.dao.MultiTenantProvider

A classe MultiTenantProvider tem como função principal fazer a seleção do schema que será usado por meio de uma instrução SQL que será executada no banco de dados antes de qualquer outra instrução que seja feita.
