$(function(){
	$('#dg').datagrid({
		url:'/game/getGames',
		columns:[[
			{field:'name',title:'游戏名',width:100},
		]],
		onBeforeLoad:function(param){
			console.info(param);
		},
		onLoadError:function(data){
			console.info(data);
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