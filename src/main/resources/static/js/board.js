$(function () {
    $.each(postList, (index, item) => {
        let className = ".post"+index;
        console.log(className);
        $(className).attr("onclick", "location.href='/board/post/" + item.id + "'");
    });
    $('.btn-write').click(function () {
        location.href="/board/write";
    });
});