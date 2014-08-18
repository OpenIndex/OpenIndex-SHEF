/**
 * Editor.java
 * $Id$
 * Copyright (C) 2007-2012, Andreas Rudolph
 */
package net.atlanticbb.tantlinger.ui.text;

import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 * Editor.
 * <br/>$Id$
 * @author Andreas Rudolph
 */
public abstract class AbstractEditor
{
  public abstract void addCaretListener( CaretListener listener );

  public void addDocumentListener( DocumentListener listener )
  {
    Document doc = getDocument();
    if (doc!=null) doc.addDocumentListener( listener );
  }

  public abstract void addFocusListener( FocusListener listener );

  public abstract void addMouseListener( MouseListener listener );

  public abstract JComponent getComponent();

  public abstract Document getDocument();

  public abstract String getTabTitle();

  public abstract String getText();

  public abstract void requestFocusInWindow();

  public abstract void removeCaretListener( CaretListener listener );

  public void removeDocumentListener( DocumentListener listener )
  {
    Document doc = getDocument();
    if (doc!=null) doc.removeDocumentListener( listener );
  }

  public abstract void removeFocusListener( FocusListener listener );

  public abstract void removeMouseListener( MouseListener listener );

  public abstract void setCaretPosition( int pos );

  public abstract void setDocument( Document document );

  public abstract void setText( String text );
}