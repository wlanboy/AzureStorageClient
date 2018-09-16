package com.wlanboy.demo.model;

import org.springframework.hateoas.ResourceSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StorageParameters extends ResourceSupport {

	public StorageParameters(Long id, String name) {
		this.identifier = id;
		this.name = name;
	}

	private Long identifier;
	private String name;
}
