# Tratamento de Erros

## Estratégia Utilizada

Texto.

O ideal é que no log de inicialização do servidor de aplicação somente existam as seguintes classes de mapeamento:

1. org.demoiselle.jee.rest.exception.mapper.**ValidationExceptionMapper**: sobrescreve o mapeamento de Exception do Beans Validation \(CONFERIR\)
2. org.demoiselle.jee.rest.exception.mapper.**AnyOtherExceptionMapper**: captura todas as outras Exceptions do sistema

## Exception Mappers

Texto.

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



