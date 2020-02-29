$(function () {
   $('#inp-search').keydown(function (key) {
       if(key.keyCode===13) {
           $("#btn-search").trigger("click");
       }
   });
   $('#btn-search').click(function () {
       var query=$('#inp-search').val();
       if(query !=="")
           location.href="/wiki/search?query="+query+"&page=1";
       return false;
   });

    $('#btn-shortcuts').click(function () {
        var query=$('#inp-search').val();
        if(query !=="")
            location.href="/wiki/direct?query="+query;
        return false;
    });
});