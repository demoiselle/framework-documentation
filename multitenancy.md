# Multitenancy (Multi-inquilino)
O termo multitenancy ou multi-inquilino em português, em geral, é aplicado ao desenvolvimento de software para indicar uma arquitetura em que uma única instância de um aplicativo serve simultaneamente vários clientes (inquilinos). 
Isto é muito comum em soluções SaaS. Isolando informações (dados, personalizações, etc.) referentes aos vários inquilinos é um desafio especial nestes sistemas. Isto inclui os dados de propriedade de cada inquilino armazenados no banco de dados.
O Framework Demoiselle fornece alguns facilitadores e a documentação necessária para que uma aplicação com as caracteristicas de Multitenancy seja desenvolvida.

## Estratégias na Camada de Serviço
