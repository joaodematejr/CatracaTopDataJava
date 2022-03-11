package com.alquimidia;

import java.io.IOException;

public class Init {

	public static void main(String[] args) throws InterruptedException, IOException {
		Machine machine = new Machine();
		Request request = new Request();
		String arch = System.getProperty("sun.arch.data.model");
		if (arch.equalsIgnoreCase("32")) {
			machine.startMachine();
			//request.post();
		} else {
			System.err.println("\nThis project only works on x86");
		}
	}

}
