package br.com.project.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class Functions {

	public static String formatMoney(String value) {

		Locale brl = new Locale("pt", "BR");

//		Currency brls = Currency.getInstance(brl);

		NumberFormat brlFormat = NumberFormat.getCurrencyInstance(brl);

		return brlFormat.format(Double.parseDouble(value));

	}
}
