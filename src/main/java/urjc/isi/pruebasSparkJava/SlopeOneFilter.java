package urjc.isi.pruebasSparkJava;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author nshandra, jaimefdez96, AlbertoCoding
 * <p>
 * data: userID | titleID | score<br>
 * diffMap: titleID-A | titleID-B | difference<br>
 * weightMap: titleID-A | titleID-B | weight<br>
 * predictions: userID | titleID | prediction
 */
public class SlopeOneFilter {

	Map<Integer, Map<Integer, Double>> data;
	Map<Integer, Map<Integer, Double>> diffMap;
	Map<Integer, Map<Integer, Integer>> weightMap;
	Map<Integer, LinkedList<Node>> predictions;

	class Node {
		int titleID;
		double prediction;

		public Node(int t, double p) {
			titleID = t;
			prediction = p;
		}
		
		public int getKey() { 
			return titleID; 
		}

		public double getValue() { 
			return prediction; 
		}

		public String toString() {
			return "titleID: " + getKey() + ", Pred: " + getValue();
		}		
	}
	
	class NodeComp implements Comparator<Node> {
		@Override
		public int compare(Node d1, Node d2) {
			if(d2.prediction<d1.prediction) {
				return -1;
			} else if(d1.prediction<d2.prediction) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	
	public SlopeOneFilter() {
//		data  = new HashMap<>();
//		JDBC call
//		buildMaps();
//		for (int user : data.keySet()){
//			predict(user);
//		}
	}

	//Introduzco las diferencias totales de cada pareja de películas: movie_A movie_B total_diff
	public void setDiffMap(int movie_A, int movie_B, Double diff, Map<Integer,Double> movie_diff) {
		if(!diffMap.containsKey(movie_A)) {
			movie_diff = new HashMap<Integer,Double>();
			movie_diff.put(movie_B,diff);
			diffMap.put(movie_A,movie_diff);
		}else{
			movie_diff = diffMap.get(movie_A);
			if(movie_diff.get(movie_B) == null) {
				movie_diff.put(movie_B,diff);
			}else {
				movie_diff.put(movie_B,diff + movie_diff.get(movie_B));
			}
		}
	}

	//Introduzco la frecuencia de cada pareja de peliculas: movie_A movie_B weight
	public void setWeightMap(int movie_A, int movie_B,Map<Integer,Integer> movie_weight) {
		if(!weightMap.containsKey(movie_A)) {
			movie_weight = new HashMap<Integer,Integer>();
			movie_weight.put(movie_B,1);
			weightMap.put(movie_A,movie_weight);
		}else {
			movie_weight = weightMap.get(movie_A);
			if(movie_weight.get(movie_B) == null) {
				movie_weight.put(movie_B,1);
			}else {
				movie_weight.put(movie_B,movie_weight.get(movie_B) + 1);
			}
		}
	}

	//Calculo y establezco la diferencia promedio de cada pareja apoyandome de la frecuencia
	public void setAvgDiff() {
		for(Map.Entry<Integer, Map<Integer, Double>> entry: diffMap.entrySet()) {
			int movie_A = entry.getKey();
			Map<Integer,Double> movie = entry.getValue();
			for(Map.Entry<Integer, Double> my_movie: movie.entrySet()) {
				Map<Integer,Integer> movie_weight = weightMap.get(movie_A);
				int movie_B = my_movie.getKey();
				int weight = movie_weight.get(movie_B);
				my_movie.setValue(my_movie.getValue()/weight);
			}
		}
	}

	public void buildMaps(){
		// Crear diffMap y weightMap a partir de data.
		diffMap = new HashMap<Integer,Map<Integer,Double>>();
		weightMap = new HashMap<Integer,Map<Integer,Integer>>();
		Map<Integer,Double> movie_B_diff = new HashMap<Integer,Double>();
		Map<Integer,Integer> movie_B_weight = new HashMap<Integer,Integer>();


		for(Map<Integer, Double> user_movies: data.values()) {
			for(Entry<Integer, Double> movie: user_movies.entrySet()) {
				int movie_A = movie.getKey();
				for(Map.Entry<Integer, Double> other_movie: user_movies.entrySet()) {
					int movie_B = other_movie.getKey();
					if(movie_A == movie_B) {
						continue;
					}

					Double score_A = movie.getValue();
					Double score_B = other_movie.getValue();
					Double diff = score_A - score_B;

					setDiffMap(movie_A,movie_B,diff,movie_B_diff);
					setWeightMap(movie_A,movie_B,movie_B_weight);
				}
			}
		}
		setAvgDiff();
	}




/*
	public int sumaWeights(Map<Integer, Map<Integer, Integer>> weights) {

		int suma = 0;

		for(Map.Entry<Integer, Map<Integer, Integer>> entry: weights.entrySet()) {
			//Map<Integer, Integer> ;
		}
		return suma;
	}


	public int predictOneMovie() {

		this.predictions = new HashMap<Integer,Map<Integer,Double>>();
		Map<Integer, Double> moviesUser = new HashMap<Integer, Double>();

		//Creamos user_movies
		for(Map.Entry<Integer, Map<Integer, Double>> entry: data.entrySet()) {
			Map<Integer, Double> user_movies = entry.getValue();
		}

		Iterator it = user_movies.entrySet().iterator();

		// Cantidad de diferencias y de veces que hay diferencias
                ndiffMap = this.diffMap.size();
		nweightMap = this.weightMap.size();

                if(ndiffMap == nweightMap){
                        int total = 0;
                        int n = sumaWeigths(this.weightMap);
			int i = 0;
			int j = 0;

				for(Map.Entry<Integer, Map<Integer, Integer>> entry: this.weightMap.entrySet()) {
                                	Map<Integer, Integer> weights = entry.getValue();
					for(Map.Entry<Integer, Integer> weight = weights.entrySet()) {
						int frec = weight.getValue();
					}
				}

				for(Map.Entry<Integer, Map<Integer, Double>> entry: this.diffMap.entrySet()) {
					Map<Integer, Double> diffs = entry.getValue();
					for(Map.Entry<Integer, Double> diff = diffs.entrySet()) {
						double diff = diff.getValue();
					}
				}

			//	total = total + (frec * (diff + );
                       

                        total = total/n;

                }else{
                        System.out.println("Error en los tamaños de los arrays");
                }

                return total;
        }



	public void predict(int user) {

		while(it.hasNext()) {
			peli= ;
			if(noPuntuada(user, peli)){
				predictions.add(predictOneMovie(user, peli));
			}else{
				predictions.add(valorPuntuadoPorElUsuario);

			}
		}
	}
*/



	public int getIndex(int user, double value) {
		int pos = 0;
		ListIterator<Node> itrator = predictions.get(user).listIterator();
		while (itrator.hasNext()) {
			if (itrator.next().getValue() <= value) {
				break;
			} else {
				pos++;
			}
		}
		return pos;
	}
	
	public void recommend(int user, int nItems) {
		// Mostrar nItems predicciones con mayor puntuación.
		LinkedList<Node> predictionList = predictions.get(user);
		predictionList.sort(new NodeComp());
		
		ListIterator<Node> itrator = predictionList.listIterator();
		 
		for (int i=0; i < nItems; i++) {
			itrator.hasNext();
			System.out.println(itrator.next());
		}
	}
	
	public static void main(String args[]){
		SlopeOneFilter so = new SlopeOneFilter();

		so.data = new HashMap<>();
		
		Integer item_A = 1;
		Integer item_B = 11;
		Integer item_C = 111;

		HashMap<Integer, Double> user1 = new HashMap<>();
		HashMap<Integer, Double> user2 = new HashMap<>();
		HashMap<Integer, Double> user3 = new HashMap<>();

		user1.put(item_A, 5.0);
		user1.put(item_B, 3.0);
		user1.put(item_C, 2.0);
		so.data.put(1, user1);
		user2.put(item_A, 3.0);
		user2.put(item_B, 4.0);
		so.data.put(2, user2);
		user3.put(item_B, 2.0);
		user3.put(item_C, 5.0);
		so.data.put(3, user3);
		System.out.println("data\n" + so.data);
		System.out.println("----");
		so.buildMaps();
		
		System.out.println("diffMap\n" + so.diffMap);
		System.out.println("----");
		System.out.println("weightMap\n" + so.weightMap);
		System.out.println("----");
		
		so.predictions = new HashMap<>();
		so.predictions.put(1, new LinkedList<Node>());

		so.predictions.get(1).add( so.new Node(1 , 8.0));
		so.predictions.get(1).add( so.new Node(2 , 1.0));
		so.predictions.get(1).add( so.new Node(3 , 10.0));
		
		System.out.println(so.predictions);
		so.recommend(1, 2);
		System.out.println(so.predictions);
		so.predictions.get(1).add(so.getIndex(1, 5.0), so.new Node(4 , 5.0));
		so.predictions.get(1).add(so.getIndex(1, 4.0), so.new Node(5 , 4.0));
		so.predictions.get(1).add(so.getIndex(1, 6.0), so.new Node(6 , 6.0));
		System.out.println(so.predictions);
	}
}
