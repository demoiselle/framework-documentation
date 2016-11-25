# Security - JWT

Esse componente usa o padrão JWT que é uma especificação do projeto JOSE(http://jose.readthedocs.io/en/latest/) e está definido na RFC7519(https://tools.ietf.org/html/rfc7519) e é apresentado no (https://jwt.io/)

Basicamente tem seu funcionamento baseado na validação de assinaturas com chaves simétricas e assimétricas. Optamos pelo uso das chaves assimétricas por possibilitar a validação dos tokens de forma distribuída sem a necessidade de conexão entre servidores.

Configuração (demoiselle.properties)

```bash
    # Diz se a app é master/slave 
    demoiselle.security.jwt.type=master
```
Existem dois tipos de gestão do token, são eles:
- master, nesse tipo o servidor tem ambas as chaves e tem a capacidade de assinar e validar os tokens.
- slave, são os servidores onde só necessitam da chave pública para validar que foi realmente criado pelo servidor master gerando o ciclo de confiança sem a necessidade de qualquer tipo de conexão entre os servidores

```bash
    # Chave public
    demoiselle.security.jwt.publicKey=-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA09A11Zaqmp5ZBTOCxgJ8qqtHhb6b-----END PUBLIC KEY-----
    # Chave privada
    demoiselle.security.jwt.privateKey=-----BEGIN PRIVATE KEY-----MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDT0DXVlqqanlkFM4LGAnyq7u+IcUizfs6OQTmTR3Xp6LryES/rLn0vwZKZIvo=-----END PRIVATE KEY-----
    # Tempo de vida do token em minutos, após esse tempo ele será considerado inválido
    demoiselle.security.jwt.timetolive=5
    # Identificação de quem criou o token 
    demoiselle.security.jwt.issuer=STORE
    # Identificação de quem pode utilizar o token
    demoiselle.security.jwt.audience=web
```

Logo que você entrega o DemoisellePrincipal preenchido para o contexto de segurança é gerado um token com esse objeto, no seguinte formato que pode ser validado em (https://jwt.io/)

São três hash separados por ponto(.) 
O primeiro identifica o algoritimo e o tipo usado para assinar a segunda parte que é um base64 de um json que contem informações usadas pela implementação do JWT e os dados do DemoisellePrincipal. Na última parte é a assinatura do segundo hash, garantindo a inviolabilidade da informação.
A assinatura é gerada pela chave privada e validada pela chave pública

```bash
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.
TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ
```
