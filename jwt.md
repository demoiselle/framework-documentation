# Security - JWT

"Os JWTs são uma seqüência segura, compacta e autônoma com informações significativas que normalmente são assinadas ou criptografadas digitalmente. Eles estão se tornando rapidamente um padrão de fato para implementações de tokens na web". -(Micah Silverman | August 13, 2015)

O padrão proposto pelo JWT é o mais maduro encontrado hoje e sua documentação de referência RFC7519(https://tools.ietf.org/html/rfc7519) explora e soluciona as principais questões de funcionamento desse modelo tão bem sucedido. Você pode encontrar implementações em várias linguagens no link (https://jwt.io/).
 O JWT que é uma das especificações do projeto JOSE(http://jose.readthedocs.io/en/latest/) 
 
O funcionamento básico é baseado na validação de assinaturas com chaves simétricas e assimétricas. Optamos pelo uso das chaves assimétricas por possibilitar a validação dos tokens de forma distribuída sem a necessidade de conexão entre servidores.

####Configuração (demoiselle.properties)

```properties
    # Define se a app é master/slave 
    demoiselle.security.jwt.type=master
```
Existem dois tipos de gestão do token, são eles:
- master, nesse tipo de abordagem o servidor tem ambas as chaves e tem a capacidade de assinar e validar tokens.
- slave, são os servidores onde só necessitam da chave pública para validar se foi realmente criado pelo servidor master, gerando com isso um ciclo de confiança sem a necessidade de qualquer tipo de conexão entre os servidores

```properties
    # Chave public
    demoiselle.security.jwt.publicKey=-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA09A11Zaqmp5ZBTOCxgJ8qqtHhb6b-----END PUBLIC KEY-----
    # Chave privada
    demoiselle.security.jwt.privateKey=-----BEGIN PRIVATE KEY-----MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDT0DXVlqqanlkFM4LGAnyq7u+IcUizfs6OQTmTR3Xp6LryES/rLn0vwZKZIvo=-----END PRIVATE KEY-----
   
```

Aqui estão as chaves, existem várias formas de criação de chaves publicas, nós colocamos uma solução para criação das chaves automaticamente ao subir o servidor e não houver as duas propriedades no demoiselle.properties. Será escrito no log do servidor que deve ser copiado e colocado no demoiselle.properties. Caso você opte por gerar manualmente seguem os comandos linux:

```linux
Linux
$> openssl genrsa -out private.pem 2048
$> openssl rsa -in private.pem -outform PEM -pubout -out public.pem
```
Agora copie o conteúdo do private.pem e do public.pem para o properties.demoiselle

Temos mais umas propriedades que definem o uso da especificação

```properties
# Tempo de vida do token em millisegundos, após esse tempo ele será considerado inválido
# 300000 milisegundos são equivalentes a 5 minutos
demoiselle.security.jwt.timetoLiveMilliseconds=300000 

# Escolha o algoritmo da assinatura, RS256, RS384 e RS512 (pode afetar o tamanho do token)
demoiselle.security.jwt.algorithmIdentifiers=RS256

# Identificação de quem criou o token
demoiselle.security.jwt.issuer=STORE
# Identificação de quem pode utilizar o token
demoiselle.security.jwt.audience=web

```
As propriedades Issuer e Audience, devem ser definidas por padrão no demoiselle.properties, mas podem ser alterados na geração do token. Caso sua app Master gere tokens para a mesma app uma única app Slave, é recomendado usar o que está definido nas propriedades, mas se sua app Master gera tokens para multiplas apps diferentes, algo parecido como o SSO(Single Sign On), você pode criar tokens para determinados seridores, alterando as propriedades Issuer e Audience, para serem validadas somente em servidores que conheçam essas propriedades.
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.
TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ
```

São três hash separados por ponto(.) 
O primeiro identifica o tipo e o algoritimo usado para assinar a segunda parte que é um base64 de um json que contem informações usadas pela implementação do JWT e os dados do DemoisellePrincipal. Na terceira e última parte é a assinatura do segundo hash, garantindo a inviolabilidade da informação.
A assinatura é gerada pela chave privada e validada pela chave pública

Exemplo de demoiselle.properties (Master)

```bash
    demoiselle.security.jwt.type=master
    demoiselle.security.jwt.publicKey=-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA09A11Zaqmp5ZBTOCxgJ8qqtHhb6b-----END PUBLIC KEY-----
    demoiselle.security.jwt.privateKey=-----BEGIN PRIVATE KEY-----MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDT0DXVlqqanlkFM4LGAnyq7u+IcUizfs6OQTmTR3Xp6LryES/rLn0vwZKZIvo=-----END PRIVATE KEY-----
    demoiselle.security.jwt.timetolive=5
    demoiselle.security.jwt.issuer=STORE
    demoiselle.security.jwt.audience=web
```
Exemplo de demoiselle.properties (Slave)

```bash
    demoiselle.security.jwt.type=slave
    demoiselle.security.jwt.publicKey=-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA09A11Zaqmp5ZBTOCxgJ8qqtHhb6b-----END PUBLIC KEY-----
    demoiselle.security.jwt.timetolive=5
    demoiselle.security.jwt.issuer=STORE
    demoiselle.security.jwt.audience=web
```