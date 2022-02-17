package com.alquimidia;

import java.util.HashMap;

import com.alquimidia.easyInner.entity.Inner;
import com.topdata.EasyInner;

public class Init {

	static boolean parar = false;
	private static HashMap<Integer, Integer> ListaInners;

	public static void main(String[] args) throws InterruptedException {
		String arch = System.getProperty("sun.arch.data.model");
		ListaInners = new HashMap<Integer, Integer>();

		if (arch.equalsIgnoreCase("32")) {
			startMachine();
			ListaInners.put(1, 1);
		} else {
			System.err.println("\nThis project only works on x86");
		}
	}

	private static void startMachine() throws InterruptedException {
		Integer ret = 0;
		EasyInner.FecharPortaComunicacao();
		EasyInner.DefinirTipoConexao(2);
		ret = EasyInner.AbrirPortaComunicacao(3570);
		if (ret == EasyInner.RET_COMANDO_OK) {
			System.out.println("Porta Aberta");
			machine();
		} else {
			System.err.println("\nErro ao tentar abrir a porta de comunicação.");
		}
	}

	private static void machine() throws InterruptedException {
		System.out.println("parei aki");

	}

}
