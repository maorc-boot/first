(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-5ad12fd8","chunk-2bfdb478"],{"0917":function(t,e,a){"use strict";a.r(e);var l=function(){var t=this,e=t._self._c;return e("section",{staticClass:"sub-section"},[e("header",[t._v(t._s(t.title))]),e("main",[t._t("default")],2)])},i=[],s={props:{title:{type:String,default:""}}},n=s,c=(a("ad4e"),a("2877")),o=Object(c["a"])(n,l,i,!1,null,"d5ef0d10",null);e["default"]=o.exports},"15c63":function(t,e,a){},"515d":function(t,e,a){},"7ce3":function(t,e,a){"use strict";a("15c63")},"9bca":function(t,e,a){"use strict";a.r(e);var l=function(){var t=this,e=t._self._c;return e("div",{staticClass:"select_products"},[e("el-tabs",{staticClass:"product_tabs",on:{"tab-click":t.handleTabClick},model:{value:t.activeName,callback:function(e){t.activeName=e},expression:"activeName"}},t._l(t.productTabList,(function(a){return e("el-tab-pane",{key:a.value,attrs:{name:a.value,label:a.label}},[e(a.component,{ref:"tab"+a.value,refInFor:!0,tag:"component",attrs:{mainProductList:t.productTabList[0].multipleSelection,tabName:a.value,showUpload:a.showUpload&&!t.isEdit,multiple:!(t.isEdit&&"product"===a.value),excluedList:t.productTabList,initialSelection:a.multipleSelection},on:{selectionChange:t.updateSelection}}),"Scene"===t.node.type&&"product"===t.activeName?e("div",{ref:"matchRef",refInFor:!0,staticClass:"scene_match"},[e("SubSection",{attrs:{title:"场景-产品匹配"}},[e("div",[e("el-table",{staticStyle:{width:"100%"},attrs:{data:t.nodesTableData}},[e("el-table-column",{attrs:{label:"序号",prop:"tableIdx"}}),e("el-table-column",{attrs:{label:"场景名称"},scopedSlots:t._u([{key:"default",fn:function(a){return[e("p",[t._v(t._s(a.row.sceneName))])]}}],null,!0)}),e("el-table-column",{attrs:{label:"目标产品"},scopedSlots:t._u([{key:"default",fn:function(a){return[e("el-select",{staticClass:"node_prod_selector",attrs:{filterable:"",placeholder:"请选择"},model:{value:a.row.planId,callback:function(e){t.$set(a.row,"planId",e)},expression:"scope.row.planId"}},t._l(t.sceneProductSelectList,(function(t){return e("el-option",{key:t.planId,attrs:{label:t.planName,value:t.planId}})})),1)]}}],null,!0)})],1)],1)])],1):t._e()],1)})),1),e("div",{staticClass:"button_box"},[e("el-button",{attrs:{type:"primary",size:"medium"},on:{click:t.handleClick}},[t._v("确 定")]),e("el-button",{attrs:{size:"medium"},on:{click:function(e){return t.$emit("cancel")}}},[t._v("取 消")])],1)],1)},i=[],s=(a("14d9"),a("3599")),n=function(){var t=this,e=t._self._c;return e("div",{staticClass:"product_tab_box"},[e("div",{staticClass:"selected_product"},[e("select-view",{attrs:{title:"已选产品",list:t.multipleSelection,option:t.selectKeyMap},on:{"update:list":t.updateSelectList}})],1),e("div",{staticClass:"search"},[e("div",{staticClass:"screen-mutual"},[e("el-checkbox",{on:{change:t.updateAllInSelection},model:{value:t.allIn,callback:function(e){t.allIn=e},expression:"allIn"}},[t._v("全选")])],1),e("div",{staticClass:"screen-mutual",on:{click:function(e){return t.getMutual()}}},[e("span",[t._v("默认互斥产品")])])]),e("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.loading,expression:"loading"}],staticStyle:{width:"100%"},attrs:{data:t.mutualTableData,height:"500"}},[e("el-table-column",{attrs:{type:"index",label:"序号",width:"45px"}}),e("el-table-column",{attrs:{prop:"planId",label:"产品编号",width:"150","show-overflow-tooltip":!0}}),e("el-table-column",{attrs:{prop:"planName",label:"产品名称"}})],1),e("el-dialog",{attrs:{title:"默认互斥产品查看",visible:t.mutualVisible,"before-close":t.changeMutual,"append-to-body":!0}},[e("div",{staticClass:"sub-title"},[t._v("主产品[ "+t._s(t.mainProduct.planId)+" ]默认互斥产品：")]),e("el-table",{attrs:{data:t.mutualList,stripe:""}},[e("el-table-column",{attrs:{label:"序号",width:"150",type:"index",align:"center"}}),e("el-table-column",{attrs:{prop:"planId",label:"产品编号",width:"200",align:"center"}}),e("el-table-column",{attrs:{prop:"planName",label:"产品名称",align:"center"}})],1)],1)],1)},c=[],o=a("dec5"),u=a("0fea"),r=a("d370"),p={components:{SelectView:o["default"]},props:{tabName:{type:String,required:!0},mainProductList:{type:Array,default:()=>[]},initialSelection:{type:Array,default:()=>[]}},computed:{mainProduct(){return this.mainProductList[0]||{}}},data(){return{allIn:!0,loading:!1,mutualVisible:!1,mutualList:[],mutualParams:{planId:""},selectKeyMap:{label:"planName",value:"planId"},mutualTableData:[],multipleSelection:[]}},watch:{mainProduct:{immediate:!0,handler(t){t.planId&&this.getmutualList()}}},created(){this.initMultipleSelection()},methods:{initMultipleSelection(){this.multipleSelection=this.initialSelection},updateAllInSelection(){if(this.allIn){const t=this.mutualTableData.map(({id:t,...e})=>e);this.updateSelectList(t)}else this.updateSelectList([])},updateSelectList(t=[]){this.multipleSelection=t,this.$emit("selectionChange",this.tabName,this.multipleSelection)},changeMutual(){this.mutualVisible=!1},getMutual(){Object(u["i"])(r["a"].mpapi.getmutualList,{planId:this.mainProduct.planId,sourceType:1}).then(t=>{this.mutualList=t&&t.planExcluList||[],this.mutualParams=this.mainProduct,this.mutualVisible=!0})},getmutualList(){Object(u["i"])(r["a"].mpapi.getmutualList,{planId:this.mainProduct.planId,sourceType:0}).then(t=>{this.mutualTableData=t.planExcluList||[],this.updateAllInSelection()})}}},d=p,h=(a("aaf9a"),a("2877")),m=Object(h["a"])(d,n,c,!1,null,"158dbb31",null),b=m.exports,f=a("0917"),v={props:{node:{type:Object,required:!0}},components:{ProductTab:s["a"],MutexProdTab:b,SubSection:f["default"]},data(){return{activeName:"product",productTabList:[{label:"主产品",value:"product",component:s["a"],showUpload:!0,multipleSelection:[]},{label:"融合产品",value:"fusion",component:s["a"],showUpload:!1,multipleSelection:[]},{label:"同系列产品",value:"series",component:s["a"],showUpload:!1,multipleSelection:[]},{label:"互斥产品",value:"mutex",component:b,showUpload:!1,multipleSelection:[]}],isBatchMatch:!0,nodesData:[]}},computed:{isEdit(){return"Product"===this.node.type},sceneProductSelectList(){return this.productTabList[0].multipleSelection},switchText(){return this.isBatchMatch?"单个匹配>>":"批量匹配>>"},nodesTableData(){return this.isBatchMatch?this.nodesData:this.nodesData.filter(t=>t.id===this.node.id)}},watch:{sceneProductSelectList:{deep:!0,handler(t){this.toggleSelectVal()}}},created(){if(this.isEdit||this.productTabList.splice(1,3),"Scene"===this.node.type){const t=this.getSceneNodeList();this.getFormatTableData(t)}this.isEdit&&this.initTabSection()},methods:{toggleSelectVal(){this.nodesTableData.forEach(t=>{if(t.planId){const e=this.sceneProductSelectList.some(e=>e.planId===t.planId);e||(t.planId="")}})},handleTabClick(t){this.$nextTick(()=>{const e=t.$children[0];e&&e.doLayout&&e.doLayout()})},updateSelection(t,e){for(let a=0;a<this.productTabList.length;a++)if(this.productTabList[a].value===t){this.productTabList[a].multipleSelection=e;break}},getFormatTableData(t){if(!t.length)return;const e=[];t.forEach(t=>{e.push(this.formatNodeData(t))}),this.nodesData=e},formatNodeData(t){const{id:e,businessData:a={},tableIdx:l}=t;return{id:e,tableIdx:l,sceneId:a.sceneId,sceneName:a.sceneName,planId:"",planName:""}},initTabSection(){if(!this.node||!this.node.businessData)return;const{businessData:t,pId:e}=this.node;this.productTabList.forEach(e=>{let a=[];"product"===e.value?a=[t.sceneList[0]]:"fusion"===e.value?a=t.fusionProductList||[]:"series"===e.value?a=t.seriesProductList||[]:"mutex"===e.value&&(a=t.mutexProductList||[]),e.multipleSelection=a})},getSceneNodeList(){function t(e){let a=[];if("Scene"===e.type)if(e.children.length)for(let l=0;l<e.children.length;l++)a=a.concat(t(e.children[l]));else a.push(e);else if(e.children.length)for(let l=0;l<e.children.length;l++)a=a.concat(t(e.children[l]));return a}const e=t(JSON.parse(JSON.stringify($graph.save())));return e.sort((t,e)=>t.id===this.node.id?-1:e.id===this.node.id?1:0),e.forEach((t,e)=>{t.tableIdx=t.id===this.node.id?"当前":e+1}),e},validate(){if(this.isEdit)return!!this.productTabList[0].multipleSelection.length||(this.$message.error("请选择主产品"),!1);if("Scene"===this.node.type){if(!this.nodesTableData.length)return!1;const t=this.nodesTableData.find(t=>t.id===this.node.id),e=t&&!!t.planId;return e||(this.$message.error("请为当前场景匹配产品"),this.$refs.matchRef[0].scrollIntoView({behavior:"smooth"})),e}return!!this.productTabList[0].multipleSelection.length||(this.$message.error("请选择主产品"),!1)},handleClick(){if(!this.validate())return;const t=this.productTabList.find(t=>"product"===t.value),e=this.productTabList.find(t=>"fusion"===t.value),a=this.productTabList.find(t=>"series"===t.value),l=this.productTabList.find(t=>"mutex"===t.value),i={productList:t.multipleSelection||[],fusionProductList:e&&e.multipleSelection||[],seriesProductList:a&&a.multipleSelection||[],mutexProductList:l&&l.multipleSelection||[],sceneList:[]};"Scene"===this.node.type?this.nodesTableData.forEach(t=>{if(t.planId){const e=i.productList.find(e=>e.planId===t.planId);i.sceneList.push({...t,...e})}}):this.isEdit&&i.sceneList.push({...t.multipleSelection[0]}),this.$emit("save","product",i)}}},S=v,g=(a("7ce3"),Object(h["a"])(S,l,i,!1,null,"e5a158ea",null));e["default"]=g.exports},aaf9a:function(t,e,a){"use strict";a("515d")},ad4e:function(t,e,a){"use strict";a("cd63")},afd9:function(t,e,a){"use strict";e["a"]={search0:"/plan-svc/api/intellpushprism/column/search0",valuePage:"/plan-svc/api/intellpushprism/column/valuePage",getTargetUserCount:"/plan-svc/api/intellpushprism/column/getTargetUserCount",batchImportProduct:"/plan-svc/api/intellpushprism/batchImportProduct",batchImpPlanTmpDownload:"/plan-svc/api/intellpushprism/batchImpPlanTmpDownload",saveScene:"/plan-svc/api/intellpushprism/saveScene",batchImportCust:"/plan-svc/api/intellpushprism/batchImportCust",batchImpCustTmpDownload:"/plan-svc/api/intellpushprism/batchImpCustTmpDownload",batchImportLabel:"/plan-svc/api/intellpushprism/batchImportLabel",batchImpLabelTmpDownload:"/plan-svc/api/intellpushprism/batchImpLabelTmpDownload",queryScoreRangeBatch:"/plan-svc/api/intellpushprism/ai/queryScoreRangeBatch",downloadChanPromptFile:"/plan-svc/mcd/chan/prompt/downloadChanPromptFile",getInferenceResByAI:"/plan-svc/api/intellpushprism/ai/getInferenceResByAI",queryScoreRange:"/plan-svc/api/intellpushprism/ai/queryScoreRange",queryAICustLoseWeightRes:"/plan-svc/api/intellpushprism/ai/queryAICustLoseWeightRes",OperationWorkshop:{queryMyAttentionPlan:"/plan-svc/api/intellpushprism/ai/queryMyAttentionPlan",addMyAttentionPlan:"/plan-svc/api/intellpushprism/ai/addMyAttentionPlan",getCampEvalTop5ByPlan:"/plan-svc/api/intellpushprism/ai/getCampEvalTop5ByPlan",getAITagTop10ByPlan:"/plan-svc/api/intellpushprism/ai/getAITagTop10ByPlan",removeMyAttentionPlan:"/plan-svc/api/intellpushprism/ai/removeMyAttentionPlan"}}},cd63:function(t,e,a){}}]);