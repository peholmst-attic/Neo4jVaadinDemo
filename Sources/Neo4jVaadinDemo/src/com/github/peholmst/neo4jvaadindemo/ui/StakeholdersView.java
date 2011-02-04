package com.github.peholmst.neo4jvaadindemo.ui;

import com.github.peholmst.neo4jvaadindemo.domain.Stakeholder;
import com.github.peholmst.neo4jvaadindemo.domain.StakeholderRepository;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Form;

public class StakeholdersView extends BaseCrudView<Stakeholder> {

	private static final long serialVersionUID = -4363230442944384982L;

	public StakeholdersView(StakeholderRepository repository) {
		super(repository, Stakeholder.class);
	}
	
	private static final String TITLE_NOT_SELECTED = "Please select a Stakeholder or create a new one";
	private static final String TITLE_EDIT = "Edit Stakeholder";
	private static final String TITLE_SHOW = "Show Stakeholder";

	@Override
	protected String getEditorTitleNoSelection() {
		return TITLE_NOT_SELECTED;
	}

	@Override
	protected String getEditorTitleEditing() {
		return TITLE_EDIT;
	}

	@Override
	protected String getEditorTitleShowing() {
		return TITLE_SHOW;
	}	
	
	@Override
	protected String[] getVisibleTableColumns() {
		return new String[] { "name", "description" };
	}

	@Override
	protected void configureFormForShowing(Form form,
			BeanItem<Stakeholder> beanItem, boolean readOnly) {
		form.setVisibleItemProperties(new String[] { "name", "description" });
	}

}
