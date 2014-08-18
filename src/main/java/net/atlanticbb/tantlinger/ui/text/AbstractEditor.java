/**
 * AbstractEditor.java
 * $Id: AbstractEditor.java 2042 2013-02-11 08:45:48Z andy $
 * Copyright (C) 2007-2012, Andreas Rudolph
 */
package net.atlanticbb.tantlinger.ui.text;

import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import org.bushe.swing.action.ActionList;

/**
 * AbstractEditor.
 * <br/>$Id: AbstractEditor.java 2042 2013-02-11 08:45:48Z andy $
 * @author Andreas Rudolph
 */
public abstract class AbstractEditor
{
  public void addCaretListener( CaretListener listener ) {}

  public void addDocumentListener( DocumentListener listener )
  {
    Document doc = getDocument();
    if (doc!=null) doc.addDocumentListener( listener );
  }

  public void addFocusListener( FocusListener listener ) {}

  public void addMouseListener( MouseListener listener ) {}

  public abstract int getCaretPosition();

  public abstract JComponent getComponent();

  public abstract Document getDocument();

  public abstract String getTabTitle();

  public abstract String getText();

  public AbstractToolBar getToolBar()
  {
    return null;
  }

  public void insertText( String txt )
  {
    insertText( txt, getCaretPosition() );
  }

  public void insertText( String txt, int location )
  {
    try
    {
      getDocument().insertString( location, txt, null );
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public abstract void registerEditor( ActionList actions );

  public abstract void requestFocusInWindow();

  public void removeCaretListener( CaretListener listener ) {}

  public void removeDocumentListener( DocumentListener listener )
  {
    Document doc = getDocument();
    if (doc!=null) doc.removeDocumentListener( listener );
  }

  public void removeFocusListener( FocusListener listener ) {}

  public void removeMouseListener( MouseListener listener ) {}

  public abstract void setCaretPosition( int pos );

  public abstract void setDocument( Document document );

  public abstract void setText( String text );
}