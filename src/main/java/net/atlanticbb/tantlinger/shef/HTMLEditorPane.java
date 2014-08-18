package net.atlanticbb.tantlinger.shef;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import net.atlanticbb.tantlinger.i18n.I18n;
import net.atlanticbb.tantlinger.ui.DefaultAction;
import net.atlanticbb.tantlinger.ui.text.AbstractEditor;
import net.atlanticbb.tantlinger.ui.text.AbstractSourceEditor;
import net.atlanticbb.tantlinger.ui.text.AbstractToolBar;
import net.atlanticbb.tantlinger.ui.text.AbstractWysiwygEditor;
import net.atlanticbb.tantlinger.ui.text.CompoundUndoManager;
import net.atlanticbb.tantlinger.ui.text.DefaultSourceEditor;
import net.atlanticbb.tantlinger.ui.text.DefaultWysiwygEditor;
import net.atlanticbb.tantlinger.ui.text.Entities;
import net.atlanticbb.tantlinger.ui.text.actions.ClearStylesAction;
import net.atlanticbb.tantlinger.ui.text.actions.FindReplaceAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLEditorActionFactory;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLElementPropertiesAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLFontAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLFontColorAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLHorizontalRuleAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLImageAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLLineBreakAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLLinkAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLTableAction;
import net.atlanticbb.tantlinger.ui.text.actions.SpecialCharAction;
import org.bushe.swing.action.ActionList;
import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.ActionUIFactory;

/**
 *
 * @author Bob Tantlinger
 * @author Andreas Rudolph
 */
public class HTMLEditorPane extends JPanel
{
  private static final I18n i18n = I18n.getInstance( "net.atlanticbb.tantlinger.shef" );
  private static final String INVALID_TAGS[] = { "html", "head", "body", "title", "meta" };
  private AbstractWysiwygEditor wysEditor;
  private AbstractSourceEditor srcEditor;
  private AbstractEditor currentEditor = null;
  private List<AbstractEditor> editors = new ArrayList<AbstractEditor>();
  private JPanel toolBarPanel;
  private JPanel toolBarPanelContainer;
  private JTabbedPane tabs;
  private JMenu editMenu;
  private JMenu formatMenu;
  private JMenu insertMenu;
  private JPopupMenu wysPopupMenu;
  private JPopupMenu srcPopupMenu;
  private ActionList actionList;
  private FocusListener focusHandler = new FocusHandler();
  private DocumentListener textChangedHandler = new TextChangedHandler();
  private MouseListener popupHandler = new PopupHandler();
  private boolean isWysTextChanged;

  public HTMLEditorPane()
  {
    build();

    // Initialisierung
    updateToolBar();
  }

  protected void addEditor( AbstractEditor editor )
  {
    if (!editors.contains( editor ))
    {
      editors.add( editor );
      tabs.addTab( editor.getTabTitle(), editor.getComponent() );
    }
  }

  private void build()
  {
    buildToolBar();
    buildEditorTabs();
    buildEditorActions();
    setLayout( new BorderLayout() );
    //add( formatToolBar, BorderLayout.NORTH );
    add( toolBarPanel, BorderLayout.NORTH );
    add( tabs, BorderLayout.CENTER );
  }

  private void buildEditorActions()
  {
    actionList = new ActionList( "editor-actions" );

    ActionList paraActions = new ActionList( "paraActions" );
    ActionList fontSizeActions = new ActionList( "fontSizeActions" );
    ActionList editActions = HTMLEditorActionFactory.createEditActionList();
    Action objectPropertiesAction = new HTMLElementPropertiesAction();

    //create editor popupmenus
    wysPopupMenu = ActionUIFactory.getInstance().createPopupMenu( editActions );
    wysPopupMenu.addSeparator();
    wysPopupMenu.add( objectPropertiesAction );
    srcPopupMenu = ActionUIFactory.getInstance().createPopupMenu( editActions );

    // create file menu
    //JMenu fileMenu = new JMenu( i18n.str( "file" ) );

    // create edit menu
    ActionList lst = new ActionList( "edits" );
    Action act = new ChangeTabAction( 0 );
    lst.add( act );
    act = new ChangeTabAction( 1 );
    lst.add( act );
    lst.add( null );//separator
    lst.addAll( editActions );
    lst.add( null );
    lst.add( new FindReplaceAction( false ) );
    actionList.addAll( lst );
    editMenu = ActionUIFactory.getInstance().createMenu( lst );
    editMenu.setText( i18n.str( "edit" ) );

    //create format menu
    formatMenu = new JMenu( i18n.str( "format" ) );
    lst = HTMLEditorActionFactory.createFontSizeActionList();//HTMLEditorActionFactory.createInlineActionList();
    actionList.addAll( lst );
    formatMenu.add( createMenu( lst, i18n.str( "size" ) ) );
    fontSizeActions.addAll( lst );

    lst = HTMLEditorActionFactory.createInlineActionList();
    actionList.addAll( lst );
    formatMenu.add( createMenu( lst, i18n.str( "style" ) ) );

    act = new HTMLFontColorAction();
    actionList.add( act );
    formatMenu.add( act );

    act = new HTMLFontAction();
    actionList.add( act );
    formatMenu.add( act );

    act = new ClearStylesAction();
    actionList.add( act );
    formatMenu.add( act );
    formatMenu.addSeparator();

    lst = HTMLEditorActionFactory.createBlockElementActionList();
    actionList.addAll( lst );
    formatMenu.add( createMenu( lst, i18n.str( "paragraph" ) ) );
    paraActions.addAll( lst );

    lst = HTMLEditorActionFactory.createListElementActionList();
    actionList.addAll( lst );
    formatMenu.add( createMenu( lst, i18n.str( "list" ) ) );
    formatMenu.addSeparator();
    paraActions.addAll( lst );

    lst = HTMLEditorActionFactory.createAlignActionList();
    actionList.addAll( lst );
    formatMenu.add( createMenu( lst, i18n.str( "align" ) ) );

    JMenu tableMenu = new JMenu( i18n.str( "table" ) );
    lst = HTMLEditorActionFactory.createInsertTableElementActionList();
    actionList.addAll( lst );
    tableMenu.add( createMenu( lst, i18n.str( "insert" ) ) );

    lst = HTMLEditorActionFactory.createDeleteTableElementActionList();
    actionList.addAll( lst );
    tableMenu.add( createMenu( lst, i18n.str( "delete" ) ) );
    formatMenu.add( tableMenu );
    formatMenu.addSeparator();

    actionList.add( objectPropertiesAction );
    formatMenu.add( objectPropertiesAction );

    //create insert menu
    insertMenu = new JMenu( i18n.str( "insert" ) );
    act = new HTMLLinkAction();
    actionList.add( act );
    insertMenu.add( act );

    act = new HTMLImageAction();
    actionList.add( act );
    insertMenu.add( act );

    act = new HTMLTableAction();
    actionList.add( act );
    insertMenu.add( act );
    insertMenu.addSeparator();

    act = new HTMLLineBreakAction();
    actionList.add( act );
    insertMenu.add( act );

    act = new HTMLHorizontalRuleAction();
    actionList.add( act );
    insertMenu.add( act );

    act = new SpecialCharAction();
    actionList.add( act );
    insertMenu.add( act );


    //createFormatToolBar( paraActions, fontSizeActions );
  }

  private void buildEditorTabs()
  {
    // create wysiwyg editor
    wysEditor = createWysiwygEditor();
    //wysEditor.addCaretListener( caretHandler );
    wysEditor.addFocusListener( focusHandler );
    wysEditor.addMouseListener( popupHandler );
    wysEditor.addDocumentListener( textChangedHandler );

    // create source editor
    srcEditor = createSourceEditor();
    //srcEditor.addCaretListener( caretHandler );
    srcEditor.addFocusListener( focusHandler );
    srcEditor.addMouseListener( popupHandler );
    //srcEditor.addDocumentListener( textChangedHandler );

    tabs = new JTabbedPane( SwingConstants.BOTTOM );
    //tabs.addTab( wysEditor.getTabTitle(), wysEditor.getComponent() );
    //tabs.addTab( srcEditor.getTabTitle(), srcEditor.getComponent() );
    initEditorTabs( wysEditor, srcEditor );
    tabs.addChangeListener( new ChangeListener()
    {
      public void stateChanged( ChangeEvent e )
      {
        updateEditor();
      }
    } );

    Document wysEditorDocument = wysEditor.getDocument();
    wysEditorDocument.addUndoableEditListener(
      new CompoundUndoManager( wysEditorDocument, new UndoManager() ) );

    Document srcEditorDocument = srcEditor.getDocument();
    srcEditorDocument.addUndoableEditListener(
      new CompoundUndoManager( srcEditorDocument, new UndoManager() ) );
  }

  private void buildToolBar()
  {
    toolBarPanel = new JPanel( new BorderLayout() );
    toolBarPanelContainer = new JPanel( new BorderLayout() );
    toolBarPanel.add( toolBarPanelContainer, BorderLayout.CENTER );
    initToolBarPanel( toolBarPanel );
  }

  private JMenu createMenu( ActionList lst, String menuName )
  {
    JMenu m = ActionUIFactory.getInstance().createMenu( lst );
    m.setText( menuName );
    return m;
  }

  protected AbstractSourceEditor createSourceEditor()
  {
    return new DefaultSourceEditor();
  }

  protected AbstractWysiwygEditor createWysiwygEditor()
  {
    return new DefaultWysiwygEditor();
  }

  public JMenu getEditMenu()
  {
    return editMenu;
  }

  public JMenu getFormatMenu()
  {
    return formatMenu;
  }

  public JMenu getInsertMenu()
  {
    return insertMenu;
  }

  public AbstractEditor getSelectedEditor()
  {
    int pos = tabs.getSelectedIndex();
    return (pos>-1 && pos<editors.size())? editors.get( pos ): null;
  }

  public String getSourceText()
  {
    String txt = removeInvalidTags( srcEditor.getText() );
    txt = deIndent( removeInvalidTags( txt ) );
    txt = Entities.HTML40.unescapeUnknownEntities( txt );
    return txt;
  }

  public String getText()
  {
    String topText = null;
    if (isWysiwygEditorSelected())
    {
      topText = getWysiwygText();
    }
    else if (isSourceEditorSelected())
    {
      topText = getSourceText();
    }
    else
    {
      AbstractEditor editor = getSelectedEditor();
      if (editor!=null)
      {
        topText = editor.getText();
      }
    }
    return (topText!=null)? topText: "";
  }

  public String getWysiwygText()
  {
    return removeInvalidTags( wysEditor.getText() );
  }

  protected void initEditorTabs( AbstractWysiwygEditor wysiwygEditor, AbstractSourceEditor sourceEditor )
  {
    addEditor( wysiwygEditor );
    addEditor( sourceEditor );
  }

  protected void initToolBarPanel( JPanel toolBarPanel )
  {
  }

  public boolean isEditorSelected( AbstractEditor editor )
  {
    int pos = editors.indexOf( editor );
    return pos>-1 && tabs.getSelectedIndex()==pos;
  }

  public boolean isSourceEditorSelected()
  {
    return isEditorSelected( srcEditor );
  }

  public boolean isWysiwygEditorSelected()
  {
    return isEditorSelected( wysEditor );
  }

  public void setCaretPosition( int pos )
  {
    AbstractEditor editor = getSelectedEditor();
    if (editor!=null)
    {
      editor.setCaretPosition( pos );
      editor.requestFocusInWindow();
    }
    //if (isWysiwygEditorSelected())
    //{
    //  wysEditor.setCaretPosition( pos );
    //  wysEditor.requestFocusInWindow();
    //}
    //else if (isSourceEditorSelected())
    //{
    //  srcEditor.setCaretPosition( pos );
    //  srcEditor.requestFocusInWindow();
    //}
  }

  public void setEditorEnabled( AbstractEditor editor, boolean enabled )
  {
    int pos = editors.indexOf( editor );
    if (pos<0) return;
    tabs.setEnabledAt( pos, enabled );
    if (!enabled && tabs.getSelectedIndex()==pos)
    {
      for (int i=0; i<tabs.getTabCount(); i++)
      {
        if (i!=pos && tabs.isEnabledAt( i ))
        {
          tabs.setSelectedIndex( i );
          break;
        }
      }
    }
  }

  public void setSelectedEditor( AbstractEditor editor )
  {
    int pos = editors.indexOf( editor );
    if (pos>-1) tabs.setSelectedIndex( pos );
  }

  public void setSourceEnabled( boolean enabled )
  {
    setEditorEnabled( srcEditor, enabled );
  }

  public void setSourceText( String text )
  {
    String t = deIndent( removeInvalidTags( text ) );
    t = Entities.HTML40.unescapeUnknownEntities( t );
    srcEditor.setText( t );
    srcEditor.setCaretPosition( 0 );
  }

  public void setText( String text )
  {
    String topText = removeInvalidTags( text );
    if (isWysiwygEditorSelected())
    {
      setWysiwygText( topText );
      CompoundUndoManager.discardAllEdits( wysEditor.getDocument() );

    }
    else if (isSourceEditorSelected())
    {
      setSourceText( topText );
      CompoundUndoManager.discardAllEdits( srcEditor.getDocument() );
    }
    else
    {
      AbstractEditor editor = getSelectedEditor();
      if (editor!=null)
      {
        editor.setText( topText );
        CompoundUndoManager.discardAllEdits( editor.getDocument() );
      }
    }
  }

  public void setToolBarVisible( boolean visible )
  {
    toolBarPanel.setVisible( visible );
  }

  public void setWysiwygEnabled( boolean enabled )
  {
    setEditorEnabled( wysEditor, enabled );
  }

  public void setWysiwygText( String text )
  {
    wysEditor.setText( "" );
    wysEditor.insertHTML( text, 0 );
    wysEditor.setCaretPosition( 0 );
  }

  /**
   * called when changing tabs
   */
  private void updateEditor()
  {
    // Code aus dem HTML-Editor in den WYSIWYG-Editor übernehmen
    if (isWysiwygEditorSelected() && currentEditor==srcEditor)
    {
      String topText = removeInvalidTags( srcEditor.getText() );
      wysEditor.setText( "" );
      wysEditor.insertHTML( topText, 0 );
      wysEditor.setCaretPosition( 0 );
      CompoundUndoManager.discardAllEdits( wysEditor.getDocument() );
    }

    // Code aus dem WYSIWYG-Editor in den HTML-Editor übernehmen
    else if (isSourceEditorSelected() && currentEditor==wysEditor)
    {
      String topText = removeInvalidTags( wysEditor.getText() );
      if (isWysTextChanged || srcEditor.getText().equals( "" ))
      {
        String t = deIndent( removeInvalidTags( topText ) );
        t = Entities.HTML40.unescapeUnknownEntities( t );
        srcEditor.setText( t );
        srcEditor.setCaretPosition( 0 );
      }
      CompoundUndoManager.discardAllEdits( srcEditor.getDocument() );
    }

    // gewählten Editor merken
    currentEditor = getSelectedEditor();

    // Toolbar zum gewählten Editor einblenden
    updateToolBar();

    isWysTextChanged = false;
    //paragraphCombo.setEnabled( tabs.getSelectedIndex() == 0 );
    //fontFamilyCombo.setEnabled( tabs.getSelectedIndex() == 0 );
    //updateState();
  }

  private void updateToolBar()
  {
    AbstractEditor editor = getSelectedEditor();
    AbstractToolBar toolBar = (editor!=null)? editor.getToolBar(): null;
    toolBarPanelContainer.removeAll();
    if (toolBar!=null) toolBarPanelContainer.add( toolBar, BorderLayout.CENTER );
    toolBarPanel.revalidate();
    toolBarPanel.repaint();
    setToolBarVisible( toolBar!=null );
  }

  /* *******************************************************************
   *  Methods for dealing with HTML between wysiwyg and source editors
   * ******************************************************************/
  private static String deIndent( String html )
  {
    String ws = "\n    ";
    StringBuilder sb = new StringBuilder( html );
    while (sb.indexOf( ws ) != -1)
    {
      int s = sb.indexOf( ws );
      int e = s + ws.length();
      sb.delete( s, e );
      sb.insert( s, "\n" );
    }
    return sb.toString();
  }

  private static String removeInvalidTags( String html )
  {
    for (int i = 0; i < INVALID_TAGS.length; i++)
    {
      html = deleteOccurance( html, '<' + INVALID_TAGS[i] + '>' );
      html = deleteOccurance( html, "</" + INVALID_TAGS[i] + '>' );
    }
    return html.trim();
  }

  private static String deleteOccurance( String text, String word )
  {
    //if(text == null) return "";
    StringBuilder sb = new StringBuilder( text );
    int p;
    while ((p = sb.toString().toLowerCase().indexOf( word.toLowerCase() )) != -1)
    {
      sb.delete( p, p + word.length() );
    }
    return sb.toString();
  }

  /* ************************************* */

  private class PopupHandler extends MouseAdapter
  {
    @Override
    public void mousePressed( MouseEvent e )
    {
      checkForPopupTrigger( e );
    }

    @Override
    public void mouseReleased( MouseEvent e )
    {
      checkForPopupTrigger( e );
    }

    private void checkForPopupTrigger( MouseEvent e )
    {
      if (e.isPopupTrigger())
      {
        JPopupMenu p = null;
        if (isWysiwygEditorSelected())
        {
          p = wysPopupMenu;
        }
        else if (isSourceEditorSelected())
        {
          p = srcPopupMenu;
        }
        else
        {
          return;
        }
        p.show( e.getComponent(), e.getX(), e.getY() );
      }
    }
  }

  private class FocusHandler implements FocusListener
  {
    public void focusGained( FocusEvent e )
    {
      AbstractEditor editor = getSelectedEditor();
      if (editor!=null)
      {
        CompoundUndoManager.updateUndo( editor.getDocument() );
      }

      /*if (e.getComponent() instanceof JEditorPane)
      {
        JEditorPane ed = (JEditorPane) e.getComponent();
        CompoundUndoManager.updateUndo( ed.getDocument() );
        focusedEditor = ed;

        updateState();
        // updateEnabledStates();
      }*/
    }

    public void focusLost( FocusEvent e )
    {
      //if (e.getComponent() instanceof JEditorPane)
      //{
      //  //focusedEditor = null;
      //  //wysiwygUpdated();
      //}
    }
  }

  private class TextChangedHandler implements DocumentListener
  {
    public void insertUpdate( DocumentEvent e )
    {
      textChanged();
    }

    public void removeUpdate( DocumentEvent e )
    {
      textChanged();
    }

    public void changedUpdate( DocumentEvent e )
    {
      textChanged();
    }

    private void textChanged()
    {
      if (isWysiwygEditorSelected())
      {
        isWysTextChanged = true;
      }
    }
  }

  private class ChangeTabAction extends DefaultAction
  {
    int tab;
    public ChangeTabAction( int tab )
    {
      //super( (tab == 0) ? i18n.str( "rich_text" ): i18n.str( "source" ) );
      super( (tab == 0) ? wysEditor.getTabTitle(): srcEditor.getTabTitle() );
      this.tab = tab;
      putValue( ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_RADIO );
    }

    @Override
    protected void execute( ActionEvent e )
    {
      int pos = editors.indexOf( (tab==0)? wysEditor: srcEditor );
      if (pos>-1) tabs.setSelectedIndex( pos );
      setSelected( pos>-1 );
      //tabs.setSelectedIndex( tab );
      //setSelected( true );
    }

    @Override
    protected void contextChanged()
    {
      int pos = editors.indexOf( (tab==0)? wysEditor: srcEditor );
      setSelected( pos>-1 && tabs.getSelectedIndex() == pos );
      //setSelected( tabs.getSelectedIndex() == tab );
    }
  }
}