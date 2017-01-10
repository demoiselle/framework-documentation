# Scripts - DynamicManager

---

Responsible for Managing Scripts, its compilation and execution.

The implementation of this component is for the use of the facilities provided by the Java Scripting AP

I to build dynamic and scripting languages ​​in which implement compatible engine with JSR-223.

Using Compiled and Invocable interfaces JSR-223 \(implemented in engines\) the manager can access the generically methods to compile the code and save the bytecode cache.

The script allows execution is passed a context with objects that will be accessible to the script code can be manipulated / altered by the same and made available via the same context for the calling code.

* To add a specific engine add the reference to the jar in the pom.xml  in your aplication.  The discover mechanism locates and register the engine to use. 



---

Example of use:

```java
//in the pom.xml :
<dependency>
     <groupId>org.codehaus.groovy</groupId>
     <artifactId>groovy-all</artifactId>
     <version>2.4.7</version>
</dependency>

//in the source code:
@Inject DynamicManager dm;

...
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



