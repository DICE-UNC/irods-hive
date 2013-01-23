package maui.main;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.wikipedia.miner.model.Article;
import org.wikipedia.miner.model.Label.Sense;



public class Context {
	
	HashMap<String, Double> cachedRelatedness = new HashMap<String, Double>() ;
	
	Vector<Article> contextArticles ;

	public Context() {
		contextArticles = new Vector<Article>() ;
	}
	
	public void addSense(Sense sense) {
		contextArticles.add(sense) ;
	}


	@Override
	public String toString() {
		String result = "";
		for (Article a : contextArticles) {
			result += a + "\n";
		}
		return result;
	}
	
	

	public double getRelatednessTo(Article art) throws SQLException {
		// commented out to get it to compile - mcc
		throw new UnsupportedOperationException("had to hack this method out to get it to compile - mcc");
		/*
		double relatedness = 0 ;

		for (Article contextArt: contextArticles) 
			relatedness = relatedness + art.getRelatednessTo(contextArt) ;	

		return relatedness / contextArticles.size() ;
		*/
	}
	
	private boolean isDate(Article art) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM d") ;
		Date date = null ;
		
		try {
			date = sdf.parse(art.getTitle()) ;
		} catch (ParseException e) {
			return false ;
		}

		return (date != null) ;		
	}
}

