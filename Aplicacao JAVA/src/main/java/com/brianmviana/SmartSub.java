package com.brianmviana;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;


public class SmartSub implements MqttCallback  {

	private MqttClient client;
	private List<Map<String,List<String>>> mensagens;

	public SmartSub(String server) {
		mensagens = new ArrayList<Map<String,List<String>>>();
		try {
			client = new MqttClient(server, MqttClient.generateClientId());
			client.setCallback(this);
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}


	public void subscribe(String topic) {
		try {
			client.connect();
			client.subscribe(topic);
		} catch (MqttSecurityException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}
	
	public List<Map<String, List<String>>> getMensagens() {
		return mensagens;
	}
	
	public List<String> getMensagensByTopic(String topic) {
		for (Map<String, List<String>> map : mensagens) {
			if(map.containsKey(topic)) {
				return map.get(topic);
			}
		}
		return null;
	}

	@Override
	public void connectionLost(Throwable cause) {}

	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
	    System.out.println("Message received:\t"+ new String(mqttMessage.getPayload()) );
	    Boolean isExist = false;
	    Integer index = 0;
	    if(!mensagens.isEmpty()) {
		    for (Map<String, List<String>> map : mensagens) {
		    	if(map.containsKey(topic)) {
   		    		isExist = true;
		    		break;
		    	}
		    	index++;
			}	    	
	    }
	    if(isExist) {
//		    if("/USUARIO/DISPOSITIVOS".equals(topic)) {
//		    	mensagens.get(index).get(topic).removeAll(mensagens.get(index).get(topic));
//		    }
	    	mensagens.get(index).get(topic).add(new String(mqttMessage.getPayload()));
	    } else {
		    List<String> lista = new ArrayList<String>();
		    lista.add(new String(mqttMessage.getPayload()));
		    Map<String, List<String>> mapa = new HashMap<String,List<String>>();
		    mapa.put(topic, lista);
		    mensagens.add(mapa);	    	
	    }
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {}
	

}
