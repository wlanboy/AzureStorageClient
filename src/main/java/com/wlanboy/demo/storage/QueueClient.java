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

	final String queueName = "kubqueue";

	public QueueClient() {

	}

	public void createQueueIfNotExists() throws URISyntaxException, StorageException {
		try {
			final CloudQueueClient queueClient = cloudStorageAccount.createCloudQueueClient();
			final CloudQueue container = queueClient.getQueueReference(queueName);

			container.createIfNotExists();
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}

	public void createMessage(String messageText){
		try {

			final CloudQueueClient queueClient = cloudStorageAccount.createCloudQueueClient();
			final CloudQueue container = queueClient.getQueueReference(queueName);
			
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
			final CloudQueueClient queueClient = cloudStorageAccount.createCloudQueueClient();
			final CloudQueue container = queueClient.getQueueReference(queueName);
			
			content = container.retrieveMessage();
			if (content != null) {
				text = new StorageParameters(content.getId(), content.getPopReceipt(), content.getMessageContentAsString());
				container.deleteMessage(content);
			}
			
			
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		
		return text;
	}

}
