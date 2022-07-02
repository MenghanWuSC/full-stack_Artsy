// Created by Meng-Han Wu on x/xx/2022.
// Copyright © 2022 Meng-Han Wu. All rights reserved.
// This content is protected and may not be shared, uploaded, or distributed.

'use strict';

// [START app]
const express = require('express');
const app = express();
// Global variables
const axios = require('axios');
const client_id = '3239baebe2f7e0270239';
const client_secret = '11281dfc6c368349bfb132ca99b96f16';
var xappToken = "";   // Once updated by #1 search requests, the value should be kept...
// Listen to the App Engine-specified port, or 8080 otherwise
const PORT = parseInt(process.env.PORT) || 8080;


app.use(
  express.urlencoded({
    extended: true,
  })
);
app.use(express.json());
app.use(express.static(process.cwd()+"/dist/artsy/"));


// Listening: search requests 'GET' *async
app.get('/api/search', async (req, res) => {
  // Local variables
  const artist_query = req.query.query;
  let url = "";
  let artists = {};
  // *Assume each round that App starts by searching => initialize Artsy Token
  // 1. Get a authentication token (POST w/ client id & secret)
  url = "https://api.artsy.net/api/tokens/xapp_token";
  xappToken = await axios({
    method: 'post',
    url: url,
    data: {
      'client_id': client_id,
      'client_secret': client_secret
    }
  })
  .then(function (response) {
    console.log("Artsy Token:", response.status);
    return response.data.token;
  });
  // 2. Make requests (GET w/ token-header)
  url = "https://api.artsy.net/api/search";
  console.log("query=", artist_query);
  artists = await axios({
    method: 'get',
    url: url,
    headers: {'Accept': 'application/vnd.artsy-v2+json', 'X-Xapp-Token': xappToken},
    params: {'q': artist_query, 'size': 10}
  })
  .then(function (response) {
    console.log("Artists:", response.status);
    return response.data._embedded.results;
  });
  // Return the result JSON
  console.log(artists);
  res.json(artists);
});


// Listening: detail requests 'GET' *async
app.get('/api/detail', async (req, res) => {
  // Local variables
  const artist_id = req.query.id;
  const url = "https://api.artsy.net/api/artists/" + artist_id;
  let details = {};
  // Make requests (GET w/ token-header)
  console.log("url=", url);
  details = await axios({
    method: 'get',
    url: url,
    headers: {'Accept': 'application/vnd.artsy-v2+json', 'X-Xapp-Token': xappToken}
  })
  .then(function (response) {
    console.log("Details:", response.status);
    return response.data;
  });
  // Return the result JSON
  console.log(details);
  res.json(details);
});


// Listening: artwork requests 'GET' *async
app.get('/api/artwork', async (req, res) => {
  // Local variables
  const artist_id = req.query.id;
  const url = "https://api.artsy.net/api/artworks";
  let artworks = {};
  // Make requests (GET w/ token-header)
  console.log("id=", artist_id);
  artworks = await axios({
    method: 'get',
    url: url,
    headers: {'Accept': 'application/vnd.artsy-v2+json', 'X-Xapp-Token': xappToken},
    params: {'artist_id': artist_id, 'size': 10}
  })
  .then(function (response) {
    console.log("Artworks:", response.status);
    return response.data._embedded.artworks;
  });
  // Return the result JSON
  console.log(artworks);
  res.json(artworks);
});


// Listening: gene requests 'GET' *async
app.get('/api/gene', async (req, res) => {
  // Local variables
  const artwork_id = req.query.id;
  const url = "https://api.artsy.net/api/genes";
  let genes = {};
  // Make requests (GET w/ token-header)
  console.log("id=", artwork_id);
  genes = await axios({
    method: 'get',
    url: url,
    headers: {'Accept': 'application/vnd.artsy-v2+json', 'X-Xapp-Token': xappToken},
    params: {'artwork_id': artwork_id}
  })
  .then(function (response) {
    console.log("Genes:", response.status);
    return response.data._embedded.genes;
  });
  // Return the result JSON
  console.log(genes);
  res.json(genes);
});


/*
app.post('/api/user', (req, res) => {
  const user = req.body.user;
  users.push(user);
  res.json("user addedd");
  console.log(req.body);
});
*/


app.get('/', (req, res) => {
  // Built [Angular] pages
  res.sendFile(process.cwd()+"/dist/artsy/index.html")
});


app.listen(PORT, () => {
  console.log(`Server listening on port ${PORT}...`);
});
// [END app]

module.exports = app;
