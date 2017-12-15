package com.brianmviana;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class SmartPub {

	private MqttClient client;
	private MqttMessage message;

	public SmartPub(String server){
		try {
			client = new MqttClient(server, MqttClient.generateClientId());
		} catch (MqttException e) {
			e.printStackTrace();
		}
		message = new MqttMessage();
	}

	public void publicar(String topic, String messageString){
		try {
			client.connect();
			message.setPayload(messageString.getBytes());
			client.publish(topic, message);
			client.disconnect();
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

}
