// TODO: onload function should retrieve the data needed to populate the UI

// fetches the url for http://localhost:8080/spamDetector-1.0/api/spam and returns the file, spamProbability and actualClass in the table
(function ()
  {
    fetch("http://localhost:8080/spamDetector-1.0/api/spam",
      {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
        },
      })

      .then(response => response.json())
      .then(data => {
        const table = document.getElementById("tbody");
        for (let i = 0; i < data.length; i++) {
          const row = '<tr><td>' + data[i].file + '</td><td>' + data[i].spamProbability + '</td><td>' + data[i].actualClass + '</td></tr>';
          table.innerHTML += row;
        }
      })
      /** fetching some data **/
      .catch((err) => {console.log("something went wrong: " + err);});
  }
)();


// fetches the url for http://localhost:8080/spamDetector-1.0/api/spam/accuracy and returns the accuracy
(function ()
  {
    fetch("http://localhost:8080/spamDetector-1.0/api/spam/accuracy",
      {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
        },
      })

      .then(response => response.json())
      .then(data => {
        const accuracyInput = document.getElementById("accuracy");
        accuracyInput.value = data.accuracy;
      })
      /** fetching some data **/
      .catch((err) => {console.log("something went wrong: " + err);});
  }
)();


// fetches the url for http://localhost:8080/spamDetector-1.0/api/spam/precision and returns precision
(function ()
  {
    fetch("http://localhost:8080/spamDetector-1.0/api/spam/precision",
      {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
        },
      })

      .then(response => response.json())
      .then(data => {
        const precisionInput = document.getElementById("precision");
        precisionInput.value = data.precision;
      })
    /** fetching some data **/
    .catch((err) => {console.log("something went wrong: " + err);});
  }
)();

// fetches the url for http://localhost:8080/spamDetector-1.0/api/spam/info and returns the information about total amount of file, and the number of ham/spam files
(function ()
  {
    fetch("http://localhost:8080/spamDetector-1.0/api/spam/info",
      {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
        },
      })

      .then(response => response.json())
      .then(data => {
        const totalInput = document.getElementById("total");
        totalInput.value = data.total;
        const hamInput = document.getElementById("ham");
        hamInput.value = data.ham;
        const spamInput = document.getElementById("spam");
        spamInput.value = data.spam;
      })
      /** fetching some data **/
      .catch((err) => {console.log("something went wrong: " + err);});
  }
)();
