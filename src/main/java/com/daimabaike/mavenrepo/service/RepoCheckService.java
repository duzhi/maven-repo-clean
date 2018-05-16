package com.daimabaike.mavenrepo.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.daimabaike.mavenrepo.util.DigestUtils;

@Service
public class RepoCheckService {

	private static final Logger logger = LoggerFactory.getLogger(RepoCheckService.class);

	// 一定存在
	public static final String REMOTE_REPO = "_remote.repositories";

	// 可能有
	public static final String M2E_LASTUPDATEED_PROPERTIES = "m2e-lastUpdated.properties";

	// 一定有
	public static final String JAR_SUFFIX = ".jar";

	public static final String JAR_SHA1_SUFFIX = JAR_SUFFIX + ".sha1";

	// 一定有
	public static final String POM_SUFFIX = ".pom";

	public static final String POM_SHA1_SUFFIX = POM_SUFFIX + ".sha1";

	// 可能有
	public static final String SOURCES_SUFFIX = "-sources.jar";

	public static final String SOURCES_SHA1_SUFFIX = SOURCES_SUFFIX + ".sha1";

	public static final List<String> FILES_MUST_EXIST = Arrays
			.asList(new String[] { REMOTE_REPO, POM_SUFFIX, POM_SHA1_SUFFIX });
	public static final List<String> FILES_MAYBE_EXIST = Arrays
			.asList(new String[] { REMOTE_REPO, M2E_LASTUPDATEED_PROPERTIES, JAR_SUFFIX, JAR_SHA1_SUFFIX, POM_SUFFIX,
					POM_SHA1_SUFFIX, SOURCES_SUFFIX, SOURCES_SHA1_SUFFIX });

	private final List<String> errorPaths = new ArrayList<String>();

	public List<String> getErrorPaths() {
		return errorPaths;
	}

	// 判断是否是Jar目录，存储有文件的就认为是Jar目录
	public boolean isJarDir(File file) {

		return file.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				String absPath = dir.getAbsolutePath() + File.separatorChar + name;

				if (new File(absPath).isFile()) {
					return true;
				}
				return false;
			}
		}).length > 0;

	}

	public boolean isMustFileExist(File file) {

		// \结尾的绝对路径，如D:\ProgramsData\mavenRepo\org\slf4j\slf4j-api\1.7.25\
		String dirPath = file.getAbsolutePath() + File.separatorChar;

		// // REMOTE_REPO 必须存在
		File repo = new File(dirPath + REMOTE_REPO);
		if (!repo.exists()) {
			errorPaths.add(file.getAbsolutePath() + File.separatorChar);
			logger.error("repo not exist:\r\n" + file.getAbsolutePath() + File.separatorChar);
		}

		// REMOTE_REPO、POM必须存在
		String filePrefix = file.getParentFile().getName() + "-" + file.getName();

		// POM
		boolean pom = DigestUtils.isSignSHA1Valid(dirPath + filePrefix + POM_SUFFIX,
				dirPath + filePrefix + POM_SHA1_SUFFIX);
		// Jar
		boolean jar = DigestUtils.isSignSHA1Valid(dirPath + filePrefix + JAR_SUFFIX,
				dirPath + filePrefix + JAR_SHA1_SUFFIX);
		// Sources
		boolean sources = DigestUtils.isSignSHA1Valid(dirPath + filePrefix + SOURCES_SUFFIX,
				dirPath + filePrefix + SOURCES_SHA1_SUFFIX);

		boolean result = pom && jar && sources;
		if (!result) {
			errorPaths.add(dirPath);
			logger.info("exists error file:\r\n" + dirPath);
		}
		return result;

	}

}
