package com.luxoft.bankapp.multithreading;

import com.luxoft.bankapp.domain.Client;

public class Email {
	private Client client;
	private String from;
	private String to;
	private String message;

	public Email(Client client, String from, String to, String message) {
		this.client = client;
		this.from = from;
		this.to = to;
		this.message = message;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Email{" + "client=" + client + ", from='" + from + '\'' + ", " +
		       "to='" + to + '\'' + ", message='" + message + '\'' + '}';
	}
}
