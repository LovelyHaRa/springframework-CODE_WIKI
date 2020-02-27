$(function () {
    $("#inputEmail").on("propertychange change keyup paste input", function() {
        $("#inputEmail").removeClass("is-invalid");
    });
    $("#inputName").on("propertychange change keyup paste input", function() {
        $("#inputName").removeClass("is-invalid");
    });
});