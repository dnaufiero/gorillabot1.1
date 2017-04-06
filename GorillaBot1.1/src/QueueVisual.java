import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

/* ListDemo.java requires no other files. */
public class QueueVisual extends JPanel
                      implements ListSelectionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList<String> list;
    private DefaultListModel<String> listModel;
    ArrayList<String> queue;
	ArrayList<String> ingame;

    private static final String ingameString = "Move Ingame";
    private JButton ingameButton;

    public QueueVisual() {
    	
        super(new BorderLayout());

        listModel = new DefaultListModel<String>();
        listModel.addElement("Test 1");
        queue = new ArrayList<String>();
    	ingame = new ArrayList<String>();

        //Create the list and put it in a scroll pane.
        list = new JList<String>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        ingameButton = new JButton(ingameString);
        ingameButton.setActionCommand(ingameString);
        ingameButton.addActionListener(new FireListener());
        ingameButton.setEnabled(false);

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(ingameButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
   
    }
    
    public void updateQueue(){
    	int index = list.getSelectedIndex(); //get selected index
        if (index == -1) { //no selection, so insert at beginning
            index = 0;
        } else {           //add after the selected item
            index++;
        }
        listModel.addElement(queue.get(queue.size()-1).toString());
        //If we just wanted to add to the end, we'd do this:
        //listModel.addElement(employeeName.getText());
        //Select the new item and make it visible.
        list.setSelectedIndex(0);
        list.ensureIndexIsVisible(0);
    	ingameButton.setEnabled(true);
	}
    
    public void updateAdd(String sender){
    	queue.add(sender);
    	updateQueue();
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
            String user = queue.get(index);
            ingame.add(user);
            
            updateQueue();
            

            int size = listModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                ingameButton.setEnabled(false);

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

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
            //No selection, disable fire button.
                ingameButton.setEnabled(false);

            } else {
            //Selection, enable the fire button.
                ingameButton.setEnabled(true);
            }
        }
    }
    
    public void mainWindow(){
    	//javax.swing.SwingUtilities.invokeLater(new Runnable() {
       //     public void run() {
                createAndShowGUI();
        //    }
       // });
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Queue");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new QueueVisual();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

}