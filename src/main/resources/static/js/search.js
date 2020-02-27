$(function () {
   $('#searchInput').keydown(function (key) {
       if(key.keyCode===13) {
           $("#searchBtn").trigger("click");
           $("#searchBtn1").trigger("click");
           $("#searchBtn2").trigger("click");
       }
   });
   $('#searchBtn').click(function () {
       var query=$('#searchInput').val();
       if(query !=="")
           location.href="/wiki/search?query="+query+"&page=1";
       return false;
   });

    $('#goBtn').click(function () {
        var query=$('#searchInput').val();
        if(query !=="")
            location.href="/wiki/direct?query="+query;
        return false;
    });
});