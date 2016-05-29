package logic;

public class GoodInfo {
	
  private String barcode;
  private String name;
  private String unit;
  private String category;
  private String sub_category;
  private double price;
  
  
  public GoodInfo(){
		
  }
  
  public GoodInfo(String barcode,
		  String name,
		  String unit,
		  String category,
		  String sub_category,
		  double price){
	  this.barcode = barcode;
	  this.name = name;
	  this.unit = unit;
	  this.category = category;
	  this.sub_category = sub_category;
	  this.price = price;
  }
  
	public String toString(){
		return new String("barcode:" + barcode + " name:" + name
				+ " unit:" + unit + " category:" + category 
				+ " sub_category:" + sub_category + " price:" + price);
	}
  
public String getBarcode() {
	return barcode;
}
public void setBarcode(String barcode) {
	this.barcode = barcode;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getUnit() {
	return unit;
}
public void setUnit(String unit) {
	this.unit = unit;
}
public String getCategory() {
	return category;
}
public void setCategory(String category) {
	this.category = category;
}
public String getSub_category() {
	return sub_category;
}
public void setSub_category(String sub_category) {
	this.sub_category = sub_category;
}
public double getPrice() {
	return price;
}
public void setPrice(double price) {
	this.price = price;
}

}
