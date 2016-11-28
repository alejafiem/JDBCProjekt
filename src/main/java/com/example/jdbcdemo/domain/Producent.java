package com.example.jdbcdemo.domain;

public class Producent {
	private int id;
	private String nazwa;
	private String miasto;
	private String nrTel;
	private int nip;
	
	public Producent() {}
	
	public Producent(String nazwa, String miasto, String nrTel, int nip) {
		this.nazwa = nazwa;
		this.miasto = miasto;
		this.nrTel = nrTel;
		this.nip = nip;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNazwa() {
		return nazwa;
	}
	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	public String getMiasto() {
		return miasto;
	}
	public void setMiasto(String miasto) {
		this.miasto = miasto;
	}
	public String getNrTel() {
		return nrTel;
	}
	public void setNrTel(String nrTel) {
		this.nrTel = nrTel;
	}
	public int getNip() {
		return nip;
	}
	public void setNip(int nip) {
		this.nip = nip;
	}
}
