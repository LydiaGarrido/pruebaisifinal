package urjc.isi.pruebasSparkJava;

import java.net.URISyntaxException;

import spark.Request;
import spark.Response;

public class Score {
	//Puntuaciones
	
		//Guardo la nueva puntuacion
		public String newScore(int score, int user, String film) {
			if (score<0 || score>10) {
				throw new IllegalArgumentException("Puntuacion invalida");
			}else if (user<0) {
				throw new IllegalArgumentException("Usuario invalido");
			}else if (film.equals(null)) {
				throw new IllegalArgumentException("Pelicula invalida");
			}else {
			// Creo la clase bbdd
			//Obtener el id de la pelicula
			//bbdd.filterByName(film); si nos hace falta sacar el id
			//Llamar a la funcion para añadir. 
			return ("Puntuacion añadida");
			}
		}
		
		//Obtengo la nueva media 
		public int getScor (String film) {
			if (film.equals(null)) {
				throw new IllegalArgumentException("Pelicula invalida");
			}else {
			//Llamara a una funcion que me devuelva todas las puntuaciones 
			//referentes al nombre que me pasan, de ese vector saco la medi
			int media = 4;
			return media;
			}
		}
		
		//Actualizo la media
		public void changeScore(int score, String film) {
			if (score<0 || score>10) {
				throw new IllegalArgumentException("Puntuacion invalida");
			}else if (film.equals(null)) {
				throw new IllegalArgumentException("Pelicula invalida");
			}else {
			//Llamar a la función para cambiar la puntuacion de la pelicula, pedir. 
				
			}
		}
		
		public String postScore(Request request) throws ClassNotFoundException, URISyntaxException {
			System.out.println("Puntuando");
			String result = new String("Puntuacion");			
			String score_string=request.queryParams("score");
			int score=Integer.parseInt(score_string);
			String user_string=request.queryParams("user");
			int user=Integer.parseInt(user_string);
			String film=request.queryParams("film");
			result=newScore(score, user, film);
			score=getScor(film);
			changeScore(score, film);
			return result;
		}
		

}
