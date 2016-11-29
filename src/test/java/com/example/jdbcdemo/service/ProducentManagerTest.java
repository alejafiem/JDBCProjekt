package com.example.jdbcdemo.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.jdbcdemo.domain.Producent;
import com.example.jdbcdemo.domain.Produkt;

public class ProducentManagerTest {
	
	private ProducentManager pm = new ProducentManager();
	
	private Producent producent1;
	private Producent producent2;
	private Producent producent3;
	
	private Produkt Produkt1;
	private Produkt Produkt2;
	private Produkt Produkt3;
	
	@Before
	public void initialize() {
		producent1 = new Producent("Sanitex", "Gdansk", "999-999-999", 9999999);
		producent2 = new Producent("Wannex", "Gdansk", "666-666-666", 1234567);
		producent3 = new Producent("Sanitozord", "Warszawa", "333-333-333", 9876543);
		
		Produkt1 = new Produkt("Kabina Prysznicowa", 150.99, 0.2);
		Produkt2 = new Produkt("Wanna", 190.99, 0.05);
		Produkt3 = new Produkt("Bateria", 80.99, 0);
		
		pm.deleteAllProducent();
		pm.deleteAllProdukt();
	}
	
	
	@Test
	public void checkConnection() {
		assertNotNull(pm.getConnection());
	}
	
	@Test
	public void checkInsertProducent() {
		assertEquals(0, pm.findAllProducent().size());
		
		assertEquals(1, pm.insert(producent1));
		assertEquals(1, pm.insert(producent2));
		assertEquals(1, pm.insert(producent3));
		
		assertEquals(3, pm.findAllProducent().size());
	}
	
	@Test
	public void checkInsertProdukt() {
		assertEquals(0, pm.findAllProdukt().size());
		
		assertEquals(1, pm.insert(Produkt1));
		assertEquals(1, pm.insert(Produkt2));
		assertEquals(1, pm.insert(Produkt3));
		
		assertEquals(3, pm.findAllProdukt().size());
	}
	
	@Test
	public void checkUpdateProducent() {
		pm.insert(producent1);
		
		assertEquals("Gdansk", producent1.getMiasto());
		
		producent1.setMiasto("Krakow");
		
		pm.update(producent1);
		
		Producent retrieved = pm.findAllProducent().get(0);
		
		assertEquals("Krakow", retrieved.getMiasto());
	}
	
	@Test
	public void checkUpdateProdukt() {
		pm.insert(Produkt1);
		
		assertEquals(150.99, Produkt1.getCena(), 0);
		
		Produkt1.setCena(99.9);
		
		pm.update(Produkt1);
		
		Produkt retrieved = pm.findAllProdukt().get(0);
		
		assertEquals(99.9, retrieved.getCena(), 0);
	}
	
	@Test
	public void checkReloadProducent() {
		pm.insert(producent1);
		
		assertEquals(9999999,  producent1.getNip());
		
		producent1.setNip(7777777);
		
		assertEquals(7777777,  producent1.getNip());
		
		pm.reload(producent1);
		
		assertEquals(9999999,  producent1.getNip());
	}
	
	@Test
	public void checkReloadProdukt() {
		pm.insert(Produkt1);
		
		assertEquals(0.2, Produkt1.getZnizka(), 0);
		
		Produkt1.setZnizka(1);
		
		assertEquals(1, Produkt1.getZnizka(), 0);
		
		pm.reload(Produkt1);
		
		assertEquals(0.2, Produkt1.getZnizka(), 0);
	}
	
	@Test
	public void checkDeleteProducent() {
		assertEquals(0, pm.findAllProducent().size());
		
		pm.insert(producent1);
		pm.insert(producent2);
		pm.insert(producent3);
		
		assertEquals(3, pm.findAllProducent().size());
		
		pm.delete(producent2);
		
		assertEquals(2, pm.findAllProducent().size());
		
		pm.delete(producent2);
		
		assertEquals(2, pm.findAllProducent().size());
		
		pm.deleteAllProducent();
		
		assertEquals(0, pm.findAllProducent().size());
	}
	
	@Test
	public void checkDeleteProdukt() {
		assertEquals(0, pm.findAllProdukt().size());
		
		pm.insert(Produkt1);
		pm.insert(Produkt2);
		pm.insert(Produkt3);
		
		assertEquals(3, pm.findAllProdukt().size());
		
		pm.delete(Produkt1);
		
		assertEquals(2, pm.findAllProdukt().size());
		
		pm.delete(Produkt1);
		
		assertEquals(2, pm.findAllProdukt().size());
		
		pm.deleteAllProducent();
		
		assertEquals(0, pm.findAllProdukt().size());
	}
	
	@Test
	public void checkFindProducentByName() {
		pm.insert(producent1);
		pm.insert(producent2);
		pm.insert(producent3);
		
		Producent retrieved = pm.findProducentByName("Wannex");
		
		assertNotNull(retrieved);
		
		assertEquals("Wannex", retrieved.getNazwa());
		assertEquals("Gdansk", retrieved.getMiasto());
		assertEquals("666-666-666", retrieved.getNrTel());
		assertEquals(1234567,  retrieved.getNip());
	}
	
	@Test
	public void checkFindProduktByName() {
		pm.insert(Produkt1);
		pm.insert(Produkt2);
		pm.insert(Produkt3);
		
		Produkt retrieved = pm.findProduktByName("Bateria");
		
		assertNotNull(retrieved);
		
		assertEquals("Bateria", retrieved.getNazwa());
		assertEquals(80.99, retrieved.getCena(), 0);
		assertEquals(0, retrieved.getZnizka(), 0);
	}
	
	@Test
	public void checkFindAllProducent() {
		assertEquals(0, pm.findAllProducent().size());
		
		pm.insert(producent1);
		
		assertEquals(1, pm.findAllProducent().size());
		
		pm.insert(producent2);
		pm.insert(producent3);
		
		assertEquals(3, pm.findAllProducent().size());
		
		Producent retrieved = pm.findAllProducent().get(1);
		
		assertNotNull(retrieved);
		
		assertEquals("Wannex", retrieved.getNazwa());
		assertEquals("Gdansk", retrieved.getMiasto());
		assertEquals("666-666-666", retrieved.getNrTel());
		assertEquals(1234567,   retrieved.getNip());
		
		for (int i = 0; i < 10; i++) {
			pm.insert(producent1);
		}
		
		assertEquals(13, pm.findAllProducent().size());
	}
	
	@Test
	public void checkFindAllProdukt() {
		assertEquals(0, pm.findAllProdukt().size());
		
		pm.insert(Produkt1);
		pm.insert(Produkt2);
		
		assertEquals(2, pm.findAllProdukt().size());
		
		pm.insert(Produkt3);
		
		assertEquals(3, pm.findAllProdukt().size());
		
		Produkt retrieved = pm.findAllProdukt().get(2);
		
		assertNotNull(retrieved);
		
		assertEquals("Bateria", retrieved.getNazwa());
		assertEquals(80.99, retrieved.getCena(), 0);
		assertEquals(0, retrieved.getZnizka(), 0);
		
		for (int i = 0; i < 13; i++) {
			pm.insert(Produkt2);
		}
		
		assertEquals(16, pm.findAllProdukt().size());
	}
	
	@Test
	public void checkFindProduktOf() {
		pm.insert(producent1);
		pm.insert(producent2);
		pm.insert(producent3);
		
		Produkt2.setProducent(producent3);
		Produkt3.setProducent(producent3);
		
		pm.insert(Produkt1);
		pm.insert(Produkt2);
		pm.insert(Produkt3);
		
		List<Produkt> Produkty = pm.findProduktOf(producent1);
		
		assertEquals(0, Produkty.size());
		
		Produkty = pm.findProduktOf(producent3);
		
		assertEquals(2, Produkty.size());
		
		Produkt retrieved = Produkty.get(0);
		
		assertEquals("Wanna", retrieved.getNazwa());
		assertEquals(190.99, retrieved.getCena(), 0);
		assertEquals(0.05, retrieved.getZnizka(), 0);
	}
	
	@Test
	public void checkAttach() {
		pm.insert(producent1);
		pm.insert(Produkt1);
		
		Produkt retrieved1 = pm.findAllProdukt().get(0);
		
		assertNull(retrieved1.getProducent());
		
		pm.attach(producent1, Produkt1);
		
		Produkt retrieved2 = pm.findAllProdukt().get(0);
		
		assertNotNull(retrieved2.getProducent());
		
		assertEquals("Kabina Prysznicowa", retrieved2.getProducent().getNazwa());
		assertEquals("Gdansk", retrieved2.getProducent().getMiasto());
		assertEquals("999-999-999", retrieved2.getProducent().getNrTel());
		assertEquals(9999999,  retrieved2.getProducent().getNip());
	}
	
	@Test
	public void checkDetach() {
		pm.insert(producent1);
		pm.insert(Produkt1);
		
		Produkt retrieved1 = pm.findAllProdukt().get(0);
		
		assertNull(retrieved1.getProducent());
		
		pm.attach(producent1, Produkt1);
		
		Produkt retrieved2 = pm.findAllProdukt().get(0);
		
		assertNotNull(retrieved2.getProducent());
		
		pm.detach(Produkt1);
		
		Produkt retrieved3 = pm.findAllProdukt().get(0);
		
		assertNull(retrieved3.getProducent());
	}
}
