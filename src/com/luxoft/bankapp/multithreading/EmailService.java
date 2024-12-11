package com.luxoft.bankapp.multithreading;

public class EmailService {
	private static EmailService instance;
	private final Queue emailQueue = new Queue();
	private volatile boolean running = true;

	private EmailService() {
		Thread emailSenderThread = new Thread(() -> {
			while (running || !emailQueue.isEmpty()) {
				try {
					Email email = emailQueue.remove();
					sendEmail(email);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		});
		emailSenderThread.start();
	}

	public static synchronized EmailService getInstance() {
		if (instance == null) instance = new EmailService();
		return instance;
	}
	public void sendNotificationEmail(Email email) {
		emailQueue.add(email);
	}

	private void sendEmail(Email email) {
		System.out.println("Sending email to " + email.getTo() + " with message: " + email.getMessage());
		try {
			Thread.sleep(2000); // Simulate email sending delay
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		System.out.println("Email sent to " + email.getTo());
	}

	public void close() {
		running = false;

		synchronized (emailQueue) {
			emailQueue.notifyAll();
		}
	}
}
