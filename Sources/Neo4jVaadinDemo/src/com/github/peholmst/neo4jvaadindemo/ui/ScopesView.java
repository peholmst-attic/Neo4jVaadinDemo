package com.github.peholmst.neo4jvaadindemo.ui;

import com.github.peholmst.neo4jvaadindemo.domain.Scope;
import com.github.peholmst.neo4jvaadindemo.domain.ScopeRepository;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Form;

public class ScopesView extends BaseCrudView<Scope> {

	private static final long serialVersionUID = -5386114051746890686L;

	public ScopesView(ScopeRepository repository) {
		super(repository, Scope.class);
	}
	
	private static final String TITLE_NOT_SELECTED = "Please select a Scope or create a new one";
	private static final String TITLE_EDIT = "Edit Scope";
	private static final String TITLE_SHOW = "Show Scope";

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
	protected void configureFormForShowing(Form form, BeanItem<Scope> beanItem,
			boolean readOnly) {
		form.setVisibleItemProperties(new String[] { "name", "description" });
	}

}
