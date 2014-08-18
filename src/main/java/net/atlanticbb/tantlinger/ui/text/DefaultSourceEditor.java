/**
 * DefaultSourceCodeEditor.java
 * $Id$
 * Copyright (C) 2007-2012, Andreas Rudolph
 */
package net.atlanticbb.tantlinger.ui.text;

import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.CaretListener;
import javax.swing.text.Document;

/**
 * DefaultSourceCodeEditor.
 * <br/>$Id$
 * @author Andreas Rudolph
 */
public class DefaultSourceEditor extends AbstractSourceEditor
{
  private JTextArea textArea;
  private JScrollPane scroller;

  public DefaultSourceEditor()
  {
    textArea = new JTextArea();
    textArea.setOpaque( true );
    scroller = new JScrollPane( textArea );
  }

  @Override
  public void addCaretListener( CaretListener listener )
  {
    textArea.addCaretListener( listener );
  }

  @Override
  public void addFocusListener( FocusListener listener )
  {
    textArea.addFocusListener( listener );
  }

  @Override
  public void addMouseListener( MouseListener listener )
  {
    textArea.addMouseListener( listener );
  }

  @Override
  public JComponent getComponent()
  {
    return scroller;
  }

  @Override
  public Document getDocument()
  {
    return textArea.getDocument();
  }

  @Override
  public String getText()
  {
    return textArea.getText();
  }

  @Override
  public void removeCaretListener( CaretListener listener )
  {
    textArea.removeCaretListener( listener );
  }

  @Override
  public void removeFocusListener( FocusListener listener )
  {
    textArea.removeFocusListener( listener );
  }

  @Override
  public void removeMouseListener( MouseListener listener )
  {
    textArea.removeMouseListener( listener );
  }

  @Override
  public void requestFocusInWindow()
  {
    textArea.requestFocusInWindow();
  }

  @Override
  public void setCaretPosition( int pos )
  {
    textArea.setCaretPosition( pos );
  }

  @Override
  public void setDocument( Document document )
  {
    textArea.setDocument( document );
  }

  @Override
  public void setText( String text )
  {
    textArea.setText( text );
  }
}