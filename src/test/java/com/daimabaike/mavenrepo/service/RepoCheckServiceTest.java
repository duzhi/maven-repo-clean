package com.daimabaike.mavenrepo.service;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.daimabaike.mavenrepo.util.DigestUtils;

public class RepoCheckServiceTest {

	RepoCheckService RepoCheckService = new RepoCheckService();

	@Test
	public void testSha1() {
//
//		Assert.assertTrue(
//				DigestUtils.genSign(new File("D:\\ProgramsData\\mavenRepo\\junit\\junit\\4.12\\junit-4.12.jar"), "SHA1")
//						.equalsIgnoreCase("2973d150c0dc1fefe998f834810d68f278ea58ec"));
//
		
		Assert.assertTrue(
				DigestUtils.genSign(new File("D:\\ProgramsData\\mavenRepo\\org\\apache\\maven\\maven-toolchain\\2.2.1\\maven-toolchain-2.2.1.jar"), "SHA1")
						.equalsIgnoreCase("0be589179cfbbad11e48572bf1a28e3490c7b197"));

//		be589179cfbbad11e48572bf1a28e3490c7b197
		
//		0be589179cfbbad11e48572bf1a28e3490c7b197
		
//		Assert.assertTrue(
//				DigestUtils.genSign(new File("D:\\ProgramsData\\mavenRepo\\org\\apache\\maven\\maven-toolchain\\2.2.1\\maven-toolchain-2.2.1.pom"), "SHA1")
//						.equalsIgnoreCase("ef19c9782d233f4515cf83b4208dd04731ed8989"));
		
		

	}

}
