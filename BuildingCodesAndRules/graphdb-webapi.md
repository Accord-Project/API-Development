# API calls to GraphDB

This is the list of sample API calls which is based on WebAPI provided by Ontotext GraphDB. 
The full description of WebAPI abilities is given [here](https://graphdb.accordproject.eu/graphdb/webapi). 

## Endpoints

GraphDB: https://graphdb.accordproject.eu/graphdb

SPARQL:  https://graphdb.accordproject.eu/graphdb/sparql

Workbench: https://graphdb.accordproject.eu/platform-workbench

GraphQL: https://graphdb.accordproject.eu/platform-workbench/graphql

## Credentials

See the credentials in the mailings

## Default path

*Caution*: 

All calls should go to **https://graphdb.accordproject.eu/graphdb/repositories**. 

Default GraphDB WebAPI generated path is https://graphdb.accordproject.eu/repositories, so when try from the UI the API calls won't work.

# /repositories

## GET /repositories

Gets list of available repositories

```
curl -u rft -X GET --header 'Accept: application/json' \
'https://graphdb.accordproject.eu/graphdb/repositories'
```
## DELETE /repositories/{repositoryID}
should not be used and will be forbidden

## DELETE /repositories/{repositoryID}/statements
should not be used and will be forbidden

## GET /repositories/{repositoryID}/statements

Gets list of statements in Turtle (N-Triples also allowed).
Use it to download current (active/latest) version of the repository. 
```
curl -u rft -X GET --header 'Accept: text/turtle' \
'https://graphdb.accordproject.eu/graphdb/repositories/aec3po/statements'
```
If in the repository there are several graphs, it will return statements from all graphs, including the default (unnamed) graph.
To select a particular graph use `context` parameter

and assuming the context = <https://graphdb.accordproject.eu/resource/aec3po/Spain/v2>
```
curl -u rft -X GET --header 'Accept: text/turtle' \
'https://graphdb.accordproject.eu/graphdb/repositories/aec3po/statements?context=%3Chttps%3A%2F%2Fgraphdb.accordproject.eu/resource/aec3po/Spain/v2%3E'
```
## GET /repositories/{repositoryID}/statements in JSONLD
To be able to download JSONLD, use real link where the context is exposed.
So to download a regulation use the following link `https://ci.mines-stetienne.fr/aec3po/aec3po.jsonld` where aec3po is actually placed.

```
curl -u rft -X \
GET https://graphdb.accordproject.eu/graphdb/repositories/aec3po/statements?context=%3Chttps://graphdb.accordproject.eu/resource/aec3po/FI-CO2/v1%3E \
--header 'Accept: application/ld+json;profile=http://www.w3.org/ns/json-ld#compacted' \
--header 'Link: <https://ci.mines-stetienne.fr/aec3po/aec3po.jsonld>; \
rel="http://www.w3.org/ns/json-ld#context"'
```
## PUT /repositories/{repositoryID}/statements

Updates data in the repository, replacing any existing data with the supplied data.

Use it for uploading any updates in the source, BUT SHOULD BE USED WITH the creation of a new named graph {repositoryID}/{version}, because otherwise it will overwrite the current version, which is not allowed 

See https://github.com/Accord-Project/API-Development/blob/main/BuildingCodesAndRules/BuildingCodesAndRules.yaml: It is important to note when reading this API that any given version of a building code is immutable and once a specific version is published it cannot be changed. To update a document a new version must be created.

So in the API call it will be:
```
curl -u rft -X PUT --header 'Content-Type: text/turtle' \
  --data-binary @new_version_of_12-62a.ttl \
  'https://graphdb.accordproject.eu/graphdb/repositories/tegel/statements?context=%3Chttps%3A%2F%2Fgraphdb.accordproject.eu/resource/tegel/12-62a/v2%3E'

curl -u rft -X PUT --header 'Content-Type: text/turtle' \
  --data-binary @new_version_of_building_code_for_spain.ttl \
  'https://graphdb.accordproject.eu/graphdb/repositories/aec3po/statements?context=%3Chttps%3A%2F%2Fgraphdb.accordproject.eu/resource/aec3po/Spain/v2%3E'
```
here `context` = name of a new named graph reflecting the version, which is <https://graphdb.accordproject.eu/resource/tegel/12-62a/v2>

## GET /repositories/{repositoryID}/size

The repository size (defined as the number of statements it contains). Do not use to learn the size of a particular named graph
```
curl -u rft -X GET --header 'Accept: text/html' \
'https://graphdb.accordproject.eu/graphdb/repositories/aec3po/size'
curl -u rft -X GET --header 'Accept: text/html' \
'https://graphdb.accordproject.eu/graphdb/repositories/tegel/12-62a/size'
```
# /contexts

## GET /repositories/{repositoryID}/contexts

    Gets a list of resources that are used as context identifiers
```
curl -u rft -X GET --header 'Accept: application/sparql-results+json' 'https://graphdb.accordproject.eu/graphdb/repositories/aec3po/contexts'
```
