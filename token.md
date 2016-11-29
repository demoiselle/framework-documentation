# Security - Token

O componente gerencia um token UUID criado pelo servidor, nesta opção não permite cluster ou validação distrubuída, geralmente recomendada para apps que tenham como requisito um nível de segurança menor, geralmente para microserviços pontuais e consultas específicas.



Exemplo de serviço de autenticação
```bash
    @Inject
    private SecurityContext securityContext;

    @Inject
    private DemoisellePrincipal loggedUser;

    @Inject
    private Token token;

    @Inject
    private DemoiselleSecurityMessages bundle;

    @POST
    @Asynchronous
    public void testeLogin(@Suspended final AsyncResponse asyncResponse, Credentials credentials) {
        asyncResponse.resume(doLogin(credentials));
    }

    private Response doLogin(Credentials credentials) {
        Usuario usu = dao.verifyEmail(credentials.getUsername(), credentials.getPassword());
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

    @GET
    @LoggedIn
    public Response retoken() {
        loggedUser = securityContext.getUser();
        securityContext.removeUser(loggedUser);
        securityContext.setUser(loggedUser);
        return Response.ok().entity("{\"token\":\"" + token.getKey() + "\"}").build();
    }

    @DELETE
    @LoggedIn
    public Response getDelete() {
        securityContext.removeUser(securityContext.getUser());
        return Response.ok().entity("Removido").build();
    }
```

Nesse modelo o usuário deve ter controle do ciclo de vida do seu token, deletando quando for preciso.
