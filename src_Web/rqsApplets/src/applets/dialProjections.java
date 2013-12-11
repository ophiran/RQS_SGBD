/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package applets;

import dbAccess.beanAccess;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import rqsData.Projection;

/**
 *
 * @author mike
 */
public class dialProjections extends javax.swing.JDialog implements ActionListener{
    Vector<Projection> projections;
    Vector<String> columns;
    String movieTitle;
    int currentIndex = 0;
    /**
     * Creates new form dialProjections
     */
    public dialProjections(java.awt.Frame parent, boolean modal, int movieId, String movieTitle) {
        super(parent, modal);
        initComponents();
        this.movieTitle = movieTitle;
        this.projections = beanAccess.getProjections(movieId);
        reloadTable();
        this.previousButton.addActionListener(this);
        this.nextButton.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(previousButton)) {
            if (currentIndex >= 7) {
                currentIndex -= 7;
                reloadTable();
            }
        }
        if (e.getSource().equals(nextButton)) {
            if (currentIndex <= (projections.size() - 8)) {
                currentIndex += 7;
                reloadTable();
            }
        }
    }
    
    public void reloadTable() {
        columns = new Vector<String>();
        columns.add("Day");
        columns.add("TimeSlot");
        columns.add("Title");
        columns.add("RemainingSeats");
        DefaultTableModel tModel = new DefaultTableModel( new Object[][] {},
                            new String [] { "Day", "TimeSlot", "Title", "RemainingSeats" });
        for(int i = currentIndex; (i < projections.size() - 1) && (i < currentIndex+7); i++) {
            Vector<String> vect = new Vector<String>();
            vect.add(projections.elementAt(i).day.toString());
            vect.add(String.valueOf(projections.elementAt(i).timeSlot));
            vect.add(movieTitle);
            vect.add(String.valueOf(projections.elementAt(i).remainingSeats));
            tModel.addRow(vect);
        }
        this.projectionList.setModel(tModel);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        projectionList = new javax.swing.JTable();
        nextButton = new javax.swing.JButton();
        previousButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        projectionList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(projectionList);

        nextButton.setText("next page");

        previousButton.setText("previous page");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(previousButton)
                        .addGap(29, 29, 29)
                        .addComponent(nextButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nextButton)
                    .addComponent(previousButton))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(89, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton previousButton;
    private javax.swing.JTable projectionList;
    // End of variables declaration//GEN-END:variables

    
}
