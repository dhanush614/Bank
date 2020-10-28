define([
	"dojo/_base/declare", "icm/action/Action", "dojo/dom-style",
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
	"dojo/mouse",
	"dojo/_base/declare",
	"icm/action/Action",
	"dojo/dom-style",
	"dijit/form/Button",
	"dojo/_base/declare",
	"dojo/_base/lang",
	"icm/util/Coordination",
	"ecm/widget/dialog/BaseDialog",
	"ecm/widget/FilteringSelect",
	"dojo/data/ItemFileWriteStore",
	"dojox/form/Uploader",
	"dojox/form/uploader/FileList",
	"dojo/aspect",
	"icmcustom/js/xlsx",
	"icmcustom/js/jszip",
	"dojo/dom-attr",
	"dojo/request/xhr",
	"dojo/domReady!"
], function(declare, Action, domStyle, Button, ComboBox, declare, lang,
	array, parser, ToggleButton, cells, ToolbarSeparator,
	Coordination, Select, BaseDialog, FilteringSelect, DataGrid,
	cellsDijit, ItemFileWriteStore, dijit, TableContainer,
	EnhancedGrid, FileList, TabContainer, ContentPane, Button,
	LayoutContainer, TabContainer, Layout, Header, domConstruct,
	Toolbar, PropertyTable, domClass, RowHeader, ObjectStore,
	Memory, CellWidget, Row, Sort, GridEditor, GridSummary, aspect,
	domAttr, xhr, dom, on, mouse, declare, Action, domStyle, Button, declare, lang, Coordination, BaseDialog, FilteringSelect, ItemFileWriteStore, Uploader, FileList, aspect, xlsx, jszip, domAttr, xhr) {

	return declare("icmcustom.action.ICMGeneratePropertiesExcelAction", [Action], {
		solution: null,

		isEnabled: function() {

			var Solution = this.getActionContext("Solution");
			if (Solution === null || Solution.length == 0) {
				return false;
			} else {
				this.solution = Solution[0];
				return true;
			}
		},
		execute: function() {
			this.caseType = this.propertiesValue.caseType;
			this.caseProperties = this.propertiesValue.caseProperties;
			console.log(this.caseType + " " + this.caseProperties);

			this.htmlTemplate = this.buildHtmlTemplate();
			var caseTypeDrop;

			this.initiateTaskDialog = new BaseDialog({
				cancelButtonLabel: "Cancel",
				contentString: this.htmlTemplate,

				createGrid: function() {

					//dojo.place("<div><input type='file' id='uploadFile'></div>", "mainDiv","after");

					var solution = ecm.model.desktop.currentSolution;
					var caseType = solution.getCaseTypes();
					var caseTyepList = [];
					var data = {
						items: []
					};

					for (var i = 0; i < caseType.length; i++) {
						caseTyepList.push({
							id: caseType[i].id,
							value: caseType[i].id
						});
					}

					for (var l = 0; l < caseTyepList.length; l++) {
						data.items.push(caseTyepList[l]);
					}
					var typeStore = new dojo.data.ItemFileWriteStore({
						data: data
					})
					var displayName = (new Date()).getTime() + "primaryInputField";
					caseTypeDrop = new FilteringSelect({
						displayName: displayName,
						name: "primaryInputField",
						store: typeStore,
						autoComplete: true,
						style: {
							width: "200px"
						},
						placeHolder: 'Select the required Case Type',
						required: true,
						searchAttr: "value"
					});

					caseTypeDrop.placeAt(this.primaryInputField);
					caseTypeDrop.startup();

				},
				onExecute: function() {
					this.htmlTemplate = this.buildHtmlTemplate1();
					this.initiateTaskDialog1 = new BaseDialog({
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
							var solution = ecm.model.desktop.currentSolution;
							var caseTypes = solution.caseTypes;
							var caseTypeValue = caseTypeDrop.value;
							for (var i = 0; i < caseTypes.length; i++) {
								if (caseTypes[i].id == caseTypeValue) {
									var propdata_list = caseTypes[i].solution.attributeDefinitions;
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


								}
							}
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
						}/*,
						onExecute: function() {
							alert('Button clicked');
						}*/
					});
					this.initiateTaskDialog1.setTitle("Properties");
					this.initiateTaskDialog1.createGrid();
					this.initiateTaskDialog1.setSize(500, 500);
					//this.initiateTaskDialog1.addButton("Ok", this.initiateTaskDialog.onExecute, false, false);
					this.initiateTaskDialog1.setResizable(true);
					this.initiateTaskDialog1.show();

				},
				buildHtmlTemplate1: function() {
					// var dialogueBoxName = "Chose Case Type";
					var htmlstring1 = '<div style="width: 500px; height: 300px;"><div data-dojo-type="dijit/layout/TabContainer" style="width: 100%; height: 100%;">' +
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
					return htmlstring1;
				},


			});
			this.initiateTaskDialog.setTitle("Create Case");
			this.initiateTaskDialog.createGrid();
			this.initiateTaskDialog.setSize(450, 500);
			this.initiateTaskDialog.addButton("Ok", this.initiateTaskDialog.onExecute, false, false);
			this.initiateTaskDialog.setResizable(true);
			this.initiateTaskDialog.show();

		},
		buildHtmlTemplate: function() {
			var dialogueBoxName = "Chose Case Type";
			var htmlstring = '<div class="fieldsSection"><div class="fieldLabel" id="mainDiv"><span style="color:red" class="mandatory">**</span><label for="primaryInputFieldLabel">' + dialogueBoxName + ':</label><div data-dojo-attach-point="primaryInputField"/></div></div></div>';
			return htmlstring;
		},

	});
});
