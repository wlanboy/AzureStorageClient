package com.wlanboy.demo.storage;

import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.queue.CloudQueueMessage;
import com.wlanboy.demo.model.StorageParameters;

@Service
public class QueueClient {

	private static final Logger logger = Logger.getLogger(QueueClient.class.getCanonicalName());

	@Autowired
	private CloudStorageAccount cloudStorageAccount;
	private CloudQueueClient queueClient;
	private CloudQueue container;
	final String queueName = "kubqueue";

	public QueueClient() {

	}

	public void createQueueIfNotExists() throws URISyntaxException, StorageException {
		try {
			container = initConnection();

			container.createIfNotExists();
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}

	private CloudQueue initConnection() throws URISyntaxException, StorageException {
		if (queueClient == null) {
			queueClient = cloudStorageAccount.createCloudQueueClient();
		}
		if (container == null) {
			container = queueClient.getQueueReference(queueName);
		}
		return container;
	}

	public void createMessage(String messageText) {
		try {

			container = initConnection();

			CloudQueueMessage message = new CloudQueueMessage(messageText);
			container.addMessage(message);

		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}

	public StorageParameters readMessage() {
		CloudQueueMessage content = null;
		StorageParameters text = null;

		try {
			container = initConnection();

			content = container.retrieveMessage();
			if (content != null) {
				text = new StorageParameters(content.getId(), content.getPopReceipt(),
						content.getMessageContentAsString());
				container.deleteMessage(content);
			}

		} catch (Exception e) {
			logger.severe(e.getMessage());
		}

		return text;
	}

}
