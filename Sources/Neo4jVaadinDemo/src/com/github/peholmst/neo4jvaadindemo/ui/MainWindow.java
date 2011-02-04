package com.github.peholmst.neo4jvaadindemo.ui;

import java.io.IOException;
import java.io.ObjectInputStream;

import com.github.peholmst.neo4jvaadindemo.Backend;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class MainWindow extends Window {

	private static final long serialVersionUID = 4907831749741884314L;

	public MainWindow() {
		super("Neo4j Vaadin Demo Application");
		initComponents();
	}
	
	private ActorsView actorsView;
	
	private StakeholdersView stakeholdersView;
	
	private ScopesView scopesView;
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		if (actorsView != null) {
			actorsView.setRepository(Backend.getInstance().getActorRepository());
		}
		if (stakeholdersView != null) {
			stakeholdersView.setRepository(Backend.getInstance().getStakeholderRepository());
		}
		if (scopesView != null) {
			scopesView.setRepository(Backend.getInstance().getScopeRepository());
		}
	}	
	
	private void initComponents() {		
		setSizeFull();
		VerticalLayout layout = new VerticalLayout();
		setContent(layout);
		layout.setSizeFull();
		
		WindowHeader header = new WindowHeader();
		layout.addComponent(header);
		
		TabSheet tabSheet = new TabSheet();
		tabSheet.addStyleName(Reindeer.TABSHEET_SMALL);
		layout.addComponent(tabSheet);
		tabSheet.setSizeFull();
		layout.setExpandRatio(tabSheet, 1.0f);
		
		tabSheet.addTab(new Label("Requirements"), "Requirements", null);
		tabSheet.addTab(new Label("Use Cases"), "Use Cases", null);
		
		actorsView = new ActorsView(Backend.getInstance().getActorRepository());
		tabSheet.addTab(actorsView, "Actors", null);
		
		stakeholdersView = new StakeholdersView(Backend.getInstance().getStakeholderRepository());
		tabSheet.addTab(stakeholdersView, "Stakeholders", null);
		
		scopesView = new ScopesView(Backend.getInstance().getScopeRepository());
		tabSheet.addTab(scopesView, "Scopes", null);
	}
	
}
