
var itemNameMap = {};

var user = "Duke Nukem";

displayAllItems();

function addItemFunction() {
    const newItemInput = document.getElementById("newItemInput");
    const enteredText = newItemInput.value;
    if (enteredText.trim() === "") {
        return;
    }
    const now = new Date().toISOString(); // TODO Correct timezone
    const data = {
        "text": enteredText,
        "modifiedBy": user,
        "modifiedAt": now
    };	
    
    fetch("http://localhost:8080/api/v1/items", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {			
            throw new Error('Network response was not okay: Status Code ' + response.status);
        }
    })
    .then(() => {
        displayAllItems();
        newItemInput.value = "";
    })
    .catch(error => {
		alert(error);
    });
}

document.getElementById("addButton").addEventListener('click', addItemFunction);

document.getElementById("newItemInput").addEventListener('keypress', event => {
    if (event.key === 'Enter') {
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
    // return '<i class="glyphicon glyphicon-remove itemRemoveIcon"'+ ' value="'+itemId +'"></i>';
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
	
	console.log("displayAllItems");
	
    itemNameMap = { };
    loadAllItemsAndExecute(items => {
        var outputHtml = "";
        items.forEach(item => {
            outputHtml += '<div id="itemId_' + item.id + '">' + createItemRow(item) + '</div>';
            itemNameMap["_" + item.id] = item.text;
        });
        document.getElementById("itemList").innerHTML = outputHtml;
        addEventsToItemCheckboxes();
        addEventsToItemRemoveIcons();
    });    
}

function addEventsToItemCheckboxes() {
	for (cb of document.getElementsByClassName("itemCheckbox")) {
		cb.addEventListener('click', event => {

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
	for (ri of document.getElementsByClassName("itemRemoveIcon")) {
		ri.addEventListener('click', event => {
			var icon = event.currentTarget;
			var itemId = icon.parentElement.id.split("_")[1];
			// console.log("Remove icon was clicked. " + itemId);
			var text = itemNameMap["_" + itemId];
			if (confirm('Really delete ' + text + ' ?') == true) {
				sendDeleteRequest(itemId);
			}		
		});		
	}	
}


