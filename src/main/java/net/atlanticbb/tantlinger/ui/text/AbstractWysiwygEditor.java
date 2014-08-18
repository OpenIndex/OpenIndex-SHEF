/**
 * AbstractWysiwygEditor.java
 * $Id$
 * Copyright (C) 2007-2012, Andreas Rudolph
 */
package net.atlanticbb.tantlinger.ui.text;

/**
 * AbstractWysiwygEditor.
 * <br/>$Id$
 * @author Andreas Rudolph
 */
public abstract class AbstractWysiwygEditor extends AbstractEditor
{
  public abstract String getFontFamily();

  @Override
  public String getTabTitle()
  {
    return "HTML-Ansicht";
  }

  public abstract void insertHTML( String html, int location );

  public abstract void setFontFamily( String fontName );
}