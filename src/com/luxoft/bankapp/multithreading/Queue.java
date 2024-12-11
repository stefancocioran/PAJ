package com.luxoft.bankapp.multithreading;

import java.util.LinkedList;
import java.util.List;

public class Queue {
	private final List<Email> emails = new LinkedList<>();
	private volatile int emailsSent = 0;

	public synchronized void add(Email email) {
		emails.add(email);
		notify();
	}

	public synchronized Email remove() throws InterruptedException {
		while (emails.isEmpty()) {
			wait();
		}
		emailsSent++;
		return emails.remove(0);
	}

	public int getEmailsSent() {
		return emailsSent;
	}

	public synchronized boolean isEmpty() {
		return emails.isEmpty();
	}
}