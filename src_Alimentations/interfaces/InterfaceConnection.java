/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Ophiran
 */
public class InterfaceConnection extends javax.swing.JDialog implements ActionListener{

	final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
	
    /**
     * Creates new form InterfaceConnection
     */
    public InterfaceConnection(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        jButtonConnect.addActionListener(this);
        jButtonCancel.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(jButtonConnect)){
        	Matcher matcher = pattern.matcher(jTextFieldIp.getText());
        	if(matcher.find()) {
        		((InterfaceCreaMovies)this.getParent()).dbAccess.connect(jTextFieldIp.getText(), jTextFieldPort.getText());;
        	}
        	else {
        		System.out.println("Nest pas une ip");
        	}
        }
        else if(e.getSource().equals(jButtonCancel)){
            this.dispose();
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextFieldIp = new javax.swing.JTextField();
        jTextFieldPort = new javax.swing.JTextField();
        jLabelIp = new javax.swing.JLabel();
        jLabelPort = new javax.swing.JLabel();
        jButtonConnect = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabelIp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelIp.setText("Ip address");

        jLabelPort.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelPort.setText("Port number");

        jButtonConnect.setText("Connect");

        jButtonCancel.setText("Cancel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonConnect)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabelPort, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelIp, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextFieldIp)
                        .addComponent(jTextFieldPort, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldIp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelIp))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPort))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonConnect)
                    .addComponent(jButtonCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonConnect;
    private javax.swing.JLabel jLabelIp;
    private javax.swing.JLabel jLabelPort;
    private javax.swing.JTextField jTextFieldIp;
    private javax.swing.JTextField jTextFieldPort;
    // End of variables declaration//GEN-END:variables

}
