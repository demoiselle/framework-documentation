# Security - JWT

"Os JWTs são uma seqüência segura, compacta e autônoma com informações significativas que normalmente são assinadas ou criptografadas digitalmente. Eles estão se tornando rapidamente um padrão de fato para implementações de tokens na web". -\(Micah Silverman \| August 13, 2015\)

O padrão proposto pelo JWT é o mais maduro encontrado hoje e sua documentação de referência RFC7519\([https://tools.ietf.org/html/rfc7519](https://tools.ietf.org/html/rfc7519)\) explora e soluciona as principais questões de funcionamento desse modelo tão bem sucedido. Você pode encontrar implementações em várias linguagens no link \([https://jwt.io/](https://jwt.io/)\).  
 O JWT que é uma das especificações do projeto JOSE\([http://jose.readthedocs.io/en/latest/](http://jose.readthedocs.io/en/latest/)\)

O funcionamento básico é baseado na validação de assinaturas com chaves simétricas e assimétricas. Optamos pelo uso das chaves assimétricas por possibilitar a validação dos tokens de forma distribuída sem a necessidade de conexão entre servidores.

##### Configuração \(demoiselle.properties\)

```properties
    # Define se a app é master/slave 
    demoiselle.security.jwt.type=master
```

Existem dois tipos de gestão do token, são eles:

* master, nesse tipo de abordagem o servidor tem ambas as chaves e tem a capacidade de assinar e validar tokens.
* slave, são os servidores onde só necessitam da chave pública para validar se foi realmente criado pelo servidor master, gerando com isso um ciclo de confiança sem a necessidade de qualquer tipo de conexão entre os servidores

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

* Os cabeçalhos das chaves devem seguir o padrão -----BEGIN PUBLIC KEY-----, em alguns casos são gerados o tipo da chave como RSA no cabeçalho, isso deve ser removido. 

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

As propriedades Issuer e Audience, devem ser definidas por padrão no demoiselle.properties, mas podem ser alterados na geração do token. Caso sua app Master gere tokens para a mesma app uma única app Slave, é recomendado usar o que está definido nas propriedades, mas se sua app Master gera tokens para multiplas apps diferentes, algo parecido como o SSO\(Single Sign On\), você pode criar tokens para determinados seridores, alterando as propriedades Issuer e Audience, para serem validadas somente em servidores que conheçam essas propriedades.

```jwt
eyJraWQiOiJkZW1vaXNlbGxlLXNlY3VyaXR5LWp3dCIsImFsZyI6IlJTMjU2In0.
eyJpc3MiOiJTVE9SRSIsImV4cCI6MTAwMTQ4MjQ5NTI3MCwiYXVkIjoid2ViIiwianRpIjoiTmxvU0NFUnktd2xXdVhtaGZhVi1IUSIsImlhdCI6MTQ4MjQ5NTI3MSwibmJmIjoxNDgyNDk1MjExLCJpZGVudGl0eSI6IjEiLCJuYW1lIjoiVGVzdGUiLCJyb2xlcyI6WyJBRE1JTklTVFJBVE9SIiwiTUFOQUdFUiJdLCJwZXJtaXNzaW9ucyI6eyJDYXRlZ29yaWEiOlsiQ29uc3VsdGFyIiwiQWx0ZXJhciIsIkluY2x1aXIiXSwiUHJvZHV0byI6WyJBbHRlcmFyIiwiRXhjbHVpciJdfSwicGFyYW1zIjp7ImZvbmUiOiI0MTM1OTM4MDAwIiwiZW5kZXJlY28iOiJydWEgY2FybG9zIHBpb2xpLCAxMzMiLCJlbWFpbCI6InVzZXJAZGVtb2lzZWxsZS5vcmcifX0.
EV8L1OEFVMsuCgVSz3gyM2mJIEHczhHBvxSjTFGslHGKItlFtM32BUrzbzA9QECzSUkk-ITnUEtmm-ERTH529clymKX1-LGcboPSQlNAHv4SNRD5i8eJxjlCz_cMSTIdSZRSYOSJZHJHYf0kWEvo1vTthLGWcH_D--b9K_WYDR9hrVmljof46Dd4THXv5_VY9RJlYVHJ1bpIl69f0UDtVzDqfxNSTsBCm6tZXS40f9dh_qjEWATZeMJmjd_t2ZRzXDSLHHbJpLnNOGd2yOdp9H4tmGCxViguRa4Jck6C7cpMM6QIFB7ta67XzS4nl0NTqY64rNseKcyQS-TdAbPxAA
```

Exemplo de token gerado pelo Demoiselle que pode ser validado em \([https://jwt.io/](https://jwt.io/)\)

São três hash separados por ponto\(.\)  
O primeiro identifica o tipo e o algoritimo usado para assinar a segunda parte que é um base64 de um json que contem informações usadas pela implementação do JWT e os dados do DemoiselleUser. Na terceira e última parte é a assinatura do segundo hash, garantindo a inviolabilidade da informação.  
A assinatura é gerada pela chave privada e validada pela chave pública

```json
{
  "iss": "STORE", (RCF7519)
  "exp": 1001482495270, (RCF7519)
  "aud": "web", (RCF7519)
  "jti": "NloSCERy-wlWuXmhfaV-HQ", (RCF7519)
  "iat": 1482495271, (RCF7519)
  "nbf": 1482495211, (RCF7519)
  "identity": "1", (DemoiselleUser)
  "name": "Teste", (DemoiselleUser)
  "roles": [ (DemoiselleUser)
    "ADMINISTRATOR",
    "MANAGER"
  ],
  "permissions": { (DemoiselleUser)
    "Categoria": [
      "Consultar",
      "Alterar",
      "Incluir"
    ],
    "Produto": [
      "Alterar",
      "Excluir"
    ]
  },
  "params": { (DemoiselleUser)
    "fone": "4135938000",
    "endereco": "rua carlos pioli, 133",
    "email": "user@demoiselle.org"
  }
}
```

* [https://tools.ietf.org/html/rfc7519](https://tools.ietf.org/html/rfc7519)

Vou especificar as informações que estão vindo do DemoiselleUser:

* identity : Um código que identifique unicamente seu usuário no sistema, sugerimos que seja usado um UUID ou email, mas nunca chaves naturais sequenciais que possa de alguma forma identificar outros usuários no sistema.

* name : Nome do usuário para exibição na frontend

* roles : Array simples contendo os perfis do usuário

* params : Map com chave/valor com informações complementares do usuário \(Opcional\)

* permissions : Map com chave/Array onde estão as funcionalidades e suas permissões

##### Exemplo de demoiselle.properties \(Master\)

```properties
   # ===== Security =====
demoiselle.security.corsEnabled=true
demoiselle.security.jwt.type=master
demoiselle.security.jwt.publicKey=-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA09A11Zaqmp5ZBTOCxgJ8qqtHhb6b+eO9C8gAX3DXFIlfcl0FU7FVwfQtHWuN3KU83c1sSj9wPMuviGvZeSV7oUp2DXML76EsEKf9r+6JNZRdnTCkXZklQSEmeuftSxnMtAwo7k+eIgPpOIrYpMJv5GzVju0zbyucnwbQnDvoGv08pMrbWaGOxcnHXCazsrRTI7UKQ+jvMDB3bsUcII0XS+92ZLQkiMkKH85HSSlm4AFKuUljRh59RlpJrCDc+TUZWQuOC6Li/H9/78tAW8kJIHfASJhkKgkcdGixBJNggp+K+0hMWvxLt5fi1BXvWiy/Ma3QNHtxOCorRa+4NBB+KwIDAQAB-----END PUBLIC KEY-----
demoiselle.security.jwt.privateKey=-----BEGIN PRIVATE KEY-----MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDT0DXVlqqanlkFM4LGAnyqq0eFvpv5470LyABfcNcUiV9yXQVTsVXB9C0da43cpTzdzWxKP3A8y6+Ia9l5JXuhSnYNcwvvoSwQp/2v7ok1lF2dMKRdmSVBISZ65+1LGcy0DCjuT54iA+k4itikwm/kbNWO7TNvK5yfBtCcO+ga/TykyttZoY7FycdcJrOytFMjtQpD6O8wMHduxRwgjRdL73ZktCSIyQofzkdJKWbgAUq5SWNGHn1GWkmsINz5NRlZC44LouL8f3/vy0BbyQkgd8BImGQqCRx0aLEEk2CCn4r7SExa/Eu3l+LUFe9aLL8xrdA0e3E4KitFr7g0EH4rAgMBAAECggEABhUilqGfAJWvhMC37qu/nL8SbLrOi9yIX0A9EoCRDJvtS8F0F7Ut+0Xhzch66G0uVEhD5dXwiS5oOgiu1BXJeRZEUZqOKzF7rHbGiDjXY9yA27S745w0P6yOCFWEsPVqtXjr6/wJVHy8Q81o70JOKEcf0tzo7zZXZxGxB+uIfM3ffGNixX/tOHOERSViBvJsBp7sPZOPdgFQy1yQzYHDHob3V7BoG3bL/6ZXBIKMovBcKPoZuHDSyU1w3UbkFpPf8NbElPeMVoPxSwT1e3gfkKwBdp3bXucUnWBJE/aEkpUuIPTHiI4oGJDapmybLx+UACqp3eid48IDC25qXIq/8QKBgQD2yuECo9mKvP6CAZTl9LUUqYqwf4HLqgzOcCzDdigJaF08jLkI+9c7kMhq5C3R+VpRXCMJVHQSOEyUaOEj/m4QrF2JmfGcLxhqnDm8kZ84nX5anBxASCVcyiM2A37fpXmohczzu+Re8VFbFIMC1qJa5r/7/whmc5UL2jBVbQ7kPwKBgQDbtz6RlZWWnlJh1Dk6PuNczhmjbNRctrsVc2RcvIuZrw3BPGbeFanJVDa9Q9n2owxMs6BoSgIfrf0XGnl71yTSKtCliMyTgxg27g8iVlLA5YPaTIsazScCzLMOJmfzP2RcDBMVg+42Zu2tHhOoRAyNIpM7PQBDk1rLbpBH/HL7FQKBgGwvSW3317BK4yKogNZBbHPvUn3Gl2ZpWA3S/Lx+elSNbHnTknWOuK5C7Kh2+GMYdPA/fJhlbjBif6d7Rl6Z9TPX63Ubh9+YgZKSg3jXOT3/RFmCH5xKRB6l+cN+yspNZsRqSwr5bcX08V4E4t2Gq0s/5h8YkF0hA9BbSF7aXPHPAoGBAJ8m7TunjuO7axFSGOIIC8l9wTSP8IP4GSxAmcJTEQwRsXT3u8vDBWnAhqYyMABnutEUjGz+rusjrOC/XKBIB3P1b414ujdgDno7ltrYjLkNh6TpLRoM4OU2Qb1ONJ4OnTPPy0MafcMKa7+qubJ5GF5jXSLb3QUWB/6z5+88/kzBAoGAWzcBjuAYmRa+Q9GpGRyml1SgS0foaKwycN9Az0IGaNmN+hvKBgoJtUvY5V/sDdNAbLz+1Hpc0s3TJpghmHTVxUrC0VKqhEyXpHIUN2IOjiqoiQbB1stW0GS3N9U4akQfYQrR7u+IcUizfs6OQTmTR3Xp6LryES/rLn0vwZKZIvo=-----END PRIVATE KEY-----
demoiselle.security.jwt.timetoLiveMilliseconds=123456
demoiselle.security.jwt.algorithmIdentifiers=RS256
demoiselle.security.jwt.issuer=STORE
demoiselle.security.jwt.audience=web
```

##### Exemplo de demoiselle.properties \(Slave\)

```properties
   # ===== Security =====
demoiselle.security.corsEnabled=true
demoiselle.security.jwt.type=slave
demoiselle.security.jwt.publicKey=-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA09A11Zaqmp5ZBTOCxgJ8qqtHhb6b+eO9C8gAX3DXFIlfcl0FU7FVwfQtHWuN3KU83c1sSj9wPMuviGvZeSV7oUp2DXML76EsEKf9r+6JNZRdnTCkXZklQSEmeuftSxnMtAwo7k+eIgPpOIrYpMJv5GzVju0zbyucnwbQnDvoGv08pMrbWaGOxcnHXCazsrRTI7UKQ+jvMDB3bsUcII0XS+92ZLQkiMkKH85HSSlm4AFKuUljRh59RlpJrCDc+TUZWQuOC6Li/H9/78tAW8kJIHfASJhkKgkcdGixBJNggp+K+0hMWvxLt5fi1BXvWiy/Ma3QNHtxOCorRa+4NBB+KwIDAQAB-----END PUBLIC KEY-----
demoiselle.security.jwt.timetoLiveMilliseconds=123456
demoiselle.security.jwt.algorithmIdentifiers=RS256
demoiselle.security.jwt.issuer=STORE
demoiselle.security.jwt.audience=web
```



