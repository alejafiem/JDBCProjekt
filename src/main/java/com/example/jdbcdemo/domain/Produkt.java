package com.example.jdbcdemo.domain;

public class Produkt {
	private int id;
	private String nazwa;
	private double cena;
	private double znizka;
	
	private Producent producent = null;
	
	public Produkt() {}
	
	public Produkt(String nazwa, double cena, double znizka) {
		this.nazwa = nazwa;
		this.cena = cena;
		this.znizka = znizka;
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
	public double getCena() {
		return cena;
	}
	public void setCena(double cena) {
		this.cena = cena;
	}
	public double getZnizka() {
		return znizka;
	}
	public void setZnizka(double znizka) {
		this.znizka = znizka;
	}
	public Producent getProducent() {
		return producent;
	}
	public void setProducent(Producent producent) {
		this.producent = producent;
	}
}
