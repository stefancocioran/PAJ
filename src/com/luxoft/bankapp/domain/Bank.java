package com.luxoft.bankapp.domain;

import java.text.DateFormat;
import java.util.*;

import com.luxoft.bankapp.exceptions.ClientExistsException;
import com.luxoft.bankapp.multithreading.Email;
import com.luxoft.bankapp.multithreading.EmailService;
import com.luxoft.bankapp.utils.ClientRegistrationListener;

public class Bank {

	private final Set<Client> clients = new HashSet<>();
	private final List<ClientRegistrationListener> listeners = new ArrayList<ClientRegistrationListener>();
	
	private int printedClients = 0;
	private int emailedClients = 0;
	private int debuggedClients = 0;
	
	public Bank() {
		listeners.add(new PrintClientListener());
		listeners.add(new EmailNotificationListener());
		listeners.add(new DebugListener());
	}
	
	public int getPrintedClients() {
		return printedClients;
	}

	public int getEmailedClients() {
		return emailedClients;
	}

	public int getDebuggedClients() {
		return debuggedClients;
	}
	
	public void addClient(final Client client) throws ClientExistsException {
    	if (clients.contains(client)) {
    		throw new ClientExistsException("Client already exists into the bank");
    	} 
    		
    	clients.add(client);
        notify(client);
	}
	
	private void notify(Client client) {
        for (ClientRegistrationListener listener: listeners) {
            listener.onClientAdded(client);
        }
    }

	public Set<Client> getClients() {
		return Collections.unmodifiableSet(clients);
	}
	
	class PrintClientListener implements ClientRegistrationListener {
		@Override 
		public void onClientAdded(Client client) {
	        System.out.println("Client added: " + client.getName());
	        printedClients++;
	    }

	}
	
	 class EmailNotificationListener implements ClientRegistrationListener {
		private final EmailService emailService = EmailService.getInstance();

		@Override
		public void onClientAdded(Client client) {

	        System.out.println("Notification email for client " + client.getName() + " to be sent");

			Email email = new Email(client, "bank@example.com", client.getName(),
			                        "Welcome! Hello " + client.getName() + ", welcome to our bank.");
			emailService.sendNotificationEmail(email);

	        emailedClients++;
	    }
	}
	
	class DebugListener implements ClientRegistrationListener {
        @Override 
        public void onClientAdded(Client client) {
            System.out.println("Client " + client.getName() + " added on: " + DateFormat.getDateInstance(DateFormat.FULL).format(new Date()));
            debuggedClients++;
        }
    }

}




