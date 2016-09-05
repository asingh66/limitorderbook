package com.capone.lob.service;

import java.text.DecimalFormat;
import java.util.Date;

public class Utility {
	
	public static Long converDateToLong(Date d) {
		return d.getTime();
	}
	
	public static Double roundPrice(Double price) {
		return Math.round(price * 100.0) / 100.0;
	}

}
