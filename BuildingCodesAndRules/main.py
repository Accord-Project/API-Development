import os
import uuid
import json
import httpx
from pathlib import Path
from dotenv import load_dotenv
from fastapi import Body, FastAPI, status
from fastapi.responses import FileResponse, JSONResponse, HTMLResponse
from urllib.parse import urljoin

dotenv_path = Path(".env")
load_dotenv(dotenv_path=dotenv_path)
db_login = os.getenv("db_login")
db_password = os.getenv("db_password")
db_endpoint = os.getenv("db_endpoint")

app = FastAPI()

allowed_repositories = {"tegel", "aec3po"}
default_repository = "tegel"

def run_sparql(url, data, username, password):
    headers = {
        "Accept": "application/sparql-results+json",
        "Content-Type": "application/sparql-query",
    }
    auth = httpx.BasicAuth(username, password)
    response = httpx.post(url, data=data, headers=headers, auth=auth, timeout=30)
    return response.json()

##################### GET #####################

@app.get("/")
async def main():
    return FileResponse("docs/about.html")

@app.get("/api/v1/{repository}/named-graphs")
def repository_named_graphs_v1(repository):
    """
    Takes the list of named graphs in the repository

    :param repository: The name of the repository
    :type repository: str

    :return: The result of the SPARQL query
    :rtype: Response object

    sample call is
    http://127.0.0.1:8000/api/v1/tegel/named-graphs

    """
    if repository not in allowed_repositories:
        return HTMLResponse(
            status_code=status.HTTP_404_NOT_FOUND,
            content="Repository not allowed",
        )

    query = """select distinct ?g where {
        graph ?g {
            ?s ?p ?o .
        }
    } limit 100"""
    return HTMLResponse(
        status_code=status.HTTP_200_OK,
        headers={"Content-Type": "application/json"},
        content=run_sparql(
            urljoin(db_endpoint, f"repositories/{repository}"),
            # https://graphdb.accordproject.eu/graphdb/repositories/tegel
            query,
            db_login,
            db_password,
        ),
    )

@app.get("/api/v1/")
def building_codes_list_v1():
    return HTMLResponse(
        status_code=status.HTTP_200_OK,
        headers={"Content-Type": "application/json"},
        content=json.dumps({
            "name": "string",
            "description": "string",
            "operator": "string",
            "documentList": [
                {
                "shortName": "string",
                "fullName": "string",
                "latestVersion": "string",
                "latestVersionDate": "2019-08-24",
                "versions:": [
                    {
                    "version": "string",
                    "versionDate": "2019-08-24"
                    }
                ],
                "language": "string",
                "jurisdiction": "string"
                }
            ]
            }),
    )

@app.get("/api/v1/{shortName}")
def building_code_latest_version_v1(shortName, purpose:str="combined", ruleFormat:str="summary"):
    """
    Returns the latest version of a Building Code
    PATH PARAMETERS
    shortName
    required
    string
    The short URL friendly name of the Building Code

    QUERY PARAMETERS
    purpose	
    string
    Default: "combined"
    Enum: "execution" "visualisation" "combined"
    Defines the purpose of the returned data, so the content of the response can be tuned accordingly

    ruleFormat	
    string
    Default: "summary"
    Enum: "explicit" "summary"
    Defines the formating of the executable rules within the returned document.
    """
    query = """SELECT (COUNT(DISTINCT ?g) AS ?graphs) 
               WHERE { graph ?g {?s ?p ?o } 
               FILTER (CONTAINS(str(?g), "{shortName}"))}""".replace('{shortName}', shortName)
    
    result=run_sparql(
            urljoin(db_endpoint, f"repositories/{default_repository}"),
            # https://graphdb.accordproject.eu/graphdb/repositories/tegel
            query,
            db_login,
            db_password,
        )
    #print(result)
    if int(result["results"]["bindings"][0]["graphs"]["value"])==0:
        return HTMLResponse(
            status_code=status.HTTP_404_NOT_FOUND,
            content=f"{shortName} not found",
        )
    return HTMLResponse(
        status_code=status.HTTP_200_OK,
        headers={"Content-Type": "application/json"},
        content=json.dumps({
            "@base": "string",
            "@context": [
                {}
            ],
            "$type": "Document",
            "identifier": "string",
            "$id": "string",
            "title": "string",
            "hasPart": [
                {
                "$type": [
                    "DocumentSubdivision"
                ],
                "identifier": "string",
                "$id": "string",
                "hasPart": [
                    {}
                ],
                "hasInlinePart": [
                    {
                    "$type": "CheckStatement",
                    "identifier": "string",
                    "$id": "string",
                    "hasInlinePart": [
                        {}
                    ],
                    "references": "string",
                    "asText": "string",
                    "isOperationalizedBy": {
                        "$type": "DeclarativeCheckMethod",
                        "identifier": "string",
                        "$id": "string",
                        "asText": "string",
                        "hasTarget": "string",
                        "hasValue": "string",
                        "hasUnit": "string",
                        "hasComparator": "CheckMethodComparator-eq"
                    }
                    }
                ],
                "title": "string",
                "relation": "string",
                "references": "string",
                "asText": "string"
                }
            ],
            "modified": "string",
            "issued": "string",
            "coverage": "string",
            "temporal": "string",
            "subject": "string"
            }),
    )

@app.get("/api/v1/{shortName}/ids")
def building_code_latest_version_ids_v1(shortName):
    """
    Returns the IDS for the latest version of a Building Code
    PATH PARAMETERS
    shortName
    required
    string
    The short URL friendly name of the Building Code

    Responses
    200
    Returns the IDS for the latest version of the building code identified by shortName. Response is IDS compliant XML
    404
    If the building code does not exist    
    """
    query = """SELECT (COUNT(DISTINCT ?g) AS ?graphs) 
               WHERE { graph ?g {?s ?p ?o } 
               FILTER (CONTAINS(str(?g), "{shortName}"))}""".replace('{shortName}', shortName)
    
    result=run_sparql(
            urljoin(db_endpoint, f"repositories/{default_repository}"),
            # https://graphdb.accordproject.eu/graphdb/repositories/tegel
            query,
            db_login,
            db_password,
        )
    #print(result)
    if int(result["results"]["bindings"][0]["graphs"]["value"])==0:
        return HTMLResponse(
            status_code=status.HTTP_404_NOT_FOUND,
            content=f"{shortName} not found",
        )
    return HTMLResponse(
        status_code=status.HTTP_200_OK,
        headers={"Content-Type": "application/json"},
# will be updated to return IDS nothing else
        content=json.dumps({
            "@base": "string",
            "@context": [
                {}
            ],
            "$type": "Document",
            "identifier": "string",
            "$id": "string",
            "title": "string",
            "hasPart": [
                {
                "$type": [
                    "DocumentSubdivision"
                ],
                "identifier": "string",
                "$id": "string",
                "hasPart": [
                    {}
                ],
                "hasInlinePart": [
                    {
                    "$type": "CheckStatement",
                    "identifier": "string",
                    "$id": "string",
                    "hasInlinePart": [
                        {}
                    ],
                    "references": "string",
                    "asText": "string",
                    "isOperationalizedBy": {
                        "$type": "DeclarativeCheckMethod",
                        "identifier": "string",
                        "$id": "string",
                        "asText": "string",
                        "hasTarget": "string",
                        "hasValue": "string",
                        "hasUnit": "string",
                        "hasComparator": "CheckMethodComparator-eq"
                    }
                    }
                ],
                "title": "string",
                "relation": "string",
                "references": "string",
                "asText": "string"
                }
            ],
            "modified": "string",
            "issued": "string",
            "coverage": "string",
            "temporal": "string",
            "subject": "string"
            }),
    )

@app.get("/api/v1/{shortName}/{version}")
def building_code_version_v1(shortName, version):
    """
    Returns a specified version of a building code

    PATH PARAMETERS
    shortName
    required
    string
    The short URL friendly name of the Building Code

    version
    required
    string
    The version name of the document to retrieve, or the latest version if absent

    QUERY PARAMETERS
    purpose	
    string
    Default: "combined"
    Enum: "execution" "visualisation" "combined"
    Defines the purpose of the returned data, so the content of the response can be tuned accordingly

    ruleFormat	
    string
    Default: "summary"
    Enum: "explicit" "summary"
    Defines the formating of the executable rules within the returned document.
    """
    query = """SELECT (COUNT(DISTINCT ?g) AS ?graphs) 
               WHERE { graph ?g {?s ?p ?o } 
               FILTER (CONTAINS(str(?g), "{shortName}"))}""".replace('{shortName}', shortName)
    
    result=run_sparql(
            urljoin(db_endpoint, f"repositories/{default_repository}"),
            # https://graphdb.accordproject.eu/graphdb/repositories/tegel
            query,
            db_login,
            db_password,
        )
    #print(result)
    if int(result["results"]["bindings"][0]["graphs"]["value"])==0:
        return HTMLResponse(
            status_code=status.HTTP_404_NOT_FOUND,
            content=f"{shortName} not found",
        )
    return HTMLResponse(
        status_code=status.HTTP_200_OK,
        headers={"Content-Type": "application/json"},
        content=json.dumps({
    "@base": "string",
    "@context": [
        {}
    ],
    "$type": "Document",
    "identifier": "string",
    "$id": "string",
    "title": "string",
    "hasPart": [
        {
        "$type": [
            "DocumentSubdivision"
        ],
        "identifier": "string",
        "$id": "string",
        "hasPart": [
            {}
        ],
        "hasInlinePart": [
            {
            "$type": "CheckStatement",
            "identifier": "string",
            "$id": "string",
            "hasInlinePart": [
                {}
            ],
            "references": "string",
            "asText": "string",
            "isOperationalizedBy": {
                "$type": "DeclarativeCheckMethod",
                "identifier": "string",
                "$id": "string",
                "asText": "string",
                "hasTarget": "string",
                "hasValue": "string",
                "hasUnit": "string",
                "hasComparator": "CheckMethodComparator-eq"
            }
            }
        ],
        "title": "string",
        "relation": "string",
        "references": "string",
        "asText": "string"
        }
    ],
    "modified": "string",
    "issued": "string",
    "coverage": "string",
    "temporal": "string",
    "subject": "string"
            }),
    )

@app.get("/api/v1/{shortName}/{version}/ids")
def building_code_version_v1(shortName, version):
    """
    Returns the IDS for the given version of a Building Code

    PATH PARAMETERS
    shortName
    required
    string
    The short URL friendly name of the Building Code

    version
    required
    string
    The version name of the document to retrieve, or the latest version if absent
    """

#query will be updated to check also whether the version exists
    query = """SELECT (COUNT(DISTINCT ?g) AS ?graphs) 
               WHERE { graph ?g {?s ?p ?o } 
               FILTER (CONTAINS(str(?g), "{shortName}"))}""".replace('{shortName}', shortName)
    
    result=run_sparql(
            urljoin(db_endpoint, f"repositories/{default_repository}"),
            # https://graphdb.accordproject.eu/graphdb/repositories/tegel
            query,
            db_login,
            db_password,
        )
    #print(result)
    if int(result["results"]["bindings"][0]["graphs"]["value"])==0:
        return HTMLResponse(
            status_code=status.HTTP_404_NOT_FOUND,
            content=f"{shortName} not found",
        )
    return HTMLResponse(
        status_code=status.HTTP_200_OK,
        headers={"Content-Type": "application/json"},
        content=json.dumps({
    "@base": "string",
    "@context": [
        {}
    ],
    "$type": "Document",
    "identifier": "string",
    "$id": "string",
    "title": "string",
    "hasPart": [
        {
        "$type": [
            "DocumentSubdivision"
        ],
        "identifier": "string",
        "$id": "string",
        "hasPart": [
            {}
        ],
        "hasInlinePart": [
            {
            "$type": "CheckStatement",
            "identifier": "string",
            "$id": "string",
            "hasInlinePart": [
                {}
            ],
            "references": "string",
            "asText": "string",
            "isOperationalizedBy": {
                "$type": "DeclarativeCheckMethod",
                "identifier": "string",
                "$id": "string",
                "asText": "string",
                "hasTarget": "string",
                "hasValue": "string",
                "hasUnit": "string",
                "hasComparator": "CheckMethodComparator-eq"
            }
            }
        ],
        "title": "string",
        "relation": "string",
        "references": "string",
        "asText": "string"
        }
    ],
    "modified": "string",
    "issued": "string",
    "coverage": "string",
    "temporal": "string",
    "subject": "string"
            }),
    )

@app.get("/api/v1/{shortName}/{version}/{documentReference}")
def building_code_version_section_v1(shortName, version, documentReference, purpose:str="combined", ruleFormat:str="summary"):
    """
    Returns a specific section/paragraph of a building code

    PATH PARAMETERS
    shortName
    required
    string
    The short URL friendly title of the Building Code

    version
    required
    string
    The version name of the building code to retrieve, or the latest version if absent

    documentReference
    required
    string
    The reference to the specific section/paragraph to retrieve. Should be a list of section or paragraph numbered seperated by the / character. An example for this is /1/2/a for Section 1, Paragraph 2 sub paragraph a

    QUERY PARAMETERS
    purpose	
    string
    Default: "combined"
    Enum: "execution" "visualisation" "combined"
    Defines the purpose of the returned data, so the content of the response can be tuned accordingly

    ruleFormat	
    string
    Default: "summary"
    Enum: "explicit" "summary"
    Defines the formating of the executable rules within the returned document.
    """

#query will be updated to check also whether the version exists
    query = """SELECT (COUNT(DISTINCT ?g) AS ?graphs) 
               WHERE { graph ?g {?s ?p ?o } 
               FILTER (CONTAINS(str(?g), "{shortName}"))}""".replace('{shortName}', shortName)
    
    result=run_sparql(
            urljoin(db_endpoint, f"repositories/{default_repository}"),
            # https://graphdb.accordproject.eu/graphdb/repositories/tegel
            query,
            db_login,
            db_password,
        )
    #print(result)
    if int(result["results"]["bindings"][0]["graphs"]["value"])==0:
        return HTMLResponse(
            status_code=status.HTTP_404_NOT_FOUND,
            content=f"{shortName} not found",
        )
    return HTMLResponse(
        status_code=status.HTTP_200_OK,
        headers={"Content-Type": "application/json"},
        content=json.dumps({
    "@base": "string",
    "@context": [
        {}
    ],
    "$type": "Document",
    "identifier": "string",
    "$id": "string",
    "title": "string",
    "hasPart": [
        {
        "$type": [
            "DocumentSubdivision"
        ],
        "identifier": "string",
        "$id": "string",
        "hasPart": [
            {}
        ],
        "hasInlinePart": [
            {
            "$type": "CheckStatement",
            "identifier": "string",
            "$id": "string",
            "hasInlinePart": [
                {}
            ],
            "references": "string",
            "asText": "string",
            "isOperationalizedBy": {
                "$type": "DeclarativeCheckMethod",
                "identifier": "string",
                "$id": "string",
                "asText": "string",
                "hasTarget": "string",
                "hasValue": "string",
                "hasUnit": "string",
                "hasComparator": "CheckMethodComparator-eq"
            }
            }
        ],
        "title": "string",
        "relation": "string",
        "references": "string",
        "asText": "string"
        }
    ],
    "modified": "string",
    "issued": "string",
    "coverage": "string",
    "temporal": "string",
    "subject": "string"
            }),
    )

##################### POST #####################

##################### PUT #####################