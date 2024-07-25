(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-841a15b0"],{1407:function(e,t,a){"use strict";a.r(t);var n=function(){var e=this,t=e._self._c;return t("div",{staticClass:"aihelper-layout"},[e.saveLoading?t("Loader"):e._e(),t("el-tabs",{model:{value:e.activeName,callback:function(t){e.activeName=t},expression:"activeName"}},[t("el-tab-pane",{attrs:{label:"AI辅助推荐",name:"AI"}},[e.isProductGuide?t("SubSection",{attrs:{title:"客群范围："}},[t("section",{staticClass:"scope-content no-padding"},[t("SelectCustomer",{ref:"customerRef",attrs:{node:e.node,parentInfo:e.parentInfo,initialData:e.initialData.customerSection||{}}})],1)]):t("SubSection",{attrs:{title:"产品范围："}},[t("section",{staticClass:"scope-content no-padding"},[t("SelectProduct",{ref:"productRef",attrs:{parentInfo:e.parentInfo,initialData:e.initialData.productSection||{}}})],1)]),t("SubSection",{ref:"channelRuleRef",attrs:{title:"渠道范围："}},[t("section",{staticClass:"scope-content"},[t("SelectChannel",{ref:"channelRef",attrs:{initialData:e.initialData.channelSection}})],1)]),t("SubSection",{ref:"aiRuleRef",attrs:{title:"AI模型参数："}},[t("section",{staticClass:"scope-content"},[t("AiModel",{ref:"aiRef",attrs:{initialData:e.initialData.aiSection}})],1)])],1)],1),t("div",{staticClass:"bottom__button"},[t("el-button",{attrs:{type:"primary",loading:e.saveLoading,size:"medium"},on:{click:e.save}},[e._v("确定")]),t("el-button",{attrs:{size:"medium"},on:{click:e.cancel}},[e._v("取消")])],1)],1)},i=[],l=a("0fea"),s=a("afd9"),o=a("82be"),r={components:{SubSection:()=>a.e("chunk-2bfdb478").then(a.bind(null,"0917")),SelectCustomer:()=>a.e("chunk-b124723c").then(a.bind(null,"00d8")),SelectProduct:()=>Promise.all([a.e("chunk-b5011952"),a.e("chunk-2d210879")]).then(a.bind(null,"c143")),SelectChannel:()=>a.e("chunk-3c76051b").then(a.bind(null,"5571")),AiModel:()=>a.e("chunk-dbd5596e").then(a.bind(null,"a0bd")),Loader:()=>a.e("chunk-4b92d70a").then(a.bind(null,"9267"))},props:{node:{type:Object,default:()=>({})},modify:{type:Boolean,default:!1}},computed:{isProductGuide(){return this.modify?0==this.node.businessData.taskType:"Product"===this.node.type},parentInfo(){if(this.modify){const e=$graph.findDataById(this.node.pId);return e.businessData}return this.node.businessData}},data(){return{activeName:"AI",saveLoading:!1,source:null,initialData:{customerSection:{},productSection:{},channelSection:{},aiSection:{}}}},methods:{async assembleParams(){try{var e,t;const a=await(null===(e=this.$refs.customerRef)||void 0===e?void 0:e.validate()),n=await(null===(t=this.$refs.productRef)||void 0===t?void 0:t.validate()),i=await this.$refs.channelRef.validate(),l=await this.$refs.aiRef.validate();this.initialData={customerSection:a,productSection:n,channelSection:i,aiSection:l};const s={taskType:this.isProductGuide?0:1,channelId:"-1"==i.channelMethod?"-1":i.selectChannel,isManualPerfection:l.channelParams,scoreRange:l.scoreRange,productList:[],ruleList:[]};this.isProductGuide?(s.productList=this.parentInfo.productList,"1"===a.attrType?s.ruleList=a.selectCustomerList.map(e=>e.finalExpression):s.ruleList=a.children.map(e=>e.finalExpression)):(s.productList=n.productList,s.ruleList=[this.parentInfo.customerInfo.finalExpression]);const r=Object(o["d"])(s);return r}catch(n){if(n.ref&&this.$refs[n.ref].$el.scrollIntoView({behavior:"smooth"}),"scroll"===n.type){var a;const e=null===(a=this.$refs.customerRef)||void 0===a||null===(a=a.$refs["fissionSceneRef"+n.childIndex][0])||void 0===a?void 0:a.$el;null===e||void 0===e||e.scrollIntoView({behavior:"smooth"})}return Promise.reject(n),null}},async save(){try{const e=await this.assembleParams();if(!e)return;this.saveLoading=!0,this.source=this.$CancelToken.source();const{respondVOList:t,aitaskId:a}=await Object(l["i"])(s["a"].getInferenceResByAI,e,this.source.token,12e4);if(t.some(e=>"0"==e.custVO.custNum))return this.$alert("AI执行推理任务未返回匹配数据，请重新选择规则！","提示",{confirmButtonText:"确定",customClass:"errorPrompt",callback:()=>{}});this.$emit("save","aiHelp",{aitaskId:a,activityList:t,initialData:this.initialData,taskType:this.isProductGuide?0:1})}catch(e){}finally{this.saveLoading=!1}},cancel(){this.saveLoading?this.source.cancel("取消请求"):this.$emit("cancel")}},created(){this.modify&&(this.initialData=this.node.businessData.initialData)},beforeDestroy(){this.saveLoading&&this.source.cancel("取消请求")}},c=r,u=(a("f49f"),a("2877")),p=Object(u["a"])(c,n,i,!1,null,"176e8c77",null);t["default"]=p.exports},2405:function(e,t,a){},"82be":function(e,t,a){"use strict";a.d(t,"b",(function(){return l})),a.d(t,"a",(function(){return s})),a.d(t,"c",(function(){return u})),a.d(t,"e",(function(){return p})),a.d(t,"f",(function(){return d})),a.d(t,"d",(function(){return m}));a("d9e2"),a("14d9");var n=a("c1df"),i=a.n(n);const l=[{label:"=",value:"="},{label:">",value:">"},{label:"<",value:"<"},{label:">=",value:">="},{label:"<=",value:"<="},{label:"( ) 介于，左右不包括",value:"()"},{label:"( ] 介于，左不包括右包括",value:"(]"},{label:"[ ) 介于，左包括右不包括",value:"[)"},{label:"[ ] 介于，左右都包括",value:"[]"}],s=[{label:"等于",value:"="},{label:"不等于",value:"!="}],o={up:"提升",down:"降低",any:"任何变化",exact:"变成","=":"等于:","!=":"不等于:",">":"大于:","<":"小于:",">=":"大于等于:","<=":"小于等于:","[]":"介于:","()":"介于:","(]":"介于:","[)":"介于:",IN:"包含:",LK:"LIKE:"},r=sessionStorage.getItem("userId"),c=sessionStorage.getItem("userNameCn");function u(e){const t=["=","!=",">","<",">=","<="],a=["[]","()","(]","[)"],n=function(e){return"number"===typeof e?e:e||""},i=function(e){return e.condition=e.condition||e.conditions,"e="===e.condition?"非规范值":t.indexOf(e.condition)>=0?o[e.condition]+n(e["stringVal"])+(n(e["remark"])||n(e["startValue"])?n(e["startValue"])+""+n(e["remark"]):""):a.indexOf(e.condition)>=0?e["remark"]?o[e.condition]+`${e["startValue"]}-${e["endValue"]}`+e["remark"]:o[e.condition]+e["startValue"]+"至"+e["endValue"]:o[e.condition]+n(e["stringVal"])+(n(e["remark"])?n(e["remark"]):"")};return i(e)}function p(e,t){var a;return{sign:"",requestNo:"",version:"",pageIndex:0,pageSize:0,customerVo:{treeDetails:[{id:1,parentId:0,type:"root",calType:"=",columnName:null,columnNum:+(null===(a=t.customerInfo)||void 0===a?void 0:a.columnNum)||0,selectedValues:["Y"],updateCycle:null},...e.map((e,t)=>{var a;return{id:t+2,parentId:t+1,type:"normal",isFission:0,calType:e.calType,columnName:e.columnName,columnNum:e.columnNum,selectedValues:e.selectedValues,numVal1:null!==(a=e.numVal1)&&void 0!==a?a:e.value,numVal2:e.numVal2,updateCycle:null}})]}}}function d(e,t){var a;return[{id:1,parentId:0,type:"root",calType:"=",columnName:null,columnNum:+(null===(a=t.customerInfo)||void 0===a?void 0:a.columnNum)||0,selectedValues:["Y"],updateCycle:null},...e.map((e,t)=>{var a;return{id:t+2,parentId:t+1,type:"normal",isFission:0,calType:e.calType,columnName:e.columnName,columnNum:e.columnNum,selectedValues:e.selectedValues,numVal1:null!==(a=e.numVal1)&&void 0!==a?a:e.value,numVal2:e.numVal2,updateCycle:null}})]}function m(e){const{crossJoin:t=!1,taskType:a,productList:n,channelId:l,ruleList:s,scoreRange:o,isManualPerfection:u=1,peopleCnt:p=null}=e;let d=[];if(!p&&!o||!p&&2!==o.length)throw this.$message.error("请正确填写客群规模或得分区间！"),new Error("参数异常");if(t||1===a){if(s.length)for(let i=0;i<n.length;i++)for(let e=0;e<s.length;e++)d.push({ruleList:[s[e]],planId:n[i].planId,priority:0,custgroupList:[]})}else d=n.map(e=>{const t={ruleList:s,planId:e.planId,priority:0,custgroupList:[]};return 0===s.length&&delete t.ruleList,t});const m=s.length?{prodCustGroupList:d}:{};return{isManualPerfection:u,resByAIReqVO:{taskType:0,channelId:l,coordinationSwitch:0,executeType:0,hotSwitch:0,marketGoal:"1,3",recommendGoal:0,recommendProdNum:-1,taskEffectTime:i()().format("yyyy-MM-DD"),taskFailTime:"2099-12-31",taskId:"",taskName:"task_"+Date.now()+parseInt(1e3*Math.random()),execFlag:"0",createUserId:r,createUserName:c,downshiftFilter:"1",...m,prodPriorityList:n.map(e=>({peopleCnt:p,planId:e.planId,priority:0,planDescribe:e.planDesc,limitScore:p?null:o[0],maxScore:p?null:o[1],recommendMode:1,presetCustName:""}))}}}},afd9:function(e,t,a){"use strict";t["a"]={search0:"/plan-svc/api/intellpushprism/column/search0",valuePage:"/plan-svc/api/intellpushprism/column/valuePage",getTargetUserCount:"/plan-svc/api/intellpushprism/column/getTargetUserCount",batchImportProduct:"/plan-svc/api/intellpushprism/batchImportProduct",batchImpPlanTmpDownload:"/plan-svc/api/intellpushprism/batchImpPlanTmpDownload",saveScene:"/plan-svc/api/intellpushprism/saveScene",batchImportCust:"/plan-svc/api/intellpushprism/batchImportCust",batchImpCustTmpDownload:"/plan-svc/api/intellpushprism/batchImpCustTmpDownload",batchImportLabel:"/plan-svc/api/intellpushprism/batchImportLabel",batchImpLabelTmpDownload:"/plan-svc/api/intellpushprism/batchImpLabelTmpDownload",queryScoreRangeBatch:"/plan-svc/api/intellpushprism/ai/queryScoreRangeBatch",downloadChanPromptFile:"/plan-svc/mcd/chan/prompt/downloadChanPromptFile",getInferenceResByAI:"/plan-svc/api/intellpushprism/ai/getInferenceResByAI",queryScoreRange:"/plan-svc/api/intellpushprism/ai/queryScoreRange",queryAICustLoseWeightRes:"/plan-svc/api/intellpushprism/ai/queryAICustLoseWeightRes",OperationWorkshop:{queryMyAttentionPlan:"/plan-svc/api/intellpushprism/ai/queryMyAttentionPlan",addMyAttentionPlan:"/plan-svc/api/intellpushprism/ai/addMyAttentionPlan",getCampEvalTop5ByPlan:"/plan-svc/api/intellpushprism/ai/getCampEvalTop5ByPlan",getAITagTop10ByPlan:"/plan-svc/api/intellpushprism/ai/getAITagTop10ByPlan",removeMyAttentionPlan:"/plan-svc/api/intellpushprism/ai/removeMyAttentionPlan"}}},f49f:function(e,t,a){"use strict";a("2405")}}]);