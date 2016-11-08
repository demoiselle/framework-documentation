# Entity Manager
A ideia principal por trás do Entity Manager é algo muito similar ao funcionamento do Hibernate Session que basicamente é responsável pela criação, leitura, atualização e exclusão de dados e suas operações relacionadas. 

As aplicações podem usar 2 tipos de Entity Manager: application-managed ou container-managed. É importante saber que o Entity Manager **não é Thread Safe**, ou seja, não o utilize em variáveis de classes de Servlets que são visíveis por várias threads.
## Application-managed entity manager
O Entity Manager do tipo Application Managed prove um acesso direto as funcionalidades básicas do provedor de persistência (No caso do Wildfly a implemenação é org.hibernate.ejb.HibernatePersistence). O escopo deste tipo de Entity Manager é desde quando a aplicação cria a instância até o momento que ela é fechada pela aplicação, ou seja, a aplicação é responsável pela gestão do ciclo de vida do Entity Manager.
## Container-managed entity manager
O tipo Container-managed faz a gestão do ciclo de vida automaticamente (auto-magically!). É possível usar contextos de persistência em escopo de transações ou extender outros contextos. 
Toda vez que um novo acesso as funcionalidades é feito no provedor de persistência uma nova instância é criada junto com um contexto de persistência.