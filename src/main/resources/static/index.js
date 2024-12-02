
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
    var itemId = item.id;
    return '<input type="checkbox" class="itemCheckbox" name="itemCheckbox'+ itemId + '" value="'+ itemId + '" ' + checked +'>' +
           '<label for="itemCheckbox'+ itemId + '">' + item.text + '</label>';  
}

function loadAllItemsAndExecute(executeFunction) {    
    var success = function(data, textStatus, jqXHR) {
        var items = [];
        for (var i = 0; i < data.length; i++) {            
            items.push(data[i]);
        }
        executeFunction(items);
    }
    var settings = {
        url: "http://localhost:8080/api/v1/items",
        contentType: "application/json",
        method: "GET",
        success: success
    }
    $.ajax(settings);
}

function loadItemsAndExecuteOnEachItem(executeFunction) {    
    var success = function(data, textStatus, jqXHR) {
        for (var i = 0; i < data.length; i++) {            
            executeFunction(data[i]);
        }
    }
    var settings = {
        url: "http://localhost:8080/api/v1/items",
        contentType: "application/json",
        method: "GET",
        success: success
    }
    $.ajax(settings);
}

function displayAllItems() {
    loadAllItemsAndExecute(items => {
        var outputHtml = "";
        items.forEach(item => {
            outputHtml += "<div>" + createItemRow(item) + "</div>";
        });
        $("#itemList").html(outputHtml);
        addEventsToItemCheckboxes();
    });    
}

displayAllItems();

function addEventsToItemCheckboxes() {
    $(".itemCheckbox").click(event => {
        var checkbox = event.currentTarget;
        var itemId = checkbox.value;
        var isCheckedNow = checkbox.checked;        
        console.log("\titemId " + itemId +" (" + isCheckedNow + ")");

        // TODO
    });    
}


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
