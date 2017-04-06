/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

/* ListDemo.java requires no other files. */
public class WindowManager{
    private JList list;
    private DefaultListModel listModel;
    
    private JList list2;
    private DefaultListModel listModel2;

    private static final String refreshString = "Refresh";
    private static final String ingameString = "Move Ingame";
    private static final String removeString = "Remove Queue";
    private static final String endString = "End Game";
    private JButton ingameButton;
    private JButton endButton;
    private JTextField employeeName;
    public static ArrayList<String> queue;
	public static ArrayList<String> ingame;
	public JFrame mainframe;
	public JPanel mainpanel;
	public ListSelectionListener listener;

    public WindowManager() {
    	mainpanel = new JPanel(new BorderLayout());

        listModel = new DefaultListModel();
        listModel2 = new DefaultListModel();
        //listModel.addElement("Jane Doe");
        //listModel.addElement("John Smith");
        //listModel.addElement("Kathy Green");
        
        queue = new ArrayList<String>();
    	ingame = new ArrayList<String>();

        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(listener);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        JButton refreshButton = new JButton(refreshString);
        HireListener hireListener = new HireListener(refreshButton);
        refreshButton.setActionCommand(refreshString);
        refreshButton.addActionListener(hireListener);

        ingameButton = new JButton(ingameString);
        ingameButton.setActionCommand(ingameString);
        ingameButton.addActionListener(new FireListener());


        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(ingameButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(refreshButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        list2 = new JList(listModel2);
        list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list2.setSelectedIndex(0);
        list2.addListSelectionListener(listener);
        list2.setVisibleRowCount(5);
        JScrollPane listScrollPane2 = new JScrollPane(list2);
        
        JButton removeButton = new JButton(removeString);
        HireListener2 hireListener2 = new HireListener2(removeButton);
        removeButton.setActionCommand(removeString);
        removeButton.addActionListener(hireListener2);
        //hireButton2.setEnabled(false);
        
        
        endButton = new JButton(endString);
        endButton.setActionCommand(endString);
        endButton.addActionListener(new FireListener2());
        //fireButton2.setEnabled(false);
        
        JPanel buttonPane2 = new JPanel();
        buttonPane2.setLayout(new BoxLayout(buttonPane2,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(endButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(removeButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        mainpanel.add(listScrollPane, BorderLayout.NORTH);
        mainpanel.add(buttonPane, BorderLayout.SOUTH);
        mainpanel.add(listScrollPane2, BorderLayout.CENTER);
        
        
        
    }
    
    public void updateAdd(String sender){
    	queue.add(sender);
    }
    
    public void updateRemove(String sender){
    	queue.remove(queue.indexOf(sender));
    }

    class FireListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            listModel.remove(index);
            String name = WindowManager.queue.get(index);
            WindowManager.queue.remove(index);
            WindowManager.ingame.add(name);
            
            index = WindowManager.ingame.size();
            
            if(index != 0){
            	index = index-1;
            }
            
        	listModel2.addElement(name);
        	list2.setSelectedIndex(index);
            list2.ensureIndexIsVisible(index);

            int size = listModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
            	//ingameButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    //This listener is shared by the text field and the hire button.
    class HireListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public HireListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            //String name = employeeName.getText();
        	
        	listModel.clear();
        	
        	for(int i = 0; i<WindowManager.queue.size(); i++){
        		if(!alreadyInList(WindowManager.queue.get(i))){
	        		listModel.addElement(WindowManager.queue.get(i));
	        		list.setSelectedIndex(i);
	                list.ensureIndexIsVisible(i);
        		}
        		//listModel.insertElementAt('q', index);
        		//list.setSelectedIndex(index);
        		//index++;
        	}
        	
        	
        	//listModel.insertElementAt('b', index);
    		//list.setSelectedIndex(index);

            //listModel.insertElementAt(employeeName.getText(), index);
            //If we just wanted to add to the end, we'd do this:
            //listModel.addElement(employeeName.getText());

            //Reset the text field.
            //employeeName.requestFocusInWindow();
            //employeeName.setText("");

            //Select the new item and make it visible.
            //list.setSelectedIndex(index);
            //list.ensureIndexIsVisible(index);
        }

        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                //button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                //button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }
    
    class FireListener2 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
        	WindowManager.ingame.clear();
           listModel2.clear();
        }
    }

    //This listener is shared by the text field and the hire button.
    class HireListener2 implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public HireListener2(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            
        	 int index = list.getSelectedIndex();
             listModel.remove(index);
             String name = WindowManager.queue.get(index);
             WindowManager.queue.remove(index);
             
             int size = listModel.getSize();

             if (size == 0) { //Nobody's left, disable firing.
            	 //ingameButton.setEnabled(false);

             } else { //Select an index.
                 if (index == listModel.getSize()) {
                     //removed item in last position
                     index--;
                 }

                 list.setSelectedIndex(index);
                 list.ensureIndexIsVisible(index);
             }
        	
        	
        }

        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                //button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                //button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
            //No selection, disable fire button.
            	//ingameButton.setEnabled(false);

            } else {
            //Selection, enable the fire button.
            	//ingameButton.setEnabled(true);
            }
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void mainWindow() {
        //Create and set up the window.
        JFrame frame = new JFrame("ListDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = mainpanel;
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }


}
