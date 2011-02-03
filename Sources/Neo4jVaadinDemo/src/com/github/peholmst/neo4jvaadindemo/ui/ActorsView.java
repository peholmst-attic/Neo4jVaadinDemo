package com.github.peholmst.neo4jvaadindemo.ui;

import com.github.peholmst.neo4jvaadindemo.domain.Actor;
import com.github.peholmst.neo4jvaadindemo.domain.ActorRepository;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Form;

public class ActorsView extends BaseCrudView<Actor> {

	private static final long serialVersionUID = 5249767276352034792L;

	public ActorsView(ActorRepository repository) {
		super(repository, Actor.class);
	}

	private static final String TITLE_NOT_SELECTED = "Please select an Actor or create a new one";
	private static final String TITLE_EDIT = "Edit Actor";
	private static final String TITLE_SHOW = "Show Actor";

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
	protected void configureFormForShowing(Form form, BeanItem<Actor> beanItem,
			boolean readOnly) {
		form.setVisibleItemProperties(new String[] { "name", "description" });
	}
}
