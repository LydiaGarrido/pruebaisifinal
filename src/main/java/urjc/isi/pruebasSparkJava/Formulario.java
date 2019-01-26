package urjc.isi.pruebasSparkJava;


public class Formulario {
	String formulary=
			"<form action='/comment' method='post'> "
				+ "Comment:<br> <input type='text' name='comment'> "
				+ "User: <input type='number' name='user' required/><br> "
				+ "<input type='submit' value='Comentar'> "
				+"<input type='hidden' name='film' value='3'>"
			+ ""
			+ "</form> <form action='/score' method='post'> "
				+ "Score(0-10):<br> <input type='number' name='score' min='0' max='10'> "
				+ "User: <input type='number' name='user' required/><br> "
				+ "<input type='submit' value='Puntuar'> </form>"
				+"<input type='hidden' name='film' value='3'";

}
