/* Global variables */
let addForm, addDate, addText, addImage, addUrl;
let editForm, editDate, editText, editImage, editId, editUrl;
let entriesHtml;

const BASE_URL = location.protocol + '/index/' + location.host + "/api/";

function initializeGlobalVariables() {
    addForm = document.getElementById('addForm');
    addDate = document.getElementById('date');
    addText = document.getElementById('text');
    addImage = document.getElementById('image');
    addUrl = document.getElementById('url');
    editForm = document.getElementById('editForm');
    editDate = document.getElementById('edit-date');
    editText = document.getElementById('edit-text');
    editImage = document.getElementById('edit-image');
    editUrl = document.getElementById('edit-url');
    editId = document.getElementById('edit-id');
    entriesHtml = document.getElementById('entries');
}

/* When ready document */
window.addEventListener("load", () => {
    initializeGlobalVariables();

    /**
     * Takes care of the form submission
     */
    addForm.addEventListener('submit', (event) => {
        event.preventDefault();
        $('#addModal').modal('hide');
        let img = addImage.files[0];
        let url = addUrl.value;
        if (img != undefined) {
            var reader = new FileReader();
            reader.onload = ((e) => {
                let result = e.target.result;
                addEntry(addDate.value, addText.value, result);
                resetFields(addDate, addText, addImage, addUrl);
            });
            reader.readAsDataURL(img);
        } else if (url != ""){
            addEntry(addDate.value, addText.value, url);
            resetFields(addDate, addText, addImage, addUrl);
        } else {
            addEntry(addDate.value, addText.value, "");
            resetFields(addDate, addText, addImage, addUrl);
        }
    });

    /**
     * Takes care of the form submission
     */
    editForm.addEventListener('submit', (event) => {
        event.preventDefault();
        $('#editModal').modal('hide');
        let img = editImage.files[0];
        let url = editUrl.value;
        if (img != undefined) {
            var reader = new FileReader();
            reader.onload = ((e) => {
                let result = e.target.result;
                editEntry(editId.value, editDate.value, editText.value, result);
            });
            reader.readAsDataURL(img);
        } else if (url != ""){
            editEntry(editId.value, editDate.value, editText.value, url);
            resetFields(editId, editDate, editText, editImage, editUrl);
        } else {
            editEntry(editId.value, editDate.value, editText.value, "");
            resetFields(editId, editDate, editText, editImage, editUrl);
        }
    });

});

/**
 * Resets a input field
 * @param args input fields
 */
function resetFields(...args) {
    args.forEach(argument => argument.value="");
}

/**
 * Checks if any of the arguments are empty
 * @param args any number of strings
 * @returns {boolean} true if one is empty
 */
function checkEmptyField(...args) {
    for (let i = 0; i < args.length; i++)
        if(args[i] === "") return true;
    return false;
}

/**
 * Sets the edit fields and shows the edit modal
 * @param element edit button
 */
function showEditEntry(element) {
    setEditFields(element.parentNode.parentNode.parentNode);
    $('#editModal').modal('show');
}

/**
 * Sets all the fields for the edit modal
 * Url field will be empty if base64
 * @param element edit button
 */
function setEditFields(element) {
    editDate.value = element.children[0].children[1].innerText;
    editId.value = element.children[0].children[0].innerText;
    editText.value = element.children[1].children[0].innerText;
    let url = element.children[1].children[2].children[0].src;
    if (!url.startsWith("data:image")) {
        editUrl.value = url;
    }
}

/* ******* Fetching functions ******* */

/**
 * Fetches all entries and generates them to the HTML doc
 */
function getAll() {
    $.ajax({
        url: BASE_URL + "entries",
        type: 'GET',
        contentType: "application/json",
        success: function(data) {
            updateEntries(data.data);
        },
        fail: (err) => console.log("Couldn't get contacts ", err)
    });
}

/**
 * Creates an entry to the database and fetches the new entries
 * @param date
 * @param text
 * @param img
 */
function addEntry(date, text, img) {
    if(checkEmptyField(date, text, img)) return;
    let dataObject = {
        date: date,
        text: text,
        img: img
    };
    console.log("Adding ", dataObject);
    $.ajax({
        url: BASE_URL + "entry/create",
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify(dataObject),
        success: function(data) {
            getAll();
        },
        fail: (err) => console.log("Couldn't create contact " + dataObject, err)
    });
}

/**
 * Modifies an entry on database and fetches the new list
 * @param id
 * @param date
 * @param text
 * @param img
 */
function editEntry(id, date, text, img) {
    if(checkEmptyField(id, date, text, img)) return;
    let dataObject = {id: id, date: date, text: text, img:img}
    console.log("Editing ", dataObject);
    $.ajax({
        url: BASE_URL + "entry/update",
        type: 'PATCH',
        contentType: "application/json",
        data: JSON.stringify(dataObject),
        success: function(data) {
            getAll();
        },
        fail: (err) => console.log("Couldn't update contact " + dataObject, err)
    });
}

/**
 * Sets entry as inactive and removes it from the document
 * @param element Delete button
 */
function deleteEntry(element) {
    let parent = element.parentNode.parentNode.parentNode
    let id = parent.children[0].children[0].innerText;
    console.log("Deleting ", id);
    console.log(location.protocol + '//' + location.host + "/api/entry/delete/"+id)
    $.ajax({
        url: BASE_URL + "entry/delete/" + id,
        type: 'DELETE',
        contentType: "application/json",
        success: function(data) {
            parent.parentNode.removeChild(parent);
        },
        fail: (err) => console.log("Couldn't delete entry", err)
    });
}

/**
 * Generates multiple DIV entries
 * @param entries entries to generate
 */
function updateEntries(entries) {
    entriesHtml.innerHTML = "";
    entries.forEach(entry => {
       entriesHtml.appendChild(generateEntry(entry));
    });
}

/**
 * Generates one entry div
 * @param entry the entry to generate
 * @returns {HTMLDivElement} HTML object with entry information
 */
function generateEntry(entry) {
    const entryHtml = `
        <div class="entry-header card-header">
            <div class="entry-id"><h2>${entry.id}</h2></div>
            <div class="entry-date"><h2>${entry.date}</h2></div>
            <div class="btn-group">
                <button class="btn btn-sm" onclick="showEditEntry(this);">
                    <span class="oi oi-pencil" title="pencil" aria-hidden="true"></span>
                </button>
                <button class="close" onclick="deleteEntry(this);">x</button>
            </div>
        </div>
        <div class="entry-body card-body">
            <div class="entry-text">${entry.text}</div>
            <br>
            <div class="entry-image">
                <img class="card-img-top" src="${entry.img}" alt="entry-img"/>
            </div>
        </div>
        <div class="clear"></div>`;

    let entryDiv = document.createElement('div');
    entryDiv.setAttribute('class', 'entry card');
    entryDiv.innerHTML = entryHtml;
    return entryDiv;
}
