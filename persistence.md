# Persistence
Persistência é um dos aspectos mais importantes de sistemas corporativos - grande parte desses sistemas devem em algum ponto persistir informações em um sistema gerenciador de banco de dados. A tecnologia Java conta hoje com algumas formas de facilitar o acesso a SGBD's - algumas são especificações Java como o JDBC e o JPA, outras são tecnologias desenvolvidas por terceiros como o Hibernate.

No Framework Demoiselle iremos utilizar como padrão a especificação JPA da JEE e como implementação padrão utilizaremos o **Framework de Persistência Hibernate**.

> **Java Persistence API** (ou simplesmente JPA) é uma API padrão da linguagem Java que descreve uma interface comum para frameworks de persistência de dados. A JPA define um meio de mapeamento objeto-relacional para objetos Java simples e comuns (POJOs), denominados beans de entidade. Diversos frameworks de mapeamento objeto/relacional como o **Hibernate** implementam a JPA. Também gerencia o desenvolvimento de entidades do Modelo Relacional usando a plataforma nativa Java SE e Java EE.

A maioria dos servidores JEE possuem implementações para a especificação JPA, que contemplam basicamente as seguintes características:
* persistence unit definitions
* persistence unit/context annotations
* persistence unit/context references in the deployment descriptor

> Fonte: https://docs.jboss.org/author/display/WFLY10/JPA+Reference+Guide