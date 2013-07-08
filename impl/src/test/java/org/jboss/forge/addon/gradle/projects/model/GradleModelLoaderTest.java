/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Adam Wyłuda
 */
public class GradleModelLoaderTest
{
   private static GradleModel model;

   @BeforeClass
   public static void init() throws IOException
   {
      GradleModelLoader loader = new GradleModelLoader();
      String source = IOUtils.toString(GradleModelLoaderTest.class.getResourceAsStream("/loader/forge-output.xml"));
      model = loader.loadFromXML(null, source);
   }

   @Test
   public void testProjectName()
   {
      assertEquals("Gradle Test Project", model.getProjectName());
   }

   @Test
   public void testVersion()
   {
      assertEquals("0.1-SNAPSHOT", model.getVersion());
   }

   @Test
   public void testTasks()
   {
      boolean zxyzSet = false;
      for (GradleTask task : model.getTasks())
      {
         if (task.getName().equals("zxyz"))
         {
            zxyzSet = true;
            Set<GradleTask> dependsOnSet = task.getDependsOn();

            boolean buildSet = false, testSet = false;
            for (GradleTask dependsOn : dependsOnSet)
            {
               if (dependsOn.getName().equals("build"))
               {
                  buildSet = true;
               }
               else if (dependsOn.getName().equals("test"))
               {
                  testSet = true;
               }
            }
            assertTrue("No dependency on build", buildSet);
            assertTrue("No dependency on test", testSet);
         }
      }
      assertTrue("zxyz task not found", zxyzSet);
   }

   @Test
   public void testDependencies()
   {
      boolean gradleToolingSet = false, junitSet = false;
      for (GradleDependency dep : model.getDependencies())
      {
         if (dep.getName().equals("gradle-tooling-api"))
         {
            gradleToolingSet = true;
            assertEquals("org.gradle", dep.getGroup());
            assertEquals("1.6", dep.getVersion());
            assertEquals(GradleDependencyConfiguration.COMPILE, dep.getConfiguration());
         }
         else if (dep.getName().equals("junit"))
         {
            junitSet = true;
            assertEquals("junit", dep.getGroup());
            assertEquals("4.11", dep.getVersion());
            assertEquals(GradleDependencyConfiguration.TEST_COMPILE, dep.getConfiguration());
         }
      }
      assertTrue("gradle-tooling-api dependency not found", gradleToolingSet);
      assertTrue("junit dependency not found", junitSet);
   }

   @Test
   @Ignore
   public void testManagedDependencies()
   {
      // TODO test managed dependencies
   }

   @Test
   public void testProfiles()
   {
      assertEquals("There are more or less than 2 profiles", 2, model.getProfiles().size());
      boolean glassfishSet = false, wildflySet = false;
      for (GradleProfile profile : model.getProfiles())
      {
         if (profile.getName().equals("glassfish"))
         {
            glassfishSet = true;
            assertTrue("Glassfish profile doesn't contain runApplicationServer task",
                     profile.getModel().hasTask("runApplicationServer"));
            assertTrue("Glassfish profile doesn't contain specified dependency",
                     profile.getModel().hasDependency(GradleDependencyBuilder.fromGradleString("compile", "javax.annotation:jsr250-api:1.0")));
         }
         else if (profile.getName().equals("wildfly"))
         {
            wildflySet = true;  glassfishSet = true;
            assertTrue("Wildfly profile doesn't contain runApplicationServer task",
                     profile.getModel().hasTask("runApplicationServer"));
            assertTrue("Glassfish profile doesn't contain specified dependency",
                     profile.getModel().hasDependency(GradleDependencyBuilder.fromGradleString("compile", "log4j:log4j:1.2.17")));
         }
      }
      assertTrue("glassfish profile not found", glassfishSet);
      assertTrue("wildfly profile not found", wildflySet);
   }

   @Test
   public void testPlugins()
   {
      assertTrue("There is no java plugin", model.hasPlugin(GradlePluginType.JAVA.getClazz()));
      assertTrue("There is no groovy plugin", model.hasPlugin(GradlePluginType.GROOVY.getClazz()));
      assertTrue("There is no scala plugin", model.hasPlugin(GradlePluginType.SCALA.getClazz()));
      assertTrue("There is no eclipse plugin", model.hasPlugin(GradlePluginType.ECLIPSE.getClazz()));
   }
   
   @Test
   public void testRepositories()
   {
      assertTrue("There is no Gradle repository", model.hasRepository("http://repo.gradle.org/gradle/libs-releases-local/"));
   }
}
