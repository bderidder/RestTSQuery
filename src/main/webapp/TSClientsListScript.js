$('#tsClientsListPage').on('pageshow', function(event) {

    var clientsServiceURL = 'rest/virtualservers/1/clients';
    var channelsServiceURL = 'rest/virtualservers/1/channels';

    $.when($.ajax({
        type: "GET",
        url: clientsServiceURL,
        data: param = "",
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }), $.ajax({
        type: "GET",
        url: channelsServiceURL,
        data: param = "",
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    })).then(successFunc, errorFunc);

    function successFunc(responseClients, responseChannels) {

        var clientsList = responseClients[0];
        var channelsList = responseChannels[0];

        // creating html string
        var listString = '<ul data-role="listview" id="clientsList">';

        if (clientsList.length === 0) {
            listString += '<li>There is nobody online right now</li>';
            return;
        }

        for (var i = 0; i < channelsList.length; i++) {

            var channel = channelsList[i];
            var channelId = channel.cid;

            var clientsLi = '';

            for (var j = 0; j < clientsList.length; j++) {

                var client = clientsList[j];

                if (client.cid === channelId) {
                    clientsLi += '<li>' + client.client_nickname + '</li>';
                }
            }

            if (clientsLi !== '')
            {
                listString += '<li data-role="list-divider">Channel ' + channel.channel_name + '</li>';
                listString += clientsLi;
            }
        }

        listString += '</ul>';

        //appending to the div
        $('#tsClientsList').html(listString);

        // refreshing the list to apply styles
        $('#tsClientsList ul').listview();
    }

    function errorFunc(xhr, ajaxOptions, thrownError) {

        var errorString = '';

        errorString += '<ul data-role="listview" id="clientsList">';
        errorString += '<li>There was an error, try again later</li>';
        errorString += '</ul>';

        //appending to the div
        $('#tsClientsList').html(errorString);

        // refreshing the list to apply styles
        $('#tsClientsList ul').listview();
    }
});