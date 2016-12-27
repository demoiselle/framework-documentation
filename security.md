# Security

O Componente de segurança tem duas implementações, uma baseada em JWT e a outra em um Token gerido pelo próprio backend, cada uma atende uma estratégia de uso para webapps e/ou microservices.

O módulo security é o gestor do contexto de segurança, suas implementações fazem a gestão do objeto de autorização, detalharemos a seguir como cada implementação funciona.

Para utilizar o módulo de segurança você deve importar a dependência 

```xml
<dependency>
   <groupId>org.demoiselle.jee</groupId>
   <artifactId>demoiselle-security-jwt</artifactId>
   <version>3.0.0</version>
</dependency>
```        
ou

```xml
<dependency>
   <groupId>org.demoiselle.jee</groupId>
   <artifactId>demoiselle-security-token</artifactId>
   <version>3.0.0</version>
</dependency>
```    

Dependendo da estratégia escolhida.

Caso você crie sua app a partir de um parent

```xml
    <parent>
        <groupId>org.demoiselle.jee</groupId>
        <artifactId>demoiselle-parent</artifactId>
        <version>3.0.0</version>
    </parent>
```
Ele já agrega o módulo JWT, não há necessidade de colocar a dependência.

O módulo de segurança funciona independente e em qualquer app JEE7.

Para manter o usuário no contexto de segurança usamos a Classe DemoisellePrincipal.

Exemplo de login com JWT:

```bash

    @Inject
    private SecurityContext securityContext;

    @Inject
    private DemoisellePrincipal loggedUser;

    @POST
    @Asynchronous
    public void login(@Suspended final AsyncResponse asyncResponse, Credentials credentials) {
        asyncResponse.resume(doLogin(credentials));
    }

    private Response doLogin(Credentials credentials) {
    // sua implementação para verificar se o usuário e senha estão ok
        Usuario usu = dao.verifyEmail(credentials.getUsername(), credentials.getPassword());
        if (usu != null) {
        // aqui é carregado o DemoisellePrincial e entregue ao 
        // contexto de segurança para ser gerado um token novo
            loggedUser.setName(usu.getNome());
            loggedUser.setIdentity("" + usu.getId());
            loggedUser.addRole(usu.getPerfil());
            loggedUser.addPermission("SWAGGER", "LIST");
            loggedUser.addParam("Fone", usu.getFone());
            loggedUser.addParam("Email", usu.getEmail());
            securityContext.setUser(loggedUser);
        } else {
            throw new DemoiselleSecurityException(bundle.invalidCredentials(), Response.Status.UNAUTHORIZED.getStatusCode());
        }
        return Response.ok().entity("{\"token\":\"" + token.getKey() + "\"}").build();
    }

    @GET
    @LoggedIn
    public void retoken(@Suspended final AsyncResponse asyncResponse) {
        asyncResponse.resume(doRetoken());
    }

    private Response doRetoken() {
    // Esse processo gera um novo token com o tempo limite novo
    // em caso de revogação nesse ponto deve-se verificar se o 
    // usuário pode renovar o token ou é negado
        loggedUser = securityContext.getUser();
        securityContext.setUser(loggedUser);
        return Response.ok().entity("{\"token\":\"" + token.getKey() + "\"}").build();
    }

    @GET
    @Path("publicKey")
    public Response getPublicKey() {
    // disponibilizar a chave pública permite outros servidores
    // poderem confiar nos tokens gerados nesse servidor
    // é uma boa pratica expor a chave pública
        return Response.ok().entity("{\"publicKey\":\"" + config.getPublicKey() + "\"}").build();
    }
```

CORS


O módulo seguraça fornece uma solução para cors, onde existe uma configuração e duas anotações.

```bash
    # Habilita/Desabilita o cors por padrão 
    demoiselle.security.corsEnabled=true
```
Você tem que colocar um arquivo de propriedades chamado demoiselle.properties na sua app com essa configuração:

```bash
    # Habilita/Desabilita o cors por padrão 
    @Cors
    Habilita o cors no método anotado quando a configuração for
    demoiselle.security.corsEnabled=false
    @NoCors
    Desabilita o cors no método anotado quando a configuração for
    demoiselle.security.corsEnabled=true
```

As anotações servem para propiciar as exceções a regra registrada no demoiselle.properties (demoiselle.security.corsEnabled) refletindo em toda app.
