/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1928.framework.servlet;

import annotation.AnnotationUrl;
import etu1928.framework.Mapping;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Tim
 */
public class FrontServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    HashMap<String, Mapping> MappingUrls;
    
    public void setMappingUrls(HashMap<String, Mapping> MappingUrls){
        this.MappingUrls = MappingUrls;
    }

    public HashMap<String, Mapping> getMappingUrls() {
        return MappingUrls;
    }
    
    public ArrayList<String> findClasses(File directory, String packageName) throws ClassNotFoundException {
        ArrayList<String> classes = new ArrayList<String>();
        if (!directory.exists()){
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files){
            if (file.isDirectory()){
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            }else if (file.getName().endsWith(".class")){
                classes.add(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
            }
        }
        System.out.println("classes : "+classes);
        return classes;
    }
    
        public ArrayList<String> getClasses(String packageName) throws ClassNotFoundException, IOException, URISyntaxException{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        System.out.println(path);
        Enumeration<URL> resources = classLoader.getResources(path);
        ArrayList<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()){
            URL resource = resources.nextElement();
            URI uri = new URI(resource.toString());
            dirs.add(new File(uri.getPath()));
        }
        ArrayList<String> classes = new ArrayList<String>();
        for (File directory : dirs){
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }
        
    @Override
    public void init() throws ServletException{
        HashMap<String,Mapping> temp = new HashMap<String,Mapping>();
        try{
            System.out.println("modelPackage = " + getInitParameter("modelPackage"));
            ArrayList<String> list = getClasses(getInitParameter("modelPackage").trim());
            for(String element : list){
               Method[] methods = Class.forName(element).getDeclaredMethods();
               for(Method m : methods){
                   if(m.isAnnotationPresent(AnnotationUrl.class)){
                       AnnotationUrl annotation = m.getAnnotation(AnnotationUrl.class);
                       temp.put(annotation.url(),new Mapping(element ,m.getName()));
                   }
               }
            }
            this.setMappingUrls(temp);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (URISyntaxException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (Exception e){
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.print("URI = "+request.getRequestURI()+"<br>");
            out.print("URL = "+request.getRequestURL()+"<br>");
            if (request.getQueryString().split("&").length>0) {
                for(int i = 0; i < request.getQueryString().split("[&]").length; i++){
                   out.print(request.getQueryString().split("&")[i]+"<br>");
                }   
            }
            out.println(this.getMappingUrls());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
