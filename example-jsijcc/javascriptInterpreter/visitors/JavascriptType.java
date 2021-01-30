package javascriptInterpreter.visitors;


import javascriptInterpreter.tree.Node;

import java.util.ArrayList;
import java.util.HashMap;

import static javascriptInterpreter.visitors.JSToJavaUtils.*;

public class JavascriptType {
    public enum Type {
        UNDEFINED(0),
        NULL(1),
        BOOLEAN(2),
        STRING(3),
        NUMBER(4),
        OBJECT(5),
        FUNCTION(6);

        int id;
        Type(int id){
            this.id = id;
        }
    }

    private Type type;

    private Object value;

    private String identifierName = null;


    public JavascriptType(){
        this.type = Type.UNDEFINED;
        this.value = null;
    }

    public JavascriptType(int value){
        this((double)value);
    }

    public JavascriptType(double value){
        this.type = Type.NUMBER;
        this.value = value;
    }

    public JavascriptType(boolean value){
        this.type = Type.BOOLEAN;
        this.value = booleanToDouble(value);
    }

    public JavascriptType(Type type, String value){
        this.type = type;
        this.value = parseValue(type, value);
    }

    public JavascriptType(ArrayList<JavascriptType> arrayLiteral){
        this.type = Type.OBJECT;

        //create a javascript object
        JavascriptObject object = new JavascriptObject();

        //add the indices as properties
        for(int i = 0; i < arrayLiteral.size(); i++){
            object.addProperty(String.valueOf(i), arrayLiteral.get(i));
        }

        //add length as property for array
        object.addProperty("length", new JavascriptType(arrayLiteral.size()));

        this.value = object;
    }

    public JavascriptType(HashMap<String, JavascriptType> objectLiteral){
        this.type = Type.OBJECT;

        JavascriptObject object = new JavascriptObject(objectLiteral);

        this.value = object;

//        System.out.println("what the fuck");
        object.printObject();
    }

    public JavascriptType(Node functionRoot){
        this.type = Type.FUNCTION;
        this.value = functionRoot;
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public String getIdentifierName(){return identifierName;}

    public static class JavascriptObject {
        private HashMap<String, JavascriptType> properties;

        public JavascriptObject(){
            properties = new HashMap<>();
        }

        public JavascriptObject(HashMap<String, JavascriptType> properties) {
            this.properties = properties;
        }

        public void addProperty(String name, JavascriptType value){
            properties.put(name, value);
        }

        public void assignProperty(String name, JavascriptType value){
            if ( properties.containsKey(name) ) {
                properties.put(name,value);
            }
        }

        public JavascriptType getProperty(String name){
            return properties.get(name);
        }

        @Override
        public String toString(){
            String s = "";
            for(JavascriptType obj : properties.values()){
                s +=  obj.value + " ,";
            }
            return s;
        }

        public void printObject(){
            for(String key : properties.keySet()){
                System.out.println(key + " : " + properties.get(key));
            }
        }
    }


    private Object parseValue(Type type, String s){
        switch(type){
            case NULL : return 0.0;
            case STRING : return s;
        }
        return null;
    }

    public String getString(){
        return (String)value;
    }

    public int getInt(){
        return (int)(double)value;
    }

    public double getDouble(){
        return (double)value;
    }

    public boolean getBoolean(){
        return doubleToBoolean((double)value);
    }

    public JavascriptObject getJavascriptObject(){
        return (JavascriptObject)value;
    }

    public void setIdentifierName(String name){
        this.identifierName = name;
    }


}
