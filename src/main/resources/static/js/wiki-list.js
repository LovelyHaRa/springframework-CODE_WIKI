$(function () {
    $.each(wikiList, function (index, item) {
        let className = ".wiki"+index;
        console.log(className);
        $(className).attr("onclick", "location.href='/wiki/" + item.codeId + "'");
    });
});