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
recursos computacionais em ambientes distribuidos além do oferecer funcionalidades que implementam maior segurança,
integração e interoperabilidade.


## Configurando o ambiente

Um aspecto importante no desenvolvimento de aplicações Angular 2 e pouco explorado nos tutoriais e treinamentos no
framework está relacionado a construção e configuração do ambiente para a execução da aplicação. Em geral os 
projetos são iniciados a partir de download das configurações básicas contendo as dependências necessárias
a execução da aplicação. Nesta sessão prentende-se oferecer ao leitor uma compreenção sobre a arquitetura
utilizada pelo framework buscando oferecer conhecimentos importantes para a construção de produtos de 
melhor qualidade além de favorecer o entendimento dos mecanismos utilizados.


Criar uma pasta para o projeto contendo o subdiretorio src/app

```bash
mkdir -p exemplo/src/app
cd exemplo
```

Criar o arquivo de configuração da aplicação com o nome **package.json**, que fornece as informações de 
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

Para garantir a validação de tipos em modulos desenvolvidos em Javascript é necessário o uso de uma ferramenta para
evitar problemas com o compilador typescript, para isso e necessário o uso da ferramenta typings.

Para o uso no ambiente de desenvolvimento é recomendada a adoção de um servidor http capaz de processar os programas em
typescript sem a necessidade de uma nova compilação a cada alteração. O servidor Webpack faz este papel de servidor
neste estudo.

```bash
npm install webpack webpack-dev-server --save-dev
```

Para o correto funcionamento do Webpack é necesário que sejam instalados alguns complementos que fazem o processamento
de arquivos utilizados em eplicações web 

```bash
npm install angular2-template-loader awesome-typescript-loader css-loader file-loader html-loader null-loader raw-loader style-loader to-string-loader --save-dev
```

Existem também plugins para o Webpack que fornecem funcionalidades que simplificam o processo de execução de aplicações
web em ambinete de desenvolvimento que podem ser instalados com o comando

```bash
npm install html-webpack-plugin webpack-merge extract-text-webpack-plugin --save-dev
```

Apenas para facilitar o processo de exclusão de arquivos também será instaldo o módulo rimraf

```bash
npm install rimraf --save-dev
```


Para uma organização padronizada dos arquivos de projetos Angular 2, este trabalho propõe a criação da seguinte estrutura de pastas
- src
   - app
      - assets
         - css
         - images

Para o uso da linguagem Typescript em conjunto com Javascript é necessário realizar algumas configurações que agregam 
funcionalidades a ferramenta de transpilação. Uma maneira de realizar esta configuração é por meio dos seguintes comandos

```bash
typings install dt~core-js --save --global
typings install dt~node --save --global
```

Após suas execuções será criado o arquivo **typings.json** na pasta onde os comandos foram executados.

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
ou estas estejam fora dos padrões é recomendado o uso de código Polyfill. Crie o arquivo **polyfills.ts** na pasta src com o 
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


Para as configurações flexiveis do Webpack crie na pasta raiz o arquivo **webpack.config.js**, com o seguinte conteúdo

```javascript
module.exports = require('./config/webpack.dev.js');
```

em seguida deve ser criado o diretório config onde serão mantidos os arquivos **webpack.dev.js** e webpack.common.js que 
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


Em **webpack.dev.js**:

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

Crie o arquivo **main.ts** na pasta src

```javascript
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { AppModule } from './app/app.module';
platformBrowserDynamic().bootstrapModule(AppModule);
```


Este arquivo faz referência ao **app.module.ts** que deve ser criado na pasta app 

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


O aquivo que contém as funcionalidades do componente, **app.component.ts** também fica na pasta app

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

Em **app.component.html**:

```html
<main>
    <h1>Hello from Angular Application with Webpack</h1>
</main>
```

e **app.component.css**:

```css
main {
    padding: 1em;
    font-family: Arial, Helvetica, sans-serif;
    text-align: center;
    margin-top: 50px;
    display: block;
}
```


Finalmente o arquivo html de inicio da aplicação **index.html** que deve ser criado na pasta src

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
Insira os scripts para a execução do servidor no arquivo **package.json**

"scripts": {
    "start": "webpack-dev-server --inline --progress --port 8080",
    "postinstall": "typings install"
},
```


Para a traspilação é necessário realizar a configuração de alguns parâmetros que pode ser feito por meio da
criação e edição do arquivo **tsconfig.json** com o seguinte conteúdo

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

No endereço local da sua máquina na porta 8080, *http://localhost:8080*, estará disponível a aplicação criada.


## Testando


## Gerando os arquivos de produção


## Referências

https://www.tektutorialshub.com/angular-2-with-webpack-tutorial/

