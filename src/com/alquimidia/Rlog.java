package com.alquimidia;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Rlog {
	private final Logger logger = Logger.getLogger(Rlog.class.getName());
	private FileHandler fh = null;

	public Rlog() {
		SimpleDateFormat format = new SimpleDateFormat("dd_MM_yyyy_HHmmss");
		try {
			fh = new FileHandler("C:/Alquimidia/" + format.format(Calendar.getInstance().getTime()) + ".log");
		} catch (Exception e) {
			e.printStackTrace();
		}

		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);
	}

	public void doLogging(String string) {
		logger.log(Level.ALL, string, string);
	}
}