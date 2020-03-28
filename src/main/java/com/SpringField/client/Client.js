const express    = require('express');
const path       = require('path');


// Setting up dependencies
const app = express();

// Home Page
app.get('/', (req, res) => {
    app.use(express.static('html'));
    res.sendFile(path.join(__dirname + "/html/template.html"));
});

// This will make it much more modular for multiple environments
// In a production envrionment it will always try to pull the preset PORT
// determined by the host environment
const port = process.env.PORT || 3001;
app.listen(port, () => console.log(`Listening on port ${port}...`));