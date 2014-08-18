/**
 * DefaultWysiwygToolBar.java
 * $Id: DefaultWysiwygToolBar.java 1998 2013-01-22 02:37:19Z andy $
 * Copyright (C) 2007-2012, Andreas Rudolph
 */
package net.atlanticbb.tantlinger.ui.text;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.html.HTMLDocument;
import net.atlanticbb.tantlinger.ui.DefaultAction;
import net.atlanticbb.tantlinger.ui.UIUtils;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLEditorActionFactory;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLFontColorAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLImageAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLInlineAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLLinkAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLTableAction;
import org.bushe.swing.action.ActionList;
import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.ActionUIFactory;

/**
 * DefaultWysiwygToolBar.
 * <br/>$Id: DefaultWysiwygToolBar.java 1998 2013-01-22 02:37:19Z andy $
 * @author Andreas Rudolph
 */
public class DefaultWysiwygToolBar extends AbstractToolBar
{
  private final AbstractWysiwygEditor wysiwygEditor;
  private JComboBox fontFamilyCombo;
  private JComboBox paragraphCombo;
  private ActionList actionList;
  private ActionListener fontChangeHandler = new FontChangeHandler();
  private ActionListener paragraphComboHandler = new ParagraphComboHandler();

  public DefaultWysiwygToolBar( AbstractWysiwygEditor wysiwygEditor )
  {
    super();
    this.wysiwygEditor = wysiwygEditor;
    this.wysiwygEditor.addCaretListener( new CaretHandler() );
    build();
  }

  private void build()
  {
    actionList = new ActionList( "wysiwygToolbar" );

    //Font comboFont = new Font( "Dialog", Font.PLAIN, 12 );

    ActionList blockActions = new ActionList( "blockActions" );
    blockActions.addAll( HTMLEditorActionFactory.createBlockElementActionList() );
    blockActions.addAll( HTMLEditorActionFactory.createListElementActionList() );

    ActionList fontSizeActions = new ActionList( "fontSizeActions" );
    fontSizeActions.addAll( HTMLEditorActionFactory.createFontSizeActionList() );

    PropertyChangeListener propLst = new PropertyChangeListener()
    {
      public void propertyChange( PropertyChangeEvent evt )
      {
        if (evt.getPropertyName().equals( "selected" ))
        {
          if (evt.getNewValue().equals( Boolean.TRUE ))
          {
            //paragraphCombo.removeActionListener( paragraphComboHandler );
            paragraphCombo.setEnabled( false );
            paragraphCombo.setSelectedItem( evt.getSource() );
            paragraphCombo.setEnabled( true );
            //paragraphCombo.addActionListener( paragraphComboHandler );
          }
        }
      }
    };
    for (Iterator it = blockActions.iterator(); it.hasNext();)
    {
      Object o = it.next();
      if (o instanceof DefaultAction)
      {
        ((DefaultAction) o).addPropertyChangeListener( propLst );
      }
    }

    paragraphCombo = new JComboBox( toArray( blockActions ) );
    paragraphCombo.setPreferredSize( new Dimension( 120, 22 ) );
    paragraphCombo.setMinimumSize( new Dimension( 120, 22 ) );
    paragraphCombo.setMaximumSize( new Dimension( 120, 22 ) );
    //paragraphCombo.setFont( comboFont );
    paragraphCombo.addActionListener( paragraphComboHandler );
    paragraphCombo.setRenderer( new ParagraphComboRenderer() );
    add( paragraphCombo );
    addSeparator();

    List<String> fonts = new ArrayList<String>();
    fonts.add( "Default" );
    fonts.add( "serif" );
    fonts.add( "sans-serif" );
    fonts.add( "monospaced" );
    GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
    fonts.addAll( Arrays.asList( gEnv.getAvailableFontFamilyNames() ) );

    fontFamilyCombo = new JComboBox( fonts.toArray( new String[fonts.size()] ) );
    fontFamilyCombo.setPreferredSize( new Dimension( 150, 22 ) );
    fontFamilyCombo.setMinimumSize( new Dimension( 150, 22 ) );
    fontFamilyCombo.setMaximumSize( new Dimension( 150, 22 ) );
    //fontFamilyCombo.setFont( comboFont );
    fontFamilyCombo.addActionListener( fontChangeHandler );
    add( fontFamilyCombo );

    final JButton fontSizeButton = new JButton( UIUtils.getIcon( UIUtils.X16, "fontsize.png" ) );
    final JPopupMenu sizePopup = ActionUIFactory.getInstance().createPopupMenu( fontSizeActions );
    ActionListener al = new ActionListener()
    {
      public void actionPerformed( ActionEvent e )
      {
        sizePopup.show( fontSizeButton, 0, fontSizeButton.getHeight() );
      }
    };
    fontSizeButton.addActionListener( al );
    configToolbarButton( fontSizeButton );
    add( fontSizeButton );

    Action act = new HTMLFontColorAction();
    //act.setEnabled( true );
    actionList.add( act );
    addToToolBar( act );
    addSeparator();

    act = new HTMLInlineAction( HTMLInlineAction.BOLD );
    //act.setEnabled( true );
    act.putValue( ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE );
    actionList.add( act );
    addToToolBar( act );

    act = new HTMLInlineAction( HTMLInlineAction.ITALIC );
    //act.setEnabled( true );
    act.putValue( ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE );
    actionList.add( act );
    addToToolBar( act );

    act = new HTMLInlineAction( HTMLInlineAction.UNDERLINE );
    //act.setEnabled( true );
    act.putValue( ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE );
    actionList.add( act );
    addToToolBar( act );
    addSeparator();

    List alst = HTMLEditorActionFactory.createListElementActionList();
    for (Iterator it = alst.iterator(); it.hasNext();)
    {
      act = (Action) it.next();
      //act.setEnabled( true );
      act.putValue( ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE );
      actionList.add( act );
      addToToolBar( act );
    }
    addSeparator();

    alst = HTMLEditorActionFactory.createAlignActionList();
    for (Iterator it = alst.iterator(); it.hasNext();)
    {
      act = (Action) it.next();
      //act.setEnabled( true );
      act.putValue( ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE );
      actionList.add( act );
      addToToolBar( act );
    }
    addSeparator();

    act = new HTMLLinkAction();
    //act.setEnabled( true );
    actionList.add( act );
    addToToolBar( act );

    act = new HTMLImageAction();
    //act.setEnabled( true );
    actionList.add( act );
    addToToolBar( act );

    act = new HTMLTableAction();
    //act.setEnabled( true );
    actionList.add( act );
    addToToolBar( act );

    // Editor in den erzeugten Aktionen registrieren
    actionList.addAll( blockActions );
    actionList.addAll( fontSizeActions );
    wysiwygEditor.registerEditor( actionList );
    //wysiwygEditor.registerEditor( blockActions );
    //wysiwygEditor.registerEditor( fontSizeActions );
  }

  public void setFontName( String fontName )
  {
    String currentName = (fontFamilyCombo.getSelectedItem()!=null)?
      fontFamilyCombo.getSelectedItem().toString(): "";
    if (fontName!=null && fontName.equalsIgnoreCase( currentName )) return;
    try
    {
      fontFamilyCombo.setEnabled( false );
      if (fontName!=null && fontName.trim().length()>0)
      {
        fontFamilyCombo.setSelectedItem( fontName );
      }
      else
      {
        fontFamilyCombo.setSelectedIndex( 0 );
      }
    }
    finally
    {
      fontFamilyCombo.setEnabled( true );
    }
  }

  private Action[] toArray( ActionList lst )
  {
    //return (Action[]) lst.toArray( new Action[lst.size()] );
    List<Action> acts = new ArrayList<Action>();
    for (Iterator it = lst.iterator(); it.hasNext();)
    {
      Object v = it.next();
      if (v != null && v instanceof Action)
      {
        acts.add( (Action) v );
      }
    }
    return acts.toArray( new Action[acts.size()] );
  }

  private void updateState()
  {
    String fontName = wysiwygEditor.getFontFamily();
    //wysiwygEditor.setFontFamily( fontName );
    setFontName( fontName );
    wysiwygEditor.registerEditor( actionList );
    actionList.updateEnabledForAll();
  }

  private class CaretHandler implements CaretListener
  {
    public void caretUpdate( CaretEvent e )
    {
      updateState();
    }
  }

  private class ParagraphComboHandler implements ActionListener
  {
    public void actionPerformed( ActionEvent e )
    {
      if (!paragraphCombo.isEnabled()) return;
      Action a = (Action) (paragraphCombo.getSelectedItem());
      a.actionPerformed( e );
    }
  }

  private class ParagraphComboRenderer extends DefaultListCellRenderer
  {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
    {
      if (value instanceof Action)
      {
        value = ((Action) value).getValue( Action.NAME );
      }
      return super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
    }
  }

  private class FontChangeHandler implements ActionListener
  {

    public void actionPerformed( ActionEvent e )
    {
      if (!fontFamilyCombo.isEnabled()) return;

      //HTMLDocument document = (HTMLDocument) focusedEditor.getDocument();
      HTMLDocument document = (HTMLDocument) wysiwygEditor.getDocument();
      CompoundUndoManager.beginCompoundEdit( document );
      if (fontFamilyCombo.getSelectedIndex()>0)
      {
        //HTMLUtils.setFontFamily(wysEditor, fontFamilyCombo.getSelectedItem().toString());
        wysiwygEditor.setFontFamily( fontFamilyCombo.getSelectedItem().toString() );
      }
      else
      {
        //HTMLUtils.setFontFamily(wysEditor, null);
        wysiwygEditor.setFontFamily( null );
      }
      CompoundUndoManager.endCompoundEdit( document );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     *
    public void itemStateChanged( ItemEvent e )
    {
    }*/
  }
}