package googletranslate;

import com.glaforge.i18n.io.CharsetToolkit;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UnicodeSet;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultStyledDocument;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.TaskMonitor;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.GroupLayout.ParallelGroup;
import org.jdesktop.layout.GroupLayout.SequentialGroup;

public class GoogleTranslateView
  extends FrameView
  implements ActionListener, PropertyChangeListener
{
  static final boolean DEBUG = false;
  Language inputLanguage = Language.valueOf("ENGLISH");
  Language outputLanguage = Language.valueOf("ENGLISH");
  Transliterator translit = null;
  private Task task;
  String theText = null;
  private JButton jButtonCopy;
  private JButton jButtonExport;
  private JButton jButtonImport;
  private JButton jButtonPaste;
  private JButton jButtonSelectInput;
  private JButton jButtonSelectOutput;
  private JButton jButtonTranslate;
  private JComboBox jComboBoxInput;
  private JComboBox jComboBoxInput1;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabelLines;
  private JMenu jMenu3;
  private JMenu jMenu4;
  private JMenuBar jMenuBar;
  private JMenuItem jMenuItem1;
  private JPanel jPanel1;
  private JPanel jPanel2;
  private JPanel jPanel3;
  private JPanel jPanel4;
  private JPanel jPanel5;
  private JPanel jPanel6;
  private JPanel jPanel7;
  private JScrollPane jScrollPane3;
  private JScrollPane jScrollPane4;
  private JTable jTable1;
  private JTable jTable2;
  private JPanel mainPanel;
  private JProgressBar progressBar;
  private JPanel statusPanel;
  private final Timer messageTimer;
  private final Timer busyIconTimer;
  private final Icon idleIcon;
  private final Icon[] busyIcons = new Icon[15];
  private int busyIconIndex = 0;
  private JDialog aboutBox;
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  void setTransliterator(String paramString1, String paramString2)
  {
    Object localObject;
    if (paramString2 == null)
    {
      this.translit = Transliterator.getInstance(paramString1);
    }
    else
    {
      localObject = "";
      int i = paramString2.indexOf('-');
      if (i < 0)
      {
        localObject = paramString2 + "-Any";
        paramString2 = "Any-" + paramString2;
      }
      else
      {
        int j = paramString2.indexOf("/", i);
        if (j < 0) {
          localObject = paramString2.substring(i + 1) + "-" + paramString2.substring(0, i);
        } else {
          localObject = paramString2.substring(i + 1, j) + "-" + paramString2.substring(0, i) + paramString2.substring(j);
        }
      }
      this.translit = Transliterator.createFromRules(paramString2, paramString1, 0);
      System.out.println("***Target Set");
      UnicodeSet localUnicodeSet1 = this.translit.getTargetSet();
      System.out.println(localUnicodeSet1.toPattern(true));
      UnicodeSet localUnicodeSet2 = new UnicodeSet("[a-z]").removeAll(localUnicodeSet1);
      System.out.println("***ASCII - Target Set");
      System.out.println(localUnicodeSet2.toPattern(true));
      Transliterator localTransliterator1 = Transliterator.createFromRules((String)localObject, paramString1, 1);
      Transliterator localTransliterator2 = this.translit.getInverse();
    }
    try
    {
      localObject = this.translit.getInverse();
    }
    catch (Exception localException)
    {
      localObject = null;
    }
    System.out.println("Set transliterator: " + this.translit.getID() + ((localObject == null) || (localObject != null) ? " and " + ((Transliterator)localObject).getID() : ""));
  }
  
  public GoogleTranslateView(SingleFrameApplication paramSingleFrameApplication)
  {
    super(paramSingleFrameApplication);
    setTransliterator("Latin-Greek", null);
    initComponents();
    DefaultTableModel localDefaultTableModel1 = new DefaultTableModel();
    localDefaultTableModel1.addColumn("");
    localDefaultTableModel1.setNumRows(100);
    this.jTable1.setModel(localDefaultTableModel1);
    DefaultTableModel localDefaultTableModel2 = new DefaultTableModel();
    localDefaultTableModel2.addColumn("");
    localDefaultTableModel2.setNumRows(100);
    this.jTable2.setModel(localDefaultTableModel2);
    this.jComboBoxInput.setModel(new DefaultComboBoxModel(new String[] { "Accents", "Arabic", "Armenian", "Bengali", "Cyrillic", "Devnagari", "Digit", "Fullwidth", "Georgian", "Greek", "Gujarati", "Gurmukhi", "Halfwidth", "Han", "Hangul", "Hebrew", "Hiragana", "Jamo", "Kannada", "Katakana", "Latin", "Malayalam", "Name", "NumericPinyin", "Oryia", "Pinyin", "Publishing", "Simplified", "Syriac", "Thamil", "Thelugu", "Thaana" }));
    this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Any" }));
    ResourceMap localResourceMap = getResourceMap();
    int i = localResourceMap.getInteger("StatusBar.messageTimeout").intValue();
    this.messageTimer = new Timer(i, new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent) {}
    });
    this.messageTimer.setRepeats(false);
    int j = localResourceMap.getInteger("StatusBar.busyAnimationRate").intValue();
    for (int k = 0; k < this.busyIcons.length; k++) {
      this.busyIcons[k] = localResourceMap.getIcon("StatusBar.busyIcons[" + k + "]");
    }
    this.busyIconTimer = new Timer(j, new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        GoogleTranslateView.this.busyIconIndex = ((GoogleTranslateView.this.busyIconIndex + 1) % GoogleTranslateView.this.busyIcons.length);
      }
    });
    this.idleIcon = localResourceMap.getIcon("StatusBar.idleIcon");
    this.progressBar.setVisible(true);
    TaskMonitor localTaskMonitor = new TaskMonitor(getApplication().getContext());
    localTaskMonitor.addPropertyChangeListener(new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent)
      {
        String str1 = paramAnonymousPropertyChangeEvent.getPropertyName();
        System.out.println("property change");
        if ("started".equals(str1))
        {
          if (!GoogleTranslateView.this.busyIconTimer.isRunning())
          {
            GoogleTranslateView.this.busyIconIndex = 0;
            GoogleTranslateView.this.busyIconTimer.start();
          }
          GoogleTranslateView.this.progressBar.setVisible(true);
          GoogleTranslateView.this.progressBar.setIndeterminate(true);
        }
        else if ("done".equals(str1))
        {
          System.out.println("task is done");
          GoogleTranslateView.this.busyIconTimer.stop();
          GoogleTranslateView.this.progressBar.setVisible(false);
          GoogleTranslateView.this.progressBar.setValue(0);
        }
        else if ("message".equals(str1))
        {
          String str2 = (String)paramAnonymousPropertyChangeEvent.getNewValue();
          GoogleTranslateView.this.messageTimer.restart();
        }
        else if ("progress".equals(str1))
        {
          int i = ((Integer)paramAnonymousPropertyChangeEvent.getNewValue()).intValue();
          GoogleTranslateView.this.progressBar.setVisible(true);
          GoogleTranslateView.this.progressBar.setIndeterminate(false);
          GoogleTranslateView.this.progressBar.setValue(i);
        }
      }
    });
  }
  
  public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
  {
    String str = paramPropertyChangeEvent.getPropertyName();
    if ("progress".equals(str))
    {
      int i = ((Integer)paramPropertyChangeEvent.getNewValue()).intValue();
      this.progressBar.setVisible(true);
      this.progressBar.setIndeterminate(false);
      this.progressBar.setValue(i);
    }
  }
  
  @Action
  public void showAboutBox()
  {
    if (this.aboutBox == null)
    {
      JFrame localJFrame = GoogleTranslateApp.getApplication().getMainFrame();
      this.aboutBox = new GoogleTranslateAboutBox(localJFrame);
      this.aboutBox.setLocationRelativeTo(localJFrame);
    }
    GoogleTranslateApp.getApplication().show(this.aboutBox);
  }
  
  private void initComponents()
  {
    this.mainPanel = new JPanel();
    this.jPanel1 = new JPanel();
    this.jPanel5 = new JPanel();
    this.jButtonSelectInput = new JButton();
    this.jButtonPaste = new JButton();
    this.jButtonImport = new JButton();
    this.jPanel4 = new JPanel();
    this.jLabel1 = new JLabel();
    this.jComboBoxInput = new JComboBox();
    this.jScrollPane4 = new JScrollPane();
    this.jTable2 = new JTable();
    this.jPanel2 = new JPanel();
    this.jPanel7 = new JPanel();
    this.jButtonSelectOutput = new JButton();
    this.jButtonCopy = new JButton();
    this.jButtonExport = new JButton();
    this.jScrollPane3 = new JScrollPane();
    this.jTable1 = new JTable();
    this.jPanel6 = new JPanel();
    this.jLabel2 = new JLabel();
    this.jComboBoxInput1 = new JComboBox();
    this.jPanel3 = new JPanel();
    this.jButtonTranslate = new JButton();
    this.jMenuBar = new JMenuBar();
    this.jMenu3 = new JMenu();
    this.jMenuItem1 = new JMenuItem();
    this.jMenu4 = new JMenu();
    this.statusPanel = new JPanel();
    this.progressBar = new JProgressBar();
    this.jLabelLines = new JLabel();
    this.mainPanel.setName("mainPanel");
    this.mainPanel.setLayout(new BoxLayout(this.mainPanel, 2));
    this.jPanel1.setBorder(BorderFactory.createEtchedBorder());
    this.jPanel1.setLayout(new GridBagLayout());
    this.jPanel5.setLayout(new GridBagLayout());
    this.jButtonSelectInput.setText("Select All");
    this.jButtonSelectInput.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        GoogleTranslateView.this.jButtonSelectInputActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.jPanel5.add(this.jButtonSelectInput, new GridBagConstraints());
    this.jButtonPaste.setText("Paste");
    this.jButtonPaste.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        GoogleTranslateView.this.jButtonPasteActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.jPanel5.add(this.jButtonPaste, new GridBagConstraints());
    this.jButtonImport.setText("Import");
    this.jButtonImport.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent paramAnonymousMouseEvent)
      {
        GoogleTranslateView.this.jButtonImportMouseClicked(paramAnonymousMouseEvent);
      }
    });
    this.jButtonImport.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        GoogleTranslateView.this.jButtonImportActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.jPanel5.add(this.jButtonImport, new GridBagConstraints());
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.anchor = 15;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.weighty = 1.0D;
    this.jPanel1.add(this.jPanel5, localGridBagConstraints);
    this.jLabel1.setText("Input Language");
    this.jComboBoxInput.setModel(new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    this.jComboBoxInput.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        GoogleTranslateView.this.jComboBoxInputActionPerformed(paramAnonymousActionEvent);
      }
    });
    GroupLayout localGroupLayout1 = new GroupLayout(this.jPanel4);
    this.jPanel4.setLayout(localGroupLayout1);
    localGroupLayout1.setHorizontalGroup(localGroupLayout1.createParallelGroup(1).add(0, 186, 32767).add(localGroupLayout1.createParallelGroup(1).add(localGroupLayout1.createSequentialGroup().add(0, 0, 32767).add(this.jLabel1).add(3, 3, 3).add(this.jComboBoxInput, -2, -1, -2).add(0, 0, 32767))));
    localGroupLayout1.setVerticalGroup(localGroupLayout1.createParallelGroup(1).add(0, 23, 32767).add(localGroupLayout1.createParallelGroup(1).add(localGroupLayout1.createSequentialGroup().add(0, 0, 32767).add(localGroupLayout1.createParallelGroup(1).add(localGroupLayout1.createSequentialGroup().add(5, 5, 5).add(this.jLabel1)).add(this.jComboBoxInput, -2, -1, -2)).add(0, 0, 32767))));
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.weighty = 1.0D;
    this.jPanel1.add(this.jPanel4, localGridBagConstraints);
    this.jScrollPane4.setPreferredSize(new Dimension(218, 403));
    this.jTable2.setFont(new Font("Code2000", 0, 14));
    this.jTable2.setModel(new DefaultTableModel(new Object[][] { { null }, { null }, { null }, { null } }, new String[] { "Title 1" }));
    this.jTable2.setIntercellSpacing(new Dimension(0, 0));
    this.jTable2.setOpaque(false);
    this.jTable2.setSelectionMode(2);
    this.jScrollPane4.setViewportView(this.jTable2);
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.weighty = 8.0D;
    this.jPanel1.add(this.jScrollPane4, localGridBagConstraints);
    this.mainPanel.add(this.jPanel1);
    this.jPanel2.setBorder(BorderFactory.createEtchedBorder());
    this.jPanel2.setLayout(new GridBagLayout());
    this.jPanel7.setLayout(new GridBagLayout());
    this.jButtonSelectOutput.setText("Select All");
    this.jButtonSelectOutput.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        GoogleTranslateView.this.jButtonSelectOutputActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.jPanel7.add(this.jButtonSelectOutput, new GridBagConstraints());
    this.jButtonCopy.setText("Copy");
    this.jButtonCopy.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        GoogleTranslateView.this.jButtonCopyActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.jPanel7.add(this.jButtonCopy, new GridBagConstraints());
    this.jButtonExport.setText("Export");
    this.jButtonExport.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        GoogleTranslateView.this.jButtonExportActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.jPanel7.add(this.jButtonExport, new GridBagConstraints());
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 2;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.anchor = 11;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.weighty = 1.0D;
    this.jPanel2.add(this.jPanel7, localGridBagConstraints);
    this.jScrollPane3.setPreferredSize(new Dimension(218, 403));
    this.jTable1.setFont(new Font("Code2000", 0, 14));
    this.jTable1.setModel(new DefaultTableModel(new Object[][] { { null }, { null }, { null }, { null } }, new String[] { "Title 1" }));
    this.jTable1.setIntercellSpacing(new Dimension(0, 0));
    this.jTable1.setOpaque(false);
    this.jTable1.setSelectionMode(2);
    this.jScrollPane3.setViewportView(this.jTable1);
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 1;
    localGridBagConstraints.fill = 1;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.weighty = 8.0D;
    this.jPanel2.add(this.jScrollPane3, localGridBagConstraints);
    this.jLabel2.setText("Input Language");
    this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    this.jComboBoxInput1.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        GoogleTranslateView.this.jComboBoxInput1ActionPerformed(paramAnonymousActionEvent);
      }
    });
    GroupLayout localGroupLayout2 = new GroupLayout(this.jPanel6);
    this.jPanel6.setLayout(localGroupLayout2);
    localGroupLayout2.setHorizontalGroup(localGroupLayout2.createParallelGroup(1).add(0, 186, 32767).add(localGroupLayout2.createParallelGroup(1).add(localGroupLayout2.createSequentialGroup().add(0, 0, 32767).add(this.jLabel2).add(3, 3, 3).add(this.jComboBoxInput1, -2, -1, -2).add(0, 0, 32767))));
    localGroupLayout2.setVerticalGroup(localGroupLayout2.createParallelGroup(1).add(0, 23, 32767).add(localGroupLayout2.createParallelGroup(1).add(localGroupLayout2.createSequentialGroup().add(0, 0, 32767).add(localGroupLayout2.createParallelGroup(1).add(localGroupLayout2.createSequentialGroup().add(5, 5, 5).add(this.jLabel2)).add(this.jComboBoxInput1, -2, -1, -2)).add(0, 0, 32767))));
    localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.gridx = 0;
    localGridBagConstraints.gridy = 0;
    localGridBagConstraints.weightx = 1.0D;
    localGridBagConstraints.weighty = 1.0D;
    this.jPanel2.add(this.jPanel6, localGridBagConstraints);
    this.mainPanel.add(this.jPanel2);
    this.jPanel3.setBorder(BorderFactory.createEtchedBorder());
    this.jButtonTranslate.setText("Transliterate");
    this.jButtonTranslate.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent paramAnonymousActionEvent)
      {
        GoogleTranslateView.this.jButtonTranslateActionPerformed(paramAnonymousActionEvent);
      }
    });
    this.jPanel3.add(this.jButtonTranslate);
    this.mainPanel.add(this.jPanel3);
    this.jMenu3.setText("File");
    this.jMenuItem1.setText("jMenuItem1");
    this.jMenu3.add(this.jMenuItem1);
    this.jMenuBar.add(this.jMenu3);
    this.jMenu4.setText("Edit");
    this.jMenuBar.add(this.jMenu4);
    this.statusPanel.setMinimumSize(new Dimension(20, 50));
    this.statusPanel.setName("statusPanel");
    this.statusPanel.setPreferredSize(new Dimension(148, 20));
    this.progressBar.setMinimumSize(new Dimension(20, 20));
    this.progressBar.setPreferredSize(new Dimension(200, 20));
    GroupLayout localGroupLayout3 = new GroupLayout(this.statusPanel);
    this.statusPanel.setLayout(localGroupLayout3);
    localGroupLayout3.setHorizontalGroup(localGroupLayout3.createParallelGroup(1).add(2, localGroupLayout3.createSequentialGroup().addContainerGap().add(this.jLabelLines, -2, 271, -2).addPreferredGap(0, 353, 32767).add(this.progressBar, -2, -1, -2)));
    localGroupLayout3.setVerticalGroup(localGroupLayout3.createParallelGroup(1).add(localGroupLayout3.createSequentialGroup().add(localGroupLayout3.createParallelGroup(2).add(1, this.jLabelLines, -1, 20, 32767).add(1, this.progressBar, -2, -1, -2)).addContainerGap()));
    setComponent(this.mainPanel);
    setStatusBar(this.statusPanel);
  }
  
  private void jButtonTranslateActionPerformed(ActionEvent paramActionEvent)
  {
    if (this.jButtonTranslate.getText() == "Cancel")
    {
      System.out.println("Cancel");
      this.task.cancel(true);
      this.task.removePropertyChangeListener(this);
      return;
    }
    System.out.println("inputLanguage = " + this.inputLanguage + "  outputLanguage = " + this.outputLanguage);
    DefaultTableModel localDefaultTableModel = (DefaultTableModel)this.jTable2.getModel();
    this.jLabelLines.setText("" + localDefaultTableModel.getRowCount() + " lines");
    try
    {
      this.task = new Task();
      this.task.addPropertyChangeListener(this);
      this.task.execute();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  private void jButtonImportActionPerformed(ActionEvent paramActionEvent)
  {
    JFileChooser localJFileChooser = new JFileChooser();
    ExampleFileFilter localExampleFileFilter = new ExampleFileFilter("txt");
    localExampleFileFilter.setDescription("text files");
    localJFileChooser.addChoosableFileFilter(localExampleFileFilter);
    localJFileChooser.setAcceptAllFileFilterUsed(true);
    localJFileChooser.setFileFilter(localExampleFileFilter);
    int i = localJFileChooser.showOpenDialog(this.mainPanel);
    if (i == 0)
    {
      this.jTable2.removeAll();
      File localFile = localJFileChooser.getSelectedFile();
      try
      {
        Charset localCharset = getEncoding(localFile.getAbsolutePath());
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(localFile), localCharset));
        StringBuilder localStringBuilder = new StringBuilder();
        long l = 0L;
        DefaultTableModel localDefaultTableModel = (DefaultTableModel)this.jTable2.getModel();
        localDefaultTableModel.setRowCount(0);
        String str;
        while ((str = localBufferedReader.readLine()) != null)
        {
          localDefaultTableModel.addRow(new Object[] { str });
          localStringBuilder.append(System.getProperty("line.separator"));
          l += 1L;
        }
        this.jLabelLines.setText("" + localDefaultTableModel.getRowCount() + " lines");
        localBufferedReader.close();
        DefaultStyledDocument localDefaultStyledDocument = new DefaultStyledDocument();
        localStringBuilder.delete(0, localStringBuilder.length());
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, localUnsupportedEncodingException);
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, localFileNotFoundException);
      }
      catch (IOException localIOException)
      {
        Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, localIOException);
      }
    }
  }
  
  private static Charset getEncoding(String paramString)
  {
    String str = System.getProperty("file.encoding");
    BufferedReader localBufferedReader = null;
    try
    {
      localBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(paramString)));
      char[] arrayOfChar = new char[3];
      int i = localBufferedReader.read(arrayOfChar);
      System.out.println(new PrintfFormat("'%x'").sprintf(arrayOfChar[0]));
      System.out.println(new PrintfFormat("'%x'").sprintf(arrayOfChar[1]));
      System.out.println(new PrintfFormat("'%x'").sprintf(arrayOfChar[2]));
      if ((i >= 2) && (((arrayOfChar[0] == 'ÿ') && (arrayOfChar[1] == 'þ')) || ((arrayOfChar[0] == 'þ') && (arrayOfChar[1] == 'ÿ')) || (arrayOfChar[0] == 65533))) {
        str = "UTF16";
      }
      if ((i >= 3) && (arrayOfChar[0] == 'ï') && (arrayOfChar[1] == '»') && (arrayOfChar[2] == '¿')) {
        str = "UTF8";
      }
      if (localBufferedReader != null) {
        try
        {
          localBufferedReader.close();
        }
        catch (IOException localIOException1) {}
      }
      localCharset = null;
    }
    catch (IOException localIOException2) {}finally
    {
      if (localBufferedReader != null) {
        try
        {
          localBufferedReader.close();
        }
        catch (IOException localIOException5) {}
      }
    }
    Charset localCharset;
    try
    {
      localCharset = CharsetToolkit.guessEncoding(new File(paramString), 100);
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, localFileNotFoundException);
    }
    catch (IOException localIOException4)
    {
      Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, localIOException4);
    }
    System.out.println("guess = " + localCharset);
    return localCharset;
  }
  
  private void jButtonExportActionPerformed(ActionEvent paramActionEvent)
  {
    JFileChooser localJFileChooser = new JFileChooser();
    ExampleFileFilter localExampleFileFilter = new ExampleFileFilter("txt");
    localExampleFileFilter.setDescription("text files");
    localJFileChooser.addChoosableFileFilter(localExampleFileFilter);
    localJFileChooser.setAcceptAllFileFilterUsed(true);
    localJFileChooser.setFileFilter(localExampleFileFilter);
    int i = localJFileChooser.showSaveDialog(this.mainPanel);
    if (i == 0)
    {
      File localFile = localJFileChooser.getSelectedFile();
      if (!localFile.getName().contains(".txt")) {
        localFile = new File(localFile.getPath() + ".txt");
      }
      try
      {
        BufferedWriter localBufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(localFile), "UTF-8"));
        String str = this.theText;
        localBufferedWriter.write(str, 0, str.length());
        localBufferedWriter.close();
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, localUnsupportedEncodingException);
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, localFileNotFoundException);
      }
      catch (IOException localIOException)
      {
        Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, localIOException);
      }
    }
  }
  
  private void jButtonPasteActionPerformed(ActionEvent paramActionEvent)
  {
    Clipboard localClipboard1 = Toolkit.getDefaultToolkit().getSystemClipboard();
    String str = "";
    Clipboard localClipboard2 = Toolkit.getDefaultToolkit().getSystemClipboard();
    Transferable localTransferable = localClipboard2.getContents(null);
    int i = (localTransferable != null) && (localTransferable.isDataFlavorSupported(DataFlavor.stringFlavor)) ? 1 : 0;
    if (i != 0) {
      try
      {
        str = (String)localTransferable.getTransferData(DataFlavor.stringFlavor);
        String[] arrayOfString = str.split("\n");
        DefaultTableModel localDefaultTableModel = (DefaultTableModel)this.jTable2.getModel();
        localDefaultTableModel.setRowCount(0);
        for (int j = 0; j < arrayOfString.length; j++) {
          localDefaultTableModel.addRow(new Object[] { arrayOfString[j] });
        }
      }
      catch (UnsupportedFlavorException localUnsupportedFlavorException)
      {
        System.out.println(localUnsupportedFlavorException);
        localUnsupportedFlavorException.printStackTrace();
      }
      catch (IOException localIOException)
      {
        System.out.println(localIOException);
        localIOException.printStackTrace();
      }
    }
  }
  
  private void jButtonCopyActionPerformed(ActionEvent paramActionEvent)
  {
    StringSelection localStringSelection = new StringSelection(this.theText);
    Clipboard localClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    localClipboard.setContents(localStringSelection, localStringSelection);
  }
  
  private void jButtonSelectInputActionPerformed(ActionEvent paramActionEvent)
  {
    this.jTable2.selectAll();
  }
  
  private void jButtonSelectOutputActionPerformed(ActionEvent paramActionEvent)
  {
    this.jTable1.selectAll();
  }
  
  private void jComboBoxInputActionPerformed(ActionEvent paramActionEvent)
  {
    String str = (String)this.jComboBoxInput.getSelectedItem();
    System.out.println("selected = " + str);
    if (str.equals("Armenian"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Latin" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Arabic"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Latin" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Accents"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Any" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Bengali"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Devanagari", "Gujarati", "Gurmukhi", "Kannada", "Latin", "Malayalam", "Oriya", "Tamil", "Telugu" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Latin"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Arabic", "Armenian", "Bengali", "Cyrillic", "Devanagari", "Georgian", "Greek", "Gujarati", "Gurmukhi", "Han", "Hangul", "Hebrew", "Hiragana", "Jamo", "Kannada", "Katakana", "Malayalam", "NumericPinyin", "Oriya", "Syriac", "Tamil", "Telugu", "Thaana", "Thai" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Cyrillic"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Latin" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Devanagari"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Bengali", "Gujarati", "Gurmukhi", "Kannada", "Latin", "Malayalam", "Oriya", "Tamil", "Telugu" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Digit"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Tone" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Fullwidth"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Halfwidth" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Gujarati"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Bengali", "Devanagari", "Gurmukhi", "Kannada", "Latin", "Malayalam", "Oriya", "Tamil", "Telugu" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Gurmukhi"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Bengali", "Devanagari", "Gujarati", "Kannada", "Latin", "Malayalam", "Oriya", "Tamil", "Telugu" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Halfwidth"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Fullwidth" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Cyrillic"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Latin" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Hiragana"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Katakana", "Latin" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Kannada"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Bengali", "Devanagari", "Gujarati", "Gurmukhi", "Kannada", "Latin", "Malayalam", "Oriya", "Tamil", "Telugu" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Katakana"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Hiragana", "Latin" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Malayalam"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Bengali", "Devanagari", "Gujarati", "Gurmukhi", "Kannada", "Latin", "Oriya", "Tamil", "Telugu" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Name"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Any" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("NumericPinyin"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Latin", "Pinyin" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Oriya"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Bengali", "Devanagari", "Gujarati", "Gurmukhi", "Kannada", "Latin", "Malayalam ", "Tamil", "Telugu" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Pinyin"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "NumericPinyin" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Publishing"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Any" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Simplified"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Traditional" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Tamil"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Bengali", "Devanagari", "Gujarati", "Gurmukhi", "Kannada", "Latin", "Malayalam ", "Oriya", "Telugu" }));
      this.jComboBoxInput1.repaint();
    }
    else if (str.equals("Telugu"))
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Bengali", "Devanagari", "Gujarati", "Gurmukhi", "Kannada", "Latin", "Malayalam ", "Oriya", "Tamil" }));
      this.jComboBoxInput1.repaint();
    }
    else
    {
      this.jComboBoxInput1.setModel(new DefaultComboBoxModel(new String[] { "Latin" }));
      this.jComboBoxInput1.repaint();
    }
    this.jComboBoxInput1.setSelectedIndex(0);
  }
  
  private void jComboBoxInput1ActionPerformed(ActionEvent paramActionEvent)
  {
    String str1 = (String)this.jComboBoxInput.getSelectedItem();
    String str2 = (String)this.jComboBoxInput1.getSelectedItem();
    String str3 = str1 + "-" + str2;
    System.out.println("tra = " + str3);
    setTransliterator(str3, null);
  }
  
  private void jButtonImportMouseClicked(MouseEvent paramMouseEvent) {}
  
  class Task
    extends SwingWorker<Void, Void>
  {
    Task() {}
    
    public Void doInBackground()
    {
      System.out.println("łiiiiiiiii");
      GoogleTranslateView.this.progressBar.setStringPainted(true);
      GoogleTranslateView.this.jButtonTranslate.setText("Cancel");
      setProgress(0);
      StringBuilder localStringBuilder1 = new StringBuilder();
      DefaultTableModel localDefaultTableModel1 = (DefaultTableModel)GoogleTranslateView.this.jTable2.getModel();
      for (int i = 0; i < localDefaultTableModel1.getRowCount(); i++)
      {
        localStringBuilder1.append(localDefaultTableModel1.getValueAt(i, 0));
        localStringBuilder1.append(System.getProperty("line.separator"));
      }
      String str1 = localStringBuilder1.toString();
      int j = str1.length();
      int k = j / 100;
      String str2 = null;
      StringBuilder localStringBuilder2 = new StringBuilder();
      int m = 0;
      Object localObject;
      DefaultTableModel localDefaultTableModel2;
      int i1;
      if (k < 10)
      {
        String str3 = GoogleTranslateView.this.translit.transliterate(str1);
        GoogleTranslateView.this.theText = str3;
        localObject = str3.split("\n");
        localDefaultTableModel2 = (DefaultTableModel)GoogleTranslateView.this.jTable1.getModel();
        localDefaultTableModel2.setRowCount(0);
        for (i1 = 0; i1 < localObject.length; i1++) {
          localDefaultTableModel2.addRow(new Object[] { localObject[i1] });
        }
        setProgress(100);
        GoogleTranslateView.this.jLabelLines.setText(GoogleTranslateView.this.jLabelLines.getText() + " / " + localDefaultTableModel2.getRowCount() + " lines transliterated");
      }
      else
      {
        int n = 0;
        while (n < j)
        {
          if (n + k > j) {
            k = j - n;
          }
          str2 = str1.substring(n, n + k);
          localObject = GoogleTranslateView.this.translit.transliterate(str2);
          localStringBuilder2.append((String)localObject);
          if (m > 100) {
            m = 100;
          }
          setProgress(m);
          m++;
          n += k;
        }
        System.out.println("-------------------------------");
        String str4 = localStringBuilder2.toString();
        GoogleTranslateView.this.theText = new String(str4);
        localObject = GoogleTranslateView.this.theText.split("\n");
        localDefaultTableModel2 = (DefaultTableModel)GoogleTranslateView.this.jTable1.getModel();
        localDefaultTableModel2.setRowCount(0);
        for (i1 = 0; i1 < localObject.length; i1++) {
          localDefaultTableModel2.addRow(new Object[] { localObject[i1] });
        }
        GoogleTranslateView.this.jLabelLines.setText(GoogleTranslateView.this.jLabelLines.getText() + " / " + localDefaultTableModel2.getRowCount() + " lines transliterated");
      }
      return null;
    }
    
    public void done()
    {
      GoogleTranslateView.this.jButtonTranslate.setText("Transliterate");
      GoogleTranslateView.this.progressBar.setValue(0);
    }
  }
}


/* Location:              /home/jas/sites/icu-transliteration-application/Translit_Application.jar!/googletranslate/GoogleTranslateView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */