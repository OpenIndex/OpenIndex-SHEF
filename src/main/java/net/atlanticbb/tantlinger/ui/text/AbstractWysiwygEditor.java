/**
 * AbstractWysiwygEditor.java
 * $Id: AbstractWysiwygEditor.java 2042 2013-02-11 08:45:48Z andy $
 * Copyright (C) 2007-2012, Andreas Rudolph
 */
package net.atlanticbb.tantlinger.ui.text;

/**
 * AbstractWysiwygEditor.
 * <br/>$Id: AbstractWysiwygEditor.java 2042 2013-02-11 08:45:48Z andy $
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

  public void insertHTML( String html )
  {
    insertHTML( html, getCaretPosition() );
  }

  public abstract void insertHTML( String html, int location );

  public abstract void setFontFamily( String fontName );
}