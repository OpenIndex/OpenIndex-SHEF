/**
 * AbstractSourceEditor.java
 * $Id$
 * Copyright (C) 2007-2012, Andreas Rudolph
 */
package net.atlanticbb.tantlinger.ui.text;

/**
 * AbstractSourceEditor.
 * <br/>$Id$
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