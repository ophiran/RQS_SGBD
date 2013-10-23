/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JOptionPane;

import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;

import dbAccess.CouchDBAccess;
import searchCI.StringSearch;
import searchCI.ThreadSearch;
import setupCI.ApplicationInfo;
import setupCI.DbCache;

/**
 *
 * @author Ophiran
 */
public class InterfaceCreaMovies extends javax.swing.JFrame implements ActionListener{

	private Vector<DbCache> indexes;
    /**
     * Creates new form InterfaceCreaMovies
     */
    public InterfaceCreaMovies(Vector<DbCache> indexes) {
        this.indexes = indexes;
        initComponents();
        
        for(DbCache db : indexes){
        	jComboBoxIndex.addItem(db);
        }
        
        int i = 0;
		while(indexes.elementAt(i) != null && !indexes.elementAt(i).toString().equals("genres_name")){
			i++;
		}

        String tooltipText = "<html>" + "Valid movie genders ( separator ; ) : ";
		for(Map.Entry<Object, Set<Integer>> entry:indexes.elementAt(i).index.entrySet()) {
			tooltipText += "<br>";
			tooltipText += entry.getKey();
		}
		tooltipText += "</html>";
		
        jTextFieldGenderSearch.setToolTipText(tooltipText);
        
        tooltipText = "Actors names ( separator ; )";
        jTextFieldActorsSearch.setToolTipText(tooltipText);
  
        tooltipText = "Directors names ( separator ; )";
        jTextFieldDirectorsSearch.setToolTipText(tooltipText);
        
        jComboBoxIndex.addActionListener(this);
        jButton1.addActionListener(this);
        jListMovies.addMouseListener(new MouseAdapter(){
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		actionPerformedWithMouse(e);
        	}
		});
    }

    //sera pour Alim
    public void actionPerformedWithMouse(MouseEvent e){
    	
    	/*
    	Entry<Object,Set<Integer>> entry=  (Entry<Object, Set<Integer>>) jListMovies.getSelectedValue();
    	
    	int id = (int) entry.getValue().toArray()[0];
    	
    	
		CouchDBAccess connection = new CouchDBAccess();
		connection.connect(ApplicationInfo.getInstance().getIp(), ApplicationInfo.getInstance().getPort(), ApplicationInfo.getInstance().getDbName());
		ViewResult view = connection.sendQueryKey("_design/main",String.valueOf(id));
		connection.close();
    	
		Iterator<Row> iterator = view.iterator();
		
		while(iterator.hasNext()) {
	    	System.out.println(iterator.next());
		}
		*/
    	//jListMovies.setToolTipText(text);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	if(jTabbedPane5.isShowing() && e.getSource().equals(jComboBoxIndex)){
    		if(jComboBoxIndex.getSelectedItem() instanceof DbCache){
    			jListMovies.setListData(((DbCache)jComboBoxIndex.getSelectedItem()).index.entrySet().toArray());
    		}
    	}
    	if(/*jTabbedPane2.isShowing() && */e.getSource().equals(jButton1)){
    		
    		String[] actors = {""};
    		String[] genders = {""};
    		String[] directors = {""};
    		String title = "";
    		String certification = "";
    		double ratingMin = 0d;
    		double ratingMax = Double.MAX_VALUE;
    		int voteMin = 0;
    		int voteMax = Integer.MAX_VALUE;
    		Vector<Set<Integer>> searchResult = new Vector<>();
    		String aDay = "1";
    		String aMon = "1";
    		String aYear = "1800";
    		String bDay = "31";
    		String bMon = "12";
    		String bYear = "5000";
    		Date afterDate;
    		Date beforeDate;
    		
    		GregorianCalendar gcStart = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
    		gcStart.clear();
    		GregorianCalendar gcEnd = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
    		gcEnd.clear();
    		
    		if(!jTextFieldRatingMin.getText().isEmpty()) {
    			ratingMin = Double.valueOf(jTextFieldRatingMin.getText());
    		}
    		if(!jTextFieldRatingMax.getText().isEmpty()) {
    			ratingMax = Double.valueOf(jTextFieldRatingMax.getText());
    		}
    		if(!jTextFieldVoteMin.getText().isEmpty()) {
    			voteMin = Integer.valueOf(jTextFieldVoteMin.getText());
    		}
    		if(!jTextFieldVoteMax.getText().isEmpty()) {
    			voteMax = Integer.valueOf(jTextFieldVoteMax.getText());
    		}
    		if(!jTextFieldTitleSearch.getText().isEmpty()) {
    			title = jTextFieldTitleSearch.getText();
    		}
    		if(!jTextFieldActorsSearch.getText().isEmpty()) {
        		actors = jTextFieldActorsSearch.getText().split(";");
    		}
    		if(!jTextFieldGenderSearch.getText().isEmpty()) {
    			genders = jTextFieldGenderSearch.getText().split(";");
    		}
    		if(!jTextFieldDirectorsSearch.getText().isEmpty()) {
    			directors = jTextFieldDirectorsSearch.getText().split(";");
    		}
    		
    		if(!jTextFieldOutADay.getText().isEmpty()){
    			aDay = jTextFieldOutADay.getText();
    		}
    		if(!jTextFieldOutAMon.getText().isEmpty()){
    			aMon = jTextFieldOutAMon.getText();
    		}
    		if(!jTextFieldOutAYear.getText().isEmpty()){
    			aYear = jTextFieldOutAYear.getText();
    		}
    		if(!jTextFieldOutBDay.getText().isEmpty()){
    			bDay = jTextFieldOutBDay.getText();
    		}
    		if(!jTextFieldOutBMon.getText().isEmpty()){
    			bMon = jTextFieldOutBMon.getText();
    		}
    		if(!jTextFieldOutBYear.getText().isEmpty()){
    			bYear = jTextFieldOutBYear.getText();
    		}

    		if(!jTextFieldCertSearch.getText().isEmpty()){
    			certification = jTextFieldCertSearch.getText();
    		}
    		
    		
    		try {
	    		gcStart.set(Integer.valueOf(aYear), Integer.valueOf(aMon) - 1, Integer.valueOf(aDay));
    		} catch(NumberFormatException nfe){
    			nfe.printStackTrace();
    		}
    		try {
	    		gcEnd.set(Integer.valueOf(bYear), Integer.valueOf(bMon)-1, Integer.valueOf(bDay));
    		} catch(NumberFormatException nfe){
    			nfe.printStackTrace();
    		}
    		
    		
    		afterDate = new Date(gcStart.getTimeInMillis());
    		beforeDate = new Date(gcEnd.getTimeInMillis());
    		
    		//Threads Creation
    		Vector<ThreadSearch> searchVector = new Vector<>();
    		int i = 0;
    		ThreadSearch tempThread;
    		while(i < indexes.size()){
    			
    			if(indexes.elementAt(i).toString().equals("actors_name")){
    				for(String actor: actors){
    	    			if(!actor.isEmpty()) {
    	    				tempThread = new StringSearch(actor, indexes.elementAt(i).index);
    	    				tempThread.start();
    	    				searchVector.add(tempThread);
    	    			}
    	    		}
    			}
    			
    			if(indexes.elementAt(i).toString().equals("genres_name")){
    				for(String gender: genders){
    	    			if(!gender.isEmpty()) {
    	    				tempThread = new StringSearch(gender, indexes.elementAt(i).index);
    	    				tempThread.start();
    	    				searchVector.add(tempThread);
    	    			}
    	    		}
    			}
    			
    			if(indexes.elementAt(i).toString().equals("directors_name")){
    				for(String director: directors){
    	    			if(!director.isEmpty()) {
    	    				tempThread = new StringSearch(director, indexes.elementAt(i).index);
    	    				tempThread.start();
    	    				searchVector.add(tempThread);
    	    			}
    	    		}
    			}
    			
    			if(indexes.elementAt(i).toString().equals("title") && !title.isEmpty()){
    				tempThread = new StringSearch(title, indexes.elementAt(i).index);
    				tempThread.start();
    				searchVector.add(tempThread);
    			}
    			
    			if(indexes.elementAt(i).toString().equals("vote_average") && 
    					(!jTextFieldRatingMin.getText().isEmpty() || !jTextFieldRatingMax.getText().isEmpty())){
    				Set<Integer> setValues = new HashSet<>();
    				for(Set<Integer> value:((TreeMap<Object,Set<Integer>>)indexes.elementAt(i).index).subMap(ratingMin, ratingMax).values()){
    					setValues.addAll(value);
    				}
    				searchResult.add(setValues);
    			}
    			
    			if(indexes.elementAt(i).toString().equals("vote_count") && 
    					(!jTextFieldVoteMin.getText().isEmpty() || !jTextFieldVoteMax.getText().isEmpty())){
    				Set<Integer> setValues = new HashSet<>();
    				for(Set<Integer> value:((TreeMap<Object,Set<Integer>>)indexes.elementAt(i).index).subMap(voteMin, voteMax).values()){
    					setValues.addAll(value);
    				}
    				searchResult.add(setValues);
    			}
    			
    			if(indexes.elementAt(i).toString().equals("release_date")){
    				if(beforeDate.getTime() >= afterDate.getTime()) {
        				Set<Integer> setValues = new HashSet<>();
        				for(Set<Integer> value:((TreeMap<Object,Set<Integer>>)indexes.elementAt(i).index).subMap(afterDate, beforeDate).values()){
        					setValues.addAll(value);
        				}
        				searchResult.add(setValues);
    				}
    				else {
    					JOptionPane.showMessageDialog(this, "The starting date must be lower than the end date (now using default date)");
    				}
    			}
    			
    			if(indexes.elementAt(i).toString().equals("certification") && !certification.isEmpty()){
    				tempThread = new StringSearch(certification, indexes.elementAt(i).index);
    				tempThread.start();
    				searchVector.add(tempThread);
    			}
    			
    			i++;
    		}
    		

    		for(ThreadSearch ts : searchVector) {
    			try {
    			ts.join();
    			searchResult.add(ts.getResultSet());
    			} catch (InterruptedException ie){
    				ie.printStackTrace();
    			}
    		}

    		
    		//Get the list of titles
    		i = 0;
    		while(indexes.elementAt(i) != null && !indexes.elementAt(i).toString().equals("title")){
    			i++;
    		}
    		
    		for(int j = 1; !searchResult.isEmpty() && j < searchResult.size();j++) {
    			searchResult.elementAt(0).retainAll(searchResult.elementAt(j));
    		}
    		
    		
    		//Search each titles for the corresponding id
    		TreeMap<Object,Set<Integer>> copyIndex = new TreeMap();
    		if(!searchResult.isEmpty()){
        		for(Map.Entry<Object, Set<Integer>> entry : indexes.elementAt(i).index.entrySet()){
        			for(Integer id: entry.getValue()){
        				if(searchResult.elementAt(0).contains(id)){
        					Set<Integer> temp = new HashSet<>();
        					temp.add(id);
            				copyIndex.put(entry.getKey(),temp);
        				}
        			}
        		}
    		}
    		else {
    			copyIndex = (TreeMap<Object, Set<Integer>>) indexes.elementAt(i).index;
    		}
    		
    		//put everything in the list
    		if(copyIndex != null){
        		jListMovies.setListData(copyIndex.entrySet().toArray());
    		}
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

        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListMovies = new javax.swing.JList();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jComboBoxIndex = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTextFieldGenderSearch = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldActorsSearch = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldTitleSearch = new javax.swing.JTextField();
        jTextFieldDirectorsSearch = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldRatingMin = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldRatingMax = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldVoteMax = new javax.swing.JTextField();
        jTextFieldVoteMin = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextFieldCertSearch = new javax.swing.JTextField();
        jTextFieldOutBDay = new javax.swing.JTextField();
        jTextFieldOutBMon = new javax.swing.JTextField();
        jTextFieldOutBYear = new javax.swing.JTextField();
        jTextFieldOutADay = new javax.swing.JTextField();
        jTextFieldOutAMon = new javax.swing.JTextField();
        jTextFieldOutAYear = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(jListMovies);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Indexes");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxIndex, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(267, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(4, 4, 4)
                .addComponent(jComboBoxIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(213, Short.MAX_VALUE))
        );

        jTabbedPane5.addTab("Indexes", jPanel1);

        jTextFieldGenderSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldGenderSearchActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Gender");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Out after");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Directors");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Out before");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Actors");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setText("Title");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Certification");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Rating");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("to");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Vote");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("to");

        jButton1.setText("Search");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextFieldCertSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextFieldActorsSearch, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextFieldDirectorsSearch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addComponent(jLabel5)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jTextFieldVoteMin, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addComponent(jLabel3)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTextFieldRatingMin, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel4)
                                                .addComponent(jLabel6))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jTextFieldRatingMax, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGap(20, 20, 20)
                                            .addComponent(jTextFieldVoteMax, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jTextFieldOutADay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldOutAMon, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldOutAYear, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jButton1))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel10)
                                                .addComponent(jLabel12)
                                                .addComponent(jTextFieldTitleSearch)
                                                .addComponent(jLabel7)
                                                .addComponent(jTextFieldGenderSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jTextFieldOutBDay, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextFieldOutBMon, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextFieldOutBYear, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 46, Short.MAX_VALUE)))))
                        .addContainerGap(55, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldTitleSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel10))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldActorsSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldDirectorsSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldGenderSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldOutBDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldOutBMon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldOutBYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldOutADay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldOutAMon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextFieldOutAYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(1, 1, 1)
                .addComponent(jTextFieldCertSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldRatingMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldRatingMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldVoteMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldVoteMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jButton1))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jTabbedPane5.addTab("Search", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldGenderSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldGenderSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldGenderSearchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBoxIndex;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jListMovies;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTextField jTextFieldActorsSearch;
    private javax.swing.JTextField jTextFieldCertSearch;
    private javax.swing.JTextField jTextFieldDirectorsSearch;
    private javax.swing.JTextField jTextFieldGenderSearch;
    private javax.swing.JTextField jTextFieldOutADay;
    private javax.swing.JTextField jTextFieldOutAMon;
    private javax.swing.JTextField jTextFieldOutAYear;
    private javax.swing.JTextField jTextFieldOutBDay;
    private javax.swing.JTextField jTextFieldOutBMon;
    private javax.swing.JTextField jTextFieldOutBYear;
    private javax.swing.JTextField jTextFieldRatingMax;
    private javax.swing.JTextField jTextFieldRatingMin;
    private javax.swing.JTextField jTextFieldTitleSearch;
    private javax.swing.JTextField jTextFieldVoteMax;
    private javax.swing.JTextField jTextFieldVoteMin;
    // End of variables declaration//GEN-END:variables

}
