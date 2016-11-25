# Security - JWT

Esse componente usa o padrão JWT que é uma especificação do projeto JOSE(http://jose.readthedocs.io/en/latest/) e está definido na RFC7519(https://tools.ietf.org/html/rfc7519) e é apresentado no (https://jwt.io/)

Basicamente tem seu funcionamento baseado na validação de assinaturas com chaves simétricas e assimétricas. Optamos pelo uso das chaves assimétricas por possibilitar a validação dos tokens de forma distribuída sem a necessidade de conexão entre servidores.

Configuração

