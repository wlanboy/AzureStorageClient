package com.wlanboy.demo.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microsoft.azure.storage.StorageException;
import com.wlanboy.demo.storage.StorageClient;

@Controller
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	StorageClient storage;

	@RequestMapping("/")
	public String index(Model model) {

		String error = null;
		ArrayList<String> latestBlobs = null;

		try {
			latestBlobs = storage.ListContainerBlobs();
		} catch (Exception ex) {
			logger.error("group", ex);
			error = ex.getMessage();
		}

		model.addAttribute("latestBlobs", latestBlobs);
		model.addAttribute("error", error);
		return "index";
	}

	@RequestMapping("/download/{name:.+}")
	public void download(@PathVariable String name, HttpServletResponse response) {
		OutputStream is = null;
		try {
			is = response.getOutputStream();
			storage.downloadBlob(name, is);
			// response.setContentType(MediaType.ALL_VALUE);

		} catch (IOException | URISyntaxException | StorageException e) {
			logger.error(e.getMessage());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Model model) {
		String error = null;
		ArrayList<String> latestBlobs = null;

		try {
			try (InputStream uploadedStream = file.getInputStream();) {
				storage.uploadBlob(file.getOriginalFilename(), uploadedStream, file.getSize());
			}

			latestBlobs = storage.ListContainerBlobs();
		} catch (Exception ex) {
			logger.error("group", ex);
			error = ex.getMessage();
		}

		model.addAttribute("latestBlobs", latestBlobs);
		model.addAttribute("error", error);

		return "index";
	}
	
	@RequestMapping("/delete/{name:.+}")
	public String delete(@PathVariable String name, Model model) {
		String error = null;
		ArrayList<String> latestBlobs = null;
		
		try {
			storage.deleteBlob(name);
			
			latestBlobs = storage.ListContainerBlobs();

		} catch (URISyntaxException | StorageException ex) {
			logger.error(ex.getMessage());
			error = ex.getMessage();
		} 
		
		model.addAttribute("latestBlobs", latestBlobs);
		model.addAttribute("error", error);
		
		return "index";
	}

	@PostMapping("/uploadMultiPart")
	public String uploadMultiPart(RequestContext request, Model model) {
		String error = null;
		ArrayList<String> latestBlobs = null;

		try {
			// boolean isMultipart = ServletFileUpload.isMultipartContent(request);

			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
			factory.setSizeThreshold(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);

			ServletFileUpload upload = new ServletFileUpload(factory);

			List<FileItem> items = upload.parseRequest(request);

			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = iter.next();

				if (!item.isFormField()) {
					try (InputStream uploadedStream = item.getInputStream();) {
						storage.uploadBlob(item.getName(), uploadedStream, item.getSize());
					}
				}
			}

			latestBlobs = storage.ListContainerBlobs();
		} catch (Exception ex) {
			logger.error("group", ex);
			error = ex.getMessage();
		}

		model.addAttribute("latestBlobs", latestBlobs);
		model.addAttribute("error", error);

		return "index";
	}

}
