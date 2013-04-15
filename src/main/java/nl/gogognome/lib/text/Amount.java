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

import java.math.BigInteger;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

/**
 * This class represents amounts. It should be used instead of floats, doubles,
 * ints or longs, since floats and doubles suffer from rounding differences
 * and all of them lack checks for overflows.
 *
 * @author Sander Kooijmans
 */
public class Amount
{
    /** The currency of this amount. */
    private Currency currency;

    /** Represents the amount in cents. */
    private BigInteger amount;

    /**
     * Maps a <code>Currency</code> to an <code>Amount</code> that represents
     * the value zero in that currency.
     */
    private static HashMap<Currency, Amount> currencyToZeroMap = new HashMap<Currency, Amount>();

    /**
     * Constructs an amount.
     * @param amount a string representation of the amount
     * @param currency the currency of the amount
     */
    protected Amount(String amount, Currency currency, Locale locale)
    {
        this.currency = currency;

        // remove fraction separator from string
        int numFractionDigits = currency.getDefaultFractionDigits();
        if (numFractionDigits > 0)
        {
            DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
            String fractionSeparator = Character.toString(dfs.getDecimalSeparator());
            StringBuffer sb = new StringBuffer(amount);
            int index = sb.indexOf(fractionSeparator);
            if (index == -1)
            {
                index = sb.length();
                sb.append(fractionSeparator);
            }

            // Append as many zeros as needed to let sb have exactly
            // numFractionDigits digits.
            while (numFractionDigits > (sb.length() - index - 1))
            {
                sb.append('0');
            }

            sb.deleteCharAt(index);
            amount = sb.toString();
        }
        this.amount = new BigInteger(amount);
    }

    private Amount(BigInteger amount, Currency currency)
    {
        this.amount = amount;
        this.currency = currency;
    }

    /**
     * Gets the currency of this amount.
     * @return the currency
     */
    public Currency getCurrency()
    {
        return currency;
    }

    /**
     * Gets the amount that represents zero in the specified currency.
     * @param currency the currency
     * @return an amount that represents zero in the specified currency
     */
    public static Amount getZero(Currency currency)
    {
        Amount result = currencyToZeroMap.get(currency);
        if (result == null)
        {
            result = new Amount(new BigInteger("0"), currency);
            currencyToZeroMap.put(currency, result);
        }
        return result;
    }

    public Amount add(Amount that)
    {
        return new Amount(this.amount.add(that.amount), currency);
    }

    public Amount subtract(Amount that)
    {
        return new Amount(this.amount.subtract(that.amount), currency);
    }

    public Amount divide(int val)
    {
        return new Amount(this.amount.divide(new BigInteger(Integer.toString(val))), currency);
    }

    public Amount multiply(int val) {
        return new Amount(this.amount.multiply(new BigInteger(Integer.toString(val))), currency);
    }

    public Amount negate()
    {
        return new Amount(amount.negate(), currency);
    }

    public int compareTo(Amount that)
    {
        if (this.currency.equals(that.currency))
        {
            return amount.compareTo(that.amount);
        }
        else
        {
            throw new IllegalArgumentException("The amounts have different currencies!");
        }
    }

    /**
     * Checks whether this amount is positive.
     * @return <code>true</code> if this amount is positive; <code>false</code> otherwise.
     */
    public boolean isPositive()
    {
        return amount.signum() == 1;
    }

    /**
     * Checks whether this amount is negative.
     * @return <code>true</code> if this amount is negative; <code>false</code> otherwise.
     */
    public boolean isNegative()
    {
        return amount.signum() == -1;
    }

    /**
     * Checks whether this amount is zero.
     * @return <code>true</code> if this amount is zero; <code>false</code> otherwise
     */
    public boolean isZero() {
        return amount.signum() == 0;
    }

    /**
     * Gets a string representation of this amount.
     * The string representation will be the result of the underlying
     * <code>BigInteger</code>'s <code>toString()</code> method.
     * @return a string representation of this amount
     */
    @Override
	public String toString()
    {
        return amount.toString();
    }

    /**
     * Checks whether this instance is equal to another instance.
     * @param o the other instance
     * @return <code>true</code> if this instance is equal to <code>o</code>;
     *          <code>false</code> otherwise
     */
    @Override
	public boolean equals(Object o) {
        if (o instanceof Amount) {
            Amount that = (Amount) o;
            return this.currency.equals(that.currency) &&
            	this.amount.equals(that.amount);
        } else {
            return false;
        }
    }

    @Override
	public int hashCode() {
        return currency.hashCode() + amount.hashCode();
    }
}
