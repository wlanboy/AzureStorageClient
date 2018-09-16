package com.wlanboy.demo.storage;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.applicationinsights.core.dependencies.apachecommons.io.FilenameUtils;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

@Service
public class StorageClient {

	private static final Logger logger = Logger.getLogger(StorageClient.class.getCanonicalName());

	@Autowired
	private CloudStorageAccount cloudStorageAccount;
	private CloudBlobClient blobClient;
	private CloudBlobContainer container;
	private final String containerName = "azurestorage";

	public StorageClient() {

	}

	public void createContainerIfNotExists() throws URISyntaxException, StorageException {
		try {
			container = initConnection();

			container.createIfNotExists();
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}

	private CloudBlobContainer initConnection() throws URISyntaxException, StorageException {
		if (blobClient == null) {
			blobClient = cloudStorageAccount.createCloudBlobClient();
		}
		if (container == null) {
			container = blobClient.getContainerReference(containerName);
		}
		return container;
	}

	public void uploadBlob(String filename, InputStream file, long length) throws URISyntaxException, StorageException {
		try {

			container = initConnection();

			CloudBlockBlob blob = container.getBlockBlobReference(filename);
			blob.upload(file, length);

		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}

	public ArrayList<String> ListContainerBlobs() {
		ArrayList<String> files = new ArrayList<String>();

		try {

			container = initConnection();

			for (ListBlobItem blobItem : container.listBlobs()) {
				files.add(FilenameUtils.getName(blobItem.getUri().getPath()));
			}

		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
		return files;
	}

	public OutputStream downloadBlob(String filename, OutputStream download)
			throws URISyntaxException, StorageException {

		try {
			final CloudBlobContainer container = initConnection();

			CloudBlockBlob blob = container.getBlockBlobReference(filename);
			blob.download(download);
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}

		return download;
	}

	public void deleteBlob(String filename) throws URISyntaxException, StorageException {

		try {
			container = initConnection();

			CloudBlockBlob blob = container.getBlockBlobReference(filename);
			blob.deleteIfExists();
		} catch (Exception e) {
			logger.severe(e.getMessage());
		}
	}

}
