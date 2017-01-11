# Build

Este Capítulo visa apresentar as ferramentas e  atividades relacionada a build do Framework Demoiselle bem como o seu processo de release.

## Integração Contínua

A integração contínua das builds do Framework Demoiselle são realizadas pela ferramenta Travis CI:  
[https://travis-ci.org/demoiselle](https://travis-ci.org/demoiselle)

### Cobertura de Testes

A cobertura dos testes unitários estão disponíveis na ferramenta Coveralls: [https://coveralls.io/github/demoiselle](https://coveralls.io/github/demoiselle)

### Sonar

As métricas de código são publicadas no Sonarqube: [https://sonarqube.com/dashboard?id=org.demoiselle.jee%3Ademoiselle-build](https://sonarqube.com/dashboard?id=org.demoiselle.jee%3Ademoiselle-build)

## Release Sonatype

### Settings.xml

Essa seção descreve as configurações e procedimentos necessários para  publicação de uma versão do Framework Demoiselle.

Iniciamos as configurações com o arquivo _settings.xml_ utilizado no repositório local maven.

Na pasta $home/.m2 crie ou edite o arquivo settings.xml.

```xml
<settings>
    <servers>
        <server>
            <id>sonatype-nexus-staging</id>
            <username>user</username>
            <password>password</password>
        </server>
        <server>
            <id>sonatype-nexus-snapshots</id>
            <username>user</username>
            <password>password</password>
        </server>
    </servers>
    <profiles>
        <profile>
            <id>gpg</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <gpg.passphrase>password</gpg.passphrase>
            </properties>
        </profile>
    </profiles>
</settings>
```

Lembrando que o profile pgp será utilizado para associar a sua chave gpg e o usuário deve ser o mesmo cadastrado na sonatype.

## Gerando Snapshot

Para gerar uma versão snapshot na Sonatype basta executar;

`mvn -Pdemoiselle deploy`

A versão snapshot será publicada no seguinte [https://oss.sonatype.org/content/repositories/snapshots/org/demoiselle/jee/](https://oss.sonatype.org/content/repositories/snapshots/org/demoiselle/jee/)

### Gerando uma Release

Primeiramente devemos verificar se o build está funcionando sem problemas.

`mvn -Pdemoiselle clean install`

Prepare o release, usaremos o modo sem interação com o terminal, portanto informaremos previamente o nome da _tag_, a _versão da release_ e a _nova versão para desenvolvimento_.

`mvn -Pdemoiselle -B -Dtag=3.0.0-BETA3 release:prepare -DreleaseVersion=3.0.0-BETA3 -DdevelopmentVersion=3.0.0-RC1-SNAPSHOT`

Se não houve erros na preparação executamos a release.

`mvn -Pdemoiselle release:perform`

Se houve erro na preparação é possível desfazer o processo.

`mvn -Pdemoiselle release:rollback`

Ao término da build é apresentado o ID de repositório temporário no qual é possível fazer últimos testes antes da liberação. Vide exemplo:

`[INFO] [INFO]  * Closing staging repository with ID "orgdemoiselle-1014".`

Para fazer a release desse repositório, deve-se acessar o site [https://oss.sonatype.org](https://oss.sonatype.org) com seu usuário proceder as seguintes ações:

1. Acessar o menu [Staging Repositories](https://oss.sonatype.org/#stagingRepositories);
2. Pesquisar pelo repositório temporário, no exemplo seria "orgdemoiselle-1014";
3. Selecionar o repositório procurado;
4. Acionar o botão Release;
5. Informar a descrição da release. Exemplo: "Release for Demoiselle JEE 3.X.X";
6. Confirmar.

![](/assets/release_repository.png)

O repositório temporário será fechado e a build será publicado no repositório principal em [https://oss.sonatype.org/content/repositories/releases/org/demoiselle/jee/](https://oss.sonatype.org/content/repositories/releases/org/demoiselle/jee/)

## Licenças

Para verificar se os arquivos possuem licença em seus cabeçalhos basta chamar o comando:

`mvn -Pdemoiselle license:check`

Caso alguma arquivo não apresente o cabeçalho é possível inclui-lo com o comando:

`mvn -Pdemoiselle license:format`

