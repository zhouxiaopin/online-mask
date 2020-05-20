/**
 *
 * @type {{ajaxRequest: Window.ticc.ajaxRequest, getBrowser: Window.ticc.getBrowser, getFixWidth: Window.ticc.getFixWidth, alertInfo: Window.ticc.alertInfo, alertQuestion: Window.ticc.alertQuestion, alertWarning: Window.ticc.alertWarning, alertError: Window.ticc.alertError, confirm: Window.ticc.confirm, addFavorite: Window.ticc.addFavorite, isNullOrEmpty: Window.ticc.isNullOrEmpty, isNull: Window.ticc.isNull, arrayIsNullOrEmpty: Window.ticc.arrayIsNullOrEmpty, setCountUp: Window.ticc.setCountUp, isNumber: Window.ticc.isNumber}}
 */
window.sk = {
    /**
     * ajax请求
     * @param path  请求路径
     * @param data   参数
     * @param success   请求成功回调函数
     * @param ptype     请求方式
     * @param error     请求失败回调函数
     * @param timeout   请求超时时间
     * @param async      是否是异步请求
     */
    ajaxRequest: function (path, data, success, ptype, error, timeout, async) {

        if (!ptype || ptype.toLowerCase() != "get") {
            ptype = "post";
        }
        if (async == undefined) {
            async = true;
        }
        $.ajax({
            url: path,
            type: ptype,
            data: data,
            async: async,
            dataType: "json",
            timeout: (timeout == undefined || timeout == null) ? 20000 : timeout,
            success: success,
            error: function (xhr, textStatus, errorThrown) {
                console.log(errorThrown);
                if (errorThrown == 'timeout') {
                    sk.failMsg('网络请求失败', 2000);
                }
            }

        });
    },
    getBrowser: function () {
        var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
        var isOpera = userAgent.indexOf("Opera") > -1;
        //判断是否Opera浏览器
        if (isOpera) {
            return "Opera"
        }
        //判断是否Firefox浏览器
        if (userAgent.indexOf("Firefox") > -1) {
            return "Firefox";
        }
        //判断是否Chrome浏览器
        if (userAgent.indexOf("Chrome") > -1) {
            return "Chrome";
        }
        //判断是否Safari浏览器
        if (userAgent.indexOf("Safari") > -1) {
            return "Safari";
        }
        //判断是否IE浏览器
        if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {
            return "IE";
        }
    },
    /**
     *
     * @param element 元素
     * @param percent   百分比
     * @returns {number}
     */
    getFixWidth: function (element, percent) {
        return ($(element).width()) * percent;
    },
    successMsg: function (msg, time) {
        if (!time) {
            time = 2500;
        }
        layer.msg(msg, {icon: 1, time: time})
    },
    failMsg: function (msg, time) {
        if (!time) {
            time = 2000;
        }
        layer.msg(msg, {icon: 2, time: time})
    },
    successFaceMsg: function (msg, time) {
        if (!time) {
            time = 2500;
        }
        layer.msg(msg, {icon: 6, time: time})
    },
    failFaceMsg: function (msg, time) {
        if (!time) {
            time = 2500;
        }
        layer.msg(msg, {icon: 5, time: time})
    },
    msg: function (msg, icon, time) {
        if (!time) {
            time = 2500;
        }
        layer.msg(msg, {icon: icon, time: time})
    },
    /**
     * 添加收藏
     * @param url
     * @param title
     */
    addFavorite: function (url, title) {
        try {
            window.external.AddFavorite(url, title);
        } catch (e) {
            ticc.alertInfo("加入收藏失败，请使用Ctrl+D进行添加");
        }
    },
    isNullOrEmpty: function (obj) {
        if (null == obj || undefined == obj || "" == obj) {
            return true;
        }
        return false;
    },
    isNull: function (obj) {
        if (null == obj || undefined == obj) {
            return true;
        }
        return false;
    },
    arrayIsNullOrEmpty: function (obj) {
        if (null == obj || undefined == obj || obj.length <= 0) {
            return true;
        }
        return false;
    },
    /*
         * 设置数字滚动
         * target：目标元素的ID, start：开始值, end：结束值, decimals ：小数位数,默认值是0,duration： 动画延迟秒数，默认值是2
         */
    setCountUp: function (target, start, end, decimals, duration) {
        var countUp_item = new CountUp(target, start, end, decimals || 0, duration || 2, {
            useEasing: true,
            useGrouping: true,
            separator: ',',
            decimal: '.'
        });
        if (!countUp_item.error) {
            countUp_item.start();
        } else {
            console.error("数字滚动" + countUp_item.error);
        }
    },
    /**
     * 校验只要是数字（包含正负整数，0以及正负浮点数）就返回true
     * @param val
     * @returns {boolean}
     */
    isNumber: function (val) {

        var regPos = /^\d+(\.\d+)?$/; //非负浮点数
        var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
        if (regPos.test(val) || regNeg.test(val)) {
            return true;
        } else {
            return false;
        }

    }
};


//禁用记录
function updateDisableRs(obj, id, pk) {
    layer.confirm('确认要禁用吗？', function (index) {
        var url = "updateRecordStatus";
        var param = {recordStatus: '02'};
        param[pk] = id;
        sk.ajaxRequest(url, param, function (r) {
            if (r.code == '0') {
                $(obj).parents("tr").find("#td-manage").prepend('<a title="启用" href="javascript:;" onclick="updateAbleRs(this,' + id + ',\'' + pk + '\')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe615;</i></a>');

                $.each(recordStatus, function (index, item) {
//                            console.log(JSON.stringify(item))
                    if (item.code == skConst.RecordStatus.DISABLE) {
                        $(obj).parents("tr").find("#td-status").html('<span class="label radius">' + item.name + '</span>');
                        return false;
                    }
                });
                $(obj).remove();

                sk.failFaceMsg(r.msg);
            } else {
                sk.failMsg(r.msg);
            }

        });
    });

}

//启用记录
function updateAbleRs(obj, id, pk) {
    layer.confirm('确认要启用吗？', function (index) {
        var url = "updateRecordStatus";
        var param = {recordStatus: '01'};
        param[pk] = id;
        sk.ajaxRequest(url, param, function (r) {
            if (r.code == '0') {
                $(obj).parents("tr").find("#td-manage").prepend('<a title="禁用" href="javascript:;" onclick="updateDisableRs(this,' + id + ',\'' + pk + '\')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe631;</i></a>');
                $.each(recordStatus, function (index, item) {
//                            console.log(JSON.stringify(item))
                    if (item.code == skConst.RecordStatus.ABLE) {
                        $(obj).parents("tr").find("#td-status").html('<span class="label label-success radius">' + item.name + '</span>');
                        return false;
                    }
                });
                $(obj).remove();
                sk.successFaceMsg(r.msg);
            } else {
                sk.failMsg(r.msg);
            }

        });
    });

}

//删除
function to_del(obj, id) {
    layer.confirm('确认要删除吗？', function (index) {
        var url = "delete";
        var param = {ids: [id]};
        sk.ajaxRequest(url, param, function (r) {
            if (r.code == '0') {
                $(obj).parents("tr").remove();
                sk.successMsg(r.msg);
            } else {
                sk.failMsg(r.msg);
            }

        });
    });
}

//批量删除
function batchDel() {
    // if ($(".checkchild:checked").length < 1) {
    //     sk.successFaceMsg('请选择要提交的记录!');
    //     return;
    // }
    if ($(".checkchild:checked").length < 2) {
        sk.successFaceMsg('最少选择2条记录!');
        return;
    }
    //获取所有选中的id
    var ids = [];
    $.each($('input:checkbox:checked'), function () {
        if ($(this).val().trim() == "on") {
            return;
        }
        ids.push($(this).val().trim());
    });
    layer.confirm('确认要批量删除吗？', function (index) {
        layer.close(index);
        var url = "delete";
        var param = {ids: ids};
        sk.ajaxRequest(url, param, function (r) {
            if (r.code == '0') {
//                        $(obj).parents("tr").remove();
                $.each($('input:checkbox:checked'), function () {
                    if ($(this).val().trim() == "on") {
                        return;
                    }
                    $(this).parents("tr").remove();
                });
                sk.successMsg(r.msg);
            } else {
                sk.failMsg(r.msg);
            }

        });
    });
}

//批量硬删除
function batchRealDel() {
    // if ($(".checkchild:checked").length < 1) {
    //     sk.successFaceMsg('请选择要提交的记录!');
    //     return;
    // }
    if ($(".checkchild:checked").length < 2) {
        sk.successFaceMsg('最少选择2条记录!');
        return;
    }
    //获取所有选中的id
    var ids = [];
    $.each($('input:checkbox:checked'), function () {
        if ($(this).val().trim() == "on") {
            return;
        }
        ids.push($(this).val().trim());
    });
    layer.confirm('这个操作很危险，确认要批量硬删除吗？', function (index) {
        layer.close(index);
        var url = "realDelete";
        var param = {ids: ids};
        sk.ajaxRequest(url, param, function (r) {
            if (r.code == '0') {
//                        $(obj).parents("tr").remove();
                $.each($('input:checkbox:checked'), function () {
                    if ($(this).val().trim() == "on") {
                        return;
                    }
                    $(this).parents("tr").remove();
                });
                sk.successMsg(r.msg);
            } else {
                sk.failMsg(r.msg);
            }

        });
    });
}

//DataTable配置一些默认参数
function SkDataTable(tableId,obj) {
    var defa = {
        "lengthChange": true,
        "searching": false,//禁用检索
        "ordering": false,
        "autoWidth": false,
        'pagingType': 'full_numbers',
        "pageLength": 10,
        "lengthMenu": [ 5,10, 25, 50, 75, 100],
        "serverSide": true,
        "bInfo": true, //是否显示页脚信息，DataTables插件左下角显示记录数
        "bProcessing": true, //DataTables载入数据时，是否显示‘进度’提示
        "oLanguage": { //国际化配置
            //"sInfo" : "从 _START_ 到  _END_ 条记录 总记录数为 _TOTAL_ 条",
            "sInfoEmpty": "当前显示 0 到 0 条，共 0 条记录",
            "sInfoFiltered": "(每页 _MAX_ 条)",
        },
        "columns": [],
        "ajax": {
            "url": "query",
            "type": "POST",
            "data": function (d) {
            },
        },
    };

    if (obj.lengthChange){
        defa.lengthChange = obj.lengthChange;
    }
    if (obj.searching){
        defa.searching = obj.searching;
    }
    if (obj.ordering){
        defa.ordering = obj.ordering;
    }
    if (obj.autoWidth){
        defa.autoWidth = obj.autoWidth;
    }
    if (obj.pagingType){
        defa.pagingType = obj.pagingType;
    }
    if (obj.pageLength){
        defa.pageLength = obj.pageLength;
    }
    if (obj.serverSide){
        defa.serverSide = obj.serverSide;
    }
    if (obj.bInfo){
        defa.bInfo = obj.bInfo;
    }
    if (obj.bProcessing){
        defa.bProcessing = obj.bProcessing;
    }
    if (obj.oLanguage){
        defa.oLanguage = obj.oLanguage;
    }
    if (obj.columns){
        defa.columns = obj.columns;
    }
    if (obj.ajax){
        if (obj.ajax.url){
            defa.ajax.url = obj.ajax.url;
        }
        if (obj.ajax.data){
            defa.ajax.data = obj.ajax.data;
        }

    }

    return $(tableId).DataTable(defa);
}

//提示
function blockPage(msg){
    var massge = '请稍候,提交数据中...';
    if (msg){
        massge = msg;
    }
    $.blockUI({
        message: '<img src="'+basePath+'static/images/loading_s.gif" /><b>'+massge+'</b>',
        css: {
            border: '2px solid #64BBE9',
        },
        overlayCSS: {opacity:0.3}
    });
}
//销毁提示
function unblockPage(){
    $.unblockUI();
}



/*****************************************************************/

/***********************h-ui.admin扩展开始*************************/

/**
 *
 * @param url   请求地址
 * @param param 请求参数
 * @param title 标题
 * @param w 弹出层宽度
 * @param h 弹出层高度
 * @param end 结束事件
 */
function requestPage(url,param,title,w,h,end){
    sk.ajaxRequest('authValidate', param, function (r) {
            if (r.code == '0') {
                if (w=="-1"&&h=="-1"){
                    layer_show_full(title, url, end);
                }else{
                    layer_show(title, url, w, h, end);
                }
            }else{
                sk.failMsg(r.msg);
            }
    });
}
function requestPage2(url,authUrl,param,title,w,h,end){
    sk.ajaxRequest(authUrl, param, function (r) {
            if (r.code == '0') {
                layer_show(title, url, w, h, end);
            }else{
                sk.failMsg(r.msg);
            }
    });
}
/***********************h-ui.admin扩展结束*************************/