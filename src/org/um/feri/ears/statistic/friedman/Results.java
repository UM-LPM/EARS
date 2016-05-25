package org.um.feri.ears.statistic.friedman;

public class Results {
	public String Name;
	public double AverageRank;
	public double Rating;
	public double RatingDev;
	public double RatingVol;
	public String DiffersNeme;
	public String DiffersHolm;
	public String DiffersShaf;
	
	public Results(){
		Name = "";
		AverageRank = 0;
		Rating = 0;
		RatingDev = 0;
		RatingVol = 0;
		DiffersNeme = "";
		DiffersHolm = "";
		DiffersShaf = "";
	}
	
	public void setName(String aName){
		Name = aName;		
	}
	public void setAverageRank(double aAverageRank){
		AverageRank = aAverageRank;
	}
	public void setRating(double aRating){
		Rating = aRating;
	}
	public void setRatingDev(double aRatingDev){
		RatingDev = aRatingDev;
	}
	public void setRatingVol(double aRatingVol){
		RatingVol = aRatingVol;
	}
	public void setDiffers(String aDiffersNeme,String aDiffersHolm,String aDiffersShaf){
		DiffersNeme = aDiffersNeme;
		DiffersHolm = aDiffersHolm;
		DiffersShaf = aDiffersShaf;
	}
	
	
}
