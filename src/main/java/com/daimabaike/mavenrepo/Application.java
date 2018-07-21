/*
 * www.daimabaike.com
 */
package com.daimabaike.mavenrepo;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import com.daimabaike.mavenrepo.service.RepoCheckService;

/**
 * @author duzhi
 */
@SpringBootApplication
@Configuration
public class Application implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	@Value("${maven.repo.root-path}")
	private String repoRootPath;
	
	public String getRepoRootPath() {
		return repoRootPath;
	}

	public void setRepoRootPath(String repoRootPath) {
		this.repoRootPath = repoRootPath;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private RepoCheckService repoCheckService;

	@Override
	public void run(String... args) throws Exception {
		logger.info("");
		logger.info("------------ start--------------");
		logger.info("");
		File repoRoot = new File(repoRootPath);

		work(repoRoot);
		logger.info("");
		if (repoCheckService.getErrorPaths().isEmpty()) {
			logger.info("---------- no error file ------------");
		} else {
			logger.info("---------- del errorPaths cmd ------------");
			for (String path : repoCheckService.getErrorPaths()) {
				System.out.println("rd/s/q " + path);
			}
		}
		logger.info("------------ end----------------");
		logger.info("");

	}

	public void work(File file) {

		if (repoCheckService.isJarDir(file)) {
			repoCheckService.isMustFileExist(file);
			return;
		}

		for (File f : file.listFiles()) {
			work(f);
		}
	}

}
