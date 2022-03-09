
package com.alquimidia.dao;

import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alquimidia.entity.Usuarios;

public class DAOUsuarios {

	public static List<Usuarios> consultarUsuarios(int qtdUsuariosLenght)
			throws IOException, SQLException, FileNotFoundException, ClassNotFoundException {
		try {
			Statement stm = DAOConexao.ConectarBase().createStatement();
			String query = "SELECT * FROM ListaOffLine";
			ResultSet rsReader = stm.executeQuery(query);
			List<Usuarios> listUsers = new ArrayList<>();
			int count = 0;
			while (rsReader.next()) {
				Usuarios user = new Usuarios();
				user.setCodigoUsuario(rsReader.getInt("Codigo"));
				user.setUsuario(rsReader.getString("Cartao"));
				user.setFaixa(rsReader.getInt("Faixa"));
				listUsers.add(user);
				if (qtdUsuariosLenght != 0) {
					count++;
					if (count == qtdUsuariosLenght) {
						break;
					}
				}
			}

			DAOConexao.getConn().close();
			return listUsers;
		} catch (Exception ex) {
			System.out.println(ex);
			return null;
		}
	}
}
