package urjc.isi.pruebasSparkJava;

import java.net.URISyntaxException;

import spark.Request;
import spark.Response;

public class Comment {
	//Comentarios
	
	//Guardo un nuevo comentario de un usuario hacia una pelicula
	public String postComment(Request request) {
		String result = new String("Comentario");
		String comment=request.queryParams("comment");
		String user_string=request.queryParams("user");
		int user=Integer.parseInt(user_string);
		String film=request.queryParams("film");
		result=newComment(comment, user, film);
		return result;
	}
	
	public String newComment(String text, int user, String film) {
		if (text.equals(null)) {
			throw new IllegalArgumentException("Comentario invalido");
		}else if (user<0) {
			throw new IllegalArgumentException("Usuario invalido");
		}else if (film.equals(null)) {
			throw new IllegalArgumentException("Pelicula invalida");
		}else {
		//Obtengo id de la pelicula
		//Almaceno el nuevo comentario
			System.out.println("Guardado");
			return "Comentario almacenado";
		}
	}
	
	//Devuelvo todos los comentarios que han hecho sobre una pelicula, con su usuario, para mostrar User: comment
	public String commentsFilm(String film){
		//Obtener el id con la función basica
		//Una funcion que me devuelva Un array de dos por dos con la info user, comen. 
		if (film.equals(null)) {
			throw new IllegalArgumentException("Pelicula invalida");
		}else {
			String coments [][]= new String[1][1];
			String result=commentToString(coments);
			return result;
		}
	}
	
	public String commentToString(String matrix_coment[][])
	{
		String text = "<h1>Comentarios que tiene la película:</h1>";
		if (matrix_coment.length==0) {
			throw new NullPointerException("No tiene comentarios");
		}else {
			for (int x = 0; x < matrix_coment.length; x++){
				String br = " ";
				text += br + matrix_coment[x][0] + ":" + matrix_coment[x][1];
			}
			text +=" ";
			return text;
		}
	}

}
