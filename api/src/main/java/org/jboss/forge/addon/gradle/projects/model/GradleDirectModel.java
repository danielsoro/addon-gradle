/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.addon.gradle.projects.model;

import java.util.List;
import java.util.Map;

import org.jboss.forge.addon.gradle.projects.exceptions.UnremovableElementException;

/**
 * @author Adam Wyłuda
 */
public interface GradleDirectModel
{
   // ---------- Group

   String getGroup();

   void setGroup(String group);

   // ---------- Name

   String getName();

   void setName(String name);

   // ---------- Version

   String getVersion();

   void setVersion(String version);
   
   // ---------- Packaging

   String getPackaging();

   void setPackaging(String packaging);

   // ----------- Archive name
   
   String getArchiveName();

   void setArchiveName(String archiveName);

   // ---------- Tasks

   List<GradleTask> getTasks();

   boolean hasTask(String name);

   void addTask(GradleTaskBuilder builder);

   // ---------- Dependencies

   void addDependency(GradleDependencyBuilder builder);

   void removeDependency(GradleDependencyBuilder builder);

   // ---------- Managed dependencies

   void addManagedDependency(GradleDependencyBuilder builder);

   void removeManagedDependency(GradleDependencyBuilder builder);

   // ---------- Profiles

   List<GradleProfile> getProfiles();

   boolean hasProfile(String name);

   void addProfile(String name);

   void removeProfile(String name);

   // ---------- Plugins

   List<GradlePlugin> getPlugins();

   boolean hasPlugin(String clazz);

   void addPlugin(String name);

   void removePlugin(String name);

   // ---------- Repositories

   List<GradleRepository> getRepositories();

   boolean hasRepository(String url);

   void addRepository(String url);

   void removeRepository(String url);

   // ---------- Properties

   Map<String, String> getProperties();

   boolean hasProperty(String key);

   void setProperty(String name, String value);

   void removeProperty(String name);
}
