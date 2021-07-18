package main.java.com.mbj.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.ArrayList;

/**
 * This department contains all the UI components of the swing library,
 * is responsible for presenting the answers to the questions and designing the interface
 */
public class CacheUnitView {

    private PropertyChangeSupport pcs;
    private cacheUnitPanel panel;
    private JFrame frame;
    private JTextArea text;

    public CacheUnitView() {
        this.pcs = new PropertyChangeSupport(this);
        this.panel = new cacheUnitPanel();
        this.frame = new JFrame();
        this.text = new JTextArea();
    }

    /**
     * add event to list
     * @param pcl
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        this.pcs.addPropertyChangeListener(pcl);
    }

    /**
     * remove event from list
     * @param pcl
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        this.pcs.removePropertyChangeListener(pcl);
    }

    public void start() {
        this.panel.run();
    }

    /**
     * get the server response and update the UI with data
     * @param response response from server
     */
    public <T> void updateUIData(T response) {

        //if request was UPDATE or DELETE and it succeeded
        if (response.toString().equals("true")) {
            text.setForeground(new Color(83, 149, 147));
            text.setFont(new Font(null,Font.PLAIN,18));
            text.setText("\n\n           ✔ Succeeded ");

        }
        ////if request was UPDATE or DELETE and it failed
        else if (response.toString().equals("false") || response.toString().equals("Empty")) {
            text.setForeground(Color.red);
            text.setFont(new Font(null,Font.PLAIN,18));
            text.setText("\n\n             ❌ Failed\n"
                    + "Check your Json file and try again ");
        }
        //if Connection refused because server was not on or another reason
        else if (response.toString().equals("Connection refused")) {
            text.setForeground(Color.red);
            text.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
            text.setText("Connection refused\n"
                    + "The error massage is: Connection refused: connect\nThe server is not turned on or server socket is closed ");
        }
        //if request was GET, show the response
        else {
            text.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
            text.setForeground(new Color(83, 149, 147));
            String temp[] = response.toString().split("},");
            String res = "";
            for (String elem : temp) {
                res += elem + '\n';

            }
            text.setText(res);
        }
        text.setBounds(75, 100, 400, 300);
        text.validate();
        panel.revalidate();
        panel.repaint();
    }


    /**
     * inner class to handle the UI and JPanel
     */
    public class cacheUnitPanel extends JPanel implements ActionListener {
        private JButton createButton, loadButton, showButton;
        private JLabel warp;

        /**
         * construct the panel with buttons and designs it
         */
        public void run() {
            //construct panel and main frame
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBounds(600, 600, 575, 500);
            frame.setTitle("MMU Project");
            panel.setBorder(new EmptyBorder(2, 2, 2, 2));
            panel.setBackground(new Color(148, 184, 184));
            frame.setContentPane(panel);
            panel.setLayout(null);
            text = new JTextArea();
            text.setBounds(75, 100, 400, 300);
            text.setSelectedTextColor(Color.BLACK);
            text.setForeground(Color.BLACK);
            panel.add(text);


            //construct "create new request" button
            createButton = new JButton("➕  create new request");
            createButton.setBackground(new Color(224, 235, 235));
            createButton.setForeground(new Color(83, 149, 147));
            createButton.setBounds(20, 20, 170, 50);

            //construct " load request" button
            loadButton = new JButton("📂  load request");
            loadButton.setBackground(new Color(224, 235, 235));
            loadButton.setForeground(new Color(83, 149, 147));
            loadButton.setBounds(200, 20, 160, 50);

            //construct "show statistics" button
            showButton = new JButton("📈  show statistics");
            showButton.setBackground(new Color(224, 235, 235));
            showButton.setForeground(new Color(83, 149, 147));
            showButton.setBounds(375, 20, 150, 50);

            createButton.addActionListener(this);
            loadButton.addActionListener(this);
            showButton.addActionListener(this);

            //add button to panel
            panel.add(createButton);
            panel.add(loadButton);
            panel.add(showButton);
            warp = new JLabel("");
            warp.setBounds(0, 0, screenSize.width, screenSize.height);
            panel.add(warp);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        }

        /**
         * What happens when each button is pressed
         * @param e - event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if ("➕  create new request".equals(e.getActionCommand())) {
                this.createNewRequest();
            } else if ("📂  load request".equals(e.getActionCommand())) {
                this.loadRequest();
            } else {
                this.shoeStatistics();
            }
        }


        /**
         * when pressing load request button,
         * Opens the File Selection pane and loads the selected file and sends the request to the server
         * show the response in UI
         */
        private void loadRequest() {
            //open chooser file window
            JFileChooser fc = new JFileChooser();
            File workingDirectory = new File(System.getProperty("user.dir"));
            fc.setCurrentDirectory(workingDirectory);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILTER", "JSON");
            fc.setFileFilter((filter));
            int i = fc.showOpenDialog(this);
            if (i == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                String filepath = f.getPath();
                //read file
                try {
                    BufferedReader br = new BufferedReader(new FileReader(filepath));
                    String s1 = "";
                    StringBuilder s2 = new StringBuilder();
                    while ((s1 = br.readLine()) != null) {
                        s2.append(s1).append("\n");
                    }
                    br.close();
                    text.append(s2.toString());
                    text.setBounds(80, 120, 700, 600);
                    //add to observer to send to server
                    pcs.firePropertyChange("load", null, s2.toString());
                } catch (IOException ioException) {
                    ioException.printStackTrace();

                }


            }

        }

        /**
         * add to observer the request to see the server statistics
         * show statistics in UI
         * if server is not ON show an error
         */
        private void shoeStatistics() {
            pcs.firePropertyChange("load", null, "{ \"headers\":{\"action\":\"STATS\"},\"body\":[]}");
        }


        /**
         * when pressing the create new request button
         * get the data models id and content in input text,
         * add to data model array,
         * choose the request type, by default - GET
         * and send the new request to observer to sent to server
         * show server response in UI
         */
        private void createNewRequest() {
            //set new request window
            ArrayList<String> dataModelList = new ArrayList<>();
            StringBuilder newRequest = new StringBuilder("{ \"headers\":\n" + "{\"action\":");
            StringBuilder tempHeader = new StringBuilder("");
            Dimension newRequestSize = Toolkit.getDefaultToolkit().getScreenSize();
            JPanel newPanel = new JPanel();
            JFrame newFrame = new JFrame();
            JLabel warp2 = new JLabel("");
            JLabel label1 = new JLabel();
            JLabel label2 = new JLabel();
            JLabel label3 = new JLabel();
            newPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            newPanel.setBackground(new Color(224, 235, 235));
            newFrame.setContentPane(newPanel);
            newPanel.setLayout(null);

            // data model ID and content
            label1.setText("DataModel ID (long int):");
            label1.setBounds(20, 10, 120, 30);
            JTextField idText = new JTextField();
            idText.setBounds(200, 10, 180, 30);
            label2.setText("DataModel content:");
            label2.setBounds(20, 55, 120, 30);
            JTextField contentText = new JTextField();
            contentText.setBounds(200, 45, 180, 30);

            //add to data model array button
            JButton addButton = new JButton("Add To DataModel Array");
            addButton.setBounds(80, 95, 250, 30);
            addButton.setBackground(new Color(115, 165, 165));
            addButton.addActionListener(new ActionListener()
                    /**
                     * add new data model to array
                     */
            {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dataModelList.add("{ \"id\":" + Long.parseLong(idText.getText() ,10)+ ", \"content\" :" + "\"" + contentText.getText() + "\"" + "}");
                    idText.setText("");
                    contentText.setText("");
                }
            });

            //select request type
            label3.setText("Select request type:");
            label3.setBounds(20, 135, 120, 30);
            String[] requestList = {"GET", "UPDATE", "DELETE"};
            JComboBox<String> requestType = new JComboBox<>(requestList);
            requestType.setBounds(150, 135, 180, 30);
            requestType.setBackground(new Color(115, 165, 165));
            requestType.setForeground(new Color(239, 245, 245));
            requestType.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                /**
                 * get the type request
                 */
                {
                    String selectedType = (String) requestType.getSelectedItem();
                    tempHeader.append(selectedType);
                    newRequest.append("\""+selectedType+"\"" + "} , \n");
                }
            });

            //close and ok buttons
            JButton okButton = new JButton("OK");
            okButton.setBounds(100, 200, 90, 30);
            okButton.setBackground(new Color(115, 165, 165));
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                /**
                 * set new request and sent to observer
                 */
                {
                    //if no type is selected
                    if (tempHeader.toString().equals("")) {
                        newRequest.append(" \"GET\" },\n");
                    }
                    newRequest.append("\"body\" : \n [");
                    if (!dataModelList.isEmpty()) {
                        for (String item : dataModelList) {
                            newRequest.append(item + ",\n");
                        }
                    }
                    if(!idText.getText().equals("")) {
                        newRequest.append("{ \"id\":" + Long.parseLong( idText.getText(),10) + ", \"content\" :" + "\"" + contentText.getText() + "\"" + "}");
                    }
                    if(newRequest.charAt(newRequest.length()-2) == ','){
                        newRequest.delete(newRequest.length()-2,newRequest.length()-1);
                    }
                    newRequest.append("]}");
                    pcs.firePropertyChange("load", null, newRequest.toString());
                    newFrame.dispose();
                }
            });

            JButton cancelButton = new JButton("Cancel");
            cancelButton.setBackground(new Color(115, 165, 165));
            cancelButton.setBounds(200, 200, 90, 30);
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //close add new request window
                    newFrame.dispose();

                }
            });

            // add all buttons and textArea to window
            newFrame.setBounds(500, 500, 400, 320);
            newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            newFrame.setTitle("create new request");
            newPanel.add(idText);
            newPanel.add(label1);
            newPanel.add(label2);
            newPanel.add(label3);
            newPanel.add(contentText);
            newPanel.add(addButton);
            newPanel.add(requestType);
            newPanel.add(okButton);
            newPanel.add(cancelButton);
            warp2.setBounds(0, 0, newRequestSize.width, newRequestSize.height);
            newPanel.add(warp2);
            newFrame.setLocationRelativeTo(null);
            newFrame.setVisible(true);
        }
    }
}
