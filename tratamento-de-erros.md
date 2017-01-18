# Tratamento de Exceções

## Estratégia Utilizada

A ideia principal do Framework é que todas as Exceções que sobem para serem mostradas para o usuário final sejam tratadas por um único ponto e que ele possa ser sobrescrito pelo desenvolvedor de acordo com as suas necessidades.

O primeiro passo foi definir a estrutura a ser utilizada no retorno padrão dos erros do Framework Demoiselle, e para isso pegamos como inspiração a **RFC 6749** que define o **OAuth 2.0**, pois dentro dela existe um padrão definido no item **5.2 \(Error Response\)** que descreve o seguinte formato:

```json
[
  {
    "error": "descrição_curta",
    "error_description": "descrição longa, legível por humanos",
    "error_uri": "URI que leva a uma descrição detalhada do erro"
  },
  {
    "error": "descrição_curta",
    "error_description": "descrição longa, legível por humanos",
    "error_uri": "URI que leva a uma descrição detalhada do erro"
  }
]
```

Por padrão usamos uma coleção contendo a estrutura do erro com o objetivo de retornar vários erros de uma só vez (isso pode ser útil no caso de uma validação de formulário server-side, por exemplo).


> RFC 6749 \(The OAuth 2.0 Authorization Framework\) - [https://tools.ietf.org/html/rfc6749\\#page-45](https://tools.ietf.org/html/rfc6749\#page-45)

## Código HTTP de Resposta



## Exception Mappers

O ideal é que no log de inicialização do servidor de aplicação somente existam as seguintes classes de mapeamento:

1. org.demoiselle.jee.rest.exception.mapper.**ValidationExceptionMapper**: sobrescreve o mapeamento de Exception do Beans Validation
2. org.demoiselle.jee.rest.exception.mapper.**AnyOtherExceptionMapper**: captura todas as outras Exceptions do sistema

Explicar o funcionamento dos Mappers.

## Padrões de Retorno

Explicar como funciona o JSON de retorno de erro e os código HTTP de erros.

## Configurações

isShowErrorDetails

## Sobrescrita do Formato Padrão

Para sobrescrever a classe que faz o tratamento das Exceções que ocorrem na aplicação basta estender da Classe `org.demoiselle.jee.rest.exception.treatment.ExceptionTreatmentImpl` com sua implementação do método `getFormatedError` e adicionar a anotação `@Specializes`, a partir disso todas as Exceptions que ocorrerem irão cair neste método.

> É importante notar que caso existam outros ExceptionMappers encontrados no Classpath do Java é possível que nem todas as Exceções sejam tratadas por este método.

```
...
import org.demoiselle.jee.rest.exception.treatment.ExceptionTreatmentImpl;

@Specializes
public class ExceptionTreatmentApp extends ExceptionTreatmentImpl {

	@Override
	public Response getFormatedError(Throwable exception, HttpServletRequest request) {		
		// Put here your implementation of exception treatment	

		return super.getFormatedError(exception, request);
	}

}
```

## Referências

* [https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-2rd-edition/content/en/part1/chapter7/exception\\_handling.html](https://dennis-xlc.gitbooks.io/restful-java-with-jax-rs-2-0-2rd-edition/content/en/part1/chapter7/exception\_handling.html)



