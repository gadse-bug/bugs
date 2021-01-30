package javascriptInterpreter.visitors;

import java.util.HashMap;

public class Context {
    private HashMap<String, JavascriptType> scope;

    public Context(){
        scope = new HashMap<>();
    }

    public JavascriptType getValue(String name) {
        return scope.get(name);
    }

    public void addIdentifier(String name, JavascriptType value){
        scope.put(name, value);
    }

    public void assignIdentifier(String name, JavascriptType value){
        if ( scope.containsKey(name) ) {
            scope.put(name,value);
        }
    }
}
