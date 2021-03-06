package com.example.jdbcdemo.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.example.jdbcdemo.domain.Producent;
import com.example.jdbcdemo.domain.Produkt;

public class ProducentManager {
	private Connection connection;
	private Statement statement;
	
	private PreparedStatement insertProducentStmt;
	private PreparedStatement insertProduktStmt;
	private PreparedStatement updateProducentStmt;
	private PreparedStatement updateProduktStmt;
	private PreparedStatement reloadProducentStmt;
	private PreparedStatement reloadProduktStmt;
	private PreparedStatement deleteProducentStmt;
	private PreparedStatement deleteProduktStmt;
	private PreparedStatement deleteProducentsProduktStmt;
	private PreparedStatement deleteAllProducentStmt;
	private PreparedStatement deleteAllProduktStmt;
	private PreparedStatement findProducentByNameStmt;
	private PreparedStatement findProduktByNameStmt;
	private PreparedStatement findAllProducentsStmt;
	private PreparedStatement findAllProduktStmt;
	private PreparedStatement findProduktOfStmt;
	
	private String url = "jdbc:hsqldb:hsql://localhost/workdb";
	
	private String createTableProducent = 
			"CREATE TABLE Producent(" +
					"id int GENERATED BY DEFAULT AS IDENTITY, " +
					"nazwa varchar(20), " +
					"miasto varchar(15), " +
					"nrTel varchar(15), " +
					"nip int, " +
					"PRIMARY KEY (id))";
	
	private String createTableProdukt = 
			"CREATE TABLE Produkt(" +
					"id int GENERATED BY DEFAULT AS IDENTITY, " +
					"nazwa varchar(30), " +
					"cena DOUBLE, " +
					"znizka DOUBLE, " +
					"producentId int, " +
					"PRIMARY KEY (id), " +
					"FOREIGN KEY (producentId) REFERENCES Producent(id))";
	
	public ProducentManager() {
		try {
			connection = DriverManager.getConnection(url);
			statement = connection.createStatement();

			ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
			boolean tableProducentExists = false;
			boolean tableProduktExists = false;
			while (rs.next()) {
				if ("Producent".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
					tableProducentExists = true;
				}
				if ("Produkt".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
					tableProduktExists = true;
				}
			}

			if (!tableProducentExists)
				statement.executeUpdate(createTableProducent);
			if (!tableProduktExists)
				statement.executeUpdate(createTableProdukt);
			
			insertProducentStmt = connection
					.prepareStatement("INSERT INTO Producent (nazwa, miasto, nrTel, nip) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			insertProduktStmt = connection
					.prepareStatement("INSERT INTO Produkt (nazwa, cena, znizka, producentId) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			updateProducentStmt = connection
					.prepareStatement("UPDATE Producent SET nazwa=?, miasto=?, nrTel=?, nip=? WHERE id=?");
			updateProduktStmt = connection
					.prepareStatement("UPDATE Produkt SET nazwa=?, cena=?, znizka=?, producentId=? WHERE id=?");
			reloadProducentStmt = connection
					.prepareStatement("SELECT * FROM Producent WHERE id=?");
			reloadProduktStmt = connection
					.prepareStatement("SELECT t.id, t.nazwa, t.cena, t.znizka, p.id as pid, p.nazwa, p.miasto, p.nrTel, p.nip FROM Produkt t LEFT JOIN Producent p ON p.id=t.producentId WHERE t.id=?");
			deleteProducentStmt = connection
					.prepareStatement("DELETE FROM Producent WHERE id=?");
			deleteProduktStmt = connection
					.prepareStatement("DELETE FROM Produkt WHERE id=?");
			deleteProducentsProduktStmt = connection
					.prepareStatement("DELETE FROM Produkt WHERE producentId=?");
			deleteAllProducentStmt = connection
					.prepareStatement("DELETE FROM Producent");
			deleteAllProduktStmt = connection
					.prepareStatement("DELETE FROM Produkt");
			findProducentByNameStmt = connection
					.prepareStatement("SELECT * FROM Producent WHERE nazwa=?");
			findProduktByNameStmt = connection
					.prepareStatement("SELECT t.id, t.nazwa, t.cena, t.znizka, p.id as pid, p.nazwa, p.miasto, p.nrTel, p.nip FROM Produkt t LEFT JOIN Producent p ON p.id=t.producentId WHERE t.nazwa=?");
			findAllProducentsStmt = connection
					.prepareStatement("SELECT * FROM Producent");
			findAllProduktStmt = connection
					.prepareStatement("SELECT t.id, t.nazwa, t.cena, t.znizka, p.id as pid, p.nazwa, p.miasto, p.nrTel, p.nip FROM Produkt t LEFT JOIN Producent p ON p.id=t.producentId");
			findProduktOfStmt = connection
					.prepareStatement("SELECT t.id, t.nazwa, t.cena, t.znizka, p.id as pid, p.nazwa, p.miasto, p.nrTel, p.nip FROM Produkt t LEFT JOIN Producent p ON p.id=t.producentId WHERE t.producentId=?");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	// CRUD
	public int insert(Producent p) {
		try {
			insertProducentStmt.setString(1, p.getNazwa());
			insertProducentStmt.setString(2, p.getMiasto());
			insertProducentStmt.setString(3, p.getNrTel());
			insertProducentStmt.setInt(4, p.getNip());
			
			int count = insertProducentStmt.executeUpdate();
			
			if (count > 0) {
				ResultSet generatedKeys = insertProducentStmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					p.setId(generatedKeys.getInt(1));
				}
			}
			
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	public int insert(Produkt t) {
		try {
			insertProduktStmt.setString(1, t.getNazwa());
			insertProduktStmt.setDouble(2, t.getCena());
			insertProduktStmt.setDouble(3, t.getZnizka());
			
			if (t.getProducent() == null)
				insertProduktStmt.setNull(4, Types.BIGINT);
			else
				insertProduktStmt.setInt(4, t.getProducent().getId());
			
			int count = insertProduktStmt.executeUpdate();
			
			if (count > 0) {
				ResultSet generatedKeys = insertProduktStmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					t.setId(generatedKeys.getInt(1));
				}
			}
			
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	public int update(Producent p) {
		try {
			updateProducentStmt.setString(1, p.getNazwa());
			updateProducentStmt.setString(2, p.getMiasto());
			updateProducentStmt.setString(3, p.getNrTel());
			updateProducentStmt.setInt(4, p.getNip());
			updateProducentStmt.setInt(5, p.getId());
			
			return updateProducentStmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	public int update(Produkt t) {
		try {
			updateProduktStmt.setString(1, t.getNazwa());
			updateProduktStmt.setDouble(2, t.getCena());
			updateProduktStmt.setDouble(3, t.getZnizka());
			updateProduktStmt.setInt(5, t.getId());
			
			if (t.getProducent() == null)
				updateProduktStmt.setNull(4, Types.INTEGER);
			else
				updateProduktStmt.setInt(4, t.getProducent().getId());
			
			return updateProduktStmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	public void reload(Producent p) {
		try {
			reloadProducentStmt.setInt(1, p.getId());
			
			ResultSet rs = reloadProducentStmt.executeQuery();
			
			if (rs.next()) {
				p.setNazwa(rs.getString("nazwa"));
				p.setMiasto(rs.getString("miasto"));
				p.setNrTel(rs.getString("nrTel"));
				p.setNip(rs.getInt("nip"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void reload(Produkt t) {
		try {
			reloadProduktStmt.setInt(1, t.getId());
			
			ResultSet rs = reloadProduktStmt.executeQuery();
			
			if (rs.next()) {
				t.setNazwa(rs.getString("nazwa"));
				t.setCena(rs.getDouble("cena"));
				t.setZnizka(rs.getDouble("znizka"));
				
				rs.getInt("pid");
				
				if (rs.wasNull()) {
					t.setProducent(null);
				} else {
					Producent p = new Producent();
					p.setId(rs.getInt("pid"));
					p.setNazwa(rs.getString("nazwa"));
					p.setMiasto(rs.getString("miasto"));
					p.setNrTel(rs.getString("nrTel"));
					p.setNip(rs.getInt("nip"));
					t.setProducent(p);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void delete(Producent p) {
		try {
			connection.setAutoCommit(false);
			
			deleteProducentsProduktStmt.setInt(1, p.getId());
			deleteProducentStmt.setInt(1, p.getId());
			deleteProducentsProduktStmt.execute();
			deleteProducentStmt.execute();
			
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void delete(Produkt t) {
		try {
			deleteProduktStmt.setInt(1, t.getId());
			deleteProduktStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteAllProducent() {
		try {
			connection.setAutoCommit(false);
			
			deleteAllProduktStmt.execute();
			deleteAllProducentStmt.execute();
			
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			e.printStackTrace();
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void deleteAllProdukt() {
		try {
			deleteAllProduktStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Producent findProducentByName(String name) {
		try {
			findProducentByNameStmt.setString(1, name);
			
			ResultSet rs = findProducentByNameStmt.executeQuery();
			
			if (rs.next()) {
				Producent p = new Producent();
				p.setId(rs.getInt("id"));
				p.setNazwa(rs.getString("nazwa"));
				p.setMiasto(rs.getString("miasto"));
				p.setNrTel(rs.getString("nrTel"));
				p.setNip(rs.getInt("nip"));
				
				return p;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public Produkt 	findProduktByName(String name) { 
		try {
			findProduktByNameStmt.setString(1, name);
			
			ResultSet rs = findProduktByNameStmt.executeQuery();
			
			if (rs.next()) {
				Produkt t = new Produkt();
				t.setId(rs.getInt("id"));
				t.setNazwa(rs.getString("nazwa"));
				t.setCena(rs.getDouble("cena"));
				t.setZnizka(rs.getDouble("znizka"));
				
				rs.getInt("pid");
				if (!rs.wasNull()) {
					Producent p = new Producent();
					p.setId(rs.getInt("pid"));
					p.setNazwa(rs.getString("nazwa"));
					p.setMiasto(rs.getString("miasto"));
					p.setNrTel(rs.getString("nrTel"));
					p.setNip(rs.getInt("nip"));
					t.setProducent(p);
				}
				
				return t;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public List<Producent> findAllProducent() { 
		List<Producent> producenci = new ArrayList<Producent>();
		
		try {
			ResultSet rs = findAllProducentsStmt.executeQuery();
			
			while (rs.next()) {
				Producent p = new Producent();
				p.setId(rs.getInt("id"));
				p.setNazwa(rs.getString("nazwa"));
				p.setMiasto(rs.getString("miasto"));
				p.setNrTel(rs.getString("nrTel"));
				p.setNip(rs.getInt("nip"));
				producenci.add(p);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return producenci;
	} 
	public List<Produkt> findAllProdukt() { 
		List<Produkt> Produkty = new ArrayList<Produkt>();
		
		try {
			ResultSet rs = findAllProduktStmt.executeQuery();
			
			while (rs.next()) {
				Produkt t = new Produkt();
				t.setId(rs.getInt("id"));
				t.setNazwa(rs.getString("nazwa"));
				t.setCena(rs.getDouble("cena"));
				t.setZnizka(rs.getDouble("znizka"));
				
				rs.getInt("pid");
				if (!rs.wasNull()) {
					Producent p = new Producent();
					p.setId(rs.getInt("pid"));
					p.setNazwa(rs.getString("nazwa"));
					p.setMiasto(rs.getString("miasto"));
					p.setNrTel(rs.getString("nrTel"));
					p.setNip(rs.getInt("nip"));
					t.setProducent(p);
				}
				
				Produkty.add(t);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Produkty;
	}
	
	// Relacje
	public List<Produkt> findProduktOf(Producent p) { 
		List<Produkt> Produkty = new ArrayList<Produkt>();
		
		try {
			findProduktOfStmt.setInt(1, p.getId());
			
			ResultSet rs = findProduktOfStmt.executeQuery();
			
			while (rs.next()) {
				Produkt t = new Produkt();
				t.setId(rs.getInt("id"));
				t.setNazwa(rs.getString("nazwa"));
				t.setCena(rs.getDouble("cena"));
				t.setZnizka(rs.getDouble("znizka"));
				
				t.setProducent(p);
				
				Produkty.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Produkty;
	}
	public void attach(Producent p, Produkt t) {
		t.setProducent(p);
		update(t);
	}
	public void detach(Produkt t) {
		t.setProducent(null);
		update(t);
	}
}
