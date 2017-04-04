package domain;

import java.util.ArrayList;
import java.util.Collection;

public class Sale {

private String date;
private Customer customer;
private final Collection<SaleItem> saleItems = new ArrayList<>();

	public Sale(String date) {
		this.date= date;
	}
	public String getDate(){
		return date;
	}
	public void setDate(String date){
		this.date = date;
	}
	public Collection<SaleItem> getSaleItems(){
		return this.saleItems;
	}
	public void setSaleItem(String id, Double price, Double quantity){
		saleItems.add(new SaleItem( id, price, quantity));
	}
	public Customer getCustomer(){
		return this.customer;
	}
	public void setCustomer(String dob,char g){
		this.customer = new Customer(dob, g);
	}
	
@Override
	public String toString(){
		return date;
	}

}
