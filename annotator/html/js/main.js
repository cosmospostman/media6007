let documents;
let tags;
let annotations;
let documentIndex = -1;
let currentDocumentId;

let highlight_text = ["climate change", "emissions", "carbon"];

async function loadData() {
    documents = await $.get('/data/documents.json');
    tags = await $.get('/data/tags.json');
    annotations = await $.get('/data/annotations.json');

    renderTagList(tags); 
    $("#status").text("Loaded " + documents.length + " documents");

    documentIndex = location.hash.replace("#", "");
    loadCurrentDocument();
}

function renderTagList(tags) {
    for (const [section, items] of Object.entries(tags)) {
        $("#annotations-list").append("<li><b>" + section + "</b></li>");
        for (const [code, description] of Object.entries(items)) {
            $("#annotations-list").append(`
                <li><label class="checkbox"><input type="checkbox" value="${code}"> ${description}</label></li>
            `);
        }
      }
}

function saveAndReset() {
    if (currentDocumentId == undefined) {
        return;
    }
    let selectedTags = [];
    $.each($("#annotations input:checked"), function(elem) {
        selectedTags.push($(this).val());
    });
    annotations[currentDocumentId] = selectedTags;
    $.post("/saveAnnotations", annotations);
    $("#annotations").trigger("reset");
}

function next() {
    documentIndex++;
    loadCurrentDocument();
}

function prev() {
    documentIndex--;
    loadCurrentDocument();
}

function loadCurrentDocument() {
    saveAndReset();
    window.location.hash = documentIndex;

    let doc = documents[documentIndex];
    if (!doc) { return; }
    let fullText = doc.fullText;
    highlight_text.forEach(term => 
            fullText = fullText.replace(new RegExp(term, "gi"), (match) => `<mark>${match}</mark>`)
        );

    currentDocumentId = doc.hashId;
    $("#hashId").text(doc.hashId);
    $("#title").text(doc.title);
    $("#section").text(doc.section);
    $("#fulltext").html(fullText);
    $("#status").text(documentIndex + " of " + documents.length);
    loadAnnotations();
}

function loadAnnotations() {
    if (annotations[currentDocumentId]) {
        annotations[currentDocumentId].forEach(tag => 
            $(`#annotations input[value='${tag}']`).prop('checked', true));
    }
}

function resize() {
    $("#article").height(window.innerHeight - 100);
    $("#right-controls").height(window.innerHeight - 100);
}

$(window).resize(resize);
$(document).ready(function() {
    loadData();
    resize();
});