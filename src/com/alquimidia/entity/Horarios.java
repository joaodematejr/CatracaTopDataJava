package com.alquimidia.entity;

import java.util.ArrayList;
import java.util.List;

public class Horarios {

	public int codigo;
	public byte faixa;
	public byte dia;
	public byte hora;
	public byte minuto;
	public byte horario;

	public static List MontarListaHorarios() {
		List<Horarios> ListaHorarios = new ArrayList();
		Horarios VHorario = new Horarios();
		VHorario.horario = 1;
		VHorario.dia = 1;
		VHorario.faixa = 1;
		VHorario.hora = 8;
		VHorario.minuto = 0;
		// HORARIO 1 DIA 1 SEGUNDA
		ListaHorarios.add(VHorario);

		VHorario = new Horarios();
		VHorario.horario = 1;
		VHorario.dia = 1;
		VHorario.faixa = 2;
		VHorario.hora = 12;
		VHorario.minuto = 0;
		// HORARIO 1 DIA 1 SEGUNDA
		ListaHorarios.add(VHorario);

		VHorario = new Horarios();
		VHorario.horario = 1;
		VHorario.dia = 1;
		VHorario.faixa = 3;
		VHorario.hora = 13;
		VHorario.minuto = 0;
		// HORARIO 1 DIA 1 SEGUNDA
		ListaHorarios.add(VHorario);

		VHorario = new Horarios();
		VHorario.horario = 1;
		VHorario.dia = 1;
		VHorario.faixa = 4;
		VHorario.hora = 18;
		VHorario.minuto = 0;
		// HORARIO 1 DIA 1 SEGUNDA
		ListaHorarios.add(VHorario);

		VHorario = new Horarios();
		VHorario.horario = 1;
		VHorario.dia = 2;
		VHorario.faixa = 1;
		VHorario.hora = 8;
		VHorario.minuto = 0;
		// HORARIO 1 DIA 2 SEGUNDA
		ListaHorarios.add(VHorario);

		VHorario = new Horarios();
		VHorario.horario = 1;
		VHorario.dia = 2;
		VHorario.faixa = 2;
		VHorario.hora = 12;
		VHorario.minuto = 0;
		// HORARIO 1 DIA 2 SEGUNDA
		ListaHorarios.add(VHorario);

		VHorario = new Horarios();
		VHorario.horario = 1;
		VHorario.dia = 2;
		VHorario.faixa = 3;
		VHorario.hora = 13;
		VHorario.minuto = 0;
		// HORARIO 1 DIA 2 SEGUNDA
		ListaHorarios.add(VHorario);

		VHorario = new Horarios();
		VHorario.horario = 1;
		VHorario.dia = 2;
		VHorario.faixa = 4;
		VHorario.hora = 18;
		VHorario.minuto = 0;
		// HORARIO 1 DIA 2 SEGUNDA
		ListaHorarios.add(VHorario);
		return ListaHorarios;
	}
}
