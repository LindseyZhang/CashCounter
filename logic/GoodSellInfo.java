package logic;

public class GoodSellInfo {
private GoodInfo good;
private double num;
private double total_price;
private double discount;

public GoodSellInfo(){
	
}

public GoodSellInfo(GoodInfo good){
	this.good = good;
	this.num = 1;
}
public GoodSellInfo(GoodInfo good,double num){
	this.good = good;
	this.num = num;
}

public String toString(){
	return good.toString()+"num:"+ num + "total_price:"+ total_price + "discount: "+ discount;
}

// getter, setter method
public GoodInfo getGood() {
	return good;
}
public void setGood(GoodInfo good) {
	this.good = good;
}
public double getNum() {
	return num;
}
public void setNum(double num) {
	this.num = num;
}
public double getTotal_price() {
	return total_price;
}
public void setTotal_price(double total_price) {
	this.total_price = total_price;
}
public double getDiscount() {
	return discount;
}
public void setDiscount(double discount) {
	this.discount = discount;
}


}
