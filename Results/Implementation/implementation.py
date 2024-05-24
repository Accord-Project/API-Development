from models import (
    Identity,
    Result,
    CheckCreate,
    ResponseJSON,
    CheckCreateSpecific,
    Answer
)

import sys

def GetIdentity():
    capabilities = [] # later we may populate this variable to document the capabilities of each microservice
    identity = Identity(name = "Test Result Service Implementation", description = "Test Result Service Implementation", operator = "Enter your Pilot Name here", capabilities=capabilities)
    return identity


def ExecuteComplianceCheck(checkData): # input is in the format of the CheckCreate class
    #This function will be started in a thread of its own so should perform the compliance check and exit returning results in the defined format.
    sys.stdout.write("Starting Compliance Check")
    results = []
    #you may create multiple results here
    result = Result (documentReference = "Document Reference", entityId= "IFC Entity Id", answer = Answer(True), supportingFileData="")
    results.append(result)
    supportingFileContentType = "" #If you are returning file content you should set the content type here
    return ResponseJSON(supportingFileContentType = supportingFileContentType, results = results)

def ExecuteComplianceCheckSpecific(checkData): # input is in the format of the CheckCreateSpecific class
    #This function will be started in a thread of its own so should perform the compliance check and exit returning results in the defined format.
    sys.stdout.write("Starting Compliance Check")
    results = []
    #you may create multiple results here
    result = Result (documentReference = "Document Reference", entityId= "IFC Entity Id", answer = Answer(True), supportingFileData="")
    results.append(result)
    supportingFileContentType = "" #If you are returning file content you should set the content type here
    return ResponseJSON(supportingFileContentType = supportingFileContentType, results = results)