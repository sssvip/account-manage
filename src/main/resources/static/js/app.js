/**
 * Created by David on 2017/2/26.
 */

/*前端就简单做，根据团队情况做约束和规范，这里就简单写在一个js中*/

/*初始化显示数据列表*/
$(document).ready(function () {
    var table = $('#account_managements').DataTable({
        ajax: '/accountmanagements?page=0&pageSize=20',
        "columns": [
            {"data": "serialNumber"},
            {"data": "name"},
            {"data": "remark"},
            {"data": "status"},
            {"data": "createOn"},
            {"data": "lastLoginTime"},
            {"data": "type"}
        ]
    });
});
/*初始化时间选择控件*/
$(function () {
    $("#daterange").daterangepicker();
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
    alert("test" + filterData.serialNumber + "-" + filterData.name + "-" + filterData.dateRange + "-" + filterData.type);
});

//点击注册按钮

$("#register").click(function () {
    var name = $("#registerName").val();
    var remark = $("#registerRemark").val();
    var status = $("input[name='registerStatus']:checked").val()
    var type = $("#registerType").val();
    var accountManagement = new AccountManagement(null, name, remark, status, null, null, type);
    alert(JSON.stringify(accountManagement));
    $.ajax({
        type: "POST",
        url: "/accountmanagements",
        contentType: "application/json",
        data: JSON.stringify(accountManagement),
        success: function (resp) {
            alert("注册成功！！！新编码为："+resp.serialNumber);
        }
    });
})

//更新表数据
function updateTable() {
    var table = $('#account_managements').DataTable({
        ajax: '/accountmanagements?page=0&pageSize=3',
        "columns": [
            {"data": "serialNumber"},
            {"data": "name"},
            {"data": "remark"},
            {"data": "status"},
            {"data": "createOn"},
            {"data": "lastLoginTime"},
            {"data": "type"}
        ]
    });
}