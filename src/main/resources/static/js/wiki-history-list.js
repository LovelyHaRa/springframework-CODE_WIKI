$(function () {
    $.each(historyList, function (index, item) {
        let className = ".history"+index;
        console.log(className);
        $(className).attr("onclick", "location.href='/wiki/" + item.codeId + "/" + item["revisionDoc"] + "'");
    });
});