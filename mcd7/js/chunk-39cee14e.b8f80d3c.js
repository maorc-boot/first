(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-39cee14e"],{"08b8":function(e,t,a){"use strict";a.r(t);var s=function(){var e=this,t=e._self._c;return t("div",{staticClass:"schedule-manage-warp fe-m-24 fe-p-24 bg"},[t("div",{staticClass:"search_box"},[t("el-select",{attrs:{placeholder:"--执行状态--"},on:{change:function(t){return e.queryApproveInfo()}},model:{value:e.queryFrom.exeStatus,callback:function(t){e.$set(e.queryFrom,"exeStatus",t)},expression:"queryFrom.exeStatus"}},e._l(e.options,(function(e){return t("el-option",{key:e.value,attrs:{label:e.value,value:e.id}})})),1),t("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],staticClass:"input-search",attrs:{placeholder:"请输入调度名称查询"},model:{value:e.queryFrom.keyWords,callback:function(t){e.$set(e.queryFrom,"keyWords","string"===typeof t?t.trim():t)},expression:"queryFrom.keyWords"}},[t("i",{staticClass:"el-input__icon el-icon-search cursor-pointer",attrs:{slot:"suffix"},on:{click:function(t){return e.queryListByKeyword()}},slot:"suffix"})]),t("div",{staticClass:"btn_box"},[t("el-button",{attrs:{type:"success",size:"small"},on:{click:e.addHandle}},[e._v("新建")])],1)],1),t("div",[t("el-table",{staticStyle:{width:"100%"},attrs:{data:e.tableData}},[t("el-table-column",{attrs:{type:"index",label:"序号",width:"50"}}),t("el-table-column",{attrs:{prop:"taskExeName",label:"调度名称",width:"180"}}),t("el-table-column",{attrs:{prop:"paramData",label:"参数"}}),t("el-table-column",{attrs:{prop:"taskExeTime",label:"QUARZT表达式"}}),t("el-table-column",{attrs:{prop:"exeType",label:"任务类型"}}),t("el-table-column",{attrs:{prop:"exeStatus",label:"执行状态"}}),t("el-table-column",{attrs:{label:"操作",width:"350px"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-link",{staticClass:"text-sm mr-16",attrs:{type:"primary",underline:!1},on:{click:function(t){return e.editHanlder(a.row)}}},[e._v("修改 ")]),t("el-link",{directives:[{name:"show",rawName:"v-show",value:"停止"==a.row.exeStatus,expression:"scope.row.exeStatus == '停止'"}],staticClass:"text-sm mr-16",attrs:{type:"primary",underline:!1},on:{click:function(t){return e.startHanlder(a.row)}}},[e._v("启动 ")]),t("el-link",{directives:[{name:"show",rawName:"v-show",value:"启动"==a.row.exeStatus,expression:"scope.row.exeStatus == '启动'"}],staticClass:"text-sm mr-16",attrs:{type:"primary",underline:!1},on:{click:function(t){return e.stopHanlder(a.row)}}},[e._v("停止 ")]),t("el-link",{staticClass:"text-sm mr-16",attrs:{type:"primary",underline:!1},on:{click:function(t){return e.nowStartScheduling(a.row)}}},[e._v("立即执行 ")]),t("el-link",{staticClass:"text-sm mr-16",attrs:{type:"primary",underline:!1},on:{click:function(t){return e.detailHanlder(a.row)}}},[e._v("调用明细 ")]),t("el-popconfirm",{attrs:{placement:"top",icon:"el-icon-warning",iconColor:"#FAAD14",title:"确定删除？","cancel-button-type":"Primary"},on:{confirm:function(t){return e.deleteHanlder(a.row)}}},[t("el-link",{staticClass:"cursor-pointer text-sm",attrs:{slot:"reference",type:"danger",underline:!1},slot:"reference"},[e._v(" 删除")])],1)]}}])})],1)],1),t("div",{staticClass:"pager"},[t("el-pagination",{attrs:{"current-page":e.pageData.currentPage,"page-sizes":e.pageData.pageSizeList,"page-size":e.pageData.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:e.pageData.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1),e.rowDialogVisible?t("schedul-dialog",{attrs:{dialogVisible:e.rowDialogVisible,dialogTitle:e.schedulDialogTitle,operationType:e.operationType,rowData:e.rowData},on:{"update:dialogVisible":function(t){e.rowDialogVisible=t},"update:dialog-visible":function(t){e.rowDialogVisible=t},getSchedulingList:e.getSchedulingList}}):e._e(),e.detailVisible?t("details-dialog",{attrs:{dialogVisible:e.detailVisible,dialogTitle:"调用明细",operationType:e.operationType,rowData:e.rowData},on:{"update:dialogVisible":function(t){e.detailVisible=t},"update:dialog-visible":function(t){e.detailVisible=t}}}):e._e()],1)},i=[],o=a("0fea"),r=a("a454"),l=function(){var e=this,t=e._self._c;return t("div",[t("el-dialog",{attrs:{title:e.dialogTitle,visible:e.dialogVisible,"before-close":e.closeHandler,width:"60%","append-to-body":""}},[t("div",{staticClass:"schedul-content"},[t("el-form",{ref:"schedulFrom",staticStyle:{width:"500px"},attrs:{model:e.formData,"label-width":"100px",rules:e.rules}},[t("el-form-item",{attrs:{label:"调度名称",prop:"taskExeName"}},[t("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],attrs:{maxlength:"16"},model:{value:e.formData.taskExeName,callback:function(t){e.$set(e.formData,"taskExeName",t)},expression:"formData.taskExeName"}})],1),t("el-form-item",{attrs:{label:"调度URL",prop:"taskUrl"}},[t("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],model:{value:e.formData.taskUrl,callback:function(t){e.$set(e.formData,"taskUrl",t)},expression:"formData.taskUrl"}})],1),t("el-form-item",{attrs:{label:"所属系统",prop:"sysId"}},[t("el-select",{attrs:{filterable:"",placeholder:"请选择"},model:{value:e.formData.sysId,callback:function(t){e.$set(e.formData,"sysId",t)},expression:"formData.sysId"}},e._l(e.sysIdArr,(function(a,s){return t("el-option",{key:s,attrs:{label:a.dicProfile},model:{value:a.dicProfile,callback:function(t){e.$set(a,"dicProfile",t)},expression:"item.dicProfile"}})})),1)],1),t("el-form-item",{attrs:{label:"调度参数",prop:"paramData"}},[t("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],attrs:{type:"textarea",maxlength:"500",placeholder:"{'key01': 'value1','key02':'value02','key03':'value03'}"},model:{value:e.formData.paramData,callback:function(t){e.$set(e.formData,"paramData",t)},expression:"formData.paramData"}})],1)],1),t("div",[t("el-table",{staticStyle:{width:"100%"},attrs:{data:e.dates}},[t("el-table-column",{attrs:{prop:"roleName",align:"center"},scopedSlots:e._u([{key:"default",fn:function(a){return[0===a.$index?t("span",[e._v(e._s(e.firstRow.roleName))]):t("span",[e._v(e._s(a.row.roleName))])]}}])}),t("el-table-column",{attrs:{align:"center",prop:"second",label:"秒"},scopedSlots:e._u([{key:"default",fn:function(a){return[0===a.$index?t("el-input",{model:{value:e.firstRow.second,callback:function(t){e.$set(e.firstRow,"second",t)},expression:"firstRow.second"}}):t("el-input",{attrs:{disabled:""},model:{value:a.row.second,callback:function(t){e.$set(a.row,"second",t)},expression:"scope.row.second"}})]}}])}),t("el-table-column",{attrs:{align:"center",prop:"minute",label:"分"},scopedSlots:e._u([{key:"default",fn:function(a){return[0===a.$index?t("el-input",{model:{value:e.firstRow.minute,callback:function(t){e.$set(e.firstRow,"minute",t)},expression:"firstRow.minute"}}):t("el-input",{attrs:{disabled:""},model:{value:a.row.minute,callback:function(t){e.$set(a.row,"minute",t)},expression:"scope.row.minute"}})]}}])}),t("el-table-column",{attrs:{align:"center",prop:"hours",label:"时"},scopedSlots:e._u([{key:"default",fn:function(a){return[0===a.$index?t("el-input",{model:{value:e.firstRow.hours,callback:function(t){e.$set(e.firstRow,"hours",t)},expression:"firstRow.hours"}}):t("el-input",{attrs:{disabled:""},model:{value:a.row.hours,callback:function(t){e.$set(a.row,"hours",t)},expression:"scope.row.hours"}})]}}])}),t("el-table-column",{attrs:{align:"center",prop:"day",label:"日"},scopedSlots:e._u([{key:"default",fn:function(a){return[0===a.$index?t("el-input",{model:{value:e.firstRow.day,callback:function(t){e.$set(e.firstRow,"day",t)},expression:"firstRow.day"}}):t("el-input",{attrs:{disabled:""},model:{value:a.row.day,callback:function(t){e.$set(a.row,"day",t)},expression:"scope.row.day"}})]}}])}),t("el-table-column",{attrs:{prop:"month",label:"月"},scopedSlots:e._u([{key:"default",fn:function(a){return[0===a.$index?t("el-input",{model:{value:e.firstRow.month,callback:function(t){e.$set(e.firstRow,"month",t)},expression:"firstRow.month"}}):t("el-input",{attrs:{disabled:""},model:{value:a.row.month,callback:function(t){e.$set(a.row,"month",t)},expression:"scope.row.month"}})]}}])}),t("el-table-column",{attrs:{align:"center",prop:"week",label:"周"},scopedSlots:e._u([{key:"default",fn:function(a){return[0===a.$index?t("el-input",{model:{value:e.firstRow.week,callback:function(t){e.$set(e.firstRow,"week",t)},expression:"firstRow.week"}}):t("el-input",{attrs:{disabled:""},model:{value:a.row.week,callback:function(t){e.$set(a.row,"week",t)},expression:"scope.row.week"}})]}}])}),t("el-table-column",{attrs:{align:"center",width:"80px"},scopedSlots:e._u([{key:"default",fn:function(a){return[0===a.$index?t("span"):t("span",{staticClass:"use",on:{click:function(t){return e.useThat(a)}}},[e._v("使用")])]}}])})],1)],1)],1),"look"!=this.operationType?t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:e.closeHandler}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.submit}},[e._v("保 存")])],1):e._e()])],1)},n=[],c=a("f9ae"),u={name:"SchedulDialog",props:{dialogTitle:{type:String},dialogVisible:{type:Boolean,default:!1},operationType:{type:String,default:()=>"add"},rowData:{type:Object}},data(){return{formData:{},sysIdArr:[],rules:{taskExeName:[{required:!0,message:"请输入调度名称",trigger:"blur"}],taskUrl:[{required:!0,message:"请输入调度URL",trigger:"blur"}],paramData:[{required:!0,message:"请输入调度参数",trigger:"blur"}],sysId:[{required:!0,message:"请选择所属系统",trigger:"blur"}]},dates:[{roleName:"调度时间规则*",second:0,minute:0,hours:"",day:"",month:"",week:""},{roleName:"每4个小时执行一次",second:0,minute:0,hours:"3",day:"*",month:"*",week:"?"},{roleName:"每月的13日的3点开始执行",second:0,minute:0,hours:"3",day:"13",month:"*",week:"?"},{roleName:"每20分钟执行一次",second:0,minute:"0/20",hours:"*",day:"*",month:"*",week:"?"},{roleName:"每30秒执行一次",second:"0/30",minute:"*",hours:"*",day:"*",month:"*",week:"?"},{roleName:"每天23点执行一次",second:0,minute:0,hours:"23",day:"*",month:"*",week:"?"},{roleName:"每天的6,12,19,23点的25分",second:0,minute:25,hours:"6,12,19,23",day:"*",month:"*",week:"?"}],firstRow:{roleName:"调度时间规则",second:"",minute:"",hours:"",day:"",month:"",week:""}}},methods:{closeHandler(){this.$emit("update:dialogVisible",!1)},submit:Object(c["a"])((function(){"add"===this.operationType?this.addSchedulingInfo():this.editSchedulingInfo()})),getSysIdArr(){Object(o["i"])(r["a"].schedManage.getSysIdArr).then(e=>{this.sysIdArr=e})},addSchedulingInfo(){let e=Object.assign(this.formData,this.firstRow);this.$refs["schedulFrom"].validate(async t=>{t?(this.formData.taskExeId=this.rowData.taskExeId,await Object(o["i"])(r["a"].schedManage.addSchedInfo,e).then(e=>{this.$message.success("新增成功"),this.$emit("getSchedulingList"),this.closeHandler()})):this.$alert("请填写必填项","校验失败",{confirmButtonText:"确定",customClass:"errorPrompt"})})},editSchedulingInfo(){let e=Object.assign(this.formData,this.firstRow);e.taskExeTime=e.second+" "+e.minute+" "+e.hours+" "+e.day+" "+e.month+" "+e.week,this.$refs["schedulFrom"].validate(async t=>{t?(this.formData.taskExeId=this.rowData.taskExeId,await Object(o["i"])(r["a"].schedManage.editSchedInfo,e).then(e=>{this.$message.success("编辑成功"),this.$emit("getSchedulingList"),this.closeHandler()})):this.$alert("请填写必填项","校验失败",{confirmButtonText:"确定",customClass:"errorPrompt"})})},getView(){Object(o["i"])(r["a"].schedManage.lookSchedulingInfo,{id:this.rowData.taskExeId}).then(e=>{this.formData=e,this.firstRow=e,this.firstRow.roleName="调度时间规则"})},useThat(e){this.firstRow.second=e.row.second,this.firstRow.minute=e.row.minute,this.firstRow.hours=e.row.hours,this.firstRow.day=e.row.day,this.firstRow.month=e.row.month,this.firstRow.week=e.row.week,this.firstRow.taskExeTime=e.row.second+" "+e.row.minute+" "+e.row.hours+" "+e.row.day+" "+e.row.month+" "+e.row.week}},created(){this.getSysIdArr(),"add"!==this.operationType&&this.getView()}},d=u,p=(a("cb54"),a("2877")),m=Object(p["a"])(d,l,n,!1,null,"0ae19c98",null),h=m.exports,f=function(){var e=this,t=e._self._c;return t("div",[t("el-dialog",{attrs:{title:e.dialogTitle,visible:e.dialogVisible,"before-close":e.closeHandler,width:"50%","append-to-body":""}},[t("div",{staticClass:"details-content"},[t("div",{staticClass:"details-header"},[t("div",{staticClass:"header-item"},[t("span",[e._v("调用名称：")]),t("span",[e._v(e._s(e.rowData.taskExeName))])]),t("div",{staticClass:"header-item"},[t("span",[e._v("调度时间：")]),t("span",[e._v(e._s(e.rowData.taskExeTime))])]),t("div",{staticClass:"header-item"},[t("span",[e._v("调度参数：")]),t("span",[e._v(e._s(e.rowData.paramData))])])]),t("div",{staticClass:"details-search"},[t("el-form",{ref:"searchForm",attrs:{model:e.search,"label-position":"right",inline:""}},[t("el-date-picker",{attrs:{type:"daterange","value-format":"yyyy-MM-dd","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",clearable:!1},on:{change:e.queryList},model:{value:e.queryFrom.rangeDate,callback:function(t){e.$set(e.queryFrom,"rangeDate",t)},expression:"queryFrom.rangeDate"}}),t("el-select",{staticStyle:{"margin-left":"10px"},attrs:{placeholder:"执行方式"},on:{change:e.queryList},model:{value:e.queryFrom.exeType,callback:function(t){e.$set(e.queryFrom,"exeType",t)},expression:"queryFrom.exeType"}},e._l(e.exeTypeOptions,(function(e){return t("el-option",{key:e.value,attrs:{label:e.value,value:e.id}})})),1),t("el-form-item",{staticStyle:{"margin-left":"20px"},attrs:{label:"",prop:"fuzzy"}},[t("el-input",{staticClass:"input-search fe-input-icon-pl",attrs:{placeholder:"执行人"},model:{value:e.queryFrom.keyWords,callback:function(t){e.$set(e.queryFrom,"keyWords",t)},expression:"queryFrom.keyWords"}},[t("i",{staticClass:"el-input__icon el-icon-search cursor-pointer",attrs:{slot:"suffix"},on:{click:e.queryList},slot:"suffix"})])],1)],1)],1),t("div",{staticClass:"details-table"},[t("el-table",{staticStyle:{width:"100%"},attrs:{data:e.tableData}},[t("el-table-column",{attrs:{prop:"startTime",label:"执行时间","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{prop:"exeType",label:"执行方式","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{prop:"status",label:"响应状态","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{prop:"returnMsg",label:"响应信息","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{prop:"userId",label:"执行人","show-overflow-tooltip":""}})],1)],1),t("div",{staticClass:"pagination"},[t("el-pagination",{attrs:{"current-page":e.queryFrom.current,"page-size":e.queryFrom.size,layout:"total, prev, pager, next, jumper",total:e.total},on:{"current-change":e.handleCurrentChange}})],1)]),"look"!=this.operationType?t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:e.closeHandler}},[e._v("取 消")])],1):e._e()])],1)},g=[],y={name:"DetailsDialog",props:{dialogTitle:{type:String},dialogVisible:{type:Boolean,default:!1},operationType:{type:String,default:()=>"use"},rowData:{type:Object}},data(){return{searchForm:{},search:{person:""},sizeForm:{radioModel:["",""]},exeTypeOptions:[{id:"0",value:"立即执行"},{id:"1",value:"延期执行"},{id:"2",value:"定时执行"}],queryFrom:{keyWords:"",exeType:"",rangeDate:[],size:10,current:1},total:0,tableData:[]}},methods:{closeHandler(){this.$emit("update:dialogVisible",!1)},getView(){let e=Object.assign({taskExtId:this.rowData.taskExeId},this.queryFrom);Object(o["i"])(r["a"].schedManage.getSchedulingDetail,e).then(e=>{e.records.forEach(e=>{"0"===e.exeType?e.exeType="立即执行":"1"===e.exeType?e.exeType="延期执行":"2"===e.exeType&&(e.exeType="定时执行")}),this.tableData=e.records,this.total=e.total})},queryList(){this.getView()},handleCurrentChange(e){this.queryFrom.current=e,this.getView()}},created(){this.getView()}},w=y,b=(a("7505"),Object(p["a"])(w,f,g,!1,null,"7c0b90d7",null)),k=b.exports,x={components:{SchedulDialog:h,DetailsDialog:k},data(){return{search:{fuzzy:""},sizeForm:{radioModel:["",""]},options:[{id:"",value:"全部"},{id:1,value:"启用"},{id:0,value:"停止"}],tableData:[],queryFrom:{keyWords:"",exeStatus:""},pageData:{currentPage:1,pageSize:10,pageSizeList:[10,20,30,40],total:0},total:0,rowDialogVisible:!1,schedulDialogTitle:"",operationType:"add",detailVisible:!1,rowData:{}}},methods:{initializationFrom(){this.pageData.current=1},queryApproveInfo(){this.getSchedulingList()},queryListByKeyword(){this.pageData.currentPage=1,this.queryApproveInfo()},async getSchedulingList(){await Object(o["i"])(r["a"].schedManage.getSchedulingList,{keyWords:this.queryFrom.keyWords,current:this.pageData.currentPage,size:this.pageData.pageSize,exeStatus:this.queryFrom.exeStatus}).then(e=>{e.records.forEach(e=>{e.exeStatus=0===e.exeStatus?"停止":"启动",e.exeType=1===e.exeType?"延迟执行":"按执行时间执行"}),this.tableData=e.records,this.pageData.total=e.total,this.initializationFrom()})},editHanlder(e){this.rowDialogVisible=!0,this.operationType="edit",this.schedulDialogTitle="编辑调度",this.rowData=e},deleteHanlder(e){Object(o["i"])(r["a"].schedManage.deleteTaskExeList,{id:e.taskExeId}).then(e=>{this.$message({type:"success",message:e.message}),this.getSchedulingList()})},statusChange(e){Object(o["i"])(r["a"].schedManage.changeSchedulingStatus,{id:e.taskExeId}).then(e=>{this.$message.success("操作成功"),this.getSchedulingList()})},startHanlder(e){this.statusChange(e)},stopHanlder(e){this.statusChange(e)},nowStartScheduling(e){Object(o["i"])(r["a"].schedManage.nowStartScheduling,{id:e.taskExeId}).then(e=>{this.$message.success(e.res),this.getSchedulingList()})},addHandle(){this.rowDialogVisible=!0,this.operationType="add",this.schedulDialogTitle="新增调度"},detailHanlder(e){this.detailVisible=!0,this.schedulDialogTitle="调用明细",this.rowData=e},handleSizeChange(e){this.pageData.pageSize=e,this.queryApproveInfo()},handleCurrentChange(e){this.pageData.currentPage=e,this.queryApproveInfo()}},created(){this.getSchedulingList()}},v=x,D=Object(p["a"])(v,s,i,!1,null,"6a52780e",null);t["default"]=D.exports},7505:function(e,t,a){"use strict";a("90cf")},"90cf":function(e,t,a){},cb54:function(e,t,a){"use strict";a("e82c")},e82c:function(e,t,a){}}]);