/*
   Copyright 2011 Sander Kooijmans

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package nl.gogognome.lib.text;

import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

/**
 * This class offers methods to format <code>Amount</code>s in a number of ways.
 *
 * @author Sander Kooijmans
 */
public class AmountFormat
{
    /** The locale used to format amounts. */
    private Locale locale;

    /**
     * Constructor.
     * @param locale the locale used to format amounts
     */
    public AmountFormat(Locale locale) {
        this.locale = locale;
    }

	private final static String EMPTY_STRING = "";

	private final static HashMap<String, Currency> SYMBOL_TO_CURRENCY_MAP = new HashMap<String, Currency>();

	static {
	    SYMBOL_TO_CURRENCY_MAP.put("EUR", Currency.getInstance("EUR"));
	}

	/**
	 * Formats an amount using this <code>WidgetFactory</code>'s locale without
	 * a currency.
	 * @param amount the amount to be formatted
	 * @return the formatted amount
	 */
	public String formatAmountWithoutCurrency(Amount amount)
	{
	    return formatAmount(amount, EMPTY_STRING);
	}

	/**
	 * Formats an amount using this <code>WidgetFactory</code>'s locale with
	 * the currency specified in the database.
	 * @param amount the amount to be formatted
	 * @return the formatted amount
	 */
	public String formatAmount(Amount amount) {
		if (amount == null) {
			return "";
		}

        return formatAmount(amount, amount.getCurrency().getSymbol(locale));
	}

	/**
	 * Formats an amount using this <code>WidgetFactory</code>'s locale.
	 * @param amount the amount to be formatted
     * @param currencySymbol the currency symbol used as prefix of the formatted amount
	 * @return the formatted amount
	 */
	public String formatAmount(Amount amount, String currencySymbol) {
		if (amount == null) {
			return "";
		}

        StringBuilder sb = new StringBuilder(amount.toString());
        int firstDigitIndex = 0;
        if (sb.charAt(0) == '-')
        {
            sb.insert(1, "/- ");
            firstDigitIndex += 4;
        }

        int numFractionDigits = amount.getCurrency().getDefaultFractionDigits();
        if (numFractionDigits > 0)
        {
            DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
            int fractionDigitIndex = sb.length() - numFractionDigits;
            if (fractionDigitIndex <= firstDigitIndex)
            {
                // There are not enough digits present to insert the fraction separator.
                // Add extra zeros.
                while(fractionDigitIndex <= firstDigitIndex)
                {
                    sb.insert(firstDigitIndex, '0');
                    fractionDigitIndex++;
                }
            }
            sb.insert(fractionDigitIndex, dfs.getDecimalSeparator());
        }

        if (currencySymbol != null && currencySymbol.length() > 0)
        {
            sb.insert(firstDigitIndex, ' ');
            sb.insert(firstDigitIndex, currencySymbol);
        }

        return sb.toString();
	}

    /**
     * Parses a string containing an amount. The format of the amount is
     * <code>[MINUS]CUR d+[DS]d*</code> where <code>d</code> represents a digit,
     * <code>MINUS</code> stands for "-" or "-/- " (note the extra space at the
     * end), <code>CUR</code> is the currency symbol and
     * <code>DS</code> is the decimal separator.
     *
     * @param amountString the string containing an amount
     * @return the amount
     * @throws ParseException if the string does not contain a valid amount
     */
    public Amount parse(String amountString)
    	throws ParseException
    {
        StringBuilder sb = new StringBuilder(amountString);
        try
        {
	        int index = 0;
	        if (amountString.startsWith("-/- "))
	        {
	            sb.replace(0, 4, "-");
	            index++;
	        }

	        int currencyIndex = index;
	        StringBuilder currencySymbol = new StringBuilder(10);
	        while (Character.isLetter(sb.charAt(index)))
	        {
	            currencySymbol.append(sb.charAt(index));
	            index++;
	        }

	        Currency currency = getCurrency(currencySymbol.toString());
	        if (currency == null)
	        {
	            throw new ParseException("Unknown currency symbol found in \""
	                    + amountString + "\"", 0);
	        }
	        sb.delete(currencyIndex, index + 1);
	        return new Amount(sb.toString(), currency, locale);
        }
        catch (Exception e)
        {
            if (e instanceof ParseException)
            {
                throw (ParseException)e;
            }
            else
            {
                throw new ParseException(e.toString(), 0);
            }
        }
    }

    /**
     * Parses a string containing an amount. The format of the amount is
     * <code>[MINUS]d+[DS]d*</code> where <code>d</code> represents a digit,
     * <code>MINUS</code> stands for "-" or "-/- " (note the extra space at the
     * end), and
     * <code>DS</code> is the decimal separator.
     *
     * @param amountString the string containing an amount
     * @param currency the currency of the amount
     * @return the amount
     * @throws ParseException if the string does not contain a valid amount
     */
    public Amount parse(String amountString, Currency currency) throws ParseException {
        StringBuilder sb;
        try {
        	sb = new StringBuilder(amountString);
	        int index = 0;
	        if (amountString.startsWith("-/- ")) {
	            sb.replace(0, 4, "-");
	            index++;
	        }

	        return new Amount(sb.toString(), currency, locale);
        }
        catch (Exception e) {
            if (e instanceof ParseException) {
                throw (ParseException)e;
            } else {
                throw new ParseException(e.toString(), 0);
            }
        }
    }

    /**
     * Gets the <code>Currency</code> that corresponds to the specified symbol.
     * @param symbol the symbol
     * @return the currency or <code>null</code> if no currency was found.
     */
    private Currency getCurrency(String symbol) {
        return SYMBOL_TO_CURRENCY_MAP.get(symbol);
    }
}
