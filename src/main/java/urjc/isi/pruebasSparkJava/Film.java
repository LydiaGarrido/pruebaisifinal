package urjc.isi.pruebasSparkJava;

public class Film {

	private static String title;
	private static String year;
	private static String genre;
	private static String actor;
	
	public String getTitle()
    {
        return title;
    }

    public String getYear()
    {
        return year;
    }
    
    public String getGenre()
    {
        return genre;
    }
    
    public String getActor()
    {
        return actor;
    }
    
    public static String setTitle(String t)
    {
        return title = t;
    }

    public static String setYear(String y)
    {
        return year = y;
    }
    
    public static String setGenre(String g)
    {
        return genre = g;
    }
    
    public static String setActor(String a)
    {
        return actor = a;
    }
}
