package ali.vu.groceryshop;

public class Product {
    private String id;
    private String title;
    private String imageUrl;
    private String price; // Change price to String
    private String category;

    public Product() {
        // Required empty constructor for Firebase
    }


    public Product(String id, String title, String imageUrl, String price) { // Change price to String
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() { // Change return type to String
        return price;
    }

    public void setPrice(String price) { // Change parameter type to String
        this.price = price;
    }
}
