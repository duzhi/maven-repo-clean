package com.daimabaike.mavenrepo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DigestUtils {

	private static final Logger logger = LoggerFactory.getLogger(DigestUtils.class);

	private static int CACHE_LEN = 4096;

	public static String str2HexStr(byte[] bytes) {
		char[] chars = "0123456789abcdef".toCharArray();
		StringBuilder sb = new StringBuilder();
		int bit;
		for (int i = 0; i < bytes.length; i++) {
			bit = (bytes[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bytes[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString();
	}

	public static String genSign(File file, String algorithm) {

		FileInputStream in = null;
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA1");
			in = new FileInputStream(file);
			byte buffer[] = new byte[CACHE_LEN];
			int len;

			while ((len = in.read(buffer, 0, CACHE_LEN)) != -1) {
				digest.update(buffer, 0, len);
			}
			String s = str2HexStr(digest.digest());

			return s;
		} catch (Exception e) {
			logger.error("error", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (digest != null) {
				try {
					digest.clone();
				} catch (CloneNotSupportedException e) {
				}
			}
		}
		return null;
	}

	public static boolean isSignSHA1Valid(String sourceFilePath, String sha1FilePath) {
		File sourceFile = new File(sourceFilePath);
		File sha1File = new File(sha1FilePath);

		if (!sha1File.exists() && !sourceFile.exists()) {
			return true;
		}

		if (!sha1File.exists() || !sourceFile.exists()) {
			return false;
		}

		try {
			String sha1 = FileUtils.readFileToString(sha1File, "iso-8859-1");
			String sha1file = genSign(sourceFile, "SHA1");
			return sha1 == null ? false : sha1.trim().equalsIgnoreCase(sha1file);

		} catch (IOException e) {
		}
		return false;
	}

}
