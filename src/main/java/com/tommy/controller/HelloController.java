package com.tommy.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.tommy.model.SignatureRequest;
import com.tommy.tool.SignatureDemo;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@RequestMapping("/")
	public String index(@RequestHeader(name = "Host", required = false) final String host) {
		return "host is : " + host;
	}

	@RequestMapping("/success")
	public Map<String, String> depositSuccess() {
		HashMap<String, String> map = new HashMap<>();
		map.put("result", "SUCCESS");
		map.put("app", "heroku");

		return map;
	}

	@RequestMapping("/fail")
	public Map<String, String> depositFail() {
		HashMap<String, String> map = new HashMap<>();
		map.put("result", "fail");
		map.put("app", "heroku");

		return map;
	}

	@PostMapping(value = "/signature")
	public Map<String, String> signature(@RequestBody SignatureRequest request) {

		String key = request.key;
		String contentType = request.contentType;
		String keyId = request.keyId;
		String body = request.body;

		HashMap<String, String> map = new HashMap<>();
		String signature = SignatureDemo.sign(key, contentType, keyId, body);
		map.put("signature", signature);
		map.put("base64Signature", Base64.getEncoder().encodeToString(signature.getBytes(StandardCharsets.UTF_8)));

		return map;

	}

	@RequestMapping("/sleep/{sleepSec}")
	public Map<String, String> sleep(@PathVariable("sleepSec") Integer sleepSec) throws InterruptedException {
		Thread.sleep(sleepSec * 1000L);
		HashMap<String, String> map = new HashMap<>();
		map.put("result", "SUCCESS");
		map.put("sleep", sleepSec + "秒");
		return map;
	}

}