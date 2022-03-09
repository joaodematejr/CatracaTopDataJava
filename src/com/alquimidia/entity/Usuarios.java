package com.alquimidia.entity;

import java.util.List;

public class Usuarios {
	private int codigoUsuario;
	private String Usuario;
	public int faixa;

	private List<Usuarios> listUsuarios;

	public int getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(int codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getUsuario() {
		return Usuario;
	}

	public void setUsuario(String Usuario) {
		this.Usuario = Usuario;
	}

	public int getFaixa() {
		return faixa;
	}

	public void setFaixa(int faixa) {
		this.faixa = faixa;
	}

	public List<Usuarios> getUsuarios() {
		return listUsuarios;
	}

	public void setUsuarios(List<Usuarios> listUsuarios) {
		this.listUsuarios = listUsuarios;
	}
}
