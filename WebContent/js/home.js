$(document).ready(function() {
    //1
    $("#btn-login").click(function() {

        var dat1 = $("#username").val();
        var dat2 = $("#password").val();

        $.get("/try_web/greeting", { name: dat1 }, function(data, status) {

            window.location.href = "showinfo.html?id=" + data.id + "&name=" + data.name + "&cid=" + data.cid + "&subject=" + data.subject + "&time=" + data.time;

        });



    });


});
