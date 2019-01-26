package urjc.isi.pruebasSparkJava;

import static spark.Spark.*;
import spark.Request;
import spark.Response;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.servlet.MultipartConfigElement;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class GraphFuncionality {
	
	/**
     * Calcula la distancia entre actores o pelicuas.
     * @param graph sobre el que calcular la distancia
     * @param name1 para buscar y comparar
     * @param name2 para buscar y comparar
     * @return String con la distancia y la ruta, u otro string en caso de error o 'no ruta'.
     */
    public static String doDistance(Graph graph, String name1, String name2) {
    	if (graph.V() == 0) throw new NullPointerException("Main.doDistance");
    	
    	String result = new String("");
    	ArrayList<String> allNames = new ArrayList<String>();
    	String noMatch = "<p>Ninguna coincidencia. Error al introducir nombre," +
    					"o no existe en nuestra BD</p><br>";
    	try {
	    	if (name1.equals("") || name2.equals("")){ //caso de no introducir nada
	    		throw new IllegalArgumentException("Main.doDistance");
	    	}else if (!graph.hasVertex(name1) && !graph.hasVertex(name2)){ //caso 2 erroneos
	    		allNames = nameChecker(Main.getConnection(),name1);
	    		result = "<p>Campo 1: </p>";
	    		if (!allNames.isEmpty()) { 
	    			result += "<p>Múltiples coincidencias. Copia nombre exacto para cálculo de distancia:</p><ul>"; 
	    			for(String aux: allNames) {
	    				result += "<li>" + aux + "</li>"; 
	    			}
	    			result += "</ul>";
	    		}else {
	    			result += noMatch;
	    		}
	    		
	    		allNames.clear(); //limpiamos por si acaso
	    		allNames = nameChecker(Main.getConnection(),name2);
	    		result += "<p>Campo 2: </p>"; 
	    		if (!allNames.isEmpty()) {
	    			result += "<p>Múltiples coincidencias. Copia nombre exacto para cálculo de distancia:</p><ul>"; 
	    			for(String aux: allNames) {
	    				result += "<li>" + aux + "</li>"; 
	    			}
	    			result += "</ul>";
	    		}else {
	    			result += noMatch;
	    		}
	    		
	    	}else if (!graph.hasVertex(name1)){ //no coincidencia nombre1
	    		allNames = nameChecker(Main.getConnection(),name1); //Control de nombres
	    		if (!allNames.isEmpty()) {
	    			result = "<p>Campo 1: </p>" + 
	    					"<p>Múltiples coincidencias. Copia nombre exacto para cálculo de distancia:</p><ul>"; 
	    			for(String aux: allNames) {
	    				result += "<li>" + aux + "</li>"; 
	    			}
	    			result += "</ul>";
	    		}else { //no coincidencia
	    			result += noMatch;
	    		}
	    	}else if (!graph.hasVertex(name2)){ //no coincidencia nombre2
	    		allNames = nameChecker(Main.getConnection(),name2);
	    		if (!allNames.isEmpty()) {
	    			result = "<p>Campo 2: </p>" + 
	    					"<p>Múltiples coincidencias. Copia nombre exacto para cálculo de distancia:</p><ul>"; 
	    			for(String aux: allNames) {
	    				result += "<li>" + aux + "</li>"; 
	    			}
	    			result += "</ul>";
	    		}else {
	    			result += noMatch;
	    		}
			}else{
				PathFinder pf = new PathFinder(graph, name1);
				if (pf.hasPathTo(name2)) { //si tenemos ruta, procedemos	
					String edge = " --> ";
					for (String v : pf.pathTo(name2)) {
						result += v + edge;
					}       
					result = result.substring(0, result.length() - edge.length());
					result += "<br><br>Distancia = " + pf.distanceTo(name2);
				} else {
					result = "<p>Ninguna ruta disponible entre " + name1 + " y " + name2 + ".</p>";
				}
			}
    	}catch(IllegalArgumentException e) {
    		result ="<p>ERROR. Ver 'uso'. Por favor, inténtalo de nuevo.</p>";
    	}
		return result;
	}
    
    /**
     * Comprueba si se han introducido nombres incorrectos/incompletos 
     * (p.e. 'Tom', en vez de 'Tom Cruise', o no coincidente como 'abcd').
     * @param name para buscar y comparar.
     * @return ArrayList con nombres coincidentes con parte de los strings dados 
     * (p.e. devuelve todos los 'Tom' de la tabla, en el caso de haber introducido 'Tom'
     * en vez de 'Tom Cruise'). Devuelve ArrayList vacío en caso de no coincidencia.
     */
    public static ArrayList<String> nameChecker(Connection conn, String name){
    	String sql = "SELECT title FROM movies " + //consulta para nombre_pelis
    			"WHERE title LIKE ?";
    	String sql2 = "SELECT primaryName FROM workers " + //consulta para nombre_actores
    			"WHERE primaryName LIKE ?";
    	
    	ArrayList<String> result = new ArrayList<String>();
    	try {
	    	PreparedStatement pstmt = conn.prepareStatement(sql);
	    	pstmt.setString(1, "%" + name + "%");
	    	PreparedStatement pstmt2 = conn.prepareStatement(sql2);
			pstmt2.setString(1, "%" + name + "%");
			
	    	ResultSet rs = pstmt.executeQuery();
	    	if(rs.next()) { //pelis
	    		do{
	    			result.add(rs.getString(1));
	    		}while(rs.next());
	    	}
	    			
	    	ResultSet rs2 = pstmt2.executeQuery(); //ejecutamos aqui la segunda query, para no matar la primera
	    	if (rs2.next()) { //actores
	        	do{
	        		result.add(rs2.getString(1));
	        	}while(rs2.next()); 
	    	}
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}	
    	return result;
    }
    
    
    /**
     * Devuelve los vecinos dado un name (nodo). Uso de IndexGraph.java.
     * @param graph sobre el que hallar vecinos
     * @param name sobre el que hallar vecinos
     * @return String con sus vecinos, u otro string en caso de error o 'no vecinos'.
     */
    public static String doGraphFilter(Graph graph, String name) {
    	if (graph.V() == 0) throw new NullPointerException("GraphFuncionality.doDistance");
    	
    	String result = new String("");
    	ArrayList<String> allNames = new ArrayList<String>();
    	String noMatch = "<p>Ninguna coincidencia. Error al introducir nombre," +
    					"o no existe en nuestra BD</p><br>";
    	try {
	    	if (name.equals("")){ //caso de no introducir nada
	    		throw new IllegalArgumentException("GraphFuncionality.doDistance");
	    	}else if (!graph.hasVertex(name)){ //no coincidencia nombre
	    		allNames = nameChecker(Main.getConnection(),name);
	    		if (!allNames.isEmpty()) {
	    			result =  "<p>Múltiples coincidencias. Copia nombre exacto en el formulario:</p><ul>"; 
	    			for(String aux: allNames) {
	    				result += "<li>" + aux + "</li>"; 
	    			}
	    			result += "</ul>";
	    		}else { //no coincidencia
	    			result += noMatch;
	    		}
			}else{
		    	for (String w : graph.adjacentTo(name)){
		    		result += "<li>" + w + "</li>";
		        }
			}
    	}catch(IllegalArgumentException e) {
    		result ="<p>ERROR. Ver 'uso'. Por favor, inténtalo de nuevo.</p>";
    	}
		return result;
    }
    
    /**
     * Dado el nombre de una película, nos proporciona películas similares, utilizando
     * la distancia entre nodos del grafo.
     * @param graph sobre el que calcular la distancia
     * @param name para buscar y comparar
     * @return ArrayList con las películas más cercanas (distancia 2 de name).
     */
    public static ArrayList<String> similarTo(Graph graph, String name) {
    	ArrayList<String> result = new ArrayList<String>();
    	return result;
    }
    
    /**
     * Ranking de los actores/actrices con más aparaciones en películas.
     * @param graph sobre el que calcular la distancia
     * @param number actores con x películas o más
     * @return Set con las películas más cercanas (distancia 2 de name).
     */
    public static Iterable<String> doRanking(Graph graph, String number) {
    	ST<String, Integer> result = new ST<String, Integer>();
    	String text = "";
    	
    	try {
    		if (number.equals("")) {
    			throw new IllegalArgumentException("GraphFuncionality.doRanking");
    		}else {
    			int numberAux = Integer.parseInt(number);
//    			System.out.println(numberAux);
    			for (String v : graph.vertices()) {
                    if (graph.type(v) == 0) { //actores
                    	if(graph.degree(v) > numberAux) result.put(v, graph.degree(v));
                    }
                }
                text = "<p>RANKING:</p>";
    		}
            
    	}catch(IllegalArgumentException e) {
    		text ="<p>ERROR. Debe introducir un número en el form. Por favor, inténtalo de nuevo.</p>";
    	}
    	return result.keys();
    }
}
