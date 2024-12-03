
// see https://philna.sh/blog/2021/04/11/dont-ever-write-your-own-function-to-parse-url-parameters/
var user = new URLSearchParams(window.location.search.substring(1)).get("user");

if (user === null) {
    document.getElementById("bigTitle").textContent = "Enter your name, please";
} else {
    document.getElementById("bigTitle").textContent = "Hello " + user;
    
    // document.forms['user'].elements['user'].value = user;
    document.forms.user.user.value = user;
}

displayAllItems();

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

function addEventsToItemCheckboxes() {
    $(".itemCheckbox").click(event => {
        var checkbox = event.currentTarget;
        var itemId = checkbox.value;
        var isCheckedNow = checkbox.checked;        
        
        var now = new Date().toISOString(); // TODO Correct timezone
        var data = {
            "done": isCheckedNow,
            "modifiedAt": now,
            "modifiedBy": user
        }    
        var success = function(data, textStatus, jqXHR) {
            displayAllItems();
        }
        var settings = {
            url: "http://localhost:8080/api/v1/items/" + itemId,
            contentType: "application/json",
            method: "PATCH",
            data: JSON.stringify(data),
            success: success
        }
        $.ajax(settings);    
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
