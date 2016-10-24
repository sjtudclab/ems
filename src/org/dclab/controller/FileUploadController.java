package org.dclab.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {

	@PostMapping("/examForm")
	public Map<String, String> handleFormUpload(@RequestParam("file") MultipartFile file) {
		String path=System.getProperty("project.root")+File.separator+"files"+File.separator+"import"+File.separator;
		System.out.println(path);
		Map<String, String> map = new HashMap<String, String>();
		try {
			FileOutputStream fos = new FileOutputStream(path + file.getOriginalFilename());
			fos.write(file.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			map.put("info", "上传失败");
			return map;
		}
		map.put("info", "上传成功");
		return map;

	}
	
	
	@RequestMapping(value = "/download")
	public void getFile(@RequestParam String token, HttpServletResponse response) {
		System.out.println(token);
		try {
			// get your file as InputStream
			InputStream is = new FileInputStream(new File(
					"C:\\Users\\ljkxy\\Desktop\\PyCharm Community Edition 2016.1.zip"));
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


}
