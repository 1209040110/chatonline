package com.yichen.entity;

import java.util.LinkedList;
import java.util.Queue;


public class Doctor extends User {
	private int waitingPatisAmount;
	private Patient attendingPatient;
	private Queue<Patient> waitingPatiQueue=new LinkedList<Patient>();
	private String status;
	public Doctor(String id, String identity) {
		super(id, identity);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}



	public Patient getAttendingPatient() {
		return attendingPatient;
	}
	public void setAttendingPatient(Patient attendingPatient) {
		this.attendingPatient = attendingPatient;
	}
	public Queue<Patient> getWaitingPatiQueue() {
		return waitingPatiQueue;
	}
	public void setWaitingPatiQueue(Queue<Patient> waitingPatiQueue) {
		this.waitingPatiQueue = waitingPatiQueue;
	}
	public int getWaitingPatisAmount() {
		return waitingPatisAmount;
	}
	public void setWaitingPatisAmount(int waitingPatisAmount) {
		this.waitingPatisAmount = waitingPatisAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Doctor other = (Doctor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	
}
