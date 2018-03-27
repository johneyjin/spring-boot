/**
 * 在js中获取user.html中的路径；
 * 在spring boot部署到tomcat后或者
 * 在application.properties文件中指定了server.context-path=/的路径；
 * 则必须从后台获取数据必须执行context-path
 * @returns 返回项目路径
 */
function getContextPath() {
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
    return result;
}
$(function(){
	var url=getContextPath();
	$('#dg').datagrid({
		url:url+'/user/getUserInfo',
		columns:[[
			{field:'name',title:'姓名',width:100},
			{field:'age',title:'年龄',width:100},
			{field:'phone',title:'手机号',width:100,align:'right'},
			{field:'address',title:'地址',width:100,align:'right'}
		]],
		onBeforeLoad:function(param){
//			console.info(param);
		},
		onLoadError:function(data){
//			console.info(data);
		}
	});
	var pager = $('#dg').datagrid().datagrid('getPager');
	//将行数变为可选择的行数
	$(pager).pagination({
		//默认显示在页面上的分页行数
		pageSize:10,
		//分页可选择的行数
		pageList: [10,20],
		beforePageText: '第',//页数文本框前显示的汉字  
		afterPageText: '页    共 {pages} 页',  
		displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	});
})