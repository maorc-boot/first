(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-581531b1"],{"271a":function(e,t,a){"use strict";var r=a("cb2d"),l=a("e330"),n=a("577e"),i=a("d6d6"),o=URLSearchParams,c=o.prototype,s=l(c.getAll),d=l(c.has),p=new o("a=1");!p.has("a",2)&&p.has("a",void 0)||r(c,"has",(function(e){var t=arguments.length,a=t<2?void 0:arguments[1];if(t&&void 0===a)return d(this,e);var r=s(this,e);i(t,1);var l=n(a),o=0;while(o<r.length)if(r[o++]===l)return!0;return!1}),{enumerable:!0,unsafe:!0})},"30b9":function(e,t,a){"use strict";const r="/eval-svc";t["a"]={effectAppraisal:{dayEffect:r+"/home/eval/day/effect",monthEffect:r+"/home/eval/month/effect",getCityList:r+"/home/eval/getCityList",activityList:r+"/evaluate/activity/list",productList:r+"/mtl-eval-info-plan/queryPage",exportProduct:r+"/mtl-eval-info-plan/export",channelList:r+"/evaluate/channel/list",getTdList:"/eval-svc/td/eval/list",exportTdEvalListData:"/eval-svc/td/eval/exportTdEvalListData",exportOrderChannelDMData:"/eval-svc/reportform/download/exportOrderChannelDMData",exportOrderProductDMData:"/eval-svc/reportform/download/exportOrderProductDMData",getPlanChannelReportList:"/eval-svc/reportform/download/getPlanChannelReportList",getPlanOrderReportList:"/eval-svc/reportform/download/getPlanOrderReportList"},enterprise:{exportMarketingData:"/eval-svc/action/jx/enterpriseMarketing/exportMarketingData",getDimGrid:"/eval-svc/action/jx/enterpriseMarketing/getDimGrid",getDimGroups:"/eval-svc/action/jx/enterpriseMarketing/getDimGroups",getDimManager:"/eval-svc/action/jx/enterpriseMarketing/getDimManager",queryMarketingList:"/eval-svc/action/jx/enterpriseMarketing/queryMarketingList",saveCase:"/eval-svc/action/jx/enterpriseMarketing/saveCase"}}},3466:function(e,t,a){},"3ae1":function(e,t,a){"use strict";a("3466")},5494:function(e,t,a){"use strict";var r=a("83ab"),l=a("e330"),n=a("edd0"),i=URLSearchParams.prototype,o=l(i.forEach);r&&!("size"in i)&&n(i,"size",{get:function(){var e=0;return o(this,(function(){e++})),e},configurable:!0,enumerable:!0})},"73a9":function(e,t,a){"use strict";a.r(t);var r=function(){var e=this,t=e._self._c;return t("div",{staticClass:"clientele-content"},[t("el-tabs",{attrs:{type:"card"},model:{value:e.tabActive,callback:function(t){e.tabActive=t},expression:"tabActive"}},[t("el-tab-pane",{attrs:{label:"产品分类订购报表",name:"a",lazy:!0}},[t("Sort")],1),t("el-tab-pane",{attrs:{label:"产品分渠道订购报表",name:"b",lazy:!0}},[t("Channel")],1)],1)],1)},l=[],n=function(){var e=this,t=e._self._c;return t("div",{staticClass:"template-wrap-content"},[t("div",{staticClass:"top-edit-part"},[t("el-date-picker",{staticStyle:{width:"200px"},attrs:{type:"date",placeholder:"请选择日期（年月日）",format:"yyyy-MM-dd","value-format":"yyyy-MM-dd"},model:{value:e.query.opTime,callback:function(t){e.$set(e.query,"opTime",t)},expression:"query.opTime"}}),t("el-button",{attrs:{type:"primary",loading:e.exportLoading},on:{click:e.exportData}},[e._v("导出Excel")])],1),t("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],ref:"tTable",staticClass:"template-table customTable",staticStyle:{width:"100%"},attrs:{data:e.tableData,stripe:"",border:"",height:600}},[t("el-table-column",{attrs:{type:"index",label:"序号",align:"center",width:"45"}}),t("el-table-column",{attrs:{prop:"cityName",label:"地市名称","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"planType",label:"产品名称","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"tuijianDs",label:"当天推荐","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"ljblDs",label:"当天立即办理","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"xyklDs",label:"当天需要考虑","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"zsjjDs",label:"当天暂时拒接","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"succDs",label:"当天是否办理","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"tuijianDt",label:"当月推荐","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"ljblDt",label:"当月立即办理","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"xyklDt",label:"当月需要考虑","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"zsjjDt",label:"当月暂时拒接","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"succDt",label:"当月是否办理","header-align":"center",align:"center"}})],1),t("el-pagination",{attrs:{"current-page":e.query.current,"page-sizes":[10,20,30,40],"page-size":e.query.size,"pager-count":5,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1)},i=[],o=(a("88a7"),a("271a"),a("5494"),a("0fea")),c=a("30b9"),s={name:"Diff",data(){return{loading:!1,exportLoading:!1,query:{opTime:"",current:1,size:10},total:0,tableData:[]}},watch:{"query.opTime"(e){this.query.current=1,this.getTableData()}},methods:{async getTableData(){try{this.loading=!0;const{records:e,total:t}=await Object(o["i"])(c["a"].effectAppraisal.getPlanOrderReportList,this.query);this.tableData=e,this.total=t}catch(e){}finally{this.loading=!1}},addPercent(e){return e.map(e=>(e.djcRate=isNaN(parseFloat(e.djcRate))?"-":(100*e.djcRate).toFixed(2)+"%",e.dxyRate=isNaN(parseFloat(e.dxyRate))?"-":(100*e.dxyRate).toFixed(2)+"%",e.dblRate=isNaN(parseFloat(e.dblRate))?"-":(100*e.dblRate).toFixed(2)+"%",e))},handleSizeChange(e){this.query.current=1,this.query.size=e,this.getTableData()},handleCurrentChange(e){this.query.current=e,this.getTableData()},async exportData(){try{this.exportLoading=!0;const{data:e}=await Object(o["b"])(c["a"].effectAppraisal.exportOrderProductDMData,this.query),t=new Blob([e],{type:e.type}),a="产品分类订购报表.xls";if("download"in document.createElement("a")){const e=document.createElement("a");e.download=a,e.style.display="none",e.href=URL.createObjectURL(t),document.body.appendChild(e),e.click(),URL.revokeObjectURL(e.href),document.body.removeChild(e)}else navigator.msSaveBlob(t,a)}catch(e){}finally{this.exportLoading=!1}}},mounted(){this.getTableData()}},d=s,p=(a("ba55"),a("2877")),u=Object(p["a"])(d,n,i,!1,null,"5e15efab",null),g=u.exports,h=function(){var e=this,t=e._self._c;return t("div",{staticClass:"template-wrap-content"},[t("div",{staticClass:"top-edit-part"},[t("el-date-picker",{staticStyle:{width:"200px"},attrs:{type:"date",placeholder:"请选择日期（年月日）",format:"yyyy-MM-dd","value-format":"yyyy-MM-dd"},model:{value:e.query.opTime,callback:function(t){e.$set(e.query,"opTime",t)},expression:"query.opTime"}}),t("el-button",{attrs:{type:"primary",loading:e.exportLoading},on:{click:e.exportData}},[e._v("导出Excel")])],1),t("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],ref:"tTable",staticClass:"template-table customTable",staticStyle:{width:"100%"},attrs:{data:e.tableData,stripe:"",border:"",height:600}},[t("el-table-column",{attrs:{type:"index",label:"序号",align:"center",width:"45"}}),t("el-table-column",{attrs:{prop:"cityName",label:"地市名称","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"countyName",label:"区县名称","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"channel",label:"渠道ID","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"operId",label:"操作ID","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"dytj",label:"当月推荐","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"ljbl",label:"立即办理","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"xykl",label:"需要考虑","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"zsjj",label:"暂时拒绝","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"isCz",label:"是否操作","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"isBanLi",label:"是否办理","header-align":"center",align:"center"}}),t("el-table-column",{attrs:{prop:"isSelfBanLi",label:"是否该营业员操作","header-align":"center",align:"center"}})],1),t("el-pagination",{attrs:{"current-page":e.query.current,"page-sizes":[10,20,30,40],"page-size":e.query.size,"pager-count":5,layout:"total, sizes, prev, pager, next, jumper",total:e.total},on:{"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}})],1)},b=[],y={name:"Diff",data(){return{loading:!1,exportLoading:!1,query:{opTime:"",current:1,size:10},total:0,tableData:[]}},watch:{"query.opTime"(e){this.query.current=1,this.getTableData()}},methods:{async getTableData(){try{this.loading=!0;const{records:e,total:t}=await Object(o["i"])(c["a"].effectAppraisal.getPlanChannelReportList,this.query);this.tableData=e,this.total=t}catch(e){}finally{this.loading=!1}},addPercent(e){return e.map(e=>(e.djcRate=isNaN(parseFloat(e.djcRate))?"-":(100*e.djcRate).toFixed(2)+"%",e.dxyRate=isNaN(parseFloat(e.dxyRate))?"-":(100*e.dxyRate).toFixed(2)+"%",e.dblRate=isNaN(parseFloat(e.dblRate))?"-":(100*e.dblRate).toFixed(2)+"%",e))},handleSizeChange(e){this.query.current=1,this.query.size=e,this.getTableData()},handleCurrentChange(e){this.query.current=e,this.getTableData()},async exportData(){try{this.exportLoading=!0;const{data:e}=await Object(o["b"])(c["a"].effectAppraisal.exportOrderChannelDMData,this.query),t=new Blob([e],{type:e.type}),a="产品分渠道订购报表.xls";if("download"in document.createElement("a")){const e=document.createElement("a");e.download=a,e.style.display="none",e.href=URL.createObjectURL(t),document.body.appendChild(e),e.click(),URL.revokeObjectURL(e.href),document.body.removeChild(e)}else navigator.msSaveBlob(t,a)}catch(e){}finally{this.exportLoading=!1}}},mounted(){this.getTableData()}},m=y,v=(a("e7a5"),Object(p["a"])(m,h,b,!1,null,"8bbe2154",null)),f=v.exports,x={name:"ReportDownload",components:{Sort:g,Channel:f},data(){return{tabActive:"a"}}},D=x,w=(a("3ae1"),Object(p["a"])(D,r,l,!1,null,"a7bb53a2",null));t["default"]=w.exports},"88a7":function(e,t,a){"use strict";var r=a("cb2d"),l=a("e330"),n=a("577e"),i=a("d6d6"),o=URLSearchParams,c=o.prototype,s=l(c.append),d=l(c["delete"]),p=l(c.forEach),u=l([].push),g=new o("a=1&a=2&b=3");g["delete"]("a",1),g["delete"]("b",void 0),g+""!=="a=2"&&r(c,"delete",(function(e){var t=arguments.length,a=t<2?void 0:arguments[1];if(t&&void 0===a)return d(this,e);var r=[];p(this,(function(e,t){u(r,{key:t,value:e})})),i(t,1);var l,o=n(e),c=n(a),g=0,h=0,b=!1,y=r.length;while(g<y)l=r[g++],b||l.key===o?(b=!0,d(this,l.key)):h++;while(h<y)l=r[h++],l.key===o&&l.value===c||s(this,l.key,l.value)}),{enumerable:!0,unsafe:!0})},b483:function(e,t,a){},ba55:function(e,t,a){"use strict";a("d8a0")},d6d6:function(e,t,a){"use strict";var r=TypeError;e.exports=function(e,t){if(e<t)throw new r("Not enough arguments");return e}},d8a0:function(e,t,a){},e7a5:function(e,t,a){"use strict";a("b483")},edd0:function(e,t,a){"use strict";var r=a("13d2"),l=a("9bf2");e.exports=function(e,t,a){return a.get&&r(a.get,t,{getter:!0}),a.set&&r(a.set,t,{setter:!0}),l.f(e,t,a)}}}]);