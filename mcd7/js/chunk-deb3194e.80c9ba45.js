(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-deb3194e"],{"0b2c":function(e,t,a){},"0e8d":function(e,t,a){"use strict";a("0b2c")},"108d":function(e,t,a){"use strict";var l=function(){var e=this,t=e._self._c;return t("div",{staticClass:"query-form-cell-wrap"},[t("el-form",{ref:"form",attrs:{model:e.sizeForm,"label-width":e.labelWidth,size:"mini"}},e._l(e.list,(function(a,l){return t("el-form-item",{key:l+"list",attrs:{label:a.label+"："}},[Array.isArray(a.values)?t("div",{staticClass:"fe-d-flex"},[t("div",{staticClass:"radio-box"},[t("el-radio-group",{attrs:{size:"mini",disabled:a.disabled},on:{change:function(t){return e.radioChange(e.sizeForm.radioModel[l],l)}},model:{value:e.sizeForm.radioModel[l],callback:function(t){e.$set(e.sizeForm.radioModel,l,t)},expression:"sizeForm.radioModel[index]"}},e._l(a.values,(function(a,l){return t("el-radio-button",{key:l+"listValues",attrs:{label:a.value}},[e._v(e._s(a.label)+" ")])})),1),a.slot?e._t("default"):e._e()],2),t("div",{staticClass:"date-box"},["date"===a.dateType?t("el-date-picker",{attrs:{type:"date",placeholder:"选择日期","value-format":"yyyy-MM-dd",clearable:!1},on:{change:e.dateChange},model:{value:e.sizeForm.dateModel[l],callback:function(t){e.$set(e.sizeForm.dateModel,l,t)},expression:"sizeForm.dateModel[index]"}}):e._e(),"month"===a.dateType?t("el-date-picker",{attrs:{type:"month",placeholder:"选择月","value-format":"yyyy-MM",clearable:!0},on:{change:e.dateChange},model:{value:e.sizeForm.dateModel[l],callback:function(t){e.$set(e.sizeForm.dateModel,l,t)},expression:"sizeForm.dateModel[index]"}}):e._e(),"daterange"===a.dateType?t("el-date-picker",{attrs:{type:"daterange","value-format":"yyyy-MM-dd","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",clearable:!0},on:{change:e.dateChange},model:{value:e.sizeForm.dateModel[l],callback:function(t){e.$set(e.sizeForm.dateModel,l,t)},expression:"sizeForm.dateModel[index]"}}):e._e(),"datetimerange"===a.dateType?t("el-date-picker",{attrs:{type:"datetimerange","value-format":"yyyy-MM-dd HH:mm:ss","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期",clearable:!1},on:{change:e.dateChange},model:{value:e.sizeForm.dateModel[l],callback:function(t){e.$set(e.sizeForm.dateModel,l,t)},expression:"sizeForm.dateModel[index]"}}):e._e()],1)]):t("div",{staticClass:"input-box"},[t("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],attrs:{placeholder:"请输入内容"},on:{change:e.inputChange},model:{value:e.sizeForm.radioModel[l],callback:function(t){e.$set(e.sizeForm.radioModel,l,t)},expression:"sizeForm.radioModel[index]"}})],1)])})),1)],1)},i=[],o={name:"QueryFormCell",props:{labelWidth:{type:String,default:()=>"85px"},defaultSizeForm:{type:Object,default:()=>({})},list:{type:Array,default:()=>[{label:"数据时间",values:[{label:"不限",value:"1"}],isDate:!0,dateType:"date"},{label:"接口类型",values:[{label:"不限",value:"不限"},{label:"文件接口",value:"文件接口"}],isDate:!1}]}},data(){return{sizeForm:{dateModel:["2021-05-08","",""],radioModel:["自定义","不限","不限"]}}},watch:{defaultSizeForm:{handler:function(e){this.sizeForm=e},deep:!0,immediate:!0}},methods:{radioChange(e,t){this.sizeForm.dateModel&&this.$set(this.sizeForm.dateModel,t,"date"===this.list[t].dateType?"":["",""]),this.$emit("queryForm",{index:t,value:e,form:this.sizeForm})},dateChange(e){e?(this.$set(this.sizeForm.dateModel,0,e),this.$emit("queryForm",{value:e,form:this.sizeForm})):this.$emit("emptyFrom",this.sizeForm)},inputChange(e){e&&this.$emit("queryForm",{value:e,form:this.sizeForm})}}},r=o,s=(a("1808"),a("2877")),n=Object(s["a"])(r,l,i,!1,null,null,null);t["a"]=n.exports},"12fc":function(e,t,a){"use strict";const l="/plan-svc/action",i="/plan-svc/api/action";t["a"]={mpapi:{getSysEnum:l+"/iop/common/getSysEnum",queryIopTemplate:l+"/iop/excellentTemplateManager/queryIopTemplate",exportStandardScene:l+"/iop/excellentTemplateManager/exportStandardScene",saveOrCancelExcellentTemplate:l+"/iop/excellentTemplateManager/saveOrCancelExcellentTemplate",uploadBeforeView:l+"/iop/excellentTemplateManager/uploadBeforeView",uploadTemplateToIop:l+"/iop/excellentTemplateManager/uploadTemplateToIop",updateDistributePersion:l+"/iop/approve/template/updateDistributePersion",queryActivityInfo:"/plan-svc/action/iop/approve/template/jx/queryActivityInfo",getActivityTemplate:l+"/iop/approve/template/getActivityTemplate",queryDistributePersion:l+"/iop/approve/template/queryDistributePersion",dismissAndRelatedCustomerGroups:i+"/tactics/tacticsManager/dismissAndRelatedCustomerGroups",getAllCustommerGroups:i+"/tactics/tacticsManager/getAllCustommerGroups",getUnityCampInfo:i+"/tactics/tacticsManager/getUnityCampInfo",getUnityTacticsDetail:i+"/tactics/tacticsManager/getUnityTacticsDetail",getWholeNetworkCampInfos:i+"/tactics/tacticsManager/getWholeNetworkCampInfos",isPopUps:i+"/tactics/tacticsManager/isPopUps",saveWholeNetwork:i+"/tactics/tacticsManager/saveWholeNetwork",searchUnityTactics:i+"/tactics/tacticsManager/jx/searchUnityTactics",unityRecomendDetail:i+"/tactics/tacticsManager/unityRecomendDetail",findMcdAutoSceneById:l+"/mcdAutoModel/findMcdAutoSceneById",getProduct:l+"/mcdAutoModel/getProduct",deleteMcdAutoSceneDef:l+"/mcdAutoModel/deleteMcdAutoSceneDef",findMcdAutoSceneByTable:l+"/mcdAutoModel/findMcdAutoSceneByTable",findMcdAutoSceneList:l+"/mcdAutoModel/findMcdAutoSceneList",queryChannelAdivList:l+"/mcdAutoModel/queryChannelAdivList",queryProductList:l+"/mcdAutoModel/queryProductList",querySmsChannelCount:l+"/mcdAutoModel/querySmsChannelCount",quoteMcdAutoModel:l+"/mcdAutoModel/quoteMcdAutoModel",saveMcdAutoSceneDef:l+"/mcdAutoModel/saveMcdAutoSceneDef",updateMcdAutoModel:l+"/mcdAutoModel/updateMcdAutoModel",updateMcdAutoSceneDef:l+"/mcdAutoModel/updateMcdAutoSceneDef",updateMcdAutoSceneDefStatId:l+"/mcdAutoModel/updateMcdAutoSceneDefStatId",queryTopics:l+"/topic/queryTopics",tacticsCreateQueryIopTemplate:i+"/tactics/tacticsCreate/queryIopTemplate",quickCreateTacticsByIopTemplate:i+"/tactics/quickTactics/quickCreateTacticsByIopTemplate",qucickApprove:i+"/tactics/quickTactics/qucickApprove"}}},1808:function(e,t,a){"use strict";a("4bb9")},"271a":function(e,t,a){"use strict";var l=a("cb2d"),i=a("e330"),o=a("577e"),r=a("d6d6"),s=URLSearchParams,n=s.prototype,c=i(n.getAll),u=i(n.has),d=new s("a=1");!d.has("a",2)&&d.has("a",void 0)||l(n,"has",(function(e){var t=arguments.length,a=t<2?void 0:arguments[1];if(t&&void 0===a)return u(this,e);var l=c(this,e);r(t,1);var i=o(a),s=0;while(s<l.length)if(l[s++]===i)return!0;return!1}),{enumerable:!0,unsafe:!0})},"357d":function(e,t,a){},"3feb":function(e,t,a){"use strict";a("8e3f")},"41e1":function(e,t,a){},"4bb9":function(e,t,a){},"53ff":function(e,t,a){},5494:function(e,t,a){"use strict";var l=a("83ab"),i=a("e330"),o=a("edd0"),r=URLSearchParams.prototype,s=i(r.forEach);l&&!("size"in r)&&o(r,"size",{get:function(){var e=0;return s(this,(function(){e++})),e},configurable:!0,enumerable:!0})},"6f3a":function(e,t,a){},7276:function(e,t,a){"use strict";a("d6a6")},"7dfc":function(e,t,a){"use strict";a("864d")},"864d":function(e,t,a){},"889e":function(e,t,a){"use strict";a("357d")},"88a7":function(e,t,a){"use strict";var l=a("cb2d"),i=a("e330"),o=a("577e"),r=a("d6d6"),s=URLSearchParams,n=s.prototype,c=i(n.append),u=i(n["delete"]),d=i(n.forEach),p=i([].push),m=new s("a=1&a=2&b=3");m["delete"]("a",1),m["delete"]("b",void 0),m+""!=="a=2"&&l(n,"delete",(function(e){var t=arguments.length,a=t<2?void 0:arguments[1];if(t&&void 0===a)return u(this,e);var l=[];d(this,(function(e,t){p(l,{key:t,value:e})})),r(t,1);var i,s=o(e),n=o(a),m=0,y=0,v=!1,b=l.length;while(m<b)i=l[m++],v||i.key===s?(v=!0,u(this,i.key)):y++;while(y<b)i=l[y++],i.key===s&&i.value===n||c(this,i.key,i.value)}),{enumerable:!0,unsafe:!0})},"8e3f":function(e,t,a){},9107:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e._self._c;return t("div",{staticClass:"product-container"},[t("el-tabs",{attrs:{type:"card"},on:{"tab-click":e.tabsClick},model:{value:e.tabActive,callback:function(t){e.tabActive=t},expression:"tabActive"}},[t("el-tab-pane",{attrs:{label:"集团模板",name:"product"}},[t("group-template")],1),t("el-tab-pane",{attrs:{label:"省级模版",name:"content"}},[t("provincial-template")],1),t("el-tab-pane",{attrs:{label:"自动化场景策略模板",name:"scene"}},[t("AutoScence")],1)],1)],1)},i=[],o=function(){var e=this,t=e._self._c;return t("div",{staticClass:"product-container"},[t("div",[t("div",{staticClass:"selech-panel"},[t("el-form",{ref:"queryForm",staticStyle:{display:"flex","flex-wrap":"wrap"},attrs:{model:e.queryForm,"label-width":"120px"}},[t("el-form-item",{attrs:{label:"策略模板编号：","label-width":"120px"}},[t("el-input",{staticStyle:{width:"187px"},attrs:{placeholder:"请输入策略模板编号"},model:{value:e.queryForm.activityTemplateId,callback:function(t){e.$set(e.queryForm,"activityTemplateId",t)},expression:"queryForm.activityTemplateId"}})],1),t("el-form-item",{attrs:{label:"营销活动名称/编号：","label-width":"160px"}},[t("el-input",{staticStyle:{width:"187px"},attrs:{placeholder:"请输入营销活动名称或编号"},model:{value:e.queryForm.activityId,callback:function(t){e.$set(e.queryForm,"activityId",t)},expression:"queryForm.activityId"}})],1),t("el-form-item",{attrs:{label:"创建时间：","label-width":"160px"}},[t("el-date-picker",{attrs:{type:"daterange","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期","value-format":"yyyy-MM-dd"},model:{value:e.queryForm.date,callback:function(t){e.$set(e.queryForm,"date",t)},expression:"queryForm.date"}})],1),t("el-form-item",{attrs:{label:"活动类型：","label-width":"120px"}},[t("el-select",{staticStyle:{width:"187px"},attrs:{placeholder:"请选择"},model:{value:e.queryForm.activityType,callback:function(t){e.$set(e.queryForm,"activityType",t)},expression:"queryForm.activityType"}},e._l(e.QueryList[1].values,(function(e){return t("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1)],1),t("el-form-item",{attrs:{label:"策略状态：","label-width":"160px"}},[t("el-select",{staticStyle:{width:"187px"},attrs:{placeholder:"请选择"},model:{value:e.queryForm.activityStatus,callback:function(t){e.$set(e.queryForm,"activityStatus",t)},expression:"queryForm.activityStatus"}},e._l(e.QueryList[2].values,(function(e){return t("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1)],1),t("el-form-item",{attrs:{label:"所属流程：","label-width":"160px"}},[t("el-select",{staticStyle:{width:"187px"},attrs:{clearable:"",placeholder:"请选择"},model:{value:e.queryForm.flow,callback:function(t){e.$set(e.queryForm,"flow",t)},expression:"queryForm.flow"}},e._l(e.flowOptions,(function(e){return t("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1)],1),t("div",{staticStyle:{"margin-left":"30px"}},[t("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.queryActivityInfo()}}},[e._v("查询")]),t("el-button",{attrs:{icon:"el-icon-refresh"},on:{click:e.resetQueryForm}},[e._v("重置")])],1)],1)],1),t("div",[t("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],staticStyle:{width:"100%"},attrs:{data:e.tableData,"row-class-name":e.tableRowClassName,height:"calc(100vh - 400px)"}},[t("el-table-column",{attrs:{prop:"activityTemplateId",label:"策划模板编号"}}),t("el-table-column",{attrs:{prop:"activityId",label:"营销策略编号","show-overflow-tooltip":""}}),t("el-table-column",{attrs:{prop:"activityName",label:"营销策略名称",width:"250px"}}),t("el-table-column",{attrs:{prop:"activityTypeName",label:"策略类型"}}),t("el-table-column",{attrs:{prop:"activityStatusName",label:"策略状态"}}),t("el-table-column",{attrs:{prop:"flow",label:"所属流程"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.flowName.label)+" ")]}}])}),t("el-table-column",{attrs:{prop:"activityStartTime",label:"开始日期"}}),t("el-table-column",{attrs:{prop:"activityEndTime",label:"结束日期"}}),t("el-table-column",{attrs:{label:"是否使用"},scopedSlots:e._u([{key:"default",fn:function(a){return[0==a.row.status?t("span",[e._v("否")]):t("span",[e._v("是")])]}}])}),t("el-table-column",{attrs:{prop:"distributePerson",label:"分配人员"}}),t("el-table-column",{attrs:{label:"操作",fixed:"right",width:"180"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-button",{attrs:{type:"text"},on:{click:function(t){return e.goPush(a.row)}}},[e._v("查看")]),0==a.row.status?t("el-button",{attrs:{type:"text"},on:{click:function(t){return e.distributePersion(a.row)}}},[e._v("分配人员")]):e._e(),23==a.row.flow&&0==a.row.status?t("el-button",{attrs:{type:"text"},on:{click:function(t){return e.createTemplate(a.row)}}},[e._v("一键生成")]):e._e()]}}])})],1)],1)]),t("div",{staticClass:"pagination"},[t("el-pagination",{attrs:{"current-page":e.queryForm.pageNum,layout:"total, prev, pager, next, jumper",total:e.Total},on:{"current-change":e.handleCurrentChange}})],1),t("el-dialog",{attrs:{title:"分配人员",visible:e.dialogVisible,width:"30%"},on:{"update:visible":function(t){e.dialogVisible=t}}},[t("el-select",{attrs:{filterable:"",placeholder:"请选择分配人员"},model:{value:e.distributePersionID,callback:function(t){e.distributePersionID=t},expression:"distributePersionID"}},e._l(e.options,(function(a){return t("el-option",{key:a.userId,attrs:{label:a.userName,value:a.userId,"before-close":e.handleClose}})})),1),t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(t){e.dialogVisible=!1}}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.saveDistributePersion}},[e._v("确 定")])],1)],1)],1)},r=[],s=(a("14d9"),a("0fea")),n=(a("d370"),a("12fc")),c=a("108d"),u={components:{QueryFormCell:c["a"]},data(){return{QueryList:[{label:"创建时间",values:[],isDate:!0,dateType:"daterange",leftCol:1,rightCol:19},{label:"策略类型",values:[{label:"不限",value:""}],isDate:!1},{label:"策略状态",values:[{label:"不限",value:""}],isDate:!1}],sizeForm:{dateModel:[["",""]],radioModel:["","",""]},tableData:[],queryForm:{pageNum:1,activityTemplateId:"",activityId:"",activityStatus:"",activityType:"",activityStartTime:"",activityEndTime:"",date:[],flow:"",size:10},Total:0,dialogVisible:!1,distributePersionID:"",options:[],activityTemplateId:"",loading:!1,flowOptions:[{label:"一级策划省级执行",value:"1"},{label:"省级策划一级执行-互联网",value:"2"},{label:"省级策划省级执行",value:"3"},{label:"一级策划一点部署-一级电渠",value:"4"},{label:"一级策划一点部署-互联网",value:"5"},{label:"一级策划一点部署-省级播控平台",value:"6"},{label:"一级策划一点部署-咪咕",value:"7"},{label:"省级策划一级执行-电渠",value:"8"},{label:"省级策划一级执行-咪咕",value:"9"},{label:"省级策划一级执行-爱流量",value:"10"},{label:"一级策划省级执行-一级精准推荐",value:"23"},{label:"一级策划一点部署",value:"98"},{label:"其他 ",value:"99"}]}},computed:{},created(){this.queryIopActivityType(),this.queryIopActivityStatus(),this.queryActivityInfo()},methods:{async queryIopActivityType(){await Object(s["i"])(n["a"].mpapi.getSysEnum,{enumType:"iop_activity_type",parentId:""}).then(e=>{e.map(e=>{this.QueryList[1].values.push({label:e.enumValue,value:e.enumKey})})})},async queryIopActivityStatus(){await Object(s["i"])(n["a"].mpapi.getSysEnum,{enumType:"op_activity_status",parentId:""}).then(e=>{e.map(e=>{this.QueryList[2].values.push({label:e.enumValue,value:e.enumKey})})})},async queryActivityInfo(){this.loading=!0;const e={size:this.queryForm.size,current:this.queryForm.pageNum,activityEndTime:this.queryForm.date[1],activityStartTime:this.queryForm.date[0],activityStatus:this.queryForm.activityStatus,activityTemplateId:this.queryForm.activityTemplateId,activityType:this.queryForm.activityType,flow:this.queryForm.flow,activityId:this.queryForm.activityId};await Object(s["i"])(n["a"].mpapi.queryActivityInfo,e).then(e=>{this.loading=!1;const t=e.records.map(e=>{const t=this.flowOptions.filter(t=>t.value===e.flow);return{...e,flowName:t[0]||{label:""}}});this.tableData=t,this.Total=e.total})},queryfrom(e){this.queryForm.activityStartTime=e.form.dateModel[0][0],this.queryForm.activityEndTime=e.form.dateModel[0][1],this.queryForm.activityType=e.form.radioModel[1],this.queryForm.activityStatus=e.form.radioModel[2],this.queryActivityInfo()},goPush(e){this.$router.push({path:"/MarketingPlanning/GroupDetails",query:{activityId:e.activityId}})},tableRowClassName({row:e,rowIndex:t}){const a=t%2;return 0===a?"gray-row":""},handleCurrentChange(e){this.queryForm.pageNum=e,this.queryActivityInfo()},async distributePersion(e){this.activityTemplateId=e.activityTemplateId,this.dialogVisible=!0,await Object(s["i"])(n["a"].mpapi.queryDistributePersion,{userInfo:""}).then(e=>{this.options=e})},async saveDistributePersion(){this.distributePersionID?await Object(s["i"])(n["a"].mpapi.updateDistributePersion,{userId:this.distributePersionID,activityTemplateId:this.activityTemplateId}).then(e=>{this.$message.success("操作成功"),this.queryActivityInfo(),this.dialogVisible=!1}):this.$message.error("请选择分配人员")},handleClose(){this.activityTemplateId=""},resetQueryForm(){this.queryForm={pageNum:1,activityTemplateId:"",activityId:"",activityStatus:"",activityType:"",activityStartTime:"",activityEndTime:"",date:[],flow:"",size:10},this.queryActivityInfo()},async createTemplate(e){const t=this.$loading({lock:!0,text:"正在生成中...",spinner:"el-icon-loading",background:"rgba(0, 0, 0, 0.0)"});try{const a=await Object(s["i"])(n["a"].mpapi.quickCreateTacticsByIopTemplate+"?activityId="+e.activityId+"&templateId="+e.activityTemplateId);this.$router.push({path:"/MarketingPlanning/MarketingDetails",query:{campsegRootId:a,backName:this.$router.currentRoute.name}})}catch(a){}finally{t.close()}}}},d=u,p=(a("889e"),a("c788"),a("2877")),m=Object(p["a"])(d,o,r,!1,null,"dda77460",null),y=m.exports,v=function(){var e=this,t=e._self._c;return t("div",[t("div",{staticClass:"product-container"},[t("div",{staticClass:"selech-panel"},[t("query-form-cell",{attrs:{list:e.QueryFormcontent,defaultSizeForm:e.sizeForm},on:{queryForm:e.search}}),t("div",{staticClass:"search-pddin"},[t("el-input",{staticStyle:{width:"320px"},attrs:{placeholder:"请输入策划名称或编号",size:"small"},model:{value:e.queryForm.keyWords,callback:function(t){e.$set(e.queryForm,"keyWords",t)},expression:"queryForm.keyWords"}},[t("i",{staticClass:"el-input__icon el-icon-search",attrs:{slot:"suffix"},on:{click:e.queryIopTemplate},slot:"suffix"})])],1)],1),t("div",[t("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],staticStyle:{width:"100%"},attrs:{data:e.ProvincialTable,"row-class-name":e.tableRowClassName,height:"calc(100vh - 331px)"}},[t("el-table-column",{attrs:{label:"序号",type:"index"}}),t("el-table-column",{attrs:{prop:"",label:"策略编号及名称",width:"400px"},scopedSlots:e._u([{key:"default",fn:function(l){return["1"==l.row.excellentCase?t("img",{staticStyle:{width:"12px",height:"12px"},attrs:{src:a("d4f7"),alt:""}}):e._e(),t("el-tooltip",{staticClass:"item",attrs:{effect:"dark",content:l.row.campsegIdWithName,placement:"top"}},[t("el-link",{staticStyle:{overflow:"hidden"},attrs:{type:"primary"},on:{click:function(t){return e.goDetail(l.row.campsegId)}}},[e._v(" "+e._s(l.row.campsegIdWithName)+" ")])],1)]}}])}),t("el-table-column",{attrs:{prop:"expireDate",label:"执行周期",width:"200px"}}),t("el-table-column",{attrs:{prop:"campsegStatDesc",label:"状态"}}),t("el-table-column",{attrs:{label:"是否是优秀案例"},scopedSlots:e._u([{key:"default",fn:function(a){return["1"==a.row.excellentCase?t("span",[e._v("是")]):t("span",[e._v("否")])]}}])}),t("el-table-column",{attrs:{label:"是否已上传"},scopedSlots:e._u([{key:"default",fn:function(a){return["1"==a.row.uploaded?t("span",[e._v("是")]):t("span",[e._v("否")])]}}])}),t("el-table-column",{attrs:{fixed:"right",prop:"address",label:"操作",width:"200px"},scopedSlots:e._u([{key:"default",fn:function(a){return["1"!==a.row.excellentCase?t("el-button",{attrs:{type:"text"},on:{click:function(t){return e.ExcellentCasesSub(a.row)}}},[e._v("生成优秀案例")]):t("el-button",{attrs:{type:"text"},on:{click:function(t){return e.ExcellentCasescancel(a.row)}}},[e._v("取消优秀案例")]),t("el-button",{attrs:{type:"text",disabled:"1"!==a.row.excellentCase||"1"==a.row.uploaded},on:{click:function(t){return e.upload(a.row)}}},[e._v("上传")])]}}])})],1)],1)]),t("div",{staticClass:"pagination"},[t("el-pagination",{attrs:{"current-page":e.queryForm.pageNum,layout:"total, prev, pager, next, jumper",total:e.Total},on:{"current-change":e.handleCurrentChange}})],1),t("div",[t("template-case",{ref:"templateCase",attrs:{name:e.templateCase.name,title:e.templateCase.title,titleName:e.templateCase.titleName}}),t("el-dialog",{attrs:{title:"上报详情",visible:e.dialogVisible,width:"933px","append-to-body":""},on:{"update:visible":function(t){e.dialogVisible=t}}},[t("div",{staticStyle:{width:"100%"}},[e.verticalFlag?t("vertical-table",{attrs:{tableData:e.Detailstable,DetailsFrom:e.detailsFrom,optionKeyName:"campsegName"}}):e._e()],1),t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:e.cancelUpload}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:e.Submit}},[e._v("上 传")])],1)])],1)])},b=[],h=(a("88a7"),a("271a"),a("5494"),a("4d42")),f=function(){var e=this,t=e._self._c;return t("div",[t("el-dialog",{attrs:{title:"系统提示",visible:e.dialogVisible,width:"412px",top:"25vh","append-to-body":"","close-on-click-modal":""},on:{"update:visible":function(t){e.dialogVisible=t}}},[t("div",{staticClass:"Dialog-center"},[t("p",[e._v(e._s(e.titleName))]),t("span",[e._v(" ["+e._s(e.name)+"]")]),t("p",[e._v(e._s(e.title))])]),t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{attrs:{type:"info"},on:{click:function(t){e.dialogVisible=!1}}},[e._v("关 闭")]),t("el-button",{attrs:{type:"primary"},on:{click:e.DleteCallback}},[e._v("确 定")])],1)])],1)},g=[],w={name:"DleteDialog",props:{titleName:{type:String,default:()=>"已将"},name:String,title:String},data(){return{dialogVisible:!1}},methods:{showTempalte(){this.dialogVisible=!0},DleteCallback(){this.$emit("Dlete",!0),this.dialogVisible=!1}}},F=w,x=(a("beca"),a("7dfc"),Object(p["a"])(F,f,g,!1,null,"6b2d5bdc",null)),T=x.exports,k=a("9e21"),C={components:{QueryFormCell:c["a"],TemplateCase:T,VerticalTable:k["a"]},data(){return{tableData:[],QueryFormcontent:[{label:"类型",values:[{label:"我的策略",value:"0"},{label:"优秀案例",value:"1"}],isDate:!1}],ProvincialTable:[],sizeForm:{radioModel:["0"]},templateCase:{name:"",title:"设为优秀案例",titleName:"已将"},tabActive:"product",option:"",currentPage:1,queryForm:{keyWords:"",queryStatus:"0",current:1,size:10},Total:0,detailsFrom:[],Detailstable:[{key:"上报人：",value:"createUserId"},{key:"营销活动类型：",value:"activityTypeName"},{key:"上报时间：",value:"createTime"},{key:"营销活动描述：",value:"campsegDesc"},{key:"策略开始时间：",value:"startTime"},{key:"营销活动目的：",value:"dest"},{key:"策略结束时间：",value:"endTime"},{key:"地市：",value:"cityName",iscolspan:!0},{key:"营销活动编号：",value:"campsegId",iscolspan:!0},{key:"营销活动名称：",value:"campsegName",iscolspan:!0}],dialogVisible:!1,campsegId:"",loading:!1,verticalFlag:!1}},computed:{QueryFormCell(){let e=[...this.QueryList];return e}},created(){this.queryIopTemplate()},methods:{async queryIopTemplate(){this.loading=!0,await Object(s["i"])(n["a"].mpapi.queryIopTemplate,this.queryForm).then(e=>{this.loading=!1,this.ProvincialTable=e.records,this.Total=e.total})},async Submit(){await Object(s["i"])(n["a"].mpapi.uploadTemplateToIop,{uploadFlag:"1",campsegId:this.campsegId}).then(e=>{"ERROR"===e.status?this.$message.error(e.message):(this.$message({type:"success",message:"上传成功"}),this.dialogVisible=!1,this.campsegId="",this.verticalFlag=!1)})},cancelUpload(){this.dialogVisible=!1,this.campsegId="",this.verticalFlag=!1},async upload(e){this.dialogVisible=!0,this.campsegId=e.campsegId,await Object(s["i"])(n["a"].mpapi.uploadBeforeView,{campsegId:e.campsegId,uploadFlag:"0"}).then(e=>{this.detailsFrom=e,this.verticalFlag=!0})},async createOrDeleteExcellentTemplate(e,t,a){let l={campsegId:e,campsegName:t,excellentFlag:a};await Object(s["i"])(n["a"].mpapi.saveOrCancelExcellentTemplate,l).then(e=>{this.templateCase={...this.templateCase,name:t},!0===e&&(this.$refs["templateCase"].showTempalte(),this.queryIopTemplate())})},search(e){this.queryForm.queryStatus=e.value,this.queryIopTemplate()},handleCurrentChange(e){this.queryForm.current=e,this.queryIopTemplate()},ExcellentCasesSub(e){this.templateCase.name=e.name,this.templateCase.title="设为优秀案例",this.templateCase.titleName="已将",this.createOrDeleteExcellentTemplate(e.campsegId,e.campsegIdWithName,1)},ExcellentCasescancel(e){this.templateCase.name=e.name,this.templateCase.title="为优秀案例",this.templateCase.titleName="已取消",this.createOrDeleteExcellentTemplate(e.campsegId,e.campsegIdWithName,0)},tabsClick(e){this.currentPage=1,e.name},tableRowClassName({row:e,rowIndex:t}){const a=t%2;return 0===a?"gray-row":""},async deriveOutstandingCase(){await Object(h["a"])({url:n["a"].mpapi.exportStandardScene,responseType:"blob"}).then(e=>{if(200===e.status){let t=window.URL.createObjectURL(new Blob([e.data],{type:"application/x-msdownload"})),a=document.createElement("a");a.style.display="none",a.href=t,a.setAttribute("download","优秀案例列表.xls"),document.body.appendChild(a),a.click(),window.URL.revokeObjectURL(t)}})},goDetail(e){this.$router.push({path:"/MarketingPlanning/MarketingDetails",query:{campsegRootId:e,backName:this.$router.currentRoute.name,tab:2}})}}},I=C,A=(a("7276"),a("c0ae"),Object(p["a"])(I,v,b,!1,null,"26265742",null)),q=A.exports,S=function(){var e=this,t=e._self._c;return t("div",{staticClass:"product-container"},[t("div",[t("div",{staticClass:"selech-panel"},[t("el-form",{ref:"queryForm",attrs:{model:e.queryForm,"label-width":"120px"}},[t("el-row",[t("el-col",{attrs:{span:8}},[t("el-form-item",{attrs:{label:"策划模板编号：","label-width":"120px"}},[t("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],staticClass:"input-search",staticStyle:{width:"80%"},attrs:{placeholder:"请输入策划模板编号"},model:{value:e.queryForm.value1,callback:function(t){e.$set(e.queryForm,"value1",t)},expression:"queryForm.value1"}})],1)],1),t("el-col",{attrs:{span:8}},[t("el-form-item",{attrs:{label:"策划模板名称：","label-width":"160px"}},[t("el-input",{staticStyle:{width:"95%"},attrs:{placeholder:"请输入策划模板名称"},model:{value:e.queryForm.value2,callback:function(t){e.$set(e.queryForm,"value2",t)},expression:"queryForm.value2"}})],1)],1),t("el-col",{attrs:{span:6}},[t("el-form-item",{attrs:{label:"活动状态：","label-width":"160px"}},[t("el-select",{staticStyle:{width:"159%"},attrs:{placeholder:"请选择"},model:{value:e.queryForm.value,callback:function(t){e.$set(e.queryForm,"value",t)},expression:"queryForm.value"}},e._l(e.options,(function(e){return t("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1)],1)],1),t("el-col",{attrs:{span:10}},[t("el-form-item",{attrs:{label:"创建时间：","label-width":"120px"}},[t("el-date-picker",{staticStyle:{width:"86%"},attrs:{type:"daterange","range-separator":"至","start-placeholder":"开始日期","end-placeholder":"结束日期"},model:{value:e.queryForm.value1,callback:function(t){e.$set(e.queryForm,"value1",t)},expression:"queryForm.value1"}})],1)],1),t("el-col",{attrs:{span:4}},[t("el-button",{attrs:{type:"primary"}},[e._v("查询")])],1)],1)],1)],1),t("div",[t("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],staticStyle:{width:"100%"},attrs:{data:e.tableData,"row-class-name":e.tableRowClassName,height:"calc(100vh - 400px)"}},[t("el-table-column",{attrs:{prop:"activityTemplateId",label:"策划模板编号"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-link",{attrs:{type:"primary",href:"#/MarketingPlanning/AutoScenceDetails?campsegId="+a.row.activityTemplateId}},[e._v(" "+e._s(a.row.activityTemplateId)+" ")])]}}])}),t("el-table-column",{attrs:{prop:"activityId",label:"营销策略编号"}}),t("el-table-column",{attrs:{prop:"activityName",label:"营销策略名称"}}),t("el-table-column",{attrs:{prop:"activityTypeName",label:"策略模板描述"}}),t("el-table-column",{attrs:{prop:"activityStatusName",label:"营销应用范围"}}),t("el-table-column",{attrs:{prop:"activityStartTime",label:"营销策略类型"}}),t("el-table-column",{attrs:{prop:"activityEndTime",label:"营销模板状态"}}),t("el-table-column",{attrs:{prop:"activityEndTime",label:"营销生效时间"}}),t("el-table-column",{attrs:{prop:"activityEndTime",label:"营销失效时间"}}),t("el-table-column",{attrs:{label:"操作",fixed:"right",width:"120"},scopedSlots:e._u([{key:"default",fn:function(a){return[t("el-button",{attrs:{type:"text"},on:{click:function(t){e.dialogVisible=!0}}},[e._v("修改")]),t("el-button",{attrs:{type:"text"},on:{click:function(t){return e.showDelDialog(1)}}},[e._v("删除")]),t("el-button",{attrs:{type:"text"},on:{click:function(t){return e.showDelDialog(2)}}},[e._v("引用")])]}}])})],1)],1)]),t("div",{staticClass:"pagination"},[t("el-pagination",{attrs:{"current-page":e.queryForm.pageNum,layout:"total, prev, pager, next, jumper",total:e.Total},on:{"current-change":e.handleCurrentChange}})],1),t("el-dialog",{attrs:{title:"修改自动化模板信息",visible:e.dialogVisible,width:"40%"},on:{"update:visible":function(t){e.dialogVisible=t}}},[t("el-form",{ref:"updateFrom",attrs:{model:e.updateFrom,"label-width":"180px"}},[t("el-row",[t("el-col",{attrs:{span:24}},[t("el-form-item",{attrs:{label:"策划模板名称："}},[t("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],staticClass:"input-search",staticStyle:{width:"220px"},attrs:{placeholder:"请输入策划模板名称"},model:{value:e.updateFrom.value1,callback:function(t){e.$set(e.updateFrom,"value1",t)},expression:"updateFrom.value1"}})],1)],1),t("el-col",{attrs:{span:24}},[t("el-form-item",{attrs:{label:"营销活动描述："}},[t("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],staticClass:"input-search",staticStyle:{width:"220px"},attrs:{placeholder:"请输入策划活动描述"},model:{value:e.updateFrom.value1,callback:function(t){e.$set(e.updateFrom,"value1",t)},expression:"updateFrom.value1"}})],1)],1),t("el-col",{attrs:{span:24}},[t("el-form-item",{attrs:{label:"开始日期："}},[t("el-date-picker",{attrs:{type:"date",placeholder:"选择日期"},model:{value:e.updateFrom.value1,callback:function(t){e.$set(e.updateFrom,"value1",t)},expression:"updateFrom.value1"}})],1)],1),t("el-col",{attrs:{span:24}},[t("el-form-item",{attrs:{label:"结束日期："}},[t("el-date-picker",{attrs:{type:"date",placeholder:"选择日期"},model:{value:e.updateFrom.value1,callback:function(t){e.$set(e.updateFrom,"value1",t)},expression:"updateFrom.value1"}})],1)],1),t("el-col",{attrs:{span:24}},[t("el-form-item",{attrs:{label:"营销应用范围："}},[t("el-input",{directives:[{name:"clear-emoij",rawName:"v-clear-emoij"}],staticClass:"input-search",staticStyle:{width:"220px"},attrs:{placeholder:"请输入营销应用范围"},model:{value:e.updateFrom.value1,callback:function(t){e.$set(e.updateFrom,"value1",t)},expression:"updateFrom.value1"}})],1)],1)],1)],1),t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(t){e.dialogVisible=!1}}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:function(t){e.dialogVisible=!1}}},[e._v("确 定")])],1)],1),t("el-dialog",{attrs:{visible:e.delVisible,width:"40%"},on:{"update:visible":function(t){e.delVisible=t}}},[1===e.delOrQuote?t("div",{staticClass:"centerText"},[e._v(" 是否确认删除此自动化场景策略模版？ ")]):t("div",{staticClass:"centerText"},[e._v(" 是否确认调用此自动化场景策略模版？ ")]),t("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[t("el-button",{on:{click:function(t){e.delVisible=!1}}},[e._v("取 消")]),t("el-button",{attrs:{type:"primary"},on:{click:function(t){e.delVisible=!1}}},[e._v("确 定")])],1)])],1)},M=[],_={data(){return{queryForm:{pageNum:1},tableData:[{activityTemplateId:"11111",activityId:"2222",activityName:"测试名称",activityTypeName:"abc",activityStatusName:"全范围",activityStartTime:"2022-11-01",activityEndTime:"2022-11-29"},{activityTemplateId:"11111",activityId:"2222",activityName:"测试名称",activityTypeName:"abc",activityStatusName:"全范围",activityStartTime:"2022-11-01",activityEndTime:"2022-11-29"}],Total:1,dialogVisible:!1,updateFrom:{},options:[],loading:!1,delVisible:!1,delOrQuote:""}},methods:{tableRowClassName({row:e,rowIndex:t}){const a=t%2;return 0===a?"gray-row":""},handleCurrentChange(){},showDelDialog(e){this.delOrQuote=e,this.delVisible=!0}}},D=_,N=(a("0e8d"),a("3feb"),Object(p["a"])(D,S,M,!1,null,"4492b49a",null)),z=N.exports,O={components:{GroupTemplate:y,ProvincialTemplate:q,AutoScence:z},data(){return{tabActive:"product"}},computed:{},methods:{tabsClick(){}},created(){var e;null!==(e=this.$route.params)&&void 0!==e&&e.tab&&(this.tabActive=2==this.$route.params.tab?"content":3==this.$route.params.tab?"scene":"product")}},V=O,P=(a("df84"),a("e227"),Object(p["a"])(V,l,i,!1,null,"70049e93",null));t["default"]=P.exports},"9e21":function(e,t,a){"use strict";var l=function(){var e=this,t=e._self._c;return t("div",{staticClass:"mailTable-layer",class:e.showByRow?"fs12":"",staticStyle:{"padding-top":"30px"}},[t("el-select",{staticStyle:{"margin-bottom":"20px"},attrs:{placeholder:"请选择查看策略"},model:{value:e.DetailsFromIndex,callback:function(t){e.DetailsFromIndex=t},expression:"DetailsFromIndex"}},e._l(e.optionList,(function(e){return t("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1),t("div",{staticClass:"mailTable",style:e.styleObject},e._l(e.rowCount,(function(a,l){return t("el-row",{key:l},[e.isAarray(a)?t("el-row",{staticClass:"ArrayMailTable"},e._l(a,(function(a,l){return t("el-col",{key:l,staticClass:"ArrayMailTable-row",attrs:{span:12}},[t("el-col",{staticClass:"key",attrs:{span:8}},[e._v(e._s(a.key))]),t("el-col",{staticClass:"value",attrs:{span:16}},[e._v(e._s(e.DetailsFromItem[a.value]))])],1)})),1):t("el-row",[t("el-col",{staticClass:"ArrayMailTable",attrs:{span:24}},[t("el-col",{staticClass:"key",attrs:{span:4}},[e._v(e._s(a.key))]),t("el-col",{staticClass:"value1",attrs:{span:20}},[e._v(e._s(e.DetailsFromItem[a.value]))])],1)],1)],1)})),1)],1)},i=[],o=(a("14d9"),{data(){return{styleObject:{},s_showByRow:!0,DetailsFromIndex:0}},props:["tableData","tableStyle","showByRow","DetailsFrom","optionKeyName"],computed:{isAarray:function(){return e=>Array.isArray(e)},optionList(){let e=[];return this.DetailsFrom.map((t,a)=>{e.push({label:t[this.optionKeyName],value:a})}),e},DetailsFromItem(){return this.DetailsFrom[this.DetailsFromIndex]},rowCount:function(){let e=[],t=[...this.tableData];for(let a=0;a<t.length;a++){let l=[];t[a].iscolspan||(l.push(t[a]),a+1<t.length&&!t[a+1].iscolspan&&(l.push(t[a+1]),a++)),0!==l.length?(l.length<2&&l.push([]),e.push(l)):e.push(t[a])}return e}},methods:{},created(){this.styleObject=this.tableStyle,void 0!==this.showByRow&&(this.s_showByRow=this.showByRow)}}),r=o,s=(a("cafa"),a("2877")),n=Object(s["a"])(r,l,i,!1,null,"15ce141f",null);t["a"]=n.exports},abaf:function(e,t,a){},b687:function(e,t,a){},b7e1:function(e,t,a){},beca:function(e,t,a){"use strict";a("b687")},c0ae:function(e,t,a){"use strict";a("abaf")},c788:function(e,t,a){"use strict";a("b7e1")},cafa:function(e,t,a){"use strict";a("6f3a")},d4f7:function(e,t){e.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAPVJREFUOE+lk8ENgkAQRf+ECkzWO1YAHYCVKGXsSTxtGWolaAdQgd7ZxArMmI+uIZhIkDmxmb/vDzszgpkhY/dT15YK2Qj0VNtlOdT/BKSuzRWyE0SF4lE11qwmAvwW0JjOifPaWPNl+LOCxPmrIFrTVfE4NNZ03/34AFJHtxAaKyQT4FRbc2ROAb7DnoraLs9B2QGCAMANQA7gLIj2tV3wHPJZzzlnZcy/AW0JyI1uifMVnfouw7LZGZpQ8xcgcf4gwIWGMwCvX/wX0HVnDuAzE5MreHcsa6wp+LiTAKm7xxyofpcCgDNfjS0W52S4VKPbOAZ9Av0TtRGHhvvtAAAAAElFTkSuQmCC"},d6a6:function(e,t,a){},d6d6:function(e,t,a){"use strict";var l=TypeError;e.exports=function(e,t){if(e<t)throw new l("Not enough arguments");return e}},df84:function(e,t,a){"use strict";a("41e1")},e227:function(e,t,a){"use strict";a("53ff")},edd0:function(e,t,a){"use strict";var l=a("13d2"),i=a("9bf2");e.exports=function(e,t,a){return a.get&&l(a.get,t,{getter:!0}),a.set&&l(a.set,t,{setter:!0}),i.f(e,t,a)}}}]);