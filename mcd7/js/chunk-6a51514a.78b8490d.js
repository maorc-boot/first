(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-6a51514a"],{"25b8":function(e,a,t){},"271a":function(e,a,t){"use strict";var l=t("cb2d"),r=t("e330"),i=t("577e"),s=t("d6d6"),n=URLSearchParams,c=n.prototype,o=r(c.getAll),u=r(c.has),h=new n("a=1");!h.has("a",2)&&h.has("a",void 0)||l(c,"has",(function(e){var a=arguments.length,t=a<2?void 0:arguments[1];if(a&&void 0===t)return u(this,e);var l=o(this,e);s(a,1);var r=i(t),n=0;while(n<l.length)if(l[n++]===r)return!0;return!1}),{enumerable:!0,unsafe:!0})},5494:function(e,a,t){"use strict";var l=t("83ab"),r=t("e330"),i=t("edd0"),s=URLSearchParams.prototype,n=r(s.forEach);l&&!("size"in s)&&i(s,"size",{get:function(){var e=0;return n(this,(function(){e++})),e},configurable:!0,enumerable:!0})},"714e":function(e,a,t){},"88a7":function(e,a,t){"use strict";var l=t("cb2d"),r=t("e330"),i=t("577e"),s=t("d6d6"),n=URLSearchParams,c=n.prototype,o=r(c.append),u=r(c["delete"]),h=r(c.forEach),p=r([].push),d=new n("a=1&a=2&b=3");d["delete"]("a",1),d["delete"]("b",void 0),d+""!=="a=2"&&l(c,"delete",(function(e){var a=arguments.length,t=a<2?void 0:arguments[1];if(a&&void 0===t)return u(this,e);var l=[];h(this,(function(e,a){p(l,{key:a,value:e})})),s(a,1);var r,n=i(e),c=i(t),d=0,m=0,g=!1,b=l.length;while(d<b)r=l[d++],g||r.key===n?(g=!0,u(this,r.key)):m++;while(m<b)r=l[m++],r.key===n&&r.value===c||o(this,r.key,r.value)}),{enumerable:!0,unsafe:!0})},b524:function(e,a,t){"use strict";a["a"]={callDetails:{outboundCallQuery:"/plan-svc/api/jx/callDetails/queryCallDetailsList",outboundCallExport:"/plan-svc/api/jx/callDetails/getCallExportDataList",outboundCallCity:"/plan-svc/api/jx/term/getDimCity",outboundCallCounty:"/plan-svc/api/jx/term/getDimCounty",outboundCallGrid:"/plan-svc/api/jx/term/getDimGrid",outboundBusiness:"/plan-svc/api/jx/term/getScenario",outboundPermissions:"/plan-svc/api/jx/term/getUserPermissionConfig",outboundUserPermission:"/plan-svc/api/jx/term/getReportUserPermission"},messageDetails:{messageCallQuery:"/plan-svc/api/jx/callDetails/getCallHttp"},earlyDetails:{warningQuery:"/plan-svc/api/jx/trace/queryTouchTraceList",warningExport:"/plan-svc/api/jx/trace/exportTrace",warningReport:"/plan-svc/api/jx/term/getReportTraceAlarm"},energyDetails:{efficiencyQuery:"/plan-svc/api/jx/energy/queryAlarmEnergyList",efficiencyExport:"/plan-svc/api/jx/energy/exportEnergy",efficiencyReport:"/plan-svc/api/jx/term/getReportEnergyAlarm"},blackList:{blackQuery:"/plan-svc/jx/customizetitle/getCustomizeTitleList",batchImport:"/plan-svc/jx/customizetitle/batchImportCustomizeTitleData",batchDelete:"/plan-svc/jx/customizetitle/deleteCustTitleById"}}},d1cf:function(e,a,t){"use strict";t("25b8")},d6d6:function(e,a,t){"use strict";var l=TypeError;e.exports=function(e,a){if(e<a)throw new l("Not enough arguments");return e}},e252:function(e,a,t){"use strict";t.r(a);var l=function(){var e=this,a=e._self._c;return a("div",{staticClass:"outbound-content"},[a("el-tabs",{attrs:{type:"card"},model:{value:e.tabActive,callback:function(a){e.tabActive=a},expression:"tabActive"}},[a("el-tab-pane",{attrs:{label:"预警轨迹查询",name:"warningTrajectory",lazy:!0}},[a("warningTrajectory")],1),a("el-tab-pane",{attrs:{label:"预警能效分析",name:"warningEnergy",lazy:!0}},[a("warningEnergy")],1)],1)],1)},r=[],i=function(){var e=this,a=e._self._c;return a("div",{staticClass:"template-wrap-content"},[a("div",{staticClass:"top-edit-part"},[a("div",{staticClass:"search-input"},[a("label",[e._v("地市：")]),a("el-select",{staticStyle:{width:"140px"},attrs:{placeholder:"请选择"},on:{change:e.getCountyList},model:{value:e.searchParams.city,callback:function(a){e.$set(e.searchParams,"city",a)},expression:"searchParams.city"}},e._l(e.cityList,(function(e){return a("el-option",{key:e.cityId+""+e.cityName,attrs:{label:e.cityName,value:e.cityId}})})),1)],1),a("div",{staticClass:"search-input"},[a("label",[e._v("区县：")]),a("el-select",{staticStyle:{width:"140px"},attrs:{placeholder:"请选择"},model:{value:e.searchParams.county,callback:function(a){e.$set(e.searchParams,"county",a)},expression:"searchParams.county"}},e._l(e.countyList,(function(e){return a("el-option",{key:e.countyId+e.countyName,attrs:{label:e.countyName,value:e.countyId}})})),1)],1),a("div",{staticClass:"search-input"},[a("label",[e._v("网格：")]),a("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],staticStyle:{width:"140px"},attrs:{placeholder:"请输入网格名称",clearable:""},model:{value:e.searchParams.grid,callback:function(a){e.$set(e.searchParams,"grid","string"===typeof a?a.trim():a)},expression:"searchParams.grid"}})],1),a("div",{staticClass:"search-input"},[a("label",[e._v("经理工号：")]),a("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],staticStyle:{width:"140px"},attrs:{placeholder:"请输入经理工号",clearable:""},model:{value:e.searchParams.managerId,callback:function(a){e.$set(e.searchParams,"managerId","string"===typeof a?a.trim():a)},expression:"searchParams.managerId"}})],1),a("div",{staticClass:"search-input"},[a("label",[e._v("预警号码：")]),a("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],staticStyle:{width:"140px"},attrs:{placeholder:"请输入预警号码",clearable:""},model:{value:e.searchParams.productNo,callback:function(a){e.$set(e.searchParams,"productNo","string"===typeof a?a.trim():a)},expression:"searchParams.productNo"}})],1),a("div",{staticClass:"search-input"},[a("label",[e._v("预警名称：")]),a("el-select",{staticStyle:{width:"140px"},attrs:{placeholder:"请选择"},model:{value:e.searchParams.alarmName,callback:function(a){e.$set(e.searchParams,"alarmName",a)},expression:"searchParams.alarmName"}},e._l(e.alarmList,(function(e){return a("el-option",{key:e.alarmId+e.alarmName,attrs:{label:e.alarmName,value:e.alarmId}})})),1)],1),a("div",{staticClass:"search-input"},[a("label",[e._v("预警类型：")]),a("el-select",{staticStyle:{width:"140px"},attrs:{placeholder:"请选择",clearable:""},model:{value:e.searchParams.alarmType,callback:function(a){e.$set(e.searchParams,"alarmType",a)},expression:"searchParams.alarmType"}},[a("el-option",{attrs:{value:"",label:"全部"}}),a("el-option",{attrs:{value:"自定义预警",label:"自定义预警"}}),a("el-option",{attrs:{value:"协同预警",label:"协同预警"}})],1)],1),a("div",{staticClass:"search-input"},[a("label",[e._v("任务状态：")]),a("el-select",{staticStyle:{width:"140px"},attrs:{placeholder:"请选择",clearable:""},model:{value:e.searchParams.taskStatus,callback:function(a){e.$set(e.searchParams,"taskStatus",a)},expression:"searchParams.taskStatus"}},[a("el-option",{attrs:{value:"",label:"全部"}}),a("el-option",{attrs:{value:"正常",label:"正常"}}),a("el-option",{attrs:{value:"终止",label:"终止"}}),a("el-option",{attrs:{value:"过滤",label:"过滤"}})],1)],1),a("div",{staticClass:"search-input"},[a("label",[e._v("接触状态：")]),a("el-select",{staticStyle:{width:"140px"},attrs:{placeholder:"请选择",clearable:""},model:{value:e.searchParams.touchStatus,callback:function(a){e.$set(e.searchParams,"touchStatus",a)},expression:"searchParams.touchStatus"}},[a("el-option",{attrs:{value:"",label:"全部"}}),a("el-option",{attrs:{value:"已接触",label:"已接触"}}),a("el-option",{attrs:{value:"接触未达时长",label:"接触未达时长"}}),a("el-option",{attrs:{value:"未接通",label:"未接通"}}),a("el-option",{attrs:{value:"待接触",label:"待接触"}})],1)],1),a("div",{staticClass:"search-input"},[a("label",[e._v("修复状态：")]),a("el-select",{staticStyle:{width:"140px"},attrs:{placeholder:"请选择",clearable:""},model:{value:e.searchParams.repairStatus,callback:function(a){e.$set(e.searchParams,"repairStatus",a)},expression:"searchParams.repairStatus"}},[a("el-option",{attrs:{value:"",label:"全部"}}),a("el-option",{attrs:{value:"是",label:"是"}}),a("el-option",{attrs:{value:"否",label:"否"}})],1)],1),a("div",{staticClass:"search-input"},[a("label",[e._v("闭环状态：")]),a("el-select",{staticStyle:{width:"140px"},attrs:{placeholder:"请选择",clearable:""},model:{value:e.searchParams.closedStatus,callback:function(a){e.$set(e.searchParams,"closedStatus",a)},expression:"searchParams.closedStatus"}},[a("el-option",{attrs:{value:"",label:"全部"}}),a("el-option",{attrs:{value:"手动闭环",label:"手动闭环"}}),a("el-option",{attrs:{value:"自动解警",label:"自动解警"}})],1)],1),a("div",{staticClass:"search-input"},[a("label",[e._v("账期：")]),a("el-date-picker",{staticStyle:{width:"215px"},attrs:{type:"daterange","value-format":"yyyy-MM-dd","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期","default-value":e.defaultDateRange},on:{change:e.dateChange},model:{value:e.queryTime,callback:function(a){e.queryTime=a},expression:"queryTime"}})],1),a("div",{staticClass:"btn-search"},[a("el-button",{attrs:{type:"primary"},on:{click:e.searchTable}},[e._v("查询")]),a("el-button",{attrs:{type:"primary"},on:{click:e.downloadTemplate}},[e._v("导出")])],1)]),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],ref:"tTable",staticClass:"template-table customTable",staticStyle:{width:"100%"},attrs:{data:e.tableData,stripe:"",height:600}},[a("el-table-column",{attrs:{prop:"statDate",label:"账期",fixed:"","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"cityName",label:"地市",fixed:"","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"countryName",label:"区县",fixed:"","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"staffId",label:"经理工号",fixed:"","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"staffName",label:"经理姓名",fixed:"","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"gridName",label:"网格编码","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"channelId",label:"渠道编码","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"channelName",label:"渠道","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"warningDate",label:"预警时间(省专用)","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"filter",label:"是否过滤","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"filterCase",label:"过滤原因","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"createTime",label:"任务下发时间","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"productNo",label:"预警号码","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"alarmId",label:"预警id","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"alarmName",label:"预警名称","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"alarmType",label:"预警类型","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"dataStatus",label:"任务状态","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"jcStatus",label:"接触状态","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"end_time",label:"接触时间","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"talkDuration",label:"接触时长","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"xf",label:"是否修复","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"updateTime",label:"修复时间","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"bhStatus",label:"闭环状态","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"bhTime",label:"闭环时间","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"bhReason",label:"闭环原因","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"reaClearWarning",label:"解警原因","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"clearWarningDate",label:"解警时间","header-align":"center",align:"center"}})],1),a("el-pagination",{attrs:{"current-page":e.query.current,"page-sizes":[10,20,30,40],"page-size":e.query.size,"pager-count":5,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1)},s=[],n=(t("88a7"),t("271a"),t("5494"),t("0fea")),c=t("b524"),o={name:"warningTrajectory",data(){const e=new Date,a=new Date(e);return a.setDate(e.getDate()-7),{loading:!1,defaultDateRange:[this.formatDate(a),this.formatDate(e)],searchParams:{city:"",county:"",managerId:"",productNo:"",alarmName:"",alarmType:"",taskStatus:"",touchStatus:"",repairStatus:"",closedStatus:"",startTime:"",endTime:"",grid:""},query:{pageNum:1,pageSize:10},total:0,queryTime:null,cityList:[],countyList:[],tableData:[],gridList:[],alarmList:[],powerNum:"",powerCityId:"",powerCounty:"",powerGridName:""}},methods:{formatDate(e){const a=e.getFullYear(),t=(e.getMonth()+1).toString().padStart(2,"0"),l=e.getDate().toString().padStart(2,"0");return`${a}-${t}-${l}`},dateChange(e){e?(this.searchParams.startTime=e[0],this.searchParams.endTime=e[1]):(this.searchParams.startTime="",this.searchParams.endTime="")},searchTable(){this.query={...this.query,...this.searchParams},this.queryTime&&(this.query.endTime=this.queryTime[1],this.query.startTime=this.queryTime[0]),this.query.pageNum=1,this.getTableData()},async getTableData(){try{this.loading=!0;const e=await Object(n["i"])(c["a"].earlyDetails.warningQuery,this.query);this.tableData=(null===e||void 0===e?void 0:e.records)||[],this.total=e.total}catch(e){}finally{this.loading=!1}},downloadTemplate(){Object(n["b"])(c["a"].earlyDetails.warningExport,{...this.searchParams,pageNum:1,pageSize:99999}).then(({data:e})=>{const a=new Blob([e],{type:e.type}),t="预警轨迹.xls",l=document.createElement("a");l.download=t,l.style.display="none",l.href=URL.createObjectURL(a),document.body.appendChild(l),l.click(),URL.revokeObjectURL(l.href),document.body.removeChild(l)})},async getCityList(){this.searchParams.city=this.powerCityId;try{const e=await Object(n["i"])(c["a"].callDetails.outboundCallCity);if(e.unshift({cityId:"",cityName:"全部"}),"1"===this.powerNum||""===this.powerNum)this.searchParams.city="",this.cityList=e;else if("2"===this.powerNum||"3"===this.powerNum||"4"===this.powerNum){const a=e.filter(e=>e.cityId===this.powerCityId);this.cityList=a}}catch(e){}},async getCountyList(e){this.searchParams.county=this.powerCounty;try{const a=await Object(n["i"])(c["a"].callDetails.outboundCallCounty,{cityId:e||("999"===this.powerCityId?"":this.powerCityId)});if(a.unshift({countyId:"",countyName:"全部"}),"3"===this.powerNum||"4"===this.powerNum){const e=a.filter(e=>e.countyId===this.powerCounty);this.countyList=e,"9999"===this.powerCounty?this.countyList=a:this.countyList=e}else this.countyList=a}catch(a){this.countyList=[{countyId:"",countyName:"全部"}]}},getGridName(){"4"===this.powerNum?this.searchParams.grid=this.powerGridName:this.searchParams.grid=""},async getWarningList(){try{const e=await Object(n["i"])(c["a"].earlyDetails.warningReport,{cityId:this.searchParams.city,countryId:this.searchParams.county,gridId:this.searchParams.grid});e.unshift({alarmId:"",alarmName:"全部"}),this.alarmList=e}catch(e){}},async getUserPermissions(){const e=await Object(n["i"])(c["a"].callDetails.outboundUserPermission);this.powerNum=e[0].permission||"",this.powerCityId="999"===e[0].cityId?"":e[0].cityId,this.powerCounty="9999"===e[0].county?"":e[0].county,this.powerGridName=e[0].gridName},handleSizeChange(e){this.query.pageNum=1,this.query.pageSize=e,this.getTableData()},handleCurrentChange(e){this.query.pageNum=e,this.getTableData()}},created(){this.queryTime=this.defaultDateRange},async mounted(){await this.getUserPermissions(),this.getCityList(),this.getCountyList(),this.getGridName(),this.getWarningList(),this.searchTable()}},u=o,h=(t("d1cf"),t("2877")),p=Object(h["a"])(u,i,s,!1,null,"5f806174",null),d=p.exports,m=function(){var e=this,a=e._self._c;return a("div",{staticClass:"template-wrap-content"},[a("div",{staticClass:"top-edit-part"},[a("div",{staticClass:"search-input"},[a("label",[e._v("地市：")]),a("el-select",{staticStyle:{width:"140px"},attrs:{placeholder:"请选择"},on:{change:e.getCountyList},model:{value:e.searchParams.city,callback:function(a){e.$set(e.searchParams,"city",a)},expression:"searchParams.city"}},e._l(e.cityList,(function(e){return a("el-option",{key:e.cityId+""+e.cityName,attrs:{label:e.cityName,value:e.cityId}})})),1)],1),a("div",{staticClass:"search-input"},[a("label",[e._v("区县：")]),a("el-select",{staticStyle:{width:"140px"},attrs:{placeholder:"请选择"},model:{value:e.searchParams.county,callback:function(a){e.$set(e.searchParams,"county",a)},expression:"searchParams.county"}},e._l(e.countyList,(function(e){return a("el-option",{key:e.countyId+e.countyName,attrs:{label:e.countyName,value:e.countyId}})})),1)],1),a("div",{staticClass:"search-input"},[a("label",[e._v("网格：")]),a("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],staticStyle:{width:"140px"},attrs:{placeholder:"请输入网格名称",clearable:""},model:{value:e.searchParams.grid,callback:function(a){e.$set(e.searchParams,"grid","string"===typeof a?a.trim():a)},expression:"searchParams.grid"}})],1),a("div",{staticClass:"search-input"},[a("label",[e._v("预警类型：")]),a("el-select",{staticStyle:{width:"140px"},attrs:{placeholder:"请选择",clearable:""},model:{value:e.searchParams.alarmType,callback:function(a){e.$set(e.searchParams,"alarmType",a)},expression:"searchParams.alarmType"}},[a("el-option",{attrs:{value:"",label:"全部"}}),a("el-option",{attrs:{value:"自定义预警",label:"自定义预警"}}),a("el-option",{attrs:{value:"协同预警",label:"协同预警"}})],1)],1),a("div",{staticClass:"search-input"},[a("label",[e._v("预警名称：")]),a("el-select",{staticStyle:{width:"140px"},attrs:{placeholder:"请选择"},model:{value:e.searchParams.alarmName,callback:function(a){e.$set(e.searchParams,"alarmName",a)},expression:"searchParams.alarmName"}},e._l(e.energyList,(function(e){return a("el-option",{key:e.alarmId+e.alarmName,attrs:{label:e.alarmName,value:e.alarmId}})})),1)],1),a("div",{staticClass:"search-input"},[a("label",[e._v("经理工号：")]),a("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],staticStyle:{width:"140px"},attrs:{placeholder:"请输入经理工号",clearable:""},model:{value:e.searchParams.managerId,callback:function(a){e.$set(e.searchParams,"managerId","string"===typeof a?a.trim():a)},expression:"searchParams.managerId"}})],1),a("div",{staticClass:"search-input"},[a("label",[e._v("创建时间：")]),a("el-date-picker",{staticStyle:{width:"215px"},attrs:{type:"daterange","value-format":"yyyy-MM-dd","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期","default-value":e.defaultDateRange},on:{change:e.dateChange},model:{value:e.queryTime,callback:function(a){e.queryTime=a},expression:"queryTime"}})],1),a("div",{staticClass:"btn-search"},[a("el-button",{attrs:{type:"primary"},on:{click:e.searchTable}},[e._v("查询")]),a("el-button",{attrs:{type:"primary"},on:{click:e.downloadTemplate}},[e._v("导出")])],1)]),a("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],ref:"tTable",staticClass:"template-table customTable",staticStyle:{width:"100%"},attrs:{data:e.tableData,stripe:"",height:600}},[a("el-table-column",{attrs:{prop:"cityName",label:"地市",fixed:"","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"countryName",label:"区县",fixed:"","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"gridName",label:"网格名称",fixed:"","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"alarmType",label:"预警类型",fixed:"","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"alarmName",label:"预警名称",fixed:"","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{label:"任务下发","header-align":"center"}},[a("el-table-column",{attrs:{prop:"issuedTaskNum",label:"任务数量","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"issuedUserNum",label:"用户数量","header-align":"center",align:"center"}})],1),a("el-table-column",{attrs:{label:"过滤情况TOP3","header-align":"center"}},[a("el-table-column",{attrs:{prop:"taskFilterNum",label:"过滤总数量","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"taskFilterReason1",label:"TOP1过滤原因","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"taskFilterNum1",label:"数量","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"taskFilterReason2",label:"TOP2过滤原因","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"taskFilterNum2",label:"数量","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"taskFilterReason3",label:"TOP3过滤原因","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"taskFilterNum3",label:"数量","header-align":"center",align:"center"}})],1),a("el-table-column",{attrs:{label:"闭环情况","header-align":"center"}},[a("el-table-column",{attrs:{prop:"closedNum",label:"闭环总数量","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"labourClosed",label:"人工闭环","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"autoClosed",label:"自动解警","header-align":"center",align:"center"}})],1),a("el-table-column",{attrs:{label:"下发APP","header-align":"center"}},[a("el-table-column",{attrs:{prop:"normalTaskNum",label:"任务数量","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"normalUserNum",label:"用户数量","header-align":"center",align:"center"}})],1),a("el-table-column",{attrs:{label:"过滤情况","header-align":"center"}},[a("el-table-column",{attrs:{prop:"unNormalTaskNum",label:"过滤总数量","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"unNormalReached",label:"已接触","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"unNormalFixed",label:"已修复","header-align":"center",align:"center"}})],1),a("el-table-column",{attrs:{label:"闭环情况","header-align":"center"}},[a("el-table-column",{attrs:{prop:"closedTermNum",label:"闭环总数量","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"closedTermReached",label:"已接触","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"closedTermFixed",label:"已修复","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"closedLabour",label:"人工闭环","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"closedLabourReached",label:"已接触","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"closedLabourFiexd",label:"已修复","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"closedAuto",label:"自动解警","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"closedAutoReached",label:"已接触","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"closedAutoFiexd",label:"已修复","header-align":"center",align:"center"}})],1),a("el-table-column",{attrs:{label:"接触状态","header-align":"center"}},[a("el-table-column",{attrs:{prop:"unTouch",label:"未接触","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"noGetTouch",label:"未接通","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"noReachGetTouch",label:"已接触未达时长","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"reachGetTouch",label:"已接触","header-align":"center",align:"center"}})],1),a("el-table-column",{attrs:{label:"修复状态","header-align":"center"}},[a("el-table-column",{attrs:{prop:"fixedRepairState",label:"未修复","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"closedRepairState",label:"已修复","header-align":"center",align:"center"}})],1),a("el-table-column",{attrs:{label:"闭环状态","header-align":"center"}},[a("el-table-column",{attrs:{prop:"closeLoopStatus",label:"未闭环","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"closeLabourDealarm",label:"人工闭环","header-align":"center",align:"center"}}),a("el-table-column",{attrs:{prop:"closeAutoDealarm",label:"自动解警","header-align":"center",align:"center"}})],1)],1),a("el-pagination",{attrs:{"current-page":e.query.current,"page-sizes":[10,20,30,40],"page-size":e.query.size,"pager-count":5,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1)},g=[],b={name:"warningEnergy",data(){const e=new Date,a=new Date(e);return a.setDate(e.getDate()-7),{loading:!1,defaultDateRange:[this.formatDate(a),this.formatDate(e)],searchParams:{city:"",county:"",grid:"",alarmType:"",alarmName:"",managerId:"",startTime:"",endTime:""},queryTime:null,query:{pageNum:1,pageSize:10},total:0,cityList:[],countyList:[],gridList:[],earlyList:[],tableData:[],energyList:[],powerNum:"",powerCityId:"",powerCounty:"",powerGridName:""}},methods:{formatDate(e){const a=e.getFullYear(),t=(e.getMonth()+1).toString().padStart(2,"0"),l=e.getDate().toString().padStart(2,"0");return`${a}-${t}-${l}`},dateChange(e){e?(this.searchParams.startTime=e[0],this.searchParams.endTime=e[1]):(this.searchParams.startTime="",this.searchParams.endTime="")},searchTable(){this.query={...this.query,...this.searchParams},this.queryTime&&(this.query.endTime=this.queryTime[1],this.query.startTime=this.queryTime[0]),this.query.pageNum=1,this.getTableData()},async getTableData(){try{this.loading=!0;const e=await Object(n["i"])(c["a"].energyDetails.efficiencyQuery,this.query);this.tableData=(null===e||void 0===e?void 0:e.records)||[],this.total=e.total}catch(e){}finally{this.loading=!1}},downloadTemplate(){Object(n["b"])(c["a"].energyDetails.efficiencyExport,{...this.searchParams,pageNum:1,pageSize:99999}).then(({data:e})=>{const a=new Blob([e],{type:e.type}),t="预警能效.xls",l=document.createElement("a");l.download=t,l.style.display="none",l.href=URL.createObjectURL(a),document.body.appendChild(l),l.click(),URL.revokeObjectURL(l.href),document.body.removeChild(l)})},async getCityList(){this.searchParams.city=this.powerCityId;try{const e=await Object(n["i"])(c["a"].callDetails.outboundCallCity);if(e.unshift({cityId:"",cityName:"全部"}),"1"===this.powerNum||""===this.powerNum)this.searchParams.city="",this.cityList=e;else if("2"===this.powerNum||"3"===this.powerNum||"4"===this.powerNum){const a=e.filter(e=>e.cityId===this.powerCityId);this.cityList=a}}catch(e){}},async getCountyList(e){this.searchParams.county=this.powerCounty;try{const a=await Object(n["i"])(c["a"].callDetails.outboundCallCounty,{cityId:e||("999"===this.powerCityId?"":this.powerCityId)});if(a.unshift({countyId:"",countyName:"全部"}),"3"===this.powerNum||"4"===this.powerNum){const e=a.filter(e=>e.countyId===this.powerCounty);this.countyList=e,"9999"===this.powerCounty?this.countyList=a:this.countyList=e}else this.countyList=a}catch(a){this.countyList=[{countyId:"",countyName:"全部"}]}},getGridName(){"4"===this.powerNum?this.searchParams.grid=this.powerGridName:this.searchParams.grid=""},async getEnergyList(){try{const e=await Object(n["i"])(c["a"].energyDetails.efficiencyReport,{cityId:this.searchParams.city,countryId:this.searchParams.county,gridId:this.searchParams.grid});e.unshift({alarmId:"",alarmName:"全部"}),this.energyList=e}catch(e){}},async getUserPermissions(){const e=await Object(n["i"])(c["a"].callDetails.outboundUserPermission);this.powerNum=e[0].permission||"",this.powerCityId="999"===e[0].cityId?"":e[0].cityId,this.powerCounty="9999"===e[0].county?"":e[0].county,this.powerGridName=e[0].gridName},handleSizeChange(e){this.query.pageNum=1,this.query.pageSize=e,this.getTableData()},handleCurrentChange(e){this.query.pageNum=e,this.getTableData()}},created(){this.queryTime=this.defaultDateRange},async mounted(){await this.getUserPermissions(),this.getCityList(),this.getCountyList(),this.getGridName(),this.getEnergyList(),this.searchTable()}},y=b,v=(t("e4d0"),Object(h["a"])(y,m,g,!1,null,"1d7168e9",null)),f=v.exports,w={name:"index",components:{warningTrajectory:d,warningEnergy:f},data(){return{tabActive:"warningTrajectory"}}},C=w,N=(t("ff02"),Object(h["a"])(C,l,r,!1,null,"1d0d4223",null));a["default"]=N.exports},e4d0:function(e,a,t){"use strict";t("714e")},edd0:function(e,a,t){"use strict";var l=t("13d2"),r=t("9bf2");e.exports=function(e,a,t){return t.get&&l(t.get,a,{getter:!0}),t.set&&l(t.set,a,{setter:!0}),r.f(e,a,t)}},f8dd:function(e,a,t){},ff02:function(e,a,t){"use strict";t("f8dd")}}]);