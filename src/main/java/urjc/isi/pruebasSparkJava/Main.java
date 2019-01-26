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

public class Main {
	
    // Connection to the SQLite database. Used by insert and select methods.
    // Initialized in main
    private static Connection connection;
	
    static int getHerokuAssignedPort() {
    	ProcessBuilder processBuilder = new ProcessBuilder();
    	if (processBuilder.environment().get("PORT") != null) {
    		return Integer.parseInt(processBuilder.environment().get("PORT"));
    	}
    	return 4707; //return default port if heroku-port isn't set (i.e. on localhost)
    }
    
    // Used to illustrate how to route requests to methods instead of
    // using lambda expressions
    public static String doSelect(Request request, Response response) {
    	return select (connection, request.params(":table"), request.params(":film"));
    }
    
    public static String select(Connection conn, String table, String film) {
    	String sql = "SELECT * FROM " + table + " WHERE film=?";
    	String result = new String();
	
    	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    		pstmt.setString(1, film);
    		ResultSet rs = pstmt.executeQuery();
    		// Commit after query is executed
    		connection.commit();

    		while (rs.next()) {
    			// read the result set
    			result += "film = " + rs.getString("film") + "\n";
    			System.out.println("film = "+rs.getString("film") + "\n");

    			result += "actor = " + rs.getString("actor") + "\n";
    			System.out.println("actor = "+rs.getString("actor")+"\n");
    		}
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    	return result;
    }
    
    public static String selectTitle_ID(Connection conn, String table, String data1, String data2, String data3) {
		String sql="";
    	String result = null;
    		sql = "SELECT * FROM " + table + " WHERE title=? AND year=? AND genres=?";
    	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    		pstmt.setString(1, data1);
    		pstmt.setString(2, data2);
    		pstmt.setString(2, data3);
    		ResultSet rs = pstmt.executeQuery();
    		while (rs.next()) {
    		    // read the result set
    		    result = rs.getString("titleID");
    		}
    		return result;
    	} catch (SQLException e) {
    	    System.out.println(e.getMessage());
    	    return null;
    	}
}
    public static String selectName_ID(Connection conn, String table, String data1) {
		String sql="";
    	String result = null;
    		sql = "SELECT * FROM " + table + " WHERE primaryName=?";
    	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    		pstmt.setString(1, data1);
    		ResultSet rs = pstmt.executeQuery();
    		while (rs.next()) {
    		    // read the result set
    		    result = rs.getString("nameID");
    		}
    		return result;
    	} catch (SQLException e) {
    	    System.out.println(e.getMessage());
    	    return null;
    	}
}
    
    public static void insert(Connection conn, String film, String actor) {
    	String sql = "INSERT INTO films(film, actor) VALUES(?,?)";

    	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    		pstmt.setString(1, film);
    		pstmt.setString(2, actor);
    		pstmt.executeUpdate();
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    }
    
    public static void insertFilm(Connection conn, String data1, String data2, String data3){
    	String sql="";
		//Comprobar que todos los elementos son distintos que null
    	if(data1 == null){
    		throw new NullPointerException();
    	}
    		sql = "INSERT INTO movies (title, year, genres) VALUES(?,?,?)";

    	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    		pstmt.setString(1, data1);
    		pstmt.setString(2, data2);
    		pstmt.setString(3, data3);
    		pstmt.executeUpdate();
    	} catch (SQLException e) {
    	    System.out.println(e.getMessage());
    	}
}

    public static void insertActor(Connection conn, String data1){
    	String sql="";
		//Comprobar que todos los elementos son distintos que null
    	if(data1 == null){
    		throw new NullPointerException();
    	}
    		sql = "INSERT INTO workers (primaryName) VALUES(?)";

    	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    		pstmt.setString(1, data1);
    		pstmt.executeUpdate();
    	} catch (SQLException e) {
    	    System.out.println(e.getMessage());
    	}
}
    public static void insertWorks_In(Connection conn, String data1, String data2){
    	String sql="";
		//Comprobar que todos los elementos son distintos que null
    	if(data1 == null || data2 == null){
    		throw new NullPointerException();
    	}
    		sql = "INSERT INTO works_in (titleID, nameID) VALUES(?,?)";

    	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    		pstmt.setString(1, data1);
    		pstmt.setString(2, data2);
    		pstmt.executeUpdate();
    	} catch (SQLException e) {
    	    System.out.println(e.getMessage());
    	}
}
    
    public static String infoPost(Request request, Response response) throws 
    		ClassNotFoundException, URISyntaxException {
    	String result = new String("TODA LA INFORMACIÓN QUE QUIERAS SOBRE PELÍCULAS"
        							+ " A TRAVÉS DE UN POST");
    	return result;
    }

    public static String infoGet(Request request, Response response) throws
    		ClassNotFoundException, URISyntaxException {
    	String result = new String("TODA LA INFORMACIÓN QUE QUIERAS SOBRE PELÍCULAS"
        							+ " A TRAVÉS DE UN GET");
    	return result;
    }

    public static String doWork(Request request, Response response) throws
    		ClassNotFoundException, URISyntaxException {
    	String result = new String("Hello World");
    	return result;
    }
    
    
    public static Connection getConnection() {
    	return connection;
    }
    
//MAIN---
    public static void main(String[] args) throws ClassNotFoundException, SQLException, URISyntaxException {
    	// Establecemos el puerto del server con el método getHerokuAssignedPort()
    	port(getHerokuAssignedPort());

    	// Connect to SQLite sample.db database
    	// connection will be reused by every query in this simplistic example
    	//El constructor para acceder a la base de datos, en el futuro se debe descomentar. 
    	//Comentar para probar en local
//    	Injector connector = new Injector("IMDb.db");
    	connection = DriverManager.getConnection("jdbc:sqlite:Database/IMDb.db");
    	
    	Score score =new Score();
    	Comment comment =new Comment();

    	// SQLite default is to auto-commit (1 transaction / statement execution)
    	// Set it to false to improve performance

    	String home = "<html><body>" +
    		"<h1>Bienvenidos a la web de películas</h1>" +
    			"<form action='/info' method='post'>" +
    				"<div class='button'>" +
    					"Ir a info: <br/>" +
    					"<button type='submit'>Información</button>" +
    				"</div>" +
    			"</form></br>"+
    			"<form action='/hello' method='get'>" +
    				"<div class='button'>" +
    					"Ir a helloWorld: <br/>" +
    					"<button type='submit'>Hello</button>" +
    				"</div>" +
    			"</form>" +
    			"<form action='/upload_films' method='get'>" +
    				"<div class='button'>" +
    					"Subir fichero con películas: <br/>" +
    					"<button type='submit'>Upload Films</button>" +
    				"</div>" +
    			"</form>" +
    			"<form action='/addfilms' method='get'>" +
    				"<div class='button'>" +
    					"Añade película: <br/>" +
    					"<button type='submit'>Add Films</button>" +
    				"</div>" +
    			"</form>" +
    			"<form action='/showlastadded' method='get'>" +
					"<div class='button'>" +
						"Últimas películas añadidas: <br/>" +
						"<button type='submit'>Show Last Added</button>" +
					"</div>" +
				"</form>" +
    			"<a href='/filter'>Búsqueda de películas</a>" +
    			"<br><br>" +
    			"<p>Grafos:</p>" +
    			"<ul>" + 
					"<li><a href= '/distance'>Distancia entre actores y películas<a/></li>" +
					"<li><a href= '/graph_info'>Información sobre el grafo<a/></li>" +
					"<li><a href= '/graph_filter'>Uso de grafos para filtrado<a/></li>" +
				"</ul>" + 
    		"</body></html>";

        // spark server
        get("/", (req, res) -> home);
        get("/info", Main::infoGet);
        post("/info", Main::infoPost);
        get("/hello", Main::doWork);
        post("/score",(req, res)-> score.postScore(req));
        post("/comment",(req, res)-> comment.postComment(req));


        // In this case we use a Java 8 method reference to specify
        // the method to be called when a GET /:table/:film HTTP request
        // Main::doWork will return the result of the SQL select
        // query. It could've been programmed using a lambda
        // expression instead, as illustrated in the next sentence.
        get("/:table/:film", Main::doSelect);

        // In this case we use a Java 8 Lambda function to process the
        // GET /upload_films HTTP request, and we return a form
        get("/upload_films", (req, res) -> 
        	"<form action='/upload' method='post' enctype='multipart/form-data'>" 
        	+ "    <input type='file' name='uploaded_films_file' accept='.txt'>"
        	+ "    <button>Upload file</button>" + "</form>");
        // You must use the name "uploaded_films_file" in the call to
        // getPart to retrieve the uploaded file. See next call:

        // Retrieves the file uploaded through the /upload_films HTML form
        // Creates table and stores uploaded file in a two-columns table
        post("/upload", (req, res) -> {
        	req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
        	String result = "File uploaded!";
        	
        	try (InputStream input = req.raw().getPart("uploaded_films_file").getInputStream()) { 
        		// getPart needs to use the same name "uploaded_films_file" used in the form

        		// Prepare SQL to create table
        		Statement statement = connection.createStatement();
        		statement.setQueryTimeout(30); // set timeout to 30 sec.
        		statement.executeUpdate("drop table if exists films");
        		statement.executeUpdate("create table films (film string, actor string)");

        		// Read contents of input stream that holds the uploaded file
        		InputStreamReader isr = new InputStreamReader(input);
        		BufferedReader br = new BufferedReader(isr);
        		String s;
        		
        		while ((s = br.readLine()) != null) {
        			System.out.println(s);

        			// Tokenize the film name and then the actors, separated by "/"
        			StringTokenizer tokenizer = new StringTokenizer(s, "/");

        			// First token is the film name(year)
        			String film = tokenizer.nextToken();

        			// Now get actors and insert them
        			while (tokenizer.hasMoreTokens()) {
        				insert(connection, film, tokenizer.nextToken());
        			}
        			// Commit only once, after all the inserts are done
        			// If done after each statement performance degrades
        			connection.commit();
        		}
        		input.close();
        	}
        	return result;
        });

        get("/addfilms", (req, res) ->
    		"<div style='color:#1A318C'><b>PÁGINA PARA AÑADIR PELÍCULA A LA BASE DE DATOS:</b>"
    		+"<form action='/add_films' method='post'>" +
    		"<label for='film'>Película que desea añadir: </label>" + //required: campo obligatorio
    		"<input type='text' required name='film' id='film'" +
    		"pattern=[A-Za-z0-9 ].{1,}>" + //Excluimos todos lo que no sean letras y numeros, y no se puede dejar el campo vacio.
    		"<p></p>"+
    		"<form action='/add_films' method='post'>" +
    		"<label for='year'>Año: </label>" + 
    		"<input type='text' required name='year' id='year'" + //required: Campo obligatorio
    		"pattern=[0-9]{4}>" + //Solo se pueden introducir 4 cifras
    		"<p></p>"+
    		"<form action='/add_films' method='post'>" +
    		"<label for='genres'>Género: </label>" + 
    		"<input type='text' name='genres' id='genres'" +
    		"pattern=[A-Za-z]{0,}>" +
    		"<p></p>"+
    		"<form action='/add_films' method='post'>" +
    		"<label for='actor'>Actor: </label>" + 
    		"<input type='text' name='actor' id='actor'" +
    		"pattern=[A-Za-z]{0,}>" +
    		"<p><input type='submit' value='Enviar'></p>" +
    		"</form>"
    		+ "<p>Implementada funcionalidad a espera de solucionar problemas con upload films debido al límite de peliculas</p>");
        //Incluido formulario para añadir películas
        
        post("/add_films", (req, res) -> {
        	Film film = new Film();
        	String result = "Has añadido ->"
        		+ "</p>pelicula: " + req.queryParams("film")
        		+ "</p>year: " + req.queryParams("year") 
        		+ "</p>Género: " + req.queryParams("genres")
        		+ "</p>Actor: " + req.queryParams("actor");
        	Film.setTitle(req.queryParams("film"));
        	Film.setYear(req.queryParams("year"));
        	Film.setGenre(req.queryParams("genres"));
        	Film.setActor(req.queryParams("actor"));
        	
        	insertFilm(connection, req.queryParams("film")
        			,req.queryParams("year"), req.queryParams("genres"));
        	String title_ID = selectTitle_ID(connection, "movies", req.queryParams("film"), req.queryParams("year"), req.queryParams("genres"));
        	insertActor(connection, req.queryParams("actor"));
        	String name_ID = selectName_ID(connection, "workers", req.queryParams("actor"));
        	//insertWorks_In(connection, title_ID, name_ID);
        	return result;	
        });
        
        get("/showlastadded", (req, res) ->
        "<div style='color:#1A318C'><b>ÚLTIMAS PELÍCULAS AÑADIDAS:</b>");
        
        
        // Recurso /filter encargado de la funcionalidad del filtrado de películas.
        get("/filter", (req, res) -> Filter.showFilterMenu());
        
        // Recurso /filter_name encargado de mostrar la info de una película dado el nombre.
        post("/filter_name", (req, res) -> Filter.showFilmByName());
        
        // Recurso /filter_year encargado de mostrar todas las películas dado un año.
        post("/filter_year", (req, res) -> Filter.showFilmByYear(req));
        
        // Recurso /filter_actoractress encargado de mostrar todas las películas
        // en las que participa un actor o una actriz.
        post("/filter_actoractress", (req, res) -> Filter.showFilmByActorActress(req));

        // Recurso /filter_duration encargado de mostrar todas las películas con una 
        //duración menor a la dada
        post("/filter_duration", (req, res) -> Filter.showFilmByDuration(req));

        // Recurso /filter_genre encargado de mostrar todas las películas dado un genero.
        post("/filter_genre", (req, res) -> Filter.showFilmByGenre(req));

        // Recurso /filter_rating encargado de mostrar todas las películas dado un año.
        post("/filter_rating", (req, res) -> Filter.showFilmByRating(req));


        get("/distance", (req, res) -> {
        	String form = 
        		"<h3>Calculador de distancias mediante grafos</h3> " +
        		"<form action='/distance_show' method='post'>" +
    				"<div>" + 
    					"<label for='name'>Nombre del actor o película (1): </label>" +
    					"<input type='text' name='name1'/><br>" +
    					"<label for='name'>Nombre del actor o película (2): </label>" +
    					"<input type='text' name='name2'/><br>" +
    					"<button type='submit'>Enviar</button>" +
    				"</div>" +
    			"</form>" +
        		"<br><p><u>--Uso--</u></p>" + 
        		"<ul>" + 
    			  "<li>Pelicula --> (1):'NombrePeli1' | (2): 'NombrePeli2'" + 
    			  "<br>Ejemplo: 'The Great Gatsby'</li>" +
    			  "<br>" +
    			  "<li>Actor --> (1):'Nombre1 Apellido1' | (2): 'Nombre2 Apellido2'" +
    			  "<br>Ejemplo: 'Leonardo DiCaprio'</li>" +
    			"</ul>" +
        		"<p>*Nota* Si no se sabe el nombre exacto, poner una palabra (p.e. 'Leonardo' " +
    			"o 'Great' en este caso). Se ofreceran las coincidencias de esa palabra.</p>";
    		return form;
        });
        
        post("/distance_show", (req, res) -> {
        	Graph graph = new Graph("Database/film_actors.txt", "/");
    		//Graph graph = new Graph("data/other-data/moviesG.txt", "/"); para antigüa tabla
    		String name1= req.queryParams("name1");
    		String name2 = req.queryParams("name2");
    		String result = GraphFuncionality.doDistance(graph, name1, name2);    		
        	return "<p>Has buscado la distancia entre: '" + 
        			name1 + "' y '" + name2 + "'.</p>" +
        			"<p>RESULTADO:</p>" + 
        			result + 
        			"<br><a href='/'>Volver</a>";
        	
        	//EJEMPLO (con moviesG.txt):
        	//Travolta, John (That's Dancing! (1985)) --> NAME 1
        	//Garland, Judy (mago de oz, dancing dancing) --> actriz que relaciona
        	//Burke, Billie (mago de oz) --> NAME 2
        	//Distancia 4
        	
        	//EJEMPLO (con film_actors.txt):
        	//Leonardo DiCaprio | The Great Gatsby --> NAME 1
        	//Tobey Maguire | The Great Gatsby / Spiderman --> actor que relaciona
        	//Willem Dafoe (Spiderman) --> NAME 2
        	//Distancia 4
        });
        
        get("/graph_info", (req, res) -> {
        	Graph graph = new Graph("Database/film_actors.txt", "/"); //podemos poner como global?
        	String nodos = String.format("%d", graph.V());
        	String edges = String.format("%d", graph.E());
        	String maxDegree = String.format("%d", SmallWorld.maxDegree(graph));
        	String maxDegreeName = SmallWorld.maxDegreeName(graph);
        	String minDegree = String.format("%d", SmallWorld.minDegree(graph));
        	String minDegreeName = SmallWorld.minDegreeName(graph);
        	String averageDegree = String.format("%.3f", SmallWorld.averageDegree(graph));
        	//String length = String.format("%d", SmallWorld.pathLength(graph, "King Kong"));
        	//El método de arriba es muy lento computacionalmente (tarda alrededor de 1 min).
        	
        	String result = "<p>Información sobre nuestro grafo:</p>" + 
        			"<ul>" + 
        			"<li>Número de nodos (vértices) = " + nodos + "</li>" +
        			"<li>Número de enlaces (edges) = " + edges + "</li>" +
        			"<li>Grado máximo (nodo con más vecinos) = " + maxDegree + " --> " + maxDegreeName +  ".</li>" +
        			"<li>Grado mínimo (nodo con menos vecinos) = " + minDegree + " --> " + minDegreeName +  ".</li>" +
        			"<li>Grado medio = " + averageDegree + "</li>" +
        			"</ul>";
        	return result;
        });
        

        get("/graph_filter", (req, res) -> {
        	String form =
        	"<h3>Filtrado mediante grafos</h3> " +
        	"<p>Proporcione nombre de película o actor. Se obtendrán actores que han " + 
        	"trabajado en esa película, o películas en las que ha trabajado ese actor:</p>" +
    		"<form action='/graph_filter_show' method='post'>" +
				"<div>" + 
					"<label for='name'>Nombre del actor o película: </label>" +
					"<input type='text' name='name'/>" +
					"<button type='submit'>Enviar</button>" +
				"</div>" +
			"</form>" +
        	"<p>*Nota* Si no se sabe el nombre exacto, poner una palabra (p.e. 'Mark' " +
			"o 'Batman'). Se ofreceran las coincidencias de esa palabra.</p>" +
        	
			"<hr>" + 
			
			"<p>Ranking de actores. Introduzca un número. Se obtendrán actores que han " + 
			"trabajado en x o más películas, el número y los nombres de estas:</p>" +
			"<form action='/graph_filter_ranking_show' method='post'>" +
			"<div>" + 
				"<label for='name'>Número mínimo de películas: </label>" +
				"<input type='number' name='number'/>" +
				"<button type='submit'>Enviar</button>" +
			"</div>" +
			"</form>"
			;
        	return form;
        });
        
        post("/graph_filter_show", (req, res) -> {
        	Graph graph = new Graph("Database/film_actors.txt", "/");
    		String name= req.queryParams("name");
    		String result = GraphFuncionality.doGraphFilter(graph, name);
    		
        	return "<p>Has buscado los vecinos de: '" + name + ".</p>" +
        			"<p>RESULTADO:</p>" +
        			"<ul>" + 
        			result + 
        			"</ul>" +
        			"<br><a href='/'>Volver</a>";
        });
        
        post("/graph_filter_ranking_show", (req, res) -> {
        	Graph graph = new Graph("Database/film_actors.txt", "/");
    		String number= req.queryParams("number");
//    		GraphFuncionality.doRanking(graph, number);
    		String result = "";
    		
    		if (number.equals("")) {
    			result = "<p>ERROR. Debe introducir un número en el form. Por favor, inténtalo de nuevo.</p>";
    		}else {
    			result = "<table>" + 
    				"<tr>" +
						"<th>Actor</th>" + 
						"<th>Número de películas</th>" +
					"</tr>";
	
    			for (String v : GraphFuncionality.doRanking(graph, number)) { //Iterador
    				if (graph.getter(v) == 0) {
    					result += "<tr>" + 
    							"<td>" + v + "</td>" + 
    							"<td>" + graph.degree(v) + "</td></tr>";
    				}
    			}
    			result += "</table>";
    		}
    		
        	return "<p>Has buscado actores con " + number + " o más películas.</p>" +
        			"<p>RESULTADO:</p>" +
        			result + 
        			"<br><a href='/'>Volver</a>";
        });
    }
}

