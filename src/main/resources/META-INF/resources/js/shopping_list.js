
var user = shopping_list_user;

var itemNameMap = {};

displayAllItems();

function addItemFunction() {
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
} 

$("#addButton").click(addItemFunction);

$("#newItemInput").keypress(event => {
    var key = event.which;
    if (key === 13) { // 13 is the enter key code      
      addItemFunction();
    }
  });
  

function createItemRow(item) {
    var checked = item.done === true? "checked" : "";
    var itemId = item.id;
    return '<input type="checkbox" class="itemCheckbox" name="itemCheckbox'+ itemId + '" value="'+ itemId + '" ' + checked +'>' +
           '<label for="itemCheckbox'+ itemId + '">' + item.text + '</label>' + createDeleteIcon(itemId);  
}

function createDeleteIcon(itemId) {
    return '<span class="itemRemoveIcon"'+ ' value="'+itemId +'">&#11199;</span>'
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
    itemNameMap = {};
    loadAllItemsAndExecute(items => {
        var outputHtml = "";
        items.forEach(item => {
            outputHtml += '<div id="itemId_' + item.id + '">' + createItemRow(item) + '</div>';
            itemNameMap["_" + item.id] = item.text;
        });
        $("#itemList").html(outputHtml);
        addEventsToItemCheckboxes();
        addEventsToItemRemoveIcons();
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

function sendDeleteRequest(itemId) {
    var success = function(data, textStatus, jqXHR) {
        displayAllItems();
    }
    var settings = {
        url: "http://localhost:8080/api/v1/items/" + itemId,
        contentType: "application/json",
        method: "DELETE",
        success: success
    }
    $.ajax(settings);    
}

function addEventsToItemRemoveIcons() {
    $(".itemRemoveIcon").click(event => {
        var icon = event.currentTarget;
        var itemId = icon.parentElement.id.split("_")[1];
        // console.log("Remove icon was clicked. " + itemId);
        var text = itemNameMap["_" + itemId];
        if (confirm('Really delete ' + text + '?')) {
            sendDeleteRequest(itemId);
        }
    });    
}

