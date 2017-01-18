# Tratamento de Exceções

## Estratégia Utilizada

A ideia principal do Framework é que todas as Exceções que sobem para serem mostradas para o usuário final sejam tratadas por um único ponto e que ele possa ser sobrescrito pelo desenvolvedor de acordo com as suas necessidades.

O primeiro passo foi definir a estrutura a ser utilizada no retorno padrão dos erros do framework, e para isso pegamos como inspiração a **RFC 6749** que define o **OAuth 2.0**, pois dentro dela existe um padrão definido no item **5.2 \(Error Response\) **que basicamente descreve o seguinte formato:

```json
{
  "error": "descrição_curta",
  "error_description": "descrição longa, legível por humanos",
  "error_uri": "URI que leva a uma descrição detalhada do erro no portal do desenvolvedor"
}
```





> RFC 6749 \(The OAuth 2.0 Authorization Framework\) - https://tools.ietf.org/html/rfc6749\#page-45



## Código HTTP de Resposta

## Exception Mappers

O ideal é que no log de inicialização do servidor de aplicação somente existam as seguintes classes de mapeamento:

1. org.demoiselle.jee.rest.exception.mapper.**ValidationExceptionMapper**: sobrescreve o mapeamento de Exception do Beans Validation \(CONFERIR\)
2. org.demoiselle.jee.rest.exception.mapper.**AnyOtherExceptionMapper**: captura todas as outras Exceptions do sistema

Explicar o funcionamento dos Mappers.

## Padrões de Retorno

Explicar como funciona o JSON de retorno de erro e os código HTTP de erros.

## Configurações

isShowErrorDetails

## Sobrescrita do Formato Padrão

main/webapp/WEB-INF/beans.xml

Alternative

## Referências

* [https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-2rd-edition/content/en/part1/chapter7/exception\\_handling.html](https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-2rd-edition/content/en/part1/chapter7/exception\_handling.html)



