# Criando aplicações com Angular 2 e Demoiselle 3


## Introdução 

Este trabalho pretende oferecer uma visão completa das estratégias relativas ao desenvolvimento de aplicações
utilizando os frameworks de desenvolvimento de aplicações Angular 2 e Demoiselle 3. Enquanto o Angular 2 tem como 
objetivos oferecer facilitadores para interfaces web o Demoiselle 3 oferece recursos de funcionalidades 
para simplificar a criação de uma estrutura básica de um projeto, insere um conjunto de tecnologias destacadas para
o desenvolvimento de aplicações, define padrões de implementação e apoia as decisões de projeto.

A versão 3 do framework Demoiselle é baseada na proposta de oferecimento de serviços REST funcionalidade esta que
agrega um grande desacoplamento entre as funcionalidades do servidor e das interfaces cliente, permitindo uma
significativa melhora de desempenho de aplicações uma vez que ocorre uma melhor divisão entre as tarefas 
realizadas pelo servidor (backend) e as interfaces (frontend). 

O uso simultâneo destes frameworks oferece a possibilidade de criação de aplicações quer sejam simples serviços que demandem de
acesso a recursos de servidor ou grandes sistemas capazes de realizarem tarefas que demandem significativos 
recursos computacionais em ambientes distribuídos além do oferecer funcionalidades que implementam maior segurança,
integração e interoperabilidade.


## Typescript básico

Ao completar esta sessão o leitor terá compreendido a sintaxe básica da linguagem e o uso de ferramentas 
necessárias ao desenvolvimento de aplicação em TypeScript.

A linguagem de programação TypeScript foi criada com o objetivo de oferecer uma opção a linguagem
JavasScript poe meio de uma sintaxe mais adequada a programadores acostumados ao
paradigma da Orientação a Objetos. 


Este tutorial de Typescript é adequado a desenvolvedores de aplicação com conhecimentos em Javascript mas sem a necessidade
de conhecimentos avançados na linguagem. Serão apresentados
alguns conceitos básicos e exemplos de aplicações transpiladas para serem executadas em navegadores
html.

O sufixo padrão para arquivos TypeScript é .ts e este será adotado neste tutorial.

Para este tutorial será necessário o uso da ferramenta tsc que pode ser instalada com o comando 

```bash
npm install -g typescript
```

Para executar o código é necessário realizar a transpilação do código com o comando 

```bash
tsc nome_do_arquivo.ts
```

O resultado deste comando, caso não exista nenhum erro no código, é um arquivo do tipo JavaScript com o mesmo nome do
arquivo .ts mas com o sufixo .js. Para executar a aplicação é possível utilizar um navegador web acessando uma página
html que deve importar o arquivo que contém o código Javascript.

```html
<html>
    <head><title>TypeScript Exemplos</title></head>
    <body>
        <script src="nome_do_arquivo.js"></script>
    </body>
</html>
```

Opcionalmente, no caso de algumas funcionalidades, é possível utilizar o comando 

```bash
node nome_do_arquivo.js
```


### Tipos em Typscript
Uma das grandes diferenças entre Javascript e Typescript é a possibilidade de tipagem de variáveis. Esta é uma característica
útil para o ambiente de desenvolvimento mas que não tem nenhuma funcionalidade no código transpilado sendo necessário criar
funções específicas no caso de necessidade da implementação de funcionalidades adequadas a verificação quando houver necessidade
de validação de tipos. Variáveis devem ser declaradas explicitamente como tais utilizando as palavras reservadas let e var. 
Não existe, no entanto, a necessidade de definir o tipo para uma variável mas quando está recebe algum valor
não é possível tentar carrega-lá com outro tipo de dado como por exemplo:

```javascript
let teste = 1;
teste = "Um";
```

**Os tipos primitivos em Typescript são**

boolean valores lógicos true e false (não são aceitos os valores 0 ou 1)
number valores numéricos inteiros ou decimais
string caracteres ou cadeia de caracteres, delimitados por aspas ou apostrofe;


**Listas**

Listas podem ser tipadas ou não, dependendo da forma com a mesma é instanciada:

```javascript
var listaGenerica=['a', 1, false];
var listaNumerica:number[]=[1, 2, 2.5];
let listaArrayNumerico: Array<number>=[0,1,2,2.5];//Mesmo efeito da listaNumerica
```

**É possível utilizar tipo complexos nas variáveis definindo a mesma baseada em uma interface ou classe**

```javascript
interface Pessoa{
    nome:string;
    idade:number;
    juridica:boolean;
}

let pessoa : Pessoa;
```

Em Typescript é possível referenciar um objeto instanciado a partir de uma interface utilizando a notação
de objetos javascript como por exemplo:

```javascript
var p:Pessoa = {firstName:"Fulano", lastName:"Beltano"}
```

### Loops e tomada de decisão

A sintaxe é a mesma utilizada em JavaScript, utilizando os simbolos {} para definição de blocos if..else ou 
switch case para controle de fluxo; for(;;) e while() para loops.

É permitida a notação ternária para tomada de decisão de fluxos de execução de programas.


```javascript
let tst:number = 1;
let result:string = tst > 0 ? "Maior que zero":"Menor que zero";
```

### Funções

A implementação de funções também permite a tipagem de retorno podendo ser construídas utilizando diversas sintaxes.
O exemplo a seguir apresenta várias formas de representar uma função que calcule o quadrado de um número.

```javascript
var pow21 = function(i: number): number { return i * i; }
var pow22 = function(i: number) { return i * i; }
var pow23 = (i: number): number => { return i * i; }
var pow24 = (i: number) => { return i * i; }
var pow25 = (i: number) =>  i * i;

console.log(pow21(2)+" "+pow22(2)+" "+" "+pow23(2)+" "+pow25(2)+" "+pow25(2));
```

A linguagem permite o uso de parâmetros opcionais utilizando o simbolo "?" junto ao nome da variável. 

### Módulos Externos e Namespace

Módulos e namespaces são utilizados para agrupar fragmentos de código como classes, interfaces e funções em uma unidade que
pode ser exportada para outro módulos. No passado utilizava-se o conceito de módulos internos ao invés de namespace mas
este tipo de uso é considerado obsoleto. Módulos permitem uma maior organização do código é geralmente são construído 
em um arquivo para cadas módulo ou namespace.

### Classes

Este trabalho se limitará a apresentação de implementação dos conceitos
de uso de classes se se alongar nas questões teóricas da orientação a objetos. Em TypeScript uma classe é definida
pelo uso da palavra reservada class. A instanciação de um objeto é feito por meio da palavra reservada new seguida
pelo nome da classe.


```javascript
class Classe{
    umaPropriedade:string;
    umMetodo(umParametro:string){
        return umParametro;
    }
}
```

A sintaxe da definição de classes permite o uso de construtores por meio da palavra reservada **constructor**;

```javascript
class Classe{
    constructor (parametroDoConstrutor: string){
        console.log("Hello classe");
    }
    umaPropriedade:string;
    umMetodo(umParametro:string){
        return umParametro;
    }
}
```

Para instanciar uma classe 

```javascript
let minhaClasse:Classe = new Classe("Hello Classe");

console.log(minhaClasse.umMetodo("Hello metodo"));

```

## Criando uma aplicação web Angular 2

Ao concluir esta sessão o leitor estará apto a construir e executar uma aplicação básica 
utilizando o framework Angular 2.

O framework Angular 2 foi concebido como uma ferramenta para oferecer maior qualidade e agilidade
no desenvolvimento de aplicações web. Uma aplicação Angular 2 é baseada em componente que 
correspondem à uma combinação de modelos em HTML e classes que controlam partes da tela. Apesar
de ser possível o desenvolvimento de todas as funcionalidades proporcionadas pelo Angular 2
em JavaScript grande parte do esforço de desenvolvimento da solução está fundamentado no uso
da linguagem TypeScript:

É possível implementar aplicações Angular 2 utilizando apenas
JavaScript mas grande parte da documentação é baseada em TypeScript e a implementação do framework Demoiselle
optou por esta linguagem de programação come referência para o desenvolvimento de aplicações.

Nesta sessão serão explorados conceitos básicos para a criação e execução de uma aplicação capaz de ser
executada por um navegador HTTP. Apesar da simplicidade da transpilação de arquivos escritos em 
TypeScript para JavaScript, a adequação das funcionalidade para seu correto funcionamento nos 
interpretadores JavaScript dos navegadores demanda algumas configurações.

### Iniciando e carregando as dependências

npn init -f


```bash
npm install @angular/common @angular/compiler @angular/core @angular/forms @angular/http @angular/platform-browser @angular/platform-browser-dynamic @angular/router core-js rxjs zone.js reflect-metadata systemjs --save
```

Para instalar o transpilador, ferramenta que faz a conversão de uma linguagem para outra, da linguagem Typescript para Javascript
é necessário instalar a ferramenta typescript na sessão que mantem as informações das dependências para o ambiente de
desenvolvimento no arquivo package.json com o comando

```bash
npm install typescript typings @types/core-js --save-dev
```


### Criando os arquivos

Arquivo inicial utilizado para carregar as dependências da aplicação

**main.ts**
```javascript
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { AppModule } from './app.module';
platformBrowserDynamic().bootstrapModule(AppModule);
```

Criar um módulo da aplicação

**app.module.ts**
```javascript
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
@NgModule({
    imports: [
        BrowserModule
    ],
    declarations: [
        AppComponent
    ],
    bootstrap: [ AppComponent ]
})
export class AppModule { }
```

Criar o componente do módulo

**app.component.ts**
```javascript
import { Component } from '@angular/core';
@Component({
    selector: 'my-app',
    templateUrl: './app.component.html',
})

export class AppComponent {
    titulo:string = "Titulo da aplicacação";
    nome:string = "Fulando de tal";
    lista:Array<string>=["Nome 1", "Nome 2", "Nome 3"];
}
```

Como o componente aponta para um arquivo de modelo será necessário criar o mesmo:

**app.component.ts**
```html
<h1>{{title}}</h1>
    <h2>Um nome: {{nome}}
</h2>
```
Para a transpilação do código é possível passar parâmetros para a aplicação por meio da linha de comando 
que permitem tanto alterar o código gerado quanto resultados relativos ao armazenamento forma de análise,
dependências, etc. Para facilitar o uso de parâmetros pelo tsc é possível utilizar um arquivo de configuração
com o nome tsconfig.json onde estes parâmetros podem ser armazenados.

Para o nosso exemplo utilizaremos os seguintes parâmetros no tsconfig.json:

```javascript
{
  "compilerOptions": {
    "target": "es5",
    "module": "commonjs",
    "moduleResolution": "node",
    "emitDecoratorMetadata": true,
    "experimentalDecorators": true,
    "sourceMap": true,
    "suppressImplicitAnyIndexErrors":true,
    "rootDir":"app",
    "outDir": "./js"
  },
  "compileOnSave": false,
  "buildOnSave": false
}
```

Para maiores informações sobre parêmetros consulte https://www.typescriptlang.org/docs/handbook/compiler-options.html

Para gerar o código JavaScript tsc básico basta digitar o comando tsc. Será criada uma pasta chamada js no diretório
corrente contendo arquivos .js que realizaram as funcionalidades necessárias a aplicação. Apesar dos arquivos 
já estarem em um formato de JavaScript os interpretadores dos navegadores não serão capazes de processa-los, é 
necessário adequar a informação utilizando uma ferramenta capaz de realizar esta tarefa. Neste trabalho
será utilizado o programa browserify que pode ser instalado com o seguinte comando:

```bash
npm install --global browserify
```

Na pasta js execute o processo de adequação do conteúdo gerando o arquivo que será importado pela interface HTML 
com o comando:

```bash
browserify main.js -o bundle.js
```

Para testar o funcionamento da aplicação basta criar um arquivo HTML importando os scripts necessários a execução
da aplicação

```html
<!DOCTYPE html>
<html>
<head>
    <base href="/">
    <script src="angular2/node_modules/zone.js/dist/zone.js"></script>
    <script src="angular2/node_modules/reflect-metadata/Reflect.js"></script>
    <script src="angular2/node_modules/systemjs/dist/system.src.js"></script>

    <script src='./angular2/bundle.js'></script>
    <title>Angular With Webpack</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
    <my-app>Loading...</my-app>
</body>
</html>
```
Para finalizar a construção do ambiente copie o arquivo index.html e as dependências para um diretório 
visível por seu servidor http, no caso do exemplo acima foi criada a pasta angular2.

```bash
cp js/bundle.js index.js node_modules /var/www/html/angular2
``` 

Acesse a aplicação pelo navegador.


## Configurando o ambiente com webpack

Ao concluir esta sessão o leitor estará apto a contruir um ambiente mais adequado ao desenvolvimento de 
aplicações Angular 2 utilizando as facilidades oferecidas pela ferramenta Webpack

Um aspecto importante no desenvolvimento de aplicações Angular 2 e pouco explorado nos tutoriais e treinamentos no
framework está relacionado a construção e configuração do ambiente para a execução da aplicação. Em geral os 
projetos são iniciados a partir de download das configurações básicas contendo as dependências necessárias
a execução da aplicação. Nesta sessão pretende-se oferecer ao leitor uma compreensão sobre a arquitetura
utilizada pelo framework buscando oferecer conhecimentos importantes para a construção de produtos de 
melhor qualidade além de favorecer o entendimento dos mecanismos utilizados.


Criar uma pasta para o projeto contendo o subdiretorio src/app

```bash
mkdir -p exemplo/src/app
cd exemplo
```

Criar o arquivo de configuração da aplicação com o nome package.json, que fornece as informações de 
dependências do projeto além de scripts de execução do npm, com o comando 


```bash
npm init -f
```

Inserir as dependências do Angular no arquivo e instala-las

```bash
npm install @angular/common @angular/compiler @angular/core @angular/forms @angular/http @angular/platform-browser @angular/platform-browser-dynamic @angular/router core-js rxjs zone.js --save
```

Para instalar o transpilador, ferramenta que faz a conversão de uma linguagem para outra, da linguagem Typescript para Javascript
é necessário instalar a ferramenta typescript na sessão que mantem as informações das dependências para o ambiente de
desenvolvimento no arquivo package.json com o comando

```bash
npm install typescript typings --save-dev
```

Para garantir a validação de tipos em módulos desenvolvidos em Javascript é necessário o uso de uma ferramenta para
evitar problemas com o compilador typescript, para isso a demanda do uso da ferramenta typings.

Para o uso no ambiente de desenvolvimento é recomendada a adoção de um servidor http capaz de processar os programas em
typescript sem a necessidade de uma nova compilação a cada alteração. O servidor Webpack faz este papel de servidor
neste estudo.

```bash
npm install webpack webpack-dev-server --save-dev
```

Para o correto funcionamento do Webpack é necessário que sejam instalados alguns complementos que fazem o processamento
de arquivos utilizados em aplicações web 

```bash
npm install angular2-template-loader awesome-typescript-loader css-loader file-loader html-loader null-loader raw-loader style-loader to-string-loader --save-dev

Existem também plugins para o Webpack que fornecem funcionalidades que simplificam o processo de execução de aplicações
web em ambinete de desenvolvimento que podem ser instalados com o comando

```bash
npm install html-webpack-plugin webpack-merge extract-text-webpack-plugin --save-dev

Apenas para facilitar o processo de exclusão de arquivos também será instaldo o módulo rimraf

```bash
npm install rimraf --save-dev


Para uma organização padronizada dos arquivos de projetos Angular 2, este trabalho propõe a criação da seguinte estrutura de pastas
- src
-- app
--- assets
---- css
---- images

Para o uso da linguagem Typescript em conjunto com Javascript é necessário realizar algumas configurações que agregam 
funcionalidades a ferramenta de transpilação. Uma maneira de realizar esta configuração é por meio dos seguintes comandos

```bash
typings install dt~core-js --save --global
typings install dt~node --save --global
```

Após suas execuções será criado o arquivo typings.json na pasta onde os comandos foram executados.

Para instalar os pacotes execute o comando 

```bash
typings install
```


### Configurando o Webpack

A execução da aplicação em ambiente de desenvolvimento depende de configurações do servidor Webpack. O arquivo
vendor.ts é utilizado para para importar os arquivos das dependências para a aplicação. Este arquivo deve
ser criado na pasta src com um conteúdo semelhante ao seguinte:

```javascript
import '@angular/platform-browser';
import '@angular/platform-browser-dynamic';
import '@angular/core';
import '@angular/common';
import '@angular/http';
import '@angular/router';
import 'rxjs';
```


Para garantir compatibilidade das funcionalidades HTML5 em navegadores que não possuam a implementação de alguns marcadores
ou estas estejam fora dos padrões é recomendado o uso de código Polyfill. Crie o arquivo polyfills.ts na pasta src com o 
seguinte conteúdo

```javascript
import 'core-js/es6';
import 'core-js/es7/reflect';
require('zone.js/dist/zone');
if (process.env.ENV === 'production') {
    // Production
} else {
   // Development
   Error['stackTraceLimit'] = Infinity;
   require('zone.js/dist/long-stack-trace-zone');
}
```


Para as configurações flexiveis do Webpack crie na pasta raiz o arquivo webpack.config.js, com o seguinte conteúdo

```javascript
module.exports = require('./config/webpack.dev.js');
```

em seguida deve ser criado o diretório config onde serão mantidos os arquivos webpack.dev.js e webpack.common.js que 
definirão o comportamento do servidor http.

Em webpack.common.js 

```javascript
var webpack = require('webpack');
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require('extract-text-webpack-plugin');

const path = require('path');
const rootDir = path.resolve(__dirname, '..');

module.exports = {
entry: {
    'polyfills': './src/polyfills.ts',
    'vendor': './src/vendor.ts',
    'app': './src/main.ts'
},

resolve: {
    extensions: ['.js', '.ts', '.json']
},

module: {
    loaders: [
        {
            test: /\.ts$/,
            loaders: ['awesome-typescript-loader', 'angular2-template-loader']
        },
        {
            test: /\.html$/,
            loader: 'html-loader'
        },
        {
            test: /\.(png|jpe?g|gif|svg|woff|woff2|ttf|eot|ico)$/,
            loader: 'file?name=assets/[name].[hash].[ext]'
        },
        {
           test: /\.css$/,
           loaders: ['to-string-loader', 'css-loader']
        }
    ]},

    plugins: [
        new webpack.optimize.CommonsChunkPlugin({
            name: ['app', 'vendor', 'polyfills']
        }),
        new HtmlWebpackPlugin({
            template: 'src/index.html'
        })
    ]
};
```


Em webpack.dev.js:

```javascript
var webpackMerge = require('webpack-merge');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var commonConfig = require('./webpack.common.js');

const path = require('path');
const rootDir = path.resolve(__dirname, '..');

module.exports = webpackMerge(commonConfig, {

devtool: 'cheap-module-eval-source-map',

output: {
    path: path.resolve(rootDir, 'dist'),
    publicPath: 'http://localhost:8080/',
    filename: '[name].js',
        chunkFilename: '[id].chunk.js'
    },

    plugins: [
        new ExtractTextPlugin('[name].css')
    ],

    devServer: {
        historyApiFallback: true,
        stats: 'minimal'
    }
});
```



## Construindo uma aplicação básica

Crie o arquivo main.ts na pasta src

```javascript
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { AppModule } from './app/app.module';
platformBrowserDynamic().bootstrapModule(AppModule);
```


Este arquivo faz referência ao app.module.ts que deve ser criado na pasta app 

```javascript
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
@NgModule({
    imports: [
        BrowserModule
    ],
    declarations: [
        AppComponent
    ],
    bootstrap: [ AppComponent ]
})
export class AppModule { }
```


O aquivo que contém as funcionalidades do componente, app.component.ts também fica na pasta app

```javascript
import { Component } from '@angular/core';
import '../assets/css/styles.css';
@Component({
    selector: 'my-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})

export class AppComponent { }
```



Nele são apresentadas as dependencias de um arquivo html e outro css que contém o seguinte
conteúdo

Em app.component.html

```html
<main>
    <h1>Hello from Angular Application with Webpack</h1>
</main>
```

e app.component.css

```css
main {
    padding: 1em;
    font-family: Arial, Helvetica, sans-serif;
    text-align: center;
    margin-top: 50px;
    display: block;
}
```


Finalmente o arquivo html de inicio da aplicação index.html que deve ser criado na pasta src

```html
<!DOCTYPE html>
<html>
<head>
    <base href="/">
    <title>Angular With Webpack</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
    <my-app>Loading...</my-app>
</body>
</html>
```



```javascript
Insira os scripts para a execução do servidor no arquivo package.json

"scripts": {
    "start": "webpack-dev-server --inline --progress --port 8080",
    "postinstall": "typings install"
},
```


Para a traspilação é necessário realizar a configuração de alguns parâmetros que pode ser feito por meio da
criação e edição do arquivo tsconfig.json com o seguinte conteúdo

```javascript
{
    "compilerOptions": {
        "target": "es5",
        "module": "commonjs",
        "moduleResolution": "node",
        "sourceMap": true,
        "emitDecoratorMetadata": true,
        "experimentalDecorators": true,
        "removeComments": false,
        "noImplicitAny": true,
        "suppressImplicitAnyIndexErrors": true
    }
}
```

Para testar a aplicação use o comando 

```bash
npm start
```

No endereço local da sua máquina na porta 8080, http://localhost:8080, estará disponível a aplicação criada.

## Adicionando vários módulos em aplicações Angular 2

## Trabalhando com formulários

## Conceitos de webservice

## Integrando aplicação Angular 2 com webservices

## Sobre o framework Demoiselle

## Criando um webservice com Demoiselle 3

## Demoiselle 3 e persistência

## Implementando os módulos http e security do Demoiselle 3

## O gerador de código (Angular 2 e Java)  Demoiselle 3


## Referências

https://www.tektutorialshub.com/angular-2-with-webpack-tutorial/

