/**
 *
 */
package nl.gogognome.lib.util;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for currencies.
 *
 * @author Sander Kooijmans
 */
public class CurrencyUtil {

	private final static Logger LOGGER = LoggerFactory.getLogger(CurrencyUtil.class);

	private CurrencyUtil() {
	}

	public static List<Currency> getAllCurrencies() {
		TreeSet<String> currencyCodes = new TreeSet<String>();
		for (Locale locale : Locale.getAvailableLocales()) {
			try {
				if (locale.getCountry() != null && locale.getCountry().length() == 2) {
					Currency currency = Currency.getInstance(locale);
					currencyCodes.add(currency.getCurrencyCode());
				}
			} catch (Exception e) {
				LOGGER.warn("Could not get currency for locale " + locale, e);
			}
		}

		List<Currency> currencies = new ArrayList<Currency>(currencyCodes.size());
		for (String currencyCode : currencyCodes) {
			currencies.add(Currency.getInstance(currencyCode));
		}
		return currencies;
	}
}
