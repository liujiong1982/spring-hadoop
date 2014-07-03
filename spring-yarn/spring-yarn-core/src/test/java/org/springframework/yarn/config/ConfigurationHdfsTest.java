/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.yarn.config;

import java.io.File;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.kerberos.KerberosPrincipal;

import org.apache.hadoop.minikdc.KerberosSecurityTestcase;
import org.apache.hadoop.minikdc.MiniKdc;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

/**
 *  tests for yarn:configuration secure login.
 *
 * @author David Liu
 *
 */
public class ConfigurationHdfsTest extends KerberosSecurityTestcase{
	@Test
	public void testAppSubmission() throws Exception {
		MiniKdc kdc = getKdc();
	    File workDir = getWorkDir();
	      String principal = "foo";
	      File keytab = new File(workDir, "foo.keytab");
	      kdc.createPrincipal(keytab, principal);

	      Set<Principal> principals = new HashSet<Principal>();
	      principals.add(new KerberosPrincipal(principal));

	      org.apache.hadoop.conf.Configuration conf=new org.apache.hadoop.conf.Configuration();
	      org.apache.hadoop.security.SecurityUtil.login(conf, keytab.getAbsolutePath(), principal);
	      AbstractApplicationContext context = new ClassPathXmlApplicationContext(
					"ConfigurationHdfsTest-context.xml", ConfigurationHdfsTest.class); 
	      System.out.println("---------------------------------------------");
	      System.out.println(context.getBean("secureHdfsConfig"));
		Assert.notNull(context.getBean("secureHdfsConfig"));
	}



}
