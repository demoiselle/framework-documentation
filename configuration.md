# Configuration

## Configuração em uma aplicação

Muitas vezes, por motivos diversos, é necessário parametrizar a aplicação à partir de algum mecanismo de configuração. E em java é comum se utilizar as seguintes abordagens para armazenas as configurações:

* arquivo de propriedades: tratam-se de simples arquivos de texto nomeados com a extensão .properties, os quais são escritos com a sintaxe chave=valor, armazenando uma única chave por linha;
* arquivo XML: são arquivos de texto altamente estruturados com a sintaxe de tags e que    permitem uma maior validação dos seus valores, sendo geralmente nomeados com a extensão.xml;
* variáveis de ambiente: valores definidos no sistema operacional, independente de plataforma \(Windows, Linux, Mac OS, etc\) e que podem ser recuperados durante a execução da aplicação.

Esse capítulo mostra de que maneira o Demoiselle Framework pode facilitar a utilização dessas formas de configuração, oferencendo vários recursos interessantes para a sua aplicação.

## As classes de configuração

O primeiro passo para a utilização do mecanismo de configuração em uma aplicação consiste em criar uma classe específica para armazenar os parâmetros desejados e anotá-la com **@Configuration**. O código abaixo mostra um exemplo de classe de configuração:

```java
@Configuration
public class BookmarkConfig {

    private String applicationTitle;
    private boolean loadInitialData;

    public String getApplicationTitle() {
        return applicationTitle;
    }

    public boolean isLoadInitialData() {
        return loadInitialData;
    }

}
```

##### Notas

* As classes anotadas com **@Configuration** são instanciadas uma única vez \(seguindo o padrão de projeto singleton\) e podem ser injetadas em qualquer ponto da aplicação. Seu ciclo de vida é gerenciado automaticamente pelo CDI. Os recursos \(arquivo ou variável de ambiente\) são lidos no primeiro acesso à respectiva classe de configuração, quando os seus atributos são preenchidos automaticamente.

* Recomenda-se usar o sufixo **“Config”** nas classes de configuração, e que sejam criados apenas os acessores para leitura \(getters\).

---

Esse é um exemplo bastante simples, no qual não são especificados nem nome nem tipo do arquivo de configuração. Nessa situação os parâmetros de nome applicationTitle e loadInitialData serão procurados em um arquivo de propriedades de nome demoiselle.properties. Ou seja, quando não especificados nome e tipo do arquivo, assume-se que o arquivo é do tipo propriedades e seu nome é demoiselle. Mas como fazer para não utilizar o valor padrão e definir nome e tipo do arquivo? Bastante simples, basta adicionar esses parâmetros à anotação **@Configuration**, como mostra o exemplo a seguir:

```java
@Configuration(resource="my-property-file", type=ConfigType.XML)
public class BookmarkConfig {

    private String applicationTitle;
    private boolean loadInitialData;

    public String getApplicationTitle() {
        return applicationTitle;
    }

    public boolean isLoadInitialData() {
        return loadInitialData;
    }

}
```

Devemos atribuir o nome do arquivo de configuração ao parâmetro resource, sem a extensão. Ao parâmetro type pode ser atribuída uma das três possibilidades: ConfigType.PROPERTIES, que é o valor padrão e indica que as configurações daquela classe estão em um arquivo do tipo properties; ConfigType.XML, que indica que as configurações daquela classe estão em um arquivo do tipo xml; e ConfigType.SYSTEM, que indica que as configurações daquela classe são valores definidos pelo Sistema Operacional. Nesse exemplo, ao definir resource e type os parâmetros de nome applicationTitle e loadInitialData serão procurados em um arquivo xml    de nome my-property-file \(my-property-file.xml\).

Outro parâmetro que você pode ajustar nessa anotação é o prefixo. Ao definir um valor de prefixo você informa que o nome das propriedades definidas naquela classe devem ser concatenados com o prefixo, de forma que o nome dos atributos procurados no arquivo seja prefixo.nomeatributo. O exemplo abaixo mostra a utilização desse parâmetro. Nesse caso, os parâmetros de nome info.applicationTitle e info.loadInitialData serão procurados em um arquivo de propriedade de nome my-property-file \(my-property-file.properties\).

```java
@Configuration(prefix="info", resource="my-property-file")
public class BookmarkConfig {

    private String applicationTitle;
    private boolean loadInitialData;

    public String getApplicationTitle() {
        return applicationTitle;
    }

    public boolean isLoadInitialData() {
        return loadInitialData;
    }

}
```

##### Nota

O Demoiselle Framework adiciona automaticamente o ponto entre o prefixo e o nome do atributo, você não precisa se preocupar com isso.

##### Dica

No arquivo xml o prefixo corresponde a uma tag acima das tag que correspondem aos atributos. O exemplo acima ficaria da seguinte forma em um arquivo xml:

```xml
<info>
    <applicationTitle>Demoiselle Application</applicationTitle>
    <loadInitialData>true</loadInitialData>
</info>
```

## Especificando os parâmetros

Atualmente são suportados nativamente pelo Demoiselle Framework parâmetros de sete categorias diferentes. São eles: tipos primitivos \(int, float, boolean, etc\), classes wrapper \(Integer, Float, Boolean, etc.\) , String, Class, Map, Array e instâncias de Enum. A seguir vamos explicar e exemplificar como utilizar cada um desses tipos, e alertar para as possíveis exceções que poderão ser lançadas para sua aplicação.

### Tipos suportados

#### Primitivos

A utilização dos tipos primitivos é bastante simples. Veja no exemplo abaixo uma classe de configuração    um arquivo de configurações, que ilustram como é o procedimento para adicionar parâmetros do tipo primitivo.

```java
@Configuration
public class BookmarkConfig {

    private int pageSize;

    public String getPageSize() {
        return pageSize;
    }

}
```

Para essa classe, o arquivo de propriedade correspondente \(demoiselle.properties\) deveria apresentar o seguinte conteúdo:

```properties
pageSize = 10
```

Bastante simples, não? Mesmo assim, é bom ficarmos atentos, pois alguns cenários diferentes podem acontecer. Vamos supor por exemplo que, por um motivo    qualquer, a classe de configuração não esteja associada a um arquivo que contenha a chave de um de seus parâmetros. Nesse caso será atribuido o valor padrão da linguagem ao atributo, que para os tipos primitivos é 0, exceto para os tipos boolean, cujo valor padrão é false, e char, cujo valor padrão é o caracter nulo \('\u0000'\). Outro cenário possível é a existência da chave, mas sem valor atribuído \(pageSize=\). Nesse caso o valor encontrado no arquivo é equivalente a uma String vazia, e a exceção ConfigurationException, cuja causa foi uma ConversionException, será lançada.

#### Wrappers

Os atributos do tipo wrapper devem ser utilizados da mesma forma que os atributos do tipo primitivo. A única diferença entre eles é que o valor padrão atribuído a um parâmetro, no caso da classe de configuração não estar associada a um arquivo que contenha sua chave, é nulo.

#### Strings

Por sua vez, as configurações do tipo String tem funcionalidade bastante similar às configurações do tipo Wrapper. A diferença fica por conta de que, ao deixar a chave sem valor atribuído, para atributo desse tipo, não será lançada exceção, pois que não haverá o problema de conversão, e à configuração será atribuido o valor de uma String vazia.

#### Class

O atributo pode ou não ser tipado, e no arquivo o valor atribuído à chave deve corresponder ao nome \( Canonical Name\) de uma classe existente. Abaixo temos um exemplo de uma classe de configuração com dois atributos do tipo Class, um tipado e outro não tipado:

```java
@Configuration
public class BookmarkConfig {

    private Class<MyClass> typedClass;
    private Class<?> untypedClass;

    public Class<MyClass> getTypedClass() {
        return typedClass;
    }

    public Class<?> getUntypedClass() {
        return untypedClass;
    }

}
```

O arquivo de propriedades teria o seguinte conteúdo:

```properties
typedClass=package.MyClass
untypedClass=package.MyOtherClass
```

Caso uma chave de uma configuração do tipo Class não tenha valor atribuído ou seja atribuído um nome de classe que não existe \(ou não possa ser encontrada pela aplicação\), no momento do seu carregamento sera lançada uma exceção do tipo ConfigurationException, cuja causa é uma ClassNotFoundException. Caso a classe de configuração não esteja associada a um arquivo que contenha a chave de um de seus parâmetros do tipo Class, este será carregado com valor nulo.

#### Map

Para utilizar parâmetros do tipo Map, o arquivo de configurações deve usar a seguinte estrutura na formação da chave: prefixo+nomedoatributo+chavedomap. Vejamos um exemplo. Se temos em nossa aplicação uma classe de configuração como a mostrada abaixo:

```java
@Configuration
public class BookmarkConfig {

    private Map<String, String> connectionConfiguration;

    public Map<String, String> getConnectionConfiguration() {
        return connectionConfiguration;
    }

}
```

O arquivo de configuração deverá ser preenchido no seguinte formato \(se for do tipo properties\):

```properties
connectionConfiguration.ip=192.168.0.120
connectionConfiguration.gateway=192.168.0.1
connectionConfiguration.dns1=200.10.128.99
connectionConfiguration.dns2=200.10.128.88
```

Dessa forma, ao fazer a chamada connectionConfiguration.get\("gateway"\); por exemplo, o valor retornado será 192.168.0.1.

##### Dica

Você pode utilizar a chave do Map com nome "default" para indicar que, no arquivo de configuração, a chave é formada apenas pela junção do prefixo com o atributo, sem utilizar a própria chave do Map. Por exemplo, se o seu arquivo de propriedades contiver uma chave:

```properties
prefix.myMap=Default Value
```

então seu código poderá ter um comando:

```java
String value = myMap.get("default");
```

Caso a classe de configuração não esteja associada a um arquivo que contenha a chave de um de seus parâmetros do tipo Map, este será carregado com valor nulo, e estará sujeito às exceções informadas anteriormente, conforme o tipo de variáveis que ele contenha.

#### Array

No caso do Array, a principal diferença em relação às demais formas de declarar configurações é a maneira de atribuir valores aos seus respectivos elementos no arquivo de configuração. Por exemplo, para que um Array de inteiros, de nome integerArray tenha o conteúdo {-1, 0, 1}, você deve criar um arquivo de propriedades que contenha as seguintes linhas:

```properties
integerArray=-1
integerArray=0
integerArray=1
```

Exceto a forma de atribuir os valores às configurações, se comporta de acordo com o tipo de variável que ele contém, conforme o espeficifcado para cada um.

#### Enum

É possível criar uma lista de constantes do tipo Enum e carregar um valor de constante através de um arquivo de configuração. Por exemplo, caso exista o seguinte Enum

```java
public enum ConfigurationType {
    PROPERTIES , XML , SYSTEM;
}
```

e ele seja usado no seguinte arquivo de configuração

```java
@Configuration
public class ConfigurationLoader {

    private ConfigurationType loadedConfigurationType;

    public ConfigurationType getLoadedConfigurationType(){
        return loadedConfigurationType;
    }

}
```

O arquivo do tipo properties pode ser criado assim:

```properties
loadedConfigurationType=SYSTEM
```

##### Dica

O valor definido no arquivo de configuração para atributos do tipo Enum deve ser idêntico ao nome da constante definida no Enum, inclusive casando letras maiúsculas e minúsculas. De fato, o valor da propriedade deve casar com o valor retornado no código: Enum.name\(\).

Caso o valor definido no arquivo de configuração não case com nenhuma constante definida no Enum, uma exceção de tipo ConfigurationException de causa ConversionException será lançada. Já se à propriedade for atribuido um valor vazio, o atributo do tipo Enum receberá o valor null.

### Alterando o nome da da chave com @ConfigurationName

É possível alterar o nome padrão da chave a ser utilizada com a anotação **@ConfigurationName** como o exemplo abaixo:

```java
import org.demoiselle.jee.configuration.annotation.ConfigurationName;

@Configuration(resource = "app", type = ConfigurationType.PROPERTIES, prefix = "")
public class MyConfig {

    @ConfigurationName("my-config-string-with-name")
    private String configStringWithName;

    public String getConfigStringWithName() {
        return configStringWithName;
    }

}
```

No exemplo acima a fonte de dados encontra-se no arquivo chamado _app.properties_ que tem seu conteúdo definido por:

```properties
#configStringWithName=Minha string
my-config-string-with-name=Minha string com novo identificador
```

### Ignorando um campo com @ConfigurationIgnore

Caso você tenha a necessidade de manter um campo em uma classe de configuração mais não deseja que ela receba nenhum valor de nenhum tipo de fonte você pode utilizar a notação **@ConfigurationIgnore** para o campo desejado.

```java
import org.demoiselle.jee.configuration.annotation.ConfigurationIgnore;


@Configuration(resource = "app", type = ConfigurationType.PROPERTIES, prefix = "")
public class ConfigModel {

    private ConfigEnum configEnum;

    @ConfigurationIgnore
    private String configFieldWithIgnore; // Não será preenchido pelo mecanismo de configuração e nem impresso na saída padrão da aplicação

    public ConfigEnum getConfigEnum() {
        return configEnum;
    }

    public String getConfigFieldWithIgnore() {
        return configFieldWithIgnore;
    }
}
```

## Logando as configurações

Para uma melhor apuração das configurações feitas na aplicação, optamos por registrar na saída padrão da aplicação todas as configurações criadas nas classes anotadas com **@Configuration**, isso facilita descobrir possíveis erros de digitação e saber exatamente as configurações que estão disponíveis para a aplicação.

Exemplo de saída:

```
*******************************************************
Carregando a classe de configuração org.demoiselle.jee.security.DemoiselleSecurityConfig
-> demoiselle.security.paramsHeaderCors: [não definada na fonte de configuração do projeto, maiores informações: https://demoiselle.gitbooks.io/documentacao-jee/content/configuration-general.html]
-> demoiselle.security.corsEnabled = true
-> demoiselle.security.paramsHeaderSecuriry = {default=x-content-type-options=nosniff,x-frame-options=SAMEORIGIN,x-xss-protection=1; mode=block}
-> demoiselle.security.paramsHeaderCors = 'null'
```

Quando não houver definição de valor em um campo \(seja por arquivo,  variável de ambiente ou na própria classe\) será impresso uma mensagem auxiliando o desenvolvedor como proceder caso precise.

A configuração será impressa uma única vez na primera chamada da classe anotada com **@Configuration**.

Caso seu campo utilize a anotação **@ConfigurationName** alterando o valor da chave a ser utilizada a impressão irá respeitar o que foi definido pela anotação **@ConfigurationName**

### Suprimindo a impressão de valores

Pensando que nem todos os valores de configurações podem ser expostos por diversos fatores como segurança, criamos uma anotação chamada **@ConfigurationSuppressLogger** que pode ser utilizada no nível de Classe ou de Campo.

Exemplo:

```java
import java.io.Serializable;
import org.demoiselle.jee.configuration.annotation.Configuration;
import org.demoiselle.jee.configuration.annotation.ConfigurationSuppressLogger;

@Configuration(prefix = "demoiselle.security.jwt")
public class DemoiselleSecurityJWTConfig implements Serializable {

    private static final long serialVersionUID = 638_435_989_235_076_782L;

    private String type;

    @ConfigurationSuppressLogger
    private String privateKey;

    private String publicKey;
    ...
 }
```

Observe na imressão abaixo que o valor do chave **demoiselle.security.jwt.privateKey** foi suprimida.

```
*******************************************************
Carregando a classe de configuração org.demoiselle.jee.security.jwt.impl.DemoiselleSecurityJWTConfig
-> demoiselle.security.jwt.type = master
-> demoiselle.security.jwt.privateKey = [valor suprimido pela anotação @ConfigurationSuppressLogger]
-> demoiselle.security.jwt.publicKey = -----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA09A11Zaqmp5ZBTOCxgJ8qqtHhb6b+eO9C8gAX3DXFIlfcl0FU7FVwfQtHWuN3KU83c1sSj9wPMuviGvZeSV7oUp2DXML76EsEKf9r+6JNZRdnTCkXZklQSEmeuftSxnMtAwo7k+eIgPpOIrYpMJv5GzVju0zbyucnwbQnDvoGv08pMrbWaGOxcnHXCazsrRTI7UKQ+jvMDB3bsUcII0XS+92ZLQkiMkKH85HSSlm4AFKuUljRh59RlpJrCDc+TUZWQuOC6Li/H9/78tAW8kJIHfASJhkKgkcdGixBJNggp+K+0hMWvxLt5fi1BXvWiy/Ma3QNHtxOCorRa+4NBB+KwIDAQAB-----END PUBLIC KEY-----
-> demoiselle.security.jwt.timetoLiveMilliseconds = 86400000
-> demoiselle.security.jwt.issuer = STORE
-> demoiselle.security.jwt.audience = web
-> demoiselle.security.jwt.algorithmIdentifiers = RS256
```

## Criando seu próprio Extrator

Você precisa de parâmetros de um tipo que ainda não é suportado pelo Demoiselle? Você pode implementar sua própria classe extratora ou substituir alguma já criada pelo Demoiselle. A aplicação pode implementar a interface **ConfigurationValueExtractor**, e ter um extrator de configurações para atributos de qualquer tipo. Essa interface obriga a classe a implementar os métodos:

```java
boolean isSupported(Field field); // retorna true caso a classe seja extratora daquele tipo de campo

Object getValue(String prefix, String key, Field field, Configuration configuration) throws DemoiselleConfigurationValueExtractorException; // método que deve extrair o valor do campo da forma correta.
```

Caso você implemente um extrator que já seja suportado pelo Demoiselle o framework irá dar prioridade ao extrator da sua aplicação.

