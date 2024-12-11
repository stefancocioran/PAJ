package com.luxoft.bankapp.main;

import com.luxoft.bankapp.domain.*;
import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.exceptions.NotEnoughFundsException;
import com.luxoft.bankapp.exceptions.OverdraftLimitExceededException;
import com.luxoft.bankapp.multithreading.EmailService;
import com.luxoft.bankapp.reports.BankReport;
import com.luxoft.bankapp.reports.BankReportI;
import com.luxoft.bankapp.service.BankService;

import java.util.*;

import static com.luxoft.bankapp.service.BankService.printMaximumAmountToWithdraw;

public class BankApplication {

	private static Bank bank;

	public static void main(String[] args) throws InterruptedException {
		bank = new Bank();

		modifyBank();
		printBalance();
		printMaximumAmountToWithdraw(bank);

		EmailService.getInstance().close();

		if (args.length > 0 && args[0].equalsIgnoreCase("-statistics"))
			displayStatistics(bank);
	}

	private static void displayStatistics(Bank bank) {
		BankReportI report = new BankReport();
		Scanner scanner = new Scanner(System.in);

		System.out.println("Statistics mode: Available commands are:");
		System.out.println(" - 'clients'    -> Show number of clients");
		System.out.println(" - 'accounts'   -> Show number of accounts");
		System.out.println(" - 'sum'        -> Show total sum in accounts");
		System.out.println(" - 'credits'    -> Show total bank credit sum");
		System.out.println(" - 'sortedClients' -> Show sorted clients");
		System.out.println(" - 'sortedAccounts' -> Show sorted accounts by " + "balance");
		System.out.println(" - 'clientsByCity' -> Show clients by city");
		System.out.println(" - 'customerAccounts' -> Show customer accounts");
		System.out.println(" - 'exit'        -> Exit the statistics mode");

		while (true) {
			System.out.print("> ");
			String command = scanner.nextLine().trim().toLowerCase();

			if ("exit".equalsIgnoreCase(command)) {
				System.out.println("Exiting statistics mode.");
				break;
			} else {
				handleCommand(command, report, bank);
			}
		}
	}

	private static void handleCommand(String command, BankReportI report,
	                                  Bank bank) {
		switch (command) {
			case "clients" -> System.out.println("Number of clients: " + report.getNumberOfClients(bank));
			case "accounts" -> System.out.println("Number of accounts: " + report.getNumberOfAccounts(bank));
			case "sum" -> System.out.println("Total sum in accounts: " + report.getTotalSumInAccounts(bank));
			case "credits" -> System.out.println("Total bank credit sum: " + report.getBankCreditSum(bank));
			case "sortedClients" -> {
				System.out.println("Clients sorted alphabetically:");
				SortedSet<Client> sortedClients =
						report.getClientsSorted(bank);
				sortedClients.forEach(client -> System.out.println("  - " + client.getName()));
			}
			case "sortedAccounts" -> {
				System.out.println("Accounts sorted by balance:");
				SortedSet<Account> sortedAccounts =
						report.getAccountsSortedBySum(bank);
				sortedAccounts.forEach(account -> System.out.println("  - " +
				                                                     "Balance" + ":" + " " + account.getBalance()));
			}
			case "clientsByCity" -> {
				System.out.println("Clients by city:");
				Map<String, List<Client>> clientsByCity =
						report.getClientsByCity(bank);
				clientsByCity.forEach((city, clients) -> {
					System.out.println("  City: " + city);
					clients.forEach(client -> System.out.println("    - " + client.getName()));
				});
			}
			case "customerAccounts" -> {
				System.out.println("Customer accounts:");
				Map<Client, Collection<Account>> customerAccounts =
						report.getCustomerAccounts(bank);
				customerAccounts.forEach((client, accounts) -> {
					System.out.println("  Client: " + client.getName());
					accounts.forEach(account -> System.out.println("    - " +
					                                               "Account " + "balance: " + account.getBalance()));
				});
			}
			default -> System.out.println("Unknown command");
		}
	}

	private static void modifyBank() {
		Client client1 = new Client("John", Gender.MALE, "New York");
		Account account1 = new SavingAccount(1, 100);
		Account account2 = new CheckingAccount(2, 100, 20);
		client1.addAccount(account1);
		client1.addAccount(account2);

		try {
			BankService.addClient(bank, client1);
		} catch (ClientExistsException e) {
			System.out.format("Cannot add an already existing client: %s%n",
			                  client1.getName());
		}

		account1.deposit(100);
		try {
			account1.withdraw(10);
		} catch (OverdraftLimitExceededException e) {
			System.out.format("Not enough funds for account %d, balance: %.2f,"
			                  + " overdraft: %.2f, tried to extract amount: %" + ".2f%n", e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
		} catch (NotEnoughFundsException e) {
			System.out.format("Not enough funds for account %d, balance: %.2f,"
			                  + " tried to extract amount: %.2f%n", e.getId(),
			                  e.getBalance(), e.getAmount());
		}

		try {
			account2.withdraw(90);
		} catch (OverdraftLimitExceededException e) {
			System.out.format("Not enough funds for account %d, balance: %.2f,"
			                  + " overdraft: %.2f, tried to extract amount: %" + ".2f%n", e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
		} catch (NotEnoughFundsException e) {
			System.out.format("Not enough funds for account %d, balance: %.2f,"
			                  + " tried to extract amount: %.2f%n", e.getId(),
			                  e.getBalance(), e.getAmount());
		}

		try {
			account2.withdraw(100);
		} catch (OverdraftLimitExceededException e) {
			System.out.format("Not enough funds for account %d, balance: %.2f,"
			                  + " overdraft: %.2f, tried to extract amount: %" + ".2f%n", e.getId(), e.getBalance(), e.getOverdraft(), e.getAmount());
		} catch (NotEnoughFundsException e) {
			System.out.format("Not enough funds for account %d, balance: %.2f,"
			                  + " tried to extract amount: %.2f%n", e.getId(),
			                  e.getBalance(), e.getAmount());
		}

		try {
			BankService.addClient(bank, client1);
		} catch (ClientExistsException e) {
			System.out.format("Cannot add an already existing client: %s%n",
			                  client1);
		}
	}

	private static void printBalance() {
		System.out.format("%nPrint balance for all clients%n");
		for (Client client : bank.getClients()) {
			System.out.println("Client: " + client);
			for (Account account : client.getAccounts()) {
				System.out.format("Account %d : %.2f%n", account.getId(),
				                  account.getBalance());
			}
		}
	}

}
