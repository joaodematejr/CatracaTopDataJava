package com.alquimidia;

import java.io.IOException;

public class Init {

	public static void main(String[] args) throws InterruptedException, IOException {
		
		Rlog log = new Rlog();
		Machine machine = new Machine();
		String arch = System.getProperty("sun.arch.data.model");
		if (arch.equalsIgnoreCase("32")) {
			log.doLogging("Chamou função iniciar Catraca");
			machine.startMachine();
		} else {
			log.doLogging("Este projeto só funciona em x86");
			System.err.println("\nThis project only works on x86");
		}
	}

}
