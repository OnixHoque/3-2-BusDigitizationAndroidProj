package com.example.anik.busdigitizationprototype.Utility;
import java.util.Comparator;

/**
 * This class is the data structure for the elements of Search Result
 */
public class ResultItems {
    String busName;
    int price;
    double time;
    double rating;
    double distance;
    int hazardCount;
    String start_time;
    String hazard;


    public ResultItems(String busName, int price, double time, double rating, double distance, int hazardCount) {
        this.busName = busName;
        this.price = price;
        this.time = time;
        this.rating = rating;
        this.distance = distance;
        this.hazardCount = hazardCount;
    }

    public ResultItems(String busName, int price, double time, double rating, double distance, int hazardCount, String hazard, String start_time) {
        this.busName = busName;
        this.price = price;
        this.time = time;
        this.rating = rating;
        this.distance = distance;
        this.hazardCount = hazardCount;
        this.start_time = start_time;
        this.hazard = hazard;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getHazard() {
        return hazard;
    }

    public String getBusName() {
        return busName;
    }


    public int getPrice() {
        return price;
    }


    public double getDistance() {
        return distance;
    }


    public int getHazardCount() {
        return hazardCount;
    }


    public double getTime()
    {
        return time;
    }

    public double getRating()
    {
        return rating;
    }

    @Override
    public String toString() {
        return "ResultItems [busName=" + busName + ", price=" + price + ", time=" + time + ", rating=" + rating
                + ", distance=" + distance + ", hazardCount=" + hazardCount + "]";
    }

    public static Comparator<ResultItems> sortByPrice = new Comparator<ResultItems>() {
        public int compare(ResultItems o1, ResultItems o2) {
            int price1 = o1.getPrice();
            int price2 = o2.getPrice();
            return price1-price2;	//Ascending
        }
    };
    public static Comparator<ResultItems> sortByHazard = new Comparator<ResultItems>() {
        public int compare(ResultItems o1, ResultItems o2) {
            int haz1 = o1.getHazardCount();
            int haz2 = o2.getHazardCount();
            return haz1-haz2;	//Ascending
        }
    };

    public static Comparator<ResultItems> sortByDistance = new Comparator<ResultItems>() {
        public int compare(ResultItems o1, ResultItems o2) {
            double dist1 = o1.getDistance();
            double dist2 = o2.getDistance();
            //return dist1-dist2;	//Ascending
            return 	 	dist1 < dist2	? -1
                    :	dist1 > dist2 	? 1
                    :0;
        }
    };
    public static Comparator<ResultItems> sortByTime = new Comparator<ResultItems>() {
        public int compare(ResultItems o1, ResultItems o2) {
            double time1 = o1.getTime();
            double time2 = o2.getTime();
            //return time1-time2;	//Ascending
            return 	 	time1 < time2	? -1
                    :	time1 > time2 	? 1
                    :0;
        }
    };

    public static Comparator<ResultItems> sortByRating = new Comparator<ResultItems>() {
        public int compare(ResultItems o1, ResultItems o2) {
            double rating1 = o1.getRating();
            double rating2 = o2.getRating();
            //Descending
            return 	 	rating1 > rating2	? -1
                    :	rating1 < rating2 	? 1
                    :0;
        }
    };
}
