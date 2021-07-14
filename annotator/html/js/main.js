let documents;
let tags;
let annotations;
let documentIndex = -1;

let highlight_text = "climate change";

async function loadData() {
    documents = await $.get('/data/documents.json');
    tags = await $.get('/data/tags.json');
    annotations = await $.get('/data/annotations.json');

    $("#status").text("Loaded " + documents.length + " documents");
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
    let doc = documents[documentIndex];
    let fullText = doc.fullText.replace(new RegExp(highlight_text, "gi"), (match) => `<mark>${match}</mark>`);

    $("#title").text(doc.title);
    $("#fulltext").html(fullText);
    $("#status").text(documentIndex + " of " + documents.length);
}


function resize() {
    $("#article").height(window.innerHeight - 100);
}

$(window).resize(resize);
$(document).ready(resize);
loadData();