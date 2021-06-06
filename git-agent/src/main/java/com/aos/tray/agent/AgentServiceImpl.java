package com.aos.tray.agent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class AgentServiceImpl implements AgentService {
	public static Logger logger = LogManager.getLogger(AgentServiceImpl.class);
	
	@Value("${tray.no}")
	private int trayNo;
	
	@Value("${zeroing.value}")
	private long zeroing;

	@Value("${sensor.sck}")
	private int sck;
	
	@Value("${sensor.dt}")
	private int dt;
	
	@Autowired
	private AgentRepository agentRepository;

	@Override
	@Scheduled(fixedDelay = 1000 * 5)
	public void isOrder() throws Exception {
		logger.info("-------------------------------------");
		logger.info("Running");
		
		long value = agentRepository.getWeight(trayNo, sck, dt, zeroing);
		double weight = value * 0.001; 
		
		String response = agentRepository.postWeight(trayNo, weight);
		
		Message message = toObject(response, Message.class);

		logger.info(message.isStatus());
		
		agentRepository.setLight(trayNo, message.isStatus());
		logger.info("-------------------------------------");
	}
	
	private <T> T toObject(String message, Class<T> resultType) {
		return (T) new Gson().fromJson(message, resultType);
	}
}
