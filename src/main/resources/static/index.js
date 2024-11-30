
// console.log("Hello " + user);

// alert("hello " + user);

// document.getElementsByTagName("span")[0].textContent = "" + new Date();

$("#addButton").click(event => {
    var enteredText = $("#newItemInput").val();
    if ($.trim(enteredText) === "") {
        return;
    }
    var now = new Date().toISOString(); // TODO Correct timezone
    var data = {
        "text": enteredText,
        "modifiedBy": user,
        "modifiedAt": now
    }    
    var success = function(data, textStatus, jqXHR) {
        /*
        console.log("data:\n" + data);
        console.log("textStatus:\n" + textStatus);
        console.log("jqXHR:\n" + jqXHR);
        */
        displayAllItems();
        $("#newItemInput").val("");
    }
    var settings = {
        url: "http://localhost:8080/api/v1/items",
        contentType: "application/json",
        method: "POST",
        data: JSON.stringify(data),
        success: success
    }
    $.ajax(settings);
});


function createItemRow(item) {
    var checked = item.done === true? "checked" : "";
    return '<input type="checkbox" class="itemCheckbox" name="itemCheckbox'+ item.id + '" value="'+ item.id + '" ' + checked +'>' +
           '<label for="itemCheckbox'+ item.id + '">' + item.text + '</label>';  
}

function loadAllItems() {
    var items = [];
    var success = function(data, textStatus, jqXHR) {
        for (var i = 0; i < data.length; i++) {            
            items.push(data[i]);
        }
    }
    var settings = {
        url: "http://localhost:8080/api/v1/items",
        contentType: "application/json",
        method: "GET",
        success: success
    }
    $.ajax(settings);
    return items;
}


function displayAllItems() {    
    console.log("displayAllItems");

    var outputHtml = "";
    var items = loadAllItems();
    console.log(items.length);
    console.log(items);

    for (var i = 0; i < items.length; i++) {
        console.log(items[i]);
    }
    items.forEach(item => {
        console.log("++++++++++++ " + item);
        outputHtml += "<div>" + createItemRow(item) + "</div>";
    });
    $("#itemList").html(outputHtml);
}

displayAllItems();


/*
POST http://localhost:8080/recordings HTTP/1.1
content-type: application/json

{
    "beginTime": "2024-12-24T20:30:00",
    "endTime": "2024-12-24T23:15:00",
    "url": "http://ndr-kultur.de/stream",
    "title": "Weihnachtsspezial",
    "details": "Diversa",
    "id": "3"
}
*/
