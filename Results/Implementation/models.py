# generated by fastapi-codegen:
#   filename:  ../Results.yaml
#   timestamp: 2024-05-23T13:10:53+00:00

from __future__ import annotations

from enum import Enum
from typing import Any, Dict, List, Optional
from uuid import UUID

from pydantic import BaseModel, Field


class Answer(Enum):
    True_ = True
    False_ = False
    unknown = 'unknown'


class Result(BaseModel):
    documentReference: Optional[str] = Field(
        None,
        description='The document reference for the compliance check being executed',
    )
    entityId: str = Field(
        ..., description='The entity Id to which this response is relevant'
    )
    answer: Answer = Field(..., description='The response')
    missValue: Optional[str] = Field(
        None,
        description='This is an indication, if the response is false, how far out of tolerance the response is.',
    )
    supportingFileData: Optional[str] = Field(
        None, description='BASE64 Encoded data that supports/evidences this answer'
    )


class ResponseJSON(BaseModel):
    supportingFileContentType: Optional[str] = Field(
        None,
        description='The MIME Content Type of the supporting file data (if present)',
    )
    results: List[Result] = Field(
        ...,
        description='A list of responses, one item for every object that was requested to be checked upon Check creation',
    )


class Comparator(Enum):
    field__ = '!='
    field___1 = '=='
    field___2 = '>='
    field___3 = '<='
    field_ = '<'
    field__1 = '>'


class ModelDetails(BaseModel):
    URL: str = Field(..., description='The URL to the model file.')
    ContentType: Optional[str] = Field(None, description='The content type of the model file.')


class CheckJobID(BaseModel):
    CheckJobID: UUID = Field(..., description='The GUID.')


class CheckStatus(BaseModel):
    complete: bool


class CapabilitiesIndex(BaseModel):
    complianceCheckName: Optional[str] = Field(
        None,
        description='The name of a compliance check that this microservice is capable of performing',
    )
    entityTypes: Optional[List[str]] = Field(
        None,
        description='A list of entity types i.e. IfcDoor to which the described check can be applied',
    )
    documentReferences: Optional[List[str]] = Field(
        None,
        description='A list of URLs to Building Codes to which the described check can be applied',
    )


class CheckCreate(BaseModel):
    buildingCodeReferences: List[str] = Field(
        ...,
        description='The URLs to one or more building codes hosted on an endpoint compatible with the Building Codes and Regulations API',
    )
    models: List[ModelDetails] = Field(
        ..., description='The list of models for this check'
    )
    additionalData: Optional[Dict[str, Any]] = Field(
        None,
        description='Any additional data (specified as valid JSON) required by the microservice for execution',
    )
    entityIds: Optional[List[str]] = Field(
        None,
        description='The set of entity Ids to restrict those that the given compliance check is performed on. If blank it is assumed all entities are checked.',
    )
    webhookUrl: Optional[str] = Field(
        None,
        description='A Webhook URL that the microservice can call to signify this check is complete.',
    )


class CheckCreateSpecific(BaseModel):
    complianceCheckName: Optional[str] = Field(
        None,
        description='The name of the compliance check',
    )
    entityIds: Optional[List[str]] = Field(
        None,
        description='The set of entity Ids to perform the given compliance check on. If blank it is assumed all entities are checked.',
    )
    comparator: Optional[Comparator] = Field(
        None,
        description='The comparator used to determine if a specific individual check has passed',
    )
    desiredValue: Optional[str] = Field(
        None, description='The desired value for the result of the compliance check.'
    )
    unit: Optional[str] = Field(
        None,
        description='The unit of desiredValue. If absent the microservice will assume either unitless or a standard SI Unit.',
    )
    buildingCodeReference: str = Field(
        ...,
        description='The URL to a building code hosted on an endpoint compatible with the Building Codes and Regulations API',
    )
    additionalData: Optional[Dict[str, Any]] = Field(
        None,
        description='Any additional data (specified as valid JSON) required by the microservice for execution',
    )
    models: List[ModelDetails] = Field(
        ..., description='The list of models for this check'
    )
    webhookUrl: Optional[str] = Field(
        None,
        description='A Webhook URL that the microservice can call to signify this check is complete.',
    )


class Identity(BaseModel):
    name: Optional[str] = Field(
        None, description='The name of this compliance checking microservice'
    )
    description: Optional[str] = Field(
        None, description='A description of this compliance checking microservice'
    )
    operator: Optional[str] = Field(
        None,
        description='The name of the organisation operating this compliance checking microservice',
    )
    capabilities: Optional[List[CapabilitiesIndex]] = Field(
        None,
        description='A list of the capabilities of the compliance checking microservice',
    )
