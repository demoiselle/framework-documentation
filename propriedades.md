# Configurações disponíveis no Demoiselle Rest {#configurações-disponíveis-no-demoiselle-rest}

Configuração deve ser colocada no arquivo demoiselle.properties:

```
# demoiselle.rest.gzipEnabled=false
   Essa configuração se tiver true permite compactar, no formato gzip, o
   conteúdodo corpo do request, sendo necessário o cliente desfazer a
   compactação para ler e compactar os dados para enviar ao servidor.
   Estratégia usada quando a limitação de banda for grande e o
   processamento de compactação não seja impeditivo.

```


