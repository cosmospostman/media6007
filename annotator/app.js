const fs = require('fs');
const express = require('express');
const app = express();
const port = 8000
const dataDir = '/Users/mlj/media-analysis'

app.use(express.static('html'))

var bodyParser = require("body-parser");
app.use(bodyParser.urlencoded())

app.post('/saveAnnotations', (req, res) => {
  fs.writeFileSync('html/data/annotations.json', JSON.stringify(req.body));
  res.sendStatus(200);
})

app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`);
})