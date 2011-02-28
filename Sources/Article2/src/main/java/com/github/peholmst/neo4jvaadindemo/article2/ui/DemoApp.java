package com.github.peholmst.neo4jvaadindemo.article2.ui;

import com.github.peholmst.neo4jvaadindemo.article2.dto.MessageDTO;
import com.github.peholmst.neo4jvaadindemo.article2.dto.TagDTO;
import com.github.peholmst.neo4jvaadindemo.article2.service.Service;
import com.github.peholmst.neo4jvaadindemo.article2.service.ServiceImpl;
import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.Collection;

/**
 * Vaadin application class for this demo. The implementation has been done
 * in the simplest possible way, therefore this may not be a good example
 * of how to properly design Vaadin applications ;-).
 * 
 * @author Petter Holmström
 */
public class DemoApp extends Application {

    @Override
    public void init() {
        Window mainWindow = new Window("Neo4j Vaadin Demo 2");
        initMainWindow(mainWindow);
        setMainWindow(mainWindow);
    }

    public Service getService() {
        return ServiceImpl.getInstance();
    }

    private void initMainWindow(Window mainWindow) {
        mainWindow.setSizeFull();
        VerticalLayout layout = (VerticalLayout) mainWindow.getContent();
        layout.setSpacing(true);
        layout.setMargin(true);
        layout.setSizeFull();

        final Label header = new Label("Neo4j Vaadin Demo 2");
        header.addStyleName(Reindeer.LABEL_H1);
        layout.addComponent(header);

        TabSheet tabs = new TabSheet();
        tabs.setSizeFull();
        layout.addComponent(tabs);
        layout.setExpandRatio(tabs, 1.0f);

        VerticalLayout storeMessageTab = new VerticalLayout();
        {
            storeMessageTab.setSpacing(true);
            storeMessageTab.setMargin(true);
            tabs.addTab(storeMessageTab, "Store Messages", null);
            messageField = new TextField("Message");
            messageField.setWidth("300px");
            messageField.setImmediate(true);
            storeMessageTab.addComponent(messageField);

            storeMessageButton = new Button("Store Message", new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    MessageDTO dto = new MessageDTO();
                    dto.setContent((String) messageField.getValue());
                    getService().storeMessage(dto);
                    messageField.setValue("");
                }
            });
            storeMessageTab.addComponent(storeMessageButton);
        }

        HorizontalSplitPanel readMessagesTab = new HorizontalSplitPanel();
        {
            tabs.addTab(readMessagesTab, "Read Messages", null);
            VerticalLayout tagsLayout = new VerticalLayout();
            tagsLayout.setSizeFull();

            filterTagsField = new TextField();
            filterTagsField.setInputPrompt("Search tags");
            filterTagsField.setImmediate(true);
            filterTagsField.setWidth("100%");
            tagsLayout.addComponent(filterTagsField);
            tagsTable = new Table();
            tagsTable.setSizeFull();
            tagsTable.setImmediate(true);
            tagsTable.setSelectable(true);
            tagsLayout.addComponent(tagsTable);
            tagsLayout.setExpandRatio(tagsTable, 1.0f);

            filterTagsField.addListener(new ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    showTags((String) filterTagsField.getValue());
                }
            });

            VerticalSplitPanel vSplitPanel = new VerticalSplitPanel();
            vSplitPanel.setSizeFull();
            VerticalLayout messagesLayout = new VerticalLayout();
            messagesLayout.setSizeFull();
            
            tagLabel = new Label();
            messagesLayout.addComponent(tagLabel);
            
            messagesTable = new Table();
            messagesTable.setImmediate(true);
            messagesTable.setSizeFull();
            messagesTable.setSelectable(true);
            messagesLayout.addComponent(messagesTable);
            messagesLayout.setExpandRatio(messagesTable, 1.0f);
            
            vSplitPanel.setFirstComponent(messagesLayout);

            VerticalLayout messageLayout = new VerticalLayout();
            messageLayout.setSizeFull();

            tagsPanel = new HorizontalLayout();
            tagsPanel.setSpacing(true);
            messageLayout.addComponent(tagsPanel);
            messageLayout.setSpacing(true);
            messageLayout.setMargin(true);
            
            messageArea = new TextArea();
            messageArea.setWordwrap(true);
            messageArea.setSizeFull();
            messageArea.setReadOnly(true);
            messageLayout.addComponent(messageArea);            
            messageLayout.setExpandRatio(messageArea, 1.0f);
            vSplitPanel.setSecondComponent(messageLayout);
            
            tagsTable.addListener(new ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    showMessagesWithTag((TagDTO) tagsTable.getValue());
                }
            });
            
            messagesTable.addListener(new ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    showMessage((MessageDTO) messagesTable.getValue());
                }
            });

            readMessagesTab.setFirstComponent(tagsLayout);
            readMessagesTab.setSecondComponent(vSplitPanel);
            readMessagesTab.setSplitPosition(25);
        }
    }

    private void showTags(String prefix) {
        Collection<TagDTO> tags = getService().getTagsStartingWith(prefix);
        tagsTable.setContainerDataSource(new BeanItemContainer(TagDTO.class, tags));
    }

    private void showMessagesWithTag(TagDTO tag) {
        if (tag == null) {
            tagLabel.setValue("");
        } else {
            tagLabel.setValue("Showing messages tagged with: " + tag);
        }
                
        Collection<MessageDTO> messages = getService().getTaggedMessages(tag);
        messagesTable.setContainerDataSource(new BeanItemContainer(MessageDTO.class, messages));
    }

    private void showMessage(MessageDTO message) {
        tagsPanel.removeAllComponents();
        messageArea.setReadOnly(false);
        messageArea.setValue(message == null ? "" : message.getContent());        
        messageArea.setReadOnly(true);
        if (message != null) {
            Label lbl = new Label("Tags in message:");
            tagsPanel.addComponent(lbl);
            tagsPanel.setComponentAlignment(lbl, Alignment.MIDDLE_LEFT);
            for (final TagDTO tag : message.getTags()) {
                Button tagButton = new Button(tag.getName(), new Button.ClickListener() {

                    @Override
                    public void buttonClick(ClickEvent event) {
                        showMessagesWithTag(tag);
                    }
                });
                tagButton.setStyleName(Reindeer.BUTTON_LINK);
                tagsPanel.addComponent(tagButton);
                tagsPanel.setComponentAlignment(tagButton, Alignment.MIDDLE_LEFT);
            }
        }
    }
    
    private Table messagesTable;
    private Table tagsTable;
    private TextField filterTagsField;
    private TextField messageField;
    private Button storeMessageButton;
    private Label tagLabel;
    private HorizontalLayout tagsPanel;
    private TextArea messageArea;
}
