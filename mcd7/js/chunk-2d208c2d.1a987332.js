(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d208c2d"],{a5be:function(e,t,r){"use strict";r.r(t);var i=function(){var e=this,t=e._self._c;return t("section",{staticStyle:{"overflow-x":"hidden"}},[t("iframe",{ref:"iframe",staticStyle:{height:"100vh"},attrs:{src:e.src,scrolling:"auto",width:"100%",height:"100%",frameborder:"0",id:"iframeNow",name:"iframe"},on:{load:e.load}})])},s=[],o={data(){return{iframeWin:{},prefix:""}},computed:{},created(){this.init()},mounted(){this.iframeWin=this.$refs.iframe.contentWindow},methods:{load(){var e=this.$refs.iframe;const t=window.sessionStorage.getItem("IOP_TOKEN");let r={token:t,params:window.sessionStorage.getItem("params")};e.contentWindow.postMessage(r,"*")},init(){const e="production",t=location.origin,r=sessionStorage.getItem("userId"),i=sessionStorage.getItem("userNameCn");this.prefix=t,this.src="production"===e?`${t}/5g/#/RedirectUrl?route=MsgSend&userId=${r}&userName=${i}`:`http://10.19.92.24:8888/5g/#/RedirectUrl?route=MsgSend&userId=${r}&userName=${i}`}}},n=o,a=r("2877"),d=Object(a["a"])(n,i,s,!1,null,null,null);t["default"]=d.exports}}]);