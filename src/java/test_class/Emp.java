package test_class;
        
import annotation.AnnotationUrl;

public class Emp{
    int id = 2;
    
    public Emp(){}
    
    @AnnotationUrl(url = "dept-all")
    public int getId() {
        return id;
    }
    
}