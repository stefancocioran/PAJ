package com.luxoft.bankapp.tests;

import com.luxoft.bankapp.domain.*;
import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.service.BankService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Test2 {

	@Test
	public void testBank() throws ClientExistsException {
		Bank bank = new Bank();
		Client client1 = new Client("Smith John", Gender.MALE, "Bucuresti");
		client1.addAccount(new SavingAccount(1, 1000.0));
		client1.addAccount(new CheckingAccount(2, 1000.0, 100.0));

		Client client2 = new Client("Smith Michelle", Gender.FEMALE, "Iasi");
		client2.addAccount(new SavingAccount(3, 2000.0));
		client2.addAccount(new CheckingAccount(4, 1500.0, 200.0));

		BankService.addClient(bank, client1);
		BankService.addClient(bank, client2);

		assertEquals(2, bank.getClients().size());
		assertEquals(2, bank.getPrintedClients());
		assertEquals(2, bank.getEmailedClients());
		assertEquals(2, bank.getDebuggedClients());
	}

}

