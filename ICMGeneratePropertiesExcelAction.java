define(
	["dojo/_base/declare", "icm/action/Action", "dojo/dom-style",
		"dijit/form/Button", 'dijit/form/ComboBox',
		"dojo/_base/declare", "dojo/_base/lang", "dojo/_base/array",
		"dojo/parser", "dijit/form/ToggleButton", "dojox/grid/cells",
		"dijit/ToolbarSeparator", "icm/util/Coordination",
		"dijit/form/Select", "ecm/widget/dialog/BaseDialog",
		"ecm/widget/FilteringSelect", "dojox/grid/DataGrid",
		"dojox/grid/cells/dijit", "dojo/data/ItemFileWriteStore",
		"dijit/dijit", "dojox/layout/TableContainer",
		"dojox/grid/EnhancedGrid", "dojox/form/uploader/FileList",
		"dijit/layout/TabContainer", "dijit/layout/ContentPane",
		"dijit/form/Button", "dijit/layout/LayoutContainer",
		"pvr/widget/TabContainer", "pvr/widget/Layout",
		"gridx/modules/Header", "dojo/dom-construct", "dijit/Toolbar",
		"pvr/widget/PropertyTable", "dojo/dom-class",
		"gridx/modules/RowHeader", "dojo/data/ObjectStore",
		"dojo/store/Memory", "gridx/modules/CellWidget",
		"gridx/modules/dnd/Row", "gridx/modules/Sort",
		"pvr/widget/editors/grid/GridEditor",
		"pvr/widget/editors/grid/GridSummary", "dojo/aspect",
		"dojo/dom-attr", "dojo/request/xhr", "dojo/dom", "dojo/on",
		"dojo/mouse", "dojo/domReady!"
	],
	function(declare, Action, domStyle, Button, ComboBox, declare, lang,
		array, parser, ToggleButton, cells, ToolbarSeparator,
		Coordination, Select, BaseDialog, FilteringSelect, DataGrid,
		cellsDijit, ItemFileWriteStore, dijit, TableContainer,
		EnhancedGrid, FileList, TabContainer, ContentPane, Button,
		LayoutContainer, TabContainer, Layout, Header, domConstruct,
		Toolbar, PropertyTable, domClass, RowHeader, ObjectStore,
		Memory, CellWidget, Row, Sort, GridEditor, GridSummary, aspect,
		domAttr, xhr, dom, on, mouse) {
		return declare(
			"icmcustom.action.ICMGeneratePropertiesExcelAction",
			[Action], {
			solution: null,
			isEnabled: function() {
				var Solution = this.getActionContext("Solution");
				if (Solution === null || Solution.length == 0) {
					return false;
				} else {
					solution = Solution[0];
					return true;
				}
			},
			execute: function() {
				this.htmlTemplate = this.buildHtmlTemplate();
				this.initiateTaskDialog = new BaseDialog({
					cancelButtonLabel: "Cancel",
					contentString: this.htmlTemplate,
					createGrid: function() {
						var taskLayout = new dijit.layout.TabContainer({
							cols: 1,
							spacing: 5,
							showLabels: true,
							orientation: "vert"
						});

						var propData = {
							items: []
						};

						var propdata_list = solution.attributeDefinitions;
						var rows = propdata_list.length;
						for (var i = 0; i < rows; i++) {
							var propSymbolicName = propdata_list[i].symbolicName;
							if (propSymbolicName.includes("_")) {
								var propList = propSymbolicName.split("_");
								if (propList[0] == "LA") {
									propData.items.push(propdata_list[i]);
								}
							}
						}

						for (var i = 0; i < propData.items.length; i++) {
							propData.items[i].id = propData.items[i].name;
						}

						var stateStore = new Memory({
							data: propData
						});
						/*var os = new ObjectStore({
							objectStore: stateStore
						});

						function formatter() {
							var select1 = new Select({
								store: os,

							});
							// select1._destroyOnRemove =
							// true;
							return select1;
						}*/
						var data = {
							identifier: "id",
							items: []
						};
						var node = dom.byId("addButton");
						var i = 0;
						on(node, "click", function() {
							var myNewItem = {
								id: (++i),
								pname: "",
								sname: "",
								isreq: ""
							};
							store.newItem(myNewItem);
						});
						var remnode = dom.byId("remButton");
						on(remnode, "click", function() {
							var items = grid.selection.getSelected();

							if (items.length) {
								dojo.forEach(items, function(selectedItem) {
									if (selectedItem != null) {
										store.deleteItem(selectedItem);
										store.save();
									}
								})
							}
						})
						layoutProperties = [{
							defaultCell: {
								width: 5,
								editable: false,
								type: cells._Widget
							},
							cells: [
								new dojox.grid.cells.RowIndex({
									name: "ID",
									width: '25px'
								}),

								{
									field: "pname",
									name: "Property Name",
									type: dojox.grid.cells._Widget,
									widgetClass: dijit.form.FilteringSelect,
									widgetProps: {
										id: name,
										store: stateStore,
										onChange: function() {
											var store = grid.store;
											var index = grid.selection.selectedIndex;
											var item = grid.getItem(index);
											store.setValue(item, 'sname', this.item.symbolicName);
											store.setValue(item, 'isreq', this.item.required);
											grid.update();
										}
									},
									searchAttr: "id",
									width: '109px',
									editable: true
								},
								{
									field: "sname",
									name: "Symbolic Name",
									width: '109px',
									height: '109px',
									editable: false
								},
								{
									field: "isreq",
									name: "isRequired?",
									width: '109px',
									height: '109px',
									editable: false
								}
							]
						}];

						var store = new ItemFileWriteStore({
							data: data
						});

						var grid = new DataGrid({
							id: 'grid',
							store: store,
							structure: layoutProperties,
							rowSelector: '20px'
						});
						grid.placeAt("gridDiv");
						grid.startup();
						// grid.resize({w: '400', h:
						// '300'});
					},
					onExecute: function() {
						alert('Button clicked');
					}
				});
				this.initiateTaskDialog.setTitle("Properties");
				this.initiateTaskDialog.createGrid();
				this.initiateTaskDialog.setSize(500, 500);
				this.initiateTaskDialog.addButton("Ok",this.initiateTaskDialog.onExecute, false,false);
				this.initiateTaskDialog.setResizable(true);
				this.initiateTaskDialog.show();
			},
			buildHtmlTemplate: function() {
				// var dialogueBoxName = "Chose Case Type";
				var htmlstring = '<div style="width: 500px; height: 300px;"><div data-dojo-type="dijit/layout/TabContainer" style="width: 100%; height: 100%;">' +
					'<div id="gridDiv" data-dojo-type="dijit/layout/ContentPane" title="Properties" ></div>' +
					'</div></div>' +
					'<div class="pvrPropertyTable" id="toolBar1"><div class="pvrPropertyTableGrid" data-dojo-attach-point="_gridNode"></div>' +
					// '<div id="toolbar1"
					// data-dojo-type="dijit/Toolbar">'+
					'<div class="pvrPropertyTableToolbar pvrGridToolbar" data-dojo-type="dijit/Toolbar"  data-dojo-attach-point="_toolbar">' +
					'<div data-dojo-type="dijit/form/Button" data-dojo-attach-point="_addButton" id="addButton"' +
					'data-dojo-props="iconClass:\'addButton\', showLabel:false" <!--data-dojo-attach-event="onClick: _onClickAdd"-->>add</div>' +
					'<div data-dojo-type="dijit/form/Button" data-dojo-attach-point="_removeButton" id="remButton"' +
					'data-dojo-props="iconClass:\'removeButton\', showLabel:false">remove</div>'
				// + '</div></div>'
				'<div class="pvrPropertyTableHidden" data-dojo-attach-point="containerNode"></div' +
					'></div></div>';
				return htmlstring;
			},
		});
	});
