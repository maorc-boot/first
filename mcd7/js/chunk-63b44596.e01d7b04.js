(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-63b44596"],{"17c2":function(e,a,t){},"61e93":function(e,a,t){"use strict";t.r(a);var s=function(){var e=this,a=e._self._c;return a("div",{staticClass:"avoid-bother-customer-wrap fe-m-24 fe-p-24 bg EventManage"},[a("div",{staticClass:"search-container"},[a("el-input",{attrs:{placeholder:"输入事件名称/编码"},model:{value:e.queryParams.keyWords,callback:function(a){e.$set(e.queryParams,"keyWords",a)},expression:"queryParams.keyWords"}}),a("el-button",{attrs:{type:"primary"},on:{click:e.onSearch}},[e._v("搜索")]),a("br"),a("span",{staticClass:"rule-type"},[e._v("规则状态：")]),a("el-radio-group",{on:{change:e.changeStatus},model:{value:e.queryParams.eventStatus,callback:function(a){e.$set(e.queryParams,"eventStatus",a)},expression:"queryParams.eventStatus"}},e._l(e.statusOptions,(function(t){return a("el-radio-button",{key:t.value,attrs:{label:t.value}},[e._v(e._s(t.name))])})),1),a("el-button",{attrs:{type:"success"},on:{click:e.onNewRule}},[e._v("新增规则")])],1),a("div",{staticClass:"main-container"},[a("div",{staticClass:"type-container"},[a("el-input",{attrs:{placeholder:"输入关键字模糊查询"},model:{value:e.keyWord,callback:function(a){e.keyWord=a},expression:"keyWord"}},[a("el-button",{attrs:{slot:"append",icon:"el-icon-search"},on:{click:e.searchName},slot:"append"})],1),e.isAlive?a("el-tree",{ref:"tree",attrs:{accordion:"",data:e.dataTree,"node-key":"categoryId",props:e.defaultProps,"highlight-current":!0,"default-expanded-keys":e.expandedKeys},on:{"node-expand":e.handleNodeExpand,"node-click":e.handleNodeClick}}):e._e()],1),a("div",{staticClass:"context-container"},[a("div",[a("el-table",{staticStyle:{width:"100%"},attrs:{data:e.tableData}},[a("el-table-column",{attrs:{prop:"eventCode",label:"事件编码","show-overflow-tooltip":!0}}),a("el-table-column",{attrs:{prop:"eventName",label:"事件名称"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-link",{staticClass:"text-sm mr-16",attrs:{type:"primary",underline:!1},on:{click:function(a){return e.openItem(t.row)}}},[e._v(e._s(t.row.eventName)+" ")])]}}])}),a("el-table-column",{attrs:{prop:"businessCategory1",label:"一级分类"}}),a("el-table-column",{attrs:{prop:"businessCategory2",label:"二级分类"}}),a("el-table-column",{attrs:{prop:"creator",label:"创建人"}}),a("el-table-column",{attrs:{prop:"createTime",label:"创建时间"}}),a("el-table-column",{attrs:{prop:"eventStatus",label:"状态"},scopedSlots:e._u([{key:"default",fn:function(a){return[e._v(" "+e._s(e._f("filterStatus")(a.row.eventStatus))+" ")]}}])}),a("el-table-column",{attrs:{label:"操作",width:"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-dropdown",{staticClass:"mr-10"},[a("el-button",[e._v(" 管理"),a("i",{staticClass:"el-icon-arrow-down el-icon--right"})]),a("el-dropdown-menu",{attrs:{slot:"dropdown"},slot:"dropdown"},[5!==t.row.eventStatus?a("el-dropdown-item",{nativeOn:{click:function(a){return e.changeItem(t.row,5)}}},[e._v("上线")]):e._e(),6!==t.row.eventStatus&&1!==t.row.eventStatus?a("el-dropdown-item",{nativeOn:{click:function(a){return e.changeItem(t.row,6)}}},[e._v("下线")]):e._e()],1)],1),1===t.row.eventStatus?a("el-link",{staticClass:"text-sm mr-16",attrs:{type:"primary",underline:!1},on:{click:function(a){return e.deleteItem(t.row)}}},[e._v("删除 ")]):e._e()]}}])})],1)],1),a("div",{staticClass:"pager"},[a("el-pagination",{attrs:{"current-page":e.pageData.currentPage,"page-sizes":e.pageData.pageSizeList,"page-size":e.pageData.pageSize,layout:"total, sizes, prev, pager, next, jumper",total:e.pageData.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1)])]),a("rule-preview-dialog",{ref:"Dialog"})],1)},n=[],r=(t("14d9"),function(){var e=this,a=e._self._c;return a("el-dialog",{class:"event-rule",attrs:{title:"事件规则预览",visible:e.dialogVisible,width:"1000px",top:"2vh","append-to-body":!1},on:{"update:visible":function(a){e.dialogVisible=a},close:e.onCancel}},[a("div",{staticClass:"main-context"},[a("div",{staticClass:"form-container"},[a("h6",[e._v("事件基本信息")]),a("el-row",[a("el-col",{attrs:{span:8}},[a("span",{staticClass:"form-name"},[e._v("事件名称：")]),e._v(e._s(e.selectedData.eventName))]),a("el-col",{attrs:{span:8}},[a("span",{staticClass:"form-name"},[e._v("一级分类：")]),e._v(e._s(e.selectedData.businessCategory1))]),a("el-col",{attrs:{span:8}},[a("span",{staticClass:"form-name"},[e._v("二级分类：")]),e._v(e._s(e.selectedData.businessCategory2))])],1),a("el-row",[a("el-col",{attrs:{span:24}},[a("span",{staticClass:"form-name"},[e._v("事件业务描述：")]),e._v(e._s(e.selectedData.businessDesc))])],1)],1),a("div",{staticClass:"rule-container"},[a("h6",[e._v("事件规则信息")]),a("div",{staticClass:"rule-desc"},e._l(e.selectedData.ruleMap,(function(t,s,n){return a("div",{key:n,staticClass:"rule-item"},e._l(e.selectedData.ruleMap[s],(function(t,s){return a("div",{key:s},[0!==n?a("p",{staticClass:"rule-name"},[a("span",[e._v("「且」")])]):e._e(),a("p",{staticClass:"rule-type"},[e._v("『"+e._s(t.rootCategoryName)),t.rootCategoryName!==t.categoryName?a("span",[e._v("-"+e._s(t.categoryName))]):e._e(),e._v("』")]),e._l(t.values.split(","),(function(t,s){return a("p",{key:s,staticClass:"rule-name"},[0!==s?a("span",[e._v("或")]):e._e(),e._v("〔"+e._s(t)+"〕")])}))],2)})),0)})),0)])])])}),i=[],o=t("72ae"),c=t("0fea"),l={data(){return{selectedData:{},dialogVisible:!1}},mounted(){},methods:{async ShowPopup(e){await Object(c["getActionUrl"])(`${o["a"].ecapi.manageGetDetail}/${e.eventId}`).then(e=>{this.selectedData=e,this.dialogVisible=!0})},onCancel(){this.dialogVisible=!1}}},p=l,d=(t("da54"),t("2877")),u=Object(d["a"])(p,r,i,!1,null,"0d935a32",null),v=u.exports,y={name:"EventManage",components:{RulePreviewDialog:v},data(){return{queryParams:{businessCategoryId1:"",businessCategoryId2:"",current:1,eventStatus:"",keyWords:"",size:10},keyWord:"",dataTree:[{categoryId:"",categoryName:"全部事件类型",categoryLevel:0,subCategoryList:[]}],defaultProps:{children:"subCategoryList",label:"categoryName"},expandedKeys:[],searchExpandedKeys:[],searchExpandedIndex:0,isAlive:!0,tableData:[],pageData:{currentPage:1,pageSize:10,pageSizeList:[10,20,30,40],total:0},statusOptions:[{name:"不限",value:""},{name:"草稿",value:1},{name:"已上线",value:5},{name:"已下线",value:6}],options:[{id:5,value:"上线"},{id:6,value:"下线"}]}},watch:{expandedKeys(e,a){return e[0]!==a[0]}},filters:{filterStatus(e){let a="",t=[{name:"不限",value:""},{name:"草稿",value:1},{name:"审批中",value:2},{name:"审批退回",value:3},{name:"待上线",value:4},{name:"已上线",value:5},{name:"已下线",value:6}];return t.forEach(t=>{t.value===e&&(a=t.name)}),a}},created(){},mounted(){this.manageListByLevel(),this.onSearch()},methods:{onNewRule(){this.$router.push("/EventCenter/EventDefinition")},onSearch(){this.manageList()},async manageListByLevel(){await Object(c["i"])(o["a"].ecapi.manageCategoryList).then(e=>{this.dataTree[0].subCategoryList=e})},async manageList(){await Object(c["i"])(o["a"].ecapi.manageList,{...this.queryParams,current:this.pageData.currentPage,size:this.pageData.pageSize}).then(e=>{this.tableData=e.records,this.pageData.total=e.total})},changeStatus(e){this.queryParams.eventStatus=e,this.pageData.currentPage=1,this.onSearch()},searchName(){if(this.isAlive=!1,this.$nextTick(()=>this.isAlive=!0),this.searchExpandedKeys[this.searchExpandedIndex+1])return this.expandedKeys=[this.searchExpandedKeys[this.searchExpandedIndex+1]],void this.searchExpandedIndex++;let e=[],a=this.dataTree[0].subCategoryList;for(let t=0;t<a.length;t++){const s=a[t];if(-1!==s.categoryName.indexOf(this.keyWord))e.push(s.categoryId);else for(let a=0;a<s.subCategoryList.length;a++){const t=s.subCategoryList[a];-1!==t.categoryName.indexOf(this.keyWord)&&e.push(t.categoryId)}}this.searchExpandedKeys=e,this.searchExpandedIndex=0,this.expandedKeys=[this.searchExpandedKeys[0]]},async handleNodeExpand(e){},async handleNodeClick(e){this.expandedKeys=[""],0===e.categoryLevel&&(this.queryParams.businessCategoryId1="",this.queryParams.businessCategoryId2=""),1===e.categoryLevel&&(this.queryParams.businessCategoryId1=e.categoryId,this.queryParams.businessCategoryId2=""),2===e.categoryLevel&&(this.queryParams.businessCategoryId1="",this.queryParams.businessCategoryId2=e.categoryId),this.onSearch()},openItem(e){this.$refs["Dialog"].ShowPopup(e)},async deleteItem(e){await Object(c["deleteActionUrl"])(`${o["a"].ecapi.manageDelete}/${e.eventId}`).then(e=>{this.$message.success("删除成功!"),this.onSearch()})},async changeItem(e,a){await Object(c["i"])(o["a"].ecapi.manageUpdateStatus,{eventId:e.eventId,status:a}).then(e=>{this.$message.success("改变状态成功!"),this.onSearch()})},handleSizeChange(e){this.pageData.pageSize=e,this.pageData.currentPage=1,this.onSearch()},handleCurrentChange(e){this.pageData.currentPage=e,this.onSearch()}}},g=y,m=(t("9e0e"),Object(d["a"])(g,s,n,!1,null,"6393fa7c",null));a["default"]=m.exports},"72ae":function(e,a,t){"use strict";const s="/mcd-cep-web/action",n="/evt-svc/api";a["a"]={ecapi:{queryEventClass:s+"/cep/rule/nx/queryEventClass.do",queryEventDictByDictCode:s+"/cep/rule/nx/queryEventDictByDictCode.do",queryParentDictByDictCode:s+"/cep/rule/nx/queryParentDictByDictCode.do",queryConfigDict:s+"/cep/rule/nx/queryConfigDict.do",queryEventClassLevel:s+"/cep/rule/queryEventClass.do",queryCepRuleNum:s+"/cep/rule/queryCepRuleNum.do",createRule:s+"/cep/rule/nx/createRule.do",manageList:n+"/event/list",manageUpdateStatus:n+"/event/updateStatus",manageDelete:n+"/event",manageListByParent:n+"/businessCategory/listByParent",manageListByLevel:n+"/businessCategory/listByLevel",manageCategoryList:n+"/businessCategory/list",manageGetDetail:n+"/event",eventSave:n+"/event/save",eventListByLevel:n+"/category/listByLevel",eventListByParent:n+"/category/listByParent",areaList:n+"/area/list",areaGetDetail:n+"/area",areaDelete:n+"/area",areaSave:n+"/area/save",areaStationList:n+"/area/stationList",stationList:n+"/station/list",listByCategory:n+"/categoryValue/listByCategory",queryEventsByUserId:"/plan-svc/api/action/cep/event/eventManager/queryEventsByUserId",queryShareById:"/plan-svc/cepRuleShare/queryShareById",saveOrUpdate:"/plan-svc/cepRuleShare/saveOrUpdate",delCampCoordinationTaskList:"/plan-svc/mcd/strategic/coordination/delCampCoordinationTaskList",exportExampleFile:"/plan-svc/mcd/strategic/coordination/exportExampleFile",pushCampCoordinationFile:"/plan-svc/mcd/strategic/coordination/pushCampCoordinationFile",queryCampCoordinationTask:"/plan-svc/mcd/strategic/coordination/queryCampCoordinationTask",queryStrategicCoordination:"/plan-svc/mcd/strategic/coordination/queryStrategicCoordination",saveStrategicCoordinationTask:"/plan-svc/mcd/strategic/coordination/saveStrategicCoordinationTask",selectAllStrategicCoordination:"/plan-svc/mcd/strategic/coordination/selectAllStrategicCoordination",recountCampTask:"/plan-svc/mcd/strategic/coordination/recountCampTask",queryCampCoordinationTaskDetail:"/plan-svc/mcd/strategic/coordination/queryCampCoordinationTaskDetail",childTaskRecord:"/plan-svc/mcd/strategic/coordination/childTaskRecord",approveTaskRecord:"/plan-svc/api/campCoordinate/taskApprove/approveTaskRecord",approveChildTaskRecord:"/plan-svc/api/campCoordinate/taskApprove/approveChildTaskRecord",subTaskApprove:"/plan-svc/api/campCoordinate/taskApprove/subTaskApprove",updateTaskStatus:"/plan-svc/api/campCoordinate/taskApprove/updateTaskStatus",queryAppTypeList:"/approve-svc/jx/cmpApprovalProcessConf/queryAppTypeList",getApproveConfig:"/approve-svc/jx/cmpApprovalProcessConf/getApproveConfig?systemId=",getNodeApprover:"/approve-svc/jx/cmpApproveProcessInstance/getNodeApprover",submitProcess:"/approve-svc/jx/cmpApproveProcessInstance/submitProcess",delPlanConfList:"/plan-svc/mcd/strategic/coordination/delPlanConfList",qryNotCfgPlanTypeAndPri:"/plan-svc/mcd/strategic/coordination/qryNotCfgPlanTypeAndPri",qryPlanConfList:"/plan-svc/mcd/strategic/coordination/qryPlanConfList",qryPlanConfListByPlanTypeOnSave:"/plan-svc/mcd/strategic/coordination/qryPlanConfListByPlanTypeOnSave",qryPlanConfListByPlanTypeOnUpdate:"/plan-svc/mcd/strategic/coordination/qryPlanConfListByPlanTypeOnUpdate",qryPlanTypeList:"/plan-svc/mcd/strategic/coordination/qryPlanTypeList",savePlanConf:"/plan-svc/mcd/strategic/coordination/savePlanConf",updatePriority:"/plan-svc/mcd/strategic/coordination/updatePriority",getSysDics:"/plan-svc/action/iop/common/getSysDics",delTagConfig:"/plan-svc/orchestrate/tag/delTagConfig",queryAccessLabel:"/plan-svc/orchestrate/tag/queryAccessLabel",queryTag:"/plan-svc/orchestrate/tag/queryTag",queryTagType:"/plan-svc/orchestrate/tag/queryTagType",saveTag:"/plan-svc/orchestrate/tag/saveTag",updateTagOrderBy:"/plan-svc/orchestrate/tag/updateTagOrderBy",queryAllTag:"/plan-svc/orchestrate/tag/queryAllTag"}}},"9e0e":function(e,a,t){"use strict";t("ba9e")},ba9e:function(e,a,t){},da54:function(e,a,t){"use strict";t("17c2")}}]);