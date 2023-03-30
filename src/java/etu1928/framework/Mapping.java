/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1928.framework;

/**
 *
 * @author Tim
 */
public class Mapping {
    String className;
    String method;

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
