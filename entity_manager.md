# Entity Manager
A ideia principal por trás do Entity Manager é algo muito similar ao funcionamento do Hibernate Session que basicamente é responsável pela criação, leitura, atualização e exclusão de dados e suas operações relacionadas. 

As aplicações podem usar 2 tipos de Entity Manager: application-managed ou container-managed. É importante saber que o Entity Manager **não é Thread Safe**, ou seja, não o utilize em variáveis de classes de Servlets que são visíveis por várias threads.
