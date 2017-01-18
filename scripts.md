# Scripts - DynamicManager



Responsável pela Gestão de Scripts dinâmicos, sua compilação e execução. Este  componente  torna disponivel as facilidades fornecidas pelo Java Scripting API para rodar engines de scripts que implementam interfaces compatíveis com a JSR-223.

Usando as  interfaces compilable e invocable da ​​JSR-223 \(que necessitam ser implementadas pelo engine\) o manager pode acessar os métodos genéricos de cada engine para compilar o código e salvar no cache o bytecode respectivo do script.

O script permite na sua execução ,  passar um contexto de variavéis \(com objetos que serão acessíveis pelo  código do script\) sendo que este pode ser manipulado /alterado e disponibilizado pelo script e retornado para o código java de chamada.

* O motor 'nashorn' que é um engine javascript já vem embutido nas jvm mais novas por padrão. Porém para adicionar um engine de uma outra linguagem específica, adicione a referência ao jar no pom.xml na sua aplicação. O mecanismo de descoberta localiza e registra o motor a ser utilizado.


Exemplo de uso:

Adicione ao pom.xml

```xml
<dependency>
     <groupId>org.codehaus.groovy</groupId>
     <artifactId>groovy-all</artifactId>
     <version>2.4.7</version>
</dependency>
```

E dentro do codigo java

```java
@Inject DynamicManager dm;

try {                                  
  String scriptSource = "int a = X; X= a + a;"; //sourcecode to be compiled .  
  String scriptName = "test.groovy"; //id to scriptCache
  Integer valueX    = 1; //value to be passed to script

  dm.loadScript("groovy","test.groovy", scriptSource); //load the script into dynamicManager cache.   

  Bindings context = new SimpleBindings(); //create the context to script where 'X' is a key in script to a dynamic variable.                                                                  
  context.put("X", valueX ); //The value can be a class too.    

  System.out.println("The value of X is " + valueX + " before script execution.");
  System.out.println("Running script...");
  dm.eval ("groovy,"test.groovy",context); //run the script.        
  valueX = (Integer) context.get("X");                  
  System.out.println("The value of X is " + valueX + " after script execution.");

} catch (ScriptException e) {
  e.printStackTrace();
}
```



