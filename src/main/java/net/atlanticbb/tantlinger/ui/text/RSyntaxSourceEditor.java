/**
 * RSyntaxTextAreaEditor.java
 * $Id$
 * Copyright (C) 2007-2012, Andreas Rudolph
 */
package net.atlanticbb.tantlinger.ui.text;

import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.event.CaretListener;
import javax.swing.text.Document;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * RSyntaxTextAreaEditor.
 * <br/>$Id$
 * @author Andreas Rudolph
 */
public class RSyntaxSourceEditor extends AbstractSourceEditor
{
  private RSyntaxTextArea textArea;
  private RTextScrollPane scroller;

  public RSyntaxSourceEditor()
  {
    textArea = new RSyntaxTextArea();
    textArea.setOpaque( true );
    textArea.setBracketMatchingEnabled( true );
    textArea.setSyntaxEditingStyle( SyntaxConstants.SYNTAX_STYLE_HTML );
    scroller = new RTextScrollPane( textArea );
    scroller.setLineNumbersEnabled( true );
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