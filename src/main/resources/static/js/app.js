/**
 * Created by David on 2017/2/26.
 */

/*前端就简单做，根据团队情况做约束和规范，这里就简单写在一个js中*/
//全局定义方便操作
var table;
/*初始化显示数据列表*/
$(document).ready(function () {
    table = $('#account_managements').DataTable({
        ajax: {
            url: '/accountmanagements?page=0&pageSize=20',
            type: "GET",
            data: function (d) {
                d.accountManagementFilter = getData()
            }
        },
        "columns": [
            {"data": "serialNumber"},
            {"data": "name"},
            {"data": "remark"},
            {"data": "status"},
            {"data": "createOn"},
            {"data": "lastLoginTime"},
            {"data": "type"},
            {"data": "serialNumber"},
        ],
        "columnDefs": [{
            "targets": -1,//操作按钮目标列
            "data": null,
            "render": function (data, type, row) {
                var status = '"' + row.status + '"';
                var id = '"' + data + '"';
                var html = "<a href='javascript:void(0);' onclick='changeAccountManagementStatus(" + id + "," + status + ")'  class='up btn btn-default btn-xs'>切换状态</a>"
                html += "<a href='javascript:void(0);'   onclick='deleteAccountManagement(" + id + ")'  class='down btn btn-default btn-xs'>删除</a>"
                return html;
            }
        }]
    });
});
/*初始化时间选择控件*/
$(function () {
    $("#dateRange").daterangepicker({
        "locale": {
            "format": "YYYY/MM/DD"
        }
    });
});

/*初始化select为select2控件*/
$('select').select2();

/*构建账户管理对象模型*/
function AccountManagement(serialNumber, name, remark, status, createOn, lastLoginTime, type) {
    this.serialNumber = serialNumber;
    this.name = name;
    this.remark = remark;
    this.status = status;
    this.createOn = createOn;
    this.lastLoginTime = lastLoginTime;
    this.type = type;
}

/*构建账户管理筛选对象模型*/
function AccountManagementFilter(serialNumber, name, dateRange, type) {
    this.serialNumber = serialNumber;
    this.name = name;
    this.dateRange = dateRange;
    this.type = type;
}

var filterData = null;
//点击筛选按钮
$("#filter").click(function () {
    var serialNumber = $("#serialNumber").val();
    var name = $("#name").val();
    var dateRange = $("#dateRange").val();
    var type = $("#type").val();
    //构建账户管理临时过滤条件对象
    var temp = new AccountManagementFilter(serialNumber, name, dateRange, type);
    //第一次查询
    if (filterData == null) {
        filterData = temp;
    } else {
        //判断是否重复点击筛选按钮
        if (filterData.name == temp.name && filterData.serialNumber == temp.serialNumber
            && filterData.dateRange == temp.dateRange && filterData.type == temp.type) {
            alert("筛选数据没任何变化，不必更新数据，防重复设置....");
            return
        } else {
            //更新过滤数据
            filterData = temp;
        }
    }
    //根据过滤数据进行筛选
    //这里重新查询数据，重绘DataTables
    //设置过滤值
    table.ajax.reload();
});

//点击注册按钮
var lastAccountManagement;//防重设置
$("#register").click(function () {
    var name = $("#registerName").val();
    if (name == '') {
        alert("示范性检验，名称不能为空");
        return;
    }
    var remark = $("#registerRemark").val();
    var status = $("input[name='registerStatus']:checked").val()
    var type = $("#registerType").val();
    var accountManagement = new AccountManagement(null, name, remark, status, null, null, type);
    if (lastAccountManagement == null) {
        lastAccountManagement = accountManagement;
    } else {
        //检测相同
        if (lastAccountManagement.name == accountManagement.name && lastAccountManagement.remark == accountManagement.remark
            && lastAccountManagement.status == accountManagement.status && lastAccountManagement.type == accountManagement.type) {
            alert("重复性按钮，简单忽略");
            return
        }
    }
    $.ajax({
        type: "POST",
        url: "/accountmanagements",
        contentType: "application/json",
        data: JSON.stringify(lastAccountManagement),
        success: function (resp) {
            alert("注册成功！！！新编码为：" + resp.serialNumber);
            table.ajax.reload();
        }
    });
})

/*获取筛选序列化对象*/
function getData() {
    return JSON.stringify(filterData);
}

function deleteAccountManagement(serialNumber) {
    $.ajax({
        type: "DELETE",
        url: "/accountmanagements/" + serialNumber,
        success: function (resp) {
            alert("删除成功！！！编码为：" + resp.serialNumber);
            table.ajax.reload();
        }
    });
}

function changeAccountManagementStatus(serialNumber, status) {
    if (status == "启封") {
        status = "封存";
    } else {
        status = "启封";
    }
    $.ajax({
        type: "put",
        url: "/accountmanagements/" + serialNumber+"/status",
        data: {
            "status": status
        },
        success: function (resp) {
            alert(status + "成功！！！编码为：" + serialNumber);
            table.ajax.reload();
        }
    });
}
