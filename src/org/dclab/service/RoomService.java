/**
 * 
 */
package org.dclab.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.dclab.model.CandidateBean;
import org.dclab.model.RoomBean;
import org.dclab.model.SessionBean;

/**
 * Room distribution service 
 * 座位分配流程：
 *  1. 选择科目，得到考生数目 
 *  2. 选择考试在哪一天举行，拿到这一天所有考场的考试安排情况，得到空闲座位数（原则上一天只安排一个科目的考试） 
 *  3， 根据空闲座位数计算 session（场次）数目
 *  4. 安排每个session 的考试时间 
 *  5. 根据session将空闲座位分配给考生
 * 
 * @author zhaoz
 *
 */
public class RoomService {

	public static boolean isFull = false;
	public static int reservedSeatNum = 2; // TODO: load reserved seat number
											// from `ems.properties` file or DB

	public int getStudentNum(int subjectId) {
		int studentNum = 123; // TODO: get student number from `student_subject`
								// table by subject Id
		return studentNum;
	}

	public int getVacantSeats(long startDay) {
		int vacantSeatNum = 125; // TODO: get vacant seats in this day

		return vacantSeatNum;
	}

	public int getVacantRooms(long startDay) {
		int vacantRoomNum = 5; // TODO: get vacant room number in this day

		return vacantRoomNum;
	}

	/**
	 * count how many session are needed for this subject
	 * 
	 * @param subjectId
	 * @return
	 */
	public int getSessionNum(int studentNum, int seatNum, int roomNum) {
		int sessionNum = 1;

		// int seatNum = 123; // TODO: load SUM of room size from `room` table
		// int roomNum = 5; // TODO: load COUNT of room from `room` table
		// int studentNum = 223; // TODO: load COUNT of student from
		// `candidate_subject` table

		if (studentNum > seatNum) {
			sessionNum = studentNum / seatNum;
			int remainder = studentNum % seatNum;

			if (remainder < reservedSeatNum * roomNum) {
				isFull = true; // use reserved seats for exam to reduce
								// unnecessary session number

			} else {
				sessionNum++;

			}

		}

		return sessionNum;
	}

	/**
	 * save session-room relation to DB
	 * 
	 * @param sessionBeans
	 */
	public boolean saveSession(int subjectId, Timestamp startTime, int duration, int roomId) {
		// TODO: firstly, load info from DB to check whether this room is vacant
		// in this time period
		boolean isVacant = true;

		// TODO: if room is available, save their relation to DB

		return isVacant;
	}

	/**
	 * After binding session to room, retrieve those rooms and be prepared to
	 * distribute seats to students
	 * 
	 * @param startTimestamp
	 * @param duration
	 * @return
	 */
	public List<RoomBean> getAvailableRooms(long startTimestamp, int duration) {
		// TODO: create a `session_room` relation table, get all available room
		// "select * from `session_room` where startTimestamp + duration <
		// startTime || startTime + table.duration < startTimestamp"
		List<RoomBean> availableRooms = new ArrayList<RoomBean>(); // TODO: load
																	// from DB

		return availableRooms;
	}

	/**
	 * distribute seats of exam room to specific students
	 * 
	 * @param subjectId
	 */
	public void distributeSeats(int subjectId) {

		// TODO: get all students registered to this course & all room & all
		// session from DB

		List<CandidateBean> students = new ArrayList<>();// TODO: load from DB
		List<RoomBean> rooms = new ArrayList<>();
		List<SessionBean> sessions = new ArrayList<>();// TODO: load from DB

		// use 1 session as an example, different session has different
		// startTime, use it to invoke method `getAvaiableRoom`

		int curPos = 0;
		for (SessionBean sessionBean : sessions) {

			// get all available rooms in this time period
			int duration=1900;//以后还用这个service的话删掉这个
			
			rooms = getAvailableRooms(sessionBean.getStartTime().getTime(),duration/*sessionBean.getDuration()*/);

			for (RoomBean roomBean : rooms) {
				int size = roomBean.getSize();
				int roomId = roomBean.getId();

				for (int i = 0; i < size - reservedSeatNum; i++) {
					/*students.get(curPos).setRoomId(roomId);*/ // set seat for each
															// student
					students.get(curPos).setSeatNum(i);
					curPos++;
				}
			}
		}

		// TODO: write data to DB, flush "students" list to table
		// `room_candidate`

	}

	/**
	 * 
	 * @param subjectId
	 * @return
	 */
	public List<SessionBean> getAvailableSessions(int subjectId) {
		long currentTime = System.currentTimeMillis();
		// TODO: get all session behind this session ( startTime > currentTime)

		return new ArrayList<SessionBean>();
	}

	/**
	 * Change exam session for a candidate
	 * 1. get subsequent session of this subject
	 * 2. change seat
	 * @param candidateId
	 * @param subjectId
	 */
	public boolean changeSession(int candidateId, int sessionId){
		
		boolean isAssigned = false;
		
		//TODO: get all rooms belong to this session
		List<RoomBean> roomBeans = new ArrayList<>();
		int roomId = 0;
		int seatId = 0;
		for (RoomBean roomBean : roomBeans) {
			roomId = roomBean.getId();
			int size   = roomBean.getSize();
			
			//TODO: get student number in this room by roomId
			int studentNum = 0;
			//assign a vacant seat to this student 
			if(size > studentNum){
				seatId = studentNum;
				isAssigned = true;	
				break;
			}
		}
		

		if(isAssigned){
			//TODO: save candiateId-sessionId-roomId-seatId to DB
			
			return true;
		} else{
			System.err.println("No Vacant Seat");
			return false;
		}
	}

}
