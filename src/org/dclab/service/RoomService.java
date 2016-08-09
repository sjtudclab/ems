/**
 * 
 */
package org.dclab.service;

import java.util.ArrayList;
import java.util.List;

import org.dclab.model.CandidateBean;
import org.dclab.model.RoomBean;

/**
 * Room distribution service
 * 1. Get all students registered to this course
 * 2. Count how many session needed for this course by comparing students number with seats number
 * 	  (Check the available room first)
 * 3. Distribute the seats to students
 * 
 * @author zhaoz
 *
 */
public class RoomService {

	public static boolean isFull = false;
	public static int sessionNumber = 1;
	
	/**
	 * count how many session are needed for this subject
	 * 
	 * @param subjectId
	 * @return
	 */
	public int getSessionNum(int subjectId) {
		int sessionNum = 1;
		
		int seatNum 	= 	123;	//TODO: load SUM of room size from `room` table
		int roomNum		=	5;		//TODO: load COUNT of room from `room` table
		int studentNum	=	223;	//TODO: load COUNT of student from `candidate_subject` table		
		int reservedSeatNum	=	2;	//TODO: load reserved seat number from `ems.properties` file
		
		if (studentNum > seatNum ) {
			sessionNum = studentNum / seatNum;
			int remainder = studentNum % seatNum;
			
			if(remainder < reservedSeatNum * roomNum){
				isFull = true;	//use reserved seats for exam to reduce unnecessary session number
			
			}else{
				sessionNum ++;
				
			}
			
		}
		this.sessionNumber = sessionNum;
		
		return sessionNum;
	}
	
	/**
	 * 
	 * @param startTimestamp
	 * @param duration
	 * @return
	 */
	public List<RoomBean> getAvailableRooms(long startTimestamp, int duration){
		//TODO: create a `session_room` relation table, get all available room
		//		"select * from `session_room` where startTimestamp + duration < startTime || startTime + table.duration < startTimestamp"
		List<RoomBean> availableRooms = new ArrayList<RoomBean>(); //load from DB
		
		return availableRooms;
	}
	
	/**
	 * distribute seats of exam room to specific students
	 * 
	 * @param subjectId
	 */
	public void distributeSeats(int subjectId){
		//TODO: get all students registered to this course
		List<CandidateBean> students 	= new ArrayList<>();
		List<RoomBean> 		rooms		= new ArrayList<>();
		
		//use 1 session as an example, different session has different startTime, use it to invoke method `getAvaiableRoom`
		int sessionNum	= 1;
		int curPos		= 0;
		for (RoomBean roomBean : rooms) {
			int size = roomBean.getSize();
			int roomId = roomBean.getId();
			
			for (int i = 0; i < size; i++) {
				students.get(curPos).setRoomId(roomId);	//set seat for each student
				students.get(curPos).setSeatNum(i);
				curPos ++;
			}
		}
		
		//TODO: write data to DB, flush "students" list to table `room_candidate`
	
	}
	
}
