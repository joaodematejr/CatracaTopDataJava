package com.alquimidia;

public class Init {

	public static void main(String[] args) throws InterruptedException {
		Machine machine = new Machine();
		String arch = System.getProperty("sun.arch.data.model");
		if (arch.equalsIgnoreCase("32")) {
			machine.startMachine();
		} else {
			System.err.println("\nThis project only works on x86");
		}
	}

}
