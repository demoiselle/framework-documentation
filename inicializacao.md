# Inicialização

Uma aplicação qualquer, seja do tipo Web ou desktop, geralmente necessita efetuar determinadas tarefas durante a sua inicialização e ou finalização. Eis alguns exemplos: abertura de conexão a um servidor de banco de dados, carregamento de parâmetros de configuração a partir de arquivos externos e execução de scripts específicos. Ou seja, normalmente essas ações são definidas como funções estruturais da aplicação.

Para que a execução dessas ações ocorra de forma concisa, faz-se necessário o uso de um mecanismo padronizado para inicialização do ambiente. 

O Demoiselle Framework fornece esse mecanismo através da especificação CDI introduzindo duas anotações: **@Startup** e **@Shutdown**.


## Implementação na aplicação

A fim de utilizar o mecanismo inerente do Demoiselle Framework, é preciso simplesmente anotar os métodos contendo as instruções desejadas com *@Startup*, para a inicialização, ou *@Shutdown*, para a finalização.

As instruções contidas em um método anotado com *@Startup* serão executadas automaticamente quando a aplicação Java for inicializada. Nenhum outro arquivo ou classe precisa ser definido. A anotação *@Startup* pode ser utilizada em conjunto com a anotação *@Priority*, que recebe como parâmetro um número inteiro que serve para definir a prioridade de execução do respectivo método, na existência de mais de um inicializador para a aplicação.

De maneira análoga, um método anotado com *@Shutdown* será executado no momento de finalização de uma aplicação, obedecendo também à ordem de prioridade definida com a anotação *@Priority*.

Eis um exemplo de implementação de inicializador em uma aplicação:

```java
public class BookmarkInitializer {

  @Startup
  @Priority(1)
  public void initialize() {  
    ...
  }

  @Shutdown
  @Priority(5)
  public void finalize() {
    ...
  }

}
```


## Um exemplo prático

Eis um interessante caso de uso de inicialização e finalização: rodar um servidor de modo standalone em paralelo à execução da aplicação principal. 

Eis o código referente a essa implementação:

```java
import org.demoiselle.jee.core.lifecycle.annotation.Shutdown;
import org.demoiselle.jee.core.lifecycle.annotation.Startup;

import static org.demoiselle.jee.core.annotation.Priority.MAX_PRIORITY;
import static org.demoiselle.jee.core.annotation.Priority.MIN_PRIORITY;

public class DatabaseServer {

    private final org.hsqldb.Server server;

    public DatabaseServer() {
        server = new Server();
        server.setDatabaseName(0, "db");
        server.setDatabasePath(0, "database/db");
        server.setPort(9001);
        server.setSilent(true);
    }

    @Startup
    @Priority(MAX_PRIORITY)
    public void startup() {
        server.start();
    }

    @Shutdown
    @Priority (MIN_PRIORITY)
    public void shutdown() {
        server.stop();
    }

}
```






