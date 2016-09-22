O principal objetivo da versão 2 do Demoiselle Framework é a completa aderência à especificação JSR 316: Java Platform, Enterprise Edition 6, ou simplesmente Java EE 6. Para saber mais, recomendamos os links Introducing the Java EE 6 Platform e As novidades da JEE 6.

Esta documentação é referente às especificações da versão 2 cadastradas no tracker do Demoiselle, as quais foram publicamente discutidas no fórum demoiselle-proposal.

Os capítulos a seguir entram em detalhes sobre cada uma das principais funcionalidades do framework. Tenha uma boa leitura!

### Premissas da Construção

* Utilizar a especificação **JEE 7** como principal norteadora do projeto
* Criar o **minimo** de código possível
* Criar uma estrutura totalmente **modularizada** em que eu possa usar apenar partes do Framework

### Objetivo do Projeto

Fornecer o mínimo de utilitários e documentação para que seja construído o backend de uma aplicação.

* Segurança
    * Token
    * JWT ([Saiba mais sobre JWT](http://jwt.io))
* Persistência
    * Transação
        * CRUD
* Mensagens
* Configuração
         