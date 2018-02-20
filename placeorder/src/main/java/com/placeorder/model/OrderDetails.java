package com.placeorder.model;

public class OrderDetails {
	private int orderId;
	private CustomerDetails custDetails;
	private ItemDetails itemDetails;


	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public ItemDetails getItemDetails() {
		return itemDetails;
	}
	public void setItemDetails(ItemDetails itemDetails) {
		this.itemDetails = itemDetails;
	}
	public CustomerDetails getCustDetails() {
		return custDetails;
	}
	public void setCustDetails(CustomerDetails custDetails) {
		this.custDetails = custDetails;
	}

}
