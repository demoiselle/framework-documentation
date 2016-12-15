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

Na pasta $home/.m2 crie ou edite o arquivo settings.xml.

```
<settings>
	<servers>
		<server>
			<id>sonatype-nexus-staging</id>
			<username>USER</username>
			<password>PASSWORD</password>
		</server>
		<server>
			<id>sonatype-nexus-snapshots</id>
			<username>USER</username>
			<password>PASSWORD</password>
		</server>
	</servers>
	<profiles>
		<profile>
			<id>gpg</id>
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
			<properties>
				<gpg.passphrase>PASSWORD</gpg.passphrase>
			</properties>
		</profile>
	</profiles>
</settings>```

Lembrando que o profile pgp será utilizado para associar a sua chave gpg e o usuário deve ser o mesmo cadastrado na sonatype.

## Gerando Snapshot

Para gerar uma versão snapshot na Sonatype basta executar;

```mvn deploy```

### Gerando uma Release

Segue exemplo para geração de um release

Verifique se tudo está ok. 

```mvn clean install```

Execute a prepação da release

```mvn -Prelease -B -Dtag=3.0.0-BETA3 release:prepare -DreleaseVersion=3.0.0-BETA3 -DdevelopmentVersion=3.0.0-RC1-SNAPSHOT```

Execute a release

```mvn -Prelease release:perform```

## Licenças

Para verificar se os arquivos possuem licença em seus cabeçalhos basta chamar o comando:

```mvn license:check```

Caso alguma arquivo não apresente o cabeçalho é possível inclui-lo com o comando:

```mvn license:format```

