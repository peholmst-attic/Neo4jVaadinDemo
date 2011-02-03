package com.github.peholmst.neo4jvaadindemo.ui;

import com.github.peholmst.neo4jvaadindemo.domain.Actor;
import com.github.peholmst.neo4jvaadindemo.domain.ActorRepository;
import com.github.peholmst.neo4jvaadindemo.domain.OptimisticTransactionLockingException;
import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;

public class ActorsView extends HorizontalSplitPanel {

	private static final long serialVersionUID = 5249767276352034792L;

	private transient ActorRepository repository;

	public ActorsView(ActorRepository repository) {
		this.repository = repository;
		initComponent();
	}

	public void setFactory(ActorRepository repository) {
		this.repository = repository;
	}

	private Table actorsTable;

	private BeanItemContainer<Actor> actorsContainer;

	private Button addActorButton;

	private Button refreshActorsButton;

	private Button toggleEditButton;

	private Button discardChangesButton;

	private Button deleteActorButton;

	private Form actorForm;

	private Label editorTitle;

	private static final String TITLE_NOT_SELECTED = "Please select an Actor or create a new one";
	private static final String TITLE_EDIT = "Edit Actor";
	private static final String TITLE_SHOW = "Show Actor";

	@SuppressWarnings("serial")
	private void initComponent() {
		setSizeFull();

		VerticalLayout browser = new VerticalLayout();
		{
			browser.setSizeFull();
			actorsTable = new Table();
			actorsTable.setSizeFull();
			actorsTable.addStyleName(Reindeer.TABLE_BORDERLESS);
			actorsTable.setImmediate(true);
			actorsTable.setSelectable(true);
			actorsTable.addListener(new Property.ValueChangeListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void valueChange(ValueChangeEvent event) {
					Actor selectedActor = (Actor) actorsTable.getValue();
					if (selectedActor == null) {
						selectActor(null, true);
					} else {
						selectActor((BeanItem<Actor>) actorsTable
								.getItem(selectedActor), true);
					}
				}
			});
			browser.addComponent(actorsTable);

			HorizontalLayout buttons = new HorizontalLayout();
			buttons.setSpacing(true);
			buttons.setMargin(false, true, false, true);
			buttons.setWidth("100%");
			browser.addComponent(buttons);
			browser.setExpandRatio(actorsTable, 1.0f);

			addActorButton = new Button("Create", new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					createActor();
				}
			});
			addActorButton.setDescription("Create a new Actor");
			addActorButton.addStyleName(Reindeer.BUTTON_SMALL);
			buttons.addComponent(addActorButton);
			buttons.setComponentAlignment(addActorButton,
					Alignment.MIDDLE_RIGHT);
			buttons.setExpandRatio(addActorButton, 1.0f);

			refreshActorsButton = new Button("Refresh",
					new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							refreshActors();
						}
					});
			refreshActorsButton.setDescription("Refresh the list of Actors");
			refreshActorsButton.addStyleName(Reindeer.BUTTON_SMALL);
			buttons.addComponent(refreshActorsButton);
			buttons.setComponentAlignment(refreshActorsButton,
					Alignment.MIDDLE_RIGHT);

			setFirstComponent(browser);
		}

		VerticalLayout editor = new VerticalLayout();
		{
			editor.setSizeFull();
			editor.setMargin(true, true, false, true);

			HorizontalLayout editorHeader = new HorizontalLayout();
			editorHeader.setSpacing(true);
			// editorHeader.setWidth("100%");
			editor.addComponent(editorHeader);

			editorTitle = new Label(
					"Please select an Actor or create a new one");
			editorTitle.addStyleName(Reindeer.LABEL_H2);
			editorHeader.addComponent(editorTitle);
			editorHeader.setComponentAlignment(editorTitle,
					Alignment.MIDDLE_LEFT);

			toggleEditButton = new Button("Edit", new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					toggleEditing();
				}
			});
			toggleEditButton.setDescription("Edit the current Actor");
			toggleEditButton.addStyleName(Reindeer.BUTTON_SMALL);
			editorHeader.addComponent(toggleEditButton);
			editorHeader.setComponentAlignment(toggleEditButton,
					Alignment.MIDDLE_LEFT);

			deleteActorButton = new Button("Delete",
					new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							deleteSelectedActor();
						}
					});
			deleteActorButton.setDescription("Delete the current Actor");
			deleteActorButton.addStyleName(Reindeer.BUTTON_SMALL);
			editorHeader.addComponent(deleteActorButton);
			editorHeader.setComponentAlignment(deleteActorButton,
					Alignment.MIDDLE_LEFT);

			discardChangesButton = new Button("Discard Changes",
					new Button.ClickListener() {

						@Override
						public void buttonClick(ClickEvent event) {
							discardChanges();
						}
					});
			discardChangesButton.setDescription("Discard any changes and exit Edit mode without saving");
			discardChangesButton.addStyleName(Reindeer.BUTTON_SMALL);
			editorHeader.addComponent(discardChangesButton);
			editorHeader.setComponentAlignment(discardChangesButton, Alignment.MIDDLE_LEFT);

			actorForm = new Form();
			actorForm.setImmediate(true);
			editor.addComponent(actorForm);
			editor.setExpandRatio(actorForm, 1.0f);

			setSecondComponent(editor);
		}
		setSplitPosition(25);
		refreshActors();
		selectActor(null, true);
	}

	public void selectActor(BeanItem<Actor> actor, boolean readOnly) {
		actorForm.setItemDataSource(actor);
		if (actor != null) {
			actorForm.setVisibleItemProperties(new String[] { "name",
					"description" });
			actorForm.setVisible(true);
			discardChangesButton.setVisible(!readOnly);
			toggleEditButton.setVisible(true);
			deleteActorButton.setVisible(true);
			actorForm.setReadOnly(readOnly);
			if (readOnly) {
				editorTitle.setValue(TITLE_SHOW);
				toggleEditButton.removeStyleName("activatedButton");
			} else {
				editorTitle.setValue(TITLE_EDIT);
				toggleEditButton.addStyleName("activatedButton");
			}
		} else {
			actorForm.setVisible(false);
			discardChangesButton.setVisible(false);
			toggleEditButton.setVisible(false);
			deleteActorButton.setVisible(false);
			editorTitle.setValue(TITLE_NOT_SELECTED);
		}
	}

	@SuppressWarnings("unchecked")
	public BeanItem<Actor> getSelectedItem() {
		return (BeanItem<Actor>) actorForm.getItemDataSource();
	}

	public void deleteSelectedActor() {
		BeanItem<Actor> selected = getSelectedItem();
		if (selected != null) {
			/*
			 * selected.getBean().delete(); actorsTable.removeItem(selected);
			 * selectActor(null, true);
			 */
		}
	}

	public void createActor() {
		Actor newActor = repository.createActor();
		BeanItem<Actor> item = actorsContainer.addBean(newActor);
		actorsTable.select(newActor);
		selectActor(item, false);
	}

	public void refreshActors() {
		// TODO Develop a container that can use lazy-loading through an
		// iterator (and that supports filtering and sorting)
		actorsContainer = new BeanItemContainer<Actor>(Actor.class,
				repository.getActors());
		actorsTable.setContainerDataSource(actorsContainer);
		actorsTable.setVisibleColumns(new String[] { "name", "description" });
	}

	public void toggleEditing() {
		BeanItem<Actor> selected = getSelectedItem();
		if (selected != null) {
			if (actorForm.isReadOnly()) {
				selected.getBean().discardChanges();
				selectActor(selected, false);
			} else {
				try {
					if (selected.getBean().hasUncommittedChanges()) {
						selected.getBean().commitChanges();
						getWindow().showNotification(
								"Your changes have been saved", Notification.TYPE_TRAY_NOTIFICATION);
					}
					selectActor(selected, true);
				} catch (OptimisticTransactionLockingException e) {
					getWindow()
							.showNotification(
									"Another user has edited the Actor. Please refresh and try again.",
									Notification.TYPE_WARNING_MESSAGE);
				}
			}
		}
	}

	public void discardChanges() {
		BeanItem<Actor> selected = getSelectedItem();
		if (selected != null) {
			selected.getBean().discardChanges();
			selectActor(selected, true);
			actorsTable.containerItemSetChange(new ItemSetChangeEvent() {

				private static final long serialVersionUID = -469941572197739778L;

				@Override
				public Container getContainer() {
					return actorsContainer;
				}
			});
		}
	}
}
