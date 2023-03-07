package etu1928.framework;

public class Mapping {
    String className;
    String Method;

    public Mapping(){}

    public void setClassName(String className) {
        this.className=className;   
    }
    
    public void setMethod(String method) {
        this.method=method;   
    }

    public String getClassName() {
        return this.className;   
    }
    
    public String getMethod() {
        return this.method;   
    }
}
