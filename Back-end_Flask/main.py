# Created by Meng-Han Wu on 6/01/2022
# Copyright © 2022 Meng-Han Wu. All rights reserved.
# This content is protected and may not be shared, uploaded, or distributed.

import requests
from flask import Flask, jsonify, request

# Flask application
app = Flask(__name__)
app.config.from_mapping(
    SECRET_KEY='menghanwu-secret'
)

# Global client ID & secret
client_id = '3239baebe2f7e0270239'
client_secret = '11281dfc6c368349bfb132ca99b96f16'


@app.route("/ajaxAuthSearch", methods=['GET'])
def ajaxAuthSearch():

    # Get a authentication token (POST w/ client id & secret)
    url = "https://api.artsy.net/api/tokens/xapp_token"
    postData = {'client_id': client_id, 'client_secret': client_secret}
    r = requests.post(url, data=postData)
    xappToken = r.json()['token']
    
    # Retrieve given query from user input by 'GET' e.g. "picasso"
    # /ajaxAuthSearch?client_query=picasso
    # *filter...
    client_query = request.args.get("client_query", "", type=str)
    print(type(client_query), len(client_query), client_query)
    
    # Make requests (GET w/ token-header)
    # e.g. https://api.artsy.net/api/search?q=picasso&size=10
    url = "https://api.artsy.net/api/search"
    headers = {'Accept': 'application/vnd.artsy-v2+json', 'X-Xapp-Token': xappToken}
    payload = {'q': client_query, 'size': 10}
    r = requests.get(url, headers=headers, params=payload)
    print(r.json()["_embedded"]["results"])
    return jsonify(key=r.json()["_embedded"]["results"])


@app.route("/ajaxArtistDetail", methods=['GET'])
def ajaxArtistDetail():

    # Get a authentication token (POST w/ client id & secret)
    url = "https://api.artsy.net/api/tokens/xapp_token"
    postData = {'client_id': client_id, 'client_secret': client_secret}
    r = requests.post(url, data=postData)
    xappToken = r.json()['token']

    # Retrieve target artist's ID by 'GET' e.g. "4d8b928b4eb68a1b2c0001f2"
    # /ajaxArtistDetail?artist_ID=4d8b928b4eb68a1b2c0001f2
    # *filter...
    artist_ID = request.args.get("artist_ID", "", type=str)
    print(type(artist_ID), len(artist_ID), artist_ID)

    # Make requests (GET w/ token-header)
    # e.g. https://api.artsy.net/api/artists/4d8b928b4eb68a1b2c0001f2
    url = "https://api.artsy.net/api/artists"
    headers = {'Accept': 'application/vnd.artsy-v2+json', 'X-Xapp-Token': xappToken}
    r = requests.get(url+"/"+artist_ID, headers=headers)
    print(r.json())
    return jsonify(key=r.json())


@app.route("/")
def root():

    # Static files in /static directory
    return app.send_static_file('index.html')


if __name__ == '__main__':
    
    # This is used when running locally only.
    app.run(host='127.0.0.1', port=8080, debug=True)
