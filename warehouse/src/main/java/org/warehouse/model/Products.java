package org.warehouse.model;

public class Products {
    private int productID;
    private String name;
    private ProductType productType;
    private double pricePerUnit;
    private int manufacturerID;
    private int inStock;
    private String productDescription;
    private String photoName;

    @Override
    public String toString() {
        return "Products{" +
                "productID=" + productID +
                ", name='" + name + '\'' +
                ", pricePerUnit=" + pricePerUnit +
                ", inStock=" + inStock +
                ", productDescription='" + productDescription + '\'' +
                ", photoName='" + photoName + '\'' +
                '}';
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public int getManufacturerID() {
        return manufacturerID;
    }

    public void setManufacturerID(int manufacturerID) {
        this.manufacturerID = manufacturerID;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }
}
