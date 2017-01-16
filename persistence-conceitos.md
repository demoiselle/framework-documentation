# Conceitos

## Entity Manager

A ideia principal por trás do Entity Manager é algo muito similar ao funcionamento do Hibernate Session que basicamente é responsável pela criação, leitura, atualização e exclusão de dados e suas operações relacionadas.

As aplicações podem usar 2 tipos de Entity Manager: application-managed ou container-managed. É importante saber que o Entity Manager **não é Thread Safe**, ou seja, não o utilize em variáveis de classes de Servlets que são visíveis por várias threads.

### Application-managed entity manager

O Entity Manager do tipo Application Managed prove um acesso direto as funcionalidades básicas do provedor de persistência \(No caso do Wildfly a implemenação é org.hibernate.ejb.HibernatePersistence\). O escopo deste tipo de Entity Manager é desde quando a aplicação cria a instância até o momento que ela é fechada pela aplicação, ou seja, a aplicação é responsável pela gestão do ciclo de vida do Entity Manager.

### Container-managed entity manager

O tipo Container-managed faz a gestão do ciclo de vida automaticamente \(auto-magically!\). É possível usar contextos de persistência em escopo de transações ou extender outros contextos.   
Toda vez que um novo acesso as funcionalidades é feito no provedor de persistência uma nova instância é criada junto com um contexto de persistência.

## Persistence Context

O Contexto de Persistência da JPA contém as Entities gerenciadas pelo provedor de persistência. O Contexto de Persistência atua como um cache de primeiro nível para as transações para interagir com o _datasource_. Entities carregadas são colocadas dentro do contexto antes de serem retornadas a aplicação. Mudanças nas Entities também devem ser colocadas dentro do contexto para que sejam salvas quando o _commit_ da transação foi feito.

### Escopo de Transação com Persistence Context

O Contexto de Persistência com escopo de transação trabalha junto com uma transação JTA \(Java Transaction API\) ativa. Quando uma transação é _commitada_ o contexto de persistência é atualizado \(_flushed_\) no _datasource_. Todas as Entities alteradas que pretendem ser salvas no _datasource_ devem ser realizadas durante a transação. Entities lidas fora do contexto de transação serão tratadas separadamente quando o processo foi concluído.

Um Contexto de Persistência com escopo de transação:

```java
@Stateful  // will use container managed transactions
public class CustomerManager {

  @PersistenceContext(unitName = "customerPU") // default type is PersistenceContextType.TRANSACTION
  EntityManager em;

  public customer createCustomer(String name, String address) {
    Customer cust = new Customer(name, address);
    em.persist(cust);  // persist new Customer when JTA transaction completes (when method ends).
                           // internally:
                           //    1. Look for existing "customerPU" persistence context in active JTA transaction and use if found.
                           //    2. Else create new "customerPU" persistence context (e.g. instance of org.hibernate.ejb.HibernatePersistence)
                           //       and put in current active JTA transaction.
    return cust;       // return Customer entity (will be detached from the persistence context when caller gets control)
  }  // Transaction.commit will be called, Customer entity will be persisted to the database and "customerPU" persistence context closed
}
```



