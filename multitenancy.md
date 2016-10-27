# Multitenancy (Multi-inquilino)
O termo multitenancy ou multi-inquilino em português, em geral, é aplicado ao desenvolvimento de software para indicar uma arquitetura em que uma única instância de um aplicativo serve simultaneamente vários clientes (inquilinos). 

Isto é muito comum em soluções SaaS. Isolando informações (dados, personalizações, etc.) referentes aos vários inquilinos é um desafio especial nestes sistemas. Isto inclui os dados de propriedade de cada inquilino armazenados no banco de dados.

O Framework Demoiselle fornece alguns facilitadores e a documentação necessária para que uma aplicação com as caracteristicas de Multitenancy seja desenvolvida.

## Estratégias na Camada de Serviço (REST)
Existem pelo menos 3 estratégias para a camada de serviço, que no caso do Framework Demoiselle versão 3 trata basicamente serviços REST.
As 3 principais abordagens são:
1. Utilização de **Subdomínio (DNS)** - nesta utilização cada inquilino se utiliza de um subdomínio próprio fornecido pelo servidor de DNS como por exemplo *cliente01.meudominio.com* e *cliente02.meudominio.com*. 
2. Utilização de **Metadados** - a utilização de meta dados basicamente utiliza algum dado do usuário para o direcionar para o inquilino correto, normalmente o direcionamento somente pode ser feito após a identificação do usuário que comumente é feito por meio de acesso ao sistema utilizando um e-mail e senha
3. Utilização de **URL** - nesta abordagem quem determinará o inquilino que será usado é a URL acessada, por exemplo *meudominio.com/cliente01/api/servico* e *meudominio.com/cliente02/api/servico*

Abaixo estão as principais **desvantagens** de cada uma das 3 abordagens:
1. A principal desvantagem no caso de subdomínios é a necessidade de uma maior integração entre a aplicação e os serviços de rede da solução, pois a criação de novos nomes no serviço de DNS necessita de alguma integração
2. A desvantagem do Metadado é que exige alguma forma de identificaço prévia do usuário, dificultando a criação de serviços públicos
3. 


## Estratégias na Camada de Persistência
Assim como na camada de serviços a camada de persistência possui pelo menos 3 estratégias de implementação de multitenancy, seguem elas:
1. **Instâncias de banco de dados separadas** - Each tenant’s data is kept in a physically separate database instance.
2. **Schemas diferentes dentro de uma mesma instância** - Each tenant’s data is kept in a distinct database schema on a single database instance. 
3. **Mesma instância e schema para todos utilizando um discriminador** - All data is kept in a single database schema. The data for each tenant is partitioned by the use of partition value or discriminator. 

https://msdn.microsoft.com/en-us/library/aa479086.aspx
https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#multitenacy

## Multitenancy no Framework Demoiselle
É importante que antes iniciar o desenvolvimento da solução que usará o multitenancy sejam definidas as estratégias em cada camada com base nos requisitos, pois como já foi comentado cada solução é única e deve ser analisada individualmente.

Nos próximos itens deste documento serão explicitadas algumas das soluções propostas pelo Framework Demoiselle e que podem ou não ser aplicadas ao projeto.