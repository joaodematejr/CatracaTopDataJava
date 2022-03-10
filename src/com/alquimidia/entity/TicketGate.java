package com.alquimidia.entity;

public class TicketGate {

	String id_catraca = "";
	String status = "";
	String sendito = "";
	String msg = "";

	public String getId_catraca() {
		return id_catraca;
	}

	public void setId_catraca(String id_catraca) {
		this.id_catraca = id_catraca;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSendito() {
		return sendito;
	}

	public void setSendito(String sendito) {
		this.sendito = sendito;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "TicketGate [id_catraca=" + id_catraca + ", status=" + status + ", sendito=" + sendito + ", msg=" + msg
				+ "]";
	}

}
