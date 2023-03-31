package test_class;
        
import annotation.AnnotationUrl;

public class Dept {
    int id = 2;
    
    public Dept(){}
    
    @AnnotationUrl(url = "dept-all")
    public int getId() {
        return id;
    }
    
}
