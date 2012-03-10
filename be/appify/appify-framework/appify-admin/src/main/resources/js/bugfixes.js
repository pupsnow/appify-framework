// bugfix for D-6 Combo box keeps loading after adding an image
// Also fixed in Ext JS 4.1
Ext.override(Ext.form.field.ComboBox, { 
    createPicker: function() {
        var me = this,
            picker,
            menuCls = Ext.baseCSSPrefix + 'menu',
            opts = Ext.apply({
                pickerField: me,
                selModel: {
                    mode: me.multiSelect ? 'SIMPLE' : 'SINGLE'
                },
                floating: true,
                hidden: true,
                ownerCt: me.ownerCt,
                cls: me.el.up('.' + menuCls) ? menuCls : '',
                store: me.store,
                displayField: me.displayField,
                focusOnToFront: false,
                pageSize: me.pageSize,
                tpl: me.tpl,
                loadMask: me.queryMode === 'local' ? false: true
            }, me.listConfig, me.defaultListConfig);


        picker = me.picker = Ext.create('Ext.view.BoundList', opts);
        if (me.pageSize) {
            picker.pagingToolbar.on('beforechange', me.onPageChange, me);
        }


        me.mon(picker, {
            itemclick: me.onItemClick,
            refresh: me.onListRefresh,
            scope: me
        });


        me.mon(picker.getSelectionModel(), {
            'beforeselect': me.onBeforeSelect,
            'beforedeselect': me.onBeforeDeselect,
            'selectionchange': me.onListSelectionChange,
            scope: me
        });


        return picker;
    }
});

// bugfix for D-7 Form detects changes for projects even though there are none
Ext.override(Ext.form.HtmlEditor, {
    defaultValue: (Ext.isOpera || Ext.isIE6) ? '&#160;' : '​',
    initEvents: function(){
        this.originalValue = Ext.isGecko ? '&nbsp;' : this.getValue();
        if(Ext.isGecko){
            this.setValue('&nbsp;');
        }
    },
    cleanHtml: function(html) {
        html = String(html);
        if(Ext.isWebKit){ // strip safari nonsense
            html = html.replace(/\sclass="(?:Apple-style-span|khtml-block-placeholder)"/gi, '');
        }
        if(html.charCodeAt(0) == this.defaultValue.replace(/\D/g, '')){
            html = html.substring(1);
        }
        return html;
    }
});