package com.wlanboy.demo.model;

import org.springframework.hateoas.ResourceSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StorageParameters extends ResourceSupport {

	public StorageParameters() {
		
	};
	
	public StorageParameters(String id, String name, String content) {
		this.identifier = id;
		this.name = name;
		this.content = content;
	}

	public String identifier;
	public String name;
	public String content;

}
