# Messages

Para as mensagens dos sistemas desenvolvidos com o Framework Demoiselle foi utilizada a biblioteca DeltaSpike que é considerada uma extensão do CDI dentro do próprio ecosistema como pode ser visto na página da especificação do CDI \([http://cdi-spec.org](http://cdi-spec.org)\).

![DeltaSpike](assets/deltaspike.png)

Ele é descrito pela própria especificação do CDI como o seguinte:

> O DeltaSpike consiste em um módulo core e módulos opcionais que fornecem funcionalidades adicionais a soluções corporativas

Para saber mais informações sobre o DeltaSpike acesse [https://deltaspike.apache.org](https://deltaspike.apache.org) e com relação a mensagens de sistema o link direto é o seguinte:

[https://deltaspike.apache.org/documentation/core.html\#Messagesandi18n](https://deltaspike.apache.org/documentation/core.html#Messagesandi18n)

## Utilização Básica

Para utilizar as mensagens no sistema basta criar uma classe como o exemplo abaixo:

```java
package meuprojeto.pacote.mensagens;

import org.apache.deltaspike.core.api.message.MessageBundle;

@MessageBundle
public interface AppMessage {
    @MessageTemplate("{mensagem-de-teste}")
    String mensagemDeTeste();
}
```

Após a criação da classe é necessária a criação de um arquivo de propriedades que contenham as mensagens propriamente ditas como mostra o exemplo abaixo:

```properties
# Arquivo de mensagens do pacote meuprojeto.pacote.mensagens
mensagem-de-teste=Mensagem a ser exibida ao usuário
```

_É importante que o arquivo de properties seja cocado na mesma estrutura de pastas da classe, por exemplo, se a classe esta em _`/src/main/java/meuprojeto/pacote/mensagens`_ o arquivo de propriedades com as mensagens deverá estar em _`/src/main/resources/meuprojeto/pacote/mensagens`_._

## Utilização de Parâmetros

É possível parametrizar a utilização das mensagens por meio de parâmetros de métodos e marcadores nas mensagens do arquivo de propriedades.  
Abaixo um exemplo de método \(Que deve estar dentro de uma classe anotada com @MessageBundle\):

```java
@MessageTemplate("{mensagem-de-teste-com-parametro}")
String mensagemDeTesteComParametro(String parametro1, String parametro2);
```

O arquivo de propriedades ficará da seguinte forma:

```properties
mensagem-de-teste-com-parametro=Mensagem ao usuário com parametro 1: %s e parametro 2 %s
```

## Sobre escrita de Mensagens dos Módulos

Para sobreescrever as mensagens dos módulos do Framework crie a mesma estrutura de pastas do arquivo properties que deseja sobrescrever, sendo que o caminho inicial padrão para estes arquivos é  `/src/main/resources/`). Por exemplo, para sobrescrever o arquivo de mensagens do core basta criar a estrutura de pastas `/src/main/resources/org/demoiselle/jee/core/message/` e o arquivo `DemoiselleMessage.properties` dentro. A interface que representa este arquivo pode ser encontrada [aqui]( https://github.com/demoiselle/framework/blob/master/demoiselle-core/src/main/java/org/demoiselle/jee/core/message/DemoiselleMessage.java) e o arquivo com os valores iniciais [aqui](https://github.com/demoiselle/framework/blob/master/demoiselle-core/src/main/resources/org/demoiselle/jee/core/message/DemoiselleMessage.properties).

Cada arquivo de properties representa uma classe Java que contém os métodos equivalentes, portanto para saber outros arquivos de mensagens do Framework e sua estrutura de pastas basta fazer uma busca dentro do projeto por qualquer arquivo que tenha no nome o texto Message, para facilitar utilize o próprio GitHub a funcionalidade Find File \([https://github.com/demoiselle/framework/find/master](https://github.com/demoiselle/framework/find/master)\) e busque por Message.

> **É importante saber que não é possível sobrescrever apenas uma mensagem, portanto ao sobrescrever um arquivo é necessário sobrescrever todas as mensagens**


