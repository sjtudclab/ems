package org.dclab.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dclab.User;
import org.dclab.model.SupervisorOperator;
import org.dclab.service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alvis
 *监考相关的controller
 */

@RestController
@RequestMapping("/supervise")
public class SuperviseController {
	@Autowired
	private SupervisorService supervisorService;
	public void setSupervisorService(SupervisorService service){
		supervisorService=service;
	}
	
	@RequestMapping("/start")
	public Map<String, Object> showDefaultPage(@RequestParam(value="Uid")int Uid){
		List<User> userList=supervisorService.getUserList(Uid);
		int roomId=SupervisorOperator.idSuperMap.get(Uid).getRoomId();
		List<Integer> loginedList=supervisorService.getLoginedList(roomId);
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("userList", userList);
		map.put("loginedList", loginedList);
		
		return map;
	}
	
	@RequestMapping("/refresh")
	public List<Integer> refreshLogin(@RequestParam(value="roomId")int roomId){
		return supervisorService.getLoginedList(roomId);
	}
	
}
