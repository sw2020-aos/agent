package com.aos.tray.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class TrayServiceImpl implements TrayService {
	@Value("${tray.no}")
	private int trayNo;
	
	@Value("${zeroing.value}")
	private long zeroing;

	@Value("${sensor.sck}")
	private int sck;
	
	@Value("${sensor.dt}")
	private int dt;
	
	@Autowired
	private TrayRepository trayRepository;

	@Override
	@Scheduled(fixedDelay = 1000 * 5)
	public void isOrder() throws Exception {
		System.out.println("-------------------------------------");
		System.out.println("Running");
		
		long value = trayRepository.getWeight(trayNo, sck, dt, zeroing);
		double weight = value * 0.001; 
		
		String response = trayRepository.postWeight(trayNo, weight);
		
		Message message = toObject(response, Message.class);

		System.out.println(message.isStatus());
		
		trayRepository.setLight(trayNo, message.isStatus());
		System.out.println("-------------------------------------");
	}
	
	private <T> T toObject(String message, Class<T> resultType) {
		return (T) new Gson().fromJson(message, resultType);
	}
}
