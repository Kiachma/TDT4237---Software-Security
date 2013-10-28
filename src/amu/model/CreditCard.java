package amu.model;

import java.util.Calendar;

import org.jasypt.util.text.BasicTextEncryptor;

public class CreditCard {

    private Integer id;
    private Customer customer;
    private String creditCardNumber;
    private Calendar expiryDate;
    private String cardholderName;

    public CreditCard(Integer id, Customer customer, String creditCardNumber, Calendar expiryDate, String cardholderName) {
        this.id = id;
        this.customer = customer;
        this.creditCardNumber = creditCardNumber;
        this.expiryDate = expiryDate;
        this.cardholderName = cardholderName;
    }

    public CreditCard(Customer customer, String creditCardNumber, Calendar expiryDate, String cardholderName) {
        this.id = null;
        this.customer = customer;
        this.creditCardNumber = encryptCCNumber(creditCardNumber);
        this.expiryDate = expiryDate;
        this.cardholderName = cardholderName;
    }

    public Integer getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getEncryptedCreditCardNumber() {
        return creditCardNumber;
    }
    
    public String getDecryptedCreditCardNumber() {
        return decryptCCNumber(creditCardNumber);
    }

    public String getMaskedCreditCardNumber() {
	String deCryptedCC = getDecryptedCreditCardNumber();
        StringBuilder maskedCreditCardNumber = new StringBuilder(deCryptedCC.length());
        for (int i = 0; i < deCryptedCC.length(); i++)
        {
            if (i >= deCryptedCC.length() - 4) {
                maskedCreditCardNumber.append(deCryptedCC.charAt(i));
            } else {
                maskedCreditCardNumber.append('*');
            }
        }
        return maskedCreditCardNumber.toString();
    }

    public Calendar getExpiryDate() {
        return expiryDate;
    }

    public String getCardholderName() {
        return cardholderName;
    }
    
    private String encryptCCNumber(String ccNumber){
    	BasicTextEncryptor decimalNumberEncryptor = new BasicTextEncryptor();
    	decimalNumberEncryptor.setPassword(getCustomer().getPassword());
	return decimalNumberEncryptor.encrypt(ccNumber);
    } 
    
    private String decryptCCNumber(String ccNumber){
    	BasicTextEncryptor decimalNumberEncryptor = new BasicTextEncryptor();
    	decimalNumberEncryptor.setPassword(getCustomer().getPassword());
	return decimalNumberEncryptor.decrypt(ccNumber);
    }

    public void setCreditCardNumber(String enCryptedNumber) {
	this.creditCardNumber=enCryptedNumber;
    }
}
