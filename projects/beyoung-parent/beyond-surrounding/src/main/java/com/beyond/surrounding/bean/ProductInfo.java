package com.beyond.surrounding.bean;

import java.util.List;

public class ProductInfo {
	private String title;
	private List<Product> product_category;
	private List<Product> product_item;
	private Float ratio;
	private Integer price;
	private Integer discount;

	public ProductInfo() {
	}

	public ProductInfo(String title, List<Product> product_category, List<Product> product_item, Float ratio) {
		this.title = title;
		this.product_category = product_category;
		this.product_item = product_item;
		this.ratio = ratio;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Product> getProduct_category() {
		return product_category;
	}

	public void setProduct_category(List<Product> product_category) {
		this.product_category = product_category;
	}

	public List<Product> getProduct_item() {
		return product_item;
	}

	public void setProduct_item(List<Product> product_item) {
		this.product_item = product_item;
	}

	public Float getRatio() {
		return ratio;
	}

	public void setRatio(Float ratio) {
		this.ratio = ratio;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

}
