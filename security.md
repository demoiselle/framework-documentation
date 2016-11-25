# Security

O Componente de segurança tem duas implementações, uma baseada em JWT e a outra em um Token gerido pelo próprio backend, cada uma atende uma estratégia de uso de webapps e microservices.

O módulo security é o gestor do contexto de segurança e suas implementações fazem a gestão do objeto de autorização, detalharemos a seguir como cada implementação funciona.

Para utilizar o módulo de segurança você deve importar a dependência 

```bash
<dependency>
   <groupId>org.demoiselle.jee</groupId>
   <artifactId>demoiselle-security-jwt</artifactId>
   <version>3.0.0</version>
</dependency>
```        
ou

```bash
<dependency>
   <groupId>org.demoiselle.jee</groupId>
   <artifactId>demoiselle-security-token</artifactId>
   <version>3.0.0</version>
</dependency>
```    

Dependendo da estratégia escolhida.

Caso você crie sua app a partir de um parent

```bash
    <parent>
        <groupId>org.demoiselle.jee</groupId>
        <artifactId>demoiselle-parent</artifactId>
        <version>3.0.0</version>
    </parent>
```
Ele já agrega o módulo JWT, não há necessidade de colocar a dependência.

O módulo de segurança funciona independente e em qualquer app JEE7.

Para manter o usuário no contexto de segurança usamos a Classe DemoisellePrincipal.

Exemplo de login:

```bash

    @Inject
    private SecurityContext securityContext;

    @Inject
    private DemoisellePrincipal loggedUser;

   @POST
    public void login(Credentials credentials) {
    //Você deve ter sua implementação de validação
    // que pode estar no SGDB, LDAP, Outro servidor...
        Usuario usu = dao.verifyEmail(credentials.getUsername(), credentials.getPassword());
        // Aqui você carrega os dados do usuário e entrega ao 
        // contexto de segurança que vai gerenciar seu ciclo de vida
        if (usu != null) {
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
```

Você tem que colocar um arquivo de propriedades chamado demoiselle.properties na sua app com as seguintes configurações:

```bash
    # Habilita/Desabilita o cors por padrão 
    demoiselle.security.corsEnabled=true
```

