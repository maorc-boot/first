(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-509f804f"],{"271a":function(e,t,a){"use strict";var l=a("cb2d"),i=a("e330"),n=a("577e"),s=a("d6d6"),r=URLSearchParams,o=r.prototype,c=i(o.getAll),d=i(o.has),u=new r("a=1");!u.has("a",2)&&u.has("a",void 0)||l(o,"has",(function(e){var t=arguments.length,a=t<2?void 0:arguments[1];if(t&&void 0===a)return d(this,e);var l=c(this,e);s(t,1);var i=n(a),r=0;while(r<l.length)if(l[r++]===i)return!0;return!1}),{enumerable:!0,unsafe:!0})},"2be0":function(e,t,a){},"31b1":function(e,t,a){"use strict";const l="/plan-svc/api/action",i="/plan-svc/action",n="/approve-svc/",s="/element-svc/";t["a"]={channel:{queryIopTemplate:l+"/tactics/tacticsCreate/queryIopTemplate",queryTopics:l+"/topic/queryTopics",getTemplateData:n+"jx/cmpApprovalProcessConf/getApproveConfig",getApproveConfig:n+"cmpApprovalProcessConf/getApproveConfig",getNextApprovers:n+"jx/cmpApproveProcessInstance/getNodeApprover",saveChannel:l+"/tactics/tacticsCreate/save",deleteCustom:l+"/custgroup/custGroupManager/deleteCustom",getMoreMyCustom:l+"/custgroup/custGroupManager/getMoreMyCustom",viewCustGroupDetail:l+"/custgroup/custGroupManager/viewCustGroupDetail",queryMmsTemplates:i+"/mmsTemplates/queryMmsTemplates",deleteMmsTemplateLists:i+"/mmsTemplates/createTactics/deleteMmsTemplateLists",updateMmsTemplate:i+"/mmsTemplates/updateMmsTemplate",smsContentBatchDel:l+"/smsTemplates/smsContentBatchDel",smsContentDel:l+"/smsTemplates/smsContentDel",smsContentEdit:l+"/smsTemplates/smsContentEdit",smsContentSaveOrUpdate:l+"/smsTemplates/smsContentSaveOrUpdate",smsTemplateOffline:l+"/smsTemplates/smsTemplateOffline",smsTemplates:l+"/smsTemplates/smsTemplates",smsTemplateSaveOrUpdate:l+"/smsTemplates/smsTemplateSaveOrUpdate",delPlanExclusivity:s+"mcd-plan-exclusivity/delPlanExclusivity",exportPlanExcluTemplate:s+"mcd-plan-exclusivity/exportPlanExcluTemplate",getPlanExclusivityInfo:s+"mcd-plan-exclusivity/getPlanExclusivityInfo",getPlanExclusivityList:s+"mcd-plan-exclusivity/getPlanExclusivityList",queryPlanDefList:s+"mcd-plan-exclusivity/queryPlanDefList",queryPlanExclusivity:s+"mcd-plan-exclusivity/queryPlanExclusivity",saveOrUpdatePlanExclusivity:s+"mcd-plan-exclusivity/saveOrUpdatePlanExclusivity",savePlanExclusivity:s+"mcd-plan-exclusivity/savePlanExclusivity",updatePlanExclusivity:s+"mcd-plan-exclusivity/updatePlanExclusivity",uploadPlanExcluFile:s+"mcd-plan-exclusivity/uploadPlanExcluFile",approveRecord:s+"api/mcdDimMaterialApprove/jx/approveRecord",getMaterialApprover:s+"api/mcdDimMaterialApprove/jx/getMaterialApprover",commitProcess:"/approve-svc/cmpApproveProcessInstance/commitProcess",subMaterialApprove:s+"api/mcdDimMaterialApprove/jx/subMaterialApprove",queryCommunicationUsers:"/plan-svc/api/channelMaterial/jx/queryCommunicationUsers",queryCommunicationTask:"/approve-svc/jx/cmpApproveProcessInstance/queryCommunicationTask",addCommunicationTask:"/approve-svc/jx/cmpApproveProcessInstance/addCommunicationTask",addCommunicationTaskBatch:"/approve-svc/jx/cmpApproveProcessInstance/addCommunicationTaskBatch",communicationAppTask:"/approve-svc/jx/cmpApproveProcessInstance/communicationAppTask",queryChannelExecInfo:"/plan-svc/mcd/camp/channelExec/queryChannelExecInfo",queryChannelExecList:"/plan-svc/mcd/camp/channelExec/queryChannelExecList",deleteChannelPrompt:"/plan-svc/mcd/chan/prompt/deleteChannelPrompt",queryChannelPrompt:"/plan-svc/mcd/chan/prompt/queryChannelPrompt",saveOrUpdateChannelPrompt:"/plan-svc/mcd/chan/prompt/saveOrUpdateChannelPrompt",uploaChanPromptFile:"/plan-svc/mcd/chan/prompt/uploaChanPromptFile",chanAndPlanMarketingHistory:"/plan-svc/mcd/marketing/history/chanAndPlanMarketingHistory",custContactNumMarketingHistory:"/plan-svc/mcd/marketing/history/custContactNumMarketingHistory",custSuccessRateMarketingHistory:"/plan-svc/mcd/marketing/history/custSuccessRateMarketingHistory",planMarketingHistory:"/plan-svc/mcd/marketing/history/planMarketingHistory",caculateContactUserNum:"/pec-timing/mcd/user/contact/caculateContactUserNum",caculateOrderUserNum:"/pec-timing/mcd/user/contact/caculateOrderUserNum",queryCampScene:"/plan-svc/api/action/tactics/tacticsCreate/jx/queryCampScene"}}},"3b2c":function(e,t,a){},"4e3f":function(e,t,a){"use strict";var l=function(){var e=this,t=e._self._c;return t("div",{staticClass:"query-form-cell-wrap"},[t("el-form",{ref:"form",attrs:{model:e.sizeForm,"label-width":e.labelWidth,"label-position":e.labelPosition,size:"mini"}},e._l(e.list,(function(a,l){return t("el-form-item",{key:l+"list",class:{disabled:a.disabled},attrs:{"label-width":a.labelWidth||e.labelWidth,label:a.label+"："}},[Array.isArray(a.values)?t("div",{staticClass:"fe-d-flex"},[t("div",{staticClass:"radio-box"},[a.dateType?e._e():t("el-radio-group",{attrs:{size:"mini",disabled:a.disabled},on:{change:function(t){return e.radioChange(e.sizeForm.radioModel[l],l)}},model:{value:e.sizeForm.radioModel[l],callback:function(t){e.$set(e.sizeForm.radioModel,l,t)},expression:"sizeForm.radioModel[index]"}},e._l(a.values,(function(a,l){return t("el-radio-button",{key:l+"listValues",attrs:{label:a.value}},[e._v(e._s(a.label)+" ")])})),1)],1),t("div",{staticClass:"date-box"},["date"===a.dateType?t("el-date-picker",{attrs:{type:"date",placeholder:"选择日期","value-format":"yyyy-MM-dd",clearable:!1},on:{change:e.dateChange},model:{value:e.sizeForm.dateModel[l],callback:function(t){e.$set(e.sizeForm.dateModel,l,t)},expression:"sizeForm.dateModel[index]"}}):e._e(),"month"===a.dateType?t("el-date-picker",{attrs:{type:"month",placeholder:"选择月","value-format":"yyyy-MM",clearable:!1},on:{change:e.dateChange},model:{value:e.sizeForm.dateModel[l],callback:function(t){e.$set(e.sizeForm.dateModel,l,t)},expression:"sizeForm.dateModel[index]"}}):e._e(),"daterange"===a.dateType?t("el-date-picker",{attrs:{type:"daterange","value-format":"yyyy-MM-dd","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",clearable:!0},on:{change:e.dateChange},model:{value:e.sizeForm.dateModel[l],callback:function(t){e.$set(e.sizeForm.dateModel,l,t)},expression:"sizeForm.dateModel[index]"}}):e._e(),"datetimerange"===a.dateType?t("el-date-picker",{attrs:{type:"datetimerange","value-format":"yyyy-MM-dd HH:mm:ss","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",clearable:!1},on:{change:e.dateChange},model:{value:e.sizeForm.dateModel[l],callback:function(t){e.$set(e.sizeForm.dateModel,l,t)},expression:"sizeForm.dateModel[index]"}}):e._e()],1)]):t("div",{staticClass:"input-box"},[t("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],attrs:{placeholder:"请输入内容"},on:{change:e.inputChange},model:{value:e.sizeForm.radioModel[l],callback:function(t){e.$set(e.sizeForm.radioModel,l,t)},expression:"sizeForm.radioModel[index]"}})],1)])})),1)],1)},i=[],n={name:"QueryFormCell",props:{labelWidth:{type:String,default:()=>"82px"},labelPosition:{type:String,default:()=>"right"},defaultSizeForm:{type:Object,default:()=>({})},list:{type:Array,default:()=>[{label:"数据时间",values:[{label:"不限",value:"1"}],isDate:!0,dateType:"date"},{label:"接口类型",values:[{label:"不限",value:"不限"},{label:"文件接口",value:"文件接口"}],isDate:!1}]}},data(){return{sizeForm:{dateModel:["2021-05-08","",""],radioModel:["自定义","不限","不限","不限"]}}},watch:{defaultSizeForm:{handler:function(e){this.sizeForm=e},deep:!0,immediate:!0}},methods:{radioChange(e,t){this.sizeForm.dateModel&&this.$set(this.sizeForm.dateModel,t,"date"===this.list[t].dateType?"":["",""]),this.$emit("queryForm",{index:t,value:e,form:this.sizeForm})},dateChange(e){e?(this.$set(this.sizeForm.dateModel,0,e),this.$emit("queryForm",{value:e,form:this.sizeForm})):this.$emit("emptyFrom",this.sizeForm)},inputChange(e){e&&this.$emit("queryForm",{value:e,form:this.sizeForm})}}},s=n,r=(a("a28d"),a("2877")),o=Object(r["a"])(s,l,i,!1,null,null,null);t["a"]=o.exports},"51e3":function(e,t,a){"use strict";a("3b2c")},5494:function(e,t,a){"use strict";var l=a("83ab"),i=a("e330"),n=a("edd0"),s=URLSearchParams.prototype,r=i(s.forEach);l&&!("size"in s)&&n(s,"size",{get:function(){var e=0;return r(this,(function(){e++})),e},configurable:!0,enumerable:!0})},"55b4":function(e,t,a){"use strict";a("2be0")},7834:function(e,t,a){},"88a7":function(e,t,a){"use strict";var l=a("cb2d"),i=a("e330"),n=a("577e"),s=a("d6d6"),r=URLSearchParams,o=r.prototype,c=i(o.append),d=i(o["delete"]),u=i(o.forEach),p=i([].push),m=new r("a=1&a=2&b=3");m["delete"]("a",1),m["delete"]("b",void 0),m+""!=="a=2"&&l(o,"delete",(function(e){var t=arguments.length,a=t<2?void 0:arguments[1];if(t&&void 0===a)return d(this,e);var l=[];u(this,(function(e,t){p(l,{key:t,value:e})})),s(t,1);var i,r=n(e),o=n(a),m=0,h=0,v=!1,y=l.length;while(m<y)i=l[m++],v||i.key===r?(v=!0,d(this,i.key)):h++;while(h<y)i=l[h++],i.key===r&&i.value===o||c(this,i.key,i.value)}),{enumerable:!0,unsafe:!0})},"9acc":function(e,t,a){"use strict";a("7834")},a28d:function(e,t,a){"use strict";a("cddb")},cddb:function(e,t,a){},d6d6:function(e,t,a){"use strict";var l=TypeError;e.exports=function(e,t){if(e<t)throw new l("Not enough arguments");return e}},e180:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e._self._c;return t("div",{staticClass:"template-wrap-content"},[t("div",{staticClass:"top-edit-part"},[t("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],staticStyle:{width:"250px"},attrs:{placeholder:"请输入主产品编码或者主产品名称"},model:{value:e.keyWords,callback:function(t){e.keyWords="string"===typeof t?t.trim():t},expression:"keyWords"}},[t("i",{staticClass:"el-input__icon el-icon-search cursor-pointer",attrs:{slot:"suffix"},on:{click:e.keywordsSearch},slot:"suffix"})]),t("div",[t("input",{ref:"uploadLink",staticStyle:{display:"none"},attrs:{type:"file"},on:{change:e.importFile}}),t("el-button",{attrs:{type:"primary"},on:{click:e.downLoad}},[e._v("下载模板")]),t("el-button",{attrs:{type:"warning",loading:e.uploadLoading},on:{click:e.importFile_}},[e._v("批量导入")]),t("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.openAddAndEditDialog("新建")}}},[e._v("新建产品互斥关系")])],1)],1),t("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],ref:"tTable",staticClass:"template-table",staticStyle:{width:"100%"},attrs:{data:e.tableData,stripe:"",height:600}},[t("el-table-column",{attrs:{type:"index",label:"序号",width:"45",align:"center"}}),t("el-table-column",{attrs:{prop:"planId",label:"主产品编号","header-align":"center",align:"center","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{prop:"planName",label:"主产品名称","header-align":"center",align:"center","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{prop:"exPlanId",label:"互斥产品编号"},scopedSlots:e._u([{key:"default",fn:function(a){return[a.row.exPlanId?e._l(a.row.exPlanId.split(","),(function(l,i){return t("div",{key:i},[a.row.exPlanId.split(",").length>1?t("label",[e._v(" ("+e._s(i+1)+"). ")]):e._e(),e._v(" "+e._s(l)+" ")])})):t("span",[e._v("-")])]}}])}),t("el-table-column",{attrs:{prop:"exPlanName",label:"互斥产品名称"},scopedSlots:e._u([{key:"default",fn:function(a){return[a.row.exPlanName?e._l(a.row.exPlanName.split(","),(function(l,i){return t("div",{key:i},[a.row.exPlanId.split(",").length>1?t("label",[e._v(" ("+e._s(i+1)+"). ")]):e._e(),e._v(" "+e._s(l)+" ")])})):t("span",[e._v("-")])]}}])}),t("el-table-column",{attrs:{label:"操作",width:"120","header-align":"center",align:"center"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-button",{attrs:{type:"text"},on:{click:function(t){return e.openViewDialog(a.row)}}},[e._v("查看")]),t("el-button",{attrs:{type:"text"},on:{click:function(t){return e.openAddAndEditDialog("修改",a.row)}}},[e._v("修改")]),t("el-button",{staticStyle:{color:"red"},attrs:{type:"text"},on:{click:function(t){return e.deleteRow(a.row.planId)}}},[e._v("删除")])]}}])})],1),t("el-pagination",{attrs:{"current-page":e.query.current,"page-sizes":[10,20,30,40],"page-size":e.query.size,"pager-count":5,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}}),t("el-dialog",{attrs:{title:"产品编号【"+e.currentProduct.planId+"】的互斥产品",visible:e.viewDialogVisible,width:"50%","close-on-click-modal":!1,"close-on-press-escape":!1,"destroy-on-close":!0},on:{"update:visible":function(t){e.viewDialogVisible=t}}},[e.currentProduct.planId?t("ViewProduct",{attrs:{currentProduct:e.currentProduct}}):e._e(),t("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{attrs:{type:"primary"},on:{click:function(t){e.viewDialogVisible=!1}}},[e._v("关闭")])],1)],1),e.addAndEditDialogVisible?t("el-dialog",{attrs:{title:e.dialogTitle,visible:e.addAndEditDialogVisible,top:"6vh",width:"70%","close-on-click-modal":!1,"close-on-press-escape":!1,"destroy-on-close":!0},on:{"update:visible":function(t){e.addAndEditDialogVisible=t}}},[t("AddAndEditProduct",{ref:"addAndEditRef",attrs:{currentProduct:e.currentProduct}}),t("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{attrs:{type:"warning"},on:{click:e.clearProductInfo}},[e._v("清空所选")]),t("el-button",{attrs:{type:""},on:{click:function(t){e.addAndEditDialogVisible=!1}}},[e._v("取消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.confirm}},[e._v("确认")])],1)],1):e._e()],1)},i=[],n=(a("88a7"),a("271a"),a("5494"),a("0fea")),s=a("31b1"),r=function(){var e=this,t=e._self._c;return t("div",{staticClass:"dialog-content"},[t("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],ref:"tTable",staticClass:"template-table",staticStyle:{width:"100%"},attrs:{data:e.tableData,stripe:"","max-height":"300px"}},[t("el-table-column",{attrs:{type:"index",label:"序号",width:"45",align:"center"}}),t("el-table-column",{attrs:{prop:"planId",label:"产品编号","header-align":"center",align:"center","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{prop:"planName",label:"产品名称","header-align":"center",align:"center","show-overflow-tooltip":""}})],1),t("el-pagination",{attrs:{"current-page":e.query.current,"page-size":e.query.size,"pager-count":7,layout:"total",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1)},o=[],c={props:["currentProduct"],data(){return{loading:!1,query:{current:1,size:20,planId:null},total:0,tableData:[]}},watch:{"currentProduct.planId"(){this.query.planId=this.currentProduct.planId,this.query.current=1,this.getTableData()}},methods:{async getTableData(){try{this.loading=!0;const{planExcluList:e}=await Object(n["i"])(s["a"].channel.getPlanExclusivityInfo,this.query);this.tableData=e,this.total=e.length}catch(e){}finally{this.loading=!1}},handleSizeChange(e){this.query.size=e,this.query.current=1,this.getTableData()},handleCurrentChange(e){this.query.current=e,this.getTableData()}},mounted(){this.query.planId=this.currentProduct.planId,this.getTableData()}},d=c,u=(a("51e3"),a("2877")),p=Object(u["a"])(d,r,o,!1,null,"01cba835",null),m=p.exports,h=function(){var e=this,t=e._self._c;return t("div",{staticClass:"dialog-content"},[t("div",{staticClass:"left"},[t("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],staticStyle:{width:"250px","margin-right":"20px"},attrs:{placeholder:"请输入主产品编码或者主产品名称"},model:{value:e.keyWords,callback:function(t){e.keyWords="string"===typeof t?t.trim():t},expression:"keyWords"}},[t("i",{staticClass:"el-input__icon el-icon-search cursor-pointer",attrs:{slot:"suffix"},on:{click:e.keywordsSearch},slot:"suffix"})]),t("el-button",{attrs:{type:"primary"},on:{click:function(t){e.showMore=!e.showMore}}},[e._v(" "+e._s(e.showMore?"收起":"更多筛选")+" "),t("i",{class:e.showMore?"el-icon-arrow-up":"el-icon-arrow-down"})]),t("QueryFormCell",{directives:[{name:"show",rawName:"v-show",value:e.showMore,expression:"showMore"}],attrs:{list:e.QueryList,defaultSizeForm:e.defaultSizeForm},on:{queryForm:e.queryForm}}),t("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],ref:"tTable",staticClass:"template-table customTable",staticStyle:{width:"100%"},attrs:{data:e.tableData,stripe:"",height:"400px"}},[t("el-table-column",{attrs:{label:"序号",type:"index","header-align":"center",align:"center",width:"60"}}),t("el-table-column",{attrs:{prop:"planName",label:"产品名称","header-align":"center"}}),t("el-table-column",{attrs:{prop:"planId",label:"产品编号","header-align":"center"}}),t("el-table-column",{attrs:{label:"产品类别","header-align":"center",align:"center"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("span",[e._v(e._s((e.QueryList[0].values.find(e=>e.value==a.row.planType)||{}).label))])]}}])}),t("el-table-column",{attrs:{label:"产品类型","header-align":"center",align:"center"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("span",[e._v(e._s((e.QueryList[1].values.find(e=>e.value==a.row.planSrvType)||{}).label))])]}}])}),t("el-table-column",{attrs:{prop:"planStartDate",label:"生效时间","header-align":"center",align:"center",width:"90"}}),t("el-table-column",{attrs:{prop:"planEndDate",label:"失效时间","header-align":"center",align:"center",width:"90"}}),t("el-table-column",{attrs:{prop:"isUsed",label:"是否匹配","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{label:"操作",width:"80",fixed:"right","header-align":"center",align:"center"},scopedSlots:e._u([{key:"default",fn:function(a){return[e.mainProductOperation?[1==a.row.isFlag?t("span",[t("el-button",{staticClass:"operation-btn",attrs:{type:"text",size:"mini"},on:{click:function(t){return e.add(a.row)}}},[e._v("选择")])],1):t("span",[t("span",{staticClass:"operation-text"},[e._v("已存在")])])]:[e.productList.some(e=>e.planId===a.row.planId)||a.row.planId===e.mainProduct.planId?t("span",{staticClass:"operation-text"},[e._v("已添加")]):t("el-button",{staticClass:"operation-btn",attrs:{type:"text",size:"mini"},on:{click:function(t){return e.add(a.row)}}},[e._v("添加")])]]}}])})],1),t("el-pagination",{attrs:{"current-page":e.query.current,"page-sizes":[10,20,30,40],"page-size":e.query.size,"pager-count":5,layout:"total, prev, pager, next",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1),t("div",{class:["right",e.mainProductOperation?"my-disabled":""]},[t("el-tabs",{model:{value:e.activeName,callback:function(t){e.activeName=t},expression:"activeName"}},[t("el-tab-pane",{attrs:{label:"主产品",name:"main"}},[t("div",{staticClass:"main-product-content"},[e.mainProduct.planId?t("span",{staticClass:"dsc"},[e._v(e._s(e.mainProduct.planName))]):t("div",{staticClass:"tips"},[e._v("请选择主产品")]),!e.currentProduct.planId&&e.mainProduct.planId?t("span",{staticClass:"choose-btn",on:{click:function(t){return e.clearProductInfo(!0)}}},[e._v("重新选择")]):e._e()]),e.mainProduct.planId&&!e.currentProduct.planId?t("div",{staticClass:"tips2"},[t("i",{staticClass:"el-icon-warning-outline"}),t("span",[e._v("仅可选择一个主产品")])]):e._e()]),t("el-tab-pane",{attrs:{label:"自定义互斥产品",name:"custom"}},[t("div",{staticClass:"product-content margintop"},[e.productList.length?t("div",{staticClass:"tips2"},[e._v("当前互斥产品数 "+e._s(e.productList.length)+"/20")]):t("div",{staticClass:"tips"},[e._v(" 请添加互斥产品 ")]),e._l(e.productList,(function(a,l){return t("div",{key:a.planId,staticClass:"list"},[t("span",[e._v(e._s(a.planName))]),t("i",{staticClass:"el-icon-close",attrs:{title:"移除互斥产品"},on:{click:function(t){return e.remove(l)}}})])}))],2)]),t("el-tab-pane",{attrs:{label:"默认互斥",name:"default"}},[t("div",{staticClass:"product-content"},e._l(e.defaultProductList,(function(a){return t("div",{key:a.planId,staticClass:"list"},[t("span",[e._v(e._s(a.planName))])])})),0)])],1)],1)])},v=[],y=(a("14d9"),a("4e3f")),g={props:{currentProduct:{type:Object,default:()=>{}}},components:{QueryFormCell:y["a"]},data(){return{loading:!1,keyWords:"",query:{keyWords:"",current:1,size:10,planTypeId:"",planSrvType:""},startPage:!1,showMore:!1,QueryList:[{label:"产品类别",values:[{label:"不限",value:""},{label:"流量",value:"1"},{label:"新增",value:"2"},{label:"终端",value:"3"},{label:"两网",value:"4"},{label:"宽带",value:"5"},{label:"存量",value:"6"},{label:"政企",value:"7"},{label:"手机应用",value:"10"},{label:"个人基本策划",value:"101"},{label:"普通增值策划",value:"102"},{label:"虚拟类政策",value:"999"},{label:"其他",value:"9"}],isDate:!1},{label:"产品类型",values:[{label:"不限",value:""},{label:"单产品",value:"1"},{label:"政策",value:"2"},{label:"综合",value:"6"}],isDate:!1}],defaultSizeForm:{radioModel:["",""]},total:0,tableData:[],activeName:"main",mainProduct:{},productList:[],defaultProductList:[],mainProductOperation:!0}},methods:{async getTableData(){try{this.loading=!0,this.startPage&&(this.query.current=1);let e=s["a"].channel.queryPlanDefList;const{records:t,total:a}=await Object(n["i"])(e,this.query);this.tableData=t,this.total=a}catch(e){}finally{this.loading=!1,this.startPage=!1}},keywordsSearch(){this.query.current=1,this.query.keyWords=this.keyWords,this.getTableData()},queryForm(e){switch(e.index){case 0:this.query.planTypeId=e.value;break;case 1:this.query.planSrvType=e.value;break}this.query.current=1,this.getTableData()},async add(e){if(this.mainProductOperation)this.mainProduct=e,this.mainProductOperation=!1,setTimeout(()=>{this.activeName="custom"},500),await this.getProductListByPlanId(e.planId,!1);else{if(this.activeName="custom",20===this.productList.length)return this.$message({message:"最多只能添加20个互斥产品",type:"warning"}),!1;this.productList.push(e)}},async getProductListByPlanId(e,t=!0){if(t){const t=await Object(n["i"])(s["a"].channel.getPlanExclusivityInfo,{planId:e,sourceType:0});this.productList=t.planExcluList}else this.productList=[];const a=await Object(n["i"])(s["a"].channel.getPlanExclusivityInfo,{planId:e,sourceType:1});this.defaultProductList=a.planExcluList,this.$forceUpdate()},remove(e){this.productList.splice(e,1)},handleSizeChange(e){this.query.current=1,this.query.size=e,this.getTableData()},handleCurrentChange(e){this.query.current=e,this.getTableData()},clearProductInfo(e){e?(this.mainProduct={},this.mainProductOperation=!0,this.productList=[],this.defaultProductList=[],this.activeName="main"):(this.productList=[],this.activeName="custom")},async confirm(){if(void 0===this.mainProduct.planId)return this.$message({message:"请添加主产品",type:"warning"}),Promise.reject(!1);if(0===this.productList.length)return this.$message({message:"请添加互斥产品",type:"warning"}),Promise.reject(!1);let e={exPlanId:this.productList.map(e=>e.planId).join(","),exPlanName:this.productList.map(e=>e.planName).join(","),planGroupId:this.mainProduct.planGroupId,planId:this.mainProduct.planId,planName:this.mainProduct.planName,sourceType:0,type:1};return this.currentProduct.planId?Object(n["i"])(s["a"].channel.updatePlanExclusivity,e):Object(n["i"])(s["a"].channel.savePlanExclusivity,e)}},mounted(){this.getTableData(),this.currentProduct.planId&&(this.activeName="custom",this.mainProduct={...this.currentProduct},this.mainProductOperation=!1,this.getProductListByPlanId(this.currentProduct.planId))}},b=g,f=(a("9acc"),Object(u["a"])(b,h,v,!1,null,"397f2eee",null)),P=f.exports,x={name:"MutualExclusionProduct",components:{ViewProduct:m,AddAndEditProduct:P},data(){return{loading:!1,uploadLoading:!1,keyWords:"",query:{keyWords:"",current:1,size:10},startPage:!1,total:0,tableData:[],viewDialogVisible:!1,addAndEditDialogVisible:!1,dialogTitle:"",currentProduct:{}}},methods:{async getTableData(){try{this.loading=!0,this.startPage&&(this.query.current=1);const{records:e,total:t}=await Object(n["i"])(s["a"].channel.getPlanExclusivityList,this.query);this.tableData=e,this.total=t}catch(e){}finally{this.loading=!1,this.startPage=!1}},keywordsSearch(){this.query.current=1,this.query.keyWords=this.keyWords,this.getTableData()},deleteRow(e){this.$confirm("此操作将永久删除该产品互斥关系, 是否继续?","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then(async()=>{try{await Object(n["i"])(s["a"].channel.delPlanExclusivity,{planId:e}),this.$message({type:"success",message:"删除成功!"}),this.getTableData()}catch(t){}finally{this.loading=!1}})},openViewDialog(e){this.currentProduct=e,this.viewDialogVisible=!0},openAddAndEditDialog(e,t){this.dialogTitle="新建"===e?e+"产品互斥关系":`修改产品编号【${t.planId}】的产品互斥关系`,this.currentProduct={...t}||{},this.addAndEditDialogVisible=!0},handleSizeChange(e){this.query.current=1,this.query.size=e,this.getTableData()},handleCurrentChange(e){this.query.current=e,this.getTableData()},downLoad(){Object(n["b"])(s["a"].channel.exportPlanExcluTemplate).then(({data:e})=>{const t=new Blob([e],{type:e.type}),a="互斥产品模板.xls",l=document.createElement("a");l.download=a,l.style.display="none",l.href=URL.createObjectURL(t),document.body.appendChild(l),l.click(),URL.revokeObjectURL(l.href),document.body.removeChild(l)})},importFile_(){this.$refs.uploadLink.files.length>0&&(this.$refs.uploadLink.value=null),this.$refs.uploadLink.click()},importFile(e){let t=e.target.files[0];if(!t)return;let a=new FormData;a.append("uploadPlanExcluFile",t),this.uploadLoading=!0,Object(n["j"])(s["a"].channel.uploadPlanExcluFile,a).then(e=>{this.uploadLoading=!1,!1!==e.data?(this.$message({message:"导入成功",type:"success"}),this.getTableData()):this.$message.error("导入失败")}).catch(e=>{this.uploadLoading=!1})},clearProductInfo(){this.$refs["addAndEditRef"].clearProductInfo(!this.currentProduct.planId)},async confirm(){try{await this.$refs["addAndEditRef"].confirm(),this.addAndEditDialogVisible=!1,this.getTableData()}catch(e){}}},mounted(){this.getTableData()}},C=x,w=(a("55b4"),Object(u["a"])(C,l,i,!1,null,"2567cb71",null));t["default"]=w.exports},edd0:function(e,t,a){"use strict";var l=a("13d2"),i=a("9bf2");e.exports=function(e,t,a){return a.get&&l(a.get,t,{getter:!0}),a.set&&l(a.set,t,{setter:!0}),i.f(e,t,a)}}}]);