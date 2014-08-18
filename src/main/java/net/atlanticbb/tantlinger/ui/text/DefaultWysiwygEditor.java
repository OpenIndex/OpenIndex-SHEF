/**
 * DefaultWysiwygEditor.java
 * $Id$
 * Copyright (C) 2007-2012, Andreas Rudolph
 */
package net.atlanticbb.tantlinger.ui.text;

import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.io.StringReader;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.CaretListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.UndoManager;

/**
 * DefaultWysiwygEditor.
 * <br/>$Id$
 * @author Andreas Rudolph
 */
public class DefaultWysiwygEditor extends AbstractWysiwygEditor
{
  private JEditorPane textArea;
  private JScrollPane scroller;

  public DefaultWysiwygEditor()
  {
    build();
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

  private void build()
  {
    textArea = new JEditorPane();
    textArea.setOpaque( true );
    scroller = new JScrollPane( textArea );
    textArea.setEditorKitForContentType( "text/html", new WysiwygHTMLEditorKit() );
    textArea.setContentType( "text/html" );
    insertHTML( "<p></p>", 0 );
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
  public String getFontFamily()
  {
    return HTMLUtils.getFontFamily( textArea );
  }

  @Override
  public String getText()
  {
    return textArea.getText();
  }

  @Override
  public void insertHTML( String html, int location )
  {
    try
    {
      HTMLEditorKit kit = (HTMLEditorKit) textArea.getEditorKit();
      Document doc = textArea.getDocument();
      StringReader reader = new StringReader( HTMLUtils.jEditorPaneizeHTML(html) );
      kit.read( reader, doc, location );
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
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
  public void setFontFamily( String fontName )
  {
    HTMLUtils.setFontFamily( textArea, fontName );
  }

  @Override
  public void setText( String text )
  {
    textArea.setText( text );
  }
}