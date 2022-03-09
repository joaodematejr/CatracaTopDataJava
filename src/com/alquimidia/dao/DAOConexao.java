package com.alquimidia.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DAOConexao {
	private static Connection Conn;

	public static Connection getConn() {
		return Conn;
	}

	public void setConn(Connection Conn) {
		this.Conn = Conn;
	}

	private static String pathDatabase;

	public static Connection ConectarBase() throws FileNotFoundException, IOException, ClassNotFoundException {
		try {
			if (Conn == null) {
				// Getting the path of the database
				File fi = new File(pathDatabase = System.getenv("ProgramFiles")
						+ "\\SDK EasyInner\\BaseExemplos\\SDK_Exemplos.mdb");
				FileReader file = null;

				if (fi.exists() == false) {
					file = new FileReader(System.getProperty("user.dir") + "\\caminhoDB.txt");
					BufferedReader fileReader = new BufferedReader(file);
					pathDatabase = fileReader.readLine();
				}

				String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ= " + pathDatabase;
				Conn = DriverManager.getConnection(database, "", "");
			}
			if (Conn.isClosed()) {
				Conn = DriverManager.getConnection(
						"jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + pathDatabase, "", "");
			}
		} catch (SQLException ex) {
			System.out.println(ex);
		}
		return Conn;
	}
}
