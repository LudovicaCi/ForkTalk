package it.unipi.inginf.lsdb.group15.forktalk.data_structures;

public class Review {
    private int id;
    private String date;
    private double rating;
    private Float rating_value;
    private Float rating_service;
    private Float rating_atmosphere;
    private Float rating_food;
    private String title;
    private String title_clean;
    private String content;
    private String content_clean;

    public Review(int id, String date, double rating, float rating_value, float rating_service, float rating_atmosphere, float rating_food,
                  String title, String title_clean, String content, String content_clean) {

        this.id = id;
        this.date = date;
        this.rating = rating;
        this.rating_value = rating_value;
        this.rating_service = rating_service;
        this.rating_atmosphere = rating_atmosphere;
        this.rating_food = rating_food;
        this.title = title;
        this.title_clean = title_clean;
        this.content = content;
        this.content_clean = content_clean;
    }

    public int getId() {return id;}

    public String getDate() {return date;}

    public double getRating() {return rating;}

    public float getRatingValue() {return rating_value;}

    public float getRatingService() {return rating_service;}

    public float getRatingAtmosphere() {return rating_atmosphere;}

    public float getRatingFood() {return rating_food;}

    public String getTitle() {return title;}

    public String getTitleClean() {return title_clean;}

    public String getContent() {return content;}

    public String getContentClean() {return content_clean;}
}