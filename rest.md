# Rest

O módulo Rest é a base dos demais módulos do framework, nele temos quatro anotações e uma configuração descritas a seguir:

Para usar o módulo rest faça a importação:
```bash
<dependency>
   <groupId>org.demoiselle.jee</groupId>
   <artifactId>demoiselle-rest</artifactId>
   <version>3.0.0</version>
</dependency>
```   

Caso você crie sua app a partir de um parent

```bash
    <parent>
        <groupId>org.demoiselle.jee</groupId>
        <artifactId>demoiselle-parent</artifactId>
        <version>3.0.0</version>
    </parent>
```
Ele já agrega o módulo Rest, não há necessidade de colocar a dependência.

Cache-control

```bash
   @CacheControl("max-age=0") 
   Insere um tempo de vida no response do método(GET) anotado, onde o
   browser lê e guarda o conteúdo pelo tempo que for definido no max-age
   em milisegundos, ideal para acesso a dados que tem pouca mudança como
   relação de UF e Municípios.
 ```
ValidatePayload

```bash
   @ValidatePayload
   Faz uma validação do objeto enviado no request no momento do parser
   JSON/Object retornando uma lista de erros(se houver), identificando
   onde e qual a falha com uma mensagem   
 ```
 CPF e CNPJ
 
 ```bash
   @CPF e @CNPJ
   Fazem a validação dos números com seus respectivos digitos
   verificadores, a mensagem de erro pode ser inserida na anotação
   @CPF(message = "CPF inválido") ou por configuração colocando
   {cpf.error} no arquivo de mensagens do projeto
   
 ```
 
 Configuração deve ser colocada no arquivo demoiselle.properties:
 
 ```bash
   # demoiselle.rest.gzipEnabled=false
   Essa configuração se tiver true permite compactar, no formato gzip, o
   conteúdo do corpo do request, sendo necessário o cliente desfazer a
   compactação para ler e compactar os dados para enviar ao servidor.
   Estratégia usada quando a limitação de banda for grande e o
   processamento de compactação não seja impeditivo.
 ```
 
 