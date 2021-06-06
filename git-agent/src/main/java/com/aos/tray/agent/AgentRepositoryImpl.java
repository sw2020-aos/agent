package com.aos.tray.agent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Repository
public class AgentRepositoryImpl implements AgentRepository {
	public static Logger logger = LogManager.getLogger(AgentRepositoryImpl.class);
	public static final MediaType MEDIA_TYPE_HTML = MediaType.get("tex/html; charset=utf-8");
	
	@Value("${server.url}")
	String url;

	@Override
	public long getWeight(int trayNo, int sck, int dt, long zeroing) {
		LoadCell loadCell = new LoadCell(sck, dt, 24, zeroing);
		
		long weight = loadCell.getValue();
		
		if (weight <= 0) {
			weight = 0;
		}
		logger.info("weight : " + weight);
		
		return weight;
	}

	@Override
	public String postWeight(int trayNo, double weight) throws Exception {
		RequestBody body = new FormBody.Builder()
				.add("trayNo", trayNo + "")
				.add("weight", weight + "")
				.build();

		Request request = new Request.Builder().url(url).post(body).build();
		try (Response response = new OkHttpClient()
				.newCall(request)
				.execute()) {
			return response.body().string();
		}
	}

	@Override
	public void setLight(int trayNo, boolean status) throws InterruptedException {
		LedControl control = new LedControl();
		
		if (status) {
			control.ledOn();
		} else {
			control.ledOff();
		}
	}
}
