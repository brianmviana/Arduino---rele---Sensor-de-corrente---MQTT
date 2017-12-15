package com.brianmviana;

public class SmartGrid {
	
	private String server;
	private SmartPub publisher;
	private SmartSub subscribe;
	
	public SmartGrid() {
		server = "tcp://localhost:1883";
		publisher = new SmartPub(server);
		subscribe = new SmartSub(server);
	}

	public SmartPub getPublisher() {
		return publisher;
	}

	public SmartSub getSubscribe() {
		return subscribe;
	}

}
