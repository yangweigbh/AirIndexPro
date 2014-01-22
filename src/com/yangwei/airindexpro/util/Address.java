package com.yangwei.airindexpro.util;

public class Address {
	String address;
	String province;
	String city;
	double longtitude;
	double latitude;
	
	public Address(String address, String province, String city,
			double longtitude, double latitude) {
		super();
		this.address = address;
		this.province = province;
		this.city = city;
		this.longtitude = longtitude;
		this.latitude = latitude;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	
	
	
	
}
