package com.example.fly;

public class ip {
	
	private static String my_ip = "192.168.155.1";
//	private static String my_ip = "172.25.168.1";
//	private static String my_ip = "172.16.15.234";
	
	public ip(){
	}

	public String getMy_ip() {
		return my_ip;
	}

	public void setMy_ip(String my_ip) {
		this.my_ip = my_ip.trim();
	}
}
