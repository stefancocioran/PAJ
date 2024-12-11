package com.luxoft.bankapp.multithreading;

import java.util.LinkedList;
import java.util.List;

class Queue {
	private final List<Email> emails = new LinkedList<>();

	public synchronized void add(Email email) {
		emails.add(email);
		notify();
	}

	public synchronized Email remove() throws InterruptedException {
		while (emails.isEmpty()) {
			wait();
		}
		return emails.remove(0);
	}

	public synchronized boolean isEmpty() {
		return emails.isEmpty();
	}
}