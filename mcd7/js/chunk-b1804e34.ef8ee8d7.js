(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-b1804e34"],{"271a":function(e,t,n){"use strict";var i=n("cb2d"),r=n("e330"),a=n("577e"),l=n("d6d6"),o=URLSearchParams,s=o.prototype,d=r(s.getAll),u=r(s.has),c=new o("a=1");!c.has("a",2)&&c.has("a",void 0)||i(s,"has",(function(e){var t=arguments.length,n=t<2?void 0:arguments[1];if(t&&void 0===n)return u(this,e);var i=d(this,e);l(t,1);var r=a(n),o=0;while(o<i.length)if(i[o++]===r)return!0;return!1}),{enumerable:!0,unsafe:!0})},5494:function(e,t,n){"use strict";var i=n("83ab"),r=n("e330"),a=n("edd0"),l=URLSearchParams.prototype,o=r(l.forEach);i&&!("size"in l)&&a(l,"size",{get:function(){var e=0;return o(this,(function(){e++})),e},configurable:!0,enumerable:!0})},"5a4a":function(module,__webpack_exports__,__webpack_require__){"use strict";__webpack_require__.d(__webpack_exports__,"b",(function(){return diffText})),__webpack_require__.d(__webpack_exports__,"a",(function(){return checkConditions})),__webpack_require__.d(__webpack_exports__,"e",(function(){return validateContent})),__webpack_require__.d(__webpack_exports__,"c",(function(){return formAddMarketingWords})),__webpack_require__.d(__webpack_exports__,"d",(function(){return marketingWords2Form}));var core_js_modules_es_array_push_js__WEBPACK_IMPORTED_MODULE_0__=__webpack_require__("14d9"),core_js_modules_es_array_push_js__WEBPACK_IMPORTED_MODULE_0___default=__webpack_require__.n(core_js_modules_es_array_push_js__WEBPACK_IMPORTED_MODULE_0__),diff__WEBPACK_IMPORTED_MODULE_1__=__webpack_require__("bf68"),diff__WEBPACK_IMPORTED_MODULE_1___default=__webpack_require__.n(diff__WEBPACK_IMPORTED_MODULE_1__),_constant__WEBPACK_IMPORTED_MODULE_2__=__webpack_require__("ac7c"),element_ui__WEBPACK_IMPORTED_MODULE_3__=__webpack_require__("5c96"),element_ui__WEBPACK_IMPORTED_MODULE_3___default=__webpack_require__.n(element_ui__WEBPACK_IMPORTED_MODULE_3__);function diffText(e,t,n={}){const i={background:"yellow",...n},{background:r}=i,a=Object(diff__WEBPACK_IMPORTED_MODULE_1__["diffChars"])(e,t);let l="",o="";for(let s=0;s<a.length;s++){const{added:e,removed:t,value:n}=a[s],{added:i,removed:d,value:u}=a[s+1]||{},c=`<span style="background: ${r}">${isNaN(parseInt(n))?"&emsp;":"&ensp;"}</span>`,p=`<span style="background: ${r}">${isNaN(parseInt(u))?"&emsp;":"&ensp;"}</span>`;if(e)l+=c.repeat(n.length),o+=`<span class="cur">${n}</span>`;else if(t){if(i){u.length===n.length?(l+=`<span class="pre">${n}</span>`,o+=`<span class="cur">${u}</span>`):u.length>n.length?(l+=`<span class="pre">${n}</span>`,o+=`<span class="cur">${u}</span>`+c.repeat(u.length-n.length)):u.length<n.length&&(l+=`<span class="pre">${n}</span>`+p.repeat(n.length-u.length),o+=`<span class="cur">${u}</span>`),s++;continue}l+=`<span class="pre">${n}</span>`,o+=c.repeat(n.length)}else l+=n,o+=n}return[l,o]}function checkConditions(a,conditions){let m=0,n=0;for(let j=1;j<=a;j++){n++;let a=j;for(let i=0;i<conditions.length;i++)eval(conditions[i])&&m++}return m>n?"当前评估规则取数有重叠，请检查":!(m<n)||"当前评估规则取数不连续，请检查"}function validateContent(e){let t=!0;for(let n=0;n<e.marketingWords.length;n++){const i=e.marketingWords[n];if(i.list&&i.list.length){for(let e=0;e<i.list.length;e++)if(!i.list[e].content)return element_ui__WEBPACK_IMPORTED_MODULE_3__["Message"].error("请填写"+i.list[e].name),t=!1,t}else if(i.uploadType){if(!i.uploadFileUrl)return element_ui__WEBPACK_IMPORTED_MODULE_3__["Message"].error("请上传"+i.typeName),t=!1,t;if(!i.linkUrl)return element_ui__WEBPACK_IMPORTED_MODULE_3__["Message"].error("请填写链接地址"),t=!1,t}else if(!i.content)return element_ui__WEBPACK_IMPORTED_MODULE_3__["Message"].error("请填写"+i.typeName),t=!1,t}return t}function formAddMarketingWords(e){for(const o in e)"number"==typeof e[o]&&(e[o]=e[o].toString());const t=_constant__WEBPACK_IMPORTED_MODULE_2__["a"].find(t=>t.value==e.contentType).marketingWords,n=JSON.parse(JSON.stringify(t));for(let o=0;o<n.length;o++){var i,r;if(n[o].isRich&&null!==(i=n[o].list)&&void 0!==i&&i.length)for(let t=0;t<n[o].list.length;t++){var a;const{id:i}=n[o].list[t],r=(null===(a=e.mkContentList)||void 0===a?void 0:a.filter(e=>1==e.isFullText))||[],l=r.find(e=>e.contentType==i)||{};n[o].list[t].content=(null===l||void 0===l?void 0:l.scriptContent)||""}else if(!n[o].isRich&&null!==(r=n[o].list)&&void 0!==r&&r.length)for(let t=0;t<n[o].list.length;t++){var l;const{id:i}=n[o].list[t],r=(null===(l=e.mkContentList)||void 0===l?void 0:l.filter(e=>0==e.isFullText))||[],a=r.find(e=>e.contentType==i)||{};n[o].list[t].content=(null===a||void 0===a?void 0:a.scriptContent)||""}else n[o].uploadType?(n[o].uploadFileName=e.fileName||"",n[o].uploadFileUrl=e.fileUrl||"",n[o].linkUrl=e.url||""):"短信语"==n[o].typeName?n[o].content=e.smsContent||"":n[o].content=e.recommendContent||""}return{...e,marketingWords:n}}function marketingWords2Form(e){const t=JSON.parse(JSON.stringify(e)),{marketingWords:n}=t,i=[];for(let a=0;a<n.length;a++){var r;if(null!==(r=n[a].list)&&void 0!==r&&r.length)for(let e=0;e<n[a].list.length;e++){const{id:r,content:l}=n[a].list[e];i.push({contentType:r,scriptContent:l,scriptId:t.scriptId,isFullText:n[a].isRich?1:0})}else n[a].uploadType?(t.fileName=n[a].uploadFileName,t.fileUrl=n[a].uploadFileUrl,t.url=n[a].linkUrl):"短信语"==n[a].typeName?t.smsContent=n[a].content:t.recommendContent=n[a].content}return t.mkContentList=i,delete t.marketingWords,t}},"5af3":function(e,t,n){"use strict";n.d(t,"d",(function(){return a})),n.d(t,"c",(function(){return l})),n.d(t,"a",(function(){return o})),n.d(t,"b",(function(){return s}));n("88a7"),n("271a"),n("5494");var i=n("0fea"),r=n("d39c");const a=async e=>{try{const{data:t}=await Object(i["b"])(r["a"].seapi.downloadChanPromptFile,{fileUrlAndName:e}),n=new Blob([t],{type:t.type});return URL.createObjectURL(n)}catch(t){return t}},l=async e=>{try{const{data:t}=await Object(i["b"])(r["a"].seapi.downloadChanPromptFile,{fileUrlAndName:e}),n=new Blob([t],{type:t.type}),a=new File([n],e);return a}catch(t){return t}},o=(e,t)=>{const n=new Blob([e],{type:e.type}),i=document.createElement("a");i.download=t,i.style.display="none",i.href=URL.createObjectURL(n),document.body.appendChild(i),i.click(),URL.revokeObjectURL(i.href),document.body.removeChild(i)};async function s(e,t){try{const{data:n}=await Object(i["b"])(r["a"].seapi.downloadChanPromptFile,{fileUrlAndName:e});o(n,t)}catch(n){return n}}},"88a7":function(e,t,n){"use strict";var i=n("cb2d"),r=n("e330"),a=n("577e"),l=n("d6d6"),o=URLSearchParams,s=o.prototype,d=r(s.append),u=r(s["delete"]),c=r(s.forEach),p=r([].push),f=new o("a=1&a=2&b=3");f["delete"]("a",1),f["delete"]("b",void 0),f+""!=="a=2"&&i(s,"delete",(function(e){var t=arguments.length,n=t<2?void 0:arguments[1];if(t&&void 0===n)return u(this,e);var i=[];c(this,(function(e,t){p(i,{key:t,value:e})})),l(t,1);var r,o=a(e),s=a(n),f=0,m=0,h=!1,v=i.length;while(f<v)r=i[f++],h||r.key===o?(h=!0,u(this,r.key)):m++;while(m<v)r=i[m++],r.key===o&&r.value===s||d(this,r.key,r.value)}),{enumerable:!0,unsafe:!0})},ac7c:function(e,t,n){"use strict";n.d(t,"a",(function(){return l})),n.d(t,"j",(function(){return o})),n.d(t,"e",(function(){return s})),n.d(t,"d",(function(){return d})),n.d(t,"h",(function(){return u})),n.d(t,"i",(function(){return c})),n.d(t,"f",(function(){return p})),n.d(t,"g",(function(){return f})),n.d(t,"c",(function(){return m})),n.d(t,"b",(function(){return h}));const i={typeId:"",typeName:"",content:"",uploadType:"",uploadFileUrl:"",uploadFileName:"",linkUrl:"",isRich:!1,list:[]};function r(e={}){return{...i,...e}}const a=[{id:"0",name:"引导语",content:""},{id:"1",name:"卖点分析",content:""},{id:"2",name:"资费介绍",content:""},{id:"3",name:"挽留语",content:""},{id:"4",name:"必讲内容",content:""},{id:"5",name:"业务办理",content:""},{id:"6",name:"结束语",content:""},{id:"7",name:"备注信息",content:""}],l=[{label:"面对面营销语(富文本)+短信语",value:"1",marketingWords:[r({typeId:"1",typeName:"面对面营销语(富文本)",isRich:!0,list:a}),r({typeId:"4",typeName:"短信语"})]},{label:"面对面营销语(富文本)",value:"2",marketingWords:[r({typeId:"1",typeName:"面对面营销语(富文本)",isRich:!0,list:a})]},{label:"面对面营销语(文本)+短信语",value:"3",marketingWords:[r({typeId:"2",typeName:"面对面营销语(文本)",list:a}),r({typeId:"4",typeName:"短信语"})]},{label:"面对面营销语(文本)",value:"4",marketingWords:[r({typeId:"2",typeName:"面对面营销语(文本)",list:a})]},{label:"推荐语+短信语",value:"5",marketingWords:[r({typeId:"3",typeName:"推荐语"}),r({typeId:"4",typeName:"短信语"})]},{label:"短信语",value:"6",marketingWords:[r({typeId:"4",typeName:"短信语"})]},{label:"推荐语",value:"7",marketingWords:[r({typeId:"3",typeName:"推荐语"})]},{label:"图片",value:"8",marketingWords:[r({typeId:"5",typeName:"图片",uploadType:"1"})]},{label:"图片+推荐语",value:"9",marketingWords:[r({typeId:"5",typeName:"图片",uploadType:"1"}),r({typeId:"3",typeName:"推荐语"})]}],o=[{label:"标准话术",value:"1"},{label:"个性化话术",value:"2"}],s=[{label:"营销活动引⽤次数",value:"1",disabled:!1}],d=[{label:"营销接触率",value:"1",disabled:!1},{label:"营销办理率",value:"2",disabled:!1}],u=[{label:"点赞",value:"1",disabled:!1},{label:"预览",value:"2",disabled:!1}],c=[{label:"小于",value:"<"},{label:"大于",value:">"},{label:"区间",value:"between"}],p=[{label:"10086呼入弹屏",value:"804"},{label:"营业厅网台",value:"906"},{label:"智慧网格",value:"808"},{label:"和我信",value:"924"},{label:"客户通",value:"809"}],f=new Map([[0,"草稿"],[1,"审批通过"],[2,"审批中"],[3,"终止"],[4,"审批驳回"],[5,"审批失败"]]),m=[{label:"优秀话术",value:"1"},{label:"普通话术",value:"0"}],h=[{label:"手动",value:"0"},{label:"自动",value:"1"},{label:"外部系统",value:"2"}]},bf68:function(e,t,n){(function(e,n){n(t)})(0,(function(e){"use strict";function t(){}function n(e,t,n,i,r){var a,l=[];while(t)l.push(t),a=t.previousComponent,delete t.previousComponent,t=a;l.reverse();for(var o=0,s=l.length,d=0,u=0;o<s;o++){var c=l[o];if(c.removed){if(c.value=e.join(i.slice(u,u+c.count)),u+=c.count,o&&l[o-1].added){var p=l[o-1];l[o-1]=l[o],l[o]=p}}else{if(!c.added&&r){var f=n.slice(d,d+c.count);f=f.map((function(e,t){var n=i[u+t];return n.length>e.length?n:e})),c.value=e.join(f)}else c.value=e.join(n.slice(d,d+c.count));d+=c.count,c.added||(u+=c.count)}}var m=l[s-1];return s>1&&"string"===typeof m.value&&(m.added||m.removed)&&e.equals("",m.value)&&(l[s-2].value+=m.value,l.pop()),l}t.prototype={diff:function(e,t){var i,r=arguments.length>2&&void 0!==arguments[2]?arguments[2]:{},a=r.callback;"function"===typeof r&&(a=r,r={}),this.options=r;var l=this;function o(e){return a?(setTimeout((function(){a(void 0,e)}),0),!0):e}e=this.castInput(e),t=this.castInput(t),e=this.removeEmpty(this.tokenize(e)),t=this.removeEmpty(this.tokenize(t));var s=t.length,d=e.length,u=1,c=s+d;r.maxEditLength&&(c=Math.min(c,r.maxEditLength));var p=null!==(i=r.timeout)&&void 0!==i?i:1/0,f=Date.now()+p,m=[{oldPos:-1,lastComponent:void 0}],h=this.extractCommon(m[0],t,e,0);if(m[0].oldPos+1>=d&&h+1>=s)return o([{value:this.join(t),count:t.length}]);var v=-1/0,g=1/0;function _(){for(var i=Math.max(v,-u);i<=Math.min(g,u);i+=2){var r=void 0,a=m[i-1],c=m[i+1];a&&(m[i-1]=void 0);var p=!1;if(c){var f=c.oldPos-i;p=c&&0<=f&&f<s}var _=a&&a.oldPos+1<d;if(p||_){if(r=!_||p&&a.oldPos+1<c.oldPos?l.addToPath(c,!0,void 0,0):l.addToPath(a,void 0,!0,1),h=l.extractCommon(r,t,e,i),r.oldPos+1>=d&&h+1>=s)return o(n(l,r.lastComponent,t,e,l.useLongestToken));m[i]=r,r.oldPos+1>=d&&(g=Math.min(g,i-1)),h+1>=s&&(v=Math.max(v,i+1))}else m[i]=void 0}u++}if(a)(function e(){setTimeout((function(){if(u>c||Date.now()>f)return a();_()||e()}),0)})();else while(u<=c&&Date.now()<=f){var y=_();if(y)return y}},addToPath:function(e,t,n,i){var r=e.lastComponent;return r&&r.added===t&&r.removed===n?{oldPos:e.oldPos+i,lastComponent:{count:r.count+1,added:t,removed:n,previousComponent:r.previousComponent}}:{oldPos:e.oldPos+i,lastComponent:{count:1,added:t,removed:n,previousComponent:r}}},extractCommon:function(e,t,n,i){var r=t.length,a=n.length,l=e.oldPos,o=l-i,s=0;while(o+1<r&&l+1<a&&this.equals(t[o+1],n[l+1]))o++,l++,s++;return s&&(e.lastComponent={count:s,previousComponent:e.lastComponent}),e.oldPos=l,o},equals:function(e,t){return this.options.comparator?this.options.comparator(e,t):e===t||this.options.ignoreCase&&e.toLowerCase()===t.toLowerCase()},removeEmpty:function(e){for(var t=[],n=0;n<e.length;n++)e[n]&&t.push(e[n]);return t},castInput:function(e){return e},tokenize:function(e){return e.split("")},join:function(e){return e.join("")}};var i=new t;function r(e,t,n){return i.diff(e,t,n)}function a(e,t){if("function"===typeof e)t.callback=e;else if(e)for(var n in e)e.hasOwnProperty(n)&&(t[n]=e[n]);return t}var l=/^[A-Za-z\xC0-\u02C6\u02C8-\u02D7\u02DE-\u02FF\u1E00-\u1EFF]+$/,o=/\S/,s=new t;function d(e,t,n){return n=a(n,{ignoreWhitespace:!0}),s.diff(e,t,n)}function u(e,t,n){return s.diff(e,t,n)}s.equals=function(e,t){return this.options.ignoreCase&&(e=e.toLowerCase(),t=t.toLowerCase()),e===t||this.options.ignoreWhitespace&&!o.test(e)&&!o.test(t)},s.tokenize=function(e){for(var t=e.split(/([^\S\r\n]+|[()[\]{}'"\r\n]|\b)/),n=0;n<t.length-1;n++)!t[n+1]&&t[n+2]&&l.test(t[n])&&l.test(t[n+2])&&(t[n]+=t[n+2],t.splice(n+1,2),n--);return t};var c=new t;function p(e,t,n){return c.diff(e,t,n)}function f(e,t,n){var i=a(n,{ignoreWhitespace:!0});return c.diff(e,t,i)}c.tokenize=function(e){this.options.stripTrailingCr&&(e=e.replace(/\r\n/g,"\n"));var t=[],n=e.split(/(\n|\r\n)/);n[n.length-1]||n.pop();for(var i=0;i<n.length;i++){var r=n[i];i%2&&!this.options.newlineIsToken?t[t.length-1]+=r:(this.options.ignoreWhitespace&&(r=r.trim()),t.push(r))}return t};var m=new t;function h(e,t,n){return m.diff(e,t,n)}m.tokenize=function(e){return e.split(/(\S.+?[.!?])(?=\s+|$)/)};var v=new t;function g(e,t,n){return v.diff(e,t,n)}function _(e){return _="function"===typeof Symbol&&"symbol"===typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"===typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},_(e)}function y(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function w(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);t&&(i=i.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,i)}return n}function b(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?w(Object(n),!0).forEach((function(t){y(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):w(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function C(e){return D(e)||M(e)||k(e)||L()}function D(e){if(Array.isArray(e))return P(e)}function M(e){if("undefined"!==typeof Symbol&&Symbol.iterator in Object(e))return Array.from(e)}function k(e,t){if(e){if("string"===typeof e)return P(e,t);var n=Object.prototype.toString.call(e).slice(8,-1);return"Object"===n&&e.constructor&&(n=e.constructor.name),"Map"===n||"Set"===n?Array.from(e):"Arguments"===n||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)?P(e,t):void 0}}function P(e,t){(null==t||t>e.length)&&(t=e.length);for(var n=0,i=new Array(t);n<t;n++)i[n]=e[n];return i}function L(){throw new TypeError("Invalid attempt to spread non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}v.tokenize=function(e){return e.split(/([{}:;,]|\s+)/)};var S=Object.prototype.toString,I=new t;function O(e,t,n){return I.diff(e,t,n)}function x(e,t,n,i,r){var a,l;for(t=t||[],n=n||[],i&&(e=i(r,e)),a=0;a<t.length;a+=1)if(t[a]===e)return n[a];if("[object Array]"===S.call(e)){for(t.push(e),l=new Array(e.length),n.push(l),a=0;a<e.length;a+=1)l[a]=x(e[a],t,n,i,r);return t.pop(),n.pop(),l}if(e&&e.toJSON&&(e=e.toJSON()),"object"===_(e)&&null!==e){t.push(e),l={},n.push(l);var o,s=[];for(o in e)e.hasOwnProperty(o)&&s.push(o);for(s.sort(),a=0;a<s.length;a+=1)o=s[a],l[o]=x(e[o],t,n,i,o);t.pop(),n.pop()}else l=e;return l}I.useLongestToken=!0,I.tokenize=c.tokenize,I.castInput=function(e){var t=this.options,n=t.undefinedReplacement,i=t.stringifyReplacer,r=void 0===i?function(e,t){return"undefined"===typeof t?n:t}:i;return"string"===typeof e?e:JSON.stringify(x(e,null,null,r),r,"  ")},I.equals=function(e,n){return t.prototype.equals.call(I,e.replace(/,([\r\n])/g,"$1"),n.replace(/,([\r\n])/g,"$1"))};var N=new t;function E(e,t,n){return N.diff(e,t,n)}function A(e){var t=arguments.length>1&&void 0!==arguments[1]?arguments[1]:{},n=e.split(/\r\n|[\n\v\f\r\x85]/),i=e.match(/\r\n|[\n\v\f\r\x85]/g)||[],r=[],a=0;function l(){var e={};r.push(e);while(a<n.length){var i=n[a];if(/^(\-\-\-|\+\+\+|@@)\s/.test(i))break;var l=/^(?:Index:|diff(?: -r \w+)+)\s+(.+?)\s*$/.exec(i);l&&(e.index=l[1]),a++}o(e),o(e),e.hunks=[];while(a<n.length){var d=n[a];if(/^(Index:|diff|\-\-\-|\+\+\+)\s/.test(d))break;if(/^@@/.test(d))e.hunks.push(s());else{if(d&&t.strict)throw new Error("Unknown line "+(a+1)+" "+JSON.stringify(d));a++}}}function o(e){var t=/^(---|\+\+\+)\s+(.*)$/.exec(n[a]);if(t){var i="---"===t[1]?"old":"new",r=t[2].split("\t",2),l=r[0].replace(/\\\\/g,"\\");/^".*"$/.test(l)&&(l=l.substr(1,l.length-2)),e[i+"FileName"]=l,e[i+"Header"]=(r[1]||"").trim(),a++}}function s(){var e=a,r=n[a++],l=r.split(/@@ -(\d+)(?:,(\d+))? \+(\d+)(?:,(\d+))? @@/),o={oldStart:+l[1],oldLines:"undefined"===typeof l[2]?1:+l[2],newStart:+l[3],newLines:"undefined"===typeof l[4]?1:+l[4],lines:[],linedelimiters:[]};0===o.oldLines&&(o.oldStart+=1),0===o.newLines&&(o.newStart+=1);for(var s=0,d=0;a<n.length;a++){if(0===n[a].indexOf("--- ")&&a+2<n.length&&0===n[a+1].indexOf("+++ ")&&0===n[a+2].indexOf("@@"))break;var u=0==n[a].length&&a!=n.length-1?" ":n[a][0];if("+"!==u&&"-"!==u&&" "!==u&&"\\"!==u)break;o.lines.push(n[a]),o.linedelimiters.push(i[a]||"\n"),"+"===u?s++:"-"===u?d++:" "===u&&(s++,d++)}if(s||1!==o.newLines||(o.newLines=0),d||1!==o.oldLines||(o.oldLines=0),t.strict){if(s!==o.newLines)throw new Error("Added line count did not match for hunk at line "+(e+1));if(d!==o.oldLines)throw new Error("Removed line count did not match for hunk at line "+(e+1))}return o}while(a<n.length)l();return r}function j(e,t,n){var i=!0,r=!1,a=!1,l=1;return function o(){if(i&&!a){if(r?l++:i=!1,e+l<=n)return l;a=!0}if(!r)return a||(i=!0),t<=e-l?-l++:(r=!0,o())}}function T(e,t){var n=arguments.length>2&&void 0!==arguments[2]?arguments[2]:{};if("string"===typeof t&&(t=A(t)),Array.isArray(t)){if(t.length>1)throw new Error("applyPatch only works with a single input.");t=t[0]}var i,r,a=e.split(/\r\n|[\n\v\f\r\x85]/),l=e.match(/\r\n|[\n\v\f\r\x85]/g)||[],o=t.hunks,s=n.compareLine||function(e,t,n,i){return t===i},d=0,u=n.fuzzFactor||0,c=0,p=0;function f(e,t){for(var n=0;n<e.lines.length;n++){var i=e.lines[n],r=i.length>0?i[0]:" ",l=i.length>0?i.substr(1):i;if(" "===r||"-"===r){if(!s(t+1,a[t],r,l)&&(d++,d>u))return!1;t++}}return!0}for(var m=0;m<o.length;m++){for(var h=o[m],v=a.length-h.oldLines,g=0,_=p+h.oldStart-1,y=j(_,c,v);void 0!==g;g=y())if(f(h,_+g)){h.offset=p+=g;break}if(void 0===g)return!1;c=h.offset+h.oldStart+h.oldLines}for(var w=0,b=0;b<o.length;b++){var C=o[b],D=C.oldStart+C.offset+w-1;w+=C.newLines-C.oldLines;for(var M=0;M<C.lines.length;M++){var k=C.lines[M],P=k.length>0?k[0]:" ",L=k.length>0?k.substr(1):k,S=C.linedelimiters&&C.linedelimiters[M]||"\n";if(" "===P)D++;else if("-"===P)a.splice(D,1),l.splice(D,1);else if("+"===P)a.splice(D,0,L),l.splice(D,0,S),D++;else if("\\"===P){var I=C.lines[M-1]?C.lines[M-1][0]:null;"+"===I?i=!0:"-"===I&&(r=!0)}}}if(i)while(!a[a.length-1])a.pop(),l.pop();else r&&(a.push(""),l.push("\n"));for(var O=0;O<a.length-1;O++)a[O]=a[O]+l[O];return a.join("")}function F(e,t){"string"===typeof e&&(e=A(e));var n=0;function i(){var r=e[n++];if(!r)return t.complete();t.loadFile(r,(function(e,n){if(e)return t.complete(e);var a=T(n,r,t);t.patched(r,a,(function(e){if(e)return t.complete(e);i()}))}))}i()}function q(e,t,n,i,r,a,l){l||(l={}),"undefined"===typeof l.context&&(l.context=4);var o=p(n,i,l);if(o){o.push({value:"",lines:[]});for(var s=[],d=0,u=0,c=[],f=1,m=1,h=function(e){var t=o[e],r=t.lines||t.value.replace(/\n$/,"").split("\n");if(t.lines=r,t.added||t.removed){var a;if(!d){var p=o[e-1];d=f,u=m,p&&(c=l.context>0?g(p.lines.slice(-l.context)):[],d-=c.length,u-=c.length)}(a=c).push.apply(a,C(r.map((function(e){return(t.added?"+":"-")+e})))),t.added?m+=r.length:f+=r.length}else{if(d)if(r.length<=2*l.context&&e<o.length-2){var h;(h=c).push.apply(h,C(g(r)))}else{var v,_=Math.min(r.length,l.context);(v=c).push.apply(v,C(g(r.slice(0,_))));var y={oldStart:d,oldLines:f-d+_,newStart:u,newLines:m-u+_,lines:c};if(e>=o.length-2&&r.length<=l.context){var w=/\n$/.test(n),b=/\n$/.test(i),D=0==r.length&&c.length>y.oldLines;!w&&D&&n.length>0&&c.splice(y.oldLines,0,"\\ No newline at end of file"),(w||D)&&b||c.push("\\ No newline at end of file")}s.push(y),d=0,u=0,c=[]}f+=r.length,m+=r.length}},v=0;v<o.length;v++)h(v);return{oldFileName:e,newFileName:t,oldHeader:r,newHeader:a,hunks:s}}function g(e){return e.map((function(e){return" "+e}))}}function U(e){if(Array.isArray(e))return e.map(U).join("\n");var t=[];e.oldFileName==e.newFileName&&t.push("Index: "+e.oldFileName),t.push("==================================================================="),t.push("--- "+e.oldFileName+("undefined"===typeof e.oldHeader?"":"\t"+e.oldHeader)),t.push("+++ "+e.newFileName+("undefined"===typeof e.newHeader?"":"\t"+e.newHeader));for(var n=0;n<e.hunks.length;n++){var i=e.hunks[n];0===i.oldLines&&(i.oldStart-=1),0===i.newLines&&(i.newStart-=1),t.push("@@ -"+i.oldStart+","+i.oldLines+" +"+i.newStart+","+i.newLines+" @@"),t.push.apply(t,i.lines)}return t.join("\n")+"\n"}function W(e,t,n,i,r,a,l){return U(q(e,t,n,i,r,a,l))}function R(e,t,n,i,r,a){return W(e,e,t,n,i,r,a)}function B(e,t){return e.length===t.length&&H(e,t)}function H(e,t){if(t.length>e.length)return!1;for(var n=0;n<t.length;n++)if(t[n]!==e[n])return!1;return!0}function $(e){var t=oe(e.lines),n=t.oldLines,i=t.newLines;void 0!==n?e.oldLines=n:delete e.oldLines,void 0!==i?e.newLines=i:delete e.newLines}function K(e,t,n){e=z(e,n),t=z(t,n);var i={};(e.index||t.index)&&(i.index=e.index||t.index),(e.newFileName||t.newFileName)&&(J(e)?J(t)?(i.oldFileName=V(i,e.oldFileName,t.oldFileName),i.newFileName=V(i,e.newFileName,t.newFileName),i.oldHeader=V(i,e.oldHeader,t.oldHeader),i.newHeader=V(i,e.newHeader,t.newHeader)):(i.oldFileName=e.oldFileName,i.newFileName=e.newFileName,i.oldHeader=e.oldHeader,i.newHeader=e.newHeader):(i.oldFileName=t.oldFileName||e.oldFileName,i.newFileName=t.newFileName||e.newFileName,i.oldHeader=t.oldHeader||e.oldHeader,i.newHeader=t.newHeader||e.newHeader)),i.hunks=[];var r=0,a=0,l=0,o=0;while(r<e.hunks.length||a<t.hunks.length){var s=e.hunks[r]||{oldStart:1/0},d=t.hunks[a]||{oldStart:1/0};if(X(s,d))i.hunks.push(Z(s,l)),r++,o+=s.newLines-s.oldLines;else if(X(d,s))i.hunks.push(Z(d,o)),a++,l+=d.newLines-d.oldLines;else{var u={oldStart:Math.min(s.oldStart,d.oldStart),oldLines:0,newStart:Math.min(s.newStart+l,d.oldStart+o),newLines:0,lines:[]};G(u,s.oldStart,s.lines,d.oldStart,d.lines),a++,r++,i.hunks.push(u)}}return i}function z(e,t){if("string"===typeof e){if(/^@@/m.test(e)||/^Index:/m.test(e))return A(e)[0];if(!t)throw new Error("Must provide a base reference or pass in a patch");return q(void 0,void 0,t,e)}return e}function J(e){return e.newFileName&&e.newFileName!==e.oldFileName}function V(e,t,n){return t===n?t:(e.conflict=!0,{mine:t,theirs:n})}function X(e,t){return e.oldStart<t.oldStart&&e.oldStart+e.oldLines<t.oldStart}function Z(e,t){return{oldStart:e.oldStart,oldLines:e.oldLines,newStart:e.newStart+t,newLines:e.newLines,lines:e.lines}}function G(e,t,n,i,r){var a={offset:t,lines:n,index:0},l={offset:i,lines:r,index:0};te(e,a,l),te(e,l,a);while(a.index<a.lines.length&&l.index<l.lines.length){var o=a.lines[a.index],s=l.lines[l.index];if("-"!==o[0]&&"+"!==o[0]||"-"!==s[0]&&"+"!==s[0])if("+"===o[0]&&" "===s[0]){var d;(d=e.lines).push.apply(d,C(ie(a)))}else if("+"===s[0]&&" "===o[0]){var u;(u=e.lines).push.apply(u,C(ie(l)))}else"-"===o[0]&&" "===s[0]?Y(e,a,l):"-"===s[0]&&" "===o[0]?Y(e,l,a,!0):o===s?(e.lines.push(o),a.index++,l.index++):ee(e,ie(a),ie(l));else Q(e,a,l)}ne(e,a),ne(e,l),$(e)}function Q(e,t,n){var i=ie(t),r=ie(n);if(ae(i)&&ae(r)){var a,l;if(H(i,r)&&le(n,i,i.length-r.length))return void(a=e.lines).push.apply(a,C(i));if(H(r,i)&&le(t,r,r.length-i.length))return void(l=e.lines).push.apply(l,C(r))}else if(B(i,r)){var o;return void(o=e.lines).push.apply(o,C(i))}ee(e,i,r)}function Y(e,t,n,i){var r,a=ie(t),l=re(n,a);l.merged?(r=e.lines).push.apply(r,C(l.merged)):ee(e,i?l:a,i?a:l)}function ee(e,t,n){e.conflict=!0,e.lines.push({conflict:!0,mine:t,theirs:n})}function te(e,t,n){while(t.offset<n.offset&&t.index<t.lines.length){var i=t.lines[t.index++];e.lines.push(i),t.offset++}}function ne(e,t){while(t.index<t.lines.length){var n=t.lines[t.index++];e.lines.push(n)}}function ie(e){var t=[],n=e.lines[e.index][0];while(e.index<e.lines.length){var i=e.lines[e.index];if("-"===n&&"+"===i[0]&&(n="+"),n!==i[0])break;t.push(i),e.index++}return t}function re(e,t){var n=[],i=[],r=0,a=!1,l=!1;while(r<t.length&&e.index<e.lines.length){var o=e.lines[e.index],s=t[r];if("+"===s[0])break;if(a=a||" "!==o[0],i.push(s),r++,"+"===o[0]){l=!0;while("+"===o[0])n.push(o),o=e.lines[++e.index]}s.substr(1)===o.substr(1)?(n.push(o),e.index++):l=!0}if("+"===(t[r]||"")[0]&&a&&(l=!0),l)return n;while(r<t.length)i.push(t[r++]);return{merged:i,changes:n}}function ae(e){return e.reduce((function(e,t){return e&&"-"===t[0]}),!0)}function le(e,t,n){for(var i=0;i<n;i++){var r=t[t.length-n+i].substr(1);if(e.lines[e.index+i]!==" "+r)return!1}return e.index+=n,!0}function oe(e){var t=0,n=0;return e.forEach((function(e){if("string"!==typeof e){var i=oe(e.mine),r=oe(e.theirs);void 0!==t&&(i.oldLines===r.oldLines?t+=i.oldLines:t=void 0),void 0!==n&&(i.newLines===r.newLines?n+=i.newLines:n=void 0)}else void 0===n||"+"!==e[0]&&" "!==e[0]||n++,void 0===t||"-"!==e[0]&&" "!==e[0]||t++})),{oldLines:t,newLines:n}}function se(e){return Array.isArray(e)?e.map(se).reverse():b(b({},e),{},{oldFileName:e.newFileName,oldHeader:e.newHeader,newFileName:e.oldFileName,newHeader:e.oldHeader,hunks:e.hunks.map((function(e){return{oldLines:e.newLines,oldStart:e.newStart,newLines:e.oldLines,newStart:e.oldStart,linedelimiters:e.linedelimiters,lines:e.lines.map((function(e){return e.startsWith("-")?"+".concat(e.slice(1)):e.startsWith("+")?"-".concat(e.slice(1)):e}))}}))})}function de(e){for(var t,n,i=[],r=0;r<e.length;r++)t=e[r],n=t.added?1:t.removed?-1:0,i.push([n,t.value]);return i}function ue(e){for(var t=[],n=0;n<e.length;n++){var i=e[n];i.added?t.push("<ins>"):i.removed&&t.push("<del>"),t.push(ce(i.value)),i.added?t.push("</ins>"):i.removed&&t.push("</del>")}return t.join("")}function ce(e){var t=e;return t=t.replace(/&/g,"&amp;"),t=t.replace(/</g,"&lt;"),t=t.replace(/>/g,"&gt;"),t=t.replace(/"/g,"&quot;"),t}N.tokenize=function(e){return e.slice()},N.join=N.removeEmpty=function(e){return e},e.Diff=t,e.applyPatch=T,e.applyPatches=F,e.canonicalize=x,e.convertChangesToDMP=de,e.convertChangesToXML=ue,e.createPatch=R,e.createTwoFilesPatch=W,e.diffArrays=E,e.diffChars=r,e.diffCss=g,e.diffJson=O,e.diffLines=p,e.diffSentences=h,e.diffTrimmedLines=f,e.diffWords=d,e.diffWordsWithSpace=u,e.formatPatch=U,e.merge=K,e.parsePatch=A,e.reversePatch=se,e.structuredPatch=q,Object.defineProperty(e,"__esModule",{value:!0})}))},cdbb:function(e,t,n){"use strict";t["a"]={getConfigRulePermission:"/mkt-term-svc/eval/getConfigRulePermission",getTermRule:"/mkt-term-svc/eval/getTermRule",saveTermEval:"/mkt-term-svc/eval/saveTermEval",clickScript:"/mkt-term-svc/api/clickScript",delScript:"/mkt-term-svc/api/delScript",exportScript:"/mkt-term-svc/api/exportScript",getScriptAppraise:"/mkt-term-svc/api/getScriptAppraise",getScriptListInfo:"/mkt-term-svc/api/getScriptListInfo",getScriptPreview:"/mkt-term-svc/api/getScriptPreview",saveScript:"/mkt-term-svc/api/saveScript",uploadScriptMaterial:"/mkt-term-svc/api/uploadScriptMaterial",queryChannelIdAndPlan:"/mkt-term-svc/api/queryChannelIdAndPlan",getScriptName:"/mkt-term-svc/api/getScriptName",queryChannelContentType:"/mkt-term-svc/api/queryChannelContentType",approveRecord:"/mkt-term-svc/term/approveRecord",submitApprove:"/mkt-term-svc/term/submitApprove",qryApproveRejectOpinion:"/mkt-term-svc/term/qryApproveRejectOpinion"}},d39c:function(e,t,n){"use strict";const i="/element-svc/api",r="/preview-svc",a="/mcd-web/action";t["a"]={seapi:{querylistEnumValue:"/element-svc/mcd-plan-label/queryLabelByKey",loadImage:i+"/mcdDimMaterial/loadImage",queryTableList:"/element-svc/api/jx/mcdPlanDef/queryPlanDefPageList",saveStatus:i+"/mcdPlanDef/updatePlanDefStatus",queryDetail:"/element-svc/api/jx/mcdPlanDef/getPlanDetailById",mcdChannelList:i+"/mcdDimMaterial/queryChannel",mcdContList:i+"/mcdDimMaterial/queryContact",mcdPositList:i+"/mcdDimMaterial/queryPosition",getChannels:i+"/mcdDimChannel/pagelistDimChannel",queryContactByChannelId:i+"/iop/operatingPositionManager/queryContactByChannelId.do",queryPosition:i+"/iop/template/queryPosition",getOnlineChannels:i+"/mcdDimChannel/listDimChannel  ",getOnlineChannelsByUser:i+"/mcdDimChannel/jx/listDimChannel",getSysDics:"/plan-svc/action/iop/common/getSysDics",queryDigitalProductContents:i+"/iopDigitalProduct/queryDigitalProductPageList",queryConentDetailById:i+"/iopDigitalProduct/getDigitalProduct",updateDigitalProuctStatus:i+"/iopDigitalProduct/updateDigitalProductStatus",queryMaterialInfo:i+"/mcdDimMaterial/queryMaterialPageList",deleteMaterById:i+"/mcdDimMaterial/deleteMaterial",queryMaterialById:i+"/mcdDimMaterial/getMaterial",saveOrUpdateMaterial:i+"/mcdDimMaterial/saveOrUpdateMaterial",uploadMaterImg:i+"/mcdDimMaterial/uploadMaterialPicture",uploadMaterVideo:i+"/mcdDimMaterial/uploadMaterialVideo",uploadMaterFile:i+"/mcdDimMaterial/uploadMaterialsFile",queryIopContactList:i+"/jx/mcdDimContact/pagelistMcdDimContact",queryContactDetailById:i+"/jx/mcdDimContact/getDimContactDetail",listAllChannelName:i+"/mcdDimChannel/listAllChannelNameAndId",deleteContactById:i+"/mcdDimContact/removeContact",saveOrUpdateContact:i+"/jx/mcdDimContact/saveOrUpdateContact",validateContactName:i+"/mcdDimContact/getValidateContactName",displayChannelList:"/element-svc/api/mcdDimChannel/jx/pagelistDimChannel",validateChannelName:i+"/mcdDimChannel/getValidateChannelName",saveOrUpdateChannel:"/element-svc/api/mcdDimChannel/jx/saveOrUpdateDimChannel",deleteChannelById:i+"/mcdDimChannel/removeDimChannel",channelDetail:i+"/mcdDimChannel/getDimChannel",customerLike:"/preview-svc/jx/chnPre/queryPreData",channelDetail:"/element-svc/mcd-dim-channel-desc/query",uploaChanPromptFile:"/element-svc/api/mcdDimChannel/jx/uploaChanPromptFile",downloadChanPromptFile:"/plan-svc/mcd/chan/prompt/downloadChanPromptFile",queryDimChannelInfo:"/element-svc/api/mcdDimChannel/jx/queryDimChannelInfo",queryChannelEValData:"/groupmarket/enterprise/queryChannelEValData",queryIopOperatingPosition:i+"/mcdDimAdivInfo/pagelistAdivInfo",deleteOperatingPositionById:i+"/mcdDimAdivInfo/removeAdivInfo",queryOperPositionDetail:i+"/mcdDimAdivInfo/getAdivInfo",queryOpelistData:i+"/mcdDimAdivInfo/listChannelInfoAndContactInfo",queryCampsegInfoWithThisPosId:i+"/mcdDimAdivInfo/listpageCampsegInfoWithThisAdivId",getConfigList:i+"/mcdDimAttrConf/queryConfigList",validateOperationName:i+"/mcdDimAdivInfo/getValidateAdivInfoName",getValidateAdivInfoId:i+"/jx/mcdDimAdivInfo/getValidateAdivInfoId",saveOrUpdateOperPosition:i+"/mcdDimAdivInfo/saveOrUpdateAdivInfo",getChannelScheduleList:a+"/channelview/getChannelScheduleList",querySensitiveCust:r+"/mcd-channel-sensitive-cust-conf/querySensitiveCust",saveSensitiveCust:r+"/mcd-channel-sensitive-cust-conf/saveSensitiveCust",deleteSensitiveCust:r+"/mcd-channel-sensitive-cust-conf/deleteSensitiveCust",batchDeleteSensitiveCust:r+"/mcd-channel-sensitive-cust-conf/batchDeleteSensitiveCust",pagelistApproveMaterial:i+"/mcdDimMaterialApprove/pagelistApproveMaterial",approveRecord:i+"/mcdDimMaterialApprove/approveRecord",pagelistMyMaterial:i+"/mcdDimMaterialTemplate/pagelistMyMaterial",pagelistExcellentMaterial:i+"/mcdDimMaterialTemplate/pagelistExcellentMaterial",pagelistHotSpotMaterial:i+"/mcdDimMaterialTemplate/pagelistHotSpotMaterial",updateMaterialStatus:i+"/mcdDimMaterial/updateMaterialStatus",getMaterialApprover:i+"/mcdDimMaterialApprove/getMaterialApprover",getCmpApprovalProcess:i+"/mcdDimMaterialApprove/getCmpApprovalProcess",subMaterialApprove:i+"/mcdDimMaterialApprove/subMaterialApprove",addExcellentMaterialLibrary:i+"/mcdDimMaterialTemplate/addExcellentMaterialLibrary",removeExcellentMaterialLibrary:i+"/mcdDimMaterialTemplate/removeExcellentMaterialLibrary",createExcellentMaterial:i+"/mcdDimMaterialTemplate/createExcellentMaterial",createHotSpotMaterial:i+"/mcdDimMaterialTemplate/createHotSpotMaterial",removeMyMaterial:i+"/mcdDimMaterialTemplate/removeMyMaterial",getIntell:i+"/mcdDimMaterial/getIntelligentMatchingMaterialList"}}},d6d6:function(e,t,n){"use strict";var i=TypeError;e.exports=function(e,t){if(e<t)throw new i("Not enough arguments");return e}},edd0:function(e,t,n){"use strict";var i=n("13d2"),r=n("9bf2");e.exports=function(e,t,n){return n.get&&i(n.get,t,{getter:!0}),n.set&&i(n.set,t,{setter:!0}),r.f(e,t,n)}}}]);