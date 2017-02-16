## Security

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

Para manter o usuário no contexto de segurança usamos a Classe DemoiselleUser.

Exemplo de login com JWT:

```java
    @Inject
    private SecurityContext securityContext;

    @Inject
    private DemoiselleUser loggedUser;

    @POST
    public Response login(Credentials credentials) {
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
        return Response.ok().entity(token.getKey().toString()).build();
    }

    @GET
    @Autenticated
    public Response retoken() {
    // Esse processo gera um novo token com o tempo limite novo
    // em caso de revogação nesse ponto deve-se verificar se o 
    // usuário pode renovar o token ou é negado
        loggedUser = securityContext.getUser();
        securityContext.setUser(loggedUser);
        return Response.ok().entity(token.getKey().toString()).build();
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

```properties
# Propriedades de segurança que serão enviadas no head  
# cada app deve colocar as suas, essas são nossas sugestões

demoiselle.security.paramsHeaderSecuriry.x-content-type-options=nosniff
#https://scotthelme.co.uk/hardening-your-http-response-headers/#x-content-type-options

demoiselle.security.paramsHeaderSecuriry.x-frame-options=SAMEORIGIN
#https://scotthelme.co.uk/hardening-your-http-response-headers/#x-frame-options

demoiselle.security.paramsHeaderSecuriry.x-xss-protection=1; mode=block
#https://scotthelme.co.uk/hardening-your-http-response-headers/#x-xss-protection

demoiselle.security.paramsHeaderSecuriry.Strict-Transport-Security=strict-transport-security: max-age=31536000; includeSubDomains
#https://scotthelme.co.uk/hsts-the-missing-link-in-tls/

demoiselle.security.paramsHeaderSecuriry.Content-Security-Policy=script-src 'self'
#https://scotthelme.co.uk/content-security-policy-an-introduction/

#demoiselle.security.paramsHeaderSecuriry.Public-Key-Pins=
#https://scotthelme.co.uk/hpkp-http-public-key-pinning/

#https://tools.ietf.org/html/rfc6797
#https://tools.ietf.org/html/rfc7762
#https://tools.ietf.org/html/rfc7469

# visite https://securityheaders.io 
```

Para maiores informações visite [https://developer.mozilla.org/en-US/docs/Web/HTTP/Access\_control\_CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS)

