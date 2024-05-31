const express = require('express');
const app = express();
const bodyParser = require('body-parser');

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use('/', require('./routes/routes'))





app.listen(3000, () => {
    console.log('Server is running on port 3000')
})