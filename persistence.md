# Persistence

Persistência é um dos aspectos mais importantes de sistemas corporativos - grande parte desses sistemas devem em algum ponto persistir informações em um sistema gerenciador de banco de dados. A tecnologia Java conta hoje com algumas formas de facilitar o acesso a SGBD's - algumas são especificações Java como o JDBC e o JPA, outras são tecnologias desenvolvidas por terceiros como o Hibernate.

> **Java Persistence API** \(ou simplesmente JPA\) é uma API padrão da linguagem Java que descreve uma interface comum para frameworks de persistência de dados. A JPA define um meio de mapeamento objeto-relacional para objetos Java simples e comuns \(POJOs\), denominados beans de entidade. Diversos frameworks de mapeamento objeto/relacional como o **Hibernate** implementam a JPA. Também gerencia o desenvolvimento de entidades do Modelo Relacional usando a plataforma nativa Java SE e Java EE.

A maioria dos servidores JEE possuem implementações para a especificação JPA, que contemplam basicamente as seguintes características:

* persistence unit definitions
* persistence unit/context annotations
* persistence unit/context references in the deployment descriptor

Na utilização do Servidor Aplicação **JBoss Wildfly** a implementação padrão utilizada é o **Hibernate \(Versão 5\)**, sendo que durante a inicialização do servidor se for detectada a utilização do JPA por meio do arquivo _persistence.xml_ ou anotações _@PersistenceContext/Unit_ por exemplo, o Wildfly injeta as dependencias do Hibernate na instalação da aplicação, isso facilita o processo de instalaçõa de aplicação que utilizam JPA.

> Fontes:
>
> * [https://docs.oracle.com/javaee/7/tutorial/persistence-intro.htm](https://docs.oracle.com/javaee/7/tutorial/persistence-intro.htm)
> * [https://docs.jboss.org/author/display/WFLY10/JPA+Reference+Guide](https://docs.jboss.org/author/display/WFLY10/JPA+Reference+Guide)
> * [https://docs.jboss.org/hibernate/jpa/2.1/api/](https://docs.jboss.org/hibernate/jpa/2.1/api/)
> * [https://jcp.org/en/jsr/detail?id=338](https://jcp.org/en/jsr/detail?id=338)
> * [http://docs.jboss.org/hibernate/orm/5.2/userguide/html\_single/Hibernate\_User\_Guide.html](http://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html)



