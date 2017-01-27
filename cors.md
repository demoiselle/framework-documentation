### CORS

Por motivos de segurança os navegadores HTTP impedem que solicitações  
realizadas por execuções de instruções Javascript das APIS  XMLHttpRequest  
ou Fetch que solicitem conteúdo em outros domínios que não aquele que foi utilizado para o carregamento  
da página.

Navegadores modernos implementam funcionalidades para contornar esta limitação  
utilizando uma solução denominada cross-origin HTTP request \(CORS\). CORS consiste  
em uma estratégia onde o servidor solicitado fornece informações extras no cabeçalho  
da resposta de uma solicitação informando quais os domínios tem permissão  
de acesso por aplicações que fazem a requisição APIS XMLHttpRequest ou Fetch.

O módulo seguraça fornece uma solução para a utilização de CORS por mgretruefalseeio de  
manipulação do parâmetro demoiselle.security.corsEnabled do arquivo de configuração  
demoiselle.properties que fica na pasta raiz da aplicação. Esta configuração afeta  
o comportamento padrão de toda aplicação, ou seja seja caso seja definido com  
o valor false nenhuma requisição poderá ser feita a outro domínio

```properties
    # Habilita/Desabilita o cors por padrão 
    demoiselle.security.corsEnabled=true

# Propriedades do cors que serão enviadas no head  
# cada app deve colocar as suas, essas são nossas sugestões

demoiselle.security.paramsHeaderCors.Access-Control-Allow-Headers=origin, content-type, accept, Authorization
demoiselle.security.paramsHeaderCors.Access-Control-Allow-Credentials=true
demoiselle.security.paramsHeaderCors.Access-Control-Allow-Origin=*
demoiselle.security.paramsHeaderCors.Access-Control-Allow-Methods=HEAD, OPTIONS, TRACE, GET, POST, PUT, PATCH, DELETE
demoiselle.security.paramsHeaderCors.Access-Control-Max-Age=3600000
```

O arquivo demoiselle.properties fica na pasta resources do projeto.

É possível alterar o comportamento de CORS definido pelo arquivo de configuração  
em métodos onde comportamento diferente do padrão seja desejado, bastando para isso inserir  
as anotações @Cors\(enabled = "false" ou "true"\), conforme apresentado no seguinte fragmento de código:

```properties
    # Habilita/Desabilita o cors por padrão 
    @Cors(enabled = false)
    Habilita o cors no método anotado quando a configuração for
    demoiselle.security.corsEnabled=false
    @Cors(enable = true)
    Desabilita o cors no método anotado quando a configuração for
    demoiselle.security.corsEnabled=true
```



