(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-4d2b8cfb"],{"08f2":function(e,t,i){"use strict";i("438d")},"438d":function(e,t,i){},"811b":function(e,t,i){},"93ac":function(e,t,i){},b669:function(e,t,i){"use strict";i("811b")},dc31:function(e,t,i){"use strict";i.r(t);var a=function(){var e=this,t=e._self._c;return t("div",{staticClass:"avoid-bother-customer-wrap fe-m-24 fe-p-24 bg"},[t("div",{staticClass:"search_box"},[t("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],staticClass:"input-search",attrs:{placeholder:"请输入菜单名称查询"},model:{value:e.queryFrom.keyWords,callback:function(t){e.$set(e.queryFrom,"keyWords","string"===typeof t?t.trim():t)},expression:"queryFrom.keyWords"}},[t("i",{staticClass:"el-input__icon el-icon-search cursor-pointer",attrs:{slot:"suffix"},on:{click:function(t){return e.queryApproveInfo()}},slot:"suffix"})]),t("div",{staticClass:"btn_box"},[t("el-button",{attrs:{type:"success"},on:{click:e.menuAdd}},[e._v("新建")])],1)],1),t("div",{staticClass:"list"},[t("el-table",{staticStyle:{width:"100%"},attrs:{data:e.tableData,"row-key":"menuId","tree-props":{children:"children",hasChildren:"hasChildren"}}},[t("el-table-column",{attrs:{prop:"menuitemTitle",label:"菜单名称","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{prop:"visible",label:"菜单状态","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{prop:"path",label:"菜单前端地址","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{prop:"createTime",label:"创建时间","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{label:"操作",width:"250"},scopedSlots:e._u([{key:"default",fn:function(i){return[t("el-link",{staticClass:"text-sm mr-16",attrs:{type:"primary",underline:!1},on:{click:function(t){return e.lookHanlder(i.row)}}},[e._v("查看 ")]),t("el-link",{staticClass:"text-sm mr-16",attrs:{type:"primary",underline:!1},on:{click:function(t){return e.editHanlder(i.row)}}},[e._v("编辑 ")]),i.row.isShow?t("el-link",{staticClass:"text-sm mr-16",attrs:{type:"primary",underline:!1},on:{click:function(t){return e.permissionsHanlder(i.row)}}},[e._v("权限管理 ")]):e._e(),t("el-popconfirm",{attrs:{placement:"top",icon:"el-icon-warning",iconColor:"#FAAD14",title:"确定删除？","cancel-button-type":"Primary"},on:{confirm:function(t){return e.deleteHanlder(i.row)}}},[t("el-link",{staticClass:"cursor-pointer text-sm",attrs:{slot:"reference",type:"danger",underline:!1},slot:"reference"},[e._v(" 删除")])],1)]}}])})],1)],1),e.dialogVisible?t("menu-dialog",{attrs:{dialogVisible:e.dialogVisible,dialogTitle:e.dialogTitle,operationType:e.operationType,rowData:e.rowData},on:{"update:dialogVisible":function(t){e.dialogVisible=t},"update:dialog-visible":function(t){e.dialogVisible=t},getMenuListData:e.getMenuListData}}):e._e(),e.permissionsDialogVisible?t("permission-dialog",{attrs:{dialogVisible:e.permissionsDialogVisible,dialogTitle:e.dialogTitle,operationType:e.operationType,rowData:e.rowData},on:{"update:dialogVisible":function(t){e.permissionsDialogVisible=t},"update:dialog-visible":function(t){e.permissionsDialogVisible=t},getMenuListData:e.getMenuListData}}):e._e()],1)},o=[],s=i("0fea"),l=i("a454"),r=function(){var e=this,t=e._self._c;return t("div",[t("el-dialog",{attrs:{title:e.dialogTitle,visible:e.dialogVisible,"before-close":e.closeHandler,width:"500px","close-on-click-modal":!1,"append-to-body":""},on:{"update:visible":function(t){e.dialogVisible=t}}},[t("div",{staticClass:"Contact-mtp"},[t("el-form",{ref:"menuFrom",attrs:{model:e.formData,"label-width":"120px",rules:e.rules}},[t("el-form-item",{attrs:{label:"菜单名称",prop:"menuitemTitle"}},[t("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],attrs:{maxlength:"16",disabled:"look"===e.operationType},model:{value:e.formData.menuitemTitle,callback:function(t){e.$set(e.formData,"menuitemTitle",t)},expression:"formData.menuitemTitle"}})],1),t("el-form-item",{attrs:{label:"菜单ID",prop:"menuId"}},[t("el-input",{attrs:{disabled:""},model:{value:e.formData.menuId,callback:function(t){e.$set(e.formData,"menuId",t)},expression:"formData.menuId"}})],1),t("el-form-item",{attrs:{label:"父菜单",prop:"parentId"}},[t("el-select",{attrs:{filterable:"",placeholder:"请选择",disabled:"look"===e.operationType},model:{value:e.formData.parentId,callback:function(t){e.$set(e.formData,"parentId",t)},expression:"formData.parentId"}},e._l(e.parentMenuArr,(function(e,i){return t("el-option",{key:i,attrs:{label:e.menuitemTitle,value:e.menuId}})})),1)],1),t("el-form-item",{attrs:{label:"菜单状态",prop:"visible"}},[t("el-radio-group",{attrs:{disabled:"look"===e.operationType},model:{value:e.formData.visible,callback:function(t){e.$set(e.formData,"visible",t)},expression:"formData.visible"}},[e._l(e.visibleConfig,(function(i,a){return[t("el-radio",{key:a,attrs:{label:i.id,value:i.id}},[e._v(e._s(i.name))])]}))],2)],1),t("el-form-item",{attrs:{label:"菜单前端地址",prop:"path"}},[t("el-input",{attrs:{disabled:"look"===e.operationType},model:{value:e.formData.path,callback:function(t){e.$set(e.formData,"path",t)},expression:"formData.path"}})],1),t("el-form-item",{attrs:{label:"菜单后端地址",prop:"url"}},[t("el-input",{attrs:{disabled:"look"===e.operationType},model:{value:e.formData.url,callback:function(t){e.$set(e.formData,"url",t)},expression:"formData.url"}})],1),t("el-form-item",{attrs:{label:"排序号",prop:"sortNum"}},[t("el-input-number",{attrs:{min:0,disabled:"look"===e.operationType},model:{value:e.formData.sortNum,callback:function(t){e.$set(e.formData,"sortNum",t)},expression:"formData.sortNum"}})],1)],1)],1),"look"!=this.operationType?t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:e.closeHandler}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.submit}},[e._v("保 存")])],1):e._e()])],1)},n=[],d=i("f9ae"),m={name:"MenuDialog",props:{dialogTitle:{type:String},dialogVisible:{type:Boolean,default:!1},operationType:{type:String,default:()=>"add"},rowData:{type:Object}},data(){return{formData:{visible:0},menuTypeArr:[],parentMenuArr:[],visibleConfig:[{id:0,name:"显示"},{id:1,name:"隐藏"}],rules:{menuId:[{required:!1,message:"请输入菜单ID",trigger:"blur"}],menuitemTitle:[{required:!0,message:"请输入菜单名称",trigger:"blur"}],parentId:[{required:!0,message:"请输入父菜单",trigger:"blur"}],menuType:[{required:!1,message:"请选择菜单类型",trigger:"blur"}],visible:[{required:!1,message:"请选择菜单状态",trigger:"blur"}],permission:[{required:!1,message:"请输入权限标识",trigger:"blur"}],path:[{required:!0,message:"请输入菜单前端地址",trigger:"blur"}],url:[{required:!1,message:"请输入菜单后端地址",trigger:"blur"}],sortNum:[{required:!0,message:"请输入排序号",trigger:"blur"}]}}},methods:{closeHandler(){this.$emit("update:dialogVisible",!1)},getSelectMenuData(){Object(s["i"])(l["a"].menu.getSelectMenuData).then(e=>{this.parentMenuArr=e})},getMenuInfo(){Object(s["i"])(l["a"].menu.lookMenuInfo,{id:this.rowData.menuId}).then(e=>{this.formData=e})},async addMenuInfo(){this.$refs["menuFrom"].validate(async e=>{e?await Object(s["i"])(l["a"].menu.addMenuInfo,this.formData).then(e=>{this.$message.success("新增成功"),this.$emit("getMenuListData"),this.closeHandler()}):this.$alert("请填写必填项","校验失败",{confirmButtonText:"确定",customClass:"errorPrompt"})})},async editMenuInfo(){this.$refs["menuFrom"].validate(async e=>{e?(this.formData.menuId=this.rowData.menuId,await Object(s["i"])(l["a"].menu.editMenuInfo,this.formData).then(e=>{this.$message.success("编辑成功"),this.$emit("getMenuListData"),this.closeHandler()})):this.$alert("请填写必填项","校验失败",{confirmButtonText:"确定",customClass:"errorPrompt"})})},submit:Object(d["a"])((function(){"add"===this.operationType?this.addMenuInfo():this.editMenuInfo()}))},created(){this.getSelectMenuData(),"add"!==this.operationType&&this.getMenuInfo()}},u=m,p=(i("08f2"),i("2877")),c=Object(p["a"])(u,r,n,!1,null,"54964903",null),b=c.exports,f=function(){var e=this,t=e._self._c;return t("div",[t("el-dialog",{attrs:{title:e.dialogTitle,visible:e.dialogVisible,"before-close":e.closeHandler,"close-on-click-modal":!1,width:"50%","append-to-body":""},on:{"update:visible":function(t){e.dialogVisible=t}}},[t("div",{staticClass:"Contact-mtp"},[t("div",[t("el-row",[t("el-col",{attrs:{span:16}},[t("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],staticClass:"searchInput",attrs:{placeholder:"请输入权限名称"},model:{value:e.keyWords,callback:function(t){e.keyWords=t},expression:"keyWords"}}),t("el-button",{staticClass:"searchBtn",attrs:{type:"primary"},on:{click:function(t){return e.queryList()}}},[e._v("查询")])],1),t("el-col",{attrs:{span:8}},[t("el-button",{attrs:{type:"success"},on:{click:e.permissionAdd}},[e._v("新增")])],1)],1)],1),t("el-table",{ref:"multipleTable",staticStyle:{width:"100%"},attrs:{data:e.tableData,"tooltip-effect":"dark"}},[t("el-table-column",{attrs:{type:"index",label:"序号",width:"55"}}),t("el-table-column",{attrs:{prop:"name",label:"权限名称",width:"120"}}),t("el-table-column",{attrs:{prop:"perm",label:"权限标识","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{prop:"type",label:"权限类型","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{label:"操作",width:"200"},scopedSlots:e._u([{key:"default",fn:function(i){return[t("el-button",{attrs:{type:"text"},on:{click:function(t){return e.editHanlder(i.row)}}},[e._v("编辑")]),t("el-button",{attrs:{type:"text"},on:{click:function(t){return e.deleteHanlder(i.row)}}},[e._v("删除")])]}}])})],1)],1)]),e.permissionsAddDialogVisible?t("permission-add-dialog",{attrs:{dialogVisible:e.permissionsAddDialogVisible,dialogTitle:e.addDialogTitle,operationType:e.addOperationType,rowData:e.addRowData},on:{"update:dialogVisible":function(t){e.permissionsAddDialogVisible=t},"update:dialog-visible":function(t){e.permissionsAddDialogVisible=t},getMenuPermission:e.getMenuPermission}}):e._e()],1)},g=[],h=function(){var e=this,t=e._self._c;return t("div",[t("el-dialog",{attrs:{title:e.dialogTitle,visible:e.dialogVisible,"before-close":e.closeHandler,"close-on-click-modal":!1,width:"50%","append-to-body":""},on:{"update:visible":function(t){e.dialogVisible=t}}},[t("div",{staticClass:"Contact-mtp"},[t("el-form",{ref:"permissionFrom",attrs:{model:e.formData,"label-width":"120px",rules:e.rules}},[t("el-form-item",{attrs:{label:"权限名称",prop:"name"}},[t("el-input",{attrs:{maxlength:"10"},model:{value:e.formData.name,callback:function(t){e.$set(e.formData,"name",t)},expression:"formData.name"}})],1),t("el-form-item",{attrs:{label:"权限标识",prop:"perm"}},[t("el-input",{model:{value:e.formData.perm,callback:function(t){e.$set(e.formData,"perm",t)},expression:"formData.perm"}})],1),t("el-form-item",{attrs:{label:"权限类型",prop:"type"}},[t("el-select",{attrs:{filterable:"",placeholder:"请选择"},model:{value:e.formData.type,callback:function(t){e.$set(e.formData,"type",t)},expression:"formData.type"}},e._l(e.typeArr,(function(e,i){return t("el-option",{key:i,attrs:{label:e.type,value:e.id}})})),1)],1)],1)],1),"look"!=this.operationType?t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:e.closeHandler}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.submit}},[e._v("保 存")])],1):e._e()])],1)},y=[],D={name:"PermissionAddDialog",props:{dialogTitle:{type:String},dialogVisible:{type:Boolean,default:!1},operationType:{type:String,default:()=>"add"},rowData:{type:Object}},data(){return{formData:{},typeArr:[{id:1,type:"接口"},{id:2,type:"按钮"}],rules:{name:[{required:!0,message:"请输入权限名称",trigger:"blur"}],perm:[{required:!0,message:"请输入权限标识",trigger:"blur"}],type:[{required:!0,message:"请输入权限类型",trigger:"blur"}]}}},methods:{closeHandler(){this.$emit("update:dialogVisible",!1)},getView(){Object(s["i"])(l["a"].menu.getPermissionInfo,{id:this.rowData.id}).then(e=>{this.formData=e})},async addPermissionInfo(){this.$refs["permissionFrom"].validate(async e=>{e?(this.formData.menuId=this.rowData.menuId,await Object(s["i"])(l["a"].menu.addPermissionList,this.formData).then(e=>{this.$message.success("新增成功"),this.$emit("getMenuPermission"),this.closeHandler()})):this.$alert("请填写必填项","校验失败",{confirmButtonText:"确定",customClass:"errorPrompt"})})},async editPermissionInfo(){this.$refs["permissionFrom"].validate(async e=>{e?(this.formData.id=this.rowData.id,await Object(s["i"])(l["a"].menu.editPermissionList,this.formData).then(e=>{this.$message.success("编辑成功"),this.$emit("getMenuPermission"),this.closeHandler()})):this.$alert("请填写必填项","校验失败",{confirmButtonText:"确定",customClass:"errorPrompt"})})},submit(){"add"===this.operationType?this.addPermissionInfo():this.editPermissionInfo()}},created(){"add"!==this.operationType&&this.getView()}},v=D,w=(i("ef44"),Object(p["a"])(v,h,y,!1,null,"26d2b396",null)),T=w.exports,k={name:"PermissionDialog",components:{PermissionAddDialog:T},props:{dialogTitle:{type:String},dialogVisible:{type:Boolean,default:!1},operationType:{type:String,default:()=>"add"},rowData:{type:Object}},data(){return{keyWords:"",formData:{},menuTypeArr:[],parentMenuArr:[],addRowData:{},addDialogTitle:"",permissionsAddDialogVisible:!1,visibleConfig:[{id:0,name:"显示"},{id:1,name:"隐藏"}],tableData:[]}},methods:{closeHandler(){this.$emit("update:dialogVisible",!1)},queryList(){this.getMenuPermission()},permissionAdd(){this.addDialogTitle="权限管理新增",this.addOperationType="add",this.permissionsAddDialogVisible=!0,this.addRowData.menuId=this.rowData.menuId},editHanlder(e){this.addDialogTitle="权限管理编辑",this.addOperationType="edit",this.permissionsAddDialogVisible=!0,this.addRowData=e},deleteHanlder(e){Object(s["i"])(l["a"].menu.deletePermissionList,{id:e.id}).then(e=>{this.$message({type:"success",message:"删除成功!"}),this.getMenuPermission()})},getMenuPermission(){Object(s["i"])(l["a"].menu.getMenuPermissionList,{menuId:this.rowData.menuId,keyWords:this.keyWords}).then(e=>{e.forEach(e=>{e.type=1===e.type?"接口":"按钮"}),this.tableData=e})}},created(){this.getMenuPermission()}},I=k,V=(i("b669"),Object(p["a"])(I,f,g,!1,null,"6c91385a",null)),M=V.exports,_={name:"DepartmentManage",components:{MenuDialog:b,PermissionDialog:M},data(){return{operationType:"add",dialogVisible:!1,permissionsDialogVisible:!1,rowData:{},queryFrom:{keyWords:""},total:0,loading:!1,dialogTitle:"",windowTitle:"",tableData:[]}},methods:{async getMenuListData(){this.loading=!0,await Object(s["i"])(l["a"].menu.getMenuList,this.queryFrom).then(e=>{this.tableData=e,e.forEach(e=>{e.visible=0===e.visible?"显示":"隐藏",0===e.children.length?e.isShow=!0:(e.isShow=!1,e.children.forEach(e=>{e.visible=0===e.visible?"显示":"隐藏",0===e.children.length?e.isShow=!0:e.isShow=!1}))}),this.loading=!1})},queryApproveInfo(){this.getMenuListData()},menuAdd(){this.dialogVisible=!0,this.dialogTitle="添加菜单",this.operationType="add"},lookHanlder(e){this.dialogVisible=!0,this.dialogTitle="查看菜单",this.operationType="look",this.rowData=e},editHanlder(e){this.dialogVisible=!0,this.dialogTitle="编辑菜单",this.operationType="edit",this.rowData=e},permissionsHanlder(e){this.dialogTitle=e.menuitemTitle+"权限管理",this.permissionsDialogVisible=!0,this.rowData=e},deleteHanlder(e){Object(s["i"])(l["a"].menu.deleteMenuList,{id:e.menuId}).then(e=>{"SUCCESS"===e.status&&this.getMenuListData(),this.$message({type:"删除成功"===e.message?"success":"error",message:e.message})})}},created(){this.getMenuListData()},mounted(){}},x=_,C=Object(p["a"])(x,a,o,!1,null,"23a90cfa",null);t["default"]=C.exports},ef44:function(e,t,i){"use strict";i("93ac")}}]);