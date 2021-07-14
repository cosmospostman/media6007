const fs = require('fs');
const express = require('express');
const app = express();
const port = 8000
const dataDir = '/Users/mlj/media-analysis'


app.use(express.static('html'))

app.get('/saveAnnotations', (req, res) => {
  //TODO: write annotations
})


app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`);
})