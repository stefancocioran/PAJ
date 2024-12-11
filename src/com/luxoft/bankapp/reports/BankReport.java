package com.luxoft.bankapp.reports;

import com.luxoft.bankapp.domain.Account;
import com.luxoft.bankapp.domain.Bank;
import com.luxoft.bankapp.domain.Client;

import java.util.*;
import java.util.stream.Collectors;

public class BankReport implements BankReportI {
	@Override
	public int getNumberOfClients(Bank bank) {
		return bank.getClients().size();
	}

	@Override
	public int getNumberOfAccounts(Bank bank) {
		return bank.getClients().stream()
		           .mapToInt(client -> client.getAccounts().size())
		           .sum();
	}

	@Override
	public SortedSet<Client> getClientsSorted(Bank bank) {
		var sortedClients = new TreeSet<>(Comparator.comparing(Client::getName));
		sortedClients.addAll(bank.getClients());

		return sortedClients;
	}


	@Override
	public double getTotalSumInAccounts(Bank bank) {
		return bank.getClients().stream()
		           .flatMap(client -> client.getAccounts().stream())
		           .mapToDouble(Account::getBalance)
		           .sum();
	}

	@Override
	public SortedSet<Account> getAccountsSortedBySum(Bank bank) {
		var sortedAccounts = new TreeSet<>(Comparator.comparingDouble(Account::getBalance));

		bank.getClients().stream()
		    .flatMap(client -> client.getAccounts().stream())
		    .forEach(sortedAccounts::add);

		return sortedAccounts;
	}

	@Override
	public double getBankCreditSum(Bank bank) {
		return bank.getClients()
		           .stream()
		           .flatMap(client -> client.getAccounts().stream())
		           .filter(account -> account.maximumAmountToWithdraw() > account.getBalance())
		           .mapToDouble(account -> account.maximumAmountToWithdraw() - account.getBalance())
		           .sum();
	}

	@Override
	public Map<Client, Collection<Account>> getCustomerAccounts(Bank bank) {
		return bank.getClients()
		           .stream()
		           .collect(Collectors.toMap(
				           client -> client,
				           Client::getAccounts
		           ));
	}

	@Override
	public Map<String, List<Client>> getClientsByCity(Bank bank) {
		return bank.getClients().stream()
		           .collect(Collectors.groupingBy(
				           Client::getCity,
				           TreeMap::new,
				           Collectors.toList()
		           ));
	}

}
