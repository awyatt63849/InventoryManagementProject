package Model;

public class ProductColour {

    private int productId;
    private int colourId;

    public ProductColour(int productId, int colourId) {
        this.productId = productId;
        this.colourId = colourId;
    }

    public int getProductId() {
        return productId;
    }

    public void setproductId(int productId) {
        this.productId = productId;
    }

    public int getColourId() {
        return colourId;
    }

    public void setcolourId(int colourId) {
        this.colourId = colourId;
    }

    @Override
    public String toString() {
        return  "productId=" + productId +
                ", colourId=" + colourId;
    }

}
