/*
 * GoogleTranslateView.java
 */

package googletranslate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;

import com.ibm.icu.lang.*;
import com.ibm.icu.text.*;

//import com.google.api.translate.Language;
//import com.google.api.translate.Translate;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;


/**
 * The application's main frame.
 */
public class GoogleTranslateView extends FrameView implements ActionListener,
                                        PropertyChangeListener {
    static final boolean DEBUG = false;
    Language inputLanguage = Language.valueOf("ENGLISH") ;
    Language outputLanguage = Language.valueOf("ENGLISH") ;
    Transliterator translit = null ;
     private Task task;
     String theText = null ;
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            System.out.println("łiiiiiiiii") ;
                progressBar.setStringPainted(true);
                jButtonTranslate.setText("Cancel");
                setProgress(0);

              //  String inputText = jTextAreaInput.getDocument().getText(0, jTextAreaInput.getDocument().getLength());
                StringBuilder input = new StringBuilder() ;
                DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel();
                for(int i = 0 ; i < model2.getRowCount() ; i++ ){
                    input.append(model2.getValueAt(i, 0)) ;
                    input.append(System.getProperty("line.separator"));
                }
                String inputText = input.toString() ;
                int length = inputText.length() ;
                int iter = length/100 ;
                String part = null ;
                final StringBuilder sb = new StringBuilder() ;
                int p = 0 ;
                if(iter < 10){
                    String output = translit.transliterate(inputText ) ;
                    theText = output ;
                    String[] lines = output.split("\n") ;
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel() ;
                    model.setRowCount(0);
                    for(int i = 0 ; i < lines.length ; i++){
                       model.addRow(new Object[]{lines[i]});
                    }
                    setProgress(100);
                                        jLabelLines.setText(jLabelLines.getText()+" / " + model.getRowCount() +" lines transliterated");

                }else {
                    for(int i = 0 ; i < length ;i+=iter){
                       //System.out.println("i =  "+i+" iter = "+iter+" lenth = "+length +" p = "+p) ;
                        if((i+iter)> length){
                            iter = length - i ;
                        }
                        part = inputText.substring(i, i+iter) ;
                      //  System.out.println("part = "+part);
                        String output = translit.transliterate(part) ;
                    //    System.out.println("output = "+output) ;
                        sb.append(output) ;
                        if(p>100)p=100 ;
                        setProgress(p);
                        p++;
                    }
                    System.out.println("-------------------------------") ;
                  //  System.out.println(sb.toString()) ;
                    final String o = sb.toString() ;
                    theText = new String(o) ;
                    String[] lines = theText.split("\n") ;
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel() ;
                    model.setRowCount(0); 
                    for(int i = 0 ; i < lines.length ; i++){
                       model.addRow(new Object[]{lines[i]});
                    }
                    jLabelLines.setText(jLabelLines.getText()+" / " + model.getRowCount() +" lines transliterated");
//                    try {
//                        SwingUtilities.invokeAndWait(new Runnable() {
//
//                            @Override
//                            public void run() {
//                                // try {
//                                // DefaultStyledDocument doc = new DefaultStyledDocument();
//                                //doc.insertString(0, o, null);
//                                //jTextAreaOutput.setDocument(doc);
//                                jTextAreaOutput.setText(o);
//                                //  } catch (BadLocationException ex) {
//                                //     Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, ex);
//                                //}
//                            }
//                        });
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (InvocationTargetException ex) {
//                        Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                   
                }

            return null ;
            
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
             jButtonTranslate.setText("Transliterate");
             progressBar.setValue(0);
             //this.
            //Toolkit.getDefaultToolkit().beep();
//            setCursor(null); //turn off the wait cursor
//            taskOutput.append("Done!\n");
        }
    }
      void setTransliterator(String name, String id) {
        if (DEBUG) System.out.println("Got: " + name);
        if (id == null) {
            translit = Transliterator.getInstance(name);
        } else {
            String reverseId = "";
            int pos = id.indexOf('-');
            if (pos < 0) {
                reverseId = id + "-Any";
                id = "Any-" + id;
            } else {
                int pos2 = id.indexOf("/", pos);
                if (pos2 < 0) {
                    reverseId = id.substring(pos+1) + "-" + id.substring(0,pos);
                } else {
                    reverseId = id.substring(pos+1, pos2) + "-" + id.substring(0,pos) + id.substring(pos2);
                }
            }


            translit = Transliterator.createFromRules(id, name, Transliterator.FORWARD);
            if (DEBUG) {
                System.out.println("***Forward Rules");
                System.out.println(translit.toRules(true));
                System.out.println("***Source Set");
                System.out.println(translit.getSourceSet().toPattern(true));
            }
                System.out.println("***Target Set");
                UnicodeSet target = translit.getTargetSet();
                System.out.println(target.toPattern(true));
                UnicodeSet rest = new UnicodeSet("[a-z]").removeAll(target);
                System.out.println("***ASCII - Target Set");
                System.out.println(rest.toPattern(true));

        //    DummyFactory.add(id, translit);

            Transliterator translit2 = Transliterator.createFromRules(reverseId, name, Transliterator.REVERSE);
            if (DEBUG) {
                System.out.println("***Backward Rules");
                System.out.println(translit2.toRules(true));
            }
        //    DummyFactory.add(reverseId, translit2);

            Transliterator rev = translit.getInverse();
            if (DEBUG) System.out.println("***Inverse Rules");
            if (DEBUG) System.out.println(rev.toRules(true));

        }
      //  text.flush();
//        text.setTransliterator(translit);
      //  convertSelectionItem.setLabel(Transliterator.getDisplayName(translit.getID()));

     //   addHistory(translit);

        Transliterator inv;
        try {
            inv = translit.getInverse();
        } catch (Exception ex) {
            inv = null;
        }
        if (inv != null) {
            //addHistory(inv);
           // swapSelectionItem.setEnabled(true);
        } else {
          //  swapSelectionItem.setEnabled(false);
        }
        System.out.println("Set transliterator: " + translit.getID()
            + (inv != null ? " and " + inv.getID() : ""));
    }
    public GoogleTranslateView(SingleFrameApplication app) {
        super(app);
        setTransliterator("Latin-Greek", null);
        initComponents();

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("");
        model.setNumRows(100) ;
       //model.
        jTable1.setModel(model);
        DefaultTableModel model2 = new DefaultTableModel();
        model2.addColumn("");
        model2.setNumRows(100) ;
       //model.
        jTable2.setModel(model2);

       // jTable1.s

        jComboBoxInput.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Accents","Arabic","Armenian","Bengali"
        ,"Cyrillic","Devnagari","Digit","Fullwidth","Georgian","Greek","Gujarati","Gurmukhi","Halfwidth","Han","Hangul",
         "Hebrew","Hiragana","Jamo","Kannada","Katakana","Latin","Malayalam","Name","NumericPinyin","Oryia","Pinyin",
        "Publishing","Simplified","Syriac","Thamil","Thelugu","Thaana" }));
                            //jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"null"}));
        jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Any"}));
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
//                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
       // statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(true);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());

        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                System.out.println("property change") ;
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                       // statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    System.out.println("task is done") ;
                    busyIconTimer.stop();
                   // statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                  //  statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
      
    }
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String property = propertyChangeEvent.getPropertyName();
        if("progress".equals(property)){
                    int value = (Integer)(propertyChangeEvent.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
        }
      }
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = GoogleTranslateApp.getApplication().getMainFrame();
            aboutBox = new GoogleTranslateAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        GoogleTranslateApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jButtonSelectInput = new javax.swing.JButton();
        jButtonPaste = new javax.swing.JButton();
        jButtonImport = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxInput = new javax.swing.JComboBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jButtonSelectOutput = new javax.swing.JButton();
        jButtonCopy = new javax.swing.JButton();
        jButtonExport = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxInput1 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jButtonTranslate = new javax.swing.JButton();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        statusPanel = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        jLabelLines = new javax.swing.JLabel();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new javax.swing.BoxLayout(mainPanel, javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jButtonSelectInput.setText("Select All");
        jButtonSelectInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectInputActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonSelectInput, new java.awt.GridBagConstraints());

        jButtonPaste.setText("Paste");
        jButtonPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPasteActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonPaste, new java.awt.GridBagConstraints());

        jButtonImport.setText("Import");
        jButtonImport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonImportMouseClicked(evt);
            }
        });
        jButtonImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportActionPerformed(evt);
            }
        });
        jPanel5.add(jButtonImport, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel5, gridBagConstraints);

        jLabel1.setText("Input Language");

        jComboBoxInput.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxInputActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 186, Short.MAX_VALUE)
            .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel4Layout.createSequentialGroup()
                    .add(0, 0, Short.MAX_VALUE)
                    .add(jLabel1)
                    .add(3, 3, 3)
                    .add(jComboBoxInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 0, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 23, Short.MAX_VALUE)
            .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel4Layout.createSequentialGroup()
                    .add(0, 0, Short.MAX_VALUE)
                    .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel4Layout.createSequentialGroup()
                            .add(5, 5, 5)
                            .add(jLabel1))
                        .add(jComboBoxInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(0, 0, Short.MAX_VALUE)))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel4, gridBagConstraints);

        jScrollPane4.setPreferredSize(new java.awt.Dimension(218, 403));

        jTable2.setFont(new java.awt.Font("Code2000", 0, 14));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Title 1"
            }
        ));
        jTable2.setIntercellSpacing(new java.awt.Dimension(0, 0));
        jTable2.setOpaque(false);
        jTable2.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jScrollPane4.setViewportView(jTable2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 8.0;
        jPanel1.add(jScrollPane4, gridBagConstraints);

        mainPanel.add(jPanel1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jButtonSelectOutput.setText("Select All");
        jButtonSelectOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectOutputActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonSelectOutput, new java.awt.GridBagConstraints());

        jButtonCopy.setText("Copy");
        jButtonCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCopyActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonCopy, new java.awt.GridBagConstraints());

        jButtonExport.setText("Export");
        jButtonExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportActionPerformed(evt);
            }
        });
        jPanel7.add(jButtonExport, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel7, gridBagConstraints);

        jScrollPane3.setPreferredSize(new java.awt.Dimension(218, 403));

        jTable1.setFont(new java.awt.Font("Code2000", 0, 14));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Title 1"
            }
        ));
        jTable1.setIntercellSpacing(new java.awt.Dimension(0, 0));
        jTable1.setOpaque(false);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jScrollPane3.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 8.0;
        jPanel2.add(jScrollPane3, gridBagConstraints);

        jLabel2.setText("Input Language");

        jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxInput1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxInput1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 186, Short.MAX_VALUE)
            .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel6Layout.createSequentialGroup()
                    .add(0, 0, Short.MAX_VALUE)
                    .add(jLabel2)
                    .add(3, 3, 3)
                    .add(jComboBoxInput1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(0, 0, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 23, Short.MAX_VALUE)
            .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel6Layout.createSequentialGroup()
                    .add(0, 0, Short.MAX_VALUE)
                    .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel6Layout.createSequentialGroup()
                            .add(5, 5, 5)
                            .add(jLabel2))
                        .add(jComboBoxInput1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(0, 0, Short.MAX_VALUE)))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel6, gridBagConstraints);

        mainPanel.add(jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButtonTranslate.setText("Transliterate");
        jButtonTranslate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTranslateActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonTranslate);

        mainPanel.add(jPanel3);

        jMenu3.setText("File");

        jMenuItem1.setText("jMenuItem1");
        jMenu3.add(jMenuItem1);

        jMenuBar.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar.add(jMenu4);

        statusPanel.setMinimumSize(new java.awt.Dimension(20, 50));
        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.setPreferredSize(new java.awt.Dimension(148, 20));

        progressBar.setMinimumSize(new java.awt.Dimension(20, 20));
        progressBar.setPreferredSize(new java.awt.Dimension(200, 20));

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabelLines, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 271, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 353, Short.MAX_VALUE)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabelLines, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        setComponent(mainPanel);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonTranslateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTranslateActionPerformed
        if(jButtonTranslate.getText() == "Cancel"){
            System.out.println("Cancel") ;
            task.cancel(true);
            task.removePropertyChangeListener(this);
            return ;
        }
        System.out.println("inputLanguage = "+inputLanguage+"  outputLanguage = "+outputLanguage) ;
        DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel() ;
        jLabelLines.setText(""+model2.getRowCount()+" lines");

        try {

            task = new Task();
            task.addPropertyChangeListener(this);
            task.execute();




        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButtonTranslateActionPerformed

    private void jButtonImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportActionPerformed

        final JFileChooser fc = new JFileChooser();
       ExampleFileFilter filter = new ExampleFileFilter("txt");
       filter.setDescription("text files");
       fc.addChoosableFileFilter(filter);
       fc.setAcceptAllFileFilterUsed(true) ;
       fc.setFileFilter(filter) ;

        int returnVal = fc.showOpenDialog(mainPanel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {

                jTable2.removeAll(); 

            File file = fc.getSelectedFile();
            try {
                Charset chars =  getEncoding(file.getAbsolutePath()) ;
                BufferedReader inputFile = new BufferedReader(new InputStreamReader
                              (new FileInputStream(file),chars));
                String line ;
                StringBuilder contents = new StringBuilder();
                long i = 0 ;
                DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel() ;
                model2.setRowCount(0);
                while (( line = inputFile.readLine()) != null){
                   // System.out.println(i+"  "+line) ;
                   // String newString = new String(line.getBytes("UTF16"), "UTF8");

                    model2.addRow(new Object[]{line});
                    contents.append(System.getProperty("line.separator"));
                    i++ ;
                }
                jLabelLines.setText(""+model2.getRowCount()+" lines") ;
                inputFile.close() ;
              //  System.out.println("con: "+contents.toString())    ;
                Document doc = new DefaultStyledDocument();
//                doc.
               // doc.insertString( 0, contents.toString() , null);
               // jTextAreaInput.getDocument().remove(0, jTextAreaInput.getDocument().getLength());
                //jTextAreaInput.setDocument(doc);
           //     jTextAreaInput.
              //  jTextAreaInput.insert(contents.toString(), 0) ;
              //  jTextAreaInput.getDocument().insertString(0, contents.toString(), null);
                contents.delete(0, contents.length()) ;
          //      jTextAreaInput.read(inputFile, doc);

        } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, ex);

        }catch (IOException ex) {
                Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, ex);
        }

        } else {

        }


    }//GEN-LAST:event_jButtonImportActionPerformed
private static Charset getEncoding(String filePath) {
		String encoding = System.getProperty("file.encoding");

		BufferedReader bufferedReader = null;

		try {
			// In order to read files with non-default encoding, specify an encoding in the FileInputStream constructor.
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

			char buffer[] = new char[3];
			int length = bufferedReader.read(buffer);

                        System.out.println(new PrintfFormat("\'%x\'").sprintf(buffer[0])) ;
                        System.out.println(new PrintfFormat("\'%x\'").sprintf(buffer[1])) ;
                        System.out.println(new PrintfFormat("\'%x\'").sprintf(buffer[2])) ;
			if (length >= 2) {
				if ((buffer[0] == (char) 0xff && buffer[1] == (char) 0xfe) /* UTF-16, little endian */ ||
				    (buffer[0] == (char) 0xfe && buffer[1] == (char) 0xff) /* UTF-16, big endian */ ||
                                    (buffer[0] == (char) 0xfffd)){
					encoding = "UTF16";
				}
			}
			if (length >= 3) {
				if (buffer[0] == (char) 0xef && buffer[1] == (char) 0xbb && buffer[2] == (char) 0xbf) /* UTF-8 */  {
					encoding = "UTF8";
				}
			}
		}
		catch (IOException ex) {
		}
		finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				}
				catch (IOException ex) {
				}
			}
		}

                 Charset guessedCharset = null ;
        try {
            guessedCharset = com.glaforge.i18n.io.CharsetToolkit.guessEncoding(new File(filePath), 100);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, ex);
        }
                 System.out.println("guess = "+ guessedCharset) ;
		return guessedCharset;
	}
    private void jButtonExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportActionPerformed
       final JFileChooser fc = new JFileChooser();
         ExampleFileFilter filter = new ExampleFileFilter("txt");
       filter.setDescription("text files");
       fc.addChoosableFileFilter(filter);
       fc.setAcceptAllFileFilterUsed(true) ;
       fc.setFileFilter(filter) ;

//In response to a button click:
        int returnVal = fc.showSaveDialog(mainPanel);


        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if(!file.getName().contains(".txt")){
                file = new File(file.getPath()+".txt") ;
            }
            try {
                BufferedWriter outputFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
           //     String text = jTextAreaOutput.getText() ;
                String text = theText ;
                outputFile.write(text, 0, text.length());
                //outputFile.w
                outputFile.close(); 
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                    Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IOException ex) {
                Logger.getLogger(GoogleTranslateView.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }


 
    }//GEN-LAST:event_jButtonExportActionPerformed

    private void jButtonPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPasteActionPerformed
    //s    jTextAreaInput.paste();
        java.awt.datatransfer.Clipboard schowek=java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
            String result = "";
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    //odd: the Object param of getContents is not currently used
    Transferable contents = clipboard.getContents(null);
    boolean hasTransferableText =
      (contents != null) &&
      contents.isDataFlavorSupported(DataFlavor.stringFlavor)
    ;
    if ( hasTransferableText ) {
      try {
        result = (String)contents.getTransferData(DataFlavor.stringFlavor);
         String[] lines = result.split("\n") ;
         DefaultTableModel model = (DefaultTableModel) jTable2.getModel() ;
         model.setRowCount(0);
         for(int i = 0 ; i < lines.length ; i++){
            model.addRow(new Object[]{lines[i]});
         }
      }
      catch (UnsupportedFlavorException ex){
        //highly unlikely since we are using a standard DataFlavor
        System.out.println(ex);
        ex.printStackTrace();
      }
      catch (IOException ex) {
        System.out.println(ex);
        ex.printStackTrace();
      }
    }
    }//GEN-LAST:event_jButtonPasteActionPerformed

    private void jButtonCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCopyActionPerformed
//       jTextAreaOutput.copy() ;
        //jTable1.c
        java.awt.datatransfer.StringSelection tekst=new java.awt.datatransfer.StringSelection(theText);
        java.awt.datatransfer.Clipboard schowek=java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        schowek.setContents(tekst, tekst);
    }//GEN-LAST:event_jButtonCopyActionPerformed

    private void jButtonSelectInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectInputActionPerformed
        jTable2.selectAll();
    }//GEN-LAST:event_jButtonSelectInputActionPerformed

    private void jButtonSelectOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectOutputActionPerformed
//        jTextAreaOutput.selectAll();
        jTable1.selectAll(); ;
    }//GEN-LAST:event_jButtonSelectOutputActionPerformed

    private void jComboBoxInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxInputActionPerformed
        String selected = (String) jComboBoxInput.getSelectedItem() ;
//        inputLanguage = Language.valueOf(selected.toUpperCase()) ;
        System.out.println("selected = "+selected) ;
        if(selected.equals("Armenian")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Latin"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Arabic")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Latin"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Accents")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Any"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Bengali")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Devanagari","Gujarati","Gurmukhi"
            ,"Kannada","Latin","Malayalam","Oriya","Tamil","Telugu"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Latin")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
            "Arabic","Armenian","Bengali","Cyrillic","Devanagari","Georgian","Greek","Gujarati","Gurmukhi",
            "Han","Hangul","Hebrew","Hiragana","Jamo","Kannada","Katakana","Malayalam","NumericPinyin",
            "Oriya","Syriac","Tamil","Telugu","Thaana","Thai"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Cyrillic")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Latin"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Devanagari")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Bengali","Gujarati",
            "Gurmukhi","Kannada","Latin","Malayalam","Oriya","Tamil","Telugu"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Digit")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Tone"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Fullwidth")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Halfwidth"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Gujarati")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Bengali",
            "Devanagari","Gurmukhi","Kannada","Latin","Malayalam","Oriya","Tamil","Telugu"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Gurmukhi")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Bengali",
            "Devanagari","Gujarati","Kannada","Latin","Malayalam","Oriya","Tamil","Telugu"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Halfwidth")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Fullwidth"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Cyrillic")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Latin"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Hiragana")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Katakana","Latin"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Kannada")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Bengali",
            "Devanagari","Gujarati","Gurmukhi","Kannada","Latin","Malayalam","Oriya","Tamil","Telugu"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Katakana")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Hiragana","Latin"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Malayalam")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Bengali",
            "Devanagari","Gujarati","Gurmukhi","Kannada","Latin","Oriya","Tamil","Telugu"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Name")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Any"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("NumericPinyin")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Latin","Pinyin"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Oriya")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Bengali",
            "Devanagari","Gujarati","Gurmukhi","Kannada","Latin","Malayalam ","Tamil","Telugu"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Pinyin")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"NumericPinyin"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Publishing")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Any"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Simplified")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Traditional"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Tamil")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Bengali",
            "Devanagari","Gujarati","Gurmukhi","Kannada","Latin","Malayalam ","Oriya","Telugu"}));
            jComboBoxInput1.repaint();
        }else if(selected.equals("Telugu")){
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Bengali",
            "Devanagari","Gujarati","Gurmukhi","Kannada","Latin","Malayalam ","Oriya","Tamil"}));
            jComboBoxInput1.repaint();
        }else {
            jComboBoxInput1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {"Latin"}));
            jComboBoxInput1.repaint();
        }
        jComboBoxInput1.setSelectedIndex(0);
    }//GEN-LAST:event_jComboBoxInputActionPerformed

    private void jComboBoxInput1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxInput1ActionPerformed
         String selected1 = (String) jComboBoxInput.getSelectedItem() ;
         String selected2 = (String) jComboBoxInput1.getSelectedItem() ;
         String tra = selected1 + "-"+selected2 ;
         System.out.println("tra = "+tra) ;
         setTransliterator(tra, null);
    }//GEN-LAST:event_jComboBoxInput1ActionPerformed

    private void jButtonImportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonImportMouseClicked
        // TODO add your handling code here:
       // evt.
    }//GEN-LAST:event_jButtonImportMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCopy;
    private javax.swing.JButton jButtonExport;
    private javax.swing.JButton jButtonImport;
    private javax.swing.JButton jButtonPaste;
    private javax.swing.JButton jButtonSelectInput;
    private javax.swing.JButton jButtonSelectOutput;
    private javax.swing.JButton jButtonTranslate;
    private javax.swing.JComboBox jComboBoxInput;
    private javax.swing.JComboBox jComboBoxInput1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelLines;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
}
