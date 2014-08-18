/**
 * AbstractSourceEditor.java
 * $Id: AbstractSourceEditor.java 1995 2013-01-17 05:24:51Z andy $
 * Copyright (C) 2007-2012, Andreas Rudolph
 */
package net.atlanticbb.tantlinger.ui.text;

/**
 * AbstractSourceEditor.
 * <br/>$Id: AbstractSourceEditor.java 1995 2013-01-17 05:24:51Z andy $
 * @author Andreas Rudolph
 */
public abstract class AbstractSourceEditor extends AbstractEditor
{
  @Override
  public String getTabTitle()
  {
    return "HTML-Quelltext";
  }
}