String.prototype.endsWith = function(str) {
    return (this.match(str + "$") == str);
};
String.prototype.format = function() {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function(match, number) { 
        return typeof args[number] != 'undefined'
            ? args[number]
            : match;
    });
};

Ext.Loader.setConfig({enabled: true});
Ext.require(['*']);

window.alert = function(key) {
	var messageArguments = [];
	for(var i in arguments) {
		if(i > 0) {
			messageArguments.push(arguments[i]);
		}
	}
	Ext.MessageBox.alert({
        title: translate('dialog.error.title'),
        msg: translate(key, messageArguments),
        buttons: Ext.MessageBox.OK,
        icon: 'dialog-error'
    });
};

var languages = ['en'];
var languagesLoaded = false;
var translations = [];


function translate(key) {
	var messageArguments = [];
	for(var i in arguments) {
		if(i > 0) {
			messageArguments.push(arguments[i]);
		}
	}
	for(var i in languages) {
		var language = languages[i];
		var localTranslations = translations[language];
		if(localTranslations != null) {
			var translation = localTranslations[key];
			if(translation != null) {
				return translation.format(messageArguments);
			}
		}
	}
	return '???' + key + '???';
}

Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));
Ext.tip.QuickTipManager.init();

Ext.define('Profile', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'name', type: 'string'},
        {name: 'pictureThumb', type: 'string'},
        {name: 'email', type: 'string'}
    ]
});

Ext.define('Namespace', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'name', type: 'string'},
        {name: 'description', type: 'string'}
    ],
    hasMany: {model: 'Type', name: 'types'}
});

Ext.define('Language', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'code', type: 'string'}
    ]
});

Ext.define('Type', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'name', type: 'string'},
        {name: 'description', type: 'string'},
        {name: 'plural', type: 'string'}
    ],
    hasMany: [
        {model: 'Attribute', name: 'attributes'},
        {model: 'Operation', name: 'operations'}
    ],
    belongsTo: 'Namespace'
});

Ext.define('Attribute', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'name', type: 'string'},
        {name: 'description', type: 'string'},
        {name: 'type', type: 'string'},
        {name: 'describing', type: 'boolean'},
        {name: 'listUri', type: 'string'},
        {name: 'referencedType', type: 'string'}
    ],
    hasMany: [
        {model: 'Attribute', name: 'components'},
    ],
    belongsTo: 'Type'
});

Ext.define('Parameter', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'name', type: 'string'},
        {name: 'description', type: 'string'},
        {name: 'type', type: 'string'},
        {name: 'required', type: 'boolean'},
        {name: 'listUri', type: 'string'},
        {name: 'referencedType', type: 'string'}
    ],
    belongsTo: 'Operation'
});

Ext.define('Operation', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'name', type: 'string'},
        {name: 'uri', type: 'string'},
        {name: 'description', type: 'string'},
        {name: 'method', type: 'string'},
        {name: 'defaultOperation', type: 'boolean'}
    ],
    hasMany: [
        {model: 'Parameter', name: 'parameters'}
    ],
    belongsTo: 'Type'
});

var types = [];
registerType({
    name: 'enumerationConstant',
    attributes: [
        {name: 'id', type: 'text'},
        {name: 'description', type: 'text', describing: 'true'}
    ]
}, {
    name: 'basic'
});

var stores = [];

var languageStore = Ext.create('Ext.data.Store', {
    model: 'Language',
    proxy: {
        type: 'ajax',
        url: 'languages',
        reader: {
            type: 'json',
            root: 'languages'
        }
    },
    listeners: {
        load: function() {
        	languages = [];
        	languageStore.each(function(record) {
        		languages.push(record.raw.code);
        	});
        	languages.push('en');
        	languagesLoaded = true;
        	initializeUI();
        }
    },
    autoLoad: true
});

var namespaces = Ext.create('Ext.data.Store', {
    model: 'Namespace',
    proxy: {
        type: 'ajax',
        url: 'types',
        reader: {
            type: 'json',
            root: 'namespaces'
        }
    },
    listeners: {
        load: function() {
            namespaces.each(function(record) {
                var namespaceNode = dataTreeStore.getRootNode().appendChild({
                    text: record.get('description'),
                    iconCls: record.get('name'),
                    expanded: true
                });
                var types = record.getAssociatedData().types;
                for(var i in types) {
                    registerType(types[i], record.data, namespaceNode);
                }
            });
            dataTreePanel.setLoading(false);
        }
    }
});

var fieldFactories = {
    image: function(type, parameter, attributes) {
        return Ext.create('Ext.form.field.File', attributes);
    },
    longText: function(type, parameter, attributes) {
        attributes.grow = true;
        return Ext.create('Ext.form.field.TextArea', attributes);
    },
    richText: function(type, parameter, attributes) {
    	attributes.enableFont = false;
    	attributes.enableColors = false;
    	attributes.enableSourceEdit = false;
    	attributes.height = 250;
        return Ext.create('Ext.form.field.HtmlEditor', attributes);
    },
    enumeration: function(type, parameter, attributes) {
    	var store = getStore(parameter.referencedType, parameter.listUri);
        attributes.store = store;
        attributes.queryMode = 'local';
        attributes.displayField = 'description';
        attributes.valueField = 'id';
        attributes.multiSelect = false;
        attributes.forceSelection = true;
        attributes.typeAhead = true;
        attributes.autoSelect = false;
        var combo = Ext.create('Ext.form.field.ComboBox', attributes);
        store.addListener('load', function() {
        	storeLoaded(store, combo, attributes.value);
        });
        combo.addListener('afterrender', function() {
        	comboRendered(store, combo, attributes.value);
        });
    	return combo;
    },
    list: function(type, parameter, attributes) {
    	var store = getStore(parameter.referencedType, parameter.listUri);
    	attributes.store = store;
    	attributes.queryMode = 'local';
    	attributes.displayField = getDisplayField(types[parameter.referencedType]);
    	attributes.valueField = 'id';
    	attributes.multiSelect = true;
    	attributes.forceSelection = true;
    	attributes.typeAhead = true;
    	attributes.autoSelect = false;
    	attributes.listConfig = {
            getInnerTpl: function() {
                return createTemplate(parameter);
            }
        };
    	var combo = Ext.create('Ext.form.field.ComboBox', attributes);
        store.addListener('load', function() {
        	storeLoaded(store, combo, attributes.value);
        });
        combo.addListener('afterrender', function() {
        	comboRendered(store, combo, attributes.value);
        });
        var component = combo;
        var createAction = types[parameter.referencedType].actions['create'];
        if(createAction != null) {
        	var button = Ext.create('Ext.button.Button', createAction);
        	component = Ext.create('Ext.form.FieldContainer', {
        		layout: {
                    type: 'hbox',
                    defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
        		},
        		title: attributes.fieldLabel,
        		defaults: {
        			hideLabel: true
        		},
        		items: [ combo, button ]
        	});
        }
    	return component;
    }
};

function comboRendered(store, combo, value) {
	if(!store.isLoading()) {
		combo.setValue(value);
	}
}

function storeLoaded(store, combo, value) {
	if(combo.rendered) {
		combo.setValue(value);
	}
}

function getDisplayField(type) {
    var displayField = null;
    for(var i in type.attributes) {
        var attribute = type.attributes[i];
        if(attribute.describing) {
            if(displayField == null) {
                displayField = attribute.name;
                break;
            }
        }
    }
    if(displayField == null) {
        displayField = type.attributes[0];
    }
    return displayField;
}

function createTemplate(parameter) {
    var referencedType = types[parameter.referencedType];
    var templateExpression = '';
    var renderers = [];
    for(var i in referencedType.attributes) {
        var attribute = referencedType.attributes[i];
        if(attribute.describing) {
            var renderer = getRenderer(attribute);
            if(templateExpression != '') {
                templateExpression = templateExpression + '<br/>';
            }
            templateExpression = templateExpression + '{' + attribute.name + ':render' + attribute.name + '}';
            Ext.util.Format['render' + attribute.name] = renderer;
        }
    }
    if(templateExpression == '') {
        var attribute = referencedType.attributes[0];
        var renderer = getRenderer(attribute);
        templateExpression = '{' + attribute.name + ':render' + attribute.name + '}';
        Ext.util.Format['render' + attribute.name] = renderer;
    }
    return templateExpression;
}

function getStore(type, uri) {
    var store = stores[type];
    if(store == null) {
        store = Ext.create('Ext.data.Store', {
            model: 'type.' + type,
            proxy: {
                type: 'ajax',
                url: uri,
                reader: {
                    type: 'json'
                }
            },
            autoLoad: true
        });
        stores[type] = store;
    } else {
        store.load();
    }
    return store;
}

function reloadStore(type) {
    var store = stores[type];
    if(store != null) {
        store.load();
    }
}

function registerType(type, namespace, namespaceNode) {
    type.namespace = namespace;
    type.getFullName = function() {
        return type.namespace.name + "." + type.name;
    };
    createTypeModel(type);
    var defaultOperation = null;
    type.actions = [ ];
    for(var j in type.operations) {
        var operation = type.operations[j];
        if(operation.defaultOperation) {
            defaultOperation = operation;
        }
        registerOperation(type, operation);
    }
    if(namespaceNode != null) {
        var typeNode = namespaceNode.appendChild({
            text: type.plural,
            iconCls: type.namespace.name + "-" + type.name,
            leaf: true
        });
        typeNode.doDefault = function() {
            this.type = type;
            this.defaultOperation = defaultOperation;
            doOperation(this.type, this.defaultOperation);
        };
        typeNode.contextMenu = Ext.create('Ext.menu.Menu', {
            items: type.actions
        });
    }
    types[type.getFullName()] = type;
}

function registerOperation(type, operation) {
    var operationId = operation.name;
    var action = Ext.create('Ext.Action', {
        iconCls: 'operation-' + operationId,
        text: operation.description,
        handler: function() {
            this.type = type;
            this.operation = operation;
            doOperation(this.type, this.operation);
        }
    });
    type.actions[operationId] = action;
}

function createTypeModel(type) {
    var fields = [{
        name: 'id',
        type: 'string'
    }];
    var associations = [];
    for(var i in type.attributes) {
        var attribute = type.attributes[i];
        fields.push({
            name: attribute.name,
            type: 'auto'
        });
    }
    Ext.define('type.' + type.getFullName(), {
        extend: 'Ext.data.Model',
        fields: fields,
        associations: associations
    });
}

function doOperation(type, operation, instance) {
    if(operation.name == 'list') {
        list(type, operation);
    } else if(operation.name == 'create') {
        create(type, operation);
    } else if(operation.name == 'update') {
    	update(type, operation, instance);
    } else {
    	window.alert('error.unknownOperation', operation.name);
    }
}

function update(type, operation, instance) {
	var id = 'type.' + type.getFullName() + '.update.' + instance.id;
	var form = createForm(type, operation, instance, id);
	var displayField = getDisplayField(type);
    var tab = openTab(id, operation.description + ': ' + instance[displayField], type.namespace.name + "-" + type.name,
        createFormToolbar(form, type, operation, id), form, {
        beforeclose: function() {
            cancelForm(id);
            return false;
        }
    });
}

function list(type, operation) {
    var grid = createGrid(type, operation);
    openTab('type.' + type.getFullName() + '.list', type.plural,  type.namespace.name + "-" + type.name, 
        createGridToolbar(type, grid.getStore(), grid), grid);
}

function create(type, operation) {
	var id = 'type.' + type.getFullName() + '.create';
    var form = createForm(type, operation, null, id);
    var tab = openTab(id, operation.description, type.namespace.name + "-" + type.name,
        createFormToolbar(form, type, operation, id), form, {
        beforeclose: function() {
            cancelForm(id);
            return false;
        }
    });
}

var tabPanel = Ext.create('Ext.tab.Panel', {
    region: 'center',
    items: [ ]
});

function openTab(id, title, iconCls, toolbar, content, listeners) {
    var component = tabPanel.getComponent(id);
    if(!component) {
        component = tabPanel.add({
            title: title,
            iconCls: iconCls,
            itemId: id,
            closable: true,
            layout: 'fit',
            dockedItems: [ toolbar ],
            items: content,
            listeners: listeners
        });
    }
    component.show();
    return component;
}

function closeTab(id) {
    tabPanel.remove(id);
}

function createFormToolbar(form, type, operation, id) {
    var saveAndClose = Ext.create('Ext.button.Button', {
        scale: 'medium',
        iconAlign: 'top',
        tooltip: translate('button.saveAndClose'),
        iconCls: 'toolbar-ok',
        disabled: true,
        handler: function() {
            saveAndCloseForm(form, type, operation, id);
        }
    });
    form.saveAndClose = saveAndClose;
    return Ext.create('Ext.toolbar.Toolbar', {
        defaults: {
            scale: 'medium',
            iconAlign:'top'
        },
        items: [saveAndClose, {
            tooltip: translate('button.cancel'),
            iconCls: 'toolbar-cancel',
            handler: function() {
                cancelForm(id);
            }
        }]
    });
}

function saveAndCloseForm(form, type, operation, id) {
    var prefix = 'type.' + type.getFullName();
    form.submit({
        url: operation.uri,
        method: operation.method,
        waitMsg: translate('progress.sendingData'),
        success: function() {
            closeTab(id);
            var listId = prefix + '.list';
            var listComponent = tabPanel.getComponent(listId);
            if(listComponent) {
                listComponent.show();
            }
            reloadStore(type.getFullName());
        },
        failure: function(response) {
            window.alert('error.failedToSaveChanges');
        }
    });
}
            
function cancelForm(id) {
    var component = tabPanel.getComponent(id);
    if(component) {
        var form = component.items.items[0];
        var dirty = false;
        form.items.each(function(field) {
            dirty = checkFieldDirty(field, dirty);
        });
        if(dirty) {
            Ext.MessageBox.show({
                title: translate('dialog.discardChanges.title'), 
                msg: translate('dialog.discardChanges.message'), 
                buttons: Ext.MessageBox.OKCANCEL,
                buttonText: {
                    ok: translate('dialog.discardChanges.ok'),
                    cancel: translate('button.cancel')
                },
                fn: function(answer) {
                    if(answer == 'ok') {
                        closeTab(id);
                    }
                },
                icon: 'dialog-question'
            });
        } else {
            closeTab(id);
        }
    }
}

function checkFieldDirty(field, dirty) {
	if(field.isXType('container')) {
		field.items.each(function(field) {
	        dirty = checkFieldDirty(field, dirty);
	    });
	} else if(field.isXType('field') && field.isDirty()) {
	    dirty = true;
	}
	return dirty;
}

function createForm(type, operation, instance, id) {
    var items = [ ];
    for(var i in operation.parameters) {
        var parameter = operation.parameters[i];
        var value = instance != null ? getParameterValue(instance, parameter, parameter.name) : null;
        items[items.length] = createField(type, parameter, value, id);
    }
    return Ext.create('Ext.form.Panel', {
        url: operation.uri,
        id: 'type.' + type.getFullName() + '.form' + (instance != null ? '.' + instance.id : ''),
        defaultType: 'textfield',
        bodyStyle: 'padding:5px 5px 0',
        items: items,
        autoScroll: true
    });
}

function getParameterValue(instance, parameter, name) {
	var value = instance[name];
	if(parameter.type == 'enumeration') {
	    value = value.id;
    } else if(parameter.type == 'list') {
    	var result = [];
    	for(var i in value) {
    		result.push(value[i].id);
    	}
    	value = result;
	} else if(name.indexOf('.') != -1) {
		var parent = name.substring(0, name.indexOf('.'));
		var child = name.substring(name.indexOf('.') + 1)
		value = getParameterValue(instance[parent], parameter, child);
    }
	return value;
}

function createField(type, parameter, value, id) {
    var attributes = {
        msgTarget: 'side',
        fieldLabel: parameter.description,
        name: parameter.name,
        allowBlank: !parameter.required,
        width: 500,
        vtype: getVType(parameter.type),
        value: value,
        listeners: {
            validitychange: function() {
                checkFormValid(id);
            }
        }
    };
    var factory = fieldFactories[parameter.type];
    var field = null;
    if(factory != null) {
        field = factory(type, parameter, attributes);
    } else {
    	field = Ext.create('Ext.form.field.Text', attributes);
    }
    return field;
}

function checkFormValid(id) {
    var component = tabPanel.getComponent(id);
    if(component) {
        var form = component.items.items[0];
        var valid = true;
        form.items.each(function(field) {
            valid = checkFieldValid(field, valid);
        });
        if(valid) {
            form.saveAndClose.enable();
        } else {
            form.saveAndClose.disable();
        }
    }
}

function checkFieldValid(field, valid) {
	if(field.isXType('container')) {
		field.items.each(function(field) {
            valid = checkFieldValid(field, valid);
        });
	} else if(field.isXType('field') && !field.isValid()) {
        valid = false;
    }
	return valid;
}

function getVType(type) {
    var result = null;
    switch(type) {
    case 'email':
    case 'url':
        result = type;
        break;
    }
    return result;
}

function createGridToolbar(type, store, grid) {
    var items = [ ];
    var basicOperations = 0;
    for(var i in type.operations) {
        var operation = type.operations[i];
        if(!operation.uri.endsWith('/list')) {
            items.push(createButton(type, operation));
            basicOperations++;
        }
    }
    items.push('-');
    items.push({
        tooltip: translate('button.refresh', type.plural),
        iconCls: 'toolbar-refresh',
        handler: function() {
            store.load();
        }
    });
    /* items[items.length] = {
        xtype: 'textfield',
        emptyText: 'Search',
        height: 24
    };
    items[items.length] = '-';
    items[items.length] = {
        tooltip: 'Print',
        iconCls: 'toolbar-print',
        disabled: true
    };
    items[items.length] = {
        tooltip: 'Export',
        iconCls: 'toolbar-export',
        menu: Ext.create('Ext.menu.Menu', {
            items: [{
                text: 'Export to Google Docs',
                iconCls: 'toolbar-export-cloud'
            }, {
                text: 'Export to Excel',
                iconCls: 'toolbar-export-excel'
            }, {
                text: 'Export to CSV',
                iconCls: 'toolbar-export-csv'
            }, {
                text: 'Export to PDF',
                iconCls: 'toolbar-export-pdf'
            }]
        })
    };*/
    
    var toolbar = Ext.create('Ext.toolbar.Toolbar', {
        defaults: {
            scale: 'medium',
            iconAlign: 'top'
        },
        items: items
    });
    toolbar.instanceOperations = [];
    
    store.addListener('load', function(store, records) {
        for(var i in records) {
            var record = records[i];
            for(var j in record.raw.operations) {
                var operation = record.raw.operations[j];
                if(toolbar.instanceOperations[operation.name] == null) {
                    toolbar.instanceOperations[operation.name] = operation;
    				toolbar.insert(basicOperations, createInstanceButton(type, operation, grid));
                }
            }
        }
    });
    
    return toolbar;
}

function createButton(type, operation) {
    return {
        tooltip: operation.description,
        iconCls: 'toolbar-' + operation.name,
        handler: function() {
            doOperation(type, operation);
        }
    };
}

function createInstanceButton(type, operation, grid) {
    var button = Ext.create('Ext.Button', {
        tooltip: operation.description,
        iconCls: 'toolbar-' + operation.name,
        handler: function() {
            var selected = grid.getSelectionModel().getSelection()[0];
            doOperation(type, getOperation(selected.raw, operation.name), selected.raw);
        },
        scale: 'medium',
        iconAlign: 'top',
        disabled: true
    });
    grid.addListener('selectionchange', function(selectionModel, selection) {
        if(selection.length == 1) {
        	button.enable();
        } else {
        	button.disable();
        }
    });
    return button;
}

function getOperation(item, name) {
	for(var i in item.operations) {
		var operation = item.operations[i];
		if(operation.name == name) {
			return operation;
		}
		return null;
	}
}

function createGrid(type, operation) {
    var columns = [ ];
    for(var i in type.attributes) {
        var attribute = type.attributes[i];
        addColumn(columns, attribute);
    }
    return Ext.create('Ext.grid.Panel', {
        store: getStore(type.getFullName(), operation.uri),
        loadMask: true,
        stateful: true,
        stateId: 'Type.' + type.getFullName() + '.gridState',
        columns: columns,
        viewConfig: {
            stripeRows: true
        },
        listeners: {
            itemdblclick: function(grid, record) {
            	for(var i in record.raw.operations) {
            		var operation = record.raw.operations[i];
            		if(operation.defaultOperation) {
            			doOperation(type, operation, record.raw);
            			break;
            		}
            	}
            },
        },
        minHeight: 50
    });
}

function addColumn(columns, attribute, parent) {
    var column = {
        text: attribute.description,
        flex: 1,
        sortable: true,
        dataIndex: parent != null ? parent.name : attribute.name,
    };
    if(attribute.type == 'composite') {
        var cols = [ ];
        for(var i in attribute.components) {
            var a = attribute.components[i];
            addColumn(cols, a, attribute);
        }
        column.columns = cols;
    } else {
        column.renderer = parent != null ? new CompositeRenderer(attribute).render : getRenderer(attribute);
    }
    columns.push(column);
}

function getRenderer(attribute) {
    switch(attribute.type) {
    case 'image':
        return renderImage;
        break;
    case 'enumeration':
        return renderEnumeration;
        break;
    case 'list':
        return new ListRenderer(attribute).render;
        break;
    case 'richText':
        return renderHtml;
        break;
    default:
        return renderPlain;
        break;
    }
}

function renderImage(value) {
    return '<img src="' + Ext.String.htmlEncode(value) + '"/>';
}

function renderEnumeration(value) {
    return Ext.String.htmlEncode(value.description);
}

function CompositeRenderer(attribute) {
    var renderChild = getRenderer(attribute);
    this.render = function(value) {
        var childValue = value[attribute.name];
        return renderChild(childValue);
    }
}

function ListRenderer(attribute) {
    var referencedType = types[attribute.referencedType];
    this.render = function(value) {
        var describingAttribute = null;
        for(var i in referencedType.attributes) {
            var attribute = referencedType.attributes[i];
            if(attribute.describing) {
                describingAttribute = attribute;
                break;
            }
        }
        if(describingAttribute == null) {
            describingAttribute = referencedType.attributes[0];
        }
        var attributeName = describingAttribute.name;
        var render = getRenderer(describingAttribute);
        var result = '';
        for(var i in value) {
            if(result.length > 0) {
                result = result + ', ';
            }
            result = result + render(value[i][attributeName]);
        }
        return result;
    }
}

function renderPlain(value) {
    return Ext.String.htmlEncode(value);
}

function renderHtml(value) {
    return value;
}

var dataTreeStore = Ext.create('Ext.data.TreeStore', {
    root: {
        expanded: true,
        children: [ ]
    }
});

var dataTreePanel = Ext.create('Ext.tree.Panel', {
    useArrows: true,
    border: 0,
    store: dataTreeStore,
    rootVisible: false,
    listeners: {
        itemclick: function(treePanel, record) {
            if(record.doDefault) {
                record.doDefault();
            }
        },
        // TODO: fix this
        itemcontextmenu: function(view, record, node, index, e) {
            if(record.contextMenu) {
                e.stopEvent();
                record.contextMenu.showAt(e.getXY());
                return false;
            }
        }
    }
});

function initializeUI() {
	Ext.onReady(function() {
		var profileButton = Ext.create('Ext.button.Button', {
			scale: 'large',
			iconAlign: 'right',
			icon: '../resources/icons/profile/profile.png',
			menu: [{
				text: translate('profile.account'),
				icon: '../resources/icons/profile/account.png',
				handler: function() {
					window.open('https://www.google.com/settings/');
				}
			}, {
				text: translate('profile.privacy'),
				icon: '../resources/icons/profile/privacy.png',
				handler: function() {
					window.open('https://www.google.com/settings/privacy?tab=4');
				}
			}, '-', {
				text: translate('profile.viewProfile'),
				icon: '../resources/icons/profile/view-profile.png',
				handler: function() {
					window.open('https://profiles.google.com/?tab=h&authuser=0');
				}
			}, '-', {
				text: translate('profile.signOut'),
				icon: '../resources/icons/profile/sign-out.png',
				handler: function() {
					window.open('https://mail.google.com/mail/u/0/?logout');
				}
			}]
		});

		var currentProfile = Ext.create('Ext.data.Store', {
			model: 'Profile',
			proxy: {
		        type: 'ajax',
		        url: 'security/profiles/current',
		        reader: {
		            type: 'json'
		        }
			},
			listeners: {
		        load: function() {
		        	var profile = currentProfile.first();
		        	if(profile) {
			        	profileButton.setText(profile.data.name);
			        	// TODO: adjust size
			        	profileButton.setIcon(profile.data.pictureThumb);
		        	}
		        }
			},
		    autoLoad: true
		});
		
		var version = '${version}';
		if(version.indexOf('SNAPSHOT') != -1) {
			version = version.replace('SNAPSHOT', '${buildNumber}');
		}
	    Ext.create('Ext.Viewport', {
	        layout: {
	            type: 'border',
	            padding: 5
	        },
	        defaults: {
	            split: true
	        },
	        items: [{
	            region: 'north',
	            collapsible: false,
	            split: false,
	            height: 64,
	            xtype: 'toolbar',
	            id: 'app-header',
	        	items: [{
	                xtype: 'box',
	                html: '<h1>Appify Administrator</h1>' +
	                      '<div class="version">Appify v' + version + ' &bull; ' +
	                      applicationName + ' v' + applicationVersion + '</div>'
	        	}, '->', profileButton]
	        },
	        {
	            region: 'west',
	            layout:'accordion',
	            width: 200,
	            collapsible: true,
	            layoutConfig: {
	                titleCollapse: false,
	                animate: true
	            },
	            items: [{
	                title: translate('menu.data'),
	                iconCls: 'menu-data',
	                layout: 'fit',
	                items: [dataTreePanel]
	            }]
	        },
	        tabPanel]
	    });
	    
	    dataTreePanel.setLoading(true);
	    namespaces.load();
	});
}
