package com.github.peholmst.neo4jvaadindemo.ui;

import com.github.peholmst.neo4jvaadindemo.domain.AggregateRoot;
import com.github.peholmst.neo4jvaadindemo.domain.OptimisticTransactionLockingException;
import com.github.peholmst.neo4jvaadindemo.domain.Repository;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public abstract class BaseCrudView<T extends AggregateRoot> extends
		HorizontalSplitPanel {

	private static final long serialVersionUID = -3419869972645541533L;

	private transient Repository<T> repository;

	private final Class<T> aggregateRootClass;

	public BaseCrudView(Repository<T> repository, Class<T> aggregateRootClass) {
		this.repository = repository;
		this.aggregateRootClass = aggregateRootClass;
		initComponent();
	}

	public void setRepository(Repository<T> repository) {
		this.repository = repository;
	}

	public Repository<T> getRepository() {
		return repository;
	}

	public Class<T> getAggregateRootClass() {
		return aggregateRootClass;
	}

	private Table table;

	// TODO Develop a container that can use lazy-loading through an
	// iterator (and that supports filtering and sorting)

	private BeanItemContainer<T> container;

	private Button createButton;

	private Button refreshButton;

	private Button toggleEditButton;

	private Button discardButton;

	private Button deleteButton;

	private Form form;

	private Label editorTitle;

	@SuppressWarnings("serial")
	protected void initComponent() {
		setSizeFull();
		VerticalLayout browser = new VerticalLayout();
		{
			browser.setSizeFull();
			table = new Table();
			table.setSizeFull();
			table.addStyleName(Reindeer.TABLE_BORDERLESS);
			table.setImmediate(true);
			table.setSelectable(true);
			table.addListener(new Property.ValueChangeListener() {

				@Override
				public void valueChange(ValueChangeEvent event) {
					show(aggregateRootClass.cast(table.getValue()), true);
				}
			});
			browser.addComponent(table);

			HorizontalLayout buttons = new HorizontalLayout();
			buttons.setSpacing(true);
			buttons.setMargin(false, true, false, true);
			buttons.setWidth("100%");
			browser.addComponent(buttons);
			browser.setExpandRatio(table, 1.0f);

			createButton = new Button("Create", new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					create();
				}
			});
			createButton.setDescription(getCreateButtonDescription());
			createButton.addStyleName(Reindeer.BUTTON_SMALL);
			buttons.addComponent(createButton);
			buttons.setComponentAlignment(createButton, Alignment.MIDDLE_RIGHT);
			buttons.setExpandRatio(createButton, 1.0f);

			refreshButton = new Button("Refresh", new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					refresh();
				}
			});
			refreshButton.setDescription(getRefreshButtonDescription());
			refreshButton.addStyleName(Reindeer.BUTTON_SMALL);
			buttons.addComponent(refreshButton);
			buttons.setComponentAlignment(refreshButton, Alignment.MIDDLE_RIGHT);

			setFirstComponent(browser);
		}

		VerticalLayout editor = new VerticalLayout();
		{
			editor.setSizeFull();
			editor.setMargin(true, true, false, true);

			HorizontalLayout editorHeader = new HorizontalLayout();
			editorHeader.setSpacing(true);
			editor.addComponent(editorHeader);

			editorTitle = new Label(getEditorTitleNoSelection());
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
			toggleEditButton.setDescription(getEditButtonDescription());
			toggleEditButton.addStyleName(Reindeer.BUTTON_SMALL);
			editorHeader.addComponent(toggleEditButton);
			editorHeader.setComponentAlignment(toggleEditButton,
					Alignment.MIDDLE_LEFT);

			deleteButton = new Button("Delete", new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					delete();
				}
			});
			deleteButton.setDescription(getDeleteButtonDescription());
			deleteButton.addStyleName(Reindeer.BUTTON_SMALL);
			editorHeader.addComponent(deleteButton);
			editorHeader.setComponentAlignment(deleteButton,
					Alignment.MIDDLE_LEFT);

			discardButton = new Button("Discard", new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {
					discard();
				}
			});
			discardButton.setDescription(getDiscardButtonDescription());
			discardButton.addStyleName(Reindeer.BUTTON_SMALL);
			editorHeader.addComponent(discardButton);
			editorHeader.setComponentAlignment(discardButton,
					Alignment.MIDDLE_LEFT);

			form = createForm();
			form.setImmediate(true);
			editor.addComponent(form);
			editor.setExpandRatio(form, 1.0f);

			setSecondComponent(editor);
		}
		setSplitPosition(25);
		refresh();
		show((BeanItem<T>) null, true);
	}

	protected Form createForm() {
		return new Form();
	}

	protected String getCreateButtonDescription() {
		return "Create a new item";
	}

	protected String getRefreshButtonDescription() {
		return "Refresh the list of items";
	}

	protected String getEditorTitleNoSelection() {
		return "Please select an item or create a new one";
	}

	protected String getEditorTitleShowing() {
		return "View Item";
	}

	protected String getEditorTitleEditing() {
		return "Edit Item";
	}

	protected String getEditButtonDescription() {
		return "Edit the current item";
	}

	protected String getDeleteButtonDescription() {
		return "Delete the current item";
	}

	protected String getDiscardButtonDescription() {
		return "Discard any changes and exit edit mode without saving";
	}

	protected String getSavedChangesMessage() {
		return "Your changes have been saved";
	}

	protected String getOptimisticTransactionLockMessage() {
		return "Another user has edited the current item. Please refresh and try again.";
	}

	protected abstract String[] getVisibleTableColumns();

	protected abstract void configureFormForShowing(Form form,
			BeanItem<T> beanItem, boolean readOnly);

	protected void show(T itemId, boolean readOnly) {
		if (itemId == null) {
			show((BeanItem<T>) null, readOnly);
		} else {
			show(container.getItem(itemId), readOnly);
		}
	}

	protected void show(BeanItem<T> beanItem, boolean readOnly) {
		form.setItemDataSource(beanItem);
		if (beanItem != null) {
			configureFormForShowing(form, beanItem, readOnly);
			form.setVisible(true);
			discardButton.setVisible(!readOnly);
			toggleEditButton.setVisible(true);
			deleteButton.setVisible(true);
			form.setReadOnly(readOnly);
			if (readOnly) {
				editorTitle.setValue(getEditorTitleShowing());
				toggleEditButton.removeStyleName("activatedButton");
			} else {
				editorTitle.setValue(getEditorTitleEditing());
				toggleEditButton.addStyleName("activatedButton");
			}
		} else {
			form.setVisible(false);
			discardButton.setVisible(false);
			toggleEditButton.setVisible(false);
			deleteButton.setVisible(false);
			editorTitle.setValue(getEditorTitleNoSelection());
		}
	}

	protected T getSelectedItemId() {
		BeanItem<T> selectedItem = getSelectedItem();
		return selectedItem != null ? selectedItem.getBean() : null;
	}

	@SuppressWarnings("unchecked")
	protected BeanItem<T> getSelectedItem() {
		return (BeanItem<T>) form.getItemDataSource();
	}

	protected void create() {
		T newObject = getRepository().create();
		BeanItem<T> item = container.addBean(newObject);
		table.select(newObject);
		show(item, false);
	}

	protected void refresh() {
		container = new BeanItemContainer<T>(getAggregateRootClass(),
				getRepository().getAll());
		table.setContainerDataSource(container);
		table.setVisibleColumns(getVisibleTableColumns());
	}

	protected void toggleEditing() {
		T selected = getSelectedItemId();
		if (selected != null) {
			if (form.isReadOnly()) {
				/*
				 * Discard the changes to make sure that we get the most recent
				 * version when we start editing.
				 */
				selected.discardChanges();
				show(selected, false);
			} else {
				try {
					if (selected.hasUncommittedChanges()) {
						selected.commitChanges();
						getWindow().showNotification(getSavedChangesMessage(),
								Notification.TYPE_TRAY_NOTIFICATION);
					}
					show(selected, true);
				} catch (OptimisticTransactionLockingException e) {
					getWindow().showNotification(
							getOptimisticTransactionLockMessage(),
							Notification.TYPE_WARNING_MESSAGE);
				}
			}
		}
	}

	protected void delete() {
		getWindow().showNotification("Deletion is not implemented yet");
	}

	@SuppressWarnings("serial")
	protected void discard() {
		T selected = getSelectedItemId();
		if (selected != null) {
			selected.discardChanges();
			show(selected, true);
			table.containerItemSetChange(new Container.ItemSetChangeEvent() {

				@Override
				public Container getContainer() {
					return container;
				}
			});
		}
	}
}
