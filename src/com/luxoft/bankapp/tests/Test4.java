package com.luxoft.bankapp.tests;

import com.luxoft.bankapp.domain.Bank;
import com.luxoft.bankapp.domain.Client;
import com.luxoft.bankapp.domain.Gender;
import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.multithreading.EmailService;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class Test4 {

	@Test()
	public void testEmailService() throws ClientExistsException {
		Bank bank = new Bank();

		EmailService emailService = EmailService.getInstance();
		Client client1 = new Client("Marcel", Gender.MALE, "Oradea");
		Client client2 = new Client("Ana", Gender.FEMALE, "Bucuresti");
		Client client3 = new Client("Dan", Gender.MALE, "Vaslui");

		bank.addClient(client1);
		bank.addClient(client2);
		emailService.close();
		bank.addClient(client3);

		Assert.assertEquals("sendEmail called exactly twice", 2,
		                    emailService.getEmailQueue().getEmailsSent());

	}
}