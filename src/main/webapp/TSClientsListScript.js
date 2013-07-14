$('#tsClientsListPage').live('pageshow', function(event) {
    var serviceURL = 'rest/virtualservers/1/clients';

    $.ajax({
        type: "GET",
        url: serviceURL,
        data: param = "",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: successFunc,
        error: errorFunc
    });

    function successFunc(data, status) {

        // creating html string
        var listString = '<ul data-role="listview" id="customerList">';

        // running a loop
        $.each(data, function(index, value) {
            listString += '<li>' + this.client_nickname + '</li>';
        });
        listString += '</ul>';

        //appending to the div
        $('#tsClientsList').html(listString);

        // refreshing the list to apply styles
        $('#tsClientsList ul').listview();
    }

    function errorFunc(xhr, ajaxOptions, thrownError) {
        alert(xhr.status + " " + xhr.responseText);
    }
});